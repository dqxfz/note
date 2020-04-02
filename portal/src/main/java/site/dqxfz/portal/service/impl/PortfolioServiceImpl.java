package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.dqxfz.common.vo.ActionResult;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.Portfolio;
import site.dqxfz.portal.pojo.User;
import site.dqxfz.portal.service.PortfolioService;
import site.dqxfz.portal.vo.EasyUITreeNode;

import java.util.List;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@Service
public class PortfolioServiceImpl implements PortfolioService {
    @Autowired
    PortfolioDao portfolioDao;
    @Autowired
    UserDao userDao;

    @Override
    public ActionResult findByFatherId(String fatherId) {
        EasyUITreeNode node = portfolioDao.findByFatherId(fatherId);
        return new ActionResult(true,null,null,node);
    }
}