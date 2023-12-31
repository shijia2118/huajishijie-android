package com.test.tworldapplication.activity.card;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
import com.test.tworldapplication.entity.PostRepair;
import com.test.tworldapplication.entity.PostTips;
import com.test.tworldapplication.entity.RequestNumberSegment;
import com.test.tworldapplication.entity.RequestPictureUpload;
import com.test.tworldapplication.entity.RequestRepair;
import com.test.tworldapplication.entity.RequestTips;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.content.FileProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import wintone.passport.sdk.utils.AppManager;

public class ReplaceCardActivity extends BaseActivity implements DialogInterface.OnKeyListener, View.OnFocusChangeListener {


    @BindView(R.id.imgFront)
    ImageView imgFront;
    @BindView(R.id.imgFrontRemove)
    ImageView imgFrontRemove;
    @BindView(R.id.imgIdBack)
    ImageView imgIdBack;
    int flag = 0;
    @BindView(R.id.imgIdBackRemove)
    ImageView imgIdBackRemove;
    int state_zere = 0, state_one = 0;
    String file_zero, file_one;
    @BindView(R.id.tvPostItem)
    TextView tvPostItem;
    @BindView(R.id.llPostItem)
    LinearLayout llPostItem;
    List<String> list = new ArrayList<>();
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etId)
    EditText etId;
    @BindView(R.id.etIdAddress)
    EditText etIdAddress;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etGetName)
    EditText etGetName;
    @BindView(R.id.etGetPhone)
    EditText etGetPhone;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.rlBack)
    RelativeLayout rlBack;
    Bitmap bitmap_zero;
    ViewHolder viewHolder;
    PopupWindow mPopupWindow;
    @BindView(R.id.activity_replace_card)
    LinearLayout activityReplaceCard;
    String strPosition = "";
    @BindView(R.id.ll_tips)
    LinearLayout llTips;
    @BindView(R.id.etLast0)
    EditText etLast0;
    @BindView(R.id.etLast1)
    EditText etLast1;
    @BindView(R.id.etLast2)
    EditText etLast2;
    final int CAMERA = 100;
    private File tempFile;
    private final int REQUEST_CODE_CAMERA = 10086;


    /*提交按钮不可见,输入框失去焦点验证号段成功之后显示按钮,上传图片成功之后调用补卡接口*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_card);
        ButterKnife.bind(this);
        setBackGroundTitle("补卡", true);
        initView();

    }

    private void initView() {
        rlBack.setVisibility(View.GONE);
        list.clear();
        list.add("顺丰到付");
        list.add("充100免费邮寄");
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
                activityReplaceCard.setAlpha((float) 1);

            }
        });
        HttpPost<PostTips> httpPost = new HttpPost<>();
        PostTips postTips = new PostTips();
        postTips.setSession_token(Util.getLocalAdmin(ReplaceCardActivity.this)[0]);
        postTips.setType(2);

        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postTips);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postTips) + BaseCom.APP_PWD));

        new CardHttp().getTips(CardRequest.getTips(new SuccessValue<HttpRequest<RequestTips>>() {
            @Override
            public void OnSuccess(HttpRequest<RequestTips> value) {
                if (value.getCode() == 10000 & value.getData().getCount() > 0) {
                    for (int i = 0; i < value.getData().getTips().length; i++) {
                        if (i == 0) {
                            TextView textView = new TextView(ReplaceCardActivity.this);
                            textView.setText("温馨提醒");
                            textView.setTextColor(Color.RED);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 5;
                            params.bottomMargin = 5;
                            textView.setLayoutParams(params);
                            llTips.addView(textView);
                        }
                        TextView textView = new TextView(ReplaceCardActivity.this);
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

    private void disPlayImage(String capturePath) {
        Bitmap bitmap = BitmapUtil.decodeSampledBitmapFromFile(getResources(), capturePath, DisplayUtil.dp2px(ReplaceCardActivity.this, 200), DisplayUtil.dp2px(ReplaceCardActivity.this, 200));
        switch (flag) {
            case 0:
                imgFront.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_zere = 1;
                    bitmap_zero = bitmap;
                    imgFrontRemove.setVisibility(View.VISIBLE);
                } else {
                    state_zere = 0;
                    bitmap_zero = null;
                    imgFrontRemove.setVisibility(View.INVISIBLE);
                }
                break;
            case 1:
                imgIdBack.setImageBitmap(bitmap);
                if (capturePath != null) {
                    state_one = 1;
                    imgIdBackRemove.setVisibility(View.VISIBLE);
                } else {
                    state_one = 0;
                    imgIdBackRemove.setVisibility(View.INVISIBLE);
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


                    }
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    file_zero = tempFile.getAbsolutePath();
                    disPlayImage(file_zero);
                }
                break;
        }
    }


    @OnClick({R.id.imgFront, R.id.imgFrontRemove, R.id.imgIdBack, R.id.imgIdBackRemove, R.id.llPostItem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgFront:
                flag = 0;
                switch (state_zere) {
                    case 0:
                        requestPermission();
//                        DialogManager.changeAvatar(ReplaceCardActivity.this, mOnHanlderResultCallback);
//                        activityReplaceCard.setAlpha((float) 0.1);
//                        mPopupWindow.showAtLocation(imgFront, Gravity.BOTTOM, 0, 0);
//                        BaseUtils.pickPhoto(ReplaceCardActivity.this);
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgFront.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(ReplaceCardActivity.this, file_zero, DisplayUtil.dp2px(ReplaceCardActivity.this, 60), DisplayUtil.dp2px(ReplaceCardActivity.this, 60), location[0], location[1]);

                        break;
                }
                break;
            case R.id.imgFrontRemove:
                flag = 0;
                disPlayImage(null);
                break;
            case R.id.imgIdBack:
                flag = 1;
                switch (state_one) {
                    case 0:
                        requestPermission();
//                        DialogManager.changeAvatar(ReplaceCardActivity.this, mOnHanlderResultCallback);
//                        activityReplaceCard.setAlpha((float) 0.1);
//                        mPopupWindow.showAtLocation(imgFront, Gravity.BOTTOM, 0, 0);
                        break;
                    case 1:
                        int[] location = new int[2];
                        imgIdBack.getLocationOnScreen(location);
                        Util.turnToPhotoDetailActivity(ReplaceCardActivity.this, file_one, DisplayUtil.dp2px(ReplaceCardActivity.this, 60), DisplayUtil.dp2px(ReplaceCardActivity.this, 60), location[0], location[1]);
                        break;
                }

                break;
            case R.id.imgIdBackRemove:
                flag = 1;
                disPlayImage(null);
                break;
            case R.id.llPostItem:
                flag = 1;
                if (!Util.isFastDoubleClick())
                    Util.createDialog(ReplaceCardActivity.this, list, tvPostItem, null);
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
            DialogManager.changeAvatar(ReplaceCardActivity.this, new SuccessNull() {
                @Override
                public void onSuccess() {
                    File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                    fileDir = new File(fileDir, "bqt");
                    if (!fileDir.exists()) fileDir.mkdirs();
                    tempFile = new File(fileDir, "temp_bkfront.jpg");
                    showCamera(ReplaceCardActivity.this, tempFile, REQUEST_CODE_CAMERA);
                }
            }, mOnHanlderResultCallback);
        else
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAMERA);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED&& grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    DialogManager.changeAvatar(ReplaceCardActivity.this, new SuccessNull() {
                        @Override
                        public void onSuccess() {
                            File fileDir = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? getFilesDir() : Environment.getExternalStorageDirectory();
                            fileDir = new File(fileDir, "bqt");
                            if (!fileDir.exists()) fileDir.mkdirs();
                            tempFile = new File(fileDir, "temp_bkfront.jpg");
                            showCamera(ReplaceCardActivity.this, tempFile, REQUEST_CODE_CAMERA);
                        }
                    }, mOnHanlderResultCallback);
                } else {
                    Util.createToast(ReplaceCardActivity.this, "请赋予权限");
                }

                break;
        }
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

                }
                disPlayImage(path);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {

        }
    };

    @OnClick(R.id.tvSubmit)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            /*strIdAddress非必输,其他信息必输,身份证号15,18,x，三个手机号11位*/
            final String strNumber = etNumber.getText().toString();
            final String strName = etName.getText().toString();
            final String strId = etId.getText().toString();
            final String strIdAddress = etIdAddress.getText().toString();
            final String strPhone = etPhone.getText().toString();
            final String strAddress = etAddress.getText().toString();
            final String strGetName = etGetName.getText().toString();
            final String strGetPhone = etGetPhone.getText().toString();
            final String strLast0 = etLast0.getText().toString();
            final String strLast1 = etLast1.getText().toString();
            final String strLast2 = etLast2.getText().toString();

            switch (tvPostItem.getText().toString()) {
                case "顺丰到付":
                    strPosition = "0";
                    break;
                case "充100免费邮寄":
                    strPosition = "1";
                    break;

            }

            if (strNumber.equals("") || strName.equals("") || strId.equals("") || strPhone.equals("") || strLast0.equals("") || strLast1.equals("") || strLast2.equals("") || strAddress.equals("") || strGetName.equals("") || strGetPhone.equals("") || strPosition.equals("") || state_zere == 0) {
                Util.createToast(ReplaceCardActivity.this, "请将信息填写完整");
            } else if (strId.length() != 15 && strId.length() != 18) {
                Util.createToast(ReplaceCardActivity.this, "请填写正确的身份证号");
            } else if (strPhone.length() != 11 || strLast0.length() != 11 || strGetPhone.length() != 11 || strLast1.length() != 11 || strLast2.length() != 11) {
                Util.createToast(ReplaceCardActivity.this, "请填写正确的联系电话");
            } else {
                dialog.getTvTitle().setText("正在上传图片");
                dialog.show();
                HttpPost<PostPictureUpload> httpPost = new HttpPost<>();
                PostPictureUpload postPictureUpload = new PostPictureUpload();
                postPictureUpload.setSession_token(Util.getLocalAdmin(ReplaceCardActivity.this)[0]);
                postPictureUpload.setType("3");
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postPictureUpload);
                Photo photo = new Photo();
                photo.setPhoto1(BitmapUtil.bitmapToBase64(BitmapUtil.compressBitmap(bitmap_zero)));
                httpPost.setPhoto(photo);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPictureUpload) + BaseCom.APP_PWD));
                Log.d("aaa", gson.toJson(httpPost));
                new OtherHttp().pictureUpload(new OtherRequest().pictureUpload(ReplaceCardActivity.this, dialog, new SuccessValue<RequestPictureUpload>() {
                    @Override
                    public void OnSuccess(RequestPictureUpload value) {
                        Log.d("aaa", value.getPhoto1());
                        dialog.getTvTitle().setText("正在提交");
                        dialog.show();
                        HttpPost<PostRepair> httpPost = new HttpPost<>();
                        PostRepair postRepair = new PostRepair();
                        postRepair.setSession_token(Util.getLocalAdmin(ReplaceCardActivity.this)[0]);
                        postRepair.setNumber(strNumber);
                        postRepair.setName(strName);
                        postRepair.setCardId(strId);
                        postRepair.setPhoto(value.getPhoto1());
                        postRepair.setTel(strPhone);
                        postRepair.setAddress(strIdAddress);
                        postRepair.setReceiveName(strGetName);
                        postRepair.setReceiveTel(strGetPhone);
                        postRepair.setMailingAddress(strAddress);
                        postRepair.setMailMethod(strPosition);
                        postRepair.setNumOne(strLast0);
                        postRepair.setNumTwo(strLast1);
                        postRepair.setNumThree(strLast2);
                        postRepair.setNumFour(strLast0);
                        postRepair.setNumFive(strLast0);
                        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                        httpPost.setParameter(postRepair);
                        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postRepair) + BaseCom.APP_PWD));
                        new CardHttp().repair(CardRequest.repair(new SuccessValue<HttpRequest<RequestRepair>>() {
                            @Override
                            public void OnSuccess(HttpRequest<RequestRepair> value) {
                                Util.createToast(ReplaceCardActivity.this, value.getMes());
                                if (value.getCode() == BaseCom.NORMAL) {
                                    AppManager.getAppManager().finishActivity();
                                } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {
                                    Util.gotoActy(ReplaceCardActivity.this, LoginActivity.class);
                                }
                            }
                        }, dialog), httpPost);
                    }
                }), httpPost);
            }
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
        } else {
            if (etNumber.getText().toString().length() != 11) {
                Util.createToast(ReplaceCardActivity.this, "请输入正确的手机号码");
            } else {
                HttpPost<PostNumberSegment> httpPost = new HttpPost<>();
                PostNumberSegment postNumberSegment = new PostNumberSegment();
                postNumberSegment.setSession_token(Util.getLocalAdmin(ReplaceCardActivity.this)[0]);
                postNumberSegment.setTel(etNumber.getText().toString());
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postNumberSegment);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNumberSegment) + BaseCom.APP_PWD));
                new CardHttp().numberSegment(CardRequest.numberSegment(new SuccessValue<HttpRequest<RequestNumberSegment>>() {
                    @Override
                    public void OnSuccess(HttpRequest<RequestNumberSegment> value) {
                        Util.createToast(ReplaceCardActivity.this, value.getMes());
                        if (value.getCode() == BaseCom.NORMAL) {
                        } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {
                            etNumber.setText("");
                            Util.gotoActy(ReplaceCardActivity.this, LoginActivity.class);
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

                    }

                    Uri uri = Uri.fromFile(out);
                    imageCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    imageCaptureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(imageCaptureIntent, 1);


                    mPopupWindow.dismiss();
                    break;
                case R.id.tvBook:
                    BaseUtils.pickPhoto(ReplaceCardActivity.this);


                    mPopupWindow.dismiss();
                    break;
                case R.id.tvCancel:
                    mPopupWindow.dismiss();
                    break;
            }
        }
    }
}
