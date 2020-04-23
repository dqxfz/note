package site.dqxfz.portal.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.constant.ResponseConsts;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.CoordinationService;

import java.util.List;

/**
 * @author WENG Yang
 * @date 2020年04月20日
 **/
@RestController
@RequestMapping("/coordination")
public class CoordinationController {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final CoordinationService coordinationService;

    public CoordinationController(CoordinationService coordinationService) {
        this.coordinationService = coordinationService;
    }

    /**
     * 设置文件为协同文件
     * @param id 将要协同的文件id
     * @param userNameStr 将要协同的用户
     */
    @GetMapping("/set")
    public ResponseEntity setCoordination(String id, String userNameStr ){
        try {
            String[] userNames = userNameStr.split(",");
            String result = coordinationService.setCoordination(id, userNames);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/children")
    public ResponseEntity getChildren(String id){
        try {
            List<EasyUiTreeNode> nodes = coordinationService.getChildren(id);
            return new ResponseEntity(nodes, nodes.size() > 0 ? HttpStatus.OK : HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @DeleteMapping
    public ResponseEntity deleteCoordinationChild(String fatherId, String id) {
        try {
            coordinationService.deleteChild(fatherId, id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
