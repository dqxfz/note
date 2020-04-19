package site.dqxfz.sso.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.sso.pojo.po.User;
import site.dqxfz.sso.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * @author WENG Yang
 * @date 2020年04月10日
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    /**
     * SQL执行异常中，键重复异常的关键词
     */
    private final String DUPLICATE_KEY_WORD = "Duplicate";
    /**
     * 重复键的索引名
     */
    private final String USER_INDEX_NAME = "user.PRIMARY";

    private final Logger logger = LogManager.getLogger(this.getClass());
    @Value("${page.login.url}")
    private String pageLoginUrl;
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/isLogin")
    public void isLogin(String serviceUrl, String serviceTicketUrl, HttpServletRequest request, HttpServletResponse response){
        try {
            String  redirectUrl = userService.isLogin(request, serviceUrl, serviceTicketUrl);
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }
    @GetMapping("/info")
    public ResponseEntity sessionId(String serviceTicket) {
        try {
            String username = userService.getUserName(serviceTicket);
            return new ResponseEntity(username, HttpStatus.OK);
        } catch ( Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/login")
    public ResponseEntity login(HttpServletResponse response, User user) {
        try{
            String serviceTicket = userService.login(response, user);
            return new ResponseEntity(serviceTicket, serviceTicket == null ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK);
        } catch ( Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @PostMapping("/register")
    public ResponseEntity register(User user) {
        try{
            userService.register(user);
            return new ResponseEntity(HttpStatus.OK);
        } catch ( Exception e) {
            String errorMsg = e.getMessage();
            logger.error(errorMsg, e);
            if(errorMsg.contains(DUPLICATE_KEY_WORD)) {
                if(errorMsg.contains(USER_INDEX_NAME)){
                    return new ResponseEntity("账号已存在", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, String userName) {
        try{
            userService.logout(request, response, userName);
        } catch ( Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}