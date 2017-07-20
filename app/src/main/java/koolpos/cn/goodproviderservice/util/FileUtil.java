package koolpos.cn.goodproviderservice.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2017/5/15.
 */

public class FileUtil {
    /**
     * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
     *
     * @return
     */
    private static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
    /**
     * 获取SD卡根目录路径
     *
     * @return
     */
    private static String getSdCardPath() throws UnsupportedOperationException {
        boolean exist = isSdCardExist();
        String sdpath = "";
        if (exist) {
            sdpath = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();
        } else {
            throw new UnsupportedOperationException("no sd card found!");
        }
        return sdpath;
    }

    // 生成文件夹
    private static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            Loger.e(e.getMessage());
        }
    }
    public static File getAidlMessageFile(String messageCode){
       final  String path =getSdCardPath()+"/goodPro/aidl";
        File root=new File(path);
        makeRootDirectory(path);
        File aidlMessageFile=new File(root,messageCode+".txt");
        if (!aidlMessageFile.exists()){
            try {
                aidlMessageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  aidlMessageFile;
    }
    private static File getImageCashFile(){
       final  String path =getSdCardPath()+"/goodPro/images";
        File root=new File(path);
        makeRootDirectory(path);
        return  root;
    }

    public static File getImageCashFile(String imageUrl) throws NoSuchAlgorithmException {
        //常用算法：MD5、SHA、CRC
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] result = digest.digest(imageUrl.getBytes());
        //消息摘要的结果一般都是转换成16 进制字符串形式展示
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < result.length; i++) {
            String hex = Integer.toHexString(result[i]&0xff);
            if (hex.length() == 1){
                sb.append("0");
            }
            sb.append(hex);
        }
        String hex = sb.toString();

       final  String filePath =getImageCashFile().getPath()+"/"+hex;
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

        public static String getPath(Context context, Uri uri) {
            if ("content".equalsIgnoreCase(uri.getScheme())) {
                String[] projection = { "_data" };
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, projection,null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        return cursor.getString(column_index);
                    }
                } catch (Exception e) {
                    // Eat it
                    e.printStackTrace();
                }
            }
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
            return null;
        }
}
