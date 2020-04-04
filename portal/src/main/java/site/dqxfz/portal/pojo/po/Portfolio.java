package site.dqxfz.portal.pojo.po;

import site.dqxfz.portal.constant.IconClsType;

/**
 * @Description: 文件或者文件夹所对应的实体类
 * @Author wengyang
 * @Date 2020年02月12日
 **/

public class Portfolio {
    private String id;
    private String name;
    private IconClsType iconCls;
    private String fatherId;

    public Portfolio() {
    }

    public Portfolio(String name, IconClsType iconCls, String fatherId) {
        this.name = name;
        this.iconCls = iconCls;
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

    public IconClsType getIconCls() {
        return iconCls;
    }

    public void setIconCls(IconClsType iconCls) {
        this.iconCls = iconCls;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", iconCls=" + iconCls +
                ", fatherId='" + fatherId + '\'' +
                '}';
    }
}
