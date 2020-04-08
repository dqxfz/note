package site.dqxfz.portal.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.util.JsonUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import site.dqxfz.portal.constant.CommandType;
import site.dqxfz.portal.pojo.dto.FileDTO;
import site.dqxfz.portal.pojo.dto.NoteFile;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.FileService;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WENG Yang
 * @date 2020年04月08日
 **/
@Component
public class WebsocketFileHandler extends AbstractWebSocketHandler {
    Logger logger = LogManager.getLogger(this.getClass());
    private final FileService fileService;

    public WebsocketFileHandler(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        FileDTO fileDTO = mapper.readValue(message.getPayload(), FileDTO.class);
        if(fileDTO != null) {
            Map<String, Object> sessionAttributes = session.getAttributes();
            switch (CommandType.getValueOf(fileDTO.getCommand())) {
                case UPLOAD_START: {
                    NoteFile noteFile = mapper.readValue(fileDTO.getData(), NoteFile.class);
                    sessionAttributes.put("noteFile",noteFile);
                    fileService.createFile(noteFile,sessionAttributes);
                    break;
                }
                case UPLOAD_COMPLETE: {
                    EasyUiTreeNode easyUiTreeNode = fileService.saveFileMetaData(sessionAttributes);
                    sendMessage(session, CommandType.RESPONSE_COMPLETE, easyUiTreeNode);
                    break;
                }
            }
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        try{
            fileService.uploadFile(message.getPayload().array(),session.getAttributes());
            sendMessage(session,CommandType.RESPONSE_CONTINUE,null);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }
    private void sendMessage(WebSocketSession session, CommandType commandType, Object data) throws Exception {
        Map<String,Object> map = new HashMap(2);
        map.put("command", commandType.getValue());
        map.put("data", data);
        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(map);
        session.sendMessage(new TextMessage(response));
    }
}