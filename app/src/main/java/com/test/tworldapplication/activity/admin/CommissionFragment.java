package com.test.tworldapplication.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostGetAccountPeriodList;
import com.test.tworldapplication.entity.RequestGetAccountPeriodList;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommissionFragment extends Fragment {
    private int i;
    private View view;
    MyAdapter myAdapter;
    List<RequestGetAccountPeriodList.AccountPeriodBean> list = new ArrayList<>();
    int page = 1;
    int per_page = 10;
    @BindView(R.id.comm_list_rv)
    RecyclerView recyclerView;
    @BindView(R.id.springView)
    SpringView springView;
    CommissionListActivity listActivity;
    String sum;
    String dataStr="";
    protected boolean isCreated = false;
    protected boolean isFirst = true;

    public static CommissionFragment newInstance(int i){
        Bundle bundle = new Bundle();
        bundle.putInt("i",i);
        CommissionFragment fragment = new CommissionFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_commission_list, container, false);
        ButterKnife.bind(this,view);
        listActivity= (CommissionListActivity) getActivity();
        Bundle bundle = getArguments();
        i = bundle.getInt("i");
        Log.d("收到i",i+"");
//        if(i==0){
//            i=1;
//        }
//        else if(i==1){
//            i=0;
//        }

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
        springView.setHeader(new DefaultHeader(CommissionFragment.this.getActivity()));   //参数为：logo图片资源，是否显示文字
        springView.setFooter(new DefaultFooter(CommissionFragment.this.getActivity()));
        myAdapter = new MyAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(myAdapter);
        getData();
        isCreated = true;


        return view;
    }


    //这个方法会在onCreate之前执行
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (!isCreated) {
            return;
        }

        if (isVisibleToUser) {
//            page = 1;
//            getData();

            if(listActivity.dateStr!=null){
               if(dataStr==null){
                   dataStr=listActivity.dateStr;
                   page = 1;
                   getData();
               }
               if(!dataStr.equals(listActivity.dateStr)){
                   page = 1;
                   getData();
               }

            }
            setSum();
        }
    }

    public void setSum(){
        ((CommissionListActivity)getActivity()).setSum(sum);
    }
    public void getData() {
        dataStr = listActivity.dateStr;
        HttpPost<PostGetAccountPeriodList> httpPost = new HttpPost<>();
        PostGetAccountPeriodList postCheck = new PostGetAccountPeriodList();
        postCheck.setSession_token(Util.getLocalAdmin(CommissionFragment.this.getActivity())[0]);
        postCheck.setPage(page+"");
        postCheck.setLinage(per_page+"");
        postCheck.setType(i+"");
        postCheck.setAccountPeriod(dataStr);
        httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost.setParameter(postCheck);
        httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + new Gson().toJson(postCheck) + BaseCom.APP_PWD));
        new AdminHttp().getAccountPeriodList(AdminRequest.getAccountPeriodList(CommissionFragment.this.getActivity(), new SuccessValue<RequestGetAccountPeriodList>() {
            @Override
            public void OnSuccess(RequestGetAccountPeriodList value) {
                if (page == 1) {
                    list.clear();
                }
                springView.onFinishFreshAndLoad();
                if(i == 2){
                    sum = "-"+value.getAmountSUM();
                }else {
                    sum = "+"+value.getAmountSUM();
                }
                if(i == 0&&isFirst){
                   setSum();
                   isFirst = false;
                }
                list.addAll(value.getAccountPeriod());
                myAdapter.notifyDataSetChanged();
            }
        }), httpPost);
    }
    class MyAdapter extends RecyclerView.Adapter<ViewHolder>{
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.commissionlist_item,parent,false);
            return new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.tel.setText(list.get(position).getTel());
            String amount;
            if(i == 2){
                amount = "-" + list.get(position).getAmount();
            }else {
                amount = "+" + list.get(position).getAmount();
            }
            holder.amount.setText(amount);
            holder.accountPeriod.setText(list.get(position).getOpenTime());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(CommissionFragment.this.getActivity(), CommissionDetailActivity.class);
                    intent.putExtra("id",list.get(holder.getAdapterPosition()).getId());
                    if(i == 2){
                        intent.putExtra("kou",true);
                    }
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
        @BindView(R.id.tel)
        TextView tel;

        @BindView(R.id.amount)
        TextView amount;

        @BindView(R.id.accountPeriod)
        TextView accountPeriod;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }



}
