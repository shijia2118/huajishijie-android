package com.test.tworldapplication.activity.order;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LiangAgents;
import com.test.tworldapplication.entity.PostBkApplyList;
import com.test.tworldapplication.entity.RequestBkApplyList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;

public class BkOrderListActivity extends BaseActivity {
    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.springview)
    SpringView springView;

    List<LiangAgents> bk_list = new ArrayList<>();

    private String from = "";
    int refresh = 0;
    private int page = 1, linage = 10;
    MyAdapter myAdapter;

    List<String> list0Order = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baika_order);
        ButterKnife.bind(this);

        setBackGroundTitle("白卡申请订单", true);
        list0Order.clear();
        list0Order.add("审核中");
        list0Order.add("审核通过");
        list0Order.add("待审核");

        springView.setHeader(new DefaultHeader(BkOrderListActivity.this));
        springView.setFooter(new DefaultFooter(BkOrderListActivity.this));

        myAdapter = new MyAdapter();
        contentView.setAdapter(myAdapter);
        getData();
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(BkOrderListActivity.this, BkApplyOrderActivity.class);
                intent.putExtra("data", bk_list.get(position));
                if (!Util.isFastDoubleClick()) {
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }
            }
        });

    }


    private void getData() {

        HttpPost<PostBkApplyList> httpPost = new HttpPost<>();
        PostBkApplyList postBkApplyList = new PostBkApplyList();
        postBkApplyList.setSession_token(Util.getLocalAdmin(BkOrderListActivity.this)[0]);

        postBkApplyList.setPage(String.valueOf(page));
        postBkApplyList.setLinage(String.valueOf(BaseCom.linage));
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postBkApplyList);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postBkApplyList) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new OrderHttp().orderBkList(new Subscriber<HttpRequest<RequestBkApplyList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestBkApplyList> requestBkApplyListHttpRequest) {
                Util.createToast(BkOrderListActivity.this, requestBkApplyListHttpRequest.getMes());
                if (requestBkApplyListHttpRequest.getCode() == BaseCom.NORMAL) {
                    switch (refresh) {
                        case 0:
                            bk_list.clear();
                            break;
                        case 1:
                            bk_list.clear();
                            springView.onFinishFreshAndLoad();
                            break;
                        case 2:
                            springView.onFinishFreshAndLoad();
                            break;
                    }
                    refresh = 0;
                    bk_list.addAll(requestBkApplyListHttpRequest.getData().getWcardApply());
                    myAdapter.notifyDataSetChanged();
                } else if (requestBkApplyListHttpRequest.getCode() == BaseCom.LOSELOG || requestBkApplyListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(BkOrderListActivity.this, LoginActivity.class);
                }
            }
        }, httpPost);


    }


    class MyAdapter extends BaseAdapter {
        LayoutInflater layoutInflater;

        public MyAdapter() {
            layoutInflater = LayoutInflater.from(BkOrderListActivity.this);
        }

        @Override
        public int getCount() {
            int size = 0;

            size = bk_list.size();

            return size;
        }

        @Override
        public Object getItem(int position) {
            Object object = null;
            object = bk_list.get(position);
            return object;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_baika, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String strNumb = "";
            String strMount = "";
            String strName = "";
            String strTime = "";
            String strStatus = "";

            String id = bk_list.get(position).getId().toString();
            strNumb = bk_list.get(position).getOrderNumber();
            strMount = "申请：" + bk_list.get(position).getApplySum() + "  " + "获批："
                    + bk_list.get(position).getActualSum();
//            strName=bk_list.get( position ).getWcardApply().get( position ).
            strStatus = bk_list.get(position).getAuditStatusName();
            strTime = bk_list.get(position).getCreateDate();

            viewHolder.mNumb.setText(strNumb);
            viewHolder.mMount.setText(strMount);
//            viewHolder.mStatus.setText( strStatus );
            viewHolder.mTime.setText(strTime);

            if (strStatus.contains("不")) {
                viewHolder.mStatus.setVisibility(View.GONE);     //审核通过
                viewHolder.mStatus1.setVisibility(View.GONE);    //审核中
                viewHolder.mStatus2.setVisibility(View.VISIBLE); //审核不通过
                viewHolder.mStatus3.setVisibility(View.GONE);    //待审核
            } else if (strStatus.contains("中")) {
                viewHolder.mStatus.setVisibility(View.GONE);
                viewHolder.mStatus1.setVisibility(View.VISIBLE);
                viewHolder.mStatus2.setVisibility(View.GONE);
                viewHolder.mStatus3.setVisibility(View.GONE);
            } else if (strStatus.contains("待")) {
                viewHolder.mStatus.setVisibility(View.GONE);
                viewHolder.mStatus1.setVisibility(View.GONE);
                viewHolder.mStatus2.setVisibility(View.GONE);
                viewHolder.mStatus3.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mStatus.setVisibility(View.VISIBLE);
                viewHolder.mStatus1.setVisibility(View.GONE);
                viewHolder.mStatus2.setVisibility(View.GONE);
                viewHolder.mStatus3.setVisibility(View.GONE);
            }

            return convertView;
        }
    }


    class ViewHolder {
        @BindView(R.id.order_numb)
        TextView mNumb;
        @BindView(R.id.order_mount)
        TextView mMount;
        @BindView(R.id.user_name)
        TextView mName;
        @BindView(R.id.tv_time)
        TextView mTime;
        @BindView(R.id.status)
        TextView mStatus;
        @BindView(R.id.order_id)
        TextView mId;
        @BindView(R.id.status1)
        TextView mStatus1;
        @BindView(R.id.status2)
        TextView mStatus2;
        @BindView(R.id.status3)
        TextView mStatus3;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
