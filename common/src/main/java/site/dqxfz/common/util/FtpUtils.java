package site.dqxfz.common.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.util.concurrent.TimeUnit;

/**
 * 简单操作FTP工具类 ,此工具类支持中文文件名，不支持中文目录
 * 如果需要支持中文目录，需要 new String(path.getBytes("UTF-8"),"ISO-8859-1") 对目录进行转码
 * @author WZH
 * 
 */
public class FtpUtils {

    public static void uploadBytes(byte[] bytes, String uuidName, String path, FTPClient ftpClient) throws Exception {
        if(!ftpClient.changeWorkingDirectory(path)) {
            ftpClient.makeDirectory(path);
            if(!ftpClient.changeWorkingDirectory(path)) {
                throw new Exception("ftp changeWorkingDirectory failed");
            }
        }
        OutputStream outputStream = ftpClient.appendFileStream(uuidName);
        outputStream.write(bytes);
        outputStream.close();
        if (!ftpClient.completePendingCommand()) {
            ftpClient.disconnect();
            throw new Exception("ftp appendFileStream failed");
        }
    }

    /**
     * 获取FTPClient对象
     * @param ftpHost 服务器IP
     * @param ftpPort 服务器端口号
     * @param ftpUserName 用户名
     * @param ftpPassword 密码
     * @return FTPClient
     */
    public static FTPClient getFTPClient(String ftpHost, int ftpPort,
            String ftpUserName, String ftpPassword) throws Exception {
        FTPClient ftpClient = new FTPClient();
        // 连接FPT服务器,设置IP及端口
        ftpClient.connect(ftpHost, ftpPort);
        // 设置用户名和密码
        ftpClient.login(ftpUserName, ftpPassword);
        //设置上传文件的类型为二进制类型
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
        // 设置为本地被动模式
//        ftpClient.enterLocalPassiveMode();
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            ftpClient.disconnect();
            throw new Exception("ftp connection failed");
        }
        return ftpClient;
    }
    
    /**
     * 关闭FTP方法
     * @param ftpClient
     * @return
     */
    public static void closeFTP(FTPClient ftpClient) throws Exception {
        ftpClient.logout();
        if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            ftpClient.disconnect();
            throw new Exception("ftp logout failed");
        }
    }

    public static void deleteFile(String uuidName, FTPClient ftpClient) throws Exception {
        if (!ftpClient.deleteFile(uuidName)) {
            ftpClient.disconnect();
            throw new Exception("ftp file delete failed");
        }
    }

    public static String getFileMd5(String uuidName, FTPClient ftpClient) throws Exception {
        InputStream inputStream = ftpClient.retrieveFileStream(uuidName);
        String endFileMd5 = DigestUtils.md5DigestAsHex(inputStream);
        inputStream.close();
        if (!ftpClient.completePendingCommand()) {
            ftpClient.disconnect();
            throw new Exception("ftp getFileMd5 failed");
        }
        return endFileMd5;
    }

    public static long getFileSize(String uuidName, FTPClient ftpClient) throws IOException {
        FTPFile[] ftpFiles = ftpClient.listFiles();
        for(FTPFile ftpFile : ftpFiles) {
            if(ftpFile.getName().equals(uuidName)) {
                return ftpFile.getSize();
            }
        }
        return -1;
    }
}
