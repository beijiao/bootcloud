package com.dj.p2p.base;

import java.io.Serializable;

public class ResultModel<T> implements Serializable {

	public ResultModel(){}
	
    private Integer code;
    private String msg;
    private T data;

    public ResultModel success(T data) {
        this.code = 200;
        this.msg = "success";
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public ResultModel success(String msg) {
        this.code = 200;
        this.msg = msg;
        return this;
    }

    public ResultModel error(String msg) {
        this.code = -1;
        this.msg = msg;
        return this;
    }

    public ResultModel error(T data) {
        this.code = -1;
        this.msg = "error";
        this.data = data;
        return this;
    }

    public ResultModel error(int code, T data) {
        this.code = code;
        this.msg = "error";
        this.data = data;
        return this;
    }

    public ResultModel error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public ResultModel error(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        return this;
    }
    
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
