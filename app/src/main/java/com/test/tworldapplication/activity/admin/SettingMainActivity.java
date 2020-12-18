package com.test.tworldapplication.activity.admin;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Setting;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.utils.DataCleanManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingMainActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.lvSetting)
    ListView lvSetting;
    String version = "";
    List<Setting> list = new ArrayList<>();
    String cache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);
        ButterKnife.bind(this);
        setBackGroundTitle("设置", true, false, false);
        lvSetting.setOnItemClickListener(this);

        getCache();

    }

    public void getCache() {
        try {
            cache = DataCleanManager.getTotalCacheSize(SettingMainActivity.this);
            Log.d("eee", cache);
        } catch (Exception e) {
            e.printStackTrace();
        }
        initView();
//        return cache;
    }

    private void initView() {

        getVersion();
        list.clear();
        list.add(new Setting("意见反馈", ""));
        list.add(new Setting("清除缓存", cache));
        list.add(new Setting("APP评分", ""));
        list.add(new Setting("关于我们", ""));
        list.add(new Setting("版本检查", version));
        lvSetting.setAdapter(new MyAdapter());
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                gotoActy(FeedbackMainActivity.class);
                break;
            case 1:
                DataCleanManager.clearAllCache(SettingMainActivity.this);
                getCache();
                break;
            case 3:
                gotoActy(AboutUsActivity.class);
                break;
        }
    }

    public void getVersion() {

        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            version = info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(SettingMainActivity.this).inflate(R.layout.adapter_setting_main, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvKey.setText(list.get(position).getKey());
            holder.tvValue.setText(list.get(position).getValue());
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.tvKey)
            TextView tvKey;
            @BindView(R.id.tvValue)
            TextView tvValue;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);

            }
        }
    }
}
