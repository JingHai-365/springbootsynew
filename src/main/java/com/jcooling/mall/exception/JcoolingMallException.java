package com.jcooling.mall.exception;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @date: 2022/04/11
* @time: 10:31:25
* @version: 1.0
* @description: nothing.
*/
public class JcoolingMallException extends RuntimeException{
    private Integer code;
       private String msg;

    public JcoolingMallException() {
    }

    public JcoolingMallException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public JcoolingMallException(JcoolingMallExceptionEnum jcoolingMallExceptionEnum){
        this(jcoolingMallExceptionEnum.getCode(),jcoolingMallExceptionEnum.getMsg());
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
