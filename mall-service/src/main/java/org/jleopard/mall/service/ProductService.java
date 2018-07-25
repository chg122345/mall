package org.jleopard.mall.service;

import org.jleopard.mall.model.Product;
import org.jleopard.mall.model.ProductKey;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-25  上午11:06
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
public interface ProductService {

    int deleteByIds(ProductKey key);

    Product insert(Product product);

    List<Product> selectByIds(ProductKey key);

    List<Product> select();

    List<Product> selectBySelective(Product product);

    Product updateByIdSelective(Product product);

}
