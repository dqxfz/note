package site.dqxfz.sso;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.sso.config.AmqpConfig;
import site.dqxfz.sso.constant.AmqpConsts;
import site.dqxfz.sso.pojo.dto.AmqpUser;

/**
 * @author WENG Yang
 * @date 2020年04月19日
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AmqpConfig.class)
public class SsoApplicationTest {
    @Autowired
    AmqpTemplate sendAmqpTemplate;
    @Test
    public void test01() throws JsonProcessingException {
        AmqpUser amqpUser = new AmqpUser("wy", "112");
        sendAmqpTemplate.convertAndSend(AmqpConsts.SESSION_ROUTING_KEY_NAME, JsonUtils.objectToJson(amqpUser));
    }
}
