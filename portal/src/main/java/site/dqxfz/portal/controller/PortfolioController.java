package site.dqxfz.portal.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.PortfolioService;

import java.util.List;

/**
 * @Description: 处理文件夹（即目录）的增删改查
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@RestController
@RequestMapping("/portfolio")
public class PortfolioController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * 通过id查询所有子portfolio
     * @param id 所属文件夹id
     * @return 返回子portfolio列表
     */
    @GetMapping
    public ResponseEntity queryPortfolio(String id){
        try{
            List<EasyUiTreeNode> nodes = portfolioService.listPortfolios(id);
            return new ResponseEntity(nodes, nodes.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 添加portfolio
     * @param portfolio 将要添加的portfolio对象
     * @return ResponseEntity
     */
    @PostMapping
    public ResponseEntity savePortfolio(Portfolio portfolio){
        try{
            Portfolio result = portfolioService.savePortfolio(portfolio);
            return new ResponseEntity(result.getId(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 修改portfolio名字
     * @param id 将要修改portfolio的id
     * @param name 新的名字
     * @return ResponseEntity
     */
    @PutMapping
    public ResponseEntity updatePortfolio(String id, String name){
        try{
            portfolioService.updatePortfolio(id, name);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}