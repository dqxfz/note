package site.dqxfz.portal.pojo;

import site.dqxfz.portal.constant.PortfolioType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 文件或者文件夹所对应的实体类
 * @Author wengyang
 * @Date 2020年02月12日
 **/

public class Portfolio {
    private String id;
    private String name;
    private PortfolioType type;
    private String fatherId;
    private List<String> childIds = new ArrayList<>();

    public Portfolio() {
    }

    public Portfolio(String name, PortfolioType type, String fatherId) {
        this.name = name;
        this.type = type;
        this.fatherId = fatherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PortfolioType getType() {
        return type;
    }

    public void setType(PortfolioType type) {
        this.type = type;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public List<String> getChildIds() {
        return childIds;
    }

    public void setChildIds(List<String> childIds) {
        this.childIds = childIds;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", fatherId='" + fatherId + '\'' +
                ", childIds=" + childIds +
                '}';
    }
}
