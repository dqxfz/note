package site.dqxfz.portal.websocket;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.BinaryMessage;
import site.dqxfz.portal.pojo.dto.NoteFile;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.FileService;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @Description: 处理文件的上传下载删除等操作
 * @Author wengyang
 * @Date 2020年04月02日
 **/
//@Controller
public class StompFileHandler {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final FileService fileService;
    private final SimpMessagingTemplate template;

    public StompFileHandler(FileService fileService, SimpMessagingTemplate template) {
        this.fileService = fileService;
        this.template = template;
    }

    private void send(String destination, HttpStatus status, Object data) {
        template.convertAndSend(destination,new ResponseEntity(data, status));
    }

    @MessageMapping("/name")
    public void handleName(String name){
        logger.info("name: " + name);
    }

    @MessageMapping("/data")
    public void handleData(ByteBuffer data) throws IOException {
//        File file = new File("/home/wy/Mine/temp/file/md");
//        if(!file.exists()) {
//            // 创建文件
//            file.createNewFile();
//        }
//        FileOutputStream outputStream = new FileOutputStream(file, true);
//        outputStream.write(data.getBytes());
//        outputStream.close();
        logger.info("data: " + data);
    }


    @MessageMapping("/file")
    public void uploadFileContent(NoteFile noteFile) {
//        try {
//            if(!noteFile.isComplete()) {
//                send("/topic/file", HttpStatus.OK, noteFile.getSnippetNum() + 1);
//            }
//            Object data = fileService.uploadFileContent(noteFile);
//            if(data instanceof EasyUiTreeNode) {
//                send("/topic/file", HttpStatus.OK, data);
//            }
//        } catch (Exception e) {
//            logger.error(e.getMessage(),e);
//            send("/topic/file", HttpStatus.INTERNAL_SERVER_ERROR, null);
//        }
    }
}