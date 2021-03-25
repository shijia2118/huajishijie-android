package com.test.tworldapplication.activity.card;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.AgentsLiangNumber;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostMode;
import com.test.tworldapplication.entity.RequestImsi;
import com.test.tworldapplication.entity.RequestLiang;
import com.test.tworldapplication.entity.RequestMode;
import com.test.tworldapplication.entity.RequestPreNumber;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.http.RequestLiangPay;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HuajiSelectActivity extends BaseActivity {
    private String from = "";
    int flag = 0;
    String code = "";
    AgentsLiangNumber agentsLiangNumber;
    RequestPreNumber requestPreNumber;
    RequestImsi requestImsi = null;
    RequestLiangPay requestLiangPay;

    @BindView( R.id.normal )
    RelativeLayout rlNormal;
    @BindView( R.id.face_reveal )
    RelativeLayout rlFace;

    private int modes=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huaji_select);
        ButterKnife.bind(this);
        setBackGroundTitle("开户方式选择", true);
        from = getIntent().getStringExtra("from");
        switch (from) {
            case "2":
                requestLiangPay = (RequestLiangPay) getIntent().getSerializableExtra("requestLiangPay");
                requestImsi = (RequestImsi) getIntent().getSerializableExtra("requestImsi");
                agentsLiangNumber = (AgentsLiangNumber) getIntent().getSerializableExtra("agentsLiangNumber");
                break;
            case "3":
                requestPreNumber = (RequestPreNumber) getIntent().getSerializableExtra("data");
                break;
        }


        SharedPreferences sharedPreferences = getSharedPreferences( "mySP", Context.MODE_PRIVATE );
        int pattern = sharedPreferences.getInt( "pattern", -1 );
        modes=sharedPreferences.getInt( "modes",-1 );
        if(pattern==1){
            rlNormal.setVisibility( View.VISIBLE );
            rlFace.setVisibility( View.GONE );
        }else if(pattern==2){
            rlNormal.setVisibility( View.GONE );
            rlFace.setVisibility( View.VISIBLE );
        }else if(pattern==3){
            rlNormal.setVisibility( View.VISIBLE );
            rlFace.setVisibility( View.VISIBLE );
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.normal,R.id.face_reveal})
    public void onClick(View view) {
        Intent intent = new Intent(HuajiSelectActivity.this, MessageCollectionNewActivity.class);
        switch (from) {
            case "2":
                intent.putExtra("requestLiangPay", requestLiangPay);
                intent.putExtra("requestImsi", requestImsi);
                intent.putExtra("agentsLiangNumber", agentsLiangNumber);
                intent.putExtra("from", "2");
                break;
            case "3":
                intent.putExtra("data", requestPreNumber);
                intent.putExtra("from", "3");
                break;
        }

        switch (view.getId()) {
            case R.id.normal:
                if (modes==1){
                    intent.putExtra("type", "1");
                    intent.putExtra( "face","0");
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    break;
                }else if(modes==2){

                    intent.putExtra("type", "0");
                    intent.putExtra( "face","0");
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }else if(modes==3) {
                    intent.putExtra("type", "1");
                    intent.putExtra( "face","0");
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }

                break;

            case R.id.face_reveal:
                if (modes==1){
                     intent.putExtra("type", "1");
                     intent.putExtra( "face","1");
                     startActivity(intent);
                     overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }else if(modes==2){
                    intent.putExtra("type", "0");
                    intent.putExtra( "face","1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }else if(modes==3) {
                    intent.putExtra("type", "1");
                    intent.putExtra( "face","1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }

                break;
        }
//        startActivity(intent);
//        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
