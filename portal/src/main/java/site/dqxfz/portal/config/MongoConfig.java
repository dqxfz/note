package site.dqxfz.portal.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import site.dqxfz.portal.pojo.Portfolio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月03日
 **/
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {
    @Override
    public MongoClient mongoClient() {
        return MongoClients.create("mongodb://localhost");
    }

    @Override
    protected String getDatabaseName() {
        return "note";
    }

//    @Override
//    public CustomConversions customConversions() {
//        Converter<Portfolio,Document> converter = portfolio -> {
//            Document document = new Document();
//            document.put("_id",portfolio.getId());
//            document.put("name",portfolio.getName());
//            document.put("type",portfolio.getType());
//            document.put("iconCls",portfolio.getIconCls());
//            document.put("fatherId",portfolio.getFatherId());
//            document.put("childIds",portfolio.getChildIds());
//            return document;
//        };
//        List<Converter<?, ?>> converters = Arrays.asList(
//                converter
//        );
//        List<Converter<?, ?>> converterList = new ArrayList<Converter<?, ?>>();
//        converterList.add(converter);
//        return new MongoCustomConversions(converters);
//    }

    public @Bean MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory) {
        return new MongoTemplate(mongoDbFactory);
    }
}