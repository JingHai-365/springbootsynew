package com.jcooling.mall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:36:27
* @version: 1.0
* @description: nothing.
*/
public class AddCategoryReq {
    @NotNull(message = "name不能为空")
    @Size(min=2,max = 5)
    private String name;
    @NotNull(message = "type不能为空")
    @Max(3)
    private Integer type;
    @NotNull(message = "parentId不能为空")
    private Integer parentId;
    @NotNull(message = "orderNum不能为空")
    private Integer orderNum;

    public AddCategoryReq() {
    }

    public AddCategoryReq(String name, Integer type, Integer parentId, Integer orderNum) {
        this.name = name;
        this.type = type;
        this.parentId = parentId;
        this.orderNum = orderNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }
}
