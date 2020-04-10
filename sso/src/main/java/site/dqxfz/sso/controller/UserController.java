package site.dqxfz.sso.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WENG Yang
 * @date 2020年04月10日
 **/
@RestController
public class UserController {
    @GetMapping("/login")
    public String login(){
        return "Congratulations! Login success!";
    }
}