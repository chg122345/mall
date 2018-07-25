package org.jleopard.mall.service.impl;

import org.jleopard.mall.dao.AddressMapper;
import org.jleopard.mall.model.Address;
import org.jleopard.mall.model.AddressKey;
import org.jleopard.mall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  下午6:05
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressMapper addressMapper;

    @Override
    public int deleteByIds(AddressKey key) {
        return addressMapper.deleteByPrimaryKey(key);
    }

    @Override
    public Address insert(Address address) {
        return addressMapper.insertSelective(address) > 0 ? address : null;
    }

    @Override
    public List<Address> selectByIds(AddressKey key) {
        return addressMapper.selectByPrimaryKey(key);
    }

    @Override
    public Address selectById(Integer id) {
        return addressMapper.selectById(id);
    }

    @Override
    public Address updateByIdSelective(Address address) {
        return addressMapper.updateByPrimaryKeySelective(address) > 0 ? address : null;
    }
}
