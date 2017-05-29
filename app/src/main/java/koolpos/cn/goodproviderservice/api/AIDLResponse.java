package koolpos.cn.goodproviderservice.api;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/14.
 */

public class AIDLResponse implements Serializable{
    private static final long serialVersionUID = -5292951167922066986L;
    public static final String SUCCESS="SUCCESS";
    public static final String FAIL="FAIL";
    private String data;
    private String message;
    private int code;
    public AIDLResponse(){
        message=SUCCESS;
        code=0;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
