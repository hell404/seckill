package org.seckill.entity;

import lombok.Data;

import java.util.Date;

/**
 * 秒杀商品
 */
@Data
public class Seckill {
    //秒杀商品Id
    private long seckillId;
    //秒杀商品名称
    private String name;
    //秒杀商品库存
    private int number;
    //秒杀开始时间
    private Date startTime;
    //秒杀结束时间
    private Date endTime;
    //创建时间
    private Date createTime;
}
