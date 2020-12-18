package com.test.tworldapplication.activity.order;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.LiangAgents;
import com.test.tworldapplication.entity.RequestBkSelect;
import com.test.tworldapplication.entity.PostBkSelect;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BkApplyOrderActivity extends BaseActivity implements View.OnClickListener,SuccessValue<RequestBkSelect>{

    @BindView(R.id.order_number)
    TextView mNumber;
    @BindView( R.id.apply_time )
    TextView mApplyTime;
    @BindView( R.id.examine_time )
    TextView mExamineTime;
    @BindView( R.id.apply_status )
    TextView mApplyStatus;
    @BindView( R.id.apply_mount )
    TextView mApplyMount;
    @BindView( R.id.approved_mount )
    TextView mApprovedMount;
    @BindView( R.id.user_name )
    TextView mName;
    @BindView( R.id.phone )
    TextView mPhone;
    @BindView( R.id.address )
    TextView mAddress;
    @BindView( R.id.reason_layout )
    LinearLayout resonLayout;
    @BindView( R.id.reason )
    TextView tvReson;

    private LiangAgents liangAgents;
    private String mReason;

    RequestBkSelect bkSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_baika_apply_order );
        ButterKnife.bind( this );
        setBackGroundTitle( "白卡申请详情",true );

        liangAgents=(LiangAgents)getIntent().getSerializableExtra( "data" );
        dialog.getTvTitle().setText("正在获取数据");


//        liangAgents=(LiangAgents)getIntent().getSerializableExtra( "data" );
//        mNumber.setText( liangAgents.getOrderNumber() );
        getData();
    }

    private void getData() {
        Toast.makeText(BkApplyOrderActivity.this, "正在查询数据,请稍后", Toast.LENGTH_SHORT).show();

        HttpPost<PostBkSelect> httpPost=new HttpPost<>();
        PostBkSelect postBkSelect=new PostBkSelect();
        postBkSelect.setSession_token(Util.getLocalAdmin(BkApplyOrderActivity.this)[0]);
        postBkSelect.setId( liangAgents.getId() );
        httpPost.setApp_key(Util.GetMD5Code( BaseCom.APP_KEY));
        httpPost.setParameter( postBkSelect );
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postBkSelect) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().getBkApplyDetail( OrderRequest.getBkApplyDetail( BkApplyOrderActivity.this,this ),httpPost);

    }


    @Override
    protected void onResume(){
        super.onResume();
//        HttpPost<PostBkSelect> httpPost=new HttpPost<>();
//        PostBkSelect postBkSelect=new PostBkSelect();
//        postBkSelect.setSession_token(Util.getLocalAdmin(BkApplyOrderActivity.this)[0]);
//        postBkSelect.setSession_token( Util.getLocalAdmin( BkApplyOrderActivity.this )[0]);
//        httpPost.setApp_key(Util.GetMD5Code( BaseCom.APP_KEY));
//        httpPost.setParameter( postBkSelect );
//        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postBkSelect) + BaseCom.APP_PWD));
//        new OrderHttp().getBkApplyDetail( OrderRequest.getBkApplyDetail( BkApplyOrderActivity.this,this ),httpPost);

    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void OnSuccess(RequestBkSelect value) {
        mNumber.setText( value.getOrderNumber() );
        mApplyTime.setText( value.getCreateDate() );
        mExamineTime.setText( value.getAuditTime() );
        mApplyStatus.setText( value.getAuditStatusName() );
        mName.setText( value.getName() );
        mPhone.setText( value.getTel() );
        mAddress.setText( value.getDeliveryAddress() );
        mApplyMount.setText( value.getApplySum()+"");
        mApprovedMount.setText( value.getActualSum()+"" );


        if(value.getAuditStatusName().contains( "不" )) {
            mReason = value.getNotAuditReasons();
//            if (mReason.equals( "" )) {
//                resonLayout.setVisibility( View.GONE );
//            } else {
            resonLayout.setVisibility( View.VISIBLE );
            tvReson.setText( mReason );
//            }
        }else {
            resonLayout.setVisibility( View.GONE );
        }
    }
}

