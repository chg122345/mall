package org.jleopard.mall.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.jleopard.Msg;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-26  上午10:42
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Slf4j
public class GlobalExceptionResolver implements HandlerExceptionResolver {

   // @ResponseBody
    @Override
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView mv;
        //进行异常判断。如果捕获异常请求跳转。
        if(e instanceof UnauthorizedException){
            mv = new ModelAndView("/login");
            return mv;
        }else {
            mv = new ModelAndView();
            log.error("", e);
           mv.addObject(Msg.msg("服务器异常"));
            return mv;

        }

    }
}
