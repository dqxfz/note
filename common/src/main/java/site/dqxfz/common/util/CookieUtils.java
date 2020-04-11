package site.dqxfz.common.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Cookie工具类
 * @author WENG Yang
 * @date 2020年04月11日
 **/
public class CookieUtils {
    public static Cookie getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                   return cookie;
                }
            }
        }
        return null;
    }
}