package site.dqxfz.portal.dao.impl;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.po.Portfolio;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;
/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@Repository
public class PortfolioDaoImpl implements PortfolioDao {
    final MongoOperations mongoOperations;

    public PortfolioDaoImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public List<Portfolio> listByFatherId(String fatherId) {
        Query query = query(where("fatherId").is(fatherId));
        List<Portfolio> portfolios = mongoOperations.find(query, Portfolio.class);
        return portfolios;
    }

    @Override
    public Portfolio savePortfolio(Portfolio portfolio) {
        Portfolio result = mongoOperations.insert(portfolio);
        return result;
    }

    @Override
    public void updateNameById(String id, String name) {
        Update update = new Update().set("name", name);
        Query query = query(where("id").is(id));
        mongoOperations.updateFirst(query, update, Portfolio.class);
    }

    @Override
    public Portfolio getPortfolioById(String id) {
        Portfolio portfolio = mongoOperations.findById(id, Portfolio.class);
        return portfolio;
    }

    @Override
    public void deleteListByIdList(List<String> portfolioIdList) {
        mongoOperations.remove(query(where("id").in(portfolioIdList)),Portfolio.class);
    }

    @Override
    public void updateFatherIdById(String id, String fatherId) {
        Update update = new Update().set("fatherId", fatherId);
        Query query = query(where("id").is(id));
        mongoOperations.updateFirst(query, update, Portfolio.class);
    }

    @Override
    public void addChild(String id, String childId) {
        Update update = new Update().push("childList", childId);
        Query query = query(where("id").is(id));
        mongoOperations.updateFirst(query, update, Portfolio.class);
    }

    @Override
    public List<Portfolio> listByIdList(List<String> childList) {
        Query query = query(where("id").in(childList));
        List<Portfolio> portfolios = mongoOperations.find(query, Portfolio.class);
        return portfolios;
    }
}