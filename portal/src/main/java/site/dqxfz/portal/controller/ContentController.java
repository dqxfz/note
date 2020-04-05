package site.dqxfz.portal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.service.ContentService;

/**
 * @Description: 处理笔记内容（markdown文件）的增删改查
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@RestController
@RequestMapping("/content")
public class ContentController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping
    public ResponseEntity getContent(String id, String iconCls){
        try {
            String contentHtml = contentService.getContent(id,iconCls);
            return new ResponseEntity(contentHtml, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping
    public ResponseEntity updateContent(Content content){
        try {
            contentService.updateContent(content);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}