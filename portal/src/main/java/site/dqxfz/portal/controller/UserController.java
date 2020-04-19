package site.dqxfz.portal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.common.util.CookieUtils;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.common.util.Md5Utils;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

/**
 * @author WENG Yang
 * @date 2020年04月19日
 **/

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/serviceTicket")
    public void serviceTicket(String serviceTicket, String serviceUrl, HttpServletResponse response){
        try {
            userService.validateServiceTicket(serviceTicket, serviceUrl, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
}