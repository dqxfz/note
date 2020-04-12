package site.dqxfz.portal.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author WENG Yang
 * @date 2020年04月11日
 **/
public interface SsoService {
    boolean isLogin(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
