package org.seckill.dto;

import lombok.Data;

//封装json结果
@Data
public class SeckillResult<T> {
    //请求状态
    private boolean success;
    //返回的数据
    private T data;
    //返回的错误信息
    private String error;

    //请求失败的构造函数
    public SeckillResult(boolean success, String error) {
        this.success = success;
        this.error = error;
    }
    //请求成功的构造函数
    public SeckillResult(boolean success, T data) {
        this.success = success;
        this.data = data;
    }
}
