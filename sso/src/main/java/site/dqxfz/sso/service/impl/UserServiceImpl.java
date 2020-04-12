package site.dqxfz.sso.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.dqxfz.common.util.CookieUtils;
import site.dqxfz.sso.pojo.dto.UserDTO;
import site.dqxfz.sso.pojo.po.User;
import site.dqxfz.sso.service.UserService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author WENG Yang
 * @date 2020年04月12日
 **/
@Service
public class UserServiceImpl implements UserService {
    @Value("${cookie.name}")
    private String cookieName;
    @Value("${session.expire.time}")
    private Long sessionExpireTime;
    @Value("${st.expire.time}")
    private Long stExpireTime;

    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, String> stringRedisTemplate;

    public UserServiceImpl(RedisTemplate<String, User> userRedisTemplate, RedisTemplate<String, String> stringRedisTemplate) {
        this.userRedisTemplate = userRedisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public String getServiceToken(HttpServletRequest request) {
        Cookie cookie = CookieUtils.getCookie(request,cookieName);
        if(cookie != null) {
            String sessionId = cookie.getValue();
            User user = userRedisTemplate.boundValueOps(sessionId).get();
            // 如果session没有过期，则返回true
            if(user != null) {
                userRedisTemplate.boundValueOps(sessionId).expire(sessionExpireTime, TimeUnit.SECONDS);
                String st = UUID.randomUUID().toString();
                stringRedisTemplate.boundValueOps(st).set(sessionId, Duration.ofSeconds(stExpireTime));
                return st;
            }
        }
        return null;
    }

    @Override
    public UserDTO getUserDTO(String serviceToken) {
        String sessionId = stringRedisTemplate.boundValueOps(serviceToken).get();
        if(!StringUtils.isEmpty(sessionId)) {
            User user = userRedisTemplate.boundValueOps(sessionId).get();
            return new UserDTO(sessionId,user);
        }
        return null;
    }
}
