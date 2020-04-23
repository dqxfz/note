package site.dqxfz.portal.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author WENG Yang
 * @date 2020年04月20日
 **/
@Service
public class CoordinationServiceImpl implements CoordinationService {
    private final Logger logger = LogManager.getLogger(this.getClass());
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
        HashSet<String> userNameSet = new HashSet<>();
        List<User> users = new ArrayList<>();
        for(String userName : userNames) {
            userNameSet.add(userName);
        }
        for(String userName : userNameSet) {
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
        portfolioDao.updateCoordinationNumById(id, users.size());
        for(User user : users) {
            List<Portfolio> portfolios = portfolioDao.listByFatherId(user.getPortfolioId());
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
    public void dealPrincipal(WebSocketSession session, Principal principal, Map<String, String> textMap, Map<String, HashSet<WebSocketSession>> sessionMap) throws Exception {
        Map<String, Object> sessionAttributes = session.getAttributes();
        String id = principal.getId();
        switch (CommandEnum.getValueOf(principal.getType())) {
            case COORDINATION_TYPE_ENTER: {
                logger.info(principal.getUserName() + "开始编辑" + principal.getId());
                sessionAttributes.put("principal", principal);
                if(!textMap.containsKey(id)) {
                    String text = contentDao.getContentById(id);
                    if(text == null) {
                        text = "";
                    }
                    textMap.put(id, text);
                }
                HashSet<WebSocketSession> set = sessionMap.get(id);
                if(set == null) {
                    set = new HashSet<>();
                    set.add(session);
                    sessionMap.put(id, set);
                } else {
                    set.add(session);
                }
                logger.info("文本" + id + "当前正在编辑人数：" + set.size());
                sendMessage(session, CommandEnum.COORDINATION_RESPONSE_ALL, textMap.get(id));
                break;
            }
            case COORDINATION_TYPE_EXIT: {
                logger.info(principal.getUserName() + "退出编辑" + principal.getId());
                dealExit(id, session, textMap, sessionMap);
                break;
            }
            default:
                break;
        }


    }
    @Override
    public void publishText(WebSocketSession session, NoteText noteText, Map<String, String> textMap, Map<String, HashSet<WebSocketSession>> sessionMap) throws Exception {
        String id = noteText.getId();

        String text = textMap.get(id);
        StringBuilder textBuilder = new StringBuilder(text);
        CommandEnum.getValueOf(noteText.getType());
        switch (CommandEnum.getValueOf(noteText.getType())) {
            case COORDINATION_ADD: {
                textBuilder.insert(noteText.getStart(), noteText.getValue());
                break;
            }
            case COORDINATION_DELETE: {
                // 删除协同文件内容
                textBuilder.delete(noteText.getStart(), noteText.getEnd());
                break;
            }
            case COORDINATION_REPLACE: {
                textBuilder.replace(noteText.getStart(), noteText.getEnd(), noteText.getValue());
                break;
            }
            default:
                break;
        }

        textMap.put(id, textBuilder.toString());
        HashSet<WebSocketSession> set = sessionMap.get(id);
        for (WebSocketSession socketSession : set) {
            if(!socketSession.equals(session)) {
                sendMessage(socketSession, CommandEnum.COORDINATION_RESPONSE_PART, noteText);
            }
        }


    }

    @Override
    public void dealExit(String id, WebSocketSession session, Map<String, String> textMap, Map<String, HashSet<WebSocketSession>> sessionMap) {
        HashSet<WebSocketSession> set = sessionMap.get(id);
        if(set != null) {
            set.remove(session);
            logger.info("文本" + id + "当前正在编辑人数：" + set.size());
            if(set.size() == 0) {
                contentDao.updateContentById(new Content(id,textMap.get(id)));
                logger.info("所有人已经退出协同编辑" + id + "，执行清理");
                textMap.remove(id);
                sessionMap.remove(id);
            }
        }
    }

    @Override
    public void deleteChild(String fatherId, String id) {
        portfolioDao.deleteChild(fatherId, id);
        Portfolio portfolio = portfolioDao.getPortfolioById(id);
        if(portfolio.getCoordinationNum() <= 0) {
            portfolioDao.deleteListByIdList(Arrays.asList(id));
            contentDao.deleteListByIdList(Arrays.asList(id));
        }
    }

}
