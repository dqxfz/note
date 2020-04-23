package site.dqxfz.portal.pojo.po;

import org.springframework.data.annotation.Id;

/**
 * @Description: 笔记用户信息对应的实体类
 * @Author wengyang
 * @Date 2020年04月02日
 **/
public class User {
    /**
     * 用户名
     */
    @Id
    private String username;
    /**
     * 根文件夹id
     */
    private String portfolioId;

    public User() {
    }

    public User(String username, String portfolioId) {
        this.username = username;
        this.portfolioId = portfolioId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPortfolioId() {
        return portfolioId;
    }

    public void setPortfolioId(String portfolioId) {
        this.portfolioId = portfolioId;
    }
}