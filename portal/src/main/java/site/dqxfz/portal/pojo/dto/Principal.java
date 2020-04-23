package site.dqxfz.portal.pojo.dto;

/**
 * @author WENG Yang
 * @date 2020年04月22日
 **/
public class Principal {
    private String userName;
    private String id;
    private String type;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Principal{" +
                "userName='" + userName + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
