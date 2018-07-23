package org.jleopard.mall.config;

import org.jleopard.mall.dao.config.DataSourceConfig;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-07-21  上午11:49
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Configuration
@ComponentScan(basePackages = "org.jleopard.mall.service")
@Import({DataSourceConfig.class})
public class RootConfig {

    @Bean
    public BeanNameAutoProxyCreator proxyCreator(){
        BeanNameAutoProxyCreator proxyCreator = new BeanNameAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        proxyCreator.setBeanNames("*Service");
        proxyCreator.setInterceptorNames("transactionInterceptor");
        return proxyCreator;
    }

   /* @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }*/

}
