package site.dqxfz.portal.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import site.dqxfz.portal.constant.PortfolioType;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.Portfolio;
import site.dqxfz.portal.vo.EasyUITreeNode;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<EasyUITreeNode> findByFatherId(String fatherId) {
        Query query = query(where("fatherId").is(fatherId));
        List<Portfolio> portfolios = mongoOperations.find(query, Portfolio.class);
        List<EasyUITreeNode> nodes = portfolios.stream()
                .map(portfolio -> {
                    return new EasyUITreeNode(
                            portfolio.getId(),
                            portfolio.getName(),
                            portfolio.getType().equals(PortfolioType.FOLDER) ? "closed" : "open",
                            portfolio.getType(),
                            portfolio.getIconCls(),
                            portfolio.getFatherId());
                })
                .collect(Collectors.toList());
        return nodes;
    }
}