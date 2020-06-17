package site.dqxfz.portal;

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

    @Autowired
    private KafkaTemplate<Integer, String> template;

    @Test
    public void testSimple() throws Exception {
//        ConsumerFactory.Listener
//        template.send("annotated1", 0, "foo");
//        template.flush();
        TimeUnit.SECONDS.sleep(100);
    }

}