package com.jcooling.mall.service;

import com.jcooling.mall.model.ov.CartVO;

import java.util.List;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/4/29
 * @time: 13:50
 * @description: nothing.
 * @version: 1.0
 */
public interface CartService {

    List<CartVO> add(Integer userId,Integer productId,Integer count);

    List<CartVO> list(Integer userId);
}
