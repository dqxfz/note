package site.dqxfz.common.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author wengyang
 * @Date 2020年02月13日
 **/
public class EasyuiTreeNode {
    private String id;
    private String text;
    private String state;
    private Integer type;
    private String fatherId;
    private List<EasyuiTreeNode> children = new ArrayList<>();

    public EasyuiTreeNode() {
    }

    public EasyuiTreeNode(String id, String text, String state, Integer type, String fatherId, List<EasyuiTreeNode> children) {
        this.id = id;
        this.text = text;
        this.state = state;
        this.type = type;
        this.fatherId = fatherId;
        this.children = children;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public List<EasyuiTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<EasyuiTreeNode> children) {
        this.children = children;
    }
}
