package org.seckill.enums;

import lombok.Data;
import lombok.Getter;

/**
 * 使用枚举表示常量数据字典
 */
@Getter
public enum  SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");
    //状态
    private int state;
    //状态描述
    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    /**
     * 通过索引获取对应的SeckillStatEnum对象
     * @param index 索引
     * @return
     */
    public static SeckillStatEnum stateOf(int index){
        for (SeckillStatEnum seckillStatEnum : values()) {
            if(seckillStatEnum.getState() == index){
                return seckillStatEnum;
            }
        }
        return null;
    }
}
