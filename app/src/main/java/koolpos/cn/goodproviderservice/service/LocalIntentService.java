package koolpos.cn.goodproviderservice.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.api.ServerApi;
import koolpos.cn.goodproviderservice.api.SrcFileApi;
import koolpos.cn.goodproviderservice.constans.Action;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Setting;

/**
 * Created by Administrator on 2017/6/5.
 */

public class LocalIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocalIntentService(String name) {
        super(name);
    }

    public LocalIntentService() {
        super("LocalService");
    }
    private Setting setting;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = "";
        if (intent != null) {
            action = intent.getAction();
        }
        setting = MyApplication.getSetting();
        switch (action) {
            case Action.InitData:
                SrcFileApi.initSrcProperties();
                new ServerApi(setting).initServerData();
                break;
        }
    }


}
