package site.dqxfz.portal.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.event.LoggingEventListener;
import org.springframework.data.mongodb.core.mapping.event.MongoMappingEvent;
import site.dqxfz.portal.converter.PortfolioReadConverter;
import site.dqxfz.portal.converter.PortfolioWriteConverter;

import java.util.*;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月03日
 **/
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Value("${mongo.url}")
    private String mongoUrl;
    @Value("${mongo.database}")
    private String mongoDatabase;

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoUrl);
    }

    @Override
    protected String getDatabaseName() {
        return mongoDatabase;
    }

    /**
     * 添加自定义的Converter
     * @return
     */
    @Override
    public CustomConversions customConversions() {
        List<Converter<?, ?>> converterList = new ArrayList();
        converterList.add(new PortfolioWriteConverter());
        converterList.add(new PortfolioReadConverter());
        return new MongoCustomConversions(converterList);
    }

}