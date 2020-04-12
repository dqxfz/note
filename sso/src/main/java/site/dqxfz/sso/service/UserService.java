package site.dqxfz.sso.service;

import site.dqxfz.sso.pojo.dto.UserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WENG Yang
 * @date 2020年04月12日
 **/
public interface UserService {
    /**
     * 判断是否登录，如果登录就生成一个ServiceTicket，然后返回它
     * @param request HttpServletRequest
     * @return ServiceTicket
     */
    String getServiceToken(HttpServletRequest request);

    UserDTO getUserDTO(String serviceToken);
}
