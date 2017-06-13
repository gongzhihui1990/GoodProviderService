package koolpos.cn.goodproviderservice.service.aidl;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import koolpos.cn.goodproviderservice.MySPEdit;
import koolpos.cn.goodproviderservice.api.AIDLResponse;
import koolpos.cn.goodproviderservice.api.LocalApi;
import koolpos.cn.goodproviderservice.api.LocalTestApi;
import koolpos.cn.goodproviderservice.api.SrcFileApi;
import koolpos.cn.goodproviderservice.service.BaseService;
import koolpos.cn.goodproviderservice.util.FileUtil;
import koolpos.cn.goodproviderservice.util.Loger;

public class GPService extends BaseService {
    public GPService() {
    }
    IGPService.Stub stub =new IGPService.Stub(){

        @Override
        public String proxyPost(String request) throws RemoteException {
            Loger.d("request:"+request);
            AIDLResponse response=new AIDLResponse();
            //TODO
            try {
                JSONObject reqJson=new JSONObject(request);
                String action = reqJson.optString("action");
                if (action.startsWith("local/")){
                    response = LocalApi.proxyPost(reqJson);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Loger.d("response:"+response.toString());
            File responseFile = FileUtil.getAidlMessageFile("request_"+request.hashCode());
            try {
                SrcFileApi.save(response.toString(),responseFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Loger.d("response in:"+responseFile.getPath());
            return responseFile.getPath();
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
}
