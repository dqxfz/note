package site.dqxfz.portal.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import site.dqxfz.common.util.CookieUtils;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.common.util.Md5Utils;
import site.dqxfz.portal.dao.UserDao;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 单点登录服务
 * @author WENG Yang
 * @date 2020年04月11日
 **/
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Value("${cookie.name}")
    private String cookieName;
    @Value("${session.expire.time}")
    private Long sessionExpireTime;
    @Value("${sso.page.login.url}")
    private String ssoPageLoginUrl;
    @Value("${sso.user.info.url}")
    private String ssoUserInfoUrl;

    private final StringRedisTemplate stringRedisTemplate;
    private final RestTemplate restTemplate;
    private final UserDao userDao;

    public UserServiceImpl(StringRedisTemplate stringRedisTemplate, RestTemplate restTemplate, UserDao userDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.restTemplate = restTemplate;
        this.userDao = userDao;
    }

    @Override
    public boolean isLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String sessionId = CookieUtils.getCookieValue(request,cookieName);
        if(!StringUtils.isEmpty(sessionId)) {
            String userJson = stringRedisTemplate.boundValueOps(sessionId).get();
            User user = JsonUtils.jsonToObject(userJson, User.class);
            // session未过期，返回true
            if(user != null) {
                return true;
            }
            // session已经过期，删除已经失效的cookie
            CookieUtils.deleteCookie(response,cookieName);
        }
        String serviceTicket = request.getParameter("serviceTicket");
        // 如果有serviceToken,则通过serviceToken向sso服务器获取user信息
        if(!StringUtils.isEmpty(serviceTicket)) {
            String username = restTemplate.getForObject(ssoUserInfoUrl + "?serviceTicket=" + serviceTicket, String.class);
            // 如果已经登录
            if(!StringUtils.isEmpty(username)) {
                // 创建session
                User user = userDao.getUserByUserName(username);
                // user为null,表示还没有创建user的笔记用户信息
                if(user == null) {
                    // 创建笔记用户信息
                    user = userDao.saveUser(username);
                }
                // 使用加密后的username作为sessionId
                sessionId = Md5Utils.crypt(username);
                String userJson = JsonUtils.objectToJson(user);
                stringRedisTemplate.boundValueOps(sessionId).set(userJson, Duration.ofSeconds(sessionExpireTime));
                CookieUtils.setCookie(response,cookieName,sessionId);
                // 权限验证成功后重定向到请求页面,删除'serviceTicket'等属性
                response.sendRedirect(request.getRequestURL().toString());
            }
        } else {
            // 跳到登录页面
            response.sendRedirect(ssoPageLoginUrl + "?service=" + request.getRequestURL());
        }
        return false;
    }
}