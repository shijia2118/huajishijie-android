package com.test.tworldapplication.activity.card;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.NumberPool;
import com.test.tworldapplication.entity.NumberType;
import com.test.tworldapplication.entity.Numbers;
import com.test.tworldapplication.entity.PostLockNumber;
import com.test.tworldapplication.entity.PostNumberPool;
import com.test.tworldapplication.entity.PostPreNumberPool;
import com.test.tworldapplication.entity.PostRandomNumber;
import com.test.tworldapplication.entity.RequestNumberPool;
import com.test.tworldapplication.entity.RequestPreNumberPool;
import com.test.tworldapplication.entity.RequestRandomNumber;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.OnSelectListener;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.NumberDetailDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class NewCardActivity extends BaseActivity {

    @BindView(R.id.imgArr)
    ImageView imgArr;
    @BindView(R.id.llArr)
    LinearLayout llArr;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.llCondition)
    LinearLayout llCondition;
    @BindView(R.id.gvNumber)
    GridView gvNumber;
    MyAdapter adapter;
    OnPosition onPosition;
    int pos = 0;
    @BindView(R.id.tvOthers)
    TextView tvOthers;
    @BindView(R.id.tvNext)
    TextView tvNext;
    //    List<String> numberList = new ArrayList<>();
//    List<String> regularList = new ArrayList<>();
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvRegular)
    TextView tvRegular;
    @BindView(R.id.shadow_view)
    View shadowView;
    //    String strRegular;//用来显示页面上
//    NumberPool[] numberPools;//承载查询出来的号码池
    Numbers[] numbers = null;//查询出来的号码集合
    //    int seletion = 0;//标记选择的item
    String[] regulars;
    ViewHolder viewHolder;
    PopupWindow mPopupWindow;

    List<String> strpools = new ArrayList<>();
    List<String> strRegulars = new ArrayList<>();
    List<NumberPool> numberPools = new ArrayList<>();
    List<NumberType> numberTypes = new ArrayList<>();
    int seletionRegular = 0, seletionPool = 0;


//    String strNumberPool = "", StrRegular = "";

    /*先获取号码池和靓号规则,根据numberpool[0],regular[0]获取号码数组,初始化列表,号码锁定之后跳转页面,传值锁定的的手机号*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        ButterKnife.bind(this);
        setBackGroundTitle("号码选择", true);
        initView();
        initOnPosition();
        adapter = new MyAdapter();

    }

    private void initView() {
        dialog.getTvTitle().setText("正在请求数据");
        imgArr.setBackgroundResource(R.drawable.arr_down);
        LayoutInflater mLayoutInflater = (LayoutInflater) NewCardActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_three, null);
        viewHolder = new ViewHolder(music_popunwindwow);
        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));

    }

    private void initOnPosition() {
        onPosition = new OnPosition() {
            @Override
            public void getPosition(int flag, int position) {
                switch (flag) {
                    case 0:
                        pos = position;
                        adapter.notifyDataSetChanged();

                        break;
                    case 1:
//                        Log.d("aaa", numbers[position].getInfos());
//                        Log.d("aaa", numbers[position].getLtype());
//                        Log.d("aaa", numbers[position].getNum());
                        NumberDetailDialog dialog = new NumberDetailDialog(NewCardActivity.this);
                        dialog.setNumber(numbers[position]);
                        dialog.show();
                        break;
                }

            }
        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        //防止页面卡顿
        strpools.clear();
        strRegulars.clear();
        numberPools.clear();
        numberTypes.clear();
        llCondition.setVisibility(View.GONE);
        imgArr.setBackgroundResource(R.drawable.arr_down);
        mPopupWindow.dismiss();
        PostPreNumberPool postPreNumberPool = new PostPreNumberPool();
        postPreNumberPool.setSession_token(Util.getLocalAdmin(NewCardActivity.this)[0]);
        HttpPost<PostPreNumberPool> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPreNumberPool);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPreNumberPool) + BaseCom.APP_PWD));
        new CardHttp().getPreWhiteNumberPool(httpPost, new Subscriber<HttpRequest<RequestPreNumberPool>>() {
            @Override
            public void onCompleted() {
                for (int i = 0; i < numberPools.size(); i++) {
                    strpools.add(numberPools.get(i).getName());
                }
                for (int i = 0; i < numberTypes.size(); i++) {
                    strRegulars.add(numberTypes.get(i).getRuleName());
                }
                if (numberPools.size() > 0 && numberTypes.size() > 0)
                    search();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPreNumberPool> requestPreNumberPoolHttpRequest) {
                if (requestPreNumberPoolHttpRequest.getCode() == BaseCom.NORMAL) {
                    numberPools.addAll(requestPreNumberPoolHttpRequest.getData().getNumberpool());
                    numberTypes.addAll(requestPreNumberPoolHttpRequest.getData().getNumberType());
                } else if (requestPreNumberPoolHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(NewCardActivity.this, LoginActivity.class);
            }
        });


//
////        if (Util.isLog(NewCardActivity.this)) {
//        HttpPost<PostNumberPool> httpPost = new HttpPost<>();
//        PostNumberPool postNumberPool = new PostNumberPool();
//        postNumberPool.setSession_token(Util.getLocalAdmin(NewCardActivity.this)[0]);
//        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
//        httpPost.setParameter(postNumberPool);
//        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNumberPool) + BaseCom.APP_PWD));
//        new CardHttp().whiteNumberPool(CardRequest.whiteNumberPool(NewCardActivity.this, new SuccessValue<RequestNumberPool>() {
//            @Override
//            public void OnSuccess(RequestNumberPool value) {
//                numberPools = value.getNumberpool();
//                regulars = value.getNumbetrType();
//                seletion = 0;
//                strRegular = regulars[0];
//
//                for (int i = 0; i < numberPools.length; i++) {
//                    numberList.add(numberPools[i].getName());
//                }
//                for (int j = 0; j < regulars.length; j++) {
//                    regularList.add(regulars[j]);
//                }
//                search(seletion, strRegular);
//            }
//        }), httpPost);
//        }
    }

    class ViewHolder {
        @BindView(R.id.tvNumberPop)
        TextView tvNumberPop;
        @BindView(R.id.llNumber)
        LinearLayout llNumber;
        @BindView(R.id.tvRegularPop)
        TextView tvRegularPop;
        @BindView(R.id.llRegular)
        LinearLayout llRegular;
        @BindView(R.id.tvInquery)
        TextView tvInquery;
        @BindView(R.id.tvReset)
        TextView tvReset;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.llNumber, R.id.llRegular, R.id.tvInquery, R.id.tvReset})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llNumber:
                    /*号码池*/
                    Util.createDialog(NewCardActivity.this, strpools, tvNumberPop, new OnSelectListener() {
                        @Override
                        public void onSelect(int selection) {
                            seletionRegular = selection;
                        }
                    });
                    break;
                case R.id.llRegular:
                    Util.createDialog(NewCardActivity.this, strRegulars, tvRegularPop, new OnSelectListener() {
                        @Override
                        public void onSelect(int selection) {
                            seletionRegular = selection;
                        }
                    });
                    break;
                case R.id.tvInquery:
                    if (numberPools.size() > 0)
                        tvNumber.setText("号码池:" + Util.strBackNull(numberPools.get(seletionPool).getName()));
                    if (numberTypes.size() > 0)
                        tvRegular.setText("靓号规则:" + Util.strBackNull(numberTypes.get(seletionRegular).getRuleName()));
                    llCondition.setVisibility(View.GONE);
                    shadowView.setVisibility(View.GONE);
                    mPopupWindow.dismiss();
                    search();
                    break;
                case R.id.tvReset:
                    if (numberPools.size() > 0)
                        tvNumberPop.setText(numberPools.get(0).getName());
                    else
                        tvNumberPop.setText("请选择");
                    if (numberTypes.size() > 0)
                        tvRegularPop.setText(numberTypes.get(seletionRegular).getRuleName());
                    else
                        tvRegularPop.setText("请选择");

                    break;
            }
        }
    }

    @OnClick({R.id.llArr, R.id.tvOthers, R.id.tvNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llArr:
                imgArr.setBackgroundResource(R.drawable.arr_up);
                if (numberPools.size() > 0)
                    viewHolder.tvNumberPop.setText(numberPools.get(seletionPool).getName());
                if (numberTypes.size() > 0)
                    viewHolder.tvRegularPop.setText(numberTypes.get(seletionRegular).getRuleName());
//                gvNumber.setAlpha((float) 0.2);
                shadowView.setVisibility(View.VISIBLE);
                mPopupWindow.showAsDropDown(vLine, 0, 0);
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                        gvNumber.setAlpha((float) 1);
                        shadowView.setVisibility(View.GONE);
                        imgArr.setBackgroundResource(R.drawable.arr_down);
                    }
                });
                break;
            case R.id.tvOthers:
                search();
                break;
            case R.id.tvNext:

//                if (Util.isLog(NewCardActivity.this)) {
//                Log.d("ppp", numbers[pos].getNum());
//                Log.d("ppp", strRegular);
                if (!Util.isFastDoubleClick()) {
                    HttpPost<PostLockNumber> httpPost = new HttpPost<>();
                    PostLockNumber postLockNumber = new PostLockNumber();
                    postLockNumber.setSession_token(Util.getLocalAdmin(NewCardActivity.this)[0]);
                    postLockNumber.setNumber(numbers[pos].getNum());
                    postLockNumber.setNumberType(numberTypes.get(seletionRegular).getRuleName());
                    postLockNumber.setNumberpoolId(String.valueOf(numberPools.get(seletionPool).getId()));
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setParameter(postLockNumber);
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postLockNumber) + BaseCom.APP_PWD));
                    Log.d("aaa", gson.toJson(httpPost));
                    new CardHttp().lockNumber(CardRequest.lockNumber(NewCardActivity.this, new SuccessNull() {
                        @Override
                        public void onSuccess() {
//                        Intent intent = new Intent(NewCardActivity.this, SelectActivity.class);
//                        intent.putExtra("from", "1");
//                        intent.putExtra("phone", numbers[pos].getNum());
//                        startActivity(intent);


                            Intent intent = new Intent(NewCardActivity.this, CardReadingActivity.class);
                            intent.putExtra("from", "1");
                            intent.putExtra("phone", numbers[pos].getNum());
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);


                        }
                    }), httpPost);
                }
//                }
                break;
        }
    }

    //Integer select, String strRegular
    public void search() {
        if (numberPools.size() > 0 && numberTypes.size() > 0) {
            dialog.show();
            HttpPost<PostRandomNumber> httpPost = new HttpPost<>();
            PostRandomNumber postRandomNumber = new PostRandomNumber();
            postRandomNumber.setSession_token(Util.getLocalAdmin(NewCardActivity.this)[0]);
            postRandomNumber.setNumberpool(numberPools.get(seletionPool).getId());
            postRandomNumber.setNumberType(numberTypes.get(seletionRegular).getRuleName());
            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost.setParameter(postRandomNumber);
            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRandomNumber) + BaseCom.APP_PWD));
            Log.d("ccc", gson.toJson(httpPost));
            new CardHttp().randomNumber(CardRequest.randomNumber(NewCardActivity.this, dialog, new SuccessValue<RequestRandomNumber>() {

                @Override
                public void OnSuccess(RequestRandomNumber value) {
                    Log.d("aaa", "456");
                    pos = 0;
                    numbers = value.getNumbers();
                    gvNumber.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }), httpPost);
        }


    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return numbers.length;
        }

        @Override
        public Object getItem(int position) {
            return numbers[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(NewCardActivity.this).inflate(R.layout.adapter_number_gride, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position % 4 == 0 || position % 4 == 3) {
                holder.llhomeAdapter.setBackgroundColor(getResources().getColor(R.color.colorGray8));
            } else {
                holder.llhomeAdapter.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            }
            if (position == pos) {
                holder.imgClick.setBackgroundResource(R.drawable.shape_checkbox_click);
            } else {
                holder.imgClick.setBackgroundResource(R.drawable.shape_checkbox_noclick);
            }

            holder.tvNumber.setText(numbers[position].getNum());
            holder.llClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPosition.getPosition(0, position);
                }
            });
            holder.imgDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPosition.getPosition(1, position);
                }
            });
            return convertView;
        }

        class ViewHolder {
            @BindView(R.id.imgClick)
            ImageView imgClick;
            @BindView(R.id.tvNumber)
            TextView tvNumber;
            @BindView(R.id.imgDetail)
            ImageView imgDetail;
            @BindView(R.id.llhomeAdapter)
            LinearLayout llhomeAdapter;
            @BindView(R.id.llClick)
            LinearLayout llClick;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);

            }
        }

    }

    public interface OnPosition {
        void getPosition(int flag, int position);
    }

}

