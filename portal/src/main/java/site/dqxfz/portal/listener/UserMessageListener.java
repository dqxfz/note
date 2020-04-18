package site.dqxfz.portal.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author WENG Yang
 * @date 2020年04月19日
 **/
@Component
public class UserMessageListener implements MessageListener {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Override
    public void onMessage(Message message) {
        byte[] body = message.getBody();
        String str = new String(body);
        logger.info(str);
    }
}
