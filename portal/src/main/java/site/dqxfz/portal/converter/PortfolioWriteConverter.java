package site.dqxfz.portal.converter;

import org.bson.Document;
import org.springframework.core.convert.converter.Converter;
import site.dqxfz.portal.pojo.po.Portfolio;

/**
 * @Description: 转换Portfolio对象为即将插入到数据库的Document,主要目的是将枚举类型属性值存入Mongo数据库，而不是将枚举类的字面量存入数据库
 * @Author wengyang
 * @Date 2020年04月03日
 **/
public class PortfolioWriteConverter implements Converter<Portfolio, Document> {
    @Override
    public Document convert(Portfolio portfolio) {
        Document document = new Document();
        document.put("_id",portfolio.getId());
        document.put("name",portfolio.getName());
        //将枚举类的value值存入数据库
        document.put("iconCls",portfolio.getIconCls().getValue());
        document.put("fatherId",portfolio.getFatherId());
        return document;
    }
}