package site.dqxfz.portal.dao.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;

/**
 * @author WENG Yang
 * @date 2020年04月13日
 **/
public class UserDaoImpl implements UserDao {
    @Value("${portfolio.root.name}")
    private String portfolioRootName;

    private final MongoOperations mongoOperations;

    public UserDaoImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public User getUserByUserName(String username) {
        User user = mongoOperations.findById(username, User.class);
        return user;
    }

    @Override
    public User saveUser(String username) {
        Portfolio portfolio = mongoOperations.insert(new Portfolio(portfolioRootName, null, IconClsEnum.FOLDER,username));
        User user = new User(username, portfolio.getId());
        User user02 = mongoOperations.insert(user);
        return user02;
    }
}