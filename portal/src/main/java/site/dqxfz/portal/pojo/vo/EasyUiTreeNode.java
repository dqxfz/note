package site.dqxfz.portal.pojo.vo;

import site.dqxfz.portal.constant.IconClsType;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月13日
 **/
public class EasyUiTreeNode {
    private String id;
    private String text;
    private String state;
    private IconClsType iconCls;
    private String fatherId;

    public EasyUiTreeNode() {
    }

    public EasyUiTreeNode(String id, String text, String state, IconClsType iconCls, String fatherId) {
        this.id = id;
        this.text = text;
        this.state = state;
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
