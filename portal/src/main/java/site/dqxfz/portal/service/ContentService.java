package site.dqxfz.portal.service;

import site.dqxfz.portal.pojo.po.Content;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
public interface ContentService {
    String getContent(String id, String iconCls);

    void updateContent(Content content);
}
