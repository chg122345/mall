package org.jleopard.mall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.jleopard.Msg;
import org.jleopard.PageTable;
import org.jleopard.mall.dao.CategoryMapper;
import org.jleopard.mall.model.Category;
import org.jleopard.mall.model.Product;
import org.jleopard.mall.model.ProductKey;
import org.jleopard.mall.service.ProductService;
import org.jleopard.mall.util.UploadFileHelper;
import org.jleopard.util.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.jleopard.ResultKeys.CATEGORY;
import static org.jleopard.ResultKeys.PRODUCT;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-30  上午8:39
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@RestController
@Log4j
@RequestMapping("/a")
public class ProductController extends BaseController {

    @Autowired
    ProductService productService;

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 管理员角色访问 admin下的页面
     * @param page
     * @return
     */
    @RequiresAuthentication
    @RequiresRoles(value = {"admin"})
    @GetMapping("/{page}")
    public ModelAndView load(@PathVariable("page") String page){

        return getModelAndView("admin/" + page);
    }

    /**
     * 管理员添加商品页面
     * @return
     */
    @RequiresAuthentication
    @RequiresRoles(value = {"admin"})
    @GetMapping("/productAdd")
    public ModelAndView addProductPage(){
        List<Category> categoryList = categoryMapper.selectAll();
        return getModelAndView("admin/productAdd").addObject(CATEGORY,categoryList);
    }

    /**
     * 展示商品
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/product")
    public PageTable getProduct(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "limit", defaultValue = "20") Integer pageSize){
        PageHelper.startPage(page,pageSize);
        List<Product> products = productService.select();
        PageInfo<Product> productPageInfo = new PageInfo<>(products);
        return PageTable.success().count(productPageInfo.getTotal()).put(products);
    }

    /**
     * 获取详情信息
     * @param id
     * @return
     */
    @Deprecated
    @GetMapping("/product/{id}")
    public Msg getProductById(@PathVariable("id") String id){
        ProductKey key = new ProductKey();
        key.setId(id);
        List<Product> products = productService.selectByIds(key);
        if (!CollectionUtils.isEmpty(products)){
            return Msg.success().put(PRODUCT,products.get(0));
        }
        return Msg.fail();
    }

    /**
     * 添加商品
     * @param product
     * @return
     */
    @RequiresAuthentication
    @RequiresRoles(value = {"admin"})
    @PostMapping("/product")
    public Msg addProduct(Product product){
        log.info("产品信息-->" + product);
        if (!StringUtils.isEmpty(product.getId())){
            product = productService.updateByIdSelective(product);
            if (product != null){
                return Msg.msg(200,"更新成功.");
            }
        }else {
            product = productService.insert(product);
            if (product != null){
                return Msg.success();
            }
        }

        return Msg.fail();
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @RequiresAuthentication
    @RequiresRoles(value = {"admin"})
    @DeleteMapping("/product")
    @Transactional
    public Msg deleteProduct(@RequestBody String ids){
        log.info("id信息-->" + ids);
        String[] id = ids.split("-");
        try {
            Arrays.stream(id).forEach(i -> {
                ProductKey productKey = new ProductKey();
                productKey.setId(i);
                productService.deleteByIds(productKey);
            });
        } catch (Exception e){
            log.error("批量删除产品出错.", e);
            return Msg.fail();
        }
        return Msg.success();
    }

    /**
     * 上传商品图片
     * @param file
     * @return
     */
    @RequiresAuthentication
    @RequiresRoles(value = {"admin"})
    @PostMapping("/upload")
    private Msg upload(MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String uri = UploadFileHelper.upload("product/", file);
                return Msg.success().put("uri",uri);
            } catch (IOException e) {
                log.error("上传图片出错了..", e);
            }
        }
        return Msg.fail();
    }
}
