package site.dqxfz.portal.service;


import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.ActionResult;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import java.util.List;

public interface PortfolioService {
    List<EasyUiTreeNode> listPortfolios(String id);

    Portfolio savePortfolio(Portfolio portfolio);

    void updatePortfolio(String id, String name);

    void detePortfolio(String id);
}
