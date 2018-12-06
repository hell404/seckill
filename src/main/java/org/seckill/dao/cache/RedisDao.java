package org.seckill.dao.cache;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;
    //提供protostuff需序列化对象的类的字节码描述
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }
    // 从redis中获取对象
    public Seckill getSeckill(long seckillId){
        //reids操作逻辑
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                String key = "seckill:"+seckillId;
                //redis,jedis并未实现内部序列化操作
                //采用自定义序列化机制,protostuff
                //获取Seckill对象对应的字节数组
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null){
                    Seckill seckill = schema.newMessage();//空对象
                    //seckill反序列
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    //将对象放入redis缓存
    public String putSeckill(Seckill seckill){
        try{
            Jedis jedis = jedisPool.getResource();
            try{
                //构造key字符串
                String key = "seckill:"+seckill.getSeckillId();
                //将对象转换成对应的字节数组（序列化）
                // LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE)为缓存器，
                // 当序列化对象过大时进行缓存
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //存活时间
                int timeout = 60 * 60;//1小时，单位为秒
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    //加锁
    public boolean lock(String key, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            if(jedis.setnx(key, value)!=0){//可以将数据插入redis数据库
                return true;
            }
            String currentValue = jedis.get(key);//获取redis中key对应的当前的值
            if(!StringUtils.isEmpty(currentValue) &&
                    Long.parseLong(currentValue) < System.currentTimeMillis()){//超时
                String oldValue = jedis.getSet(key, value);//getset是单线程执行
                if(!StringUtils.isEmpty(oldValue) && oldValue.equals(currentValue)){//只有一个线程可以进入此方法
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }finally {
            jedis.close();
        }
    }
    //解锁
    public void unlock(String key, String value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String oldValue = jedis.get(key);
            if(!StringUtils.isEmpty(oldValue) && oldValue.equals(value)){
                jedis.del(key);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            jedis.close();
        }
    }
}
