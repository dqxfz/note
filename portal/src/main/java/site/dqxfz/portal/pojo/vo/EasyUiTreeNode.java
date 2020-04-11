package site.dqxfz.portal.pojo.vo;

import site.dqxfz.portal.constant.IconClsEnum;

/**
 * @Description: 返回给EasyUI Tree控件的数据类
 * @Author wengyang
 * @Date 2020年02月13日
 **/
public class EasyUiTreeNode {
    private String id;
    private String text;
    private String state;
    private IconClsEnum iconCls;
    private String fatherId;

    public EasyUiTreeNode() {
    }

    public EasyUiTreeNode(String id, String text, String state, IconClsEnum iconCls, String fatherId) {
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

    public IconClsEnum getIconCls() {
        return iconCls;
    }

    public void setIconCls(IconClsEnum iconCls) {
        this.iconCls = iconCls;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }
}
