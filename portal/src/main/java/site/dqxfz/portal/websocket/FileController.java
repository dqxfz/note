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
public class FileController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final FileService fileService;
    private final SimpMessagingTemplate template;

    public FileController(FileService fileService, SimpMessagingTemplate template) {
        this.fileService = fileService;
        this.template = template;
    }

    private void send(String destination, HttpStatus status, Object data) {
        template.convertAndSend(destination,new ResponseEntity(data, status));
    }


    @MessageMapping("/file")
    public void uploadFileContent(NoteFile noteFile) {
        try {
            Object data = fileService.uploadFileContent(noteFile);
            send("/topic/file", HttpStatus.OK, data);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            send("/topic/file", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }
}