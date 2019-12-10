package org.seckill.exception;

/**
 * 所有秒杀相关的业务异常
 */
public class SeckillException extends RuntimeException {
    public SeckillException(String message) {
        super(message);
    }
}
