package com.test.tworldapplication.activity.card;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Photo;
import com.test.tworldapplication.entity.PostNumberSegment;
import com.test.tworldapplication.entity.PostPictureUpload;
import com.test.tworldapplication.entity.PostTips;
import com.test.tworldapplication.entity.PostTransfer;
import com.test.tworldapplication.entity.RequestNumberSegment;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.entity.RequestTips;
import com.test.tworldapplication.entity.RequestTransfer;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.DialogManager;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;
import com.wildma.idcardcamera.camera.IDCardCamera;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import wintone.passport.sdk.utils.AppManager;

public class TransferCardActivity extends BaseActivity implements DialogInterface.OnKeyListener, View.OnFocusChangeListener {

    @BindView(R.id.imgNewIdFront)
    ImageView imgNewIdFront;
    @BindView(R.id.imgNewIdFrontRemove)
    ImageView imgNewIdFrontRemove;
    @BindView(R.id.imgNewIdBack)
    ImageView imgNewIdBack;
    @BindView(R.id.imgNewIdBackRemove)
    ImageView imgNewIdBackRemove;
    int flag = 0;
    int state_zere = 0, state_one = 0, state_two = 0, state_three = 0;
    String file_zero, file_one, file_two, file_three;
    Bitmap bitmap_zero, bitmap_one, bitmap_two, bitmap_three;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etId)
    EditText etId;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.imgOldIdFront)
    ImageView imgOldIdFront;
    @BindView(R.id.imgOldIdFrontRemove)
    ImageView imgOldIdFrontRemove;
    @BindView(R.id.imgOldIdBack)
    ImageView imgOldIdBack;
    @BindView(R.id.imgOldIdBackRemove)
    ImageView imgOldIdBackRemove;
    PopupWindow mPopupWindow;
    ViewHolder viewHolder;
    @BindView(R.id.activity_transfer_card)
    LinearLayout activityTransferCard;
    @BindView(R.id.ll_tips)
    LinearLayout llTips;
    @BindView(R.id.etLast0)
    EditText etLast0;
    @BindView(R.id.etLast1)
    EditText etLast1;
    @BindView(R.id.etLast2)
    EditText etLast2;
    final int CAMERA = 100;
    File tempFile;
    private final int REQUEST_CODE_CAMERA = 10086;


    /*隐藏提交按钮 号段验证成功之后显示*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_card);
        ButterKnife.bind(this);
        setBackGroundTitle("过户", true);
        initView();
    }

    private void initView() {
        etNumber.setOnFocusChangeListener(this);

        LayoutInflater mLayoutInflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_photo, null);
        viewHolder = new ViewHolder(music_popunwindwow);
        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                activityTransferCard.setAlpha((float) 1);

            }
        });
        HttpPost<PostTips> httpPost = new HttpPost<>();
        PostTips postTips = new PostTips();
        postTips.setSession_token(Util.getLocalAdmin(TransferCardActivity.this)[0]);
        postTips.setType(1);

        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postTips);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postTips) + BaseCom.APP_PWD));

        new CardHttp().getTips(CardRequest.getTips(new SuccessValue<HttpRequest<RequestTips>>() {
            @Override
            public void OnSuccess(HttpRequest<RequestTips> value) {
                if (value.getCode() == 10000 & value.getData().getCount() > 0) {
                    for (int i = 0; i < value.getData().getTips().length; i++) {
                        if (i == 0) {
                            TextView textView = new TextView(TransferCardActivity.this);
                            textView.setText("温馨提醒");
                            textView.setTextColor(Color.RED);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 5;
                            params.bottomMargin = 5;
                            textView.setLayoutParams(params);
                            llTips.addView(textView);
                        }
                        TextView textView = new TextView(TransferCardActivity.this);
                        textView.setText(i + 1 + ". " + value.getData().getTips()[i].getTips());
                        textView.setTextColor(Color.RED);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.topMargin = 5;
                        params.bottomMargin = 5;
                        textView.setLayoutParams(params);
                        llTips.addView(textView);
                    }
                }
            }
        }), httpPost);
    }

    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {

        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            Log.i("test", "onHanlderSuccess: " + resultList.toString());
            if (resultList != null) {
                String path = resultList.get(0).getPhotoPath();
//                Bitmap bitmap = BitmapFactory.decodeFile(path);
//                viewHolder.imgSelect.setImageBitmap(bitmap);
                switch (flag) {
                    case 0:
                        file_zero = path;
                        break;
                    case 1:
                        file_one = path;
                        break;
                    case 2:
                        file_two = path;
                        break;
                    case 3:
                        file_three = path;
                        break;

                }
                disPlayImage(path);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

    @OnClick({R.id.imgNewIdFront, R.id.imgNewIdFrontRemove, R.id.imgNewIdBack, R.id.imgNewIdBackRemove, R.id.imgOldIdFront, R.id.imgOldIdFrontRemove, R.id.imgOldIdBack, R.id.imgOldIdBackRemove})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgOldIdFront:
                flag = 0;
                switch (state_zere) {
                    case 0:
                        requestPermission();
//                        DialogManager.changeAvatar(TransferCardActivity.this, mOnHanlderResultCallback);
//                        BaseUtils.pickPhoto(TransferCardActivity.this);
//                        activityTransferCard.setAlpha((float) 0.1);
//                        mPopupWindow.showAtLocation(imgOldIdFront, Gravity.BOTTOM, 0, 0);

                        break;
                    case 1:
                        int[] location = new int[2];
                        imgOldIdFront.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(TransferCardActivity.this, file_zero, DisplayUtil.dp2px(TransferCardActivity.this, 60), DisplayUtil.dp2px(TransferCardActivity.this, 60), location[0], location[1]);


                        break;
                }
                break;
            case R.id.imgOldIdFrontRemove:
                flag = 0;
                disPlayImage(null);
                break;
            case R.id.imgOldIdBack:
                flag = 1;
                switch (state_one) {
                    case 0:
                        requestPermission();
//                        DialogManager.changeAvatar(TransferCardActivity.this, mOnHanlderResultCallback);
//                        BaseUtils.pickPhoto(TransferCardActivity.this);
//                        activityTransferCard.setAlpha((float) 0.1);
//                        mPopupWindow.showAtLocation(imgOldIdFront, Gravity.BOTTOM, 0, 0);
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgOldIdBack.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(TransferCardActivity.this, file_one, DisplayUtil.dp2px(TransferCardActivity.this, 60), DisplayUtil.dp2px(TransferCardActivity.this, 60), location[0], location[1]);
                        break;
                }
                break;
            case R.id.imgOldIdBackRemove:
                flag = 1;
                disPlayImage(null);
                break;
            case R.id.imgNewIdFront:
                flag = 2;
                switch (state_two) {
                    case 0:
                        requestPermission();
//                        DialogManager.changeAvatar(TransferCardActivity.this, mOnHanlderResultCallback);
//                        BaseUtils.pickPhoto(TransferCardActivity.this);
//                        activityTransferCard.setAlpha((float) 0.1);
//                        mPopupWindow.showAtLocation(imgOldIdFront, Gravity.BOTTOM, 0, 0);
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgNewIdFront.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(TransferCardActivity.this, file_two, DisplayUtil.dp2px(TransferCardActivity.this, 60), DisplayUtil.dp2px(TransferCardActivity.this, 60), location[0], location[1]);
                        break;
                }
                break;
            case R.id.imgNewIdFrontRemove:
                flag = 2;
                disPlayImage(null);
                break;
            case R.id.imgNewIdBack:
                flag = 3;
                switch (state_three) {
                    case 0:
                        requestPermission();
//                        DialogManager.changeAvatar(TransferCardActivity.this, mOnHanlderResultCallback);
//                        BaseUtils.pickPhoto(TransferCardActivity.this);
//                        activityTransferCard.setAlpha((float) 0.1);
//                        mPopupWindow.showAtLocation(imgOldIdFront, Gravity.BOTTOM, 0, 0);
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgNewIdBack.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(TransferCardActivity.this, file_three, DisplayUtil.dp2px(TransferCardActivity.this, 60), DisplayUtil.dp2px(TransferCardActivity.this, 60), location[0], location[1]);
                        break;
                }
                break;
            case R.id.imgNewIdBackRemove:
                flag = 3;
                disPlayImage(null);
                break;

        }
    }

    private void disPlayImage(String capturePath) {
        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(getResources(), capturePath, DisplayUtil.dp2px(TransferCardActivity.this, 200), DisplayUtil.dp2px(TransferCardActivity.this, 200));
        switch (flag) {
            case 0:
                imgOldIdFront.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_zere = 1;
                    bitmap_zero = bitmap;
                    imgOldIdFrontRemove.setVisibility(View.VISIBLE);
                } else {
                    state_zere = 0;
                    bitmap_zero = null;
                    imgOldIdFrontRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                imgOldIdBack.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_one = 1;
                    bitmap_one = bitmap;
                    imgOldIdBackRemove.setVisibility(View.VISIBLE);
                } else {
                    state_one = 0;
                    bitmap_one = null;
                    imgOldIdBackRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 2:
                imgNewIdFront.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_two = 1;
                    bitmap_two = bitmap;
                    imgNewIdFrontRemove.setVisibility(View.VISIBLE);
                } else {
                    state_two = 0;
                    bitmap_two = null;
                    imgNewIdFrontRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 3:
                imgNewIdBack.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_three = 1;
                    bitmap_three = bitmap;
                    imgNewIdBackRemove.setVisibility(View.VISIBLE);
                } else {
                    state_three = 0;
                    bitmap_three = null;
                    imgNewIdBackRemove.setVisibility(View.INVISIBLE);
                }
                break;

        }

    }

    public static void showCamera(Activity activity, File file, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = activity.getPackageName() + ".fileprovider"; //【清单文件中provider的authorities属性的值】
            Log.d("xxx", authority);
            Log.d("xxx", file.getName());
            uri = FileProvider.getUriForFile(activity, authority, file);
        } else {
            uri = Uri.fromFile(file);
        }
        Log.i("bqt", "【uri】" + uri);//【content://{$authority}/files/bqt/temp】或【file:///storage/emulated/0/bqt/temp】
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            DialogManager.changeAvatar(TransferCardActivity.this, new SuccessNull() {
                @Override
                public void onSuccess() {
                    File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                    fileDir = new File(fileDir, "bqt");
                    if (!fileDir.exists()) fileDir.mkdirs();
                    switch (flag) {
                        case 0:
                            tempFile = new File(fileDir, "temp_gh0.jpg");
                            showCamera(TransferCardActivity.this, tempFile, REQUEST_CODE_CAMERA);
                            break;
                        case 1:
                            tempFile = new File(fileDir, "temp_gh1.jpg");
                            showCamera(TransferCardActivity.this, tempFile, REQUEST_CODE_CAMERA);
                            break;
                        case 2:
                            tempFile = new File(fileDir, "temp_gh2.jpg");
                            IDCardCamera.create(TransferCardActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                            break;
                        case 3:
                            tempFile = new File(fileDir, "temp_gh3.jpg");
                            showCamera(TransferCardActivity.this, tempFile, REQUEST_CODE_CAMERA);
                            break;

                    }
                }
            }, mOnHanlderResultCallback);
        } else
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    {
                        DialogManager.changeAvatar(TransferCardActivity.this, new SuccessNull() {
                            @Override
                            public void onSuccess() {
                                File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                                fileDir = new File(fileDir, "bqt");
                                if (!fileDir.exists()) fileDir.mkdirs();
                                switch (flag) {
                                    case 0:
                                        tempFile = new File(fileDir, "temp_gh0.jpg");
                                        showCamera(TransferCardActivity.this, tempFile, REQUEST_CODE_CAMERA);
                                        break;
                                    case 1:
                                        tempFile = new File(fileDir, "temp_gh1.jpg");
                                        showCamera(TransferCardActivity.this, tempFile, REQUEST_CODE_CAMERA);
                                        break;
                                    case 2:
                                        tempFile = new File(fileDir, "temp_gh2.jpg");
                                        IDCardCamera.create(TransferCardActivity.this).openCamera(IDCardCamera.TYPE_IDCARD_FRONT);
                                        break;
                                    case 3:
                                        tempFile = new File(fileDir, "temp_gh3.jpg");
                                        showCamera(TransferCardActivity.this, tempFile, REQUEST_CODE_CAMERA);
                                        break;

                                }
                            }
                        }, mOnHanlderResultCallback);
                    }
                } else {
                    Util.createToast(TransferCardActivity.this, "请赋予权限");
                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    String capturePath = cursor.getString(columnIndex);
                    switch (flag) {
                        case 0:
                            file_zero = capturePath;
                            break;
                        case 1:
                            file_one = capturePath;
                            break;
                        case 2:
                            file_two = capturePath;
                            break;
                        case 3:
                            file_three = capturePath;
                            break;
                    }
                    cursor.close();
                    disPlayImage(capturePath);
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    switch (flag) {
                        case 0:
                            disPlayImage(file_zero);
                            break;
                        case 1:
                            disPlayImage(file_one);
                            break;
                        case 2:
                            disPlayImage(file_two);
                            break;
                        case 3:
                            disPlayImage(file_three);
                            break;

                    }
                    break;
                }else if(resultCode==IDCardCamera.RESULT_CODE){  //身份证识别页返回的数据
                    file_two=IDCardCamera.getImagePath(data);
                    disPlayImage(file_two);
                    Log.d("aaa", ">>>>>>filetow>>>>"+file_two);

                }
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    String path = tempFile.getAbsolutePath();
                    Log.d("aaa", ">>>>>>flat>>>>"+path);

                    switch (flag) {
                        case 0:
                            file_zero = path;
                            disPlayImage(file_zero);
                            break;
                        case 1:
                            file_one = path;
                            disPlayImage(file_one);
                            break;
                        case 2:
                            file_two = path;
                            disPlayImage(file_two);
                            break;
                        case 3:
                            file_three = path;
                            disPlayImage(file_three);
                            break;
                    }

                }
                break;
//                if (resultCode == IDCardCamera.RESULT_CODE) {
//                    //获取图片路径，显示图片
//                    final String path = IDCardCamera.getImagePath(data);
//                    if (!TextUtils.isEmpty(path)) {
//                        if (requestCode == IDCardCamera.TYPE_IDCARD_FRONT) { //身份证正面
//                            imgNewIdFront.setImageBitmap(BitmapFactory.decodeFile(path));
//                        } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
//                            imgNewIdBack.setImageBitmap(BitmapFactory.decodeFile(path));
//                        }
//
//                        //实际开发中将图片上传到服务器成功后需要删除全部缓存图片
////                FileUtils.clearCache(this);
//                    }
//                }
        }
    }

    @OnClick(R.id.tvSubmit)
    public void onClick() {
        /*所有信息必输*/
        if (!Util.isFastDoubleClick()) {
            final String strNumber = etNumber.getText().toString();
            final String strName = etName.getText().toString();
            final String strId = etId.getText().toString();
            final String strAddress = etAddress.getText().toString();
            final String strPhone = etPhone.getText().toString();
            final String strLast0 = etLast0.getText().toString();
            final String strLast1 = etLast1.getText().toString();
            final String strLast2 = etLast2.getText().toString();
            if (strNumber.equals("") || strName.equals("") || strId.equals("") || strAddress.equals("") || strPhone.equals("")) {
                Util.createToast(TransferCardActivity.this, "请将信息填写完整!");
            } else if (strNumber.length() != 11 || strPhone.length() != 11 || strLast0.length() != 11 || strLast1.length() != 11 || strLast2.length() != 11) {
                Util.createToast(TransferCardActivity.this, "请输入正确的手机号!");
            } else if (strId.length() != 15 && strId.length() != 18) {
                Util.createToast(TransferCardActivity.this, "请输入正确证件号码!");
            } else if (state_zere == 0 || state_one == 0 || state_two == 0 || state_three == 0) {
                Util.createToast(TransferCardActivity.this, "请将图片上传完整!");
            } else {
                Intent intent = new Intent(this, FaceRecordingActivity.class);
                Bundle bundle = new Bundle();
                intent.putExtra("from", "7");
                intent.putExtra("name",strName);
                intent.putExtra("number",strNumber);
                intent.putExtra("cardId",strId);
                intent.putExtra("phone",strPhone);
                intent.putExtra("address",strAddress);
                intent.putExtra("numOne",strLast0);
                intent.putExtra("numTwo",strLast1);
                intent.putExtra("numThree",strLast2);

                BaseCom.photoOne = bitmap_two;
                BaseCom.photoTwo = bitmap_zero;
                BaseCom.photoThree = bitmap_three;
                BaseCom.photoFour = bitmap_one;

                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);


//                dialog.getTvTitle().setText("正在上传图片");
//                dialog.show();
//                HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
//                PostPictureUpload postPictureUpload = new PostPictureUpload();
//                postPictureUpload.setSession_token(Util.getLocalAdmin(TransferCardActivity.this)[0]);
//                postPictureUpload.setType("2");
//                Photo photo = new Photo();
//                photo.setPhoto1(BitmapUtil.compressitmapToBase64(bitmap_zero));
//                photo.setPhoto2(BitmapUtil.compressitmapToBase64(bitmap_one));
//                photo.setPhoto3(BitmapUtil.compressitmapToBase64(bitmap_two));
//                photo.setPhoto4(BitmapUtil.compressitmapToBase64(bitmap_three));
//                httpPost.setPhoto(photo);
//                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
//                httpPost.setParameter(postPictureUpload);
//                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
//                new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(TransferCardActivity.this, dialog, new SuccessValue<RequestPictureUpload>() {
//                    @Override
//                    public void OnSuccess(RequestPictureUpload value) {
//                        Log.d("aaa", value.getPhoto1());
//                        Log.d("aaa", value.getPhoto2());
//                        Log.d("aaa", value.getPhoto3());
//                        Log.d("aaa", value.getPhoto4());
//                        dialog.getTvTitle().setText("正在提交");
//                        dialog.show();
//                        HttpPost<PostTransfer> httpPost = new HttpPost<>();
//                        PostTransfer postTransfer = new PostTransfer();
//                        postTransfer.setSession_token(Util.getLocalAdmin(TransferCardActivity.this)[0]);
//                        postTransfer.setNumber(strNumber);
//                        postTransfer.setName(strName);
//                        postTransfer.setCardId(strId);
//                        postTransfer.setPhotoOne(value.getPhoto1());
//                        postTransfer.setPhotoTwo(value.getPhoto2());
//                        postTransfer.setPhotoThree(value.getPhoto3());
//                        postTransfer.setPhotoFour(value.getPhoto4());
//                        postTransfer.setTel(strPhone);
//                        postTransfer.setAddress(strAddress);
//                        postTransfer.setNumOne(strLast0);
//                        postTransfer.setNumTwo(strLast1);
//                        postTransfer.setNumThree(strLast2);
//                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
//                        httpPost.setParameter(postTransfer);
//                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postTransfer) + BaseCom.APP_PWD));
//                        new CardHttp().transfer(CardRequest.transfer(new SuccessValue<HttpRequest<RequestTransfer>>() {
//                            @Override
//                            public void OnSuccess(HttpRequest<RequestTransfer> value) {
//                                Log.d("aaa", value.getMes());
//                                Util.createToast(TransferCardActivity.this, value.getMes());
//                                if (value.getCode() == BaseCom.NORMAL) {
//                                    AppManager.getAppManager().finishActivity();
//                                } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT)
//                                    Util.gotoActy(TransferCardActivity.this, LoginActivity.class);
//                            }
//                        }, dialog), httpPost);
//                    }
//                }), httpPost);


            }
        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
        } else {
            if (etNumber.getText().toString().length() != 11) {
                Util.createToast(TransferCardActivity.this, "请输入正确的手机号码");
            } else {
                HttpPost<PostNumberSegment> httpPost = new HttpPost<>();
                PostNumberSegment postNumberSegment = new PostNumberSegment();
                postNumberSegment.setSession_token(Util.getLocalAdmin(TransferCardActivity.this)[0]);
                postNumberSegment.setTel(etNumber.getText().toString());
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postNumberSegment);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNumberSegment) + BaseCom.APP_PWD));
                new CardHttp().numberSegment(CardRequest.numberSegment(new SuccessValue<HttpRequest<RequestNumberSegment>>() {
                    @Override
                    public void OnSuccess(HttpRequest<RequestNumberSegment> value) {
                        Util.createToast(TransferCardActivity.this, value.getMes());
                        if (value.getCode() == BaseCom.NORMAL) {
                        } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {
                            etNumber.setText("");
                            Util.gotoActy(TransferCardActivity.this, LoginActivity.class);
                        } else
                            etNumber.setText("");
                    }
                }), httpPost);
            }
        }
    }

    class ViewHolder {
        @BindView(R.id.tvTake)
        TextView tvTake;
        @BindView(R.id.tvBook)
        TextView tvBook;
        @BindView(R.id.tvCancel)
        TextView tvCancel;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.tvTake, R.id.tvBook, R.id.tvCancel})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvTake:
                    Intent imageCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    String strImgPath = Environment.getExternalStorageDirectory()
                            .toString() + "/dlion/";// 存放照片的文件夹
                    String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                            .format(new Date()) + ".jpg";// 照片命名
                    File out = new File(strImgPath);
                    if (!out.exists()) {
                        out.mkdirs();
                    }
                    out = new File(strImgPath, fileName);
                    strImgPath = strImgPath + fileName;// 该照片的绝对路径
                    switch (flag) {
                        case 0:
                            file_zero = strImgPath;
                            break;
                        case 1:
                            file_one = strImgPath;
                            break;
                        case 2:
                            file_two = strImgPath;
                            break;
                        case 3:
                            file_three = strImgPath;
                            break;
                    }

                    Uri uri = Uri.fromFile(out);
                    imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(imageCaptureIntent, 1);


                    mPopupWindow.dismiss();
                    break;
                case R.id.tvBook:
                    BaseUtils.pickPhoto(TransferCardActivity.this);


                    mPopupWindow.dismiss();
                    break;
                case R.id.tvCancel:
                    mPopupWindow.dismiss();
                    break;
            }
        }
    }
}
