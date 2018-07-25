package org.jleopard.mall.controller;

import org.jleopard.Msg;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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
     * 单纯页面跳转
     * @param page
     * @return
     */
    @RequestMapping("/{page}")
    public ModelAndView skippage(@PathVariable("page") String page) {

        ModelAndView mv = getModelAndView(page);
        return mv;
    }

    @PostMapping(value = "/register")
    public Msg register(User user){
        User u = userService.insert(user);
        if (u != null){
            return Msg.success().put("user",u);
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
}
