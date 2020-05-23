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
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Wengyang
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
        // modify the fatherId of the coordination file to null
        portfolioDao.updateFatherIdById(id, null);
        portfolioDao.updateCoordinationNumById(id, users.size());
        for(User user : users) {
            List<Portfolio> portfolios = portfolioDao.listByFatherId(user.getPortfolioId());
            // find the coordination file object of user
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
    public void dealPrincipal(WebSocketSession session, Principal principal, Map<String, String> textMap, Map<String, Set<WebSocketSession>> sessionMap) throws Exception {
        Map<String, Object> sessionAttributes = session.getAttributes();
        String id = principal.getId();
        switch (CommandEnum.getValueOf(principal.getType())) {
            case COORDINATION_TYPE_ENTER: {
                logger.info(principal.getUserName() + "start editing coordination file " + principal.getId());
                sessionAttributes.put("principal", principal);
                String data = null;
                // add a lock to prevent concurrent crisis
                synchronized (textMap) {
                    if (!textMap.containsKey(id)) {
                        String text = contentDao.getContentById(id);
                        textMap.put(id, text);
                    }
                    data = textMap.get(id);
                }
                Set<WebSocketSession> set = null;
                // add a lock to prevent concurrent crisis
                synchronized (sessionMap) {
                    // if sessionMap not contain id key, put new Set into sessionMap and id as a key
                    if (!sessionMap.containsKey(id)) {
                        sessionMap.put(id, ConcurrentHashMap.newKeySet());
                    }
                    set = sessionMap.get(id);
                }
                set.add(session);
                logger.info("the number of people who are editing the coordination file " + id + "is " + set.size());
                sendMessage(session, CommandEnum.COORDINATION_RESPONSE_ALL, data);
                break;
            }
            case COORDINATION_TYPE_EXIT: {
                logger.info(principal.getUserName() + "exit editing coordination file " + principal.getId());
                dealExit(id, session, textMap, sessionMap);
                break;
            }
            default:
                break;
        }


    }

    @Override
    public void publishText(WebSocketSession session, NoteText noteText, Map<String, String> textMap, Map<String, Set<WebSocketSession>> sessionMap) throws Exception {
        String id = noteText.getId();
        String text = null;
        synchronized (textMap) {
            text = textMap.get(id);
        }
        StringBuilder textBuilder = new StringBuilder(text);
        CommandEnum.getValueOf(noteText.getType());
        switch (CommandEnum.getValueOf(noteText.getType())) {
            case COORDINATION_ADD: {
                // insert content from coordination file
                textBuilder.insert(noteText.getStart(), noteText.getValue());
                break;
            }
            case COORDINATION_DELETE: {
                // delete content of coordination file
                textBuilder.delete(noteText.getStart(), noteText.getEnd());
                break;
            }
            case COORDINATION_REPLACE: {
                // replace content of coordination file
                textBuilder.replace(noteText.getStart(), noteText.getEnd(), noteText.getValue());
                break;
            }
            default:
                break;
        }
        // add a lock to prevent concurrent crisis
        synchronized (textMap) {
            textMap.put(id, textBuilder.toString());
        }
        List<WebSocketSession> collect = null;
        // add a lock to prevent concurrent crisis
        synchronized (sessionMap) {
            collect = sessionMap.get(id).stream()
                    .filter(socketSession -> !socketSession.equals(session))
                    .collect(Collectors.toList());
        }
        for(WebSocketSession socketSession : collect) {
            sendMessage(socketSession, CommandEnum.COORDINATION_RESPONSE_PART, noteText);
        }
    }

    @Override
    public void dealExit(String id, WebSocketSession session, Map<String, String> textMap, Map<String, Set<WebSocketSession>> sessionMap) {
        Boolean isSave = false;
        int numEdit = 0;
        String text = null;
        // add a lock to prevent concurrent crisis
        synchronized (sessionMap) {
            if(sessionMap.containsKey(id)) {
                Set<WebSocketSession> set = sessionMap.get(id);
                if (set != null) {
                    set.remove(session);
                    // all of people exit editing of coordination file
                    if (set.size() == 0) {
                        sessionMap.remove(id);
                        // add a lock to prevent concurrent crisis
                        synchronized (textMap) {
                            if(textMap.containsKey(id)) {
                                text = textMap.get(id);
                                textMap.remove(id);
                                isSave = true;
                            }
                        }
                    }
                    numEdit = set.size();
                }
            }
        }
        logger.info("the remaining number of people who are editing the coordination file " + id + "is " + numEdit);
        if(isSave) {
            contentDao.updateContentById(new Content(id, text));
            logger.info("Everyone exit editing the coordination file " + id + " then system perform cleanup");
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
