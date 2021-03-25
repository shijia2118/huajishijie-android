package com.test.tworldapplication.activity.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.Display;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.order.OrderCZDetailNewActivity;
import com.test.tworldapplication.activity.order.OrderKhListActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.OrderEntity;
import com.test.tworldapplication.entity.PostOrderEntity;
import com.test.tworldapplication.entity.PostOrderList;
import com.test.tworldapplication.entity.RequestOrderEntity;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.DoubleDatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class YewuListActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.main_tab1)
    TextView mainTab1;
    @BindView(R.id.main_tab2)
    TextView mainTab2;
    @BindView(R.id.main_tab3)
    TextView mainTab3;
    @BindView(R.id.tvCollection)
    TextView tvCollection;

    @BindView(R.id.main_tab_line)
    View mainTabLine;
    @BindView(R.id.vLine)
    View vLine;
    @BindView(R.id.shadow_view)
    View shadowView;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    private int mTabLineWidth;
    PopupWindow mPopupWindow;
    Holder holder;

    public String strBegin0 = "请选择", strEnd0 = "请选择", strNumber0 = "", strBegin1 = "请选择", strEnd1 = "请选择", strNumber1 = "", strBegin2 = "请选择", strEnd2 = "请选择", strNumber2 = "";
    String strState0 = "请选择";
    Integer page0 = 1, page1 = 1, page2 = 1;
    Integer index = 0;
    MyPagerAdapter adapter;
    ArrayList<TextView> titles = new ArrayList<>();
    ArrayList<Fragment> pages = new ArrayList<>();
    MyFragment fragment0, fragment1, fragment2;

    public String getStrBegin0() {
        return strBegin0;
    }

    public String getStrEnd0() {
        return strEnd0;
    }

    public String getStrNumber0() {
        return strNumber0;
    }

    public String getStrBegin1() {
        return strBegin1;
    }

    public String getStrEnd1() {
        return strEnd1;
    }

    public String getStrNumber1() {
        return strNumber1;
    }

    public String getStrBegin2() {
        return strBegin2;
    }

    public String getStrEnd2() {
        return strEnd2;
    }

    public String getStrNumber2() {
        return strNumber2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yewu_list);
        ButterKnife.bind(this);
        setBackGroundTitle("业务订单", true);
        tvCollection.setVisibility(View.VISIBLE);
        tvCollection.setText("筛选");
        dialog.getTvTitle().setText("正在获取数据");
        initPop();
        initTabline();
        fragment0 = new MyFragment(0, new RefreshListener() {
            @Override
            public void refresh() {
                holder.tvPopBegin.setText("请选择");
                holder.tvPopEnd.setText("请选择");
                holder.etNumber.setText("");
                strBegin0 = "请选择";
                strEnd0 = "请选择";
                strNumber0 = "";
            }
        });
        fragment1 = new MyFragment(1, new RefreshListener() {
            @Override
            public void refresh() {
                holder.tvPopBegin.setText("请选择");
                holder.tvPopEnd.setText("请选择");
                holder.etNumber.setText("");
                strBegin1 = "请选择";
                strEnd1 = "请选择";
                strNumber1 = "";
            }
        });
        fragment2 = new MyFragment(2, new RefreshListener() {
            @Override
            public void refresh() {
                holder.tvPopBegin.setText("请选择");
                holder.tvPopEnd.setText("请选择");
                holder.etNumber.setText("");
                strBegin2 = "请选择";
                strEnd2 = "请选择";
                strNumber2 = "";
            }
        });


        titles.add(mainTab1);
        titles.add(mainTab2);
        titles.add(mainTab3);

        pages.add(fragment0);
        pages.add(fragment1);
        pages.add(fragment2);


        // create new fragments

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOnPageChangeListener(this);
        mainViewpager.setCurrentItem(0);
        titles.get(0).setSelected(true);


//        adapter = new MyAdapter();
//        listview.setAdapter(adapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mainTabLine.getLayoutParams();
        lp.leftMargin = (int) ((position + positionOffset) * mTabLineWidth);
        mainTabLine.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
        index = position;
        for (int i = 0; i < titles.size(); i++) {
            if (position == i) {
                titles.get(i).setSelected(true);
            } else {
                titles.get(i).setSelected(false);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    interface RefreshListener {
        void refresh();
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return pages.get(arg0);
        }
    }

    @SuppressLint("ValidFragment")
    public static class MyFragment extends Fragment {
        @BindView(R.id.listview)
        ListView listview;
        @BindView(R.id.tvCondition)
        TextView tvCondition;
        @BindView(R.id.springview)
        SpringView springView;
        private View parentView;
        List<OrderEntity> adapterOrder = new ArrayList<>();
        MyAdapter adapter;
        Integer index;
        Integer page0 = 1, page1 = 1, page2 = 1;
        RefreshListener refreshListener;

        public MyFragment(Integer index, RefreshListener refreshListener) {
            this.index = index;
            this.refreshListener = refreshListener;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if (parentView == null) {
                parentView = inflater.inflate(R.layout.fragment_yewu, container, false);
                ButterKnife.bind(this, parentView);
                initView();
                //在这里做一些初始化处理
//                initChoiceLayout();
            } else {
                ViewGroup viewGroup = (ViewGroup) parentView.getParent();
                if (viewGroup != null)
                    viewGroup.removeView(parentView);
            }
            return parentView;
        }

        private void initView() {
            adapter = new MyAdapter();
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), OrderCZDetailNewActivity.class);
                    intent.putExtra("from", "4");
                    intent.putExtra("index", index + "");
                    intent.putExtra("data", adapterOrder.get(position));
                    startActivity(intent);
                }
            });

            springView.setType(SpringView.Type.FOLLOW);
            springView.setListener(new SpringView.OnFreshListener() {
                @Override
                public void onRefresh() {
                    switch (index) {
                        case 0:
                            page0 = 1;
                            break;
                        case 1:
                            page1 = 1;
                            break;
                        case 2:
                            page2 = 1;
                            break;
                    }

                    refreshListener.refresh();

                    getData();
                }

                @Override
                public void onLoadmore() {
                    switch (index) {
                        case 0:
                            page0++;
                            break;
                        case 1:
                            page1++;
                            break;
                        case 2:
                            page2++;
                            break;
                    }
                    getData();
                }
            });
            springView.setHeader(new DefaultHeader(getActivity()));   //参数为：logo图片资源，是否显示文字
            springView.setFooter(new DefaultHeader(getActivity()));
            getData();
        }

        public void getData() {
            HttpPost<PostOrderEntity> httpPost = new HttpPost<>();
            PostOrderEntity postOrderEntity = new PostOrderEntity();
            postOrderEntity.setSession_token(Util.getLocalAdmin(getActivity())[0]);
            String strNumber = "";
            String strBegin = "";
            String strEnd = "";
            switch (index) {
                case 0:
                    strNumber = ((YewuListActivity) getActivity()).getStrNumber0();
                    strBegin = ((YewuListActivity) getActivity()).getStrBegin0();
                    strEnd = ((YewuListActivity) getActivity()).getStrEnd0();
                    break;
                case 1:
                    strNumber = ((YewuListActivity) getActivity()).getStrNumber1();
                    strBegin = ((YewuListActivity) getActivity()).getStrBegin1();
                    strEnd = ((YewuListActivity) getActivity()).getStrEnd1();
                    break;
                case 2:
                    strNumber = ((YewuListActivity) getActivity()).getStrNumber2();
                    strBegin = ((YewuListActivity) getActivity()).getStrBegin2();
                    strEnd = ((YewuListActivity) getActivity()).getStrEnd2();
                    break;
            }
            if (Util.isNull(strNumber)) {
                postOrderEntity.setNumber(strNumber);
            }
            if (Util.isNull(strBegin)) {
                postOrderEntity.setStartDate(strBegin);
            }
            if (Util.isNull(strEnd)) {
                postOrderEntity.setEndDate(strEnd);
            }
            postOrderEntity.setType(index + 1);
            switch (index) {
                case 0:
                    postOrderEntity.setPage(page0);
                    break;
                case 1:
                    postOrderEntity.setPage(page1);
                    break;
                case 2:
                    postOrderEntity.setPage(page2);
                    break;
            }

            postOrderEntity.setLinage(10);
            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost.setParameter(postOrderEntity);
            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postOrderEntity) + BaseCom.APP_PWD));

            new OrderHttp()._2019QueryOrders(new Subscriber<HttpRequest<RequestOrderEntity>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpRequest<RequestOrderEntity> requestOrderEntityHttpRequest) {
                    springView.onFinishFreshAndLoad();

                    if (requestOrderEntityHttpRequest.getCode() == BaseCom.NORMAL || requestOrderEntityHttpRequest.getCode() == 39997) {
                        switch (index) {
                            case 0:
                                if (page0 == 1)
                                    adapterOrder.clear();
                                break;
                            case 1:
                                if (page1 == 1)
                                    adapterOrder.clear();
                                break;
                            case 2:
                                if (page2 == 1)
                                    adapterOrder.clear();
                                break;
                        }
                        if (requestOrderEntityHttpRequest.getData() != null && requestOrderEntityHttpRequest.getData().getOrders() != null) {
                            adapterOrder.addAll(requestOrderEntityHttpRequest.getData().getOrders());
                            SpannableString title = new SpannableString("共" + requestOrderEntityHttpRequest.getData().getCount() + "条");
                            title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, String.valueOf(requestOrderEntityHttpRequest.getData().getCount()).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvCondition.setText(title);
                        } else {
                            SpannableString title = new SpannableString("共0条");
                            title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorOrange)), 1, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            tvCondition.setText(title);
                        }

                        adapter.notifyDataSetChanged();


                    } else
                        Toast.makeText(getActivity(), requestOrderEntityHttpRequest.getMes() + "", Toast.LENGTH_SHORT).show();
                }
            }, httpPost);

        }

        class MyAdapter extends BaseAdapter {
            LayoutInflater layoutInflater;

            public MyAdapter() {
                layoutInflater = LayoutInflater.from(getActivity());
            }

            @Override
            public int getCount() {
                return adapterOrder.size();
            }

            @Override
            public Object getItem(int position) {
                return adapterOrder.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder viewHolder = null;
                if (convertView == null) {
                    convertView = layoutInflater.inflate(R.layout.item_yewu, null);
                    viewHolder = new ViewHolder(convertView);
                    convertView.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) convertView.getTag();
                }
                viewHolder.tvTime.setText(adapterOrder.get(position).getCreateDate());
                viewHolder.tvStatus.setText(adapterOrder.get(position).getStatusName());
                viewHolder.tvName.setText(adapterOrder.get(position).getProductName());
                viewHolder.tvAmount.setText(adapterOrder.get(position).getOrderAmount() + "元");
                viewHolder.tvPhone.setText(adapterOrder.get(position).getNumber());
                if (index == 0) {
                    viewHolder.tvAmount.setVisibility(View.VISIBLE);
                    viewHolder.tvName.setVisibility(View.GONE);
                } else {
                    viewHolder.tvAmount.setVisibility(View.GONE);
                    viewHolder.tvName.setVisibility(View.VISIBLE);
                }
                return convertView;
            }


        }

        class ViewHolder {
            @BindView(R.id.tvTime)
            TextView tvTime;
            @BindView(R.id.tvStatus)
            TextView tvStatus;
            @BindView(R.id.tvName)
            TextView tvName;
            @BindView(R.id.tvAmount)
            TextView tvAmount;
            @BindView(R.id.tvPhone)
            TextView tvPhone;

            public ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }

    }


    private void initPop() {
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View music_popunwindwow = mLayoutInflater.inflate(
                R.layout.view_pop_condition, null);
        holder = new Holder(music_popunwindwow);
        holder.llState.setVisibility(View.GONE);
        holder.vLine1.setVisibility(View.GONE);

        mPopupWindow = new PopupWindow(music_popunwindwow, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white1));
    }

    private void initTabline() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        mTabLineWidth = outMetrics.widthPixels / 3;
        ViewGroup.LayoutParams lp = mainTabLine.getLayoutParams();
        lp.width = mTabLineWidth;
        mainTabLine.setLayoutParams(lp);
    }

    private void showDatePikerDialog(final int click_flag, final TextView textView) {
        final Calendar calendar = Calendar.getInstance();
        DoubleDatePickerDialog dialog = new DoubleDatePickerDialog(YewuListActivity.this, "选择日期", 0, new DoubleDatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth,
                                  DatePicker endDatePicker, int endYear, int endMonthOfYear, int endDayOfMonth) {
                String date = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);
                textView.setText(date);

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), true);
        dialog.show();
    }


    class Holder {
        @BindView(R.id.tvReset)
        TextView tvReset;
        @BindView(R.id.tvPopBegin)
        TextView tvPopBegin;
        @BindView(R.id.llBegin)
        LinearLayout llBegin;
        @BindView(R.id.tvPopEnd)
        TextView tvPopEnd;
        @BindView(R.id.llEnd)
        LinearLayout llEnd;
        @BindView(R.id.tvTitleState)
        TextView tvTitleState;
        @BindView(R.id.tvState)
        TextView tvState;


        @BindView(R.id.vLine1)
        View vLine1;
        @BindView(R.id.llState)
        LinearLayout llState;
        @BindView(R.id.etNumber)
        EditText etNumber;
        @BindView(R.id.tvInquery)
        TextView tvInquery;

        @BindView(R.id.linePhoneNumber)
        View linePhoneNumber;
        @BindView(R.id.llPhoneNumber)
        RelativeLayout llPhoneNumber;

        Holder(View view) {
            ButterKnife.bind(this, view);
        }


        @OnClick({R.id.llBegin, R.id.llEnd, R.id.llState, R.id.tvReset, R.id.tvInquery})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.llBegin:
                    if (!Util.isFastDoubleClick())
                        showDatePikerDialog(0, tvPopBegin);
                    break;
                case R.id.llEnd:
                    if (!Util.isFastDoubleClick())
                        showDatePikerDialog(1, tvPopEnd);
                    break;
                case R.id.llState:

                    break;
                case R.id.tvReset:
                    switch (index) {
                        case 0:
                            page0 = 1;
                            break;
                        case 1:
                            page1 = 1;
                            break;
                        case 2:
                            page2 = 1;
                            break;
                    }

                    tvPopBegin.setText("请选择");
                    tvPopEnd.setText("请选择");
                    etNumber.setText("");
                    tvState.setText("请选择");

                    break;
                case R.id.tvInquery:
                    String strBegin = tvPopBegin.getText().toString().trim();
                    String strEnd = tvPopEnd.getText().toString().trim();
                    if (Util.compare(strEnd, strBegin)) {
                        Util.createToast(YewuListActivity.this, "请输入正确的起止日期");
                    } else {
                        switch (index) {
                            case 0:
                                page0 = 1;
                                break;
                            case 1:
                                page1 = 1;
                                break;
                            case 2:
                                page2 = 1;
                                break;
                        }

                        mPopupWindow.dismiss();
                        switch (index) {
                            case 0:
                                strNumber0 = etNumber.getText().toString().trim();
                                strBegin0 = strBegin;
                                strEnd0 = strEnd;
                                fragment0.getData();
                                break;
                            case 1:
                                strBegin1 = strBegin;
                                strEnd1 = strEnd;
                                strNumber1 = etNumber.getText().toString().trim();
                                fragment1.getData();
                                break;
                            case 2:
                                strBegin2 = strBegin;
                                strEnd2 = strEnd;
                                strNumber2 = etNumber.getText().toString().trim();
                                fragment2.getData();
                                break;
                        }

                    }


                    break;
            }
        }
    }


    @OnClick({R.id.tvCollection, R.id.main_tab1, R.id.main_tab2, R.id.main_tab3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvCollection:
                shadowView.setVisibility(View.VISIBLE);
                switch (index) {
                    case 0:
                        holder.tvPopBegin.setText(strBegin0);
                        holder.tvPopEnd.setText(strEnd0);
                        holder.etNumber.setText(strNumber0);
                        holder.etNumber.setSelection(strNumber0.length());
                        break;
                    case 1:
                        holder.tvPopBegin.setText(strBegin1);
                        holder.tvPopEnd.setText(strEnd1);
                        holder.etNumber.setText(strNumber1);
                        holder.etNumber.setSelection(strNumber1.length());
                        break;
                    case 2:
                        holder.tvPopBegin.setText(strBegin2);
                        holder.tvPopEnd.setText(strEnd2);
                        holder.etNumber.setText(strNumber2);
                        holder.etNumber.setSelection(strNumber2.length());
                        break;
                }
                mPopupWindow.showAsDropDown(vLine, 0, 0);
                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                contentView.setAlpha((float) 1);
                        shadowView.setVisibility(View.GONE);
                    }
                });
                break;
            case R.id.main_tab1:
                index = 0;
                mainViewpager.setCurrentItem(0, true);
                break;
            case R.id.main_tab2:
                index = 1;
                mainViewpager.setCurrentItem(1, true);
                break;
            case R.id.main_tab3:
                index = 2;
                mainViewpager.setCurrentItem(2, true);
                break;
        }
//        contentView.setAlpha((float) 0.2);

    }


}
