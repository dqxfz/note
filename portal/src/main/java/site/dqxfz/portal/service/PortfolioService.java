package site.dqxfz.portal.service;


import site.dqxfz.common.vo.ActionResult;

public interface PortfolioService {
    ActionResult findByFatherId(String fatherId);
}
