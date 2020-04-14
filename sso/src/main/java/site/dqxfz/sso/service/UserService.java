package site.dqxfz.sso.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import site.dqxfz.sso.pojo.po.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author WENG Yang
 * @date 2020年04月12日
 **/
public interface UserService {
    /**
     * 验证是否已经登录过
     * @param request HttpServletRequest请求
     * @return 如果已经登录过就返回ServiceTicket，否则返回null
     */
    String isLogin(HttpServletRequest request) throws IOException;

    /**
     * 根据serviceTicket获取username
     * @param serviceTicket 将要获取的username的key
     * @return 返回username
     */
    String getUserName(String serviceTicket);

    /**
     * 执行登录身份验证
     *
     * @param response
     * @param user 将要验证的用户
     * @return 登录成功返回serviceTicket,登录失败返回null
     */
    String login(HttpServletResponse response, User user) throws Exception;

    void logout(HttpServletRequest request, HttpServletResponse response, User user) throws Exception;

    void register(User user) throws Exception;

}
