package koolpos.cn.goodproviderservice.util;

import android.widget.Toast;

import koolpos.cn.goodproviderservice.MyApplication;


/**
 * Created by caroline on 2017/6/19.
 */

public class SimpleToast {
    public static void toast(String text){
        Toast.makeText(MyApplication.getContext(),text,Toast.LENGTH_LONG).show();
    }
}
