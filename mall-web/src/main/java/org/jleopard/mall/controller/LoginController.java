package org.jleopard.mall.controller;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.jleopard.Msg;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.UserService;
import org.jleopard.mall.util.MD5Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import static org.jleopard.AttributeKeys.USER_SESSION;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-26  上午10:39
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@RestController
@Log4j
public class LoginController extends BaseController{

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Msg login(HttpSession session, @RequestParam("email") String email
            , @RequestParam("password") String password, @RequestParam("vercode") String vercode){
        if (!vercode.toLowerCase().equals(session.getAttribute("imageCode").toString().toLowerCase())) {
            return Msg.msg("验证码不对");
        }
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()) {
           try {
               UsernamePasswordToken token = new UsernamePasswordToken(email, MD5Helper.md5(password,email));
               token.setRememberMe(true);
               subject.login(token);
               User user = userService.selectByEmail(email);
               SecurityUtils.getSubject().getSession().setAttribute(USER_SESSION, user);
               return Msg.success().put("role", user.getRole());
           }catch (Exception e){
               return Msg.msg(400, e.getMessage());
           }
        }

        return Msg.msg(400,"未知错误");

    }

    /**
     * 退出处理
     * @return
     */
    @RequestMapping("/logout")
    public ModelAndView logout(){
        SecurityUtils.getSubject().logout();
        return redirectModelAndView("/index");
    }
}
