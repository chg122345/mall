package org.jleopard.test;

import org.jleopard.mall.config.RootConfig;
import org.jleopard.mall.model.Product;
import org.jleopard.mall.model.ProductKey;
import org.jleopard.mall.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-26  下午5:55
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    public void deleteByIds() {
    }

    @Test
    public void insert() {
        productService.insert(getProduct());
    }

    @Test
    public void selectByIds() {
        ProductKey pk = new ProductKey();
       // pk.setId("pdqsa9s8ecusv0cift4xdbndjargtaqs");
        pk.setMlCategoryId(1);
        System.out.println(productService.selectByIds(pk));
    }

    @Test
    public void select() {
        System.out.println(productService.select());
    }

    @Test
    public void selectBySelective() {
        System.out.println(productService.selectBySelective(getProduct()));
    }

    @Test
    public void updateByIdSelective() {

        Product product = new Product();
        product.setId("pdqsa9s8ecusv0cift4xdbndjargtaqs");
        product.setStock(8);
        product.setDetails("非常好吃的辣条快来买吧");
        System.out.println(productService.updateByIdSelective(product));
    }

    private Product getProduct(){
        Product product = new Product();
        product.setMlCategoryId(1);
        product.setImgs("http:///");
        product.setPrice(25.3);
        product.setName("辣条");
        product.setStock(9);
        product.setDetails("非常好吃的辣条");
        return product;
    }
}