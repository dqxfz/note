package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import site.dqxfz.common.util.CookieUtils;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.SsoService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 单点登录服务
 * @author WENG Yang
 * @date 2020年04月11日
 **/
@Service
public class SsoServiceImpl implements SsoService {
    @Value("${cookie.name}")
    private String cookieName;
    @Value("${session.expire.time}")
    private Long sessionExpireTime;

    private final RedisTemplate<String, User> redisTemplate;

    public SsoServiceImpl(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtils.getCookie(request,cookieName);
        if(cookie != null) {
            String sessionId = cookie.getValue();
            User user = redisTemplate.boundValueOps(sessionId).get();
            // 如果session没有过期，则返回true
            if(user != null) {
                redisTemplate.boundValueOps(sessionId).expire(sessionExpireTime, TimeUnit.SECONDS);
                return true;
            }
            // 如果session过期，则重新登录
//            response.sendRedirect();

        }

        return false;
    }
}