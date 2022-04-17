package com.jcooling.mall.controller;

import com.github.pagehelper.PageInfo;
import com.jcooling.mall.common.ApiRestResponse;
import com.jcooling.mall.model.ov.CategoryVO;
import com.jcooling.mall.model.pojo.Category;
import com.jcooling.mall.model.request.AddCategoryReq;
import com.jcooling.mall.model.request.UpdateCategoryReq;
import com.jcooling.mall.service.CategoryService;
import com.jcooling.mall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
/**
* Cteate by IntelliJ IDEA.
* @author: JingHai
* @data: 2022/04/11
* @time: 10:30:38
* @version: 1.0
* @description: nothing.
*/
@Controller
public class CategroyController {

    @Autowired
    private UserService userService;
    @Autowired
    private CategoryService categoryService;
    @ApiOperation("添加商品分类")
    @PostMapping("/admin/category/add")
    @ResponseBody//let java object convert to json data,will not go view adapter again
    public ApiRestResponse categroyAdd(@RequestBody @Valid AddCategoryReq addCategoryReq){//HttpSession session
       /*//空值校验
        if(addCategoryReq.getName()==null||addCategoryReq.getType()==null||
        addCategoryReq.getParentId()==null||addCategoryReq.getOrderNum()==null){
            return ApiRestResponse.error(JcoolingMallExceptionEnum.PARAM_NOT_NULL);
        }*/
        //身份校验
      /* User currentUser = (User)session.getAttribute(Constant.SMOOTH_MALL_USER);
        if (currentUser==null){
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_LOING);
        }
        boolean adminRole= userService.checkAdminRole(currentUser);
        if (adminRole){
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        }else {
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NAME_ADMIN);
        }*/
        categoryService.add(addCategoryReq);
        return ApiRestResponse.success();
    }

    @ApiOperation("修改目录分类")
    @PostMapping("/admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(@RequestBody @Valid UpdateCategoryReq updateCategoryReq){//HttpSession session
        /*User currentUser = (User)session.getAttribute(Constant.SMOOTH_MALL_USER);
        if(currentUser==null){
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_LOING);
        }
        //校验管理员
        boolean adminRole = userService.checkAdminRole(currentUser);
        if(adminRole){
            Category category = new Category();
            BeanUtils.copyProperties(updateCategoryReq,category);
            categoryService.update(category);
            return ApiRestResponse.success();
        }else{
            return ApiRestResponse.error(JcoolingMallExceptionEnum.NEED_ADMIN);
        }*/
        //test git sentence
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq,category);
        categoryService.update(category);
        return ApiRestResponse.success();
    }

    @ApiOperation("删除目录分类")
    @PostMapping("/admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam Integer id){
        categoryService.delete(id);
        return ApiRestResponse.success();
    }


    @ApiOperation("目录分类列表")
    @PostMapping("/admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam Integer pageNumber,@RequestParam(value = "pageSize",required = false) Integer pageSize){
        PageInfo pageInfo = categoryService.listForAdmin(pageNumber, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台目录分类列表")
    @PostMapping("/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer(){
        List<CategoryVO> categoryVOS = categoryService.listForCustomer();
        return ApiRestResponse.success(categoryVOS);
    }
}
