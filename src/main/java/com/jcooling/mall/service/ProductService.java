package com.jcooling.mall.service;

import com.github.pagehelper.PageInfo;
import com.jcooling.mall.model.pojo.Product;
import com.jcooling.mall.model.request.AddProductReq;
import com.jcooling.mall.model.request.ProductListReq;
import com.jcooling.mall.model.request.UpdateProductReq;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/4/23
 * @time: 2:04
 * @description: nothing.
 * @version: 1.0
 */
public interface ProductService {

    public void add(AddProductReq addProductReq);

    void update(UpdateProductReq updateProductReq);

    void delete(Integer id);

    void batchUpdateSellStatus(Integer[] ids, Integer sellStatus);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    Product detail(Integer id);

    PageInfo list(ProductListReq productListReq);
}
