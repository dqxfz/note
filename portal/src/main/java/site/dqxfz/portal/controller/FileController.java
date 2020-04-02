package site.dqxfz.portal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 处理文件的上传下载删除等操作
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@RestController
public class FileController {
    @GetMapping("/index")
    public String index(){
        return "Hello World!";
    }
}