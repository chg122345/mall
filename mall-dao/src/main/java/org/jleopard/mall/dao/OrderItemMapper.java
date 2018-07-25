package org.jleopard.mall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jleopard.mall.model.OrderItem;
import org.jleopard.mall.model.OrderItemKey;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    int deleteByPrimaryKey(OrderItemKey key);

    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    List<OrderItem> selectByPrimaryKey(OrderItemKey key);

    List<OrderItem> selectBySelective(OrderItem record);

    List<OrderItem> selectByOrderPrimaryKey(@Param("orderId") String orderId);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}