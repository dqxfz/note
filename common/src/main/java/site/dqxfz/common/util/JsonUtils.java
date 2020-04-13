package site.dqxfz.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * @author WENG Yang
 * @date 2020年04月13日
 **/
public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 对象转json字符串
     * @param obj 将要转换的对象
     * @return 转换后的json字符串
     */
    public static String objectToJson(Object obj) throws JsonProcessingException {
        if(obj == null){
            return null;
        }
        String json = objectMapper.writeValueAsString(obj);
        return json;
    }

    /**
     * json字符串转对象
     * @param json 将要转换的字符串
     * @param objClass 对象的class
     * @param <T> 对象的类型
     * @return 转换后的对象
     */
    public static <T> T jsonToObject(String json, Class<T> objClass) throws IOException {
        if(StringUtils.isEmpty(json)) {
            return null;
        }
        T obj = objectMapper.readValue(json, objClass);
        return obj;
    }
}