package org.jleopard.mall.service.impl;

import org.jleopard.Primarys;
import org.jleopard.mall.dao.OrderMapper;
import org.jleopard.mall.model.Order;
import org.jleopard.mall.model.OrderItem;
import org.jleopard.mall.model.OrderKey;
import org.jleopard.mall.service.OrderItemService;
import org.jleopard.mall.service.OrderService;
import org.jleopard.util.RandomKeyUtils;
import org.jleopard.util.RandomOrderSerialKeyHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  下午5:32
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemService orderItemService;

    @Override
    public int deleteByIds(OrderKey key) {
        return orderMapper.deleteByPrimaryKey(key);
    }

    @Transactional
    @Override
    public Order insert(Order order) {
        String orderId = RandomKeyUtils.generateString(Primarys.ORDER_ID);
        order.setId(orderId);
        order.setSerial(RandomOrderSerialKeyHelper.getInstance().getSerialKey());
        if (orderMapper.insertSelective(order) > 0){
            List<OrderItem> orderItems = order.getOrderItem();
            orderItems.stream().forEach(orderItem -> {
                orderItem.setMlOrderId(orderId);
                orderItemService.insert(orderItem);
            });
        }
        return  order;
    }

    @Override
    public List<Order> selectByIds(OrderKey key) {
        return orderMapper.selectByPrimaryKey(key);
    }

    @Override
    public List<Order> selectBySelective(Order order) {
        return orderMapper.selectBySelective(order);
    }

    @Override
    public Order updateByIdSelective(Order order) {
        return orderMapper.updateByPrimaryKeySelective(order) > 0 ? order : null;
    }

    @Override
    public List<Order> select() {
        return orderMapper.selectAll();
    }
}
