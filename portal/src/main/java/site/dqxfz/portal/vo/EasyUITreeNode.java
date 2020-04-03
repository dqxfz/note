package site.dqxfz.portal.vo;

import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.constant.PortfolioType;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月13日
 **/
public class EasyUITreeNode {
    private String id;
    private String text;
    private String state;
    private PortfolioType type;
    private IconClsType iconCls;
    private String fatherId;

    public EasyUITreeNode() {
    }

    public EasyUITreeNode(String id, String text, String state, PortfolioType type, IconClsType iconCls, String fatherId) {
        this.id = id;
        this.text = text;
        this.state = state;
        this.type = type;
        this.iconCls = iconCls;
        this.fatherId = fatherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public PortfolioType getType() {
        return type;
    }

    public void setType(PortfolioType type) {
        this.type = type;
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
}
