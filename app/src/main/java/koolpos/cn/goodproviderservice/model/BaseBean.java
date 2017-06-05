package koolpos.cn.goodproviderservice.model;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/5.
 */

public class BaseBean implements Serializable {
    private static final long serialVersionUID = 8942931220417122735L;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
