package com.example.GenAI.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
    	/*
    	 * ViewControllerRegistry allows to create 
    	 * simple automated controllers pre-configured 
    	 * with status code and/or a view
    	 */
        registry.addViewController("/").setViewName("auth/signin");
    }
}