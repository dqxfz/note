package site.dqxfz.sso.pojo.po;

/**
 * @Description: 用户对应的实体类
 * @Author wengyang
 * @Date 2020年04月02日
 **/
public class User {
    private String username;
    private String password;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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
}
