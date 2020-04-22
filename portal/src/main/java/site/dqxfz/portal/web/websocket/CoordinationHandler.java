package site.dqxfz.portal.web.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.portal.pojo.dto.NoteText;
import site.dqxfz.portal.pojo.dto.Principal;
import site.dqxfz.portal.service.CoordinationService;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author WENG Yang
 * @date 2020年04月20日
 **/
@Component
public class CoordinationHandler extends AbstractWebSocketHandler {
    private final Map<String, List<WebSocketSession>> sessionMap = new ConcurrentHashMap();
    private final Map<String, String> textMap = new ConcurrentHashMap();
    private final Logger logger = LogManager.getLogger(this.getClass());
    private final CoordinationService coordinationService;

    public CoordinationHandler(CoordinationService coordinationService) {
        this.coordinationService = coordinationService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        NoteText noteText = JsonUtils.jsonToObject(payload, NoteText.class);
        if(noteText != null) {
            coordinationService.publishText(session, noteText, textMap, sessionMap);
            return;
        }
        Principal principal = JsonUtils.jsonToObject(payload, Principal.class);
        if(principal != null) {
            coordinationService.startCoordinate(session, principal, textMap, sessionMap);
        }
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        logger.info(message.getPayload());
        super.handleBinaryMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }
}