package site.dqxfz.portal.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.pojo.po.Portfolio;

/**
 * @Description: 转换从数据库查询的Document为Portfolio对象，主要目的是为了根据枚举类的属性值转换为对应的枚举类型
 * @Author wengyang
 * @Date 2020年04月03日
 **/
public class PortfolioReadConverter implements Converter<Document, Portfolio> {
    @Override
    public Portfolio convert(Document document) {
        Portfolio portfolio = new Portfolio();
        portfolio.setFatherId(document.getString("fatherId"));
        // 根据枚举类型属性值转换为对应的枚举类
        portfolio.setIconCls(IconClsType.getValueOf(document.getString("iconCls")));
        portfolio.setId(document.getObjectId("_id").toString());
        portfolio.setName(document.getString("name"));
        return portfolio;
    }
}