package site.dqxfz.common.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author WENG Yang
 * @date 2020年04月11日
 **/
public class ResourceUtils {
    private static Logger logger = LogManager.getLogger(ResourceUtils.class);
    public static String getResource(ApplicationContext ac, String sourcePath) {
        Resource template = ac.getResource("classpath:" + sourcePath);
        InputStream inputStream = null;
        InputStreamReader in = null;
        BufferedReader reader = null;
        try {
            inputStream = template.getInputStream();
            in = new InputStreamReader(inputStream);
            reader = new BufferedReader(in);
            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line).append("\n");
                line = reader.readLine();
            }
            return buffer.toString();

        } catch (IOException e) {
            logger.error(e);
        } finally {
            try {
                reader.close();
                in.close();
                inputStream.close();
            } catch (IOException e) {
                logger.error(e);
            }
        }
        return "";

    }
}