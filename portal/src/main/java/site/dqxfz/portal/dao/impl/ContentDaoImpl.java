package site.dqxfz.portal.dao.impl;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;

import javax.sound.sampled.Port;
import java.util.List;

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
        Query query = query(where("id").is(content.getId()));
        mongoOperations.updateFirst(query, update, Content.class);
    }

    @Override
    public List<Content> listContentByIdList(List<String> idList) {
        Query query = query(where("id").in(idList));
        List<Content> contents = mongoOperations.find(query, Content.class);
        return contents;
    }

    @Override
    public void deleteListByIdList(List<String> contentIdList) {
        Query query = query(where("id").in(contentIdList));
        mongoOperations.remove(query, Content.class);
    }

    @Override
    public Content getContentBytext(String uuidName) {
        Query query = query(where("text").is(uuidName));
        Content content = mongoOperations.findOne(query, Content.class);
        return content;
    }
}
