package org.jleopard.mall.service;

import org.apache.ibatis.annotations.Param;
import org.jleopard.mall.model.OrderItem;
import org.jleopard.mall.model.OrderItemKey;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  下午5:58
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
public interface OrderItemService {

    int deleteByIds(OrderItemKey key);

    int insert(OrderItem orderItem);

    List<OrderItem> selectByIds(OrderItemKey key);

    List<OrderItem> selectBySelective(OrderItem orderItem);

    int updateByIdSelective(OrderItem orderItem);

}
