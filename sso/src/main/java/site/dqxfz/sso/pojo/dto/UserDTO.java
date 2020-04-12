package site.dqxfz.sso.pojo.dto;

import site.dqxfz.sso.pojo.po.User;

/**
 * @author WENG Yang
 * @date 2020年04月12日
 **/
public class UserDTO {
    private String sessionId;
    private User user;

    public UserDTO() {
    }

    public UserDTO(String sessionId, User user) {
        this.sessionId = sessionId;
        this.user = user;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
