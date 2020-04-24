package site.dqxfz.portal.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author WENG Yang
 * @date 2020年04月24日
 **/
//@RestController
@Controller
public class PageController {
    @RequestMapping("/")
    public String index(){
        return "portal.html";
//        return "你好";
    }
}