package com.jcooling.mall.service;

import com.github.pagehelper.PageInfo;
import com.jcooling.mall.model.ov.CategoryVO;
import com.jcooling.mall.model.pojo.Category;
import com.jcooling.mall.model.request.AddCategoryReq;

import java.util.List;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:36:51
* @version: 1.0
* @description: nothing.
*/
public interface CategoryService {
    public void add(AddCategoryReq addCategoryReq);
    public void update(Category updateCategory);
    public void delete(Integer id);
    public PageInfo listForAdmin(Integer pageNumber, Integer pageSize);
    public List<CategoryVO> listForCustomer();
}
