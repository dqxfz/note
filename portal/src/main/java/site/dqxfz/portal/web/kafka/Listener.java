package site.dqxfz.portal.web.kafka;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author Weng Yang
 * @date 06/12/2020
 **/
@Component
public class Listener {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @KafkaListener(id = "foo01", topics = "test")
    public void listen1(String foo) {
        logger.info(foo);
    }
}