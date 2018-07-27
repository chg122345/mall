package org.jleopard.mall.shiro;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-27  上午10:12
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
public class ShiroPermFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) throws Exception {
        Subject subject = getSubject(req, resp);
        String[] permsArray = (String[]) mappedValue;
        if (permsArray == null || permsArray.length == 0) { //没有权限限制
            return true;
        }
        for (int i = 0; i < permsArray.length; i++) {
            //如果是角色，就是subject.hasRole()
            //若当前用户是permsArray中的任何一个，则有权限访问
            if (subject.isPermitted(permsArray[i])) {
                return true;
            }
        }
        return false;
    }

}
