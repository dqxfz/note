package site.dqxfz.sso.pojo.po;

/**
 * @Description: 用户对应的实体类
 * @Author wengyang
 * @Date 2020年04月02日
 **/
public class User {
    private String id;
    private String username;
    private String password;
    private String pofolioId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}