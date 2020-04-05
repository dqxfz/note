package site.dqxfz.portal.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import site.dqxfz.portal.pojo.dto.NoteFile;
import site.dqxfz.portal.service.FileService;

/**
 * @Description: 处理文件的上传下载删除等操作
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@Controller
@MessageMapping("/file")
public class FileController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final FileService fileService;
    private final SimpMessagingTemplate template;

    public FileController(FileService fileService, SimpMessagingTemplate template) {
        this.fileService = fileService;
        this.template = template;
    }

    /**
     * 创建文件
     * @param name 文件名
     * @return 生成的唯一文件名，如果失败返回500错误
     */
    @MessageMapping("/name")
    public void createFile(String name){
        try {
            String uuidName = fileService.createFile(name);
            template.convertAndSend("/file/name",new ResponseEntity(uuidName, HttpStatus.OK));
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            template.convertAndSend("/file/name",new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @MessageMapping("/content")
    public void uploadFileContent(NoteFile noteFile) {
        try {
            fileService.uploadFileContent(noteFile);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
}