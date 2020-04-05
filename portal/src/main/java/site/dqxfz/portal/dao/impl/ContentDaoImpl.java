package site.dqxfz.portal.dao.impl;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.pojo.po.Content;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

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

    @Override
    public String getContentById(String id) {
        Content content = mongoOperations.findById(id, Content.class);
        return content.getText();
    }

    @Override
    public void updateContentById(Content content) {
        Update update = new Update().set("text", content.getText());
        mongoOperations.updateFirst(query(where("id").is(content.getId())), update, Content.class);
    }
}
