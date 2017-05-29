package koolpos.cn.goodproviderservice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

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


	public void setKey(String device_key) {
		editor.putString("device_key", device_key).commit();

	}

	public String getKey() {
		return sPreferences.getString("device_key", "");
	}
}
