package site.dqxfz.sso.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.dqxfz.common.util.CookieUtils;
import site.dqxfz.common.util.JsonUtils;
import site.dqxfz.common.util.Md5Utils;
import site.dqxfz.sso.dao.UserDao;
import site.dqxfz.sso.pojo.po.User;
import site.dqxfz.sso.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author WENG Yang
 * @date 2020年04月12日
 **/
@Service
public class UserServiceImpl implements UserService {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Value("${cookie.name}")
    private String cookieName;
    @Value("${session.expire.time}")
    private Long sessionExpireTime;
    @Value("${service.ticket.expire.time}")
    private Long serviceTicketExpireTime;
    @Value("${page.login.url}")
    private String pageLoginUrl;

    private final StringRedisTemplate stringRedisTemplate;
    private final UserDao userDao;

    public UserServiceImpl(StringRedisTemplate stringRedisTemplate, UserDao userDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.userDao = userDao;
    }


    @Override
    public String isLogin(HttpServletRequest request) throws IOException {
        String sessionId = CookieUtils.getCookieValue(request, cookieName);
        if(!StringUtils.isEmpty(sessionId)) {
            String userJson = stringRedisTemplate.boundValueOps(sessionId).get();
            User user = JsonUtils.jsonToObject(userJson, User.class);
            if(user != null) {
                String serviceTicket = createServiceTicket(sessionId, user.getUsername());
                return serviceTicket;
            }
        }
        return null;
    }

    @Override
    public String getUserName(String serviceTicket) {
        String username = stringRedisTemplate.boundValueOps(serviceTicket).get();
        return username;
    }

    @Override
    public String login(HttpServletResponse response, User user) throws Exception {
        User user02 = userDao.selectUserByUserName(user.getUsername());
        if(user02 == null || !Md5Utils.crypt(user.getPassword()).equals(user02.getPassword())) {
            // 登录失败，用户名或密码错误
            return null;
        }
        // 生成sessionId
        String sessionId = UUID.randomUUID().toString();
        String userJson = JsonUtils.objectToJson(user);
        // 保存session到redis并设置生存时间
        stringRedisTemplate.boundValueOps(sessionId).set(userJson,Duration.ofSeconds(sessionExpireTime));
        // 设置cookie
        CookieUtils.setCookie(response,cookieName,sessionId);
        logger.info("为用户" + user.getUsername() + "生成sessionId: " + sessionId);
        return createServiceTicket(sessionId, user.getUsername());
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, User user) throws Exception {
        String sessionId = CookieUtils.getCookieValue(request, cookieName);
        stringRedisTemplate.delete(sessionId);
        CookieUtils.deleteCookie(response,cookieName);
        String redirectUrl = pageLoginUrl;
        response.sendRedirect(redirectUrl);
    }

    @Override
    public void register(User user) throws Exception{
        user.setPassword(Md5Utils.crypt(user.getPassword()));
        userDao.insertUser(user);
    }

    private String createServiceTicket(String sessionId, String username) {
        // 刷新session生存时间
        stringRedisTemplate.boundValueOps(sessionId).expire(sessionExpireTime, TimeUnit.SECONDS);
        // 生成serviceTicket
        String serviceTicket = UUID.randomUUID().toString();
        // 保存servicetTicket到redis并设置生存时间
        stringRedisTemplate.boundValueOps(serviceTicket).set(username, Duration.ofSeconds(serviceTicketExpireTime));
        return serviceTicket;
    }

}
