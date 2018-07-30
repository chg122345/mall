package org.jleopard.mall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jleopard.mall.model.Order;
import org.jleopard.mall.model.OrderKey;

import java.util.List;

@Mapper
public interface OrderMapper {

    int deleteByPrimaryKey(OrderKey key);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByPrimaryKey(OrderKey key);

    List<Order> selectBySelective(Order record);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectAll();
}