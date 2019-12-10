package org.seckill.service;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

public interface SeckillService {
    /**
     * 查询所有的秒杀商品
     */
    List<Seckill> getSeckillList();

    /**
     * 通过id查询单个秒杀商品
     */
    Seckill getById(long seckillId);

    /**
     * 暴露秒杀接口地址
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillCloseException, RepeatKillException, SeckillException;

    /**
     * 使用存储过程执行秒杀操作
     */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
