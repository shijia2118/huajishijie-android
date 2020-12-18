package com.test.tworldapplication.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.View;

import com.test.tworldapplication.R;

/**
 * Created by 27733 on 2016/10/18.
 */
public class ChartView extends View {
    Context context;
    public int XPoint = 160;    //原点的X坐标
    public int YPoint = 610;     //原点的Y坐标
    public int XScale = 155;     //X的刻度长度
    public int YScale = 140;     //Y的刻度长度
    public int XLength = 580;        //X轴的长度
    public int YLength = 560;        //Y轴的长度
    public String[] XLabel = new String[]{"", "10", "20", "30", "40", "50", "60"};    //X的刻度
    public String[] YLabel = new String[]{"", "10", "20", "30", "40", "50", "60"};    //Y的刻度
    public String[] Data = new String[]{"", "10", "20", "30", "40", "50", "60"};;      //数据
    public String Title = "开户量";    //显示的标题

    public ChartView(Context context) {
        super(context);
        this.context = context;
    }

//    public void SetInfo(String[] XLabels, String[] YLabels, String[] AllData, String strTitle, int XScale, int YScale) {
//        this.XScale = XScale;
//        this.YScale = YScale;
//        XLabel = XLabels;
//        YLabel = YLabels;
//        Data = AllData;
//        Title = strTitle;
//        Resources resources = this.getResources();
//        DisplayMetrics dm = resources.getDisplayMetrics();
//        XLength = dm.widthPixels - 300;
//        XScale = dm.widthPixels / 8;
////        (WindowManager)context.getSystemService(Context.WINDOW_SERVICE).g
////        context.getW\
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);//重写onDraw方法

        //canvas.drawColor(Color.WHITE);//设置背景颜色
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//去锯齿
        paint.setColor(getResources().getColor(R.color.colorGray6));//颜色
        paint.setStrokeWidth((float) 5.0);
        Paint paint1 = new Paint();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setAntiAlias(true);//去锯齿
        paint1.setColor(Color.DKGRAY);
        Paint txtPaint = new Paint();
        txtPaint.setColor(getResources().getColor(R.color.colorGray5));//颜色
        txtPaint.setTextSize(30);  //设置轴文字大小
        txtPaint.setStrokeWidth((float) 1.0);
        Paint titlePaint = new Paint();
        titlePaint.setColor(getResources().getColor(R.color.colorGray));//颜色
        titlePaint.setTextSize(40);  //设置轴文字大小
        titlePaint.setStrokeWidth((float) 2.0);
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);//去锯齿
        linePaint.setColor(getResources().getColor(R.color.colorRed));//颜色
        linePaint.setStrokeWidth((float) 5.0);


        //设置Y轴
        canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paint);   //轴线
        for (int i = 0; i * YScale < YLength; i++) {
            canvas.drawLine(XPoint, YPoint - i * YScale, XPoint + 10, YPoint - i * YScale, paint);  //刻度
            try {
                canvas.drawText(YLabel[i], XPoint - 60, YPoint - i * YScale + 10, txtPaint);  //文字
            } catch (Exception e) {
            }
        }
        canvas.drawLine(XPoint, YPoint - YLength, XPoint - 10, YPoint - YLength + 20, paint);  //箭头
        canvas.drawLine(XPoint, YPoint - YLength, XPoint + 10, YPoint - YLength + 20, paint);
        //设置X轴
        canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paint);   //轴线
        for (int i = 0; i * XScale < XLength; i++) {
            canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i * XScale, YPoint - 10, paint);  //刻度X轴
            try {
                canvas.drawText(XLabel[i], XPoint + i * XScale - 30, YPoint + 60, txtPaint);  //文字
                //数据值
                if (i > 0 && YCoord(Data[i - 1]) != -999 && YCoord(Data[i]) != -999)  //保证有效数据
                    canvas.drawLine(XPoint + (i - 1) * XScale, YCoord(Data[i - 1]), XPoint + i * XScale, YCoord(Data[i]), linePaint);
                canvas.drawCircle(XPoint + i * XScale, YCoord(Data[i]), 4, linePaint);
                canvas.drawText(Data[i-1],  XPoint + (i-1) * XScale-20, YCoord(Data[i-1])-20, txtPaint);
                int j = XLength/XScale;
                canvas.drawText(Data[j],  XPoint + (j) * XScale-20, YCoord(Data[j])-20, txtPaint);
            } catch (Exception e) {
            }
        }
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 20, YPoint - 10, paint);    //箭头
        canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength - 20, YPoint + 10, paint);
        canvas.drawText(Title, 100, 50, titlePaint);
    }

    private float YCoord(String y0)  //计算绘制时的Y坐标，无数据时返回-999
    {
        float y;
        try {
            y = Float.parseFloat(y0);
        } catch (Exception e) {
            return -999;    //出错则返回-999
        }
        try {
            return (YPoint - y * YScale / Integer.parseInt(YLabel[1]));
        } catch (Exception e) {
        }
        return y;
    }


}
