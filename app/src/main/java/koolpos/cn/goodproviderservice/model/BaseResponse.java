package koolpos.cn.goodproviderservice.model;

/**
 * Created by Administrator on 2017/6/5.
 */

public class BaseResponse<T>  {
    private String status;
    private String message;
    private String code;
    private T data;
}
