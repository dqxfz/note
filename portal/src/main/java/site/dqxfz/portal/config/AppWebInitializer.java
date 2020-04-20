package site.dqxfz.portal.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.FormContentFilter;
import org.springframework.web.servlet.DispatcherServlet;
import site.dqxfz.portal.config.web.WebConfig;

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
        // 允许通过以'.do'结尾的请求和'/ui/'开头的请求
        dispatcher.addMapping("*.do","/ui/*","/note","/file");
        // 添加FormContentFilter，使spring mvc能够接受put请求和delete请求
        FilterRegistration.Dynamic FormContentFilter = servletContext.addFilter("formContentFilter", new FormContentFilter());
        FormContentFilter.addMappingForUrlPatterns(null,false,"/*");
    }
}