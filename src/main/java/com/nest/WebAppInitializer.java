package com.nest;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
 
 /**
  * 
  * 初始化配置 相当于配置web.xml
  * @author jzy
  *
  **/
public class WebAppInitializer implements WebApplicationInitializer {
	
	    @Override
	    public void onStartup(ServletContext servletContext) throws ServletException {
        /**
         * 
         * Load Spring web application configuration
         *
         */
    	AnnotationConfigWebApplicationContext cont=new AnnotationConfigWebApplicationContext();
    	cont.register(AppConfig.class);
    	cont.setServletContext(servletContext);
    	cont.refresh();
    	
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebConfig.class);//注册web mvc配置文件
        ctx.setParent(cont);
        cont.refresh();
  
        /**
         * 配置控制转发
         */
        Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));//设置DispatcherServlet
        servlet.addMapping("/*");//url-pattern
        servlet.setLoadOnStartup(1);//开机启动
        
 
        /**
         * 配置转码
         */
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);//ǿ��
        javax.servlet.FilterRegistration.Dynamic filter = servletContext.addFilter("encoding", characterEncodingFilter);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class),true,"*");
        
    }
}
