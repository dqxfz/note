package site.dqxfz.portal.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import site.dqxfz.portal.service.SsoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器，用于验证用户是否登录
 * @author WENG Yang
 * @date 2020年04月10日
 **/
@Component
public class LoginInterceptor implements HandlerInterceptor {
    private final Logger logger = LogManager.getLogger(this.getClass());

    private final SsoService ssoService;

    public LoginInterceptor(SsoService ssoService) {
        this.ssoService = ssoService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isLogin = ssoService.isLogin(request,response);
        return isLogin;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}