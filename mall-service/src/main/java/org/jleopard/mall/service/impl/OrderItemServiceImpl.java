package org.jleopard.mall.service.impl;

import org.jleopard.mall.dao.OrderItemMapper;
import org.jleopard.mall.model.OrderItem;
import org.jleopard.mall.model.OrderItemKey;
import org.jleopard.mall.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  下午6:01
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    OrderItemMapper orderItemMapper;

    @Override
    public int deleteByIds(OrderItemKey key) {
        return orderItemMapper.deleteByPrimaryKey(key);
    }

    @Override
    public int insert(OrderItem orderItem) {
        return orderItemMapper.insertSelective(orderItem);
    }

    @Override
    public List<OrderItem> selectByIds(OrderItemKey key) {
        return orderItemMapper.selectByPrimaryKey(key);
    }

    @Override
    public List<OrderItem> selectBySelective(OrderItem orderItem) {
        return orderItemMapper.selectBySelective(orderItem);
    }

    @Override
    public int updateByIdSelective(OrderItem orderItem) {
        return orderItemMapper.updateByPrimaryKeySelective(orderItem);
    }
}
