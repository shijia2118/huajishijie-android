package com.test.tworldapplication.activity.other;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.card.AccountClosingActivity;
import com.test.tworldapplication.activity.card.TransferCardActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.Message;
import com.test.tworldapplication.entity.Notice;
import com.test.tworldapplication.entity.PostNoticeList;
import com.test.tworldapplication.entity.RequestNoticeList;
import com.test.tworldapplication.entity.RequestOrderList;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;
import com.test.tworldapplication.view.pullableview.PullToRefreshLayout;
import com.test.tworldapplication.view.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageMainActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    Notice[] list = null;
    Notice[] list_true = null;
    @BindView(R.id.content_view)
    PullableListView ivMessage;
    @BindView(R.id.refresh_view)
    PullToRefreshLayout refreshView;
    int page = 1;
    int linage = 10;
    int refresh = 0;
    MyAdapter adapter = new MyAdapter();
    PullToRefreshLayout pullToRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_main);
        ButterKnife.bind(this);
        setBackGroundTitle("消息中心", true, false, false);
        refreshView.setOnRefreshListener(this);
        ivMessage.setOnItemClickListener(this);
        dialog.getTvTitle().setText("正在获取数据");

    }

    @Override
    protected void onResume() {
        super.onResume();
        list = null;

//        if (Util.isLog(MessageMainActivity.this)) {
        noticeList();
//        }
    }


    public void noticeList() {
        dialog.show();
        HttpPost<PostNoticeList> httpPost = new HttpPost<>();
        PostNoticeList postNoticeList = new PostNoticeList();
        postNoticeList.setSession_token(Util.getLocalAdmin(MessageMainActivity.this)[0]);
        postNoticeList.setPage(String.valueOf(page));
        postNoticeList.setLinage(String.valueOf(linage));
        postNoticeList.setType(2);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postNoticeList);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNoticeList) + BaseCom.APP_PWD));
        new OtherHttp().noticeList(OtherRequest.noticeList(1, MessageMainActivity.this, dialog, new SuccessValue<RequestNoticeList>() {
            @Override
            public void OnSuccess(RequestNoticeList value) {

                list = value.getNotice();
                SharedPreferences share = getSharedPreferences(BaseCom.MESSAGE, MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit(); //编辑文件
                edit.putString("time", list[0].getUpdateDate());
                edit.commit();  //保存数据信息

                switch (refresh) {
                    case 0:
                        adapter.setList(list);
                        ivMessage.setAdapter(adapter);
                        list_true = null;
                        list_true = list;
                        break;
                    case 1:
                        pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                        adapter.setList(list);
                        adapter.notifyDataSetChanged();
                        list_true = null;
                        list_true = list;
                        break;
                    case 2:
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        list_true = Util.concat(list_true, list);
                        adapter.setList(list_true);
                        adapter.notifyDataSetChanged();
                        break;

                }


            }
        }), httpPost);

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        refresh = 1;
        this.pullToRefreshLayout = pullToRefreshLayout;
        noticeList();

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        refresh = 2;
        this.pullToRefreshLayout = pullToRefreshLayout;
        noticeList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!Util.isFastDoubleClick()) {
        Intent intent = new Intent(this, MessageDetailActivity.class);
        intent.putExtra("id", list_true[position].getId());
        startActivity(intent);
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);}
    }

    class MyAdapter extends BaseAdapter {
        Notice[] mList;

        public void setList(Notice[] list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.length;
        }

        @Override
        public Object getItem(int position) {
            return mList[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(MessageMainActivity.this).inflate(R.layout.adapter_message_list, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvTitle.setText(mList[position].getTitle());
            holder.tvTime.setText(mList[position].getUpdateDate());
            return convertView;
        }

        class ViewHolder {

            @BindView(R.id.tvTitle)
            TextView tvTitle;
            @BindView(R.id.tvTime)
            TextView tvTime;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);

            }
        }
    }
}
