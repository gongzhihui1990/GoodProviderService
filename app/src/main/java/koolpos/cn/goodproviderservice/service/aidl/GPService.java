package koolpos.cn.goodproviderservice.service.aidl;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import org.json.JSONException;
import org.json.JSONObject;

import koolpos.cn.goodproviderservice.MySPEdit;
import koolpos.cn.goodproviderservice.api.AIDLResponse;
import koolpos.cn.goodproviderservice.api.LocalApi;
import koolpos.cn.goodproviderservice.api.LocalTestApi;
import koolpos.cn.goodproviderservice.service.BaseService;
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
            return response.toString();
        }
    };
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }
}
