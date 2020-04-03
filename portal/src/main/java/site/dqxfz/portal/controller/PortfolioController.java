package site.dqxfz.portal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.common.vo.ActionResult;
import site.dqxfz.common.vo.EasyuiTreeNode;
import site.dqxfz.portal.service.PortfolioService;
import site.dqxfz.portal.vo.EasyUITreeNode;

import java.util.List;

/**
 * @Description: 处理文件夹（即目录）的增删改查
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    Logger logger = LogManager.getLogger(this.getClass());

    final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * 通过fatherId查询所有子portfolio
     * @param fatherId 所属文件夹id
     * @return 返回portfolio列表
     */
    @GetMapping
    public List<EasyUITreeNode> portfolio(String fatherId){
        try{
            return portfolioService.findByFatherId(fatherId);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}