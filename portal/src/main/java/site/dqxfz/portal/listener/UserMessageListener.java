package site.dqxfz.portal.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.portal.pojo.dto.AmqpUser;
import site.dqxfz.portal.service.UserService;

import java.io.IOException;

/**
 * @author WENG Yang
 * @date 2020年04月19日
 **/
@Component
public class UserMessageListener {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final UserService userService;

    public UserMessageListener(UserService userService) {
        this.userService = userService;
    }

    public void onMessage(String message) {
        try {
            AmqpUser amqpUser = JsonUtils.jsonToObject(message, AmqpUser.class);
            if (amqpUser != null) {
                userService.deleteSession(amqpUser.getUser());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
