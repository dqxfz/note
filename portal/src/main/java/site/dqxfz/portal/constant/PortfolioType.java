package site.dqxfz.portal.constant;

import org.springframework.lang.Nullable;

/**
 * @Description: portfolio类型的枚举
 * @Author wengyang
 * @Date 2020年04月2日
 **/

public enum PortfolioType {
    /**
     * 文件夹
     */
    FOLDER(0),
    /**
     * 笔记文件
     */
    NOTEFILE(1),
    /**
     * 上传的图片文件
     */
    UPLOADIMAGE(2),
    /**
     * 上传的非图片文件
     */
    UPLOADNOTIMAGE(3);

    private final int value;

    PortfolioType(int value) {
        this.value = value;
    }

    /**
     * return the integer value of this portfolio type.
     */
    public int value() {
        return this.value;
    }


    /**
     * Return a string representation of this portfolio type.
     */
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    /**
     * Return the enum constant of this type with the specified numeric value.
     * @param portfolioType the numeric value of the enum to be returned
     * @return the enum constant with the specified numeric value
     * @throws IllegalArgumentException if this enum has no constant for the specified numeric value
     */
    public static PortfolioType valueOf(int portfolioType) {
        PortfolioType type = resolve(portfolioType);
        if (type == null) {
            throw new IllegalArgumentException("No matching constant for [" + portfolioType + "]");
        }
        return type;
    }

    /**
     * Resolve the given portfolio type to an PortfolioType, if possible.
     * @param portfolioType the numeric value of the enum to be returned
     * @return
     */
    @Nullable
    public static PortfolioType resolve(int portfolioType) {
        for (PortfolioType type : values()) {
            if (type.value == portfolioType) {
                return type;
            }
        }
        return null;
    }
}
