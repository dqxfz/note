package site.dqxfz.portal.service.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.dqxfz.common.util.CookieUtils;
import site.dqxfz.common.util.FtpUtils;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.PortfolioService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@Service
public class PortfolioServiceImpl implements PortfolioService {
    @Value("${file.ftp.url}")
    private String fileFtpUrl;
    @Value("${file.ftp.port}")
    private Integer fileFtpPort;
    @Value("${file.ftp.user}")
    String fileFtpUser;
    @Value("${file.ftp.password}")
    String fileFtpPassword;
    @Value("${cookie.name}")
    private String cookieName;

    private final PortfolioDao portfolioDao;
    private final ContentDao contentDao;
    private final StringRedisTemplate stringRedisTemplate;

    public PortfolioServiceImpl(PortfolioDao portfolioDao, ContentDao contentDao, StringRedisTemplate stringRedisTemplate) {
        this.portfolioDao = portfolioDao;
        this.contentDao = contentDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public List<EasyUiTreeNode> listPortfolios(String fatherId) {
        List<Portfolio> portfolios = portfolioDao.listByFatherId(fatherId);
        List<EasyUiTreeNode> nodes = portfolios.stream()
                .map(portfolio -> new EasyUiTreeNode(
                        portfolio.getId(),
                        portfolio.getName(),
                        portfolio.getIconCls().equals(IconClsEnum.FOLDER) || portfolio.getIconCls().equals(IconClsEnum.COORDINATION) ? "closed" : "open",
                        portfolio.getIconCls(),
                        portfolio.getFatherId())
                )
                .collect(Collectors.toList());
        return nodes;
    }

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        Portfolio result = portfolioDao.savePortfolio(portfolio);
        // 如果是创建markdown文件，则初始化markdown文件的内容
        if (result.getIconCls() == IconClsEnum.MARKDOWN) {
            contentDao.saveContent(new Content(result.getId(), null));
        }
        return result;
    }

    @Override
    public void updatePortfolio(String id, String name) {
        portfolioDao.updateNameById(id, name);
    }

    @Override
    public void detePortfolio(String id) throws Exception {
        // 查询出所有要删除的portfolio
        List<Portfolio> portfolios = new ArrayList<>();
        portfolios.add(portfolioDao.getPortfolioById(id));
        listIds(portfolios, id);
        // 获取要删除portfolio和content的id
        List<String> portfolioIdList = portfolios.stream()
                .map(portfolio -> portfolio.getId())
                .collect(Collectors.toList());
        // 获取类型是属于上传文件的portfolio的id
        List<String> fileIdList = portfolios.stream()
                .filter(portfolio -> !StringUtils.isEmpty(portfolio.getType()))
                .map(portfolio -> portfolio.getId())
                .collect(Collectors.toList());
        // 查询要删除的文件uuidName
        List<Content> contents = contentDao.listContentByIdList(fileIdList);
        List<String> uuidNameList = contents.stream()
                .map(content -> content.getText())
                .collect(Collectors.toList());
        /**
         * 接下来开始执行删除操作
         */
        // 删除portfolio
        portfolioDao.deleteListByIdList(portfolioIdList);
        // 删除content
        contentDao.deleteListByIdList(portfolioIdList);
        FTPClient ftpClient = FtpUtils.getFTPClient(fileFtpUrl, fileFtpPort, fileFtpUser, fileFtpPassword);
        // 删除文件
        for (String uuidName : uuidNameList) {
            FtpUtils.deleteFile(uuidName, ftpClient);
        }
    }

    @Override
    public String getDownloadUrl(String id) {
        String uuidName = contentDao.getContentById(id);
        String downloadUrl = "http://" + fileFtpUrl + "/" + uuidName;
        return downloadUrl;
    }

    @Override
    public User getRootId(HttpServletRequest request) throws IOException {
        String sessionId = CookieUtils.getCookieValue(request, cookieName);
        String userJson = stringRedisTemplate.boundValueOps(sessionId).get();
        User user = JsonUtils.jsonToObject(userJson, User.class);
        return user;
    }

    @Override
    public void downloadNote(String id, HttpServletResponse response) throws IOException {
        String content = contentDao.getContentById(id);
        // 防止content为null
        content += "";
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(content.getBytes());
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    /**
     * 根据fatherId递归查找子节点
     * @param idList   将查询到的子节点保存在idList
     * @param fatherId 将要查找的父节点
     */
    private void listIds(List<Portfolio> idList, String fatherId) {
        List<Portfolio> portfolios = portfolioDao.listByFatherId(fatherId);
        idList.addAll(portfolios);
        for (Portfolio portfolio : portfolios) {
            listIds(idList, portfolio.getId());
        }
    }
}