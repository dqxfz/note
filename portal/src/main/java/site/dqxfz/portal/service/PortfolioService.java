package site.dqxfz.portal.service;


import site.dqxfz.portal.vo.EasyUITreeNode;

import java.util.List;

public interface PortfolioService {
    List<EasyUITreeNode> findByFatherId(String fatherId);
}
