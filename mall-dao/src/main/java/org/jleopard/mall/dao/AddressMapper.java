package org.jleopard.mall.dao;

import org.apache.ibatis.annotations.Mapper;
import org.jleopard.mall.model.Address;
import org.jleopard.mall.model.AddressKey;

import java.util.List;

@Mapper
public interface AddressMapper {
    int deleteByPrimaryKey(AddressKey key);

    int insert(Address record);

    int insertSelective(Address record);

    List<Address> selectByPrimaryKey(AddressKey key);

    Address selectById(Integer id);

    int updateByPrimaryKeySelective(Address record);

    int updateByPrimaryKey(Address record);
}