package site.dqxfz.portal.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;

/**
 * @Description: iconCls枚举类型
 * @Author wengyang
 * @Date 2020年04月2日
 **/
public enum IconClsType {
    /**
     * markdown文件
     */
    MARKDOWN("markdown"),
    /**
     * 文件夹
     */
    FOLDER("folder"),
    /**
     * MIME类型为application
     */
    APPLICATION("application"),
    /**
     * MIME类型为audio
     */
    AUDIO("audio"),
    /**
     * MIME类型为image
     */
    IMAGE("image"),
    /**
     * MIME类型为text
     */
    TEXT("text"),
    /**
     * MIME类型为video
     */
    VIDEO("video"),
    /**
     * MIME类型未知
     */
    UNKNOWN("unknown");

    private final String value;

    IconClsType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
    /**
     * 根据typeValue获取对应的枚举类
     * @param iconCls 将要获取枚举类的值
     * @return 返回value为typeValue的枚举类，如果为空，则抛出异常IllegalArgumentException
     */
    public static IconClsType getValueOf(String iconCls) {
        IconClsType iconClsType = resolve(iconCls);
        return iconClsType;
    }

    @Nullable
    public static IconClsType resolve(String typeValue) {
        for (IconClsType iconClsType : values()) {
            if (iconClsType.value.equals(typeValue)) {
                return iconClsType;
            }
        }
        return null;
    }
}
