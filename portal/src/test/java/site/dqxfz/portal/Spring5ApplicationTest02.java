package site.dqxfz.portal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.dqxfz.portal.config.common.KafkaConfig;
import site.dqxfz.portal.web.kafka.Listener;

import java.util.concurrent.TimeUnit;

/**
 * @author Weng Yang
 * @date 06/12/2020
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = KafkaConfig.class)
public class Spring5ApplicationTest02 {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Autowired
    private KafkaTemplate<Integer, String> template;

    @Test
    public void test01() throws Exception {
//        ConsumerFactory.Listener
        template.send("test", 0, "{'time':'2020.06.17 19:56:03.488','level':'INFO','class':'site.dqxfz.portal.Spring5ApplicationTest02','line':'36','method':'test02','content':'hello'}");
        template.flush();
//        TimeUnit.SECONDS.sleep(100);
    }
    @Test
    public void test02(){
        logger.info("hello");
    }

}