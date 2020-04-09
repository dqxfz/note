package site.dqxfz.portal.constant;

import org.springframework.lang.Nullable;

/**
 * @author WENG Yang
 * @date 2020年04月08日
 **/
public enum CommandType {
    UPLOAD_START("upload_start"),
    UPLOAD_COMPLETE("upload_complete"),
    RESPONSE_CONTINUE("response_continue"),
    RESPONSE_ERROR("response_error"),
    RESPONSE_COMPLETE("response_complete");

    private final String value;

    CommandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static CommandType getValueOf(String typeValue) {
        CommandType commandType = resolve(typeValue);
        if (commandType == null) {
            throw new IllegalArgumentException("No matching constant for [" + typeValue + "]");
        }
        return commandType;
    }

    @Nullable
    public static CommandType resolve(String typeValue) {
        for (CommandType commandType : values()) {
            if (commandType.value.equals(typeValue)) {
                return commandType;
            }
        }
        return null;
    }
}
