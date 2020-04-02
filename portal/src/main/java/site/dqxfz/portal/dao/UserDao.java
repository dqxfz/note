package site.dqxfz.portal.dao;

import site.dqxfz.portal.pojo.User;

public interface UserDao {
    User findByUsername(String username);
}
