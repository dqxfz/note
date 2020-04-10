package site.dqxfz.sso.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import site.dqxfz.sso.interceptor.LoginInterceptor;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @Description: 配置DispatcherServlet的WebApplicationContext
 * @Author wengyang
 * @Date 2020年04月01日
 **/
@EnableWebMvc
@ComponentScan({"site.dqxfz.sso.controller"})
public class WebConfig implements WebMvcConfigurer {
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
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
        converters.add(mappingJackson2HttpMessageConverter);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Ant path匹配规则
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**/*.do","/html/**");
    }
}