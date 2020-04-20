package site.dqxfz.portal.service;


import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface PortfolioService {
    List<EasyUiTreeNode> listPortfolios(String id);

    Portfolio savePortfolio(Portfolio portfolio);

    void updatePortfolio(String id, String name);

    void detePortfolio(String id) throws Exception;

    String getDownloadUrl(String id);

    /**
     * 根据请求中的cookie获取user信息
     * @param request 前端请求
     * @return user
     * @throws IOException
     */
    User getRootId(HttpServletRequest request) throws IOException;

    void downloadNote(String id, HttpServletResponse response) throws IOException;
}
