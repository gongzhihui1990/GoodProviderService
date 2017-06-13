package koolpos.cn.goodproviderservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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
    TextView lastView;
    @BindView(R.id.internal_ad_view)
    EditText adInternalView;
    @BindView(R.id.play_time_ad_view)
    EditText adPlayLongView;
    @BindView(R.id.use_cache_sw)
    Switch useCacheView;
    Setting setting;

    @BindView(R.id.set_bg_res_view)
    View set_bg_res_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setting = MyApplication.getSetting();
        setUpActionBar();
        renderView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if (setting != null) {
                    Observable.just(setting)
                            .map(new Function<Setting, Setting>() {
                                @Override
                                public Setting apply(@NonNull Setting setting) throws Exception {
                                    int adInternal = Integer.valueOf(adInternalView.getText().toString());
                                    setting.setIntervalAd(adInternal);
                                    int adPlayLong = Integer.valueOf(adPlayLongView.getText().toString());
                                    setting.setPlayLongAd(adPlayLong);
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
                                    Toast.makeText(MyApplication.getContext(), "保存成功", Toast.LENGTH_SHORT);
                                    renderView();
                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onComplete() {

                                }
                            });

                }
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void renderView() {
        if (setting == null) {
            return;
        }
        snView.setText(setting.getDeviceSn());
        keyView.setText(setting.getDeviceKey());
        adInternalView.setText(setting.getIntervalAd() + "");
        adPlayLongView.setText(setting.getPlayLongAd() + "");
        if (setting.getLastUpdateTime() != null) {
            lastView.setText(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA).format(setting.getLastUpdateTime()));
        }
        useCacheView.setChecked(setting.getLoadCacheFirst());
        useCacheView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setting.setLoadCacheFirst(isChecked);
            }
        });
        set_bg_res_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SrcDrawableSettingActivity.class));
            }
        });
    }
}
