package org.jleopard.mall.controller;

import org.jleopard.Msg;
import org.jleopard.mall.model.Product;
import org.jleopard.mall.model.ProductKey;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.ProductService;
import org.jleopard.mall.service.UserService;
import org.jleopard.mall.util.MD5Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.jleopard.ResultKeys.PRODUCT;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018/7/20
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@RestController
public class IndexController extends BaseController{

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    /**
     * 跳转到首页
     * @return
     */
    @GetMapping("/")
    public ModelAndView index() {
        return getModelAndView("index");
    }

    /**
     * 单纯页面跳转
     * @param page
     * @return
     */
    @GetMapping("/{page}")
    public ModelAndView skippage(@PathVariable("page") String page) {
        return  getModelAndView(page);
    }


    /**
     * 跳转商品详情页
     * @param id
     * @return
     */
    @GetMapping("/product/{id}")
    public ModelAndView detail(@PathVariable("id") String id){
        ProductKey key = new ProductKey();
        key.setId(id);
        List<Product> products = productService.selectByIds(key);
        if (!CollectionUtils.isEmpty(products)){
            return getModelAndView("/detail").addObject(PRODUCT,products.get(0));
        }
        return getModelAndView("/detail");
    }

    /**
     * 会员注册
     * @param user
     * @param vercode
     * @return
     */
    @PostMapping("/register")
    public Msg register(User user, @RequestParam("vercode") String vercode){
        if (!vercode.toLowerCase().equals(getSession().getAttribute("imageCode").toString().toLowerCase())) {
            return Msg.msg("验证码不对");
        }
       if (userService.checkEmail(user.getEmail())){
           return Msg.msg("该邮箱已经注册过,请更换邮箱注册");
       }
       user.setPassword(MD5Helper.md5(user.getPassword(), user.getEmail()));
        User result = null;
        try {
            result = userService.insert(user);
            if (result != null){
                return Msg.success().put("info",result);
            }
        } catch (Exception e) {
           return Msg.msg(400,e.getMessage());
        }

       return Msg.msg(400,"未知错误");
    }


}
