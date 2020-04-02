package site.dqxfz.portal.pojo;

/**
 * @Description: 文件内容对应的实体类
 * @Author wengyang
 * @Date 2020年02月16日
 **/
public class NoteContent {
    private String id;
    private String content;

    public NoteContent() {
    }

    public NoteContent(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
