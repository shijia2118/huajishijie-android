package com.test.tworldapplication.activity.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostAdmin;
import com.test.tworldapplication.entity.PostUpdatePersonalInfo;
import com.test.tworldapplication.entity.RequestAdmin;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
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

public class PersonInformationActivity extends BaseActivity implements SuccessValue<RequestAdmin> {

    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPerson)
    TextView tvPerson;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.etMail)
    TextView etMail;
    @BindView(R.id.tvChannel)
    TextView tvChannel;
    @BindView(R.id.tvSuperiorName)
    TextView tvSuperiorName;
    @BindView(R.id.tvSuperiorPhone)
    TextView tvSuperiorPhone;
    @BindView(R.id.tvSuperiorReferral)
    TextView tvSuperiorReferral;
    @BindView(R.id.tvSelfReferral)
    TextView tvSelfReferral;
    @BindView(R.id.tvIdAddress)
    TextView tvIdAddress;
    @BindView(R.id.user_address)
    TextView tvUserAddress;
    @BindView(R.id.llSuper)
    LinearLayout llSuper;





    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;

    //    private TextView tv_juzhu;
//    tv_juzhu = findViewById();
    private String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_information);
        ButterKnife.bind(this);
        setBackGroundTitle("个人信息", true);
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        etMail.setFocusable(false);
//        if (Util.isLog(PersonInformationActivity.this)) {
        HttpPost<PostAdmin> httpPost = new HttpPost<>();
        PostAdmin postAdmin = new PostAdmin();
        postAdmin.setSession_token(Util.getLocalAdmin(PersonInformationActivity.this)[0]);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postAdmin);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postAdmin) + BaseCom.APP_PWD));
        new AdminHttp().getAdmin(AdminRequest.getAdmin(PersonInformationActivity.this, this), httpPost);
    }

//    }

    @OnClick({R.id.tvSave, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvSave:
                break;
            case R.id.submit:
//                if(!Util.isFastDoubleClick()){

//                }
                break;


        }
    }

    @Override
    public void OnSuccess(RequestAdmin value) {
        tvName.setText(value.getUsername());
        tvPerson.setText(value.getContact());
        tvPhone.setText(value.getTel());
        if (value.getEmail() != null) {
            etMail.setText(value.getEmail());
        }
        Log.d("aaa", BaseCom.login.getGrade());
        if (BaseCom.login.getGrade().equals("lev0")) {
            llSuper.setVisibility(View.GONE);
        }
        tvChannel.setText(value.getChannelName());
        tvSuperiorName.setText(value.getSupUserName());
        tvSuperiorPhone.setText(value.getSupTel());
        tvSuperiorReferral.setText(value.getSupRecomdCode());
        tvSelfReferral.setText(value.getRecomdCode());
        tvIdAddress.setText(value.getAddress());
        tvUserAddress.setText(value.getWorkAddress());

    }




    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了

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


    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
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
//            options3Items.add(Province_AreaList);
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

    public static boolean isEmail(String email) {
        String pattern = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }


}
