package org.jleopard.mall.controller;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.jleopard.Msg;
import org.jleopard.mall.model.Order;
import org.jleopard.mall.model.OrderItem;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.OrderService;
import org.jleopard.mall.service.UserService;
import org.jleopard.mall.util.MD5Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @GetMapping("/user/{page}")
    public ModelAndView userSkippage(@PathVariable("page") String page) {
        return  getModelAndView("user/" + page);
    }

    @PostMapping("/register")
    public Msg register(User user){
       if (userService.checkEmail(user.getEmail())){
           return Msg.msg("该邮箱已经注册过,请更换邮箱注册");
       }
       user.setPassword(MD5Helper.md5(user.getPassword(), user.getEmail()));
       User result = userService.insert(user);
       if (result != null){
           return Msg.success().put("info",result);
       }
       return Msg.fail();
    }


}
