package site.dqxfz.portal.config;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoClientFactoryBean;

/**
 * @Description: 配置根WebApplicationContext（父容器）,通常管理三层架构的服务层Service和持久层Dao（所有子容器共享Service和Dao）
 * @Author wengyang
 * @Date 2020年04月01日
 **/
@ComponentScan({"site.dqxfz.portal.dao","site.dqxfz.portal.service"})
public class RootConfig {
    public @Bean MongoTemplate mongoTemplate() {
        return new MongoTemplate(MongoClients.create("mongodb://localhost"),"note");
    }
}