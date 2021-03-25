package com.test.tworldapplication.activity.admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountBalanceActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.activity.main.MainNewActivity;
import com.test.tworldapplication.activity.other.MessageMainActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Admin;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostLogout;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.utils.CacheUtil;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import wintone.passport.sdk.utils.AppManager;

public class AdminActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lvAdmin)
    ListView lvAdmin;
    @BindView(R.id.btExit)
    TextView btExit;
    List<Admin> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ButterKnife.bind(this);
        setBackGroundTitle("个人中心", true, false, false);
        lvAdmin.setOnItemClickListener(this);
        initView();

    }

    private void initView() {
        list.clear();
        list.add(new Admin(R.mipmap.grxx, "个人信息"));
        list.add(new Admin(R.mipmap.mmgl, "密码管理"));
        list.add(new Admin(R.mipmap.xxzx, "消息中心"));
        list.add(new Admin(R.mipmap.yjtj, "佣金统计"));
        list.add(new Admin(R.mipmap.sz, "设置"));
        lvAdmin.setAdapter(new MyAdaper());
    }

    @OnClick(R.id.btExit)
    public void onClick() {
        if (!Util.isFastDoubleClick()) {
            HttpPost<PostLogout> httpPost = new HttpPost<>();
            PostLogout postLogout = new PostLogout();
            postLogout.setSession_token(Util.getLocalAdmin(AdminActivity.this)[0]);
            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost.setParameter(postLogout);
            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postLogout) + BaseCom.APP_PWD));
            new AdminHttp().logout(AdminRequest.logout(AdminActivity.this, new SuccessNull() {
                @Override
                public void onSuccess() {
                    SharedPreferences share = getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
                    SharedPreferences.Editor edit = share.edit(); //编辑文件
//                edit.putString("session_token", "");
//                edit.putString("gride", "");
                    edit.clear();
                    edit.commit();  //保存数据信息
                    Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                    intent.putExtra("from", "1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                    AppManager.getAppManager().finishActivity(MainNewActivity.class);
//                    AppManager.getAppManager().finishActivity();
//                AdminActivity.this.finish();

                }
            }), httpPost);
        }


    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                gotoActy(PersonInformationActivity.class);
                break;
            case 1:
                gotoActy(PasswordManageActivity.class);
                break;
            case 2:
                gotoActy(MessageMainActivity.class);
                break;
            case 3:
                //gotoActy(CommissionStatisticActivity.class);
                gotoActy(CommissionListActivity.class);
                break;
            case 4:
                gotoActy(SettingMainActivity.class);
                break;

        }
    }

    class MyAdaper extends BaseAdapter {

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
                convertView = LayoutInflater.from(AdminActivity.this).inflate(R.layout.adapter_admin_main, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.imgLogo.setBackgroundResource(list.get(position).getImageId());
            holder.tvLogo.setText(list.get(position).getStrItem());
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.imgLogo)
            ImageView imgLogo;
            @BindView(R.id.tvLogo)
            TextView tvLogo;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);

            }
        }
    }
}
