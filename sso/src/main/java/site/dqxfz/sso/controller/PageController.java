package site.dqxfz.sso.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.sso.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author WENG Yang
 * @date 2020年04月12日
 **/
@RestController
@RequestMapping("/page")
public class PageController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Value("${page.login.url}")
    private String pageLoginUrl;
    private final UserService userService;

    public PageController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response, String service){
        try {
            String  serviceTicket = userService.isLogin(request);
            String redirectUrl = pageLoginUrl + "?service=" + service;
            if(!StringUtils.isEmpty(serviceTicket)) {
                redirectUrl = service + "?serviceTicket=" + serviceTicket;
            }
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
