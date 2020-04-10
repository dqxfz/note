package site.dqxfz.portal.pojo.vo;

import site.dqxfz.portal.constant.CommandType;

/**
 * websocket上传消息内容格式
 * @author WENG Yang
 * @date 2020年04月08日
 **/
public class FileDTO {
    private String command;
    private String data;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}