package site.dqxfz.portal.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import site.dqxfz.common.util.ResourceUtils;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.service.ContentService;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
@Service
public class ContentServiceImpl implements ContentService {
    @Value("${content.template.placeholder}")
    private String contentTemplatePlaceholder;
    @Value("${file.ftp.url}")
    private String fileFtpUrl;

    private final ApplicationContext ac;
    private final ContentDao contentDao;

    public ContentServiceImpl(ApplicationContext ac, ContentDao contentDao) {
        this.ac = ac;
        this.contentDao = contentDao;
    }

    @Override
    public String getContent(String id, String iconCls) {
        String text = contentDao.getContentById(id);
        IconClsEnum iconClsType = IconClsEnum.getValueOf(iconCls);
        // 如果是上传的文件，则返回文件链接
        if(!(iconClsType == IconClsEnum.FOLDER || iconClsType == IconClsEnum.MARKDOWN)) {
            text = "http://" + fileFtpUrl + "/" + text;
        }
        // 获取模板文件，然后填充内容到模板文件中
        String sourcePath = "static/template/" + iconCls + ".html";
        String resource = ResourceUtils.getResource(ac, sourcePath);
        String render = resource.replace(contentTemplatePlaceholder,text == null ? "" : text);
        return render;
    }

    @Override
    public void updateContent(Content content) {
        contentDao.updateContentById(content);
    }
}