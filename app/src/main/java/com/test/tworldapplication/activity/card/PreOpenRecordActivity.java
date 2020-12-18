package com.test.tworldapplication.activity.card;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.adapter.NumberAdapter;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.Number;
import com.test.tworldapplication.entity.PostPreNumberList;
import com.test.tworldapplication.entity.PostPreRandomNumber;
import com.test.tworldapplication.entity.RequestPreNumberList;
import com.test.tworldapplication.entity.WhitePreOpen;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.DoubleDatePickerDialog;
import com.test.tworldapplication.view.pullableview.PullToRefreshLayout;
import com.test.tworldapplication.view.pullableview.PullableListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

public class PreOpenRecordActivity extends BaseActivity {
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.ll_condition)
    LinearLayout llCondition;
    @BindView(R.id.vLine0)
    View vLine0;

    List<Number> list = new ArrayList<>();
    PopupWindow mPopupWindow;
    Holder holder;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.tvCondition)
    TextView tvCondition;
    //    @BindView(R.id.tvCondition0)
//    TextView tvCondition0;
    @BindView(R.id.tvCondition1)
    TextView tvCondition1;
    @BindView(R.id.content_view)
    ListView contentView;
    @BindView(R.id.springview)
    SpringView springView;
    @BindView(R.id.shadow_view)
    View shadowView;
    int selection = 0;

    private int page = 1, linage = 10;
    int refresh = 0;
    //    PullToRefreshLayout pullToRefreshLayout;
    MyAdapter myAdapter;
    String strPopBegin = "", strPopEnd = "", strNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_open_record);
        ButterKnife.bind(this);
        setBackGroundTitle("预开户记录", true);
        tvCollection.setText("筛选");
        tvCollection.setVisibility(View.VISIBLE);
        getData();
//        list.add(new WhitePreOpen("12345678900", "已激活"));
//        list.add(new WhitePreOpen("12345678900", "已使用"));

        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                refresh = 1;
                getData();
            }

            @Override
            public void onLoadmore() {
                page++;
                refresh = 2;
                getData();
            }
        });
        springView.setHeader(new DefaultHeader(PreOpenRecordActivity.this));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultHeader(PreOpenRecordActivity.this));
        myAdapter = new MyAdapter(PreOpenRecordActivity.this, list);
        contentView.setAdapter(myAdapter);
        contentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(PreOpenRecordActivity.this, CutePhoneDetailDailiActivity.class);
                intent.putExtra("data", list.get(position));
                startActivity(intent);
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });


        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_seven, null);
        holder = new Holder(music_popunwindwow);

        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));

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


    private void finishGetData() {
        switch (refresh) {
            case 0:
                list.clear();
                break;
            case 1:
                list.clear();
                springView.onFinishFreshAndLoad();
                break;
            case 2:
                springView.onFinishFreshAndLoad();
                break;
        }
        refresh = 0;


    }


    class Holder {

        @BindView(R.id.tvAll)
        TextView tvAll;
        @BindView(R.id.tvWait)
        TextView tvWait;
        @BindView(R.id.tvDone)
        TextView tvDone;
        @BindView(R.id.etNumber)
        EditText etNumber;
        @BindView(R.id.tvPopBegin)
        TextView tvPopBegin;
        @BindView(R.id.tvPopEnd)
        TextView tvPopEnd;

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.tvAll, R.id.tvWait, R.id.tvDone, R.id.llBegin, R.id.llEnd, R.id.tvInquery, R.id.tvReset})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tvAll:
                    selection = 0;
                    tvAll.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvAll.setBackgroundResource(R.drawable.shape_recharge);
                    tvWait.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvWait.setBackgroundResource(R.drawable.shape_recharge_gray);
                    tvDone.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvDone.setBackgroundResource(R.drawable.shape_recharge_gray);
                    break;
                case R.id.tvWait:
                    selection = 1;
                    tvWait.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvWait.setBackgroundResource(R.drawable.shape_recharge);
                    tvAll.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvAll.setBackgroundResource(R.drawable.shape_recharge_gray);
                    tvDone.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvDone.setBackgroundResource(R.drawable.shape_recharge_gray);
                    break;
                case R.id.tvDone:
                    selection = 2;
                    tvDone.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvDone.setBackgroundResource(R.drawable.shape_recharge);
                    tvWait.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvWait.setBackgroundResource(R.drawable.shape_recharge_gray);
                    tvAll.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvAll.setBackgroundResource(R.drawable.shape_recharge_gray);
                    break;
                case R.id.llBegin:
                    if (!Util.isFastDoubleClick())
                        showDatePikerDialog(0, tvPopBegin);
                    break;
                case R.id.llEnd:
                    if (!Util.isFastDoubleClick())
                        showDatePikerDialog(1, tvPopEnd);
                    break;
                case R.id.tvInquery:
                    page = 1;
                    mPopupWindow.dismiss();
                    llCondition.setVisibility(View.VISIBLE);
                    vLine0.setVisibility(View.VISIBLE);
                    strPopBegin = Util.strBack(tvPopBegin.getText().toString());
                    strPopEnd = Util.strBack(tvPopEnd.getText().toString());
                    strNumber = etNumber.getText().toString();


//                    tvCondition0.setText("筛选条件:  " + etNumber.getText().toString() + " " + Util.strBack(tvPopBegin.getText().toString()) + " " + Util.strBack(tvPopEnd.getText().toString()));
                    String status = "";
                    switch (selection) {
                        case 0:
                            status = "全部";
                            break;
                        case 1:
                            status = "待开户";
                            break;
                        case 2:
                            status = "已卡户";
                            break;
                    }
//                    tvCondition1.setText(status);


                    String s = "筛选条件:  " + status + "  ";
                    String t = s + etNumber.getText().toString()+"  ";
                    String filter = t + Util.strBack(tvPopBegin.getText().toString()) + "  " + Util.strBack(tvPopEnd.getText().toString());

                    SpannableString content = new SpannableString(filter);
                    content.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), s.length(), t.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tvCondition1.setText(content);


                    getData();
                    break;
                case R.id.tvReset:
                    page = 1;
                    selection = 0;
                    tvAll.setTextColor(getResources().getColor(R.color.colorBlue));
                    tvAll.setBackgroundResource(R.drawable.shape_recharge);
                    tvWait.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvWait.setBackgroundResource(R.drawable.shape_recharge_gray);
                    tvDone.setTextColor(getResources().getColor(R.color.colorGray5));
                    tvDone.setBackgroundResource(R.drawable.shape_recharge_gray);
                    etNumber.setText("");
                    tvPopBegin.setText("请选择");
                    tvPopEnd.setText("请选择");
                    break;
            }
        }
    }


    private void showDatePikerDialog(final int click_flag, final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(PreOpenRecordActivity.this, "选择日期", 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                                  DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                String date = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);
                textView.setText(date);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), true);
        dialog.show();
    }


    private void getData() {
        PostPreNumberList postPreNumberList = new PostPreNumberList();
        postPreNumberList.setSession_token(Util.getLocalAdmin(PreOpenRecordActivity.this)[0]);
        postPreNumberList.setPage(String.valueOf(page));
        postPreNumberList.setLinage(String.valueOf(linage));
        if (!strPopBegin.equals("") && !strPopBegin.equals("请选择")) {
            postPreNumberList.setStartTime(strPopBegin);
        }
        if (!strPopEnd.equals("") && !strPopEnd.equals("请选择"))
            postPreNumberList.setEndTime(holder.tvPopEnd.getText().toString());
        if (!strNumber.equals(""))
            postPreNumberList.setNumber(strNumber);
        switch (selection) {
            case 0:
                break;
            case 1:
                postPreNumberList.setPreState("0");
                break;
            case 2:
                postPreNumberList.setPreState("1");
                break;
        }


        HttpPost<PostPreNumberList> httpPost = new HttpPost<>();
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postPreNumberList);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPreNumberList) + BaseCom.APP_PWD));
        new CardHttp().preNumberList(httpPost, new Subscriber<HttpRequest<RequestPreNumberList>>() {
            @Override
            public void onCompleted() {
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HttpRequest<RequestPreNumberList> requestPreNumberListHttpRequest) {
                finishGetData();
                Util.createToast(PreOpenRecordActivity.this, requestPreNumberListHttpRequest.getMes());
                if (requestPreNumberListHttpRequest.getCode() == BaseCom.NORMAL) {
                    list.addAll(requestPreNumberListHttpRequest.getData().getNumbers());

                    SpannableString title = new SpannableString("共" + list.size() + "条");
                    title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, String.valueOf(list.size()).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    tvCondition.setText(title);
                } else if (requestPreNumberListHttpRequest.getCode() == BaseCom.LOSELOG || requestPreNumberListHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                    Util.gotoActy(PreOpenRecordActivity.this, LoginActivity.class);
                }
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        Context context;
        List<Number> list;

        public MyAdapter(Context context, List<Number> list) {
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
            viewHolder.tvStatus.setTextColor(context.getResources().getColor(R.color.colorOrange));
//            Log.d("aaa", list.get(position).getNumber());
//            Log.d("aaa", list.get(position).getStatus());
            viewHolder.tvPhone.setText("手机号:  " + list.get(position).getNumber() + "  状态:  ");
            String status = "";
            switch (list.get(position).getPreState()) {
                case 0:
                    status = "待开户";
                    break;
                case 1:
                    status = "已开户";
                    break;

            }
            viewHolder.tvStatus.setText(status);
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
