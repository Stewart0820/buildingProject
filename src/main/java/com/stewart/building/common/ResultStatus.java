package com.stewart.building.common;

/**
 * @ Created by Stewart on 2017/5/10 .
 * @ Description:自定义请求状态码
 */
public enum ResultStatus {
    /**
     * 500：失败
     * 200：成功
     */
    /**
     * info
     */
    NOT_LOGIN(401,"未登录，请登录"),
    NOT_PERMISSION(403,"权限不足，请联系管理员"),
    /**
     * 成功信息
     */
    SUCCESS(200, "成功"),
    SAVE_SUCCESS(200, "增加成功"),
    DELETE_SUCCESS(200, "删除成功"),
    UPDATE_SUCCESS(200, "更改成功"),
    SELECT_SUCCESS(200, "查询成功"),
    SINGUP_SUCCESS(200, "注册成功"),
    LOGIN_SUCCESS(200, "登录成功"),
    LOGOUT_SUCCESS(200, "退出成功"),
    GAIN_SUCCESS(200,"获取成功"),

    /**
     * 失败
     */
    ERROR(500,"失败"),
    TYPE_ERROR(500,"类型转换错误"),
    ASSOCIATED_DATA_ERROR(500,"该数据有关联数据，操作失败！"),
    MYSQL_ERROR(500,"数据库异常，操作失败！"),
    BUSINESS_ERROR(500,"业务出现错误,请联系后端开发人员"),
    USERNAME_ERROR(500,"用户名错误或者密码错误")
    ;

    /**
     * 返回码
     */
    private int status;

    /**
     * 返回结果描述
     */
    private String msg;

    ResultStatus() {
    }

    ResultStatus(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}