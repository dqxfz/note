package site.dqxfz.portal.service;

import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import java.util.List;

/**
 * @author WENG Yang
 * @date 2020年04月20日
 **/
public interface CoordinationService {
    String setCoordination(String id, String[] userNames);

    List<EasyUiTreeNode> getChildren(String id);
}
