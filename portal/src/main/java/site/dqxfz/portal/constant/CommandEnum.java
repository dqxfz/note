package site.dqxfz.portal.constant;

import org.springframework.lang.Nullable;

/**
 * @author WENG Yang
 * @date 2020年04月08日
 **/
public enum CommandEnum {
    UPLOAD_START("upload_start"),
    UPLOAD_COMPLETE("upload_complete"),
    RESPONSE_CONTINUE("response_continue"),
    RESPONSE_ERROR("response_error"),
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
