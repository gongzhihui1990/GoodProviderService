package koolpos.cn.goodproviderservice.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.MySPEdit;
import koolpos.cn.goodproviderservice.R;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Setting;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.SettingDao;
import koolpos.cn.goodproviderservice.service.aidl.IGPService;
import koolpos.cn.goodproviderservice.util.AndroidUtils;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.ACCESS_WIFI_STATE;
import static android.Manifest.permission.READ_PHONE_STATE;
/**
 * A login screen that offers login via email/password.
 */
public class MainActivity extends AppCompatActivity {

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
    private AutoCompleteTextView  mDeviceSnView;
    private EditText mKeyView;
    private View mProgressView;
    private View mLoginFormView;

    private void renderSetting(){
        Observable.just(MyApplication.getDaoSession().getSettingDao())
                .map(new Function<SettingDao, Setting>() {
                    @Override
                    public Setting apply(@io.reactivex.annotations.NonNull SettingDao settingDao) throws Exception {
                        Setting setting =settingDao.queryBuilder().where(SettingDao.Properties.DeviceSn.eq(Build.SERIAL)).unique();
                        if (setting==null){
                            setting =new Setting();
                            setting.setDeviceSn(Build.SERIAL);
                        }
                        return setting;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Setting>() {
                    @Override
                    public void accept(@io.reactivex.annotations.NonNull Setting setting) throws Exception {
                        renderSetting(setting);
                    }
                    private void renderSetting(Setting setting) {

                    }
                });
    }
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IGPService gpService = IGPService.Stub.asInterface(iBinder);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent serviceIntent = new Intent(IGPService.class.getName());
        serviceIntent = AndroidUtils.getExplicitIntent(getBaseContext(), serviceIntent);
        boolean bindService = bindService(serviceIntent, connection, Context.BIND_AUTO_CREATE);
        // Set up the login form.
        mDeviceSnView.setText(Build.SERIAL);
        mKeyView = (AutoCompleteTextView) findViewById(R.id.device_key);
        mKeyView.setText(MySPEdit.getInstance().getKey());
        populateAutoComplete();

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connection!=null){
            unbindService(connection);
        }
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
        final String[] permissions={INTERNET,WRITE_EXTERNAL_STORAGE,ACCESS_NETWORK_STATE,ACCESS_WIFI_STATE,READ_PHONE_STATE};
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


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mDeviceSnView.setError(null);

        // Store values at the time of the login attempt.
        String deviceKey = mKeyView.getText().toString();
        String deviceId = mDeviceSnView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid email address.
        if (TextUtils.isEmpty(deviceId)) {
            mDeviceSnView.setError(getString(R.string.error_field_required));
            focusView = mDeviceSnView;
            cancel = true;
        } else if (TextUtils.isEmpty(deviceId)) {
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
        }
    }

    private boolean isEmailValid(String userId) {
        //TODO: Replace this with your own logic
        return userId.length() > 2;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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

