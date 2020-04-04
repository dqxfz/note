package site.dqxfz.portal.dao;

import site.dqxfz.portal.pojo.po.User;

public interface UserDao {
    User findByUsername(String username);
}
