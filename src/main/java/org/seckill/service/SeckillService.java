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
     * 查询所有的秒杀记录
     */
    List<Seckill> getSeckillList();
    /**
     * 通过id查询单个秒杀记录
     */
    Seckill getById(long seckillId);
    /**
     * 秒杀开启时输出秒杀接口地址
     * 否则输出秒杀开始时间-结束时间
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException,SeckillCloseException,RepeatKillException;

    List<Seckill> getSeckillListByPage(int pageIdx);

    int getSeckillPageNumber(int pageNumber);
}
