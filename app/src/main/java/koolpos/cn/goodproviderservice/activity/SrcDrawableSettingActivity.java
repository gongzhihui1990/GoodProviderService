package koolpos.cn.goodproviderservice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import koolpos.cn.goodproviderservice.R;
import koolpos.cn.goodproviderservice.api.SrcFileApi;
import koolpos.cn.goodproviderservice.constans.Action;
import koolpos.cn.goodproviderservice.constans.ImageEnum;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by Administrator on 2017/6/12.
 */

public class SrcDrawableSettingActivity extends BaseActivity {
    @BindView(R.id.src_list_view)
    RecyclerView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_src_drawable_setting);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(mLayoutManager);
        list.setAdapter(new SrcPropertyAdapter(getBaseContext()));
    }


    public class SrcPropertyAdapter extends RecyclerView.Adapter<SrcPropertyAdapter.ViewHolder> {

        private LayoutInflater mInflater;
        private List<ImageEnum> mData = new ArrayList<>();

        public SrcPropertyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
            mData = Arrays.asList(ImageEnum.values());
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, int position) {
            final ImageEnum imageEnum = mData.get(position);
            viewHolder.srcTypeText.setText(imageEnum.name());
            try {
                Glide.with(getBaseContext())
                        .load(SrcFileApi.getImageSrcPath(imageEnum))
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .into(viewHolder.srcDrawableImg);
            } catch (IOException e) {
                e.printStackTrace();
            }
            viewHolder.srcSelectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gatFile(imageEnum);
                }
            });
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
            View view = mInflater.inflate(R.layout.src_drawable_item_layout, arg0, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
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
    private int REQUEST_FILE =10;
    private ImageEnum reqImageEnum;
    /*
  * 从相册获取
 */
    public void gatFile(ImageEnum imageEnum) {
        reqImageEnum=imageEnum;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//设置类型，我这里是任意类型，任意后缀的可以这样写。
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_FILE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_FILE&&resultCode== Activity.RESULT_OK) {
            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                if (cursor==null){
                    return;
                }
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                File file = new File(filePath);
                Toast.makeText(getBaseContext(), file.toString(), Toast.LENGTH_SHORT).show();
                Loger.d("filePath:"+filePath);
                try {
                    SrcFileApi.resetFileSrc(reqImageEnum,filePath);
                    list.getAdapter().notifyDataSetChanged();
                    sendBroadcast(new Intent(Action.State_Update));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}