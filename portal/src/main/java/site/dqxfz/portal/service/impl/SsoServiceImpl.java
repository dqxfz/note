package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UrlPathHelper;
import site.dqxfz.common.util.CookieUtils;
import site.dqxfz.portal.pojo.dto.UserDTO;
import site.dqxfz.portal.pojo.po.User;
import site.dqxfz.portal.service.SsoService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    @Value("${sso.login.url}")
    private String ssoLoginUrl;
    @Value("${sso.user.info.url}")
    private String ssoUserInfoUrl;

    private final RedisTemplate<String, User> redisTemplate;
    private final RestTemplate restTemplate;

    public SsoServiceImpl(RedisTemplate<String, User> redisTemplate, RestTemplate restTemplate) {
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
    }
    @Override
    public boolean isLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie cookie = CookieUtils.getCookie(request,cookieName);
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        StringBuffer requestURL = request.getRequestURL();
        if(cookie != null) {
            String sessionId = cookie.getValue();
            User user = redisTemplate.boundValueOps(sessionId).get();
            // 如果session没有过期，则返回true
            if(user != null) {
                redisTemplate.boundValueOps(sessionId).expire(sessionExpireTime, TimeUnit.SECONDS);
                return true;
            }
        }
        String serviceToken = request.getParameter("serviceToken");
        // 如果有serviceToken,则通过serviceToken向sso服务器获取user信息
        if(!StringUtils.isEmpty(serviceToken)) {
            UserDTO userDTO = restTemplate.getForObject(ssoUserInfoUrl, UserDTO.class);
            if(userDTO != null && userDTO.getUser() != null) {
                request.setAttribute("user", userDTO.getUser());
                // 设置cookie
                return true;
            }
        }
        // 未登录，则跳到登录页面
        response.sendRedirect(ssoLoginUrl + "?service=" + request.getRequestURL());
        return false;
    }
}