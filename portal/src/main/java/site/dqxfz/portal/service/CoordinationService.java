package site.dqxfz.portal.service;

import org.springframework.web.socket.WebSocketSession;
import site.dqxfz.portal.pojo.dto.NoteText;
import site.dqxfz.portal.pojo.dto.Principal;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import java.util.List;
import java.util.Map;

/**
 * @author WENG Yang
 * @date 2020年04月20日
 **/
public interface CoordinationService {
    String setCoordination(String id, String[] userNames);

    List<EasyUiTreeNode> getChildren(String id);

    void startCoordinate(WebSocketSession session, Principal principal, Map<String, String> textMap, Map<String, List<WebSocketSession>> sessionMap) throws Exception;

    void publishText(WebSocketSession session, NoteText noteText, Map<String, String> textMap, Map<String, List<WebSocketSession>> sessionMap) throws Exception;
}
