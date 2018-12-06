package org.seckill.service;

import org.seckill.dao.cache.RedisDao;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 **/
@Service
public class ProductSeckillService {

    @Resource
    private RedisDao redisDao;

    private static Map<String, Object> products;
    private static Map<String, Object> orders;
    private static Map<String, Object> stocks;

    static {
        products = new HashMap<>();
        orders = new HashMap<>();
        stocks = new HashMap<>();
        products.put("123456", 10000);
        stocks.put("123456", 10000);
    }

    public String query(String seckillId) throws Exception{

        return "商品总数："+products.get(seckillId)+"   剩余数量："+stocks.get(seckillId)
                +"   订单成功数："+orders.size();
    }

    public void order(String seckillId) throws Exception{
        long time = System.currentTimeMillis() + 1000;
        if(!redisDao.lock(seckillId, String.valueOf(time))){
            throw new Exception("订购失败");
        }

        int stockNum = (int) stocks.get(seckillId);
        if(stockNum==0){
            throw new Exception("活动结束！");
        }else{//下单
            orders.put(String.valueOf(System.currentTimeMillis()), seckillId);
            stockNum = stockNum - 1;
            try{
                Thread.sleep(100);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            stocks.put(seckillId, stockNum);
        }

        redisDao.unlock(seckillId, String.valueOf(time));
    }
}
