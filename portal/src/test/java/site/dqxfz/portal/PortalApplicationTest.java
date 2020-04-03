package site.dqxfz.portal;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.dqxfz.portal.config.RootConfig;
import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.constant.PortfolioType;
import site.dqxfz.portal.pojo.Portfolio;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
public class PortalApplicationTest {
    Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    ApplicationContext ac;
    /**
     * 初始化数据库
     */
    @Test
    public void test01(){
        Portfolio portfolio = new Portfolio("我的文件夹", PortfolioType.FOLDER, IconClsType.FOLDER,"wy");
        logger.info(portfolio);
        mongoOperations.insert(portfolio);
    }
    @Test
    public void test02(){
        Portfolio portfolio = mongoOperations.findById("5e86e97562246e1cc378e467", Portfolio.class);
        logger.info(portfolio);
//        logger.info(portfolio.getType() == PortfolioType.NOTEFILE);
//        logger.info(portfolio.getType().equals(PortfolioType.NOTEFILE));
    }
    @Test
    public void test03(){
//        logger.info(HttpStatus.ACCEPTED.name());
//        String[] beanNamesForType = ac.getBeanNamesForType(HttpMessageConverter.class);
//
//        logger.info(Arrays.asList(beanNamesForType));
//        IconClsType application = IconClsType.valueOf("APPLICATION");
//        IconClsType application1 = Enum.valueOf(IconClsType.class, "APPLICATION");
//        logger.info(application1 == IconClsType.APPLICATION);
    }
    @Test
    public void test04(){
        Converter<Portfolio, Document> converter = portfolio -> {
            Document document = new Document();
            document.put("_id",portfolio.getId());
            document.put("name",portfolio.getName());
            document.put("type",portfolio.getType());
            document.put("iconCls",portfolio.getIconCls());
            document.put("fatherId",portfolio.getFatherId());
            document.put("childIds",portfolio.getChildIds());
            return document;
        };
//        Collections.sort();
        Document convert = convert(new Portfolio("我的文件夹", PortfolioType.FOLDER, IconClsType.FOLDER, "wy"), portfolio -> {
            Document document = new Document();
            document.put("_id",portfolio.getId());
            document.put("name",portfolio.getName());
            document.put("type",portfolio.getType());
            document.put("iconCls",portfolio.getIconCls());
            document.put("fatherId",portfolio.getFatherId());
            document.put("childIds",portfolio.getChildIds());
            return document;
        });
        logger.info(convert);
    }

    private  <S,T> T convert(S obj,Converter<S, T> converter) {
        return converter.convert(obj);
    }


}