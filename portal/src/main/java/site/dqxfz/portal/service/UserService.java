package site.dqxfz.portal.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * @author WENG Yang
 * @date 2020年04月11日
 **/
public interface UserService {
    boolean isLogin(HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 删除user的session
     * @param user 将要删除session的user
     * @throws NoSuchAlgorithmException
     */
    void deleteSession(String user) throws NoSuchAlgorithmException;

    void validateServiceTicket(String serviceTicket, String serviceUrl, HttpServletResponse response) throws Exception;
}
