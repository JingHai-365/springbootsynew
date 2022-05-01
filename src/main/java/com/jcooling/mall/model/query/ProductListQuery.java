package com.jcooling.mall.model.query;

import java.util.List;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/4/23
 * @time: 19:55
 * @description: nothing.
 * @version: 1.0
 */
public class ProductListQuery {
    private String keyword;
    private List<Integer> categoryIds;//category message

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
