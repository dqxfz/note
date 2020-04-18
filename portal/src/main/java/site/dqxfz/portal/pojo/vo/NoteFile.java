package site.dqxfz.portal.pojo.vo;

/**
 * websocket上传文件元信息格式
 * @author WENG Yang
 * @date 2020年04月05日
 **/
public class NoteFile {
    /**
     * 父portfolio的id
     */
    private String fatherId;
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
     * 文件大小
     */
    private Long size;

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
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
}