package site.dqxfz.portal.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Repository;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.User;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年04月02日
 **/
@Repository
public class UserDaoImpl implements UserDao {
    @Autowired
    MongoOperations mongoOperations;

    @Override
    public User findByUsername(String username) {
        return null;
    }
}