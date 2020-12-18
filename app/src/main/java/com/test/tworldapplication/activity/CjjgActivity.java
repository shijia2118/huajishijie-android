package com.test.tworldapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.test.tworldapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CjjgActivity extends AppCompatActivity {
    private String name = "";
    @BindView(R.id.tvName)
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cjjg);
        ButterKnife.bind(this);
        setFinishOnTouchOutside(false);
        name = getIntent().getStringExtra("name");
        tvName.setText(name);

    }

    @OnClick({R.id.imgClose, R.id.imgGet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imgClose:
//                finish();
//                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
            case R.id.imgGet:
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                break;
        }

    }
}
