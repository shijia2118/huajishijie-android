package com.test.tworldapplication.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.test.tworldapplication.R;

/**
 * Created by dasiy on 16/10/17.
 */
public class LineChart extends View {
    /**
     * 坐标轴的颜色
     */
    private int xyColor;

    /**
     * 坐标轴的宽度
     */
    private int xyWidth;

    /**
     * 坐标轴文字的颜色
     */
    private int xyTextColor;

    /**
     * 坐标轴文字的大小
     */
    private int xyTextSize;

    /**
     * 坐标轴的之间的间距
     */
    private int interval;

    /**
     * 折线的颜色
     */
    private int lineColor;

    /**
     * 背景颜色
     */
    private int bgColor;

    /**
     * 原点坐标最大x
     */
    private int ori_x;

    /**
     * 第一个点的坐标
     */
    private int first_x;

    /**
     * 第一个点的坐标最小x，和最大x坐标
     */
    private int ori_min_x, ori_max_x;

    /**
     * 原点坐标y
     */
    private int ori_y;

    /**
     * x的刻度值长度 默认值40
     */
    private int xScale = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 80, getResources().getDisplayMetrics());

    /**
     * y的刻度值长度
     */
    private int yScale = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 55, getResources().getDisplayMetrics());

    /**
     * x刻度
     */
    private String[] xLabels;

    /**
     * y刻度
     */
    private String[] yLabels;

    /**
     * x坐标轴中最远的坐标值
     */
    private int maxX_X, maxX_Y;

    /**
     * y坐标轴的最远坐标值
     */
    private int minY_X, minY_Y;

    /**
     * x轴最远的坐标轴
     */
    private int x_last_x, x_last_y;
    /**
     * y轴最远的坐标值
     */
    private int y_last_x, y_last_y;

    private double[] dataValues;

    /**
     * 滑动时候，上次手指的x坐标
     */
    private float startX;

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LineChart);
//        int count = array.getIndexCount();
//        for (int i = 0; i < count; i++) {
//            int attr = array.getIndex(i);
//            switch (attr) {
//                case R.styleable.LineChart_xylinecolor:
//                    xyColor = array.getColor(attr, Color.GRAY);
//
//                    break;
//
//                case R.styleable.LineChart_xylinewidth:
//                    xyWidth = (int) array.getDimension(attr, 5);
//                    break;
//
//                case R.styleable.LineChart_xytextcolor:
//
//                    xyTextColor = array.getColor(attr, Color.BLACK);
//                    break;
//                case R.styleable.LineChart_xytextsize:
//                    xyTextSize = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                            12, getResources().getDisplayMetrics()));
//                    break;
//
//                case R.styleable.LineChart_linecolor:
//
//                    lineColor = array.getColor(attr, Color.GRAY);
//                    break;
//
//                case R.styleable.LineChart_bgcolor:
//                    bgColor = array.getColor(attr, Color.WHITE);
//                    break;
//
//                case R.styleable.LineChart_interval:
//                    interval = (int) array.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
//                            100, getResources().getDisplayMetrics()));
//                    break;
//                default:
//                    break;
//            }
//        }
//        array.recycle();

//        int width = getWidth();
//        int height = getHeight();
//        ori_x=40;
//        ori_y=height-40;
//
//        maxX_X=width-50;
//        minY_Y=50;
//
//
//        ori_min_x=width-50-40-dataValues.length*xScale;
//        first_x=ori_x;
//        ori_max_x=first_x;

    }
//        private void drawXYLine(Canvas canvas)
//        {
//                 Paint paint = new Paint();
//                 paint.setColor(xyColor);
//                 paint.setAntiAlias(true);
//                 paint.setStrokeWidth(xyWidth);
//                 paint.setTextSize(xyTextSize);
//                 // 绘画x轴
//                 int max = first_x + (xLabels.length-1) * xScale + 50;
//                 if (max > maxX_X)
//                 {
//                     max = getMeasuredWidth();}
//
//            x_last_x = max;
//            x_last_y = ori_y;
//            canvas.drawLine(first_x, ori_y, max, ori_y, paint);
//            // 绘画y轴
//            int min = ori_y - (yLabels.length - 1) * yScale - 50;
//            if (min < minY_Y)
//            {
//                min = minY_Y;
//            }
//            y_last_x = first_x;
//            y_last_y = min;
//            canvas.drawLine(first_x, ori_y, first_x, min, paint);

            // 绘画x轴的刻度
//            drawXLablePoints(canvas, paint);
            // 绘画y轴的刻度
//            drawYLablePoints(canvas, paint);
//        }

//    private void drawDataLine(Canvas canvas)
//         {
//             Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
//             // paint.setStyle(Paint.Style.FILL);
//               paint.setColor(xyColor);
//             Path path = new Path();
//             for (int i = 0; i < dataValues.length; i++)
//                     {
//                         int x = first_x + xScale * i;
//                       if (i == 0)
//                              {
//                                 path.moveTo(x, getYValue(dataValues[i]));
//                              }
//                      else
//                         {
//                                  path.lineTo(x, getYValue(dataValues[i]));
//                              }
//                           canvas.drawCircle(x, getYValue(dataValues[i]), xyWidth, paint);
//                      }
//                path.lineTo(first_x + xScale * (dataValues.length - 1), ori_y);
//                   path.lineTo(first_x, ori_y);
//                   path.close();
//                   paint.setStrokeWidth(5);
//                   // paint.setColor(Color.parseColor("#D7FFEE"));
//                   paint.setColor(Color.parseColor("#A23400"));
//                   paint.setAlpha(100);
//                   // 画折线
//                  canvas.drawPath(path, paint);
//                   paint.setStyle(Paint.Style.FILL);
//                   paint.setColor(Color.RED);
//                  canvas.clipPath(path);
//
//                   // 将折线超出x轴坐标的部分截取掉
//                   paint.setStyle(Paint.Style.FILL);
//                   paint.setColor(bgColor);
//                   paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
//                   RectF rectF = new RectF(0, 0, x_last_x, ori_y);
//                   canvas.drawRect(rectF, paint);
//            }
//
//                private float getYValue(double value)
//           {
//
//                 return (float) (ori_y - value / 50 * yScale);
//              }
    //    <br><br>

}
