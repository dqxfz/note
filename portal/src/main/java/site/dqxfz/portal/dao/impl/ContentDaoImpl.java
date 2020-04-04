package site.dqxfz.portal.dao.impl;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.pojo.po.Content;

@Repository
public class ContentDaoImpl implements ContentDao {
    private final MongoOperations mongoOperations;

    public ContentDaoImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void saveContent(Content content) {
        mongoOperations.save(content);
    }
}
