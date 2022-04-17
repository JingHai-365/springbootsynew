package com.jcooling.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.exception.JcoolingMallExceptionEnum;
import com.jcooling.mall.model.dao.CategoryMapper;
import com.jcooling.mall.model.ov.CategoryVO;
import com.jcooling.mall.model.pojo.Category;
import com.jcooling.mall.model.request.AddCategoryReq;
import com.jcooling.mall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:36:41
* @version: 1.0
* @description: nothing.
*/
@Service
public class CategroyServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public void add(AddCategoryReq addCategoryReq) {
        Category category=new Category();
        BeanUtils.copyProperties(addCategoryReq,category);
        Category categoryId = categoryMapper.selectByName(category.getName());
        if (categoryId!=null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NAME_NOT_NULL);
        }
        int count = categoryMapper.insertSelective(category);//add
        if (count==0){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.CREATE_FAILED);
        }
    }

    @Override
    public void update(Category updateCategory){
        if(updateCategory.getName()!=null){
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
        /*    判断获取的对象不为空，
             且获取的对象id与参数中的id不同，
             则名字冲突，抛出不允许重名异常*/
            if(categoryOld!=null&&!categoryOld.getId().equals(updateCategory.getId())){
                throw new JcoolingMallException(JcoolingMallExceptionEnum.NAME_EXISTED);
            }
        }
        int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
        if(count==0){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.UPDATE_FAILED);
        }
    }

    public void delete(Integer id){
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        //查不到记录，无法删除，删除失败
        if(categoryOld==null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.DELETE_FAILED);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if(count==0){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.DELETE_FAILED);
        }
    }
    @Override
    public PageInfo listForAdmin(Integer pageNumber, Integer pageSize) {
        PageHelper.startPage(pageNumber,pageSize,"type,order_num");
        List<Category> categoryList = categoryMapper.selectList();
        PageInfo pageInfo = new PageInfo(categoryList);
        return pageInfo;
    }

    @Cacheable(value = "listForCustomer")
    @Override
    public List<CategoryVO> listForCustomer() {
        ArrayList<CategoryVO> categoryVOList= new ArrayList<>();
        recursiveFindCategories(categoryVOList,0);
        return categoryVOList;
    }

    //递归
    private void recursiveFindCategories(List<CategoryVO> categoryVOList,Integer parentId) {
        //递归获取所有子类别，并组合成为一个“目录树”
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if (!CollectionUtils.isEmpty(categoryList)) {
            for (int i = 0; i < categoryList.size(); i++) {
                Category category = categoryList.get(i);
                //System.out.println(category+"***********************************************");
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category,categoryVO);
                categoryVOList.add(categoryVO);
                //给categoryVO设置childCategory属性
                recursiveFindCategories(categoryVO.getChildCategory(), categoryVO.getId());
            }
        }
    }
}
