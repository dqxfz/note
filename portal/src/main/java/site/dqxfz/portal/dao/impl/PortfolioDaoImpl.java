package site.dqxfz.portal.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import site.dqxfz.portal.constant.PortfolioType;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.Portfolio;
import site.dqxfz.portal.vo.EasyUITreeNode;

import static org.springframework.data.mongodb.core.query.Criteria.*;
import static org.springframework.data.mongodb.core.query.Query.*;
/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@Repository
public class PortfolioDaoImpl implements PortfolioDao {
    private final MongoOperations mongoOperations;

    public PortfolioDaoImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public EasyUITreeNode findByFatherId(String fatherId) {
        Query query = query(where("fatherId").is(fatherId));
        Portfolio portfolio = mongoOperations.findOne(query, Portfolio.class);
        return new EasyUITreeNode(
                portfolio.getId(),
                portfolio.getName(),
                portfolio.getType().equals(PortfolioType.FOLDER) ? "closed" : "open",
                portfolio.getType(),
                portfolio.getFatherId());
    }
}