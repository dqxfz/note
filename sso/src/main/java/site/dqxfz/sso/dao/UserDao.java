package site.dqxfz.sso.dao;

import org.apache.ibatis.annotations.Mapper;
import site.dqxfz.sso.pojo.po.User;

/**
 * @author WENG Yang
 * @date 2020年04月13日
 **/
public interface UserDao {
    void insertUser(User user);

    User selectUserByUserName(String username);
}
