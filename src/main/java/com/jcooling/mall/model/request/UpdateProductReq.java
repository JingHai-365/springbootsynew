package com.jcooling.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @data: 2022/4/23
 * @time: 10:54
 * @description: nothing.
 * @version: 1.0
 */
public class UpdateProductReq {

    @NotNull(message = "id不能为空")
    private Integer id;
    @NotNull(message = "商品名称不能为空")
    private String name;

    private String image;

    private  String detail;

    private Integer categoryId;

    @Max(value = 10000,message = "库存不能大于10000")
    private Integer stock;
    @Min(value = 1,message = "价格不能小于一分")
    private Integer price;
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
