package site.dqxfz.portal.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.dto.NoteFile;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.FileService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
@Service
public class FileServiceImpl implements FileService {
    Logger logger = LogManager.getLogger(this.getClass());

    @Value("${file.path}")
    private String filePath;

    private final PortfolioDao portfolioDao;
    private final ContentDao contentDao;

    public FileServiceImpl(PortfolioDao portfolioDao, ContentDao contentDao) {
        this.portfolioDao = portfolioDao;
        this.contentDao = contentDao;
    }

    @Override
    public Object uploadFileContent(NoteFile noteFile) throws IOException {
//        File file = new File(filePath + noteFile.getUuidName());
//        if(!file.exists()) {
//            // 创建文件
//            file.createNewFile();
//        }
//        byte[] bytes = new BASE64Decoder().decodeBuffer(noteFile.getContent());
//        FileOutputStream outputStream = new FileOutputStream(file, true);
//        outputStream.write(bytes);
//        outputStream.close();
//        // 文件传输完成，保存文件元信息到数据库
//        if(noteFile.isComplete()) {
//          upload_complete  EasyUiTreeNode easyUiTreeNode = savePortfolio(noteFile);
//            return easyUiTreeNode;
//        } else {
//            // 返回要传输的下一段
//            return noteFile.getSnippetNum() + 1;
//        }
        return null;
    }

    @Override
    public void uploadFile(byte[] bytes, Map<String, Object> sessionAttributes) throws IOException {
        File file = (File) sessionAttributes.get("file");
        FileOutputStream outputStream = new FileOutputStream(file, true);
        outputStream.write(bytes);
        outputStream.close();
    }

    @Override
    public void createFile(NoteFile noteFile, Map<String, Object> sessionAttributes) throws IOException {
        logger.info(noteFile.getName() + "创建时间：" + Instant.now());
        sessionAttributes.put("start", Instant.now());
        File file = new File(filePath + noteFile.getUuidName());
        if(!file.exists()) {
            file.createNewFile();
        }
        sessionAttributes.put("file", file);
    }

    @Override
    public EasyUiTreeNode saveFileMetaData(Map<String, Object> sessionAttributes) throws IOException {
        // 保存文件元信息
        NoteFile noteFile = (NoteFile) sessionAttributes.get("noteFile");
        EasyUiTreeNode easyUiTreeNode = savePortfolio(noteFile);
        Instant start = (Instant) sessionAttributes.get("start");
        logger.info(start);
        Duration duration = Duration.between(start, Instant.now());
        logger.info(noteFile.getName() + "结束时间：" + Instant.now());
        logger.info(noteFile.getName() + " 上传完成，共耗时： " + duration.toMillis() + "ms");
        return easyUiTreeNode;
    }

    /**
     * 保存文件元信息到数据库
     * @param noteFile
     * @return
     */
    private EasyUiTreeNode savePortfolio(NoteFile noteFile) {
        String type = noteFile.getType();
        int endIndex = type.indexOf('/');
        IconClsType iconClsType = null;
        if(endIndex != -1) {
            iconClsType = IconClsType.getValueOf(type.substring(0, endIndex));
        }
        if(iconClsType == null) {
            iconClsType = IconClsType.UNKNOWN;
            type = iconClsType.getValue();
        }
        // 保存元信息到数据库
        Portfolio portfolio = portfolioDao.savePortfolio(new Portfolio(
                noteFile.getName(),
                type,
                iconClsType,
                noteFile.getFatherId()
        ));
        // 保存文件的地址到数据库
        contentDao.saveContent(new Content(portfolio.getId(), noteFile.getUuidName()));
        EasyUiTreeNode easyUiTreeNode = new EasyUiTreeNode(
                portfolio.getId(),
                portfolio.getName(),
                portfolio.getIconCls().equals(IconClsType.FOLDER) ? "closed" : "open",
                portfolio.getIconCls(),
                portfolio.getFatherId());
        return easyUiTreeNode;
    }
}