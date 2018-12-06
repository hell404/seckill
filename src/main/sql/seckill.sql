-- 1.存储过程可优化事务行级锁所持有的时间
-- 2.不能过度依赖存储过程
-- 3.简单的逻辑可以应用存储过程

-- 秒杀执行的存储过程
DELIMITER $$
-- 把使用“；”换行转换成使用“$$”换行，防止存储过程中的编写与sql语句中换行的编写冲突
-- 定义存储过程
-- 参数：in表示输入参数（存储过程中可使用）；out表示输出参数（存储过程中不可使用，但可赋值）
-- ROW_COUNT()函数返回上一条sql语句的执行修改（update,insert,delete）影响的行数:
-- 0：表示未修改数据；>0：表示修改的行数；<0：sql错误或sql修改语句（update,insert,delete）未执行
CREATE PROCEDURE `seckill`.`execute_seckill` -- 表示存储过程名
  (in v_seckill_id bigint,in v_phone bigint,in kill_time timestamp,out r_result int)
  BEGIN -- 定义存储过程体
    DECLARE insert_count int DEFAULT 0; -- 定义一个变量
    START TRANSACTION; -- 开启事务
    INSERT ignore INTO success_seckill(seckill_id,user_phone,create_time)
    VALUES (v_seckill_id,v_phone,kill_time);
    SELECT ROW_COUNT() INTO insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK; -- 回滚
      SET r_result = -1; -- 数据字段中定义的为重复秒杀
    ELSEIF(insert_count < 0) THEN
      ROLLBACK;
      SET r_result = -2;-- 数据字段中定义的为系统异常
    ELSE
      UPDATE seckill
      SET number = number - 1
      WHERE seckill_id = v_seckill_id
      AND end_time > kill_time
      AND start_time < kill_time
      AND number > 0;
      SELECT ROW_COUNT() INTO insert_count;
      IF (insert_count = 0) THEN
        ROLLBACK; -- 回滚
        SET r_result = 0; -- 数据字段中定义的为秒杀结束
      ELSEIF(insert_count < 0) THEN
        ROLLBACK;
        SET r_result = -2;-- 数据字段中定义的为系统异常
      ELSE
        COMMIT; -- 提交
        SET r_result = 1; -- 秒杀成功
      END IF;
    END IF;
  END;
$$
-- 存储过程定义结束
DELIMITER ;
-- 将存储过程的换行符重新设置成分号，方便编写代码
-- 调用存储过程
set @r_result = -3; -- 定义一个变量
-- 执行存储过程
call execute_seckill(1003,12311231231,now(),@r_result);
--获取结果
SELECT @r_result;
