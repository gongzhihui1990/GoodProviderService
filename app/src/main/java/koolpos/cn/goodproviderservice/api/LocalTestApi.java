package koolpos.cn.goodproviderservice.api;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import koolpos.cn.goodproviderservice.util.Data;

import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.mvcDao.greenDao.Goods;
import koolpos.cn.goodproviderservice.util.FileUtil;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by Administrator on 2017/5/14.
 */

public class LocalTestApi {
    public static void addTestGoodsData() {
        MyApplication.State=MyApplication.State_Loading;
        MyApplication.getDaoSession().getGoodsDao().deleteAll();
        List<Goods> goods = new ArrayList<Goods>();
        goods.addAll(createTest("薯片", "食品"));
        goods.addAll(createTest("面包", "食品"));
        goods.addAll(createTest("香肠", "食品"));
        goods.addAll(createTest("牛奶", "食品"));
        goods.addAll(createTest("奇多", "食品"));
        goods.addAll(createTest("泡面", "食品"));
        goods.addAll(createTest("茶叶", "食品"));
        goods.addAll(createTest("可乐", "食品"));
        goods.addAll(createTest("雪碧", "食品"));
        goods.addAll(createTest("话梅", "食品"));
        goods.addAll(createTest("巧克力", "食品"));
        goods.addAll(createTest("糖果", "食品"));
        goods.addAll(createTest("果冻", "食品"));

        goods.addAll(createTest("电视", "家电"));
        goods.addAll(createTest("浴霸", "家电"));
        goods.addAll(createTest("空调", "家电"));
        goods.addAll(createTest("热水器", "家电"));
        goods.addAll(createTest("电饭锅", "家电"));
        goods.addAll(createTest("电扇", "家电"));
        goods.addAll(createTest("灯具", "家电"));
        goods.addAll(createTest("电锯", "家电"));

        goods.addAll(createTest("肥皂", "日用"));
        goods.addAll(createTest("拖把", "日用"));
        goods.addAll(createTest("牙刷", "日用"));
        goods.addAll(createTest("牙膏", "日用"));
        goods.addAll(createTest("洗发水", "日用"));
        goods.addAll(createTest("垃圾桶", "日用"));
        goods.addAll(createTest("毛巾", "日用"));
        goods.addAll(createTest("地毯", "日用"));
        goods.addAll(createTest("热水瓶", "日用"));
        goods.addAll(createTest("化妆棉", "日用"));

        goods.addAll(createTest("Iphone", "3c数码"));
        goods.addAll(createTest("小米", "3c数码"));
        goods.addAll(createTest("鼠标", "3c数码"));
        goods.addAll(createTest("Mac", "3c数码"));
        goods.addAll(createTest("电脑", "3c数码"));

        goods.addAll(createTest("头疼药", "药品"));
        goods.addAll(createTest("胃药", "药品"));
        goods.addAll(createTest("感冒药", "药品"));

        goods.addAll(createTest("TT", "其他"));

        MyApplication.getDaoSession().getGoodsDao().insertOrReplaceInTx(goods);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MyApplication.State=MyApplication.State_Load_Image;
               List<Goods> goodsList= MyApplication.getDaoSession().getGoodsDao().queryBuilder().list();
                for (Goods itemGood: goodsList){
                    File file = null;
                    try {
                        file = FileUtil.getImageCashFile(itemGood.getImage_url());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = null;
                    try {
                        bitmap = Glide.with(MyApplication.getContext())
                                .load(itemGood.getImage_url())
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get();
                        if (bitmap != null){
                            // 在这里执行图片保存方法
                            saveImageToGallery(file,bitmap);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                MyApplication.State=MyApplication.State_OK;
            }
        }).start();
    }
    private static void saveImageToGallery(File savedFile, Bitmap bmp) {
        // 首先保存图片
        if (savedFile.exists()&&savedFile.length()!=0){
            //已经下过
            Loger.d("file save 已经下过 - "+savedFile.getName());
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(savedFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            Loger.d("file save ok - "+savedFile.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        private static ArrayList<Goods> createTest(String name, String type) {
        ArrayList<Goods> goods = new ArrayList<>();
        int size = (int) ((10 * Math.random())) +1;
        for (int i = 0; i < size; i++) {
            int x = (int) ((10000 * Math.random())) % Data.URLS.length;
            String url= Data.URLS[x];
            goods.add(Goods.createFromTest(name, type,url));
        }
        return goods;
    }
}
