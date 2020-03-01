package com.fukuadiary.community.community.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
//@Configuration这说明这类似于以前的xml
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterception sessionInterception;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(sessionInterception).addPathPatterns("/**");

    }


}
