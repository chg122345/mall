package org.jleopard.mall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jleopard.mall.model.Product;
import org.jleopard.mall.model.ProductKey;

import java.util.List;

@Mapper
public interface ProductMapper {

    int deleteByPrimaryKey(ProductKey key);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectById(@Param("id") String id);

    List<Product> selectByPrimaryKey(ProductKey key);

    List<Product> selectAll();

    List<Product> selectBySelective(Product record);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
}