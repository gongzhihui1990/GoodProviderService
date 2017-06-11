package koolpos.cn.goodproviderservice.api;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.Properties;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import koolpos.cn.goodproviderservice.MyApplication;
import koolpos.cn.goodproviderservice.constans.ImageEnum;
import koolpos.cn.goodproviderservice.model.response.BaseResponse;
import koolpos.cn.goodproviderservice.model.response.PageDataResponse;
import koolpos.cn.goodproviderservice.model.response.ProductCategoryBean;
import koolpos.cn.goodproviderservice.model.response.ProductRootItem;
import koolpos.cn.goodproviderservice.util.Loger;

/**
 * Created by caroline on 2017/6/11.
 */

public class SrcFileApi {
    public static void initSrcProperties() {
        Observable.just(Environment.getExternalStorageDirectory())
                .map(new Function<File, File>() {//初始化配置文件
                    @Override
                    public File apply(@NonNull File root) throws Exception {
                        File rootSrc = new File(root, "cloudSrc.properties");
                        if (!rootSrc.exists()) {
                            boolean create = rootSrc.createNewFile();
                            Loger.i("新建文件");
                        } else {
                            Loger.i("已有文件");
                            //TODO 需要删除
                            rootSrc.delete();
                            boolean create = rootSrc.createNewFile();

                        }
                        return rootSrc;
                    }
                })
                .map(new Function<File, File>() {//如果配置文件为空，则进行首次初始化
                    @Override
                    public File apply(@NonNull File propertiesFile) throws Exception {
                        InputStream inputStream = null;
                        FileOutputStream fileOutputStream = null;
                        if (propertiesFile.length() != 0) {
                            Loger.i("已经初始过");
                            return propertiesFile;
                        }
                        Loger.i("首次初始化");
                        try {
                            inputStream = MyApplication.getContext().getAssets().open("config.properties");
                            fileOutputStream = new FileOutputStream(propertiesFile);
                            Properties properties = new Properties();
                            properties.load(inputStream);
                            properties.store(fileOutputStream, new Date().toString());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } finally {
                            if (null != inputStream) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (null != fileOutputStream) {
                                try {
                                    fileOutputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return propertiesFile;
                    }
                })
                .map(new Function<File, Properties>() {
                    @Override
                    public Properties apply(@NonNull File propertiesFile) throws Exception {
                        Loger.i(propertiesFile.getAbsolutePath() + ",配置文件解析中");
                        Properties props = new Properties();
                        InputStream in = null;
                        try {
                            in = new FileInputStream(propertiesFile);
                            props.load(in);
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        } finally {
                            if (null != in) {
                                try {
                                    in.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        return props;
                    }
                }).filter(new Predicate<Properties>() {
            @Override
            public boolean test(@NonNull Properties properties) throws Exception {
                Loger.i("配置是否存在：" + (properties != null));
                return properties != null;
            }
        }).map(new Function<Properties, Properties>() {
            @Override
            public Properties apply(@NonNull Properties properties) throws Exception {
                File imageFile = getImageRootPathFile(properties);
                String title_bar_file = properties.getProperty("title_bar_file");
                String bg_main_file = properties.getProperty("bg_main_file");
                String bt_home_file = properties.getProperty("bt_home_file");
                String bt_search_file = properties.getProperty("bt_search_file");
                Loger.i("配置开始载入" + title_bar_file);
                File titleBarFile = new File(imageFile, title_bar_file);
                File bgMainFile = new File(imageFile, bg_main_file);
                File homeBtnFile = new File(imageFile, bt_home_file);
                File searchFile = new File(imageFile, bt_search_file);
                initFileSrc(titleBarFile, "title_bar.png");
                initFileSrc(bgMainFile, "main_bg.png");
                initFileSrc(homeBtnFile, "bt_home.png");
                initFileSrc(searchFile, "bt_search.png");
                return properties;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Properties>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Properties properties) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
//                        for (ImageEnum imageEnum:ImageEnum.values()){
//                            try {
//                                Loger.d(imageEnum.name()+"="+getSrcPath(imageEnum));
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
                    }
                });
    }

    private static File getRootPath(@NonNull Properties properties){
        File sdFile = Environment.getExternalStorageDirectory();
        String rootPath = properties.getProperty("rootPath");
        File rootFile = new File(sdFile, rootPath);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        return rootFile;
    }

    private static File getImageRootPathFile(@NonNull Properties properties) {
        File rootFile =getRootPath(properties);
        Loger.i("配置开始载入" + rootFile.getAbsolutePath());

        String imagePath = properties.getProperty("imagePath");
        File imageFile = new File(rootFile, imagePath);
        Loger.i("配置开始载入" + imageFile.getAbsolutePath());

        if (!imageFile.exists()) {
            imageFile.mkdirs();
        }
        return imageFile;
    }

    private static Properties getSDProperties() throws IOException {
        File root = Environment.getExternalStorageDirectory();
        File propertiesFile = new File(root, "cloudSrc.properties");
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(propertiesFile);
            props.load(in);
        } catch (IOException e1) {
            e1.printStackTrace();
            throw e1;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return props;
    }

    private static void initFileSrc(File targetFile, String fileName) throws IOException {
        Loger.i("配置开始载入:" + targetFile);
        if (!targetFile.exists() || targetFile.length() == 0) {
            Loger.i(targetFile + " 初始化");
            targetFile.createNewFile();
            copyFile(targetFile, fileName);
        } else {
            Loger.i(targetFile + " 已经存在");

        }
        Loger.i(targetFile + "初始化成功");

    }

    private static void copyFile(File targetFile, String fileName) throws IOException {
        InputStream inputStream = MyApplication.getContext().getAssets().open(fileName);
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        byte[] buffer = new byte[512];
        int count = 0;
        while ((count = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, count);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        inputStream.close();
        Loger.i("copyFile " + fileName + " to " + targetFile.getAbsolutePath() + " success");
    }

    public static String getImageSrcPath(ImageEnum imageEnum) throws IOException {
        String path = "";
        File imageFile = null;
        Properties properties = getSDProperties();
        File imageRootPathFile = getImageRootPathFile(properties);
        String title_bar_file = properties.getProperty("title_bar_file");
        String bg_main_file = properties.getProperty("bg_main_file");
        String bt_home_file = properties.getProperty("bt_home_file");
        String bt_search_file = properties.getProperty("bt_search_file");
        switch (imageEnum) {
            case MAIN_BG:
                imageFile = new File(imageRootPathFile, bg_main_file);
                break;
            case TITLE_BAR:
                imageFile = new File(imageRootPathFile, title_bar_file);
                break;
            case HOME_BTN:
                imageFile = new File(imageRootPathFile, bt_home_file);
                break;
            case SEARCH_BTN:
                imageFile = new File(imageRootPathFile, bt_search_file);
                break;
        }
        path = imageFile.getAbsolutePath();
        return path;
    }
    private final static String productFileName="productJson.txt";
    private final static String productCategoryFileName="categoryJson.txt";

    public static void save(BaseResponse<PageDataResponse<ProductRootItem>> allProducts, BaseResponse<PageDataResponse<ProductCategoryBean>> allCategory) {

        File productJsonFile =new File(getRootPath(),productCategoryFileName)


    }

    private static void save(String data,File targetFile) throws IOException {
        FileWriter fileWriter=new FileWriter(targetFile);
        fileWriter.write(data);
        fileWriter.flush();
        fileWriter.close();
    }

}