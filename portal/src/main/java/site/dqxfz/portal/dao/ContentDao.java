package site.dqxfz.portal.dao;

import site.dqxfz.portal.pojo.po.Content;

public interface ContentDao {
    void saveContent(Content content);

    String getContentById(String id);

    void updateContentById(Content content);
}
