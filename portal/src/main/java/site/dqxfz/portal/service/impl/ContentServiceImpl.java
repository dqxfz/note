package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.service.ContentService;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
@Service
public class ContentServiceImpl implements ContentService {
    @Value("${content.templateParam}")
    private String templateParam;

    private final ApplicationContext ac;
    private final TemplateEngine templateEngine;
    private final ContentDao contentDao;

    public ContentServiceImpl(ApplicationContext ac, TemplateEngine templateEngine, ContentDao contentDao) {
        this.ac = ac;
        this.templateEngine = templateEngine;
        this.contentDao = contentDao;
    }

    @Override
    public String getContent(String id, String iconCls) {
        String text = contentDao.getContentById(id);
        String render = templateEngine.process(iconCls, new Context());
        render = render.replace(templateParam,text == null ? "" : text);
        return render;
    }

    @Override
    public void updateContent(Content content) {
        contentDao.updateContentById(content);
    }
}