package site.dqxfz.portal.constant;

import org.springframework.lang.Nullable;

/**
 * @author WENG Yang
 * @date 2020年04月08日
 **/
public enum CommandEnum {
    /**
     * 上传开始，服务器接收到此命令后创建文件
     */
    UPLOAD_START("upload_start"),
    /**
     * 上传结束，服务器接收到此命令后开始保存文件元信息到数据库中
     */
    UPLOAD_COMPLETE("upload_complete"),
    /**
     * 继续上传，每当服务器将客户端上传的一个片段保存成功后就会发送此命令给客户端，以通知客户端可以继续传输文件的下一个片段了
     */
    RESPONSE_CONTINUE("response_continue"),
    /**
     * 文件上传失败，服务器保存文件元信息的时候会检查文件的md5值是否正确，如果文件内容出错则返回此值
     */
    RESPONSE_ERROR("response_error"),
    /**
     * 文件元信息保存完成，整个上传流程结束
     */
    RESPONSE_COMPLETE("response_complete");

    private final String value;

    CommandEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static CommandEnum getValueOf(String commandValue) {
        CommandEnum command = resolve(commandValue);
        if (command == null) {
            throw new IllegalArgumentException("No matching constant for [" + commandValue + "]");
        }
        return command;
    }

    @Nullable
    public static CommandEnum resolve(String commandValue) {
        for (CommandEnum command : values()) {
            if (command.value.equals(commandValue)) {
                return command;
            }
        }
        return null;
    }
}