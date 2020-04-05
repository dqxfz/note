package site.dqxfz.portal.service.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import site.dqxfz.portal.dao.PortfolioDao;
import site.dqxfz.portal.pojo.dto.NoteFile;
import site.dqxfz.portal.pojo.po.Portfolio;
import site.dqxfz.portal.service.FileService;

import java.io.File;
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

    public FileServiceImpl(PortfolioDao portfolioDao) {
        this.portfolioDao = portfolioDao;
    }

    @Override
    public String createFile(String name) throws IOException {
        // 生成唯一文件名
        String uuidName = UUID.randomUUID().toString();
        File file = new File(FILE_PATH + uuidName);
        file.createNewFile();
        return uuidName;
    }

    @Override
    public void uploadFileContent(NoteFile noteFile) {
        // 文件传输完成，保存文件元信息到数据库
//        if(noteFile.isComplete()) {
//            Portfolio portfolio = portfolioDao.savePortfolio(null);
//            return portfolio;
//        } else {
//
//        }
//        return 0;
    }
}