package org.jleopard.mall.controller;

import org.jleopard.mall.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping("/out")
    public String out(){
        System.out.println("执行--->" +userService.say("GXF"));
        return "shuchude data";
    }
}
