package com.test.tworldapplication.activity.card;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.AgentsLiangNumber;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.City;
import com.test.tworldapplication.entity.CutePhone;
import com.test.tworldapplication.entity.DailiAgentsLiangNumber;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LiangAgent;
import com.test.tworldapplication.entity.LiangRule;
import com.test.tworldapplication.entity.PostAgentsLiangNumber;
import com.test.tworldapplication.entity.Province;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class DailiCuteDetailActivity extends BaseActivity {
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvPreStore)
    TextView tvPreStore;
    @BindView(R.id.tvLess)
    TextView tvLess;
    @BindView(R.id.tvDaili)
    TextView tvDaili;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    private LiangAgent liangAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daili_cute_detail);
        setBackGroundTitle("号码详情", true);
        ButterKnife.bind(this);
        liangAgent = (LiangAgent) getIntent().getSerializableExtra("data");
        getData();
//        cutePhone = new CutePhone("12345678900", "浙江省杭州市", "代理商", "100", "200", "12312345643");
//        initView();
    }

    private void getData() {
        Util.createToast(DailiCuteDetailActivity.this, "正在查询,请稍后");
        PostAgentsLiangNumber postAgentsLiangNumber = new PostAgentsLiangNumber();
        postAgentsLiangNumber.setSession_token(Util.getLocalAdmin(DailiCuteDetailActivity.this)[0]);
        postAgentsLiangNumber.setNumber(liangAgent.getNumber());
        HttpPost httpPost = new HttpPost();
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postAgentsLiangNumber);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postAgentsLiangNumber) + BaseCom.APP_PWD));

        new CardHttp().getAgentsLiangNumber(httpPost, new Subscriber<HttpRequest<DailiAgentsLiangNumber>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<DailiAgentsLiangNumber> agentsLiangNumberHttpRequest) {
                Util.createToast(DailiCuteDetailActivity.this, agentsLiangNumberHttpRequest.getMes());
                initView(agentsLiangNumberHttpRequest.getData());
            }
        });
    }

    private void initView(DailiAgentsLiangNumber agentsLiangNumber) {
        tvNumber.setText(agentsLiangNumber.getNumber());

        tvAddress.setText(agentsLiangNumber.getProvinceName() + agentsLiangNumber.getCityName());
        tvPreStore.setText(agentsLiangNumber.getPrestore() + "元");
        tvDaili.setText(agentsLiangNumber.getUserName());
        tvLess.setText(agentsLiangNumber.getMinConsumption() + "元");
        tvPhone.setText(agentsLiangNumber.getUserTel());
    }
}
