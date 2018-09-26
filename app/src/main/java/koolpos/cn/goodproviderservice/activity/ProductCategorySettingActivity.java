package koolpos.cn.goodproviderservice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.R;
import koolpos.cn.goodproviderservice.api.SrcFileApi;
import koolpos.cn.goodproviderservice.model.response.BaseResponseV1;
import koolpos.cn.goodproviderservice.model.response.PageDataResponse;
import koolpos.cn.goodproviderservice.model.response.ProductCategoryBean;
import koolpos.cn.goodproviderservice.util.FileUtil;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by Administrator on 2017/6/12.
 */

public class ProductCategorySettingActivity extends BaseActivity {
    @BindView(R.id.src_list_view)
    RecyclerView list;
    public SrcPropertyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_src_drawable_setting);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(mLayoutManager);
        Observable<BaseResponseV1<PageDataResponse<ProductCategoryBean>>> observable = getCategoryByFile();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponseV1<PageDataResponse<ProductCategoryBean>>>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponseV1<PageDataResponse<ProductCategoryBean>> pageDataResponseBaseResponse) {
                        adapter = new SrcPropertyAdapter(getBaseContext(), pageDataResponseBaseResponse);
                        list.setAdapter(adapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private Observable<BaseResponseV1<PageDataResponse<ProductCategoryBean>>> getCategoryByFile() {
        return Observable.just(SrcFileApi.productCategoryFileName)
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String fileName) throws Exception {
                        return SrcFileApi.getJsonFileToString(fileName);
                    }
                }).map(new Function<String, BaseResponseV1<PageDataResponse<ProductCategoryBean>>>() {
                    @Override
                    public BaseResponseV1<PageDataResponse<ProductCategoryBean>> apply(@NonNull String jsonStr) throws Exception {
                        java.lang.reflect.Type token = new TypeToken<BaseResponseV1<PageDataResponse<ProductCategoryBean>>>() {
                        }.getType();
                        return new Gson().fromJson(jsonStr, token);
                    }
                });
    }

    public class SrcPropertyAdapter extends RecyclerView.Adapter<SrcPropertyAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private List<ProductCategoryBean> mData = new ArrayList<>();
        private BaseResponseV1<PageDataResponse<ProductCategoryBean>> mResponse;

        public SrcPropertyAdapter(Context context, BaseResponseV1<PageDataResponse<ProductCategoryBean>> response) {
            mInflater = LayoutInflater.from(context);
            List<ProductCategoryBean> mDataTmp = response.getData().getData();
            for (ProductCategoryBean data:mDataTmp){
                if (data.getParentCategory()==null||data.getParentCategory().equals("")){
                    mData.add(data);
                }
            }
            mResponse = response;
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            final ProductCategoryBean categoryBean = mData.get(position);
            viewHolder.srcTypeText.setText(categoryBean.getName());
            Loger.d("load:"+categoryBean.toString());
            Glide.with(getBaseContext())
                    .load(categoryBean.getImageUrl())
                    .skipMemoryCache(true)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.srcDrawableImg);
            viewHolder.srcSelectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gatFile(categoryBean);
                }
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
            View view = mInflater.inflate(R.layout.src_drawable_item_layout, arg0, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        public void update(ProductCategoryBean reqProductCategory) {
            Loger.i("update ");
            Observable.just(reqProductCategory)
                    .map(new Function<ProductCategoryBean, Boolean>() {
                        @Override
                        public Boolean apply(@NonNull ProductCategoryBean reqProductCategory) throws Exception {
                            ProductCategoryBean beanUpdate = null;
                            for (ProductCategoryBean bean : mData) {
                                if (bean.getId() == reqProductCategory.getId()) {
                                    beanUpdate = bean;
                                    break;
                                }
                            }
                            if (beanUpdate != null) {
                                mData.remove(beanUpdate);
                                Loger.i("update "+reqProductCategory.toString());
                                mData.add(reqProductCategory);
                                mResponse.getData().setData(mData);
                                Loger.i("update success");
                                Loger.i("update "+mResponse.toString());
                                SrcFileApi.save(null,mResponse,null);
                            }
                            return true;
                        }
                    }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Boolean aBoolean) {

                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onComplete() {
                            notifyDataSetChanged();
                            MyApplication.appInit();
                        }
                    });
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.src_type)
            TextView srcTypeText;
            @BindView(R.id.src_drawable)
            ImageView srcDrawableImg;
            @BindView(R.id.src_select)
            Button srcSelectBtn;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }

        }

    }

    private int REQUEST_FILE = 10;
    private ProductCategoryBean reqProductCategory;

    /*
  * 从相册获取
 */
    public void gatFile(ProductCategoryBean productCategory) {
        reqProductCategory = productCategory;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILE && resultCode == Activity.RESULT_OK) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri selectedImage = data.getData();
                Loger.d("filePath:" + selectedImage);
                String filePath =FileUtil.getPath(getBaseContext(),selectedImage);
                Loger.d("filePath:" + filePath);
                File file = new File(filePath);
                Toast.makeText(getBaseContext(), file.toString(), Toast.LENGTH_SHORT).show();
                try {
                    File srcFile = SrcFileApi.importFileSrc(filePath);
                    reqProductCategory.setImageUrl(srcFile.getAbsolutePath());
                    adapter.update(reqProductCategory);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}