package site.dqxfz.portal.pojo.vo;

/**
 * @Description: 响应数据格式
 * @Author wengyang
 * @Date 2020年03月18日
 **/
public class ActionResult {
    // 默认message信息
    public static final String SUCCESS_MESSAGE = "执行成功";
    public static final String FAILURE_MESSAGE = "执行失败";
    // 提示信息
    private String message;
    // 错误码
    private Integer errorCode;
    // 返回数据
    private Object data;

    public ActionResult(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ActionResult(String message, Integer errorCode, Object data) {
        this.message = message;
        this.errorCode = errorCode;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}