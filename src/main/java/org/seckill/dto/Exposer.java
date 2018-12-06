package org.seckill.dto;

import lombok.Data;

/**
 * 暴露秒杀地址DTO
 */
@Data
public class Exposer {
    //是否开启秒杀
    private boolean exposed;
    //加密措施
    private String md5;
    //商品id
    private long seckillId;
    //系统当前时间
    private long now;
    //秒杀开始时间
    private long start;
    //秒杀结束时间
    private long end;

    //开启秒杀的构造函数
    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }
    //未开启秒杀的构造函数
    public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }
    //未找到秒杀商品的构造函数
    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }
}
