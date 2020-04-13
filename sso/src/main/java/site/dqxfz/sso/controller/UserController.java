package site.dqxfz.sso.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.sso.pojo.po.User;
import site.dqxfz.sso.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WENG Yang
 * @date 2020年04月10日
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, User user) {
        try{
            userService.logout(request,response, user);
        } catch ( Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}