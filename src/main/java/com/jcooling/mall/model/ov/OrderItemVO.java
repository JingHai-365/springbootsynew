package com.jcooling.mall.model.ov;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/5/11
 * @time: 4:20
 * @description: nothing.
 * @version: 1.0
 ***/
public class OrderItemVO {

    private String orderNo;

    private String productName;

    private String productImg;

    private Integer unitPrice;

    private Integer quantity;

    private Integer totalPrice;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}
