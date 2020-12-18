package com.test.tworldapplication.utils;

import android.app.Activity;
import android.content.Intent;

import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.City;
import com.test.tworldapplication.entity.Province;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dasiy on 16/12/8.
 */

public class BaseUtils {
    public static List<String> provinceStrList = new ArrayList<>();
    public static List<String> cityStrList = new ArrayList<>();

    public static void pickPhoto(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        activity.startActivityForResult(intent, 0);


    }

    public static Area getArea(Activity activity) {
        Area area = null;
        provinceStrList.clear();
        cityStrList.clear();
        try {
            String s = XstreamHelper.inputStream2String(activity.getAssets().open("city_three.xml"));
            JSONObject obj = XML.toJSONObject(s);
            JSONObject areaObject = (JSONObject) obj.get("area");
            JSONArray provinceArray = (JSONArray) areaObject.get("province");
            List<Province> provinceList = new ArrayList<>();
            for (int i = 0; i < provinceArray.length(); i++) {
                JSONObject provinceObject = provinceArray.getJSONObject(i);
                provinceStrList.add(provinceObject.get("p_name").toString());
                List<City> cityList = new ArrayList<>();
                if (i == 1 || i == 2 || i == 6 || i == 8 || i == 12 || i == 22) {
                    JSONObject cityObject = (JSONObject) provinceObject.get("p_city");
                    City city = new City(cityObject.get("c_id").toString(), cityObject.get("c_name").toString());
                    cityList.add(city);
                    if (i == 2) {
                        cityStrList.add(cityObject.get("c_name").toString());
                    }
                } else {
                    JSONArray cityeArray = (JSONArray) provinceObject.get("p_city");
                    for (int j = 0; j < cityeArray.length(); j++) {
                        JSONObject cityObject = cityeArray.getJSONObject(j);

                        City city = new City(cityObject.get("c_id").toString(), cityObject.get("c_name").toString());
                        cityList.add(city);
                    }
                }
                Province province = new Province(provinceObject.get("p_id").toString(), provinceObject.get("p_name").toString(), cityList);
                provinceList.add(province);
                area = new Area(provinceList);
            }
//            for (int i = 0; i < area.getList().size(); i++) {
//                for (int j = 0; j < area.getList().get(i).getP_list().size(); j++) {
//                    Log.d("aaa", area.getList().get(i).getP_name() + ":" + area.getList().get(i).getP_list().get(j).getC_name());
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return area;
    }

    public static String getDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());
        return date;// new Date()为获取当前系统时间
    }
}
