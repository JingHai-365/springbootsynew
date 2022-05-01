package com.jcooling.mall.controller;

import com.github.pagehelper.PageInfo;
import com.jcooling.mall.common.ApiRestResponse;
import com.jcooling.mall.common.Constant;
import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.exception.JcoolingMallExceptionEnum;
import com.jcooling.mall.model.pojo.Product;
import com.jcooling.mall.model.request.AddProductReq;
import com.jcooling.mall.model.request.ProductListReq;
import com.jcooling.mall.model.request.UpdateProductReq;
import com.jcooling.mall.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/4/22
 * @time: 23:58
 * @description: nothing.
 * @version: 1.0
 */

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @ApiOperation("后台添加商品")
    @PostMapping("/admin/product/add")
    @ResponseBody
    public ApiRestResponse addProduct(@Valid @RequestBody AddProductReq addProductReq){

        productService.add(addProductReq);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台加载商品图片")
    @PostMapping("/admin/product/upload")
    @ResponseBody
    public ApiRestResponse upload(HttpServletRequest httpServletRequest, @RequestBody MultipartFile file){

       String fileName=file.getOriginalFilename();
       String suffixName=fileName.substring(fileName.lastIndexOf("."));
        UUID uuid = UUID.randomUUID();
        String newFileName=uuid.toString()+suffixName;
        File fileDirectory= new File(Constant.FILE_UPLOAD_DIR);
        File destFile=new File(Constant.FILE_UPLOAD_DIR+newFileName);
        if (!fileDirectory.exists()){
            if (!fileDirectory.mkdirs()){
                throw new JcoolingMallException(JcoolingMallExceptionEnum.MKDIR_FAILED);
            }
        }
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return ApiRestResponse.success(getHost(new URI(httpServletRequest.getRequestURL()+""))+"/Code/Gallery/image/imagessy05/"+newFileName);//这里是URL,不是URI
        } catch (URISyntaxException e) {
           return ApiRestResponse.error(JcoolingMallExceptionEnum.UPLOAD_FAILED);
        }
    }

    //read URI's data
    private URI getHost(URI uri){
        URI effectiveURI;
        try {
            effectiveURI=new URI(uri.getScheme(),uri.getUserInfo(),uri.getHost(),uri.getPort(),null,null,null);
           /* System.out.println(uri.getScheme()+"######################");
            System.out.println(uri.getUserInfo()+"######################");
            System.out.println(uri.getHost()+"######################");
            System.out.println(uri.getPort()+"######################");
            System.out.println(effectiveURI+"********************************");*/
        } catch (URISyntaxException e) {
            effectiveURI=null;
        }
        return effectiveURI;
    }

    @ApiOperation("后台更新商品")
    @PostMapping("/admin/product/update")
    @ResponseBody
    public ApiRestResponse updateProduct(@RequestBody @Valid UpdateProductReq updateProductReq){

        productService.update(updateProductReq);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台删除商品")
    @PostMapping("/admin/product/delete")
    @ResponseBody
    public ApiRestResponse deleteProduct(@RequestParam("id") Integer id){
        productService.delete(id);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台批量更新商品上下架状态")
    @PostMapping("/admin/product/batchUpdateSellStatus")
    @ResponseBody
    public ApiRestResponse batchUpdateSellStatus(@RequestParam Integer[] ids,@RequestParam Integer sellStatus){
        productService.batchUpdateSellStatus(ids,sellStatus);
        return ApiRestResponse.success();
    }

    @ApiOperation("后台商品列表分页")
    @PostMapping("/admin/product/list")
    @ResponseBody
    public ApiRestResponse list(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize){
        PageInfo pageInfo=productService.listForAdmin(pageNum,pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @ApiOperation("前台商品详情")
    @GetMapping("/product/detail")
    @ResponseBody
    public ApiRestResponse detail(@RequestParam("id") Integer id){
        Product product =productService.detail(id);
        return ApiRestResponse.success(product);
    }

    @ApiOperation("前台商品详情列表")
    @GetMapping("/product/list")
    @ResponseBody
    public ApiRestResponse list(ProductListReq productListReq){
        PageInfo list =productService.list(productListReq);
        return ApiRestResponse.success(list);
    }
}
