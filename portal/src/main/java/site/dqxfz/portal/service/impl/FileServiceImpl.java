package site.dqxfz.portal.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import site.dqxfz.portal.constant.IconClsType;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.dto.NoteFile;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.FileService;

import java.io.*;
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
    public void uploadFile(byte[] bytes, Map<String, Object> sessionAttributes) throws IOException {
        String filePathName = (String) sessionAttributes.get("filePathName");
        FileOutputStream outputStream = new FileOutputStream(filePathName, true);
        outputStream.write(bytes);
        outputStream.close();
    }

    @Override
    public void createFile(NoteFile noteFile, Map<String, Object> sessionAttributes) throws IOException {
        logger.info(noteFile.getName() + "创建时间：" + Instant.now());
        sessionAttributes.put("start", Instant.now());
        String filePathName = filePath + noteFile.getUuidName();
        File file = new File(filePathName);
        if(!file.exists()) {
            file.createNewFile();
        }
        sessionAttributes.put("filePathName", filePathName);
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
        String filePathName = (String) sessionAttributes.get("filePathName");
        File file = new File(filePathName);
        String size = String.valueOf(file.length());
        System.out.println(size);
        System.out.println(noteFile.getSize());
        logger.info(size.equals(noteFile.getSize()));
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