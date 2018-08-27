package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复
     * @param seckillId 秒杀商品id
     * @param userPhone 购买用户的手机号
     * @return 更新行数/插入行数
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId , @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuucessKilled
     * @param seckillId 秒杀商品id
     * @return 购买商品明细对象，携带Seckill秒杀产品实体对象
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId , @Param("userPhone") long userPhone);

}
