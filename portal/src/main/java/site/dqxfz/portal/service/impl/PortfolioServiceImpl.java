package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.ActionResult;
import site.dqxfz.portal.service.PortfolioService;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import javax.sound.sampled.Port;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@Service
public class PortfolioServiceImpl implements PortfolioService {
    private final PortfolioDao portfolioDao;
    private final UserDao userDao;
    private final ContentDao contentDao;

    public PortfolioServiceImpl(PortfolioDao portfolioDao, UserDao userDao, ContentDao contentDao) {
        this.portfolioDao = portfolioDao;
        this.userDao = userDao;
        this.contentDao = contentDao;
    }

    @Override
    public List<EasyUiTreeNode> listPortfolios(String fatherId) {
        List<Portfolio> portfolios = portfolioDao.listByFatherId(fatherId);
        List<EasyUiTreeNode> nodes = portfolios.stream()
                .map(portfolio -> new EasyUiTreeNode(
                        portfolio.getId(),
                        portfolio.getName(),
                        portfolio.getIconCls().equals(IconClsType.FOLDER) ? "closed" : "open",
                        portfolio.getIconCls(),
                        portfolio.getFatherId())
                )
                .collect(Collectors.toList());
        return nodes;
    }

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        Portfolio result = portfolioDao.savePortfolio(portfolio);
        // 如果是创建markdown文件，则初始化markdown文件的内容
        if(result.getIconCls() == IconClsType.MARKDOWN) {
            contentDao.saveContent(new Content(result.getId(),null));
        }
        return result;
    }

    @Override
    public void updatePortfolio(String id, String name) {
        portfolioDao.updateNameById(id,name);
    }
}