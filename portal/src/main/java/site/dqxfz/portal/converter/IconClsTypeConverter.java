package site.dqxfz.portal.converter;

import org.springframework.core.convert.converter.Converter;
import site.dqxfz.portal.constant.IconClsEnum;

/**
 * 转换请求中的String参数为controller中对应IconClsType类
 * @author WENG Yang
 * @date 2020年04月04日
 **/
public class IconClsTypeConverter implements Converter<String, IconClsEnum> {
    @Override
    public IconClsEnum convert(String source) {
        return IconClsEnum.getValueOf(source);
    }
}