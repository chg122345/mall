package org.jleopard.mall.service.impl;

import org.jleopard.mall.dao.ProductMapper;
import org.jleopard.mall.model.Product;
import org.jleopard.mall.model.ProductKey;
import org.jleopard.mall.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  上午11:09
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductMapper productMapper;

    @Override
    public int deleteByIds(ProductKey key) {
        return productMapper.deleteByPrimaryKey(key);
    }

    @Override
    public Product insert(Product product) {
        if (productMapper.insertSelective(product) > 0){
            return product;
        }
        return null;
    }

    @Override
    public List<Product> selectByIds(ProductKey key) {
        return productMapper.selectByPrimaryKey(key);
    }

    @Override
    public List<Product> select() {
        return productMapper.selectAll();
    }

    @Override
    public List<Product> selectBySelective(Product product) {
        return productMapper.selectBySelective(product);
    }

    @Override
    public Product updateByIdSelective(Product product) {
        if (productMapper.updateByPrimaryKeySelective(product) > 0){
            return product;
        }
        return null;
    }
}
