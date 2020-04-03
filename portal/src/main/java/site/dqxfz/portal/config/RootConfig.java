package site.dqxfz.portal.config;

import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import site.dqxfz.portal.pojo.Portfolio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description: 配置根WebApplicationContext（父容器）,通常管理三层架构的服务层Service和持久层Dao（所有子容器共享Service和Dao）
 * @Author wengyang
 * @Date 2020年04月01日
 **/
@ComponentScan({"site.dqxfz.portal.dao","site.dqxfz.portal.service","site.dqxfz.portal.config"})
public class RootConfig {

}