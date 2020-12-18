package com.test.tworldapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.activity.HbcjActivity;
import com.test.tworldapplication.adapter.CjRecordAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.CjRecord;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LotteryRecord;
import com.test.tworldapplication.entity.PostLuckNumber;
import com.test.tworldapplication.entity.PostWinningOrder;
import com.test.tworldapplication.entity.RequestWinningOrder;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

public class CjRecordActivity extends BaseActivity {
    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvToast)
    TextView tvToast;
    CjRecordAdapter adapter;
    List<LotteryRecord> list = new ArrayList<>();
    int page = 1;
    int per_page = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cj_record);
        ButterKnife.bind(this);
        tvTitle.setText("抽奖记录");
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
//                refresh = 1;
                getData();
            }

            @Override
            public void onLoadmore() {
                page++;
//                refresh = 2;
                getData();
            }
        });
        springView.setHeader(new DefaultHeader(CjRecordActivity.this));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultFooter(CjRecordActivity.this));
        adapter = new CjRecordAdapter(this, list);
        contentView.setAdapter(adapter);
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(CjRecordActivity.this, CjDetailActivity.class).putExtra("data", list.get(i)));
            }
        });

        getData();


    }

    private void getData() {
        HttpPost<PostWinningOrder> httpPost = new HttpPost<PostWinningOrder>();
        PostWinningOrder postLogin = new PostWinningOrder();
        postLogin.setSession_token(Util.getLocalAdmin(CjRecordActivity.this)[0]);
        postLogin.setPage(page + "");
        postLogin.setLinage(per_page + "");
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postLogin) + BaseCom.APP_PWD));
        httpPost.setParameter(postLogin);
        new AdminHttp().getWinningOrders(new Subscriber<HttpRequest<RequestWinningOrder>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                springView.onFinishFreshAndLoad();

            }

            @Override
            public void onNext(HttpRequest<RequestWinningOrder> requestWinningOrderHttpRequest) {
                springView.onFinishFreshAndLoad();
                if (requestWinningOrderHttpRequest.getCode() == BaseCom.NORMAL) {
                    if (page == 1) {
                        list.clear();
                    }
                    tvToast.setText("温馨提示:您已使用过" + requestWinningOrderHttpRequest.getData().getCount() + "次抽奖机会");
                    list.addAll(requestWinningOrderHttpRequest.getData().getLotteryRecord());
                    adapter.notifyDataSetChanged();
                }
            }
        }, httpPost);


    }


    @OnClick(R.id.imgBack)
    public void onViewClicked() {
        AppManager.getAppManager().finishActivity();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
