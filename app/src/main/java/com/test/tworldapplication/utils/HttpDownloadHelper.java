package com.test.tworldapplication.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dasiy on 16/10/27.
 */

public class HttpDownloadHelper {
    private URL url = null;

    /**
     * 根据出入的参数URL保存输出流
     */
    public String downLoad(String urlString) {
        //实例化一个StringBuffer类，用来存储字符串
        StringBuffer stringBuffer = new StringBuffer();
        String line = null;
        //实例化一个BufferedReader类，用来读取输入流
        BufferedReader bufferedReader = null;
        try {
            //实例化一个URL解析
            url = new URL(urlString);
            //实例化一个HttpURLConnection，并打开连接
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //将打开连接中的输入流，缓存到bufferdReader中
            bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            //循环读取数据，保存到line中，直到全部读取
            while ((line = bufferedReader.readLine()) != null) {
                //将line中的内容添加到stringBuffer中
                stringBuffer.append(line);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                //关闭缓冲字节输入流
                bufferedReader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return stringBuffer.toString();
    }

    /**
     * -1：下载文件出错
     * 0：文件下载成功
     * 1：文件已经存在
     */
    public int downFile(String urlString, String path, String fileName) {
        //定义一个InputStream，用来读取输入流
        InputStream inputStream = null;
        //自定义的文件操作类
        FileUtils fileUtils = new FileUtils();
        if (fileUtils.isFileExist(path + fileName)) {
            return 1;
        } else {
            //从资源中读取数据
            inputStream = getInputStream(urlString);
            //创建文件
            File resultFile = fileUtils.saveFile(path, fileName, inputStream);
            if (resultFile == null) {
                return -1;
            }
        }
        try {
            //关闭输入流
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据URL得到输入流
     */
    public InputStream getInputStream(String urlString) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputStream;
    }
}
