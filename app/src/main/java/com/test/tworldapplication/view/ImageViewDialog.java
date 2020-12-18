package com.test.tworldapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.RegisterActivity;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DisplayUtil;

/**
 * Created by dasiy on 16/10/21.
 */

public class ImageViewDialog extends Dialog {
    ImageView imageView;
    Context context;

    public ImageViewDialog(Context context) {
        super(context);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageView = (ImageView) inflater.inflate(R.layout.view_image_detail, null);
        this.setContentView(imageView);
    }

    public void setFile(String file) {
        Bitmap bitmap = BitmapFactory.decodeFile(file);
        imageView.setImageBitmap(bitmap);
    }
}
