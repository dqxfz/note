package site.dqxfz.portal.service;

import site.dqxfz.portal.pojo.vo.NoteFile;
import site.dqxfz.portal.pojo.vo.EasyUiTreeNode;

import java.io.IOException;
import java.util.Map;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
public interface FileService {
    void uploadFile(byte[] array, Map<String, Object> attributes) throws Exception;

    void createFile(NoteFile noteFile, Map<String, Object> sessionAttributes) throws Exception;

    EasyUiTreeNode saveFileMetaData(Map<String, Object> sessionAttributes) throws Exception;

    void closeFtp(Map<String, Object> attributes) throws Exception;

    String uploadImage(String uuidName, String base64) throws Exception;
}
