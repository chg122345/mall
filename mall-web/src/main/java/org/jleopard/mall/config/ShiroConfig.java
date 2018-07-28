package org.jleopard.mall.config;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.jleopard.mall.shiro.ShiroPermFilter;
import org.jleopard.mall.shiro.ShiroRealmImpl;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-26  上午9:56
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Configuration
public class ShiroConfig {


    @Bean(name = "ShiroRealmImpl")
    public ShiroRealmImpl getShiroRealm() {
        ShiroRealmImpl shiroRealm = new ShiroRealmImpl();
      //  shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    @Bean(name = "shiroEhcacheManager")
    public EhCacheManager getEhCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManagerConfigFile("classpath:shiro/ehcache-shiro.xml");
        return em;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
        return authorizationAttributeSourceAdvisor;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
        defaultWebSecurityManager.setSessionManager(defaultWebSessionManager());
        defaultWebSecurityManager.setRememberMeManager(cookieRememberMeManager());
        defaultWebSecurityManager.setRealm(getShiroRealm());
        return defaultWebSecurityManager;
    }

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
        shiroFilterFactoryBean.setLoginUrl("/login");
        shiroFilterFactoryBean.setSuccessUrl("/index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
       /* Map filtersMap = new HashMap<>();
        filtersMap.put("authc",getShiroPermFilter());
        shiroFilterFactoryBean.setFilters(filtersMap);*/

        Map chainMap = new LinkedHashMap<>();
        chainMap.put("/static/**","anon");
        chainMap.put("/favicon.ico", "anon");
        chainMap.put("/logout","logout");
       // chainMap.put("/index","user");
        chainMap.put("/**","anon");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(chainMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public ShiroPermFilter getShiroPermFilter(){
        return new ShiroPermFilter();
    }
    /**
     * 凭证匹配器
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5"); //散列算法
        hashedCredentialsMatcher.setHashIterations(1); //散列次数
        return hashedCredentialsMatcher;
    }


    @Bean
    public SimpleCookie simpleCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        simpleCookie.setMaxAge(2592000);
        return simpleCookie;
    }

    /**
     * rememberMeManager管理器，写cookie，取出cookie生成用户信息
     */
    @Bean
    public CookieRememberMeManager cookieRememberMeManager() {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
        cookieRememberMeManager.setCookie(simpleCookie());
        return cookieRememberMeManager;
    }

    /**
     * 会话管理器
     */
    @Bean
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
        defaultWebSessionManager.setGlobalSessionTimeout(600000);
        defaultWebSessionManager.setDeleteInvalidSessions(true);
        return defaultWebSessionManager;
    }

    @Bean
   public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator(){
       DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
       advisorAutoProxyCreator.setProxyTargetClass(true);
       return advisorAutoProxyCreator;
   }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }


    /**
     * 注册全局异常处理
     * @return
     */
    @Bean(name = "exceptionHandler")
    public HandlerExceptionResolver handlerExceptionResolver(){
        return new GlobalExceptionResolver();
    }

}
