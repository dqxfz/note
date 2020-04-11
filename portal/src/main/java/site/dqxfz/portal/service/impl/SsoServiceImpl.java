package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.dqxfz.common.util.CookieUtils;
import site.dqxfz.portal.service.SsoService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 单点登录服务
 * @author WENG Yang
 * @date 2020年04月11日
 **/
@Service
public class SsoServiceImpl implements SsoService {
    @Value("${cookie.name}")
    private String cookieName;

    @Override
    public boolean isLogin(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = CookieUtils.getCookie(request,cookieName);
        if(cookie != null) {
            String sessionId = cookie.getValue();
        }
        return true;
    }
}