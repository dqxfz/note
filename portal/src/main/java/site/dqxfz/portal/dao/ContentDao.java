package site.dqxfz.portal.dao;

import site.dqxfz.portal.pojo.po.Content;

import java.util.List;

public interface ContentDao {
    void saveContent(Content content);

    String getContentById(String id);

    void updateContentById(Content content);

    List<Content> listContentByIdList(List<String> idList);

    void deleteListByIdList(List<String> contentIdList);

    Content getContentBytext(String uuidName);
}
