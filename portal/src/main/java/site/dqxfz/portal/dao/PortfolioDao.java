package site.dqxfz.portal.dao;


import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import java.util.List;

public interface PortfolioDao {
    List<Portfolio> listByFatherId(String fatherId);

    Portfolio savePortfolio(Portfolio portfolio);

    void updateNameById(String id, String name);
}
