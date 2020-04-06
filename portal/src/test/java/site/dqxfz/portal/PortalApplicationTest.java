package site.dqxfz.portal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.Resource;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.dqxfz.portal.config.RootConfig;
import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.service.ContentService;
import site.dqxfz.portal.service.impl.ContentServiceImpl;

import java.io.*;
import java.util.UUID;

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
    /**
     * 初始化数据库
     */
    @Test
    public void test01() throws IOException {
        Portfolio portfolio = mongoOperations.insert(new Portfolio("我的文件夹", null, IconClsType.FOLDER,"wy"));
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
        Resource template = ac.getResource("classpath:static/template/markdown.html");
        InputStream inputStream = template.getInputStream();
        InputStreamReader in = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(in);
        StringBuffer buffer = new StringBuffer();
        String line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        inputStream.close();
        in.close();
        reader.close();
        logger.info(buffer.toString());
    }
    @Test
    public void test04(){
    }

    private  <S,T> T convert(S obj,Converter<S, T> converter) {
        return converter.convert(obj);
    }

    @Test
    public void test05(){
        String uuid = UUID.randomUUID().toString();
        System.out.println(uuid);
    }

}