package site.dqxfz.portal.dao;


import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import java.util.List;

public interface PortfolioDao {
    List<Portfolio> listByFatherId(String fatherId);

    Portfolio savePortfolio(Portfolio portfolio);

    void updateNameById(String id, String name);

    Portfolio getPortfolioById(String id);

    void deleteListByIdList(List<String> portfolioIdList);

    void updateFatherIdById(String id, String fatherId);

    void addChild(String id, String childId);

    List<Portfolio> listByIdList(List<String> childList);

    void updateCoordinationNumById(String id, int size);

    void deleteChild(String fatherId, String id);
}
