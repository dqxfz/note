package site.dqxfz.portal.service.impl;

import org.springframework.stereotype.Service;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.constant.ResponseConsts;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.CoordinationService;

import java.util.ArrayList;
import java.util.List;

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
        Portfolio coordinationPortfolio = portfolioDao.getPortfolioById(id);
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

        for(User user : users) {
            List<Portfolio> portfolios = portfolioDao.listByFatherId(user.getPofolioId());
            // 查找用户的协同文件夹对象
            for (Portfolio portfolio : portfolios) {
                if (portfolio.getIconCls().equals(IconClsEnum.COORDINATION)) {
                    // 克隆协同文件为新的文件，插入到协同文件夹下
//                        Portfolio newPortfolio = new Portfolio(coordinationPortfolio.getName(),
//                                coordinationPortfolio.getType(),
//                                coordinationPortfolio.getIconCls(),
//                                portfolio.getId());
//                        portfolioDao.savePortfolio(newPortfolio);
//                        // 复制协同文件的内容为新文件的内容
//                        String content = contentDao.getContentById(coordinationPortfolio.getId());
//                        contentDao.saveContent(new Content(newPortfolio.getId(), content));
                    break;
                }
            }
        }
        return ResponseConsts.COORDINATION_SET_SUCCESS;
    }
}
