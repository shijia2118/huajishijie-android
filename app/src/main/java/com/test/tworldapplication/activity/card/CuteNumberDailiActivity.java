package com.test.tworldapplication.activity.card;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.adapter.NumberAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.LiangRule;
import com.test.tworldapplication.entity.Number;
import com.test.tworldapplication.entity.NumberPool;
import com.test.tworldapplication.entity.NumberType;
import com.test.tworldapplication.entity.PostCuteRegular;
import com.test.tworldapplication.entity.PostNumberPool;
import com.test.tworldapplication.entity.PostPreNumberList;
import com.test.tworldapplication.entity.PostPreNumberPool;
import com.test.tworldapplication.entity.PostPreRandomNumber;
import com.test.tworldapplication.entity.PostRandomNumber;
import com.test.tworldapplication.entity.PreRandomNumber;
import com.test.tworldapplication.entity.RequestNumberPool;
import com.test.tworldapplication.entity.RequestPreNumberList;
import com.test.tworldapplication.entity.RequestPreNumberPool;
import com.test.tworldapplication.entity.RequestPreRandomNumber;
import com.test.tworldapplication.entity.WhitePreOpen;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.OnSelectListener;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.DisplayUtil;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.MyPopWindow;
import com.test.tworldapplication.view.WheelView;
import com.test.tworldapplication.view.pullableview.PullToRefreshLayout;
import com.test.tworldapplication.view.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

import static com.test.tworldapplication.base.BaseCom.page;

public class CuteNumberDailiActivity extends BaseActivity {
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvCondition)
    TextView tvCondition;
    @BindView(R.id.tvCondition0)
    TextView tvCondition0;
    @BindView(R.id.tvCondition1)
    TextView tvCondition1;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.vLine0)
    View vLine0;
    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.shadow_view)
    View shadowView;
//    @BindView(R.id.refresh_view)
//    PullToRefreshLayout refreshView;

    Area area;
    Holder holder = null;
    PopupWindow mPopupWindow;
    List<String> strpools = new ArrayList<>();
    List<String> strRegulars = new ArrayList<>();
    List<NumberPool> numberPools = new ArrayList<>();
    List<NumberType> numberTypes = new ArrayList<>();


    int refresh = 0;
    PullToRefreshLayout pullToRefreshLayout;
    MyAdapter myAdapter;
    int seletionRegular = 0, seletionPool = 0;

    private List<PreRandomNumber> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cute_number_daili);
        ButterKnife.bind(this);
        setBackGroundTitle("号码列表", true);
        tvCollection.setVisibility(View.VISIBLE);
        tvCollection.setText("筛选");
        area = BaseUtils.getArea(CuteNumberDailiActivity.this);
        getFilterData();
        myAdapter = new MyAdapter(CuteNumberDailiActivity.this, list);
        contentView.setAdapter(myAdapter);

        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CuteNumberDailiActivity.this, CuteNumberDetailDailiActivity.class);
                intent.putExtra("data", list.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_four, null);
        holder = new Holder(music_popunwindwow);

        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
    }

//    private void getPostNumberPool() {
//    /*号码池*/
//        HttpPost<PostNumberPool> httpPost = new HttpPost<>();
//        PostNumberPool postNumberPool = new PostNumberPool();
//        postNumberPool.setSession_token(Util.getLocalAdmin(CuteNumberDailiActivity.this)[0]);
//        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
//        httpPost.setParameter(postNumberPool);
//        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNumberPool) + BaseCom.APP_PWD));
//        Log.d("ggg", gson.toJson(httpPost));
//        new CardHttp().whiteNumberPool(CardRequest.whiteNumberPool(CuteNumberDailiActivity.this, new SuccessValue<RequestNumberPool>() {
//            @Override
//            public void OnSuccess(RequestNumberPool value) {
//                NumberPool[] numberpools = value.getNumberpool();
//                for (int i = 0; i < numberpools.length; i++) {
//                    numberPools.add(numberpools[i]);
//                    strpools.add(numberpools[i].getName());
//                }
//                getData();
//
//            }
//        }), httpPost);
//
//
//    }

//    private void initPopWindow() {
//        myPopWindow = new MyPopWindow(CuteNumberDailiActivity.this, 5, strRegulars, strpools, null);
//        myPopWindow.setOnQueryListener(new MyPopWindow.OnQueryListener() {
//            @Override
//            public void OnQuery(String strPopBegin, String strPopEnd, String strAddress, String strRegular, String strPool, String strNumber, int regularIndex, int poolIndex, int stateIndex, int selection, String provinceCode, String cityCode) {
//
//            }
//        });
//    }

    private void getFilterData() {
        PostPreNumberPool postPreNumberPool = new PostPreNumberPool();
        postPreNumberPool.setSession_token(Util.getLocalAdmin(CuteNumberDailiActivity.this)[0]);
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
//                initPopWindow();
                getData();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPreNumberPool> requestPreNumberPoolHttpRequest) {
                if (requestPreNumberPoolHttpRequest.getCode() == BaseCom.NORMAL) {
                    numberPools.addAll(requestPreNumberPoolHttpRequest.getData().getNumberpool());
                    numberTypes.addAll(requestPreNumberPoolHttpRequest.getData().getNumberType());
                } else if (requestPreNumberPoolHttpRequest.getCode() == BaseCom.LOSELOG || requestPreNumberPoolHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                    Util.gotoActy(CuteNumberDailiActivity.this, LoginActivity.class);
            }
        });

    }

    @OnClick(R.id.tvCollection)
    public void onClick() {
        shadowView.setVisibility(View.VISIBLE);
//        contentView.setAlpha((float) 0.2);
        mPopupWindow.showAsDropDown(vLine, 0, 0);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                contentView.setAlpha((float) 1);
                shadowView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void getData() {
        list.clear();
        PostPreRandomNumber postRandomNumber = new PostPreRandomNumber();
        postRandomNumber.setSession_token(Util.getLocalAdmin(CuteNumberDailiActivity.this)[0]);
        postRandomNumber.setNumberpool(numberPools.get(seletionPool).getId());
//        Log.d("aaa", numberTypes.get(0).getId() + "");
        postRandomNumber.setRuleNameId(String.valueOf(numberTypes.get(seletionRegular).getRuleNameId()));
        HttpPost<PostPreRandomNumber> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postRandomNumber);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRandomNumber) + BaseCom.APP_PWD));
        new CardHttp().preRandomNumber(httpPost, new Subscriber<HttpRequest<RequestPreRandomNumber>>() {
            @Override
            public void onCompleted() {
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPreRandomNumber> requestPreRandomNumberHttpRequest) {
                Util.createToast(CuteNumberDailiActivity.this, requestPreRandomNumberHttpRequest.getMes());
                if (requestPreRandomNumberHttpRequest.getCode() == BaseCom.NORMAL) {
                    tvCondition.setText(requestPreRandomNumberHttpRequest.getData().getCount() + "");
                    list.addAll(requestPreRandomNumberHttpRequest.getData().getNumbers());
                } else if (requestPreRandomNumberHttpRequest.getCode() == BaseCom.LOSELOG || requestPreRandomNumberHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(CuteNumberDailiActivity.this, LoginActivity.class);
                }
            }
        });


    }
//
//
//    private void finishGetData() {
//        switch (refresh) {
//            case 0:
//                list.clear();
//                break;
//            case 1:
//                list.clear();
//                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
//                break;
//            case 2:
//                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//                break;
//        }
//        refresh = 0;
//
//
//    }

    class Holder {
        @BindView(R.id.tvRegular)
        TextView tvRegular;

        @BindView(R.id.tvPool)
        TextView tvPool;

        public Holder(View view) {

            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.ll_regular, R.id.ll_pool, R.id.tvInquery, R.id.tvReset})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_regular:
                    Util.createDialog(CuteNumberDailiActivity.this, strRegulars, tvRegular, new OnSelectListener() {
                        @Override
                        public void onSelect(int selection) {
                            seletionRegular = selection;
                        }
                    });
                    break;
                case R.id.ll_pool:
                    Util.createDialog(CuteNumberDailiActivity.this, strpools, tvPool, new OnSelectListener() {
                        @Override
                        public void onSelect(int selection) {
                            seletionPool = selection;
                        }
                    });
                    break;
                case R.id.tvInquery:
                    mPopupWindow.dismiss();
                    llCondition.setVisibility(View.VISIBLE);
                    vLine0.setVisibility(View.VISIBLE);
                    tvCondition0.setText("筛选条件:  " + Util.strBack(tvPool.getText().toString()));
                    tvCondition1.setText(Util.strBack(tvRegular.getText().toString()));
                    getData();
                    break;
                case R.id.tvReset:
                    seletionPool = 0;
                    seletionRegular = 0;
//                    if (numberTypes.size() > 0)
                        tvRegular.setText("请选择");
//                    if (numberPools.size() > 0)
                        tvPool.setText("请选择");
                    break;
            }
        }
    }


    class MyAdapter extends BaseAdapter {
        Context context;
        List<PreRandomNumber> list;

        public MyAdapter(Context context, List<PreRandomNumber> list) {
            this.context = context;
            this.list = list;
        }

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
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_whitepreopen_list, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
//            Log.d("aaa", list.get(position).getPhone());
//            Log.d("aaa", list.get(position).getStatus());
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorOrange));
            viewHolder.tvPhone.setText("手机号:  " + list.get(position).getNum() + "  说明:  ");
            viewHolder.tvStatus.setText(list.get(position).getInfos());
            return convertView;
        }

    }

    class ViewHolder {
        @BindView(R.id.tvPhone)
        TextView tvPhone;
        @BindView(R.id.tvStatus)
        TextView tvStatus;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
