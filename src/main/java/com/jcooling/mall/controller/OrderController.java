package com.jcooling.mall.controller;

import com.github.pagehelper.PageInfo;
import com.jcooling.mall.common.ApiRestResponse;
import com.jcooling.mall.model.ov.OrderVO;
import com.jcooling.mall.model.request.CreateOrderReq;
import com.jcooling.mall.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/5/11
 * @time: 2:33
 * @description: nothing.
 * @version: 1.0
 ***/

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @ApiOperation("创建订单")
    @PostMapping("/order/create")
    @ResponseBody
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq){
       String orderNo=orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }


    @GetMapping("/order/detail")
    @ApiOperation("前台订单详情")
    @ResponseBody
    public ApiRestResponse detail(@RequestParam String orderNo){
        OrderVO orderVO = orderService.detail(orderNo);
        return ApiRestResponse.success(orderVO);
    }

    @GetMapping("/order/list")
    @ApiOperation("前台订单列表")
    @ResponseBody
    public ApiRestResponse list(@RequestParam Integer pageNum ,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForCustomer(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @PostMapping("/order/cancel")
    @ApiOperation("前台取消订单")
    @ResponseBody
    public ApiRestResponse cancel(@RequestParam String orderNo){
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    @GetMapping("/admin/order/list")
    @ApiOperation("管理员订单列表")
    @ResponseBody
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum,@RequestParam Integer pageSize){
        PageInfo pageInfo = orderService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @PostMapping("/admin/qrcode")
    @ApiOperation("生成二维码")
    @ResponseBody
    public ApiRestResponse listForAdmin(@RequestParam String orderNo){
        String pngAddress = orderService.qrcode(orderNo);
        return ApiRestResponse.success(pngAddress);
    }

    @GetMapping("/pay")
    @ApiOperation("前台支付订单")
    @ResponseBody
    public ApiRestResponse pay(@RequestParam String orderNo){
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/order/delivered")
    @ApiOperation("后台订单发货")
    @ResponseBody
    public ApiRestResponse delivered(@RequestParam String orderNo){
        orderService.delivered(orderNo);
        return ApiRestResponse.success();
    }

    @PostMapping("/order/finish")
    @ApiOperation("订单完结")
    @ResponseBody
    public ApiRestResponse finish(@RequestParam String orderNo){
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }

}
