package com.crud_restful.util;

public class RespBean {
    private Integer code;
    private String msg;
    private Object date;

    public static RespBean success(Integer code, String msg) {
        return new RespBean(code, msg, null);
    }

    public static RespBean success(Integer code, String msg, Object date) {
        return new RespBean(code, msg, date);
    }

    public static RespBean fail(Integer code, String msg) {
        return new RespBean(code, msg, null);
    }

    public static RespBean fail(Integer code, String msg, Object date) {
        return new RespBean(code, msg, date);
    }

    public RespBean(Integer code, String msg, Object date) {
        this.code = code;
        this.msg = msg;
        this.date = date;
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

    public Object getDate() {
        return date;
    }

    public void setDate(Object date) {
        this.date = date;
    }
}
