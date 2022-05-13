package com.jcooling.mall.model.dao;

import com.jcooling.mall.model.ov.CartVO;
import com.jcooling.mall.model.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectByUserIdAndProduct(@Param("userId") Integer userId,@Param("productId") Integer productId);

    List<CartVO> selectList(@Param("userId") Integer userId);

    Integer selectOrNot(@Param("userId") Integer userId,@Param("productId") Integer productId,@Param("selected") Integer selected);

}