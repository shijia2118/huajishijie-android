package com.test.tworldapplication.activity.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordManageActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lvpassword)
    ListView lvpassword;
    List<String> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manage);
        ButterKnife.bind(this);
        setBackGroundTitle("密码管理", true, false, false);
        initView();

    }

    private void initView() {

        lvpassword.setOnItemClickListener(this);
        list.clear();
        if (Util.getLocalAdmin(PasswordManageActivity.this)[1].equals("lev3")) {
            list.add("登录密码修改");
            list.add("支付密码创建");
            list.add("支付密码修改");
        } else {
            list.add("登录密码修改");
        }

        lvpassword.setAdapter(new MyAdapter());
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                gotoActy(LogPassReviseActivity.class);
                break;
            case 1:
                gotoActy(PayPassCreateActivity.class);
                break;
            case 2:
                gotoActy(PayPassVerifyActivity.class);
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(PasswordManageActivity.this).inflate(R.layout.adapter_password_main, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvLogo.setText(list.get(position));
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tvLogo)
            TextView tvLogo;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
