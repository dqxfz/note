package site.dqxfz.portal.dao;

import site.dqxfz.portal.pojo.po.User;

/**
 * @author WENG Yang
 * @date 2020年04月13日
 **/
public interface UserDao {
    User getUserByUserName(String username);

    User saveUser(String username);
}
