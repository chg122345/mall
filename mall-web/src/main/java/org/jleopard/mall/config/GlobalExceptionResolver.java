package org.jleopard.mall.config;

import lombok.extern.log4j.Log4j;
import org.apache.shiro.ShiroException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-26  上午10:42
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Log4j
@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {

        String requestType = httpServletRequest.getHeader("X-Requested-With");
        if(e instanceof ShiroException){
            httpServletResponse.setStatus(403);//无权限异常  主要用于ajax请求返回
            httpServletResponse.addHeader("Error-Json", "{\"code\":403,\"msg\":\""+e.getMessage()+"\"}");
            httpServletResponse.setContentType("text/html;charset=utf-8");
            if("XMLHttpRequest".equals(requestType)){
                return new ModelAndView();
            }
            return new ModelAndView("403").addObject("exception" ,e);
        }
        return null;
    }
}
