package com.jcooling.mall.model.request;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @data: 2022/4/23
 * @time: 19:44
 * @description: nothing.
 * @version: 1.0
 */
public class ProductListReq {
    private String keyword;
    private Integer categoryId;
    private String orderBy;
    //paging message
    private Integer pageNum=1;
    private Integer pageSize=9;

    public String getKeyword(){
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
