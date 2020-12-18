package com.test.tworldapplication.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.test.tworldapplication.R;
import com.test.tworldapplication.view.MyPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TextActivity extends AppCompatActivity {


    @BindView(R.id.activity_main2)
    RelativeLayout activityMain2;
    MyPopWindow myPopuWindow;
    @BindView(R.id.btClick)
    Button btClick;
    private List<String> regularList = new ArrayList<>();
    private List<String> poolList = new ArrayList<>();
    private List<String> stateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        ButterKnife.bind(this);


        regularList.add("0");
        regularList.add("1");
        regularList.add("2");
        regularList.add("3");
        regularList.add("4");
        poolList.add("1");
        poolList.add("2");
        poolList.add("3");
        poolList.add("4");
        poolList.add("5");
        stateList.add("0");
        stateList.add("1");
        stateList.add("2");
        stateList.add("3");
        stateList.add("4");
        myPopuWindow = new MyPopWindow(TextActivity.this, 0, regularList, poolList, stateList);
        myPopuWindow.setOnQueryListener(new MyPopWindow.OnQueryListener() {
            @Override
            public void OnQuery(String strPopBegin, String strPopEnd, String strAddress, String strRegular, String strPool, String strNumber, int regularIndex, int poolIndex, int stateIndex, int selection, String provinceCode, String cityCode) {
                Log.d("aaa", strPopBegin);
                Log.d("aaa", strPopEnd);
                Log.d("aaa", strAddress);
                Log.d("aaa", strRegular);
                Log.d("aaa", strPool);
                Log.d("aaa", strNumber);
                Log.d("aaa", regularIndex + "");
                Log.d("aaa", poolIndex + "");
                Log.d("aaa", stateIndex + "");
                Log.d("aaa", selection + "");
                Log.d("aaa", provinceCode);
                Log.d("aaa", cityCode);
            }
        });

    }

    @OnClick(R.id.btClick)
    public void onClick() {

        myPopuWindow.showPopupWindow(btClick);

    }
}
