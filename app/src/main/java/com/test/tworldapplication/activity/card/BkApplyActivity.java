package com.test.tworldapplication.activity.card;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostBkApply;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class BkApplyActivity extends BaseActivity {

    @BindView( R.id.reciever_name )
    EditText etName;
    @BindView( R.id.phone )
    EditText etPhone;
    @BindView( R.id.address )
    EditText etAddresss;
    @BindView( R.id.mount )
    EditText etMount;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_baika_apply );
        ButterKnife.bind(this);
        setBackGroundTitle( "白卡申请",true );
    }


    @OnClick({R.id.submit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.submit:
                if(!Util.isFastDoubleClick()){
                    final String mName=etName.getText().toString();
                    final String mPhone=etPhone.getText().toString();
                    final String mAddress=etAddresss.getText().toString();
                    final String mMount=etMount.getText().toString();

                    if(etName.getText().toString().equals( "" )){
                        Util.createToast( BkApplyActivity.this,"请输入姓名" );
                    }else if(!checkPhone(etPhone.getText().toString())){
                        Util.createToast( BkApplyActivity.this,"请输入正确的手机号码!" );
                    }else if(etAddresss.getText().toString().equals( "" )){
                        Util.createToast( BkApplyActivity.this,"请输入地址" );
                    }else if(etMount.getText().toString().equals( "" )){
                        Util.createToast( BkApplyActivity.this,"请输入申请数量" );
                    }else{
                        HttpPost<PostBkApply>httpPost=new HttpPost<>();
                        PostBkApply postBkApply=new PostBkApply();
                        postBkApply.setName( mName );
                        postBkApply.setTel( mPhone );
                        postBkApply.setDeliveryAddress( mAddress );
                        postBkApply.setApplySum( mMount );

                        postBkApply.setSession_token(Util.getLocalAdmin(BkApplyActivity.this)[0]);
                        httpPost.setApp_key( Util.encode( BaseCom.APP_KEY ) );
                        httpPost.setParameter( postBkApply );
                        httpPost.setApp_sign( Util.encode( BaseCom.APP_PWD+gson.toJson( postBkApply )+BaseCom.APP_PWD ));
                        Log.d("aaa", gson.toJson(httpPost));

                        new CardHttp().BkApply(CardRequest.bkApply(new SuccessValue<HttpRequest<RequestOpenPower>>() {
                            @Override
                            public void OnSuccess(HttpRequest<RequestOpenPower> value) {
                                Util.createToast( BkApplyActivity.this,value.getMes() );
                                if (value.getCode() == BaseCom.NORMAL) {
                                    AppManager.getAppManager().finishActivity();
                                    AppManager.getAppManager().finishActivity(QdsCuteSelectActivity.class);

                                } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {
                                    Util.gotoActy(BkApplyActivity.this, LoginActivity.class);
                                }
                            }
                        }),httpPost );

                    }
                }
                break;
        }
    }


    protected boolean checkPhone(String str) {
        String pattern = "0?[0-9]{11}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        return m.matches();
    }
}
