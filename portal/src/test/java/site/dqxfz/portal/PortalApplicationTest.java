package site.dqxfz.portal;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.dqxfz.portal.config.RootConfig;
import site.dqxfz.portal.constant.PortfolioType;
import site.dqxfz.portal.pojo.Portfolio;

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

    /**
     * 初始化数据库
     */
    @Test
    public void test01(){
        Portfolio portfolio = new Portfolio("我的文件夹", PortfolioType.NOTEFILE, "wy");
        logger.info(portfolio);
        mongoOperations.insert(portfolio);
    }
    @Test
    public void test02(){
        Portfolio portfolio = mongoOperations.findById("5e85fa0c120f345719690a9d", Portfolio.class);
        logger.info(portfolio.getType() == PortfolioType.NOTEFILE);
        logger.info(portfolio.getType().equals(PortfolioType.NOTEFILE));
    }
}