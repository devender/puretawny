package com.gdr.puretawny.rest.webroot;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.gdr.puretawny.rest.config.RestAppCtx;


public class WebApplicationInitializer
        implements org.springframework.web.WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(RestAppCtx.class);
        
        servletContext.addListener(new ContextLoaderListener(applicationContext));
        
        ServletRegistration.Dynamic servlet = servletContext.addServlet("servlet", new DispatcherServlet(applicationContext));
        servlet.addMapping("/");
        servlet.addMapping("*.png");
        servlet.setLoadOnStartup(1);
    }

}
