package org.jleopard.mall.service;

import org.jleopard.mall.model.Order;
import org.jleopard.mall.model.OrderKey;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  下午5:27
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
public interface OrderService {

    int deleteByIds(OrderKey key);

    Order insert(Order order);

    List<Order> selectByIds(OrderKey key);

    List<Order> selectBySelective(Order order);

    Order updateByIdSelective(Order order);

    List<Order> select();

    int updateStatusBySerial(String serial, Byte s);
}
