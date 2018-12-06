package org.seckill.entity;

import lombok.Data;

import java.util.Date;


/**
 * 秒杀结果对象
 */
@Data
public class SuccessKilled {
    //商品Id
    private long seckillId;
    //用户手机号
    private long userPhone;
    //秒杀状态
    private short state;
    //创建时间
    private Date createTime;
    //一对一
    private Seckill seckill;
}
