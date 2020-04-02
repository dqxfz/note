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
    /**
     * Log4j2 日志工具
     */
    Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    PortfolioService portfolioService;

    /**
     * 获取所有portfolio
     * @return 返回所有的portfolio
     */
    @GetMapping
    public ActionResult portfolio(String fatherId){
        try{
            return portfolioService.findByFatherId(fatherId);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}