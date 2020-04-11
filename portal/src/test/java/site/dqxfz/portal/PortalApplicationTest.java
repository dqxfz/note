package site.dqxfz.portal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.dqxfz.common.util.ResourceUtils;
import site.dqxfz.portal.config.RootConfig;
import site.dqxfz.portal.config.web.WebConfig;
import site.dqxfz.portal.constant.CommandEnum;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.impl.ContentServiceImpl;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    ContentServiceImpl contentService;

    @Autowired
    RedisTemplate<String, User> redisTemplate;
    /**
     * 初始化数据库
     */
    @Test
    public void test01() throws IOException {
        Portfolio portfolio = mongoOperations.insert(new Portfolio("我的文件夹", null, IconClsEnum.FOLDER,"wy"));
        logger.info(portfolio);

    }
    @Test
    public void test02(){
        Portfolio portfolio = mongoOperations.findById("5e8822212b33d81356b974c9", Portfolio.class);
        logger.info(portfolio);
//        logger.info(portfolio.getType() == PortfolioType.NOTEFILE);
//        logger.info(portfolio.getType().equals(PortfolioType.NOTEFILE));
    }
    @Test
    public void test03() throws IOException {
        String sourcePath = "static/template/video01.html";
        String resource = ResourceUtils.getResource(ac, sourcePath);
        logger.info(resource);
    }
    @Test
    public void test04() throws InterruptedException {
        Instant start = Instant.now();
        TimeUnit.SECONDS.sleep(2);
        TimeUnit.NANOSECONDS.sleep(100);
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        logger.info(duration.toMillis());
        logger.info(duration.toNanos());
//        int nano = duration.getNano();
//        logger.info(nano);
    }

    @Test
    public void test05(){
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
    }
    @Test
    public void test06() throws JsonProcessingException {
        Map<String,Object> map = new HashMap(2);
        map.put("responseCode", CommandEnum.RESPONSE_CONTINUE);
        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(map);
        logger.info(response);
    }
    @Test
    public void test07(){
        redisTemplate.boundValueOps("user").set(new User("1","2","3"));
    }
    @Test
    public void test08(){
        User user = redisTemplate.boundValueOps("user").get();
        logger.info(user);
    }

}