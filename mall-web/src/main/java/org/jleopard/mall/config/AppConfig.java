package org.jleopard.mall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
public class AppConfig implements WebMvcConfigurer {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ApplicationContext applicationContext;

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

    @Bean
    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {

        SimpleMappingExceptionResolver smer = new SimpleMappingExceptionResolver();

        return smer;

    }

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
}
