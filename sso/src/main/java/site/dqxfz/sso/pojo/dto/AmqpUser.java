package site.dqxfz.sso.pojo.dto;

import java.io.Serializable;

/**
 * @author WENG Yang
 * @date 2020年04月19日
 **/
public class AmqpUser implements Serializable {
    private String user;
    private Object data;

    public AmqpUser() {
    }

    public AmqpUser(String user, Object data) {
        this.user = user;
        this.data = data;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}