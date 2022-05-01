package com.jcooling.mall.controller;

import com.jcooling.mall.common.ApiRestResponse;
import com.jcooling.mall.filter.UserFilter;
import com.jcooling.mall.model.ov.CartVO;
import com.jcooling.mall.service.CartService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
}
