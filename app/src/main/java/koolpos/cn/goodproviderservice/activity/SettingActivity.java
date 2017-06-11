package koolpos.cn.goodproviderservice.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.R;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Setting;

/**
 * Created by caroline on 2017/6/10.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.sn_view)
    TextView snView;
    @BindView(R.id.key_view)
    TextView keyView;
    @BindView(R.id.last_edit_view)
    TextView lastView ;
    @BindView(R.id.internal_ad_view)
    EditText adInternalView;
    private Setting setting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setting = (Setting) getIntent().getSerializableExtra(Setting.class.getName());
        setupActionBar();
        renderView(setting);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                if (setting!=null){
                    Observable.just(setting)
                            .map(new Function<Setting, Setting>() {
                                @Override
                                public Setting apply(@NonNull Setting setting) throws Exception {
                                    int adInternal = Integer.valueOf(adInternalView.getText().toString());
                                    setting.setIntervalAd(adInternal);
                                    return setting;
                                }
                            }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<Setting>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Setting setting) {
                                    setting.setLastUpdateTime(new Date());
                                    MyApplication.getDaoSession().getSettingDao().insertOrReplace(setting);
                                    Toast.makeText(MyApplication.getContext(),"保存成功",Toast.LENGTH_SHORT);
                                    renderView(setting);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                }
        }
        return true;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void renderView(Setting setting){
        if (setting!=null){
            snView.setText(setting.getDeviceSn());
            keyView.setText(setting.getDeviceKey());
            adInternalView.setText(setting.getIntervalAd()+"");
            if (setting.getLastUpdateTime()!=null){
                lastView.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA).format(setting.getLastUpdateTime()));
            }
        }
    }
}
