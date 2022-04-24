package com.jcooling.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcooling.mall.common.Constant;
import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.exception.JcoolingMallExceptionEnum;
import com.jcooling.mall.model.dao.ProductMapper;
import com.jcooling.mall.model.ov.CategoryVO;
import com.jcooling.mall.model.pojo.Product;
import com.jcooling.mall.model.request.AddProductReq;
import com.jcooling.mall.model.request.ProductListQuery;
import com.jcooling.mall.model.request.ProductListReq;
import com.jcooling.mall.model.request.UpdateProductReq;
import com.jcooling.mall.service.CategoryService;
import com.jcooling.mall.service.ProductService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @data: 2022/4/23
 * @time: 2:05
 * @description: nothing.
 * @version: 1.0
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryService categoryService;
    @Override
    public void add(AddProductReq addProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq,product);
        Product productOld = productMapper.selectByName(addProductReq.getName());
        if (productOld!=null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum. NAME_EXISTED);
        }
        int count=productMapper.insertSelective(product);
        if (count==0){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(UpdateProductReq updateProductReq) {

        int count;
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        Product productOld = productMapper.selectByName(product.getName());

        if (productOld != null && productOld.getId().equals(updateProductReq.getId())) {
            count = productMapper.updateByPrimaryKeySelective(product);
        } else {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NAME_EXISTED);
        }
        if (count == 0) {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        Product productOld = productMapper.selectByPrimaryKey(id);
        if (productOld==null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.DELETE_FAILED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count==0){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.DELETE_FAILED);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids,sellStatus);
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> product=productMapper.selectListForAdmin();
        PageInfo pageInfo = new PageInfo(product);
        return pageInfo;
    }

    @Override
    public Product detail(Integer id) {
        Product product = productMapper.selectByPrimaryKey(id);
        return product;
    }

    @Override
    public PageInfo list(ProductListReq productListReq) {
        ProductListQuery productListQuery = new ProductListQuery();
        //search processing
        if (!StringUtils.hasLength(productListQuery.getKeyword())){
            String keyword=new StringBuffer().append("%").append(productListReq.getKeyword()).append("%").toString();
            productListQuery.setKeyword(keyword);
        }
        /*目录处理：如果查某个目录下的商品，不仅是需要查出该目录下的，
        还要把所有子目录的所有商品都查出来，所以要拿到一个目录id的List*/
        if (productListReq.getCategoryId()!=null){
            List<CategoryVO> categoryVOList = categoryService.listForCustomer(productListReq.getCategoryId());
            ArrayList<Integer> categoryIds = new ArrayList<>();
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(categoryVOList, categoryIds);
            productListQuery.setCategoryIds(categoryIds);
        }
        //排序处理，不能根据前端的排序参数来处理，不安全
        String orderBy = productListReq.getOrderBy();
        //定义排序规则接口Constant.ProductListOrderBy.PRICE_ASC_DESC
        if (Constant.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            //获取的前端排序规则支持接口中的排序规则，使用规则进行构建分页对象
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(),
                    orderBy);
        } else {
            //不支持接口中的规则，不进行排序
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }
        //
        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    //递归获取子目录信息
    private void getCategoryIds(List<CategoryVO> categoryVOList, ArrayList<Integer> categoryIds) {
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }
        }
    }
}
