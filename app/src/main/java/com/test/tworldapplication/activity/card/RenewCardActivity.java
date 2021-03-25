package com.test.tworldapplication.activity.card;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostCheck;
import com.test.tworldapplication.entity.PostJudgeIsLiang;
import com.test.tworldapplication.entity.PostOpenPower;
import com.test.tworldapplication.entity.PostSelectionCheck;
import com.test.tworldapplication.entity.RequestCheck;
import com.test.tworldapplication.entity.RequestJudgeIsLiang;
import com.test.tworldapplication.entity.RequestOpenPower;
import com.test.tworldapplication.entity.RequestSelectionCheck;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RenewCardActivity extends BaseActivity {

    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.ll_inumber)
    RelativeLayout llInumber;
    @BindView(R.id.etPUK)
    EditText etPUK;
    @BindView(R.id.ll_PUK)
    RelativeLayout llPUK;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.ll_renew)
    LinearLayout llRenew;
    String strNumber = "";
    @BindView(R.id.v_puk)
    View vPuk;
    int post = 0;
    @BindView(R.id.tvToast)
    TextView tvToast;
    /*puk码验证成功之后向套餐选择页面传递数据*/

    String from = "";
    String face="";
    String type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_card);
        ButterKnife.bind(this);
        setBackGroundTitle("号码验证", true);
        dialog.getTvTitle().setText("正在验证");

        from=getIntent().getStringExtra( "from" );
        face=getIntent().getStringExtra( "face" );
        type=getIntent().getStringExtra( "type" );
        post=getIntent().getIntExtra( "post",-1);
        if(post==0){
            llPUK.setVisibility( View.GONE );
        }else if(post==2){
            llPUK.setVisibility( View.GONE );
        }else if(post==1){
            llPUK.setVisibility( View.VISIBLE );
        }


        switch (from){
            case "0":

                break;
            case "1":

                break;
        }

    }


    @OnClick(R.id.tvNext)
    public void onClick() {
        /*成卡开户之PUK码验证*/
        switch (post) {
            case 0:
                strNumber = etNumber.getText().toString();
                if (strNumber.equals("") || strNumber.length() != 11) {
                    Toast.makeText(RenewCardActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
                } else {
                    HttpPost<PostJudgeIsLiang> httpPost = new HttpPost<>();
                    PostJudgeIsLiang postJudgeIsLiang = new PostJudgeIsLiang();
                    postJudgeIsLiang.setSession_token(Util.getLocalAdmin(RenewCardActivity.this)[0]);
                    postJudgeIsLiang.setTel(strNumber);
                    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                    httpPost.setParameter(postJudgeIsLiang);
                    httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postJudgeIsLiang) + BaseCom.APP_PWD));
                    new CardHttp().judgeIsLiang(CardRequest.judgeIsLiang(new SuccessValue<HttpRequest<RequestJudgeIsLiang>>() {
                        @Override
                        public void OnSuccess(HttpRequest<RequestJudgeIsLiang> value) {
                            Toast.makeText(RenewCardActivity.this, value.getMes(), Toast.LENGTH_SHORT).show();
                            if (value.getCode() != 10000) {

                            } else {
                                if (value.getData().getIsLiang().equals("Y")) {
                                    vPuk.setVisibility(View.VISIBLE);
                                    llPUK.setVisibility(View.VISIBLE);
                                    post = 1;
                                    tvToast.setVisibility(View.VISIBLE);
                                } else {
                                    RequestJudgeIsLiang requestJudgeIsLiang = value.getData();
                                    RequestCheck requestCheck = new RequestCheck();
                                    requestCheck.setOrg_number_poolsId(requestJudgeIsLiang.getOrg_number_poolsId());
                                    requestCheck.setPackages(requestJudgeIsLiang.getPackages());
                                    requestCheck.setOperatorName(requestJudgeIsLiang.getOperatorName());
                                    requestCheck.setNumberStatus(requestJudgeIsLiang.getNumberStatus());
                                    requestCheck.setCityName(requestJudgeIsLiang.getCityName());
                                    requestCheck.setPrestore(requestJudgeIsLiang.getPrestore());
                                    requestCheck.setSimICCID(requestJudgeIsLiang.getSimICCID());
                                    requestCheck.setSimId(requestJudgeIsLiang.getSimId());
                                    requestCheck.setNumber(requestJudgeIsLiang.getNumber());
                                    /*跳转页面*/
                                    Intent intent = new Intent(RenewCardActivity.this, PackageSelectActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("check", requestCheck);
                                    intent.putExtra( "face",face );
                                    intent.putExtra( "from",from );
                                    intent.putExtra( "type",type );
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                }
                            }
                        }
                    }), httpPost);

                }
                break;
            case 1:
                strNumber = etNumber.getText().toString();
                String strPUK = etPUK.getText().toString();
                if (strNumber.length() != 11) {
                    Util.createToast(RenewCardActivity.this, "请输入正确的手机号码!");
                }
                else if (strPUK.equals("")) {
                    Util.createToast(RenewCardActivity.this, "PUK码不能为空!");
                }
                else {
                    HttpPost<PostCheck> httpPost = new HttpPost<>();
                    PostCheck postCheck = new PostCheck();
                    postCheck.setSession_token(Util.getLocalAdmin(RenewCardActivity.this)[0]);
                    postCheck.setTel(strNumber);
                    postCheck.setPuk(strPUK);
                    httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                    httpPost.setParameter(postCheck);
                    httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCheck) + BaseCom.APP_PWD));
                    new CardHttp().check(CardRequest.check(new SuccessValue<HttpRequest<RequestCheck>>() {
                        @Override
                        public void OnSuccess(HttpRequest<RequestCheck> value) {
                            Util.createToast(RenewCardActivity.this, value.getMes());
                            if (value.getCode() == BaseCom.NORMAL) {

                                Intent intent = new Intent(RenewCardActivity.this, PackageSelectActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("check", value.getData());
                                intent.putExtra( "from",from );
                                intent.putExtra( "type",type );
                                intent.putExtra( "face",face );
                                intent.putExtras(bundle);
                                startActivity(intent);
                                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                            } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT){
                                Util.gotoActy(RenewCardActivity.this, LoginActivity.class);}
                        }
                    }, dialog), httpPost);
                }
                break;
            case 2:
                strNumber = etNumber.getText().toString();
//                if (!checkPhone( strNumber )) {
//                    Toast.makeText(RenewCardActivity.this, "请输入正确的手机号!", Toast.LENGTH_SHORT).show();
//                } else {
                    HttpPost<PostSelectionCheck> httpPost = new HttpPost<>();
                    PostSelectionCheck postSelectionCheck = new PostSelectionCheck();
                    postSelectionCheck.setSession_token( Util.getLocalAdmin( RenewCardActivity.this )[0] );
                    postSelectionCheck.setNumber( strNumber );
                    httpPost.setApp_key( Util.GetMD5Code( BaseCom.APP_KEY ) );
                    httpPost.setParameter( postSelectionCheck );
                    httpPost.setApp_sign( Util.GetMD5Code( BaseCom.APP_PWD + gson.toJson( postSelectionCheck ) + BaseCom.APP_PWD ) );
                    new CardHttp().checkSelect( CardRequest.checkSelect(this, new SuccessValue<RequestSelectionCheck>() {
                        @Override
                        public void OnSuccess(RequestSelectionCheck value) {
                            RequestSelectionCheck requestSelectionCheck = value;
                            RequestCheck requestCheck = new RequestCheck();
                            requestCheck.setCityName( requestSelectionCheck.getCityName() );
                            requestCheck.setOperatorName( requestSelectionCheck.getOperatorName() );
                            requestCheck.setNumberStatus( requestSelectionCheck.getNumberStatus() );
                            requestCheck.setPrestore( requestSelectionCheck.getPrestore() );
                            requestCheck.setPackages( requestSelectionCheck.getPackages() );
                            requestCheck.setNumber( strNumber );
                            /*跳转页面*/
                            Intent intent = new Intent( RenewCardActivity.this, PackageSelectActivity.class );
                            Bundle bundle = new Bundle();
                            bundle.putSerializable( "check", requestCheck );
                            intent.putExtra( "face", "3" );
                            intent.putExtra( "from", from );
                            intent.putExtra( "type", type );
                            intent.putExtras( bundle );
                            startActivity( intent );
                            overridePendingTransition( R.anim.zoomin, R.anim.zoomout );
                        }
                    }),httpPost);
//                        @Override
//                        public void OnSuccess(HttpRequest<RequestSelectionCheck> value) {
//                            RequestSelectionCheck requestSelectionCheck = value.getData();
//                            RequestCheck requestCheck = new RequestCheck();
//                            requestCheck.setCityName( requestSelectionCheck.getCityName() );
//                            requestCheck.setOperatorName( requestSelectionCheck.getOperatorName() );
//                            requestCheck.setNumberStatus( requestSelectionCheck.getNumberStatus() );
//                            requestCheck.setPrestore( requestSelectionCheck.getPrestore() );
//                            requestCheck.setPackages( requestSelectionCheck.getPackages() );
//                            requestCheck.setNumber( strNumber );
//                            /*跳转页面*/
//                            Intent intent = new Intent( RenewCardActivity.this, PackageSelectActivity.class );
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable( "check", requestCheck );
//                            intent.putExtra( "face", "3" );
//                            intent.putExtra( "from", from );
//                            intent.putExtra( "type", type );
//                            intent.putExtras( bundle );
//                            startActivity( intent );
//                            overridePendingTransition( R.anim.zoomin, R.anim.zoomout );
//                        }
//                    }, dialog ), httpPost );
//                }

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