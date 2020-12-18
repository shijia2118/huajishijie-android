package com.test.tworldapplication.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.other.PhotoDetailActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Commission;
import com.test.tworldapplication.entity.MessageEvent;
import com.test.tworldapplication.entity.Statistics;
import com.test.tworldapplication.inter.OnSelectListener;
import com.test.tworldapplication.view.WheelView;

import org.greenrobot.eventbus.EventBus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import wintone.passport.sdk.utils.AppManager;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dasiy on 16/10/20.
 */

public class Util {
    public static String strBegin, strEnd, strOrderState, strPhoneNumber, strCzState;
    public static String strRegular, strNumPool;
    private static long lastClickTime, lastShowTime;
    private static Subscription checkObservable, startObservable;

    public static Observable<Integer> countdown(int time) {
        if (time < 0) time = 0;

        final int countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long increaseTime) {
                        return countTime - increaseTime.intValue();
                    }
                })
                .take(countTime + 1);

    }

    public static void checkCount() {
        stopSub();
        checkObservable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        Log.d("fff", aLong + "");
                        EventBus.getDefault().post(new MessageEvent(MessageEvent.CHECK_PERMISSION, aLong + ""));
                    }
                });

    }

    public static void startLocate() {
//        startObservable = Observable.interval(0, 60, TimeUnit.SECONDS)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        Log.d("fff",aLong+"");
//                        EventBus.getDefault().post(new MessageEvent(MessageEvent.START_LOCATE, ""));
//                    }
//                });

    }

    public static void stopSub() {
        if (checkObservable != null)
            checkObservable.unsubscribe();
        if (startObservable != null)
            startObservable.unsubscribe();
        checkObservable = null;
        startObservable = null;
    }

    public static AlertDialog.Builder createAlertDialog(final Context context, String title, String message) {
        // TODO Auto-generated method stub
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.create();
        return dialog;
    }

    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }


    public static void createDialog(Activity context, final List<String> list, final TextView textView, final OnSelectListener onSelectListener) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_wheel_dialog, null);
        final WheelView wheelView = (WheelView) view.findViewById(R.id.wheelView);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        TextView tvDone = (TextView) view.findViewById(R.id.tvDone);
        wheelView.setDivideLineColor(context.getResources().getColor(R.color.colorGray5));
        wheelView.setTextFocusColor(context.getResources().getColor(R.color.colorBlue));
        wheelView.setTextOutsideColor(context.getResources().getColor(R.color.colorGray6));
        wheelView.setTextSize(18);
//        view.setFontFace(DisplayUtil.getFontFace());
        wheelView.setOffset(2);
        wheelView.setTextPadding(0, DisplayUtil.dp2px(context, 5), 0, DisplayUtil.dp2px(context, 5));

        wheelView.setItems(list);
        if (list.size() < 3) {
            wheelView.setSeletion(list.size() - 1);
        } else {
            wheelView.setSeletion(2);
        }

        final AlertDialog dialog = new AlertDialog.Builder(context, R.style.ActionSheetDialogStyle).create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setLayout(DisplayUtil.getWindowWidth(context), WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.setContentView(view);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != null && list.size() != 0)
                    textView.setText(list.get(wheelView.getSeletedIndex()));
                if (onSelectListener != null) {
                    onSelectListener.onSelect(wheelView.getSeletedIndex());
                }
//
                dialog.dismiss();
            }
        });
    }

    public static String getVersion() {
        String version = "";
        try {
            PackageManager manager = AppManager.getAppManager().currentActivity().getPackageManager();
            PackageInfo info = manager.getPackageInfo(AppManager.getAppManager().currentActivity().getPackageName(), 0);
            version = info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }


    public static String strPassword(String string) {
        Log.d("aaa", string);
        char[] array = string.toCharArray();
        char[] array_true = new char[array.length];
        array_true[0] = array[0];
        array_true[array.length - 1] = array[array.length - 1];
        for (int i = 1; i < array.length - 1; i++) {
            array_true[i] = '*';
        }
//        Log.d("aaa", array_true.toString());
        return String.valueOf(array_true);
    }

    public static void turnToPhotoDetailActivity(Activity activity, String path, int width, int height, int locationX, int locationY) {
        Intent intent = new Intent(activity, PhotoDetailActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("width", width);
        intent.putExtra("height", height);
        intent.putExtra("locationX", locationX);
        intent.putExtra("locationY", locationY);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    public static void setValue(String begin, String end, String orderState, String phoneNumber, String czState) {
        strBegin = begin;
        strEnd = end;
        strOrderState = orderState;
        strPhoneNumber = phoneNumber;
        strCzState = czState;
    }

    public static void setValueNumber(String regular, String numberPool) {
        strRegular = regular;
        strNumPool = numberPool;
    }

    public static String strBack(String pre) {
        String now = "";
        if (!pre.equals("请选择")) {
            now = pre;
        }
        return now;
    }

    public static String strBackNull(String pre) {
        String now = "无";
        if (!pre.equals("请选择") && !pre.equals("")) {
            now = pre;
        }
        return now;
    }

    public static boolean isNull(String pre) {
        if (!pre.equals("请选择") && !pre.equals("") && !pre.equals("全部")) {
            return true;
        } else {
            return false;
        }

    }

    public static String encode(String text) {
        try {
            byte[] btInput = text.getBytes("UTF-8");
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                int number = b & 0xff;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0" + hex);
                } else {
                    sb.append(hex);
                }
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    public final static String GetMD5Code(String data) {
        try {
            byte[] btInput = data.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(btInput);
            byte b[] = md.digest();

            int i;

            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString().toUpperCase();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public static void createToast(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
    }

    private String toJson(Object obj, int method) {
        // TODO Auto-generated method stub
        if (method == 1) {

            //字段是首字母小写，其余单词首字母大写
            Gson gson = new Gson();
            String obj2 = gson.toJson(obj);
            return obj2;
        } else if (method == 2) {

            // FieldNamingPolicy.LOWER_CASE_WITH_DASHES    全部转换为小写，并用空格或者下划线分隔

            //FieldNamingPolicy.UPPER_CAMEL_CASE    所以单词首字母大写
            Gson gson2 = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
            String obj2 = gson2.toJson(obj);
            return obj2;
        }
        return "";
    }

    public static void gotoActy(Context context, Class clazz) {
        Intent intent = new Intent();
        if (clazz == LoginActivity.class) {
            intent.putExtra("from", "0");
        }
        intent.setClass(context, clazz);
        context.startActivity(intent);

    }

    public static String encodeBase(String path) {


        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 40, out);
//            bitmap.co

            out.flush();
            out.close();

            byte[] imgBytes = out.toByteArray();
            return Base64.encodeToString(imgBytes, Base64.DEFAULT);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            return null;
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

//    }
//        Bitmap bitmap = BitmapFactory.decodeFile(path);
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, in);

//
//        InputStream in = null;
//        byte[] data = null;
//// 读取图片字节数组
//        try {
//            in = new FileInputStream(path);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//// 对字节数组Base64编码
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);// 返回Base64编码过的字节数组字符串

    }

    public static boolean isLog(Activity context) {
        if (BaseCom.login.getSession_token() != null) {
            return true;
        } else {
            Util.createToast(context, "用户未登录,请先登录!");
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra("from", "0");
            context.startActivity(intent);
            return false;
        }
    }

    public static String[] getLocalAdmin(Activity activity) {
        SharedPreferences share = activity.getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
        String session = share.getString("session_token", "");
        String gride = share.getString("gride", "");
        String[] strings = new String[2];
        strings[0] = session;
        strings[1] = gride;
        return strings;
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static void showChart(int state, LineChart lineChart, LineData lineData, int color) {
        //是否在折线图上添加边框
        lineChart.setDrawBorders(false);
        // 数据描述
        switch (state) {
            case 0:
                lineChart.setDescription("月份");
                break;
            case 1:
                lineChart.setDescription("年份");
                break;
        }

        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("暂无数据");

        // 是否显示表格颜色
        lineChart.setDrawGridBackground(false);
        // 表格的的颜色，在这里是是给颜色设置一个透明度
//        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF);

        // 设置是否可以触摸
        lineChart.setTouchEnabled(true);
        // 是否可以拖拽
        lineChart.setDragEnabled(true);
        // 是否可以缩放
        lineChart.setScaleEnabled(true);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        lineChart.setBackgroundColor(Color.WHITE);// 设置背景
        // 设置数据
        lineChart.setData(lineData);

        // 设置比例图标示，就是那个一组y的value的
        Legend mLegend = lineChart.getLegend();
        //mLegend.setPosition(LegendPosition.BELOW_CHART_CENTER);
        // 样式
        mLegend.setForm(Legend.LegendForm.CIRCLE);
        // 字体
        mLegend.setFormSize(0f);
        // 颜色
        mLegend.setTextColor(Color.RED);
        // 字体
        //mLegend.setTypeface(mTf);

        // 设置Y轴右边不显示数字
        lineChart.getAxisRight().setEnabled(true);
        XAxis xAxis = lineChart.getXAxis();
        // 设置X轴的数据显示在报表的下方
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        // 设置不从X轴发出纵向直线

        xAxis.setDrawGridLines(false);
        // 立即执行的动画,x轴
        lineChart.animateX(2500);
    }

    public static LineData getLineData(int count, int flag, int state, Statistics[] statisticses, Commission[] commissions) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
//            xValues.add(""+i);
            switch (flag) {
                //佣金
                case 0:
                    switch (state) {
                        case 0:
                            xValues.add(commissions[i].getTime().substring(5, commissions.length + 1));
                            break;
                        case 1:
                            xValues.add(commissions[i].getTime());
                            break;
                    }

                    break;
                //开户量
                case 1:
                    switch (state) {
                        case 0:
                            xValues.add(statisticses[i].getTime().substring(5, statisticses.length + 1));
                            break;
                        case 1:
                            xValues.add(statisticses[i].getTime());
                            break;
                    }

                    break;
            }
        }

        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        // y轴的数据 1======================================start
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
//            float value = (float) (Math.random() * range) + 3;

            switch (flag) {
                case 0:
                    yValues.add(new Entry((float) commissions[i].getCommission(), i));
                    break;
                case 1:
                    yValues.add(new Entry((float) statisticses[i].getRecord(), i));
                    break;
            }
        }

        // create a dataset and give it a type
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "" /*测试折线图*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        // 线宽
        lineDataSet.setLineWidth(1.0f);
        // 显示的圆形大小
        lineDataSet.setCircleSize(2.5f);
        // 显示颜色
        lineDataSet.setColor(Color.RED);
        // 圆形的颜色
        lineDataSet.setCircleColor(Color.RED);
        // 高亮的线的颜色
        lineDataSet.setHighLightColor(Color.RED);
        // 设置圆点的颜色
        lineDataSet.setFillColor(Color.RED);
        lineDataSet.setDrawCircleHole(true);
        //lineDataSet.setValueTextSize(9f);
        lineDataSet.setFillAlpha(65);

        lineDataSets.add(lineDataSet);
        // y轴的数据 1======================================end

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }

    public static void saveClickTime(Activity context, String name) {
        SharedPreferences share = context.getSharedPreferences(name, MODE_PRIVATE);
        Integer click = share.getInt("click", 0);
        SharedPreferences.Editor edit = share.edit(); //编辑文件
        if (click == 0) {

            edit.putInt("click", 1);

        } else {
            edit.putInt("click", click + 1);

        }
        edit.commit();  //保存数据信息

    }

    public static void showBarChart(BarChart barChart, BarData barData) {
        barChart.setDrawBorders(false);  ////是否在折线图上添加边框

        barChart.setDescription("");// 数据描述

        // 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        barChart.setNoDataTextDescription("You need to provide data for the chart.");

        barChart.setDrawGridBackground(false); // 是否显示表格颜色
//        barChart.setGridBackgroundColor(Color.WHITE);
        barChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度

        barChart.setTouchEnabled(true); // 设置是否可以触摸

        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放

        barChart.setPinchZoom(false);//

        barChart.setBackgroundColor(Color.WHITE);// 设置背景

        barChart.setDrawBarShadow(false);

        barChart.setData(barData); // 设置数据

//        Legend mLegend = barChart.getLegend(); // 设置比例图标示
//        mLegend.setForm(Legend.LegendForm.LINE);
//        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
//        mLegend.setFormSize(6f);// 字体
//        mLegend.setTextColor(Color.BLACK);// 颜色

//      X轴设定
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        barChart.animateX(2500); // 立即执行的动画,x轴
    }

    public static BarData getBarData(int count, float range) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xValues.add("第" + (i + 1) + "季度");
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++) {
            float value = (float) (Math.random() * range/*100以内的随机数*/) + 3;
            yValues.add(new BarEntry(value, i));
        }

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, null);
//        BarDataSet barDataSet = new BarDataSet(yValues, "测试饼状图");
//
        barDataSet.setColor(Color.rgb(114, 188, 223));

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets
        BarData barData = new BarData(xValues, barDataSets);

        return barData;
    }

    public static int getClickTime(Activity context, String name) {
        SharedPreferences share = context.getSharedPreferences(name, MODE_PRIVATE);
        Integer click = share.getInt("click", 0);
        return click;

    }

    public static void cleartClickTime(Activity context, String name) {
        SharedPreferences share = context.getSharedPreferences(name, MODE_PRIVATE);
        SharedPreferences.Editor edit = share.edit(); //编辑文件
        edit.putInt("click", 0);
        edit.commit();  //保存数据信息

    }

    public static boolean compare(String s1, String s2) {
        if (s1.equals("请选择") || s2.equals("请选择")) {
            return false;
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dt1 = null, dt2 = null;
            try {
                dt1 = dateFormat.parse(s1);
                dt2 = dateFormat.parse(s2);
            } catch (java.text.ParseException e) {
                System.err.println("格式不正确");
            }
            //c1小于c2
            if (dt1.getTime() < dt2.getTime()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static boolean compareSecond(String s1, String s2) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date dt1 = null, dt2 = null;
        try {
            dt1 = dateFormat.parse(s1);
            dt2 = dateFormat.parse(s2);
        } catch (java.text.ParseException e) {
            System.err.println("格式不正确");
        }
        //c1小于c2
        if (dt1.getTime() < dt2.getTime()) {
            return true;
        } else {
            return false;
        }
    }

    public static String swap(String v) {
        char[] arr_v = v.toCharArray();
        char[] chars_v = new char[v.length()];
        for (int i = 0; i < arr_v.length; i++) {
            if (i % 2 == 0) {
                chars_v[i + 1] = arr_v[i];
            } else {
                chars_v[i - 1] = arr_v[i];
            }
        }
        return String.valueOf(chars_v);
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static void getFocus(EditText editText) {
        InputMethodManager

                imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleShow() {
        long time = System.currentTimeMillis();
        long timeD = time - lastShowTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastShowTime = time;
        return false;
    }


}

