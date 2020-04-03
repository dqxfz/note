package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.service.PortfolioService;
import site.dqxfz.portal.vo.EasyUITreeNode;

import java.util.ArrayList;
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
    public List<EasyUITreeNode> findByFatherId(String fatherId) {
        List<EasyUITreeNode> nodes = portfolioDao.findByFatherId(fatherId);
        return nodes;
    }
}