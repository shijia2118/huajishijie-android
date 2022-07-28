package com.test.tworldapplication.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.test.tworldapplication.R;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import wintone.passport.sdk.utils.AppManager;

import static com.test.tworldapplication.utils.DisplayUtil.dp2px;

//import com.min.xiju.Interface.SaveImageInterface;
//import com.min.xiju.R;


public class BitmapUtil {
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float) height / (float) reqHeight);
            int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromFile(Resources res, String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        if (BitmapFactory.decodeFile(pathName, options) == null) {
            return BitmapFactory.decodeResource(res, R.drawable.add);
        }
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static boolean isImageExist(String pathName) {
        return !(BitmapFactory.decodeFile(pathName) == null);
    }

//    public static Bitmap decodeSampledBitmapFromBitmap(Bitmap bitmap, int reqWidth, int reqHeight) {
//        byte[] data = Bitmap2Byte(bitmap);
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeByteArray(Bitmap2Byte(bitmap), 0, data.length, options);
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeByteArray(Bitmap2Byte(bitmap), 0, data.length, options);
//    }

    public static Bitmap decodeSampledBitmapFromURI(Context context, Uri uri, int reqWidth, int reqHeight) {
        return zoomBitmap(getBitmapFromUri(context, uri), reqWidth, reqHeight);
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.image_not_found);
        }
    }

    public static byte[] Bitmap2Byte(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        bitmap.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

//    public static String Image2Str(String imgFilePath) {
//        byte[] data = null;
//        try {
//            InputStream in = new FileInputStream(imgFilePath);
//            data = new byte[in.available()];
//            in.read(data);
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        BASE64Encoder encoder = new BASE64Encoder();
//        return encoder.encode(data);
//    }

    public static Bitmap compressBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 10;
        while (baos.toByteArray().length / 1024 > 200 && options >= 50) {
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);

        }
        ByteArrayInputStream stream = new ByteArrayInputStream(baos.toByteArray());

        return BitmapFactory.decodeStream(stream, null, null);
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int requestWidth, int requestHeight) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap temp = BitmapFactory.decodeStream(stream, null, options);
        options.inJustDecodeBounds = false;
        int width = options.outWidth;
        int height = options.outHeight;
        int scale = 1;
        if (width > height && width > requestWidth) {
            scale = (int) (options.outWidth / requestWidth);
        } else if (width < height && height > requestHeight) {
            scale = (int) (options.outHeight / requestHeight);
        }
        options.inSampleSize = scale > 0 ? scale : 1;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        stream = new ByteArrayInputStream(baos.toByteArray());
        temp = BitmapFactory.decodeStream(stream, null, options);

        return compressBitmap(temp);
    }

    //    public static boolean Str2Image(String imgStr, String imgFilePath) {
//        if (imgStr == null) {
//            return false;
//        }
//        BASE64Decoder decoder = new BASE64Decoder();
//        try {
//            byte[] bytes = decoder.decodeBuffer(imgStr);
//            for (int i = 0; i < bytes.length; ++i) {
//                if (bytes[i] < 0) {
//                    bytes[i] += 256;
//                }
//            }
//            OutputStream out = new FileOutputStream(imgFilePath);
//            out.write(bytes);
//            out.flush();
//            out.close();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
    public static String urlToBase64(Activity activity, String path) {
        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(activity.getResources(), path, DisplayUtil.dp2px(activity, 200), DisplayUtil.dp2px(activity, 200));
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String bitmapToBase64X(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String compressitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 65, baos);
                baos.flush();
                baos.close();
                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static Bitmap drawTextToBitmap(Bitmap bitmap, String text, int color, int size) {
        Bitmap.Config bitmapConfig = bitmap.getConfig();
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setShadowLayer(1f, 0f, 1f, Color.WHITE);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width()) / 20 * 19;
        int y = (bitmap.getHeight() + bounds.height()) / 20 * 19;
        canvas.drawText(text, x, y, paint);

        return bitmap;
    }

    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static void LoadImageByUrl(Context context, ImageView imageView, String url, int width, int height) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                .setSize(width, height)
//                        .setRadius(DisplayUtil.dp2px(context, 5))
//                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_CENTER)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)// 加载中或错误图片的ScaleType
                .setLoadingDrawableId(R.drawable.image_not_found) //设置加载过程中的图片
                .setFailureDrawableId(R.drawable.image_not_found) //设置加载失败后的图片
                .setUseMemCache(true)//设置使用缓存
                .setIgnoreGif(false)//设置支持gif
                //.setCircular(false)//设置显示圆形图片
                //.setSquare(true)
                .build();
        x.image().bind(imageView, url, imageOptions);
    }

    public static void LoadImageByUrl(ImageView.ScaleType type, ImageView imageView, String url, int width, int height) {
        ImageOptions imageOptions = new ImageOptions.Builder()
                //.setSize(width, height)
//                        .setRadius(DisplayUtil.dp2px(context, 5))
//                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                //.setPlaceholderScaleType(type)
                .setImageScaleType(type)// 加载中或错误图片的ScaleType
                .setLoadingDrawableId(R.drawable.image_not_found) //设置加载过程中的图片
                .setFailureDrawableId(R.drawable.image_not_found) //设置加载失败后的图片
                .setUseMemCache(true)//设置使用缓存
                .setIgnoreGif(false)//设置支持gif
                //.setCircular(false)//设置显示圆形图片
                //.setSquare(true)
                .build();
        x.image().bind(imageView, url, imageOptions);
    }

    public static void _LoadImageByUrl(ImageView.ScaleType type, ImageView imageView, String url, int width, int height) {
        ImageOptions imageOptions = new ImageOptions.Builder()
//                .setSize(width, height)
//                        .setRadius(DisplayUtil.dp2px(context, 5))
//                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setPlaceholderScaleType(type)
                .setImageScaleType(type)// 加载中或错误图片的ScaleType
                .setLoadingDrawableId(R.drawable.image_not_found) //设置加载过程中的图片
                .setFailureDrawableId(R.drawable.image_not_found) //设置加载失败后的图片
                .setUseMemCache(true)//设置使用缓存
                .setIgnoreGif(false)//设置支持gif
                //.setCircular(false)//设置显示圆形图片
                //.setSquare(true)
                .build();
        x.image().bind(imageView, url, imageOptions);
    }

    public static void LoadImageByUrl(ImageView imageView, String url) {


        ImageOptions imageOptions = new ImageOptions.Builder()
                .setPlaceholderScaleType(ImageView.ScaleType.FIT_CENTER)
                .setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                .setLoadingDrawableId(R.drawable.image_not_found)
                .setFailureDrawableId(R.drawable.image_not_found)
                .setUseMemCache(true)
                .setIgnoreGif(false)
                .build();
        x.image().bind(imageView, url, imageOptions);
    }

//    public static void saveBitmap(Context context, Bitmap bmp, String path, String name, SaveImageInterface saveImageInterface) {
//        File appDir = new File(path);
//        if (!appDir.exists()) {
//            appDir.mkdir();
//        }
//        File file = new File(appDir, name);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//            saveImageInterface.onSaveImageSucceed("保存成功");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            saveImageInterface.onSaveImageFailed("保存失败");
//        } catch (IOException e) {
//            e.printStackTrace();
//            saveImageInterface.onSaveImageFailed("保存失败");
//        }
//        try {
//            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), name, null);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path + name)));
//    }


    public static Bitmap newBitmap(Bitmap bit1, Bitmap bit2) {
        Bitmap newBit = null;
        int width = bit1.getWidth();
        if (bit2.getWidth() != width) {
            int h2 = bit2.getHeight() * width / bit2.getWidth();
            newBit = Bitmap.createBitmap(width, bit1.getHeight() + h2, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBit);
            Bitmap newSizeBitmap2 = getNewSizeBitmap(bit2, width, h2);
            canvas.drawBitmap(bit1, 0, 0, null);
            canvas.drawBitmap(newSizeBitmap2, 0, bit1.getHeight(), null);
        } else {
            newBit = Bitmap.createBitmap(width, bit1.getHeight() + bit2.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(newBit);
            canvas.drawBitmap(bit1, 0, 0, null);
            canvas.drawBitmap(bit2, 0, bit1.getHeight(), null);
        }
        return newBit;

    }

    public static Bitmap getNewSizeBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        float scaleWidth = ((float) newWidth) / bitmap.getWidth();
        float scaleHeight = ((float) newHeight) / bitmap.getHeight();
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap bit1Scale = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix,
                true);
        return bit1Scale;
    }


    public static Bitmap createWatermark(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newbitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和src长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newbitmap);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片


        //水印缩放
//        float scale = ((float) width) / 3;
//        Matrix matrix = new Matrix();
//        matrix.postScale(scale, scale);
//        Bitmap newbm = Bitmap.createBitmap(watermark, 0, 0, width, height, matrix, true);
        float width2 = ((float) width) / 3;
        float scale = width2 / watermark.getWidth();
        float height2 = watermark.getHeight() * scale;
        Bitmap newbm = Bitmap.createBitmap(watermark, 0, 0, (int) width2, (int) height2);

        canvas.drawBitmap(watermark, src.getWidth() - newbm.getWidth(),
                src.getHeight() - newbm.getHeight() - 50, null);
        // 保存
        canvas.save();
//        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();
        return newbitmap;
    }


    public static Bitmap createWaterMaskRightBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingRight, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark,
                src.getWidth() - watermark.getWidth() - dp2px(context, paddingRight),
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }

    public static Bitmap createWaterMaskLeftBottom(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingBottom) {
        return createWaterMaskBitmap(src, watermark,
                dp2px(context, paddingLeft),
                src.getHeight() - watermark.getHeight() - dp2px(context, paddingBottom));
    }


    private static Bitmap createWaterMaskBitmap(Bitmap src, Bitmap watermark,
                                                int paddingLeft, int paddingTop) {
        if (src == null) {
            return null;
        }
        int width = src.getWidth();
        int height = src.getHeight();
        //创建一个bitmap
        Bitmap newb = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        //将该图片作为画布
        Canvas canvas = new Canvas(newb);
        //在画布 0，0坐标上开始绘制原始图片
        canvas.drawBitmap(src, 0, 0, null);
        //在画布上绘制水印图片
        canvas.drawBitmap(watermark, paddingLeft, paddingTop, null);
        // 保存
        canvas.save();
//        canvas.save(Canvas.ALL_SAVE_FLAG);
        // 存储
        canvas.restore();
        return newb;
    }


    public static Bitmap drawTextToCenter(Context context, Bitmap bitmap, String text,
                                          int size, int color) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                (bitmap.getWidth() - bounds.width()) / 2,
                (bitmap.getHeight() + bounds.height()) / 2);
    }

    //图片上绘制文字
    private static Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String text,
                                           Paint paint, Rect bounds, int paddingLeft, int paddingTop) {
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();

        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        //canvas.rotate(45);
        canvas.drawText(text, paddingLeft, paddingTop, paint);
        return bitmap;

    }


    public static Bitmap drawableToBitmap(Drawable drawable) {
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap.Config config =
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        //注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap createWaterMaskLeftTop(
            Context context, Bitmap src, Bitmap watermark,
            int paddingLeft, int paddingTop) {
        return createWaterMaskBitmap(src, watermark,
                dp2px(context, paddingLeft), dp2px(context, paddingTop));
    }

    public static Bitmap drawTextToLeftBottom(Context context, Bitmap bitmap, String text,
                                              int size, int color, int paddingLeft, int paddingBottom) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color);
        paint.setTextSize(dp2px(context, size));
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        return drawTextToBitmap(context, bitmap, text, paint, bounds,
                dp2px(context, paddingLeft),
                bitmap.getHeight() - dp2px(context, paddingBottom));
    }


}