package com.crud_restful.exception;

public enum ServiceEnumType {
    GET_FAIL(-1, "查询失败"),
    DELETE_FAIL(-2, "删除失败"),
    UPDATE_FAIL(-3, "修改失败"),
    INSERT_FAIL(-4, "添加失败"),

    GET_SUCCESS(1, "查询成功"),
    DELETE_SUCCESS(2, "删除成功"),
    UPDATE_SUCCESS(3, "修改成功"),
    INSERT_SUCCESS(4, "添加成功"),
    ;
    private Integer code;
    private String msg;

    ServiceEnumType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
