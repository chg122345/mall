package org.jleopard.mall.shiro;

import lombok.extern.log4j.Log4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.jleopard.mall.model.User;
import org.jleopard.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.jleopard.AttributeKeys.USER_SESSION;

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
        String username = (String) super.getAvailablePrincipal(principalCollection);
        User user = (User) SecurityUtils.getSubject().getSession().getAttribute(USER_SESSION);
        log.info("查询权限用户-->" + user);
        user = user == null ? userService.selectByEmail(username) :user ;
        if (user != null) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            Set<String> roles = new HashSet<>();
            roles.add(user.getRole());
            info.addRoles(roles);
            log.info("权限信息" + info.getRoles());
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
            if (user == null){
                throw new UnknownAccountException("用户不存在");
            }
            if (user.getType() == 2){
                throw new LockedAccountException("账号被锁定");
            }
            if (user.getType() ==3){
                throw new DisabledAccountException("账号被禁用");
            }
            if (!user.getPassword().equals(new String(token.getPassword()))){
                throw new CredentialsException("密码不正确");
            }
            return new SimpleAuthenticationInfo(username, user.getPassword().toCharArray(), username);
        }
        return null;
    }
}
