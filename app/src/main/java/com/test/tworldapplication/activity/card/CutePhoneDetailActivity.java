package com.test.tworldapplication.activity.card;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

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
import com.test.tworldapplication.entity.PostAgentsLiangNumber;
import com.test.tworldapplication.entity.Province;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class CutePhoneDetailActivity extends BaseActivity {
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvPreStore)
    TextView tvPreStore;
    @BindView(R.id.tvLess)
    TextView tvLess;
    @BindView(R.id.tvTime)
    TextView tvTime;
    LiangAgent liangAgent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cute_phone_detail);
        setBackGroundTitle("靓号详情", true);
        ButterKnife.bind(this);
        liangAgent = (LiangAgent) getIntent().getSerializableExtra("data");
        getData();
    }

    private void getData() {
        Toast.makeText(CutePhoneDetailActivity.this, "正在查询,请稍后", Toast.LENGTH_SHORT).show();
        PostAgentsLiangNumber postAgentsLiangNumber = new PostAgentsLiangNumber();
        postAgentsLiangNumber.setSession_token(Util.getLocalAdmin(CutePhoneDetailActivity.this)[0]);
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
                Toast.makeText(CutePhoneDetailActivity.this, agentsLiangNumberHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
                initView(agentsLiangNumberHttpRequest.getData());
            }
        });
    }

    private void initView(DailiAgentsLiangNumber agentsLiangNumber) {
        tvNumber.setText(agentsLiangNumber.getNumber());

        tvAddress.setText(agentsLiangNumber.getProvinceName() + agentsLiangNumber.getCityName());
        tvPreStore.setText(agentsLiangNumber.getPrestore() + "元");
        String status = "";
        switch (agentsLiangNumber.getLiangStatus()) {
            case 0:
                status = "未分配";
                break;
            case 1:
                status = "已激活";
                break;
            case 2:
                status = "已使用";
                break;
            case 3:
                status = "锁定";
                break;
            case 4:
                status = "开户中";
                break;
        }
        tvStatus.setText(status);
        tvLess.setText(agentsLiangNumber.getMinConsumption() + "元");
        tvTime.setText(agentsLiangNumber.getCreateDate());
    }
}
