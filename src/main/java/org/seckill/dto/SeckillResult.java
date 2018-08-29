package org.seckill.dto;

//封装json结果
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
