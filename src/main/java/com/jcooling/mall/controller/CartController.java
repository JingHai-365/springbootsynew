package com.jcooling.mall.controller;

import com.jcooling.mall.common.ApiRestResponse;
import com.jcooling.mall.filter.UserFilter;
import com.jcooling.mall.model.ov.CartVO;
import com.jcooling.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/4/29
 * @time: 0:15
 * @description: nothing.
 * @version: 1.0
 */
@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @ApiOperation("添加商品到购物车")
    @PostMapping("/cart/add")
    public ApiRestResponse addCart(@RequestParam Integer productId,@RequestParam Integer count){
        List<CartVO> cartVOList = cartService.add(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    //购物车列表
    @ApiOperation("购物车列表")
    @GetMapping("/cart/list")
    public ApiRestResponse listCart() {
        //内部获取用户ID，防止横向越权
        List<CartVO> list = cartService.list(UserFilter.currentUser.getId());
        return ApiRestResponse.success(list);
    }

    @ApiOperation("更新购物车商品")
    @PostMapping("/cart/update")
    public ApiRestResponse updateCart(@RequestParam Integer productId,@RequestParam Integer count) {
        List<CartVO> cartVOList = cartService.update(UserFilter.currentUser.getId(), productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("删除购物车商品")
    @PostMapping("/cart/delete")
    public ApiRestResponse deleteCart(@RequestParam Integer productId) {
        List<CartVO> cartVOList = cartService.delete(UserFilter.currentUser.getId(), productId);
        return ApiRestResponse.success(cartVOList);
    }

    @ApiOperation("选中或不选中购物车商品")
    @PostMapping("/cart/select")
    public ApiRestResponse selectCart(@RequestParam Integer productId,@RequestParam Integer selected) {
        List<CartVO> cartVOList = cartService.selectOrNot(UserFilter.currentUser.getId(), productId, selected);
        return ApiRestResponse.success(cartVOList);
    }
}
