package site.dqxfz.sso.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.sso.pojo.dto.UserDTO;
import site.dqxfz.sso.pojo.po.User;
import site.dqxfz.sso.service.UserService;

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
    @ResponseBody
    @GetMapping("/info")
    public UserDTO info(String serviceToken) {
        try {
            UserDTO userDTO = userService.getUserDTO(serviceToken);
            return userDTO;
        } catch ( Exception e) {
            logger.error(e);
        }
        return null;
    }
}