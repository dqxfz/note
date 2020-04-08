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
    Object uploadFileContent(NoteFile noteFile) throws IOException;

    void uploadFile(byte[] array, Map<String, Object> attributes) throws IOException;

    void createFile(NoteFile noteFile, Map<String, Object> sessionAttributes) throws IOException;

    EasyUiTreeNode saveFileMetaData(Map<String, Object> sessionAttributes) throws IOException;
}
