package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.ActionResult;
import site.dqxfz.portal.service.PortfolioService;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import javax.sound.sampled.Port;
import java.io.File;
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
    @Autowired
    private String filePath;
    private final PortfolioDao portfolioDao;
    private final UserDao userDao;
    private final ContentDao contentDao;

    public PortfolioServiceImpl(PortfolioDao portfolioDao, UserDao userDao, ContentDao contentDao) {
        this.portfolioDao = portfolioDao;
        this.userDao = userDao;
        this.contentDao = contentDao;
    }

    @Override
    public List<EasyUiTreeNode> listPortfolios(String fatherId) {
        List<Portfolio> portfolios = portfolioDao.listByFatherId(fatherId);
        List<EasyUiTreeNode> nodes = portfolios.stream()
                .map(portfolio -> new EasyUiTreeNode(
                        portfolio.getId(),
                        portfolio.getName(),
                        portfolio.getIconCls().equals(IconClsType.FOLDER) ? "closed" : "open",
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
        if(result.getIconCls() == IconClsType.MARKDOWN) {
            contentDao.saveContent(new Content(result.getId(),null));
        }
        return result;
    }

    @Override
    public void updatePortfolio(String id, String name) {
        portfolioDao.updateNameById(id,name);
    }

    @Override
    public void detePortfolio(String id) {
        // 查询出所有要删除的portfolio
        List<Portfolio> portfolios = new ArrayList<>();
        portfolios.add(portfolioDao.getPortfolioById(id));
        listIds(portfolios,id);
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
        // 删除文件
        for(String uuidName : uuidNameList) {
            File file = new File(filePath + uuidName);
            file.delete();
        }
    }

    /**
     * 根据fatherId递归查找子节点
     * @param idList 将查询到的子节点保存在idList
     * @param fatherId 将要查找的父节点
     */
    private void listIds(List<Portfolio> idList, String fatherId) {
        List<Portfolio> portfolios = portfolioDao.listByFatherId(fatherId);
        idList.addAll(portfolios);
        for(Portfolio portfolio : portfolios) {
            listIds(idList,portfolio.getId());
        }
    }
}