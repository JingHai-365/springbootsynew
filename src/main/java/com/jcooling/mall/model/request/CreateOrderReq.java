package com.jcooling.mall.model.request;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/5/6
 * @time: 11:40
 * @description: nothing.
 * @version: 1.0
 ***/

import javax.validation.constraints.NotNull;

public class CreateOrderReq {

    @NotNull(message = "接收人名字不为空")
    private String receiverName;

    @NotNull(message = "接收人电话不为空")
    private String receiverMobile;

    @NotNull(message = "接收人地址不为空")
    private String receiverAddress;

    private Integer postage = 0;

    private Integer paymentType = 1;

    public CreateOrderReq() {
    }

    public CreateOrderReq(@NotNull(message = "接收人名字不为空") String receiverName, @NotNull(message = "接收人电话不为空") String receiverMobile, @NotNull(message = "接收人地址不为空") String receiverAddress, Integer postage, Integer paymentType) {
        this.receiverName = receiverName;
        this.receiverMobile = receiverMobile;
        this.receiverAddress = receiverAddress;
        this.postage = postage;
        this.paymentType = paymentType;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverMobile() {
        return receiverMobile;
    }

    public void setReceiverMobile(String receiverMobile) {
        this.receiverMobile = receiverMobile;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public Integer getPostage() {
        return postage;
    }

    public void setPostage(Integer postage) {
        this.postage = postage;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }
}
