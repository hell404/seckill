package org.seckill.dao.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.entity.Seckill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class RedisDaoTest {
    private long id = 1000L;//可省略L，自动类型转化，若值超过int型值范围，则必须使用L

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void testSeckill() {
        //get and put
        Seckill seckill = redisDao.getSeckill(id);
        System.out.println("redisDao = " + seckill);
        if(seckill == null){
            seckill = seckillDao.queryById(id);
            System.out.println("seckillDao = " + seckill);
            if(seckill != null){
                String result = redisDao.putSeckill(seckill);
                System.out.println("result = " + result);
                seckill = redisDao.getSeckill(id);
                System.out.println("redisDao2 = " + seckill);
            }
        }
    }


}