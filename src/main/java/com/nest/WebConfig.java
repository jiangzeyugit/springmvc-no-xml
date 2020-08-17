package com.nest;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

/**
 * Web Configuration Class
 *
 * @author jzy
 * @date 2019/12/25
 * 
 */
@Configuration
@ComponentScan(value="com.nest.manage.controller")
@EnableWebMvc
public class WebConfig extends WebMvcConfigurationSupport {

	@Bean
	public FreeMarkerConfigurer freeMarkerConfigurer() {
		FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
		configurer.setDefaultEncoding("utf-8");
		configurer.setTemplateLoaderPath("/WEB-INF/views/");
		Properties properties = new Properties();
		properties.setProperty("defaultEncoding", "UTF-8");
		configurer.setFreemarkerSettings(properties);
		return configurer;
	}

	@Bean
	public FreeMarkerViewResolver freeMarkerViewResolver() {
		FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
		resolver.setSuffix(".ftl");
		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setCache(false);
		resolver.setExposeRequestAttributes(true);
		resolver.setRequestContextAttribute("request");
		resolver.setOrder(0);
		return resolver;
	}
	
	  /**
     * multipart 配置表单上传
     * @return
     */
    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(50 * 1024 * 1024l);  //50m
        return multipartResolver;
    }

}