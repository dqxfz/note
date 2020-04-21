package site.dqxfz.portal.service.impl;

import org.springframework.stereotype.Service;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.constant.ResponseConsts;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.CoordinationService;

import java.util.ArrayList;
import java.util.List;
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
}
