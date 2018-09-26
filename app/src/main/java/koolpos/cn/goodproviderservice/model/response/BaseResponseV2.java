package koolpos.cn.goodproviderservice.model.response;

import koolpos.cn.goodproviderservice.model.BaseBean;

/**
 * Created by Administrator on 2017/6/5.
 */

public class BaseResponseV2<T> extends BaseBean {
    private static final long serialVersionUID = -3812140468003097002L;
    private boolean success;
    private ErrorBean error;
    private T result;

}
