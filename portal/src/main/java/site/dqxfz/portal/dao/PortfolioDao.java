package site.dqxfz.portal.dao;


import site.dqxfz.portal.vo.EasyUITreeNode;

public interface PortfolioDao {
    EasyUITreeNode findByFatherId(String fatherId);
}
