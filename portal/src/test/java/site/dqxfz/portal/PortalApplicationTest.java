package site.dqxfz.portal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.dqxfz.common.util.FtpUtils;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.common.util.ResourceUtils;
import site.dqxfz.portal.config.RootConfig;
import site.dqxfz.portal.constant.CommandEnum;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.impl.ContentServiceImpl;

import java.io.*;
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

    @Value("${file.ftp.url}")
    private String fileFtpUrl;
    @Value("${file.ftp.port}")
    private Integer fileFtpPort;
    @Value("${file.ftp.user}")
    String fileFtpUser;
    @Value("${file.ftp.password}")
    String fileFtpPassword;

    @Autowired
    MongoOperations mongoOperations;

    @Autowired
    ApplicationContext ac;

    @Autowired
    ContentServiceImpl contentService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    AmqpTemplate sendAmqpTemplate;

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
    public void test07() throws JsonProcessingException {
        String key = "";
        stringRedisTemplate.boundValueOps(key).set(JsonUtils.objectToJson(new User("1","3")));
    }
    @Test
    public void test08() throws IOException {
        String userJson = stringRedisTemplate.boundValueOps("1dd05fa5-9c20-453f-9811-c08a57ac187e").get();
        User user = JsonUtils.jsonToObject(userJson, User.class);
        logger.info(user);
    }
    @Test
    public void test09(){
        stringRedisTemplate.boundValueOps("name").set("wy",Duration.ofSeconds(10));
    }
    @Test
    public void test10(){
        String name = stringRedisTemplate.boundValueOps("name").get();
        logger.info(name);
    }
    @Test
    public void test11() throws IOException {
        User user1 = new User("1", "3");
        String json = JsonUtils.objectToJson(null);
        logger.info(json);
        User user = JsonUtils.jsonToObject(json, User.class);
        logger.info(user);
    }
    @Test
    public void test12(){
        String sessionId02 = "\"a4b12a15-8e4a-4d54-ba8d-c5cfe1ab3e92\"";
        logger.info(sessionId02);
        String userJson = stringRedisTemplate.boundValueOps(sessionId02).get();
        logger.info(userJson);
    }
    @Test
    public void test13(){
//        sendAmqpTemplate.convertAndSend(AmqpConsts.SESSION_ROUTING_KEY_NAME,"false");
        sendAmqpTemplate.convertAndSend("test","false");
        sendAmqpTemplate.convertAndSend("session","false");
    }
    @Test
    public void test14() throws Exception {
        FtpUtils ftp = new FtpUtils();
        FTPClient ftpClient = ftp.getFTPClient(fileFtpUrl, fileFtpPort, fileFtpUser, fileFtpPassword);
        OutputStream outputStream = ftpClient.appendFileStream("hello.txt");
        String hello_world = new String("Hello World");
        outputStream.write(hello_world.getBytes());
        outputStream.close();
        boolean completePendingCommand = ftpClient.completePendingCommand();
        logger.info(completePendingCommand);
        InputStream inputStream = ftpClient.retrieveFileStream("hello.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        logger.info(line);
    }
    @Test
    public void test15() throws Exception {
        FTPClient ftpClient = FtpUtils.getFTPClient(fileFtpUrl, fileFtpPort, fileFtpUser, fileFtpPassword);
        InputStream inputStream = ftpClient.retrieveFileStream("c5d13159-5291-492c-9e11-14fa75a4d871");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        logger.info(line);
//        String endFileMd5 = DigestUtils.md5DigestAsHex(inputStream);
//        logger.info(endFileMd5);
    }
    @Test
    public void test16() throws Exception {
        FTPClient ftpClient = FtpUtils.getFTPClient(fileFtpUrl, fileFtpPort, fileFtpUser, fileFtpPassword);
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for(FTPFile ftpFile : ftpFiles) {
            logger.info(ftpFile.getName() + "  size: " + ftpFile.getSize());
        }
    }
}