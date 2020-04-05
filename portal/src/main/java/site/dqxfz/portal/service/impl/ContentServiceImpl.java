package site.dqxfz.portal.service.impl;

import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.service.ContentService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
@Service
public class ContentServiceImpl implements ContentService {
    private final String ICON_PREFIX = "icon-";

    private final String TEXT_SIGN = "${text}";

    private final TemplateEngine templateEngine;

    private final ContentDao contentDao;

    public ContentServiceImpl(TemplateEngine templateEngine, ContentDao contentDao) {
        this.templateEngine = templateEngine;
        this.contentDao = contentDao;
    }

    @Override
    public String getContent(String id, String iconCls) {
        String text = contentDao.getContentById(id);
        Context context = new Context();
//        context.setVariable("text",text);
        String render = templateEngine.process(iconCls.substring(ICON_PREFIX.length()), context);
        render = render.replace(TEXT_SIGN,text);
        return render;
    }

    @Override
    public void updateContent(Content content) {
        contentDao.updateContentById(content);
    }
}