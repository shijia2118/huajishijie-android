package com.test.tworldapplication.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by dasiy on 16/10/27.
 */

public class FileUtils {
    private String SDPATH;
    //定义缓存区大小
    private int FILESIZE = 4 * 1024;

    public String getSDPAHT(){
        return SDPATH;
    }
    /**
     * 获取SD卡路径
     */
    public FileUtils(){
        SDPATH = Environment.getExternalStorageState()+"/";
    }
    /**
     * 在SD卡上创建文件
     */
    public File createFile(String dirName){
        File dir = new File(SDPATH + dirName);
        dir.mkdirs();
        return dir;
    }
    /**
     * 判断SD卡上的文件夹是否存在
     */
    public boolean isFileExist(String fileName){
        File file = new File(SDPATH + fileName);
        return file.exists();
    }
    /**
     * 将InputStream里面的数据写入到SD卡中
     */
    public File saveFile(String path, String fileName, InputStream inputStream){
        File file = null;
        //定义一个输出流，用来写数据
        OutputStream outputStream = null;
        //创建文件夹
        createFile(path);
        //保存文件
        file = createFile(path + fileName);
        try {
            //构造一个新的文件输出流写入文件
            outputStream = new FileOutputStream(file);
            //创建一个缓存区
            byte[] buffer = new byte[FILESIZE];
            //从输入流中读取的数据存入缓存区
            while ((inputStream.read(buffer)) != -1) {
                //将缓存区的数据写入输出流
                outputStream.write(buffer);
            }
            //刷新流，清空缓冲区数据
            outputStream.flush();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally{
            try {
                //关闭流
                outputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }
}
