package site.dqxfz.portal.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.PortfolioService;
import site.dqxfz.portal.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WENG Yang
 * @date 2020年04月19日
 **/

@RestController
@RequestMapping("/user")
public class UserController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final UserService userService;
    private final PortfolioService portfolioService;

    public UserController(UserService userService, PortfolioService portfolioService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
    }

    @RequestMapping("/serviceTicket")
    public void serviceTicket(String serviceTicket, String serviceUrl, HttpServletResponse response){
        try {
            userService.validateServiceTicket(serviceTicket, serviceUrl, response);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }
    /**
     * 根据request中的cookie获取user的用户名
     * @param request 前端请求
     * @return 用户信息-用户名
     */
    @GetMapping("/info")
    public ResponseEntity getInfo(HttpServletRequest request) {
        try {
            User user = portfolioService.getRootId(request);
            return new ResponseEntity(user.getUsername(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}