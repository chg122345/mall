package org.jleopard.mall.config;

import lombok.extern.log4j.Log4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.HostUnauthorizedException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

        if ((httpServletRequest.getHeader("accept").contains("application/json") || (httpServletRequest.getHeader("X-Requested-With") != null && httpServletRequest
                .getHeader("X-Requested-With").contains("XMLHttpRequest")))) {
            doException(httpServletResponse, e);
        }
        return new ModelAndView("403").addObject("exception", doException(e));
    }


    private void doException(HttpServletResponse response, Exception e){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        if (e instanceof HostUnauthorizedException || e instanceof UnauthorizedException){
            out.write("{\"code\":"+403+",\"msg\":"+"\"没有访问权限，访问异常\""+"}");
            out.flush();
            out.close();
        }else if (e instanceof UnauthenticatedException || e instanceof AuthorizationException){
            out.write("{\"code\":"+403+",\"msg\":"+"\"没有授权，请重新授权 \""+"}");
            out.flush();
            out.close();
        }else if (e instanceof ShiroException){
            out.write("{\"code\":"+403+",\"msg\":"+"\""+e.getMessage()+"\""+"}");
            out.flush();
            out.close();
        }else {
            out.write("{\"code\":"+401+",\"msg\":"+"\""+e.getMessage()+"\""+"}");
            out.flush();
            out.close();
        }
    }

    private String doException(Exception e){
        if (e instanceof HostUnauthorizedException || e instanceof UnauthorizedException){
            return "没有访问权限，访问异常";
        }else if (e instanceof UnauthenticatedException || e instanceof AuthorizationException){
           return "没有授权，请重新授权";

        }else if (e instanceof ShiroException){
         return "未知错误，请检查你的账号是否安全";

        }else {
           return e.getMessage();

        }
    }
}
