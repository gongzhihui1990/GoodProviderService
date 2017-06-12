package koolpos.cn.goodproviderservice.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;

import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.R;
import koolpos.cn.goodproviderservice.constans.Constant;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Setting;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.SettingDao;
import koolpos.cn.goodproviderservice.util.Loger;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.READ_PHONE_STATE;

/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends BaseActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_PERMISSIONS = 1;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */

    // UI references.
    @BindView(R.id.device_sn_view)
    AutoCompleteTextView mDeviceSnView;
    @BindView(R.id.device_key)
    EditText mKeyView;
    @BindView(R.id.login_progress)
    View mProgressView;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.key_set_button)
    Button mSetKeyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateAutoComplete();
        boolean resettingFlag = false;
        if (getIntent() != null) {
            resettingFlag = getIntent().getBooleanExtra("reset", false);
        }
        initUI(resettingFlag);
    }

    private class SettingContainer {
        private Setting setting;
        private String deviceId;
    }

    private void initUI(final boolean resetting) {
//        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        Observable.just(Build.SERIAL)
                .map(new Function<String, SettingContainer>() {
                    @Override
                    public SettingContainer apply(@io.reactivex.annotations.NonNull String sn) throws Exception {
                        Setting deviceSetting = MyApplication.getDaoSession()
                                .getSettingDao().queryBuilder()
                                .where(SettingDao.Properties.DeviceSn.eq(sn)).unique();
                        SettingContainer settingContain = new SettingContainer();
                        settingContain.setting = deviceSetting;
                        settingContain.deviceId = sn;
                        return settingContain;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SettingContainer>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull SettingContainer settingContainer) throws Exception {

                        if (settingContainer.setting == null || resetting) {
                            Loger.d("setting is null," + settingContainer.deviceId);
                            mDeviceSnView.setText(settingContainer.deviceId);
                            mDeviceSnView.setEnabled(false);
                            mKeyView.setText(Constant.MYTestKey);
                            mSetKeyButton.setOnClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    attemptSetKey();
                                }
                            });
                        } else {
                            Loger.d("setting is set");
                            mDeviceSnView.setText(settingContainer.setting.getDeviceSn());
                            mDeviceSnView.setEnabled(false);
                            mKeyView.setText(settingContainer.setting.getDeviceKey());
                            mKeyView.setEnabled(false);
                            mSetKeyButton.setEnabled(false);

                            //展示Setting
                            Intent settingIntent = new Intent(getBaseContext(), SettingActivity.class);
                            settingIntent.putExtra(Setting.class.getName(), settingContainer.setting);
                            startActivity(settingIntent);
                            finish();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void populateAutoComplete() {
        if (!mayRequestInternet()) {
            return;
        }
    }

    private boolean mayRequestInternet() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        final String[] permissions = {INTERNET, WRITE_EXTERNAL_STORAGE, ACCESS_NETWORK_STATE, ACCESS_WIFI_STATE, READ_PHONE_STATE};
        if (shouldShowRequestPermissionRationale(INTERNET)) {
            Snackbar.make(mDeviceSnView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(permissions, REQUEST_PERMISSIONS);
                        }
                    });
        } else {
            requestPermissions(permissions, REQUEST_PERMISSIONS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    private void attemptSetKey() {
        // Reset errors.
        mDeviceSnView.setError(null);

        // Store values at the time of the login attempt.
        String deviceKey = mKeyView.getText().toString();
        String deviceSn = mDeviceSnView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid deviceKey.
        if (TextUtils.isEmpty(deviceKey)) {
            mKeyView.setError(getString(R.string.error_field_required));
            focusView = mKeyView;
            cancel = true;
        } else if (TextUtils.isEmpty(deviceSn)) {
            mDeviceSnView.setError(getString(R.string.error_field_required));
            focusView = mDeviceSnView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //TODO
            Setting deviceSetting = MyApplication.getDaoSession()
                    .getSettingDao().queryBuilder()
                    .where(SettingDao.Properties.DeviceSn.eq(deviceKey)).unique();
            if (deviceSetting == null) {
                deviceSetting = new Setting();
                deviceSetting.setLastUpdateTime(new Date());
                deviceSetting.setIntervalAd(Constant.Def_AD_INTERNAL);
                deviceSetting.setPlayLongAd(Constant.Def_AD_PLAY_LONG);
                deviceSetting.setDeviceSn(deviceSn);
                deviceSetting.setDeviceKey(deviceKey);
            }
            MyApplication.getDaoSession().getSettingDao().insertOrReplace(deviceSetting);
            initUI(false);
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

