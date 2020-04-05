package site.dqxfz.portal.service;

import site.dqxfz.portal.pojo.dto.NoteFile;

import java.io.IOException;

/**
 * @author WENG Yang
 * @date 2020年04月05日
 **/
public interface FileService {
    Object uploadFileContent(NoteFile noteFile) throws IOException;
}
