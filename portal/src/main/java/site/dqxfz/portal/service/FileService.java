package site.dqxfz.portal.service;

import site.dqxfz.portal.pojo.dto.NoteFile;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import java.io.IOException;
import java.util.Map;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
public interface FileService {
    void uploadFile(byte[] array, Map<String, Object> attributes) throws IOException;

    void createFile(NoteFile noteFile, Map<String, Object> sessionAttributes) throws IOException;

    EasyUiTreeNode saveFileMetaData(Map<String, Object> sessionAttributes, String md5) throws IOException;

    void clearFile(String filePathName, String uuidName);

    String uploadImage(String uuidName, String base64) throws IOException;
}
