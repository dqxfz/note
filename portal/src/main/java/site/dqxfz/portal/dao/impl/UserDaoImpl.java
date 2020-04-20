package site.dqxfz.portal.dao.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.po.User;

/**
 * @author WENG Yang
 * @date 2020年04月13日
 **/
@Service
public class UserDaoImpl implements UserDao {
    @Value("${portfolio.root.name}")
    private String portfolioRootName;
    @Value("${portfolio.coordination.name}")
    private String portfolioCoordinationName;

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
        // 添加根目录
        Portfolio portfolioRoot = mongoOperations.insert(new Portfolio(portfolioRootName, null, IconClsEnum.FOLDER,username));
        // 添加协同文件夹
        mongoOperations.insert(new Portfolio(portfolioCoordinationName, null, IconClsEnum.COORDINATION,portfolioRoot.getId() ));
        User user = new User(username, portfolioRoot.getId());
        User user02 = mongoOperations.insert(user);
        return user02;
    }
}