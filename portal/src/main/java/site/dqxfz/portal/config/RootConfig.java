package site.dqxfz.portal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * @author WENG Yang
 * @date 2020年04月11日
 **/
@ComponentScan({
        "site.dqxfz.portal.service",
        "site.dqxfz.portal.dao",
        "site.dqxfz.portal.config.common"})
@PropertySource(value = {"classpath:properties/config.properties"}, encoding = "utf-8")
public class RootConfig {
    public @Bean RestTemplate restTemplate(){
        return new RestTemplate();
    }
}