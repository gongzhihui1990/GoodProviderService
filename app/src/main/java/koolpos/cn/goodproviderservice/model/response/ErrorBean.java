package koolpos.cn.goodproviderservice.model.response;

import koolpos.cn.goodproviderservice.model.BaseBean;

/**
 * Created by caroline on 2018/7/25.
 */

public class ErrorBean extends BaseBean{
    private int code;
    private   String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
