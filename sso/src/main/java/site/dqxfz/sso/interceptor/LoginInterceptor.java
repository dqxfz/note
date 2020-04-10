package site.dqxfz.sso.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

/**
 * @author WENG Yang
 * @date 2020年04月10日
 **/
public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LogManager.getLogger(this.getClass());
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String name = request.getParameter("name");
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("cookiename") && cookie.getValue().equals("cookieValue")) {
                    logger.info("有cookie了");
                    break;
                }
            }
        }
        if("wy".equals(name)) {
            Cookie cookie = new Cookie("cookiename","cookieValue");
            cookie.setDomain("common.site");
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}