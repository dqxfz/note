package site.dqxfz.portal.constant;

/**
 * @Description: iconCls枚举类型
 * @Author wengyang
 * @Date 2020年04月2日
 **/
public enum IconClsType {
    /**
     * markdown文件类型
     */
    MARKDOWN("icon-markdown"),
    /**
     * 文件夹类型
     */
    FOLDER("icon-folder"),
    /**
     * MIME类型为application类型
     */
    APPLICATION("icon-application"),
    /**
     * MIME类型为audio类型
     */
    AUDIO("icon-audio"),
    /**
     * MIME类型为image类型
     */
    IMAGE("icon-image"),
    /**
     * MIME类型为text类型
     */
    TEXT("icon-text"),
    /**
     * MIME类型为video类型
     */
    VIDEO("icon-video"),
    /**
     * MIME类型未知
     */
    UNKNOW("icon-unknow");

    private final String value;

    IconClsType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
