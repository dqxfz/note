package site.dqxfz.portal.pojo.po;

/**
 * @Description: 文件内容对应的实体类
 * @Author wengyang
 * @Date 2020年02月16日
 **/
public class Content {
    private String id;
    private String text;

    public Content() {
    }

    public Content(String id, String text) {
        this.id = id;
        this.text = text;
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

    @Override
    public String toString() {
        return "Content{" +
                "id='" + id + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
