package site.dqxfz.portal.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.lang.Nullable;

/**
 * @Description: iconCls枚举类型
 * @Author wengyang
 * @Date 2020年04月2日
 **/
public enum IconClsEnum {
    /**
     * markdown文件
     */
    MARKDOWN("markdown"),
    /**
     * 文件夹
     */
    FOLDER("folder"),
    /**
     * 协同文件夹
     */
    COORDINATION("coordination"),
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

    IconClsEnum(String value) {
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
     * 根据iconClsValue获取对应的枚举类
     * @param iconClsValue 将要获取枚举类的值
     * @return 返回value为iconClsValue的枚举类，如果为空，则抛出异常IllegalArgumentException
     */
    public static IconClsEnum getValueOf(String iconClsValue) {
        IconClsEnum iconCls = resolve(iconClsValue);
        return iconCls;
    }

    @Nullable
    public static IconClsEnum resolve(String iconClsValue) {
        for (IconClsEnum iconCls : values()) {
            if (iconCls.value.equals(iconClsValue)) {
                return iconCls;
            }
        }
        return null;
    }
}
