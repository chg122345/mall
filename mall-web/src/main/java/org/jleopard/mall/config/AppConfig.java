package org.jleopard.mall.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.jleopard.util.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018/7/20
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "org.jleopard.mall.controller" )
@EnableAspectJAutoProxy
public class AppConfig extends WebMvcConfigurerAdapter implements WebMvcConfigurer {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        //addResourceHandler是指你想在url请求的路径

        //addResourceLocations是图片存放的真实路径

        registry.addResourceHandler("/avatar/**","/icon/**").addResourceLocations("file:"+ PathUtils.getUploadImgBasePath());
        super.addResourceHandlers(registry);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /*
     * STEP 1 - Create SpringResourceTemplateResolver
     * */
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    /*
     * STEP 2 - Create SpringTemplateEngine
     * */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        // 整合shiro
        Set<IDialect> additionalDialects = new HashSet<IDialect>();
        additionalDialects.add(new ShiroDialect());
        templateEngine.setAdditionalDialects(additionalDialects);
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    /*
     * STEP 3 - Register ThymeleafViewResolver
     * */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public ResourceHttpRequestHandler uploadHandler() {

        ResourceHttpRequestHandler font = new ResourceHttpRequestHandler();

        List<Resource> locations = new ArrayList<>();

        ServletContextResource scr = new ServletContextResource(servletContext,"/uploads/");

        locations.add(scr);

        font.setLocations(locations);

        return font;

    }

    @Bean
    public ResourceHttpRequestHandler staticHandler() {

        ResourceHttpRequestHandler staticHandler = new ResourceHttpRequestHandler();

        List<Resource> locations = new ArrayList<>();
        ServletContextResource scr = new ServletContextResource(servletContext,"/static/");
        locations.add(scr);

        staticHandler.setLocations(locations);

        return staticHandler;

    }

    @Bean
    public SimpleUrlHandlerMapping handlerMapping() {

        SimpleUrlHandlerMapping shm = new SimpleUrlHandlerMapping();

        Properties properties = new Properties();
       properties.setProperty("/static/**","staticHandler");
        properties.setProperty("/uploads/**", "uploadHandler");

        shm.setMappings(properties);

        return shm;

    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(30*1024*1024);
        multipartResolver.setMaxInMemorySize(10*1024*1024);
        return multipartResolver;
    }

   /* @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {

        SimpleMappingExceptionResolver smer = new SimpleMappingExceptionResolver();
        Properties prop = new Properties();
        prop.setProperty("org.apache.shiro.ShiroException","/403");
        smer.setExceptionMappings(prop);
        smer.setDefaultErrorView("/404");
        return smer;

    }*/

    @Bean
    public RestTemplate restTemplate(){

        RestTemplate rt=new RestTemplate();

        rt.getMessageConverters().add(new org.springframework.http.converter.FormHttpMessageConverter());

        return rt;
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping(){

        RequestMappingHandlerMapping rmhm = new RequestMappingHandlerMapping();

        //interceptor
       /* Object[] interceptors = {new TokenInterceptor()};
        rmhm.setInterceptors(interceptors);*/ //TODO
        return rmhm;
    }

    @Bean
    public HttpPutFormContentFilter httpPutFormContentFilter() {
        return new HttpPutFormContentFilter();
    }


}
