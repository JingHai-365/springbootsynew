package com.jcooling.mall.service;

import com.github.pagehelper.PageInfo;
import com.jcooling.mall.model.ov.OrderVO;
import com.jcooling.mall.model.request.CreateOrderReq;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/5/11
 * @time: 2:18
 * @description: nothing.
 * @version: 1.0
 ***/
public interface OrderService {
    public String create(CreateOrderReq createOrderReq);

    public OrderVO detail(String orderNo);

    public PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    public void cancel(String orderNo);

    public PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    public String qrcode(String orderNo);

    public void pay(String orderNo);

    public void delivered(String orderNo);

    void finish(String orderNo);
}
