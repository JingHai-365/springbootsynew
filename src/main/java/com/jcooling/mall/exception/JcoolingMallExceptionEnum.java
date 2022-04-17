package com.jcooling.mall.exception;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:31:30
* @version: 1.0
* @description: nothing.
*/
public enum JcoolingMallExceptionEnum {
    NEED_USER_NAME(10001,"用户名不能为空"),
    NEED_PASSWORD(10002,"密码不能为空"),
    SYSTEM_ERROR(10003,"系统异常"),
    USER_EXISTED(10004,"用户已经存在"),
    INSERT_FAILED(10005,"插入用户失败"),
    PASSWORD_TOO_LONG_OR_SHORT(10006,"密码长度小于8或者大于16"),
    WRONG_PASSWORD(10007,"密码错误"),
    NEED_LOING(10008,"需要登陆"),
    UPDATE_FAILED(10009,"更新失败"),
    NEED_ADMIN(10010,"需要管理员权限"),
    REQUEST_PARAM_ERROR(10011, "参数错误"),
    //PARAM_NOT_NULL(10011,"参数不能为空"),
    NAME_NOT_NULL(10012,"目录名称已存在"),
    CREATE_FAILED(10013,"新增失败"),
    NAME_EXISTED(10014,"不允许重名"),
    DELETE_FAILED(10015,"删除失败");


    //异常编码
    private Integer code;
    //异常描述信息
    private String msg;

    JcoolingMallExceptionEnum(Integer code, String msg) {
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