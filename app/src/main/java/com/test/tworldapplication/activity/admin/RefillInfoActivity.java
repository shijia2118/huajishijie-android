package com.test.tworldapplication.activity.admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostReSubmit;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.utils.GetJsonDataUtil;
import com.test.tworldapplication.utils.JsonBean;
import com.test.tworldapplication.utils.Util;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class RefillInfoActivity extends BaseActivity {

    @BindView(R.id.user_name)
    EditText etName;
    @BindView( R.id.user_numb )
    EditText etNumb;
    @BindView( R.id.user_id_card )
    EditText etId;
    @BindView( R.id.user_e_mail )
    EditText etEmail;
    @BindView( R.id.user_provience )
    TextView tvProvince;
    @BindView( R.id.user_address )
    EditText etAddress;


    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_refill_info );
        ButterKnife.bind( this );
        setBackGroundTitle( "补登记资料",true );

        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

    }

    @OnClick({R.id.submit,R.id.province_layout,R.id.user_provience,R.id.ll_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.submit:
                if(!Util.isFastDoubleClick()){
                    String mName=etName.getText().toString();
                    String mNumb=etNumb.getText().toString();
                    String mId=etId.getText().toString();
                    String mEmail=etEmail.getText().toString();
                    String mProvince=tvProvince.getText().toString();
                    String mAddress=etAddress.getText().toString();

                    if(etName.getText().toString().equals( "" )){
                        Util.createToast( RefillInfoActivity.this,"请输入姓名" );
                    }else if(etNumb.getText().toString().equals("") || etNumb.getText().toString().length() != 11){
                            Util.createToast( RefillInfoActivity.this,"请输入正确的手机号" );
                    }else if(!isIdcard(etId.getText().toString())){
                        Util.createToast( RefillInfoActivity.this,"请输入正确的身份证号码" );
                    }else if(!mEmail.equals( "" )&&!isEmail( mEmail )){
                        Util.createToast( RefillInfoActivity.this,"请输入正确的邮箱" );
                    }else if(tvProvince.getText().toString().equals( "请输入" )){
                        Util.createToast( RefillInfoActivity.this,"请选择省市" );
                    }else if(etAddress.getText().toString().equals( "" )){
                        Util.createToast( RefillInfoActivity.this,"请输入详细地址" );
                    } else{
                        HttpPost<PostReSubmit>httpPost=new HttpPost<>();
                        PostReSubmit postReSubmit=new PostReSubmit();
                        postReSubmit.setContact( mName );
                        postReSubmit.setTel( mNumb );
                        postReSubmit.setCardId( mId );
                        postReSubmit.setEmail( mEmail );
                        postReSubmit.setAddress( mProvince+mAddress );

                        postReSubmit.setSession_token(Util.getLocalAdmin(RefillInfoActivity.this)[0]);
                        httpPost.setApp_key(Util.GetMD5Code( BaseCom.APP_KEY));
                        httpPost.setParameter( postReSubmit );
                        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postReSubmit) + BaseCom.APP_PWD));
                        Log.d("aaa", gson.toJson(httpPost));

                        new CardHttp().writeIn( CardRequest.reSubmit( RefillInfoActivity.this, new SuccessNull() {
                            @Override
                            public void onSuccess() {
                                gotoActy( MainNewActivity.class );
                                AppManager.getAppManager().finishActivity();

                                SharedPreferences sharedPreferences =getSharedPreferences( "mySP",Context.MODE_PRIVATE );
                                SharedPreferences.Editor editor = sharedPreferences.edit(); //编辑文件
                                editor.putString( "YorN", "0" );
                                editor.commit();
                            }
                        } ),httpPost );


//                        new CardHttp().writeIn( httpPost, new Subscriber<HttpRequest<String>>() {
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onNext(HttpRequest<String> stringHttpRequest) {
//                                gotoActy( MainActivity.class );
//                                AppManager.getAppManager().finishActivity();
//                            }
//                        } );

//                        gotoActy( MainActivity.class );
//                        AppManager.getAppManager().finishActivity();
                    }
                }
                break;
            case R.id.province_layout:
//                showDialog1();
                if (isLoaded){
                    ShowPickerView();
                }else {
//                    Toast.makeText(getActivity(),"Please waiting until the data is parsed",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.user_provience:
                if (isLoaded){
                    ShowPickerView();
                }else {
//                    Toast.makeText(getActivity(),"Please waiting until the data is parsed",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_back:
                AppManager.getAppManager().finishActivity();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread==null){//如果已创建就不再重新创建子线程了

//            Toast.makeText(JsonDataActivity.this,"Begin Parse Data",Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
//          Toast.makeText(JsonDataActivity.this,"Parse Succeed",Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
//          Toast.makeText(JsonDataActivity.this,"Parse Failed",Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    private void ShowPickerView() {// 弹出选择器

        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2);

//        Toast.makeText(getActivity(), tx, Toast.LENGTH_SHORT).show();
                tvProvince.setText(tx );
//        presenter.chage(RegisterPresenter.TYPE_AREA, tx);

            }
        }).setTitleText("城市选择")
                .setDividerColor( Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
//        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器
        pvOptions.show();
    }


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this,"province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i=0;i<jsonBean.size();i++){//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c=0; c<jsonBean.get(i).getCityList().size(); c++){//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        ||jsonBean.get(i).getCityList().get(c).getArea().size()==0) {
                    City_AreaList.add("");
                }else {

                    for (int d=0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }


    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }



    protected boolean checkPhone(String str) {
        String pattern = "0?[0-9]{11}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        String pattern= "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher( email );
        return m.matches();
    }

    public static boolean isIdcard(String idcard) {
        String pattern=  "^\\d{15}$|^\\d{17}[0-9Xx]$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher( idcard );
        return m.matches();
    }

}
