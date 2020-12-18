package com.test.tworldapplication.utils;


import com.test.tworldapplication.base.MyApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {
    //路径为 /sd卡路径/你的App名称/log.txt ,如有需要，自行修改代码
    private static final String appName = "willSpace";
    private static File file;
    public static String BUG_PATH = MyApplication.context.getFilesDir() + File.separator;

    public static String getPath() {
        return BUG_PATH + MyApplication.mTodayYear + "-" + (MyApplication.mTodayMonth + 1) + "-" + MyApplication.mTodayDay + "-log.txt";
    }

    private static void checkFile() {
        String fileName = BUG_PATH + MyApplication.mTodayYear + "-" + (MyApplication.mTodayMonth + 1) + "-" + MyApplication.mTodayDay + "-log.txt";
        file = new File(fileName);

        if (!file.exists()) {
            File dir = new File(file.getParent());
            dir.mkdirs();
            try {
                file.createNewFile();
                Runtime.getRuntime().exec("chmod 755 " + file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将文本追加写入到文件
     */
    public static void setAppendFile(String value) {
//        checkFile();
//
//        try {
//            FileWriter fw = new FileWriter(file, true);
//            BufferedWriter bw = new BufferedWriter(fw);
//            PrintWriter printWriter = new PrintWriter(bw);
//            printWriter.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":\n");
//            printWriter.println(value);
//            printWriter.close();
//        } catch (Exception e) {
//        }
    }

    public static void setAppendFileTime(String value) {
        checkFile();

        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter printWriter = new PrintWriter(bw);
            printWriter.print(value);
//            printWriter.println(value);
            printWriter.close();
        } catch (Exception e) {
        }
    }

    /**
     * 将异常信息写入到文件
     */
    public static void setAppendFile(Throwable ex) {//Throwable ex
        try {
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter printWriter = new PrintWriter(bw);
            printWriter.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ":\n");
            ex.printStackTrace(printWriter);
            printWriter.close();
        } catch (Exception e) {
        }
    }

}
