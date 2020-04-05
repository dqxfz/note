package site.dqxfz.portal.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.dto.NoteFile;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.FileService;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
@Service
public class FileServiceImpl implements FileService {
    private final String FILE_PATH = "/home/wy/Mine/temp/file/";

    private final PortfolioDao portfolioDao;
    private final ContentDao contentDao;

    public FileServiceImpl(PortfolioDao portfolioDao, ContentDao contentDao) {
        this.portfolioDao = portfolioDao;
        this.contentDao = contentDao;
    }

    @Override
    public Object uploadFileContent(NoteFile noteFile) throws IOException {
        // 文件传输完成，保存文件元信息到数据库
        if(noteFile.isComplete()) {
            String type = noteFile.getType();
            IconClsType iconClsType = IconClsType.getValueOf(type.substring(0, type.indexOf('/')));
            // 保存元信息到数据库
            Portfolio portfolio = portfolioDao.savePortfolio(new Portfolio(
                    noteFile.getName(),
                    type,
                    iconClsType == null ? IconClsType.UNKNOWN : iconClsType,
                    noteFile.getFatherId()
            ));
            // 保存文件的地址到数据库
            contentDao.saveContent(new Content(portfolio.getId(), noteFile.getUuidName()));
            return new EasyUiTreeNode(
                    portfolio.getId(),
                    portfolio.getName(),
                    portfolio.getIconCls().equals(IconClsType.FOLDER) ? "closed" : "open",
                    portfolio.getIconCls(),
                    portfolio.getFatherId());
        } else {
            File file = new File(FILE_PATH + noteFile.getUuidName());
            if(!file.exists()) {
                // 创建文件
                file.createNewFile();
            }
            byte[] bytes = new BASE64Decoder().decodeBuffer(noteFile.getContent());
            FileOutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(bytes);
            outputStream.close();
            return noteFile.getSnippetNum() + 1;
        }
    }
}