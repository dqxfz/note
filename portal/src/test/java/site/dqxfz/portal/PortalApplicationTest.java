package site.dqxfz.portal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.SendingContext.RunTime;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import site.dqxfz.common.util.FtpUtils;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.common.util.ResourceUtils;
import site.dqxfz.portal.config.RootConfig;
import site.dqxfz.portal.constant.CommandEnum;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.pojo.dto.NoteText;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.impl.ContentServiceImpl;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
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
    ContentDao contentDao;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    SpringTemplateEngine springTemplateEngine;
    @Test
    public void test01() throws IOException {
//        Query query = Query.query(Criteria.where("id").is("12"));
//        Content one = mongoOperations.findOne(query, Content.class);
//        System.out.println("one = " + one);
//        Content content = mongoOperations.findById("12", Content.class);
//        System.out.println("content = " + content);
//        content = contentDao.getContentBytext("12");
//        System.out.println("content01 = " + content);
        mongoOperations.insert(new Content("5ea1840481212e4087ecfd84",""));
//        Portfolio portfolio = mongoOperations.findById("5ea183cc81212e4087ecfd82", Portfolio.class);
//        User user = mongoOperations.findById("wy01", User.class);
//        System.out.println("portfolio = " + portfolio);
//        System.out.println("user = " + user);
    }
    @Test
    public void test02(){
        Content content = mongoOperations.findById("5ea1840481212e4087ecfd84", Content.class);
        System.out.println("content = " + content);
    }
    @Test
    public void test03(){
        stringRedisTemplate.boundValueOps("name").set("wy");
    }
    @Test
    public void test04(){
        String process = springTemplateEngine.process("portal.html", new Context());
        System.out.println(process);
    }
}