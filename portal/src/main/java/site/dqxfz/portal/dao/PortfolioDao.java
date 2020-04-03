package site.dqxfz.portal.dao;


import site.dqxfz.portal.vo.EasyUITreeNode;

import java.util.List;

public interface PortfolioDao {
    List<EasyUITreeNode> findByFatherId(String fatherId);
}
