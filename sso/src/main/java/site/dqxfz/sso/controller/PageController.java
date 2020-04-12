package site.dqxfz.sso.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import site.dqxfz.sso.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author WENG Yang
 * @date 2020年04月12日
 **/
@Controller
@RequestMapping("/page")
public class PageController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Value("page.login.url")
    private String pageLoginUrl;
    private final UserService userService;

    public PageController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response, String service){
        String  st = userService.getServiceToken(request);
        String redirectUrl = pageLoginUrl + "?service=" + request.getRequestURL();
        if(!StringUtils.isEmpty(st)) {
            redirectUrl = service + "?serviceToken=" + st;
        }
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            logger.error(e);
        }
    }
}
