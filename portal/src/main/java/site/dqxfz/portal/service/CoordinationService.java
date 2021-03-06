package site.dqxfz.portal.service;

import org.springframework.web.socket.WebSocketSession;
import site.dqxfz.portal.pojo.dto.NoteText;
import site.dqxfz.portal.pojo.dto.Principal;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author WENG Yang
 * @date 2020年04月20日
 **/
public interface CoordinationService {
    String setCoordination(String id, String[] userNames);

    List<EasyUiTreeNode> getChildren(String id);

    void dealPrincipal(WebSocketSession session, Principal principal, Map<String, String> textMap, Map<String, Set<WebSocketSession>> sessionMap) throws Exception;

    void publishText(WebSocketSession session, NoteText noteText, Map<String, String> textMap, Map<String, Set<WebSocketSession>> sessionMap) throws Exception;

    void dealExit(String id, WebSocketSession session, Map<String, String> textMap, Map<String, Set<WebSocketSession>> sessionMap);

    void deleteChild(String fatherId, String id);
}
