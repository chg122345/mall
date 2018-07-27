package org.jleopard.mall.controller;

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

    @Autowired
    private OrderService orderService;
    /**
     * 单纯页面跳转
     * @param page
     * @return
     */
    @RequestMapping("/{page}")
    public ModelAndView skippage(@PathVariable("page") String page) {

        ModelAndView mv = getModelAndView(page);
        return mv;
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

    @GetMapping(value = "/user/{id}")
    public Msg getUser(@PathVariable("id") String id){
        User u = userService.selectById(id);
        if (u != null){
            return Msg.success().put("user",u);
        }
        return Msg.fail();
    }

    @PostMapping(value = "/order")
    public Msg order(@RequestParam("mlUserId") String uid, @RequestParam("mlAddressId") Integer aid){
        Order order = new Order();
        order.setMlUserId(uid);
        order.setMlAddressId(aid);
        order.setNumber(5);
        order.setMoney(245.5);
        OrderItem o1 = new OrderItem();
        o1.setMlProductId("pdqsa9s8ecusv0cift4xdbndjargtaqs");
        o1.setMoney(56.2);
        o1.setNumber(2);
        OrderItem o2 = new OrderItem();
        o2.setMlProductId("pd0v3ytxuvvnh91ejgwweqhqxxa0ts2z");
        o2.setMoney(99.9);
        o2.setNumber(3);
        List<OrderItem> list = new ArrayList<>();
        list.add(o1);
        list.add(o2);
        order.setOrderItem(list);
        Order u = orderService.insert(order);
        if (u != null){
            return Msg.success().put("order",u);
        }
        return Msg.fail();
    }
}
