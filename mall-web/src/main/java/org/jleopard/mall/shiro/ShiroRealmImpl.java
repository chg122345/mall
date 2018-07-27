package org.jleopard.mall.shiro;

import lombok.extern.log4j.Log4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-26  上午10:11
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Component
@Log4j
public class ShiroRealmImpl extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo
            (PrincipalCollection principalCollection) {
        //根据自己的需求编写获取授权信息，这里简化代码获取了用户对应的所有权限
        String username = (String) principalCollection.fromRealm(getName()).iterator().next();
        User user = null;
        if ("".equals(username) || null != username){
            user = userService.selectByEmail(username);
        }
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            info.addRole(user.getRole());
            return info;
            }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo
            (AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        log.info("用户名-->" + username);
        if (username != null && !"".equals(username)) {
            //查询密码
            User user = userService.selectByEmail(username);
            System.out.println("yonghu-----》" + user);
            if (user != null){
                return new SimpleAuthenticationInfo(username, user.getPassword().toCharArray(), username);
            }
        }
        return null;
    }
}
