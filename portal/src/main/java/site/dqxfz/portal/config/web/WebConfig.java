package site.dqxfz.portal.config.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import site.dqxfz.portal.converter.IconClsConverter;
import site.dqxfz.portal.interceptor.LoginInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description: 配置DispatcherServlet的WebApplicationContext（子容器），通常管理三层架构的视图层view
 * @Author wengyang
 * @Date 2020年04月01日
 **/
@EnableWebMvc
@ComponentScan({
        "site.dqxfz.portal.controller",
        "site.dqxfz.portal.config.web",
        "site.dqxfz.portal.interceptor"})
@PropertySource({"classpath:properties/config.properties"})
public class WebConfig implements WebMvcConfigurer {
    private final LoginInterceptor loginInterceptor;

    public WebConfig(LoginInterceptor loginInterceptor) {
        this.loginInterceptor = loginInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:static/ui/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new IconClsConverter());
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(mappingJackson2HttpMessageConverter);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Ant path匹配规则
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**/*.do","/html/**");
    }
}