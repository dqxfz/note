package site.dqxfz.portal.pojo.dto;

/**
 * websocket上传文件内容格式
 * @author WENG Yang
 * @date 2020年04月05日
 **/
public class NoteFile {
    /**
     * 上传是否结束
     */
    private boolean complete;
    /**
     * 上传文件的第几段
     */
    private Integer snippetNum;
    /**
     * 文件的原始名称
      */
    private String name;
    /**
     * 生成的唯一文件名称
     */
    private String uuidName;
    /**
     * 文件MIME类型
     */
    private String type;
    /**
     * 文件内容
     */
    private String content;

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Integer getSnippetNum() {
        return snippetNum;
    }

    public void setSnippetNum(Integer snippetNum) {
        this.snippetNum = snippetNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuidName() {
        return uuidName;
    }

    public void setUuidName(String uuidName) {
        this.uuidName = uuidName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}