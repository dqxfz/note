package site.dqxfz.portal.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import site.dqxfz.portal.constant.CommandEnum;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.constant.ResponseConsts;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.dto.NoteText;
import site.dqxfz.portal.pojo.dto.Principal;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.CoordinationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WENG Yang
 * @date 2020年04月20日
 **/
@Service
public class CoordinationServiceImpl implements CoordinationService {
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ContentDao contentDao;

    public CoordinationServiceImpl(UserDao userDao, PortfolioDao portfolioDao, ContentDao contentDao) {
        this.userDao = userDao;
        this.portfolioDao = portfolioDao;
        this.contentDao = contentDao;
    }

    @Override
    public String setCoordination(String id, String[] userNames) {
        List<User> users = new ArrayList<>();
        for(String userName : userNames) {
            User user = userDao.getUserByUserName(userName);
            if (user != null) {
                users.add(user);
            }
        }
        if(users.size() <= 1) {
            return ResponseConsts.COORDINATION_USER_LESS_TWO;
        }
        // 设置协同文件的fatherId为null
        portfolioDao.updateFatherIdById(id, null);
        for(User user : users) {
            List<Portfolio> portfolios = portfolioDao.listByFatherId(user.getPofolioId());
            // 查找用户的协同文件夹对象
            for (Portfolio portfolio : portfolios) {
                if (portfolio.getIconCls().equals(IconClsEnum.COORDINATION)) {
                    portfolioDao.addChild(portfolio.getId(), id);
                    break;
                }
            }
        }
        return null;
    }

    @Override
    public List<EasyUiTreeNode> getChildren(String id) {
        Portfolio coordinationPortfolio = portfolioDao.getPortfolioById(id);
        List<Portfolio> portfolios = portfolioDao.listByIdList(coordinationPortfolio.getChildList());
        List<EasyUiTreeNode> nodes = portfolios.stream()
                .map(portfolio -> new EasyUiTreeNode(
                        portfolio.getId(),
                        portfolio.getName(),
                        "open",
                        portfolio.getIconCls(),
                        portfolio.getFatherId())
                )
                .collect(Collectors.toList());
        return nodes;
    }
    private void sendMessage(WebSocketSession session, CommandEnum command, Object data) throws Exception {
        Map<String,Object> map = new HashMap(2);
        map.put("command", command.getValue());
        map.put("data", data);
        ObjectMapper mapper = new ObjectMapper();
        String response = mapper.writeValueAsString(map);
        session.sendMessage(new TextMessage(response));
    }
    @Override
    public void startCoordinate(WebSocketSession session, Principal principal, Map<String, String> textMap, Map<String, List<WebSocketSession>> sessionMap) throws Exception {
        Map<String, Object> sessionAttributes = session.getAttributes();
        sessionAttributes.put("principal", principal);
        String id = principal.getId();
        if(!textMap.containsKey(id)) {
            String text = contentDao.getContentById(id);
            textMap.put(id, text);
        }
        if(!sessionMap.containsKey(id)) {
            List<WebSocketSession> list = new ArrayList<>();
            list.add(session);
            sessionMap.put(id, list);
        } else {
            List<WebSocketSession> list = sessionMap.get(id);
            list.add(session);
        }
        sendMessage(session, CommandEnum.COORDINATION_RESPONSE_ALL, textMap.get(id));

    }
    @Override
    public void publishText(WebSocketSession session, NoteText noteText, Map<String, String> textMap, Map<String, List<WebSocketSession>> sessionMap) throws Exception {
        String id = noteText.getId();

        String text = textMap.get(id);
        StringBuilder textBuilder = new StringBuilder(text);
        if(CommandEnum.COORDINATION_ADD.getValue().equals(noteText.getType())) {
            // 向协同文件添加内容
            textBuilder.insert(noteText.getStart(), noteText.getValue());
        } else {
            // 删除协同文件内容
            textBuilder.delete(noteText.getStart(), noteText.getEnd());
        }
        textMap.put(id, textBuilder.toString());

        List<WebSocketSession> list = sessionMap.get(id);
        for (WebSocketSession socketSession : list) {
            if(!session.equals(session)) {
                sendMessage(socketSession, CommandEnum.COORDINATION_RESPONSE_PART, noteText);
            }
        }

    }

}
