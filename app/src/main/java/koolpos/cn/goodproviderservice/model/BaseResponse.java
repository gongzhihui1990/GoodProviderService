package koolpos.cn.goodproviderservice.model;

/**
 * Created by Administrator on 2017/6/5.
 */

public class BaseResponse<T> extends BaseBean{
    private static final long serialVersionUID = -3812140468003097002L;
    private String status;
    private String message;
    private String code;
    private T data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
