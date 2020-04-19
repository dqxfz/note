package site.dqxfz.common.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author WENG Yang
 * @date 2020年04月19日
 **/
public class HttpUtils {
    public static String getWholeRequestUrl(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return StringUtils.isEmpty(queryString) ? String.valueOf(request.getRequestURL()) : request.getRequestURL() + "?" + queryString;
    }

    public static String getServletAndPort(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort();

    }
}