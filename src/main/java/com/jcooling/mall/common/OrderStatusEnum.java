package com.jcooling.mall.common;
import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.exception.JcoolingMallExceptionEnum;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/5/6
 * @time: 11:44
 * @description: 订单状态.
 * @version: 1.0
 ***/

public enum OrderStatusEnum{
    CANCELED(0,"用户已取消"),
    NOT_PAID(10,"未付款"),
    PAID(20,"已付款"),
    DELIVERED(30,"已发货"),
    FINISHED(40,"交易完成");
    private String value;
    private int code;

    OrderStatusEnum(int code,String value) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    //通过code返回
    public static OrderStatusEnum codeOf(int code){
        for(OrderStatusEnum orderStatusEnum:values()){
            if (orderStatusEnum.getCode() == code){
                return orderStatusEnum;
            }
        }
        throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_ENUM);
    }
}
