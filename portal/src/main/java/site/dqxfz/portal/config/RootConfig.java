package site.dqxfz.portal.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

/**
 * @Description: 配置根WebApplicationContext（父容器）,通常管理三层架构的服务层Service和持久层Dao（所有子容器共享Service和Dao）
 * @Author wengyang
 * @Date 2020年04月01日
 **/
@ComponentScan({"site.dqxfz.portal.dao","site.dqxfz.portal.service","site.dqxfz.portal.config"})
@PropertySource({"classpath:properties/config.properties"})
public class RootConfig {

}