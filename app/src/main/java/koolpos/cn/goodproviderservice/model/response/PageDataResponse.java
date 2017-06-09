package koolpos.cn.goodproviderservice.model.response;

import java.util.List;

import koolpos.cn.goodproviderservice.model.BaseBean;

/**
 * Created by Administrator on 2017/6/5.
 */

public class PageDataResponse<T> extends BaseBean {
    private static final long serialVersionUID = 7573832955122324654L;
    private int page;
    private int pageSize;
    private int totalCount;
    private List<T> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
