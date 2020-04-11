package site.dqxfz.portal.pojo.po;

import java.io.Serializable;

/**
 * @Description: 用户对应的实体类
 * @Author wengyang
 * @Date 2020年04月02日
 **/
public class User implements Serializable {
    private String username;
    private String password;
    private String pofolioId;

    public User() {
    }

    public User(String username, String password, String pofolioId) {
        this.username = username;
        this.password = password;
        this.pofolioId = pofolioId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPofolioId() {
        return pofolioId;
    }

    public void setPofolioId(String pofolioId) {
        this.pofolioId = pofolioId;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", pofolioId='" + pofolioId + '\'' +
                '}';
    }
}