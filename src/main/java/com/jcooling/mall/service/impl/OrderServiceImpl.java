package com.jcooling.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.jcooling.mall.common.Constant;
import com.jcooling.mall.common.OrderStatusEnum;
import com.jcooling.mall.exception.JcoolingMallException;
import com.jcooling.mall.exception.JcoolingMallExceptionEnum;
import com.jcooling.mall.filter.UserFilter;
import com.jcooling.mall.model.dao.CartMapper;
import com.jcooling.mall.model.dao.OrderItemMapper;
import com.jcooling.mall.model.dao.OrderMapper;
import com.jcooling.mall.model.dao.ProductMapper;
import com.jcooling.mall.model.ov.CartVO;
import com.jcooling.mall.model.ov.OrderItemVO;
import com.jcooling.mall.model.ov.OrderVO;
import com.jcooling.mall.model.pojo.Order;
import com.jcooling.mall.model.pojo.OrderItem;
import com.jcooling.mall.model.pojo.Product;
import com.jcooling.mall.model.request.CreateOrderReq;
import com.jcooling.mall.service.CartService;
import com.jcooling.mall.service.OrderService;
import com.jcooling.mall.service.UserService;
import com.jcooling.mall.utils.OrderCodeFactory;
import com.jcooling.mall.utils.QRCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cteate by IntelliJ IDEA.
 *
 * @author: JingHai
 * @date: 2022/5/11
 * @time: 2:19
 * @description: 订单Service实现类
 * @version: 1.0
 ***/

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    CartService cartService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    UserService userService;

    //创建订单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateOrderReq createOrderReq) {
        //拿到userId
        Integer userId = UserFilter.currentUser.getId();
        //从购物车查找已经勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            if (cartVO.getSelected().equals(Constant.Cart.CHCKED)){
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList=cartVOListTemp;

        //如果购物车勾选为空，报错
        if (CollectionUtils.isEmpty(cartVOList)){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.CART_SELECTED_EMPTY);
        }
        //判断商品是否存在，上下架状态，库存
        validSaleStatusAndStock(cartVOList);

        //把购物车对象转化为订单item对象
        List<OrderItem> orderItemList = cartVOListOrderItemList(cartVOList);

        //更新库存
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock=product.getStock()-orderItem.getQuantity();
            if (stock<0){
                throw new JcoolingMallException(JcoolingMallExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }

        //把购物车中的已勾选商品删除
        cleanCart(cartVOList);

        //生成订单
        Order order = new Order();

        //生成订单号，有独立的规则
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(OrderStatusEnum.NOT_PAID.getCode());
        order.setPostage(0);
        order.setPaymentType(1);

        //插入到order表
        orderMapper.insertSelective(order);

        //循环保存每个商品到order_item表
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }

        //把结果返回
        return orderNo;
    }

    //订单详情
    @Override
    public OrderVO detail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        //订单不存在，则报错
        if (order == null) {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_ORDER);
        }
        //订单存在，则判断所属
        Integer userId = UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_YOUR_ORDER);
        }
        //拼接信息，将order转成orderVO
        OrderVO orderVO = getOrder(order);
        return orderVO;
    }


    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize) {
        Integer userId = UserFilter.currentUser.getId();
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectForCustomer(userId);
        //将order转化为orderVO
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        //包装成pageInfo进行返回
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Override
    public void cancel(String orderNo) {
        //查不到order，报错
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order==null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_ORDER);
        }
        //验证user身份
        //归属
        Integer userId = UserFilter.currentUser.getId();
        if (!order.getUserId().equals(userId)){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_YOUR_ORDER);
        }
        //没有付款才取消
        if (order.getOrderStatus().equals(OrderStatusEnum.NOT_PAID.getCode())) {
            //取消
            order.setOrderStatus(OrderStatusEnum.CANCELED.getCode());
            order.setEndTime(new Date());
            //将订单传到数据库，来更新数据库的状态
            orderMapper.updateByPrimaryKeySelective(order);
        }else{
            throw new JcoolingMallException(JcoolingMallExceptionEnum.WRONG_ORDER);
        }
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectForAdmin();
        //将order转化为orderVO
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        //包装成pageInfo进行返回
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Value("${file.upload.ip}")
    String ip;
    @Override
    public String qrcode(String orderNo){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String address = ip+":"+request.getLocalPort();
        //支付URL
        String payUrl = "http://"+address+"/pay?orderNo="+orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl,350,350, Constant.FILE_UPLOAD_DIR+orderNo+".PNG");
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String pngAddress= "http://"+address+"/Code/Gallery/image/imagessy05/"+orderNo+".png";
        return pngAddress;
    }

    @Override
    public void pay(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order == null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_ORDER);
        }
        if (order.getOrderStatus() ==OrderStatusEnum.NOT_PAID.getCode()){
            order.setOrderStatus(OrderStatusEnum.PAID.getCode());
            order.setPayTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.WRONG_ORDER);
        }
    }

    @Override
    public void delivered(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order == null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_ORDER);
        }
        //订单状态是否付款
        if (order.getOrderStatus() == OrderStatusEnum.PAID.getCode()){
            order.setOrderStatus(OrderStatusEnum.DELIVERED.getCode());
            order.setDeliveryTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.WRONG_ORDER);
        }
    }

    @Override
    public void finish(String orderNo){
        Order order = orderMapper.selectByOrderNo(orderNo);
        //查不到订单，报错
        if (order == null){
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_ORDER);
        }
        //如果是普通用户，就要校验订单的所属
        if (!userService.checkAdminRole(UserFilter.currentUser) && !order.getUserId().equals(UserFilter.currentUser.getId())) {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.NO_YOUR_ORDER);
        }
        // 发货后完结订单
        if (order.getOrderStatus() == OrderStatusEnum.DELIVERED.getCode()){
            order.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
            order.setEndTime(new Date());
            orderMapper.updateByPrimaryKeySelective(order);
        }else {
            throw new JcoolingMallException(JcoolingMallExceptionEnum.WRONG_ORDER);
        }
    }



    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            OrderVO orderVO = getOrder(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    private OrderVO getOrder(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order,orderVO);
        //获取订单对应的orderItemVOList
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem,orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (int i = 0; i < orderItemList.size(); i++) {
            OrderItem orderItem = orderItemList.get(i);
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    private void cleanCart(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    private List<OrderItem> cartVOListOrderItemList(List<CartVO> cartVOList) {
        ArrayList<OrderItem> orderItemList = new ArrayList<>();
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            //记录商品信息
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);

        }
        return orderItemList;
    }

    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (int i = 0; i < cartVOList.size(); i++) {
            CartVO cartVO = cartVOList.get(i);
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            //判断商品是否存在，商品是否上架
            if (product==null||product.getStatus().equals(Constant.SaleStatus.NOT_SALE)){
                throw new JcoolingMallException(JcoolingMallExceptionEnum.NOT_SALE);
            }
            //stock
            if (cartVO.getQuantity()>product.getStock()){
                throw new JcoolingMallException(JcoolingMallExceptionEnum.NOT_ENOUGH);
            }
        }
    }
}
