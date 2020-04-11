package site.dqxfz.portal.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author WENG Yang
 * @date 2020年04月11日
 **/
@ComponentScan({
        "site.dqxfz.portal.service",
        "site.dqxfz.portal.dao",
        "site.dqxfz.portal.config.common"})
@PropertySource({"classpath:properties/config.properties"})
public class RootConfig {
}