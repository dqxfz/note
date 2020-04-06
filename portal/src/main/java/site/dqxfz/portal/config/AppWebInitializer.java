package site.dqxfz.portal.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.HttpPutFormContentFilter;
import org.springframework.web.servlet.DispatcherServlet;
import site.dqxfz.portal.config.RootConfig;
import site.dqxfz.portal.config.WebConfig;

import javax.servlet.*;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年03月22日
 **/
public class AppWebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebConfig.class);
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(webContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        // 添加FormContentFilter，使spring mvc能够接受put请求和delete请求
        FilterRegistration.Dynamic FormContentFilter = servletContext.addFilter("formContentFilter", new FormContentFilter());
        FormContentFilter.addMappingForUrlPatterns(null,false,"/*");
        // 添加编码过滤器
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter("characterEncodingFilter", encodingFilter);
        characterEncodingFilter.addMappingForUrlPatterns(null,false,"/*");
    }
}