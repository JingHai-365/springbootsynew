package com.jcooling.mall.service.impl;

import com.jcooling.mall.common.Constant;
import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.exception.JcoolingMallExceptionEnum;
import com.jcooling.mall.model.dao.CartMapper;
import com.jcooling.mall.model.dao.ProductMapper;
import com.jcooling.mall.model.ov.CartVO;
import com.jcooling.mall.model.pojo.Cart;
import com.jcooling.mall.model.pojo.Product;
import com.jcooling.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CartMapper cartMapper;

    //每次添加完购物车，购物车的内容是变化的，故需要返回一个List集合
    //采用直接返回List集合，可以减少前端刷新次数，提高查询效率
    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        validProduct(productId,count);
        Cart cart = cartMapper.selectByUserIdAndProduct(userId, productId);
        //判断查出来的cart是不是为空
        if (cart == null) {
            //这个商品之前不再购物车，需要新增一个记录
            cart = new Cart();
            cart.setProductId(productId);
            cart.setUserId(userId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHCKED);
            cartMapper.insertSelective(cart);
        }else {
            //这个商品已经在购物车里了，则数量相加
            count = cart.getQuantity() +count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHCKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }
    //验证添加是不是合法的
    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        //判断商品是否存在，商品是否上架
        if (product == null  || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NOT_SALE);
        }
        //判断商品库存
        if (count > product.getStock()){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NOT_ENOUGH);
        }
    }

    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        //计算总价
        for (int i = 0; i < cartVOS.size(); i++) {
            CartVO cartVO = cartVOS.get(i);
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOS;
    }

    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) {
        validProduct(productId,count);
        Cart cart = cartMapper.selectByUserIdAndProduct(userId, productId);
        //判断查出来的cart是不是为空
        if (cart == null) {
           throw new JcoolingMallException(JcoolingMallExceptionEnum.UPDATE_FAILED);
        }else {
            count = cart.getQuantity() +count;
            Cart cartNew = new Cart();
            cartNew.setQuantity(count);
            cartNew.setId(cart.getId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setSelected(Constant.Cart.CHCKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
   }

    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        Cart cart = cartMapper.selectByUserIdAndProduct(userId, productId);
        //判断查出来的cart是不是为空
        if (cart == null) {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.DELETE_FAILED);
        }else {
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected){
        //将购物车查询出来
        Cart cart = cartMapper.selectByUserIdAndProduct(userId, productId);
        //判断查出来的cart是不是为空
        if (cart == null) {
            //这个商品之前不再购物车，无法选中或不选中
            throw new JcoolingMallException(JcoolingMallExceptionEnum.UPDATE_FAILED);
        } else {
            //这个商品已经在购物车里了，则可以选中或不选中
            cartMapper.selectOrNot(userId,productId,selected);
        }
        return this.list(userId);
    }


}
