package site.dqxfz.portal.service.impl;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.dqxfz.common.util.FtpUtils;
import site.dqxfz.portal.constant.IconClsEnum;
import site.dqxfz.portal.dao.ContentDao;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.vo.NoteFile;
import site.dqxfz.portal.pojo.po.Content;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;
import site.dqxfz.portal.service.FileService;
import sun.misc.BASE64Decoder;

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

    @Value("${file.ftp.url}")
    private String fileFtpUrl;
    @Value("${file.ftp.port}")
    private Integer fileFtpPort;
    @Value("${file.ftp.user}")
    private String fileFtpUser;
    @Value("${file.ftp.password}")
    private String fileFtpPassword;
    @Value("${file.ftp.passive}")
    private Boolean passive;

    private final PortfolioDao portfolioDao;
    private final ContentDao contentDao;

    public FileServiceImpl(PortfolioDao portfolioDao, ContentDao contentDao) {
        this.portfolioDao = portfolioDao;
        this.contentDao = contentDao;
    }

    @Override
    public void uploadFile(byte[] bytes, Map<String, Object> sessionAttributes) throws Exception {
        NoteFile noteFile = (NoteFile) sessionAttributes.get("noteFile");
        FTPClient ftpClient = (FTPClient) sessionAttributes.get("ftpClient");
        FtpUtils.uploadBytes(bytes, noteFile.getUuidName(), "./", ftpClient);
    }

    @Override
    public void createFile(NoteFile noteFile, Map<String, Object> sessionAttributes) throws Exception {
        logger.info(noteFile.getName() + "创建时间：" + Instant.now());
        sessionAttributes.put("start", Instant.now());
        FTPClient ftpClient = FtpUtils.getFTPClient(fileFtpUrl, fileFtpPort, fileFtpUser, fileFtpPassword, passive);
        sessionAttributes.put("ftpClient", ftpClient);
    }

    @Override
    public EasyUiTreeNode saveFileMetaData(Map<String, Object> sessionAttributes) throws Exception {
        NoteFile noteFile = (NoteFile) sessionAttributes.get("noteFile");
        FTPClient ftpClient = (FTPClient) sessionAttributes.get("ftpClient");
        long fileSize = FtpUtils.getFileSize(noteFile.getUuidName(), ftpClient);
        // 文件上传出错
        if(fileSize != noteFile.getSize()) {
            return null;
        }
        // 保存文件元信息
        EasyUiTreeNode easyUiTreeNode = savePortfolio(noteFile);
        Instant start = (Instant) sessionAttributes.get("start");
        Duration duration = Duration.between(start, Instant.now());
        logger.info(noteFile.getName() + "结束时间：" + Instant.now());
        logger.info(noteFile.getName() + " 上传完成，共耗时： " + duration.toMillis() + "ms");
        return easyUiTreeNode;
    }

    @Override
    public void closeFtp(Map<String, Object> sessionAttributes) throws Exception {
        NoteFile noteFile = (NoteFile) sessionAttributes.get("noteFile");
        FTPClient ftpClient = (FTPClient) sessionAttributes.get("ftpClient");
        Content content = contentDao.getContentBytext(noteFile.getUuidName());
        // 如果数据库没有已经保存好了的文件信息，说明文件没有上传完成
        if(content == null) {
            FtpUtils.deleteFile(noteFile.getUuidName(), ftpClient);
        }
        FtpUtils.closeFTP(ftpClient);
    }

    @Override
    public String uploadImage(String uuidName, String base64) throws Exception {
        String imageBase64 = base64.substring(base64.indexOf(',') + 1);
        BASE64Decoder d = new BASE64Decoder();
        byte[] bytes = d.decodeBuffer(imageBase64);
        FTPClient ftpClient = FtpUtils.getFTPClient(fileFtpUrl, fileFtpPort, fileFtpUser, fileFtpPassword, passive);
        FtpUtils.uploadBytes(bytes, uuidName, "image", ftpClient);
        FtpUtils.closeFTP(ftpClient);
        return "![](http://" + fileFtpUrl + "/image/" + uuidName + ")";
    }

    /**
     * 保存文件元信息到数据库
     * @param noteFile
     * @return
     */
    private EasyUiTreeNode savePortfolio(NoteFile noteFile) {
        String type = noteFile.getType();
        int endIndex = type.indexOf('/');
        IconClsEnum iconCls = null;
        if(endIndex != -1) {
            iconCls = IconClsEnum.getValueOf(type.substring(0, endIndex));
        }
        if(iconCls == null) {
            iconCls = IconClsEnum.UNKNOWN;
            type = iconCls.getValue();
        }
        // 保存元信息到数据库
        Portfolio portfolio = portfolioDao.savePortfolio(new Portfolio(
                noteFile.getName(),
                type,
                iconCls,
                noteFile.getFatherId()
        ));
        // 保存文件的地址到数据库
        contentDao.saveContent(new Content(portfolio.getId(), noteFile.getUuidName()));
        EasyUiTreeNode easyUiTreeNode = new EasyUiTreeNode(
                portfolio.getId(),
                portfolio.getName(),
                portfolio.getIconCls().equals(IconClsEnum.FOLDER) ? "closed" : "open",
                portfolio.getIconCls(),
                portfolio.getFatherId());
        return easyUiTreeNode;
    }
}