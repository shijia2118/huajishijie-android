package com.test.tworldapplication.utils;

import android.os.Environment;
import android.util.Log;


import com.test.tworldapplication.base.MyApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class FileLogUtils {
    public static String BUG_PATH = MyApplication.context.getFilesDir() + File.separator;

    public static File findIfExists(String path) {
        File file = new File(path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    /**
     * 获取网络文件大小
     */

    public static long getFileLength(String downloadUrl) {


        if (downloadUrl == null || "".equals(downloadUrl)) {

            return 0L;

        }


        URL url = null;
        try {
            url = new URL(downloadUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpURLConnection conn = null;

        try {

            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("HEAD");

            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows 7; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/47.0.2526.73 Safari/537.36 YNoteCef/5.8.0.1 (Windows)");

            return (long) conn.getContentLength();

        } catch (IOException e) {

            return 0L;

        } finally {

            conn.disconnect();

        }

    }

    public static void checkFilePath(String path, boolean isDir) {
        File file = new File(path);
        if (file != null) {
            if (!isDir) {
                file = file.getParentFile();
            }

            if (file != null && !file.exists()) {
                file.mkdirs();
            }
//            else if (file.exists())
//                file.delete();

        }
    }

    public static long getFileSize(File file) {
        long size = 0;
        try {
            if (file.exists()) {
                FileInputStream fis = null;
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                size = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return size;
    }

    public static boolean checkFileExit(String filePath) {
        File file = new File(filePath);
        if (file.exists())
            return true;
        else
            return false;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists())
            file.delete();

    }


    public static void writeFile(InputStream inputString, File file) {
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {
//            listener.onFail("FileNotFoundException");
        } catch (IOException e) {
//            listener.onFail("IOException");
        }

    }

    public static String getApkPath() {
        String directoryPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
            directoryPath = MyApplication.context.getExternalFilesDir("apk").getAbsolutePath();
        } else {//没外部存储就使用内部存储
            directoryPath = MyApplication.context.getFilesDir() + File.separator + "apk";

        }

        File file = new File(directoryPath);
        Log.e("测试路径", directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }

    public static String getSharePath() {
        String directoryPath = MyApplication.context.getFilesDir().getParent() + "/shared_prefs/share_data.xml";
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
//            directoryPath = MyApplication.applicationContext.getExternalFilesDir("apk").getAbsolutePath();
//        } else {//没外部存储就使用内部存储
//            directoryPath = MyApplication.applicationContext.getFilesDir() + File.separator + "apk";
//
//        }

//        File file = new File(directoryPath);
        Log.e("测试路径", directoryPath);
//        if (!file.exists()) {//判断文件目录是否存在
//            file.mkdirs();
//        }
        return directoryPath;
    }


    public static String getVideoPath() {
        String directoryPath = MyApplication.context.getFilesDir() + File.separator + "video";
//        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
//            directoryPath = MyApplication.applicationContext.getExternalFilesDir("video").getAbsolutePath();
//        } else {//没外部存储就使用内部存储
//            directoryPath = MyApplication.applicationContext.getFilesDir() + File.separator + "video";
//        }
        File file = new File(directoryPath);
        Log.e("测试路径", directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }

    public static String getImagePath() {
        String directoryPath = "";
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {//判断外部存储是否可用
            directoryPath = MyApplication.context.getExternalFilesDir("image").getAbsolutePath();
        } else {//没外部存储就使用内部存储
            directoryPath = MyApplication.context.getFilesDir() + File.separator + "image";
        }
        File file = new File(directoryPath);
        Log.e("测试路径", directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }


    public static String getMD5Str(String str) {
        byte[] digest = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            digest = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //16是表示转换为16进制数
        String md5Str = new BigInteger(1, digest).toString(16);
        return md5Str;
    }

}
