package koolpos.cn.goodproviderservice.model;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */

public class PageDataResponse<T> {
    private int page;
    private int pageSize;
    private int totalCount;
    private List<T> data;
}
