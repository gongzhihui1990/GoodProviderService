package koolpos.cn.goodproviderservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import koolpos.cn.goodproviderservice.util.Loger;

@SuppressLint("CommitPrefEdits")
public class MySPEdit {
	private static SharedPreferences sPreferences;
	private SharedPreferences.Editor editor;
	private static MySPEdit _instancePublic = null;

	@SuppressWarnings("deprecation")
	@SuppressLint("WorldWriteableFiles")
	private MySPEdit(Context context) {
		sPreferences = context.getSharedPreferences("MySharedPreferencesEdit",
				Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
		editor = sPreferences.edit();
	}

	/**
	 * @return
	 */
	public static MySPEdit getInstance() {
		if (_instancePublic == null) {
			_instancePublic = new MySPEdit(MyApplication.getContext());
		}
		return _instancePublic;
	}

	public void setMacSN(String mac) {
		if (TextUtils.isEmpty(mac)) {
			mac = Build.SERIAL;
		}
		editor.putString("mac", mac).commit();
	}
	public String getMacSN() {
		Loger.d("getMacSN"+ sPreferences.getString("mac",Build.SERIAL));
		return sPreferences.getString("mac", Build.SERIAL);
	}




}
