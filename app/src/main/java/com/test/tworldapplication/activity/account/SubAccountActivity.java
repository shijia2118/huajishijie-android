package com.test.tworldapplication.activity.account;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostGetSonOrderList;
import com.test.tworldapplication.entity.RequestGetSonOrderList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OrderRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubAccountActivity extends BaseActivity {

    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.shadow_view)
    View shadowView;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.springView)
    SpringView springView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    String code;
    String startTime;
    String endTime;
    String number;
    String username;
    Holder holder;
    PopupWindow mPopupWindow;
    MyAdapter myAdapter;
    Calendar calendar = Calendar.getInstance();
    TimePickerView pvTime;
    TimePickerView pvTime2;

    List<RequestGetSonOrderList.order> list = new ArrayList<>();
    int page = 1;
    int per_page = 10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_account);
        ButterKnife.bind(this);
        setBackGroundTitle("子账户开户列表", true);
        tvCollection.setText("筛选");
        tvCollection.setVisibility(View.VISIBLE);
        getData();
        springView.setType(SpringView.Type.FOLLOW);
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                getData();
            }

            @Override
            public void onLoadmore() {
                page++;
                getData();
            }
        });
        springView.setHeader(new DefaultHeader(SubAccountActivity.this));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultHeader(SubAccountActivity.this));

        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition_eight, null);
        holder = new Holder(music_popunwindwow);
        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                springView.setVisibility(View.VISIBLE);
                shadowView.setVisibility(View.GONE);
            }
        });

    }


    private void getData() {
        HttpPost<PostGetSonOrderList> httpPost = new HttpPost<>();
        PostGetSonOrderList postCheck = new PostGetSonOrderList();
        postCheck.setSession_token(Util.getLocalAdmin(SubAccountActivity.this)[0]);
        postCheck.setPage(page+"");
        postCheck.setLinage(per_page+"");
        postCheck.setNumber(number);
        postCheck.setUsername(username);
        postCheck.setStartTime(startTime);
        postCheck.setEndTime(endTime);
        postCheck.setOrderStatusCode(code);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postCheck);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postCheck) + BaseCom.APP_PWD));
        new OrderHttp().getSonOrderList(OrderRequest.getSonOrderList(SubAccountActivity.this, dialog, new SuccessValue<RequestGetSonOrderList>() {
            @Override
            public void OnSuccess(RequestGetSonOrderList value) {
                springView.onFinishFreshAndLoad();
                if (page == 1) {
                    list.clear();
                }
                list.addAll(value.getOrder());
                myAdapter.notifyDataSetChanged();
            }
        }), httpPost);

    }


    @OnClick(R.id.tvCollection)
    public void onClick() {
        if(!mPopupWindow.isShowing()){
            springView.setVisibility(View.INVISIBLE);
            shadowView.setVisibility(View.VISIBLE);
            mPopupWindow.showAsDropDown(vLine, 0, 0);

        }else {
            mPopupWindow.dismiss();
        }

    }



    @Override
    protected void onResume() {
        super.onResume();


    }
    class Holder {
        @BindView(R.id.tv0)
        TextView tv0;
        @BindView(R.id.tv1)
        TextView tv1;
        @BindView(R.id.tv2)
        TextView tv2;



        @BindView(R.id.etNumber)
        EditText etNumber;
        @BindView(R.id.etChannel)
        EditText etChannel;


        @BindView(R.id.tvStart)
        TextView tvStart;
        @BindView(R.id.tvEnd)
        TextView tvEnd;


        public Holder(View view) {
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.tv0, R.id.tv1, R.id.tv2, R.id.tvInquery, R.id.tvReset, R.id.tvStart, R.id.tvEnd})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv0:
                    code = null;
                    tv0.setTextColor(getResources().getColor(R.color.colorOrange));
                    tv1.setTextColor(getResources().getColor(R.color.colorGray1));
                    tv2.setTextColor(getResources().getColor(R.color.colorGray1));
                    break;
                case R.id.tv1:
                    code = "PENDING";
                    tv1.setTextColor(getResources().getColor(R.color.colorOrange));
                    tv0.setTextColor(getResources().getColor(R.color.colorGray1));
                    tv2.setTextColor(getResources().getColor(R.color.colorGray1));
                    break;
                case R.id.tv2:
                    code = "SUCCESS";
                    tv2.setTextColor(getResources().getColor(R.color.colorOrange));
                    tv0.setTextColor(getResources().getColor(R.color.colorGray1));
                    tv1.setTextColor(getResources().getColor(R.color.colorGray1));
                    break;
                case R.id.tvStart:
                    if(pvTime == null){
                        pvTime = new TimePickerBuilder(SubAccountActivity.this, new OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date, View v) {
                                SimpleDateFormat formatStart = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                                SimpleDateFormat formatStart1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                                tvStart.setText(formatStart.format(date));
                                startTime = formatStart1.format(date);

                            }
                        }).setType(new boolean[]{true, true, true, false, false, false})
                                .setRangDate(null, calendar)
                                .build();
                    }
                    pvTime.show();
                    pvTime.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            tvCollection.performClick();
                        }
                    });

                    break;
                case R.id.tvEnd:
                    if(pvTime2==null){
                        pvTime2 = new TimePickerBuilder(SubAccountActivity.this, new OnTimeSelectListener() {
                            @Override
                            public void onTimeSelect(Date date, View v) {
                                SimpleDateFormat formatEnd = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
                                SimpleDateFormat formatEnd1 = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                                tvEnd.setText(formatEnd.format(date));
                                endTime = formatEnd1.format(date);

                            }
                        }).setType(new boolean[]{true, true, true, false, false, false})
                                .setRangDate(null, calendar)
                                .build();
                    }

                    pvTime2.show();
                    pvTime2.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(Object o) {
                            tvCollection.performClick();
                        }
                    });


                    break;
                case R.id.tvInquery:
//                    if(TextUtils.isEmpty(etNumber.getText())||TextUtils.isEmpty(etChannel.getText())
//                            ||TextUtils.isEmpty(tvStart.getText())||TextUtils.isEmpty(tvEnd.getText())){
//                        Toast.makeText(SubAccountActivity.this, "请输入完整信息", Toast.LENGTH_LONG).show();
//                    }else if(etNumber.length()!=11){
//                        Toast.makeText(SubAccountActivity.this, "请输入正确的手机号", Toast.LENGTH_LONG).show();
//                    }
//                    else {
                        number = etNumber.getText().toString();
                        if(TextUtils.isEmpty(number))
                            number = null;
                        username = etChannel.getText().toString();
                        if(TextUtils.isEmpty(username))
                            username = null;
                        page = 1;
                        getData();
                        mPopupWindow.dismiss();
                   // }

                    break;
                case R.id.tvReset:
                    tvStart.setText(null);
                    tvEnd.setText(null);
                    etChannel.setText(null);
                    etNumber.setText(null);
                    startTime = null;
                    endTime = null;
                    onClick(tv0);
                    break;
            }
        }
    }


    class MyAdapter extends RecyclerView.Adapter<ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subaccount_item,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.orderNo.setText(list.get(position).getOrderNo());
            holder.number.setText(list.get(position).getNumber());
            holder.acceptUser.setText(list.get(position).getAcceptUser());
            holder.statusName.setText(list.get(position).getStatusName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(SubAccountActivity.this, SubAccountDetailActivity.class);
                    intent.putExtra("orderNum",list.get(holder.getAdapterPosition()).getOrderNo());
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.orderNo)
        TextView orderNo;

        @BindView(R.id.number)
        TextView number;

        @BindView(R.id.acceptUser)
        TextView acceptUser;

        @BindView(R.id.statusName)
        TextView statusName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
