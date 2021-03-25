package com.test.tworldapplication.activity.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.PayResultActivity;
import com.test.tworldapplication.activity.admin.PayPassCreateActivity;
import com.test.tworldapplication.activity.card.ReplaceCardActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Discount;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostBalanceQuery;
import com.test.tworldapplication.entity.PostCaptcha;
import com.test.tworldapplication.entity.PostCode;
import com.test.tworldapplication.entity.PostNumberCheck;
import com.test.tworldapplication.entity.PostNumberSegment;
import com.test.tworldapplication.entity.PostOtherDiscount;
import com.test.tworldapplication.entity.PostPsdCheck;
import com.test.tworldapplication.entity.PostQueryBalance;
import com.test.tworldapplication.entity.PostQueryProduct;
import com.test.tworldapplication.entity.PostRecharge;
import com.test.tworldapplication.entity.PostRechargeDiscount;
import com.test.tworldapplication.entity.PostServiceProduct;
import com.test.tworldapplication.entity.ProductBeanDetail;
import com.test.tworldapplication.entity.ProductListEntity;
import com.test.tworldapplication.entity.RequestBalanceQuery;
import com.test.tworldapplication.entity.RequestCode;
import com.test.tworldapplication.entity.RequestNumberSegment;
import com.test.tworldapplication.entity.RequestOtherDiscount;
import com.test.tworldapplication.entity.RequestPsdCheck;
import com.test.tworldapplication.entity.RequestQueryBalance;
import com.test.tworldapplication.entity.RequestRecharge;
import com.test.tworldapplication.entity.RequestRechargeDiscount;
import com.test.tworldapplication.entity.RequestServiceProduct;
import com.test.tworldapplication.http.AccountHttp;
import com.test.tworldapplication.http.AccountRequest;
import com.test.tworldapplication.http.AdminHttp;
import com.test.tworldapplication.http.AdminRequest;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.OnPasswordInputFinish;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.BitmapUtil;
import com.test.tworldapplication.utils.CountDownTimerUtils;
import com.test.tworldapplication.utils.EventBusCarrier;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.CheckResultDialog;
import com.test.tworldapplication.view.PasswordPop;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class YewubanliActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.main_tab1)
    TextView mainTab1;
    @BindView(R.id.main_tab2)
    TextView mainTab2;
    @BindView(R.id.main_tab3)
    TextView mainTab3;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    @BindView(R.id.main_tab_line)
    View mainTabLine;
    private int mTabLineWidth;
    ArrayList<TextView> titles = new ArrayList<>();
    ArrayList<Fragment> pages = new ArrayList<>();
    List<ProductBeanDetail> productBeanDetails = new ArrayList<>();
    Double balance;

    MyFragment fragment0, fragment1, fragment2;
    MyAdapter adapter;
    static int tab0Finish, tab1Finish, tab2Finish;
    static Activity activity;

    public List<ProductBeanDetail> getProductBeanDetails() {
        return productBeanDetails;
    }

    public Double getBalance() {
        return balance;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(EventBusCarrier carrier) {
        if (carrier.getEventType().equals("444"))
            AppManager.getAppManager().finishActivity();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yewubanli);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        setBackGroundTitle("业务办理", true);
        activity = YewubanliActivity.this;
        tab0Finish = 0;
        tab1Finish = 0;
        tab2Finish = 0;
        dialog.getTvTitle().setText("正在获取数据");
        mainTab1.setOnClickListener(this);
        mainTab2.setOnClickListener(this);
        mainTab3.setOnClickListener(this);
        fragment0 = new MyFragment("tab1", new CreateFinishListener() {
            @Override
            public void createFinish() {
                tab0Finish = 1;

            }
        });
        fragment1 = new MyFragment("tab2", new CreateFinishListener() {
            @Override
            public void createFinish() {
                tab1Finish = 1;
            }
        });
        fragment2 = new MyFragment("tab3", new CreateFinishListener() {
            @Override
            public void createFinish() {
                tab2Finish = 1;
            }
        });


        titles.add(mainTab1);
        titles.add(mainTab2);
        titles.add(mainTab3);

        pages.add(fragment0);
        pages.add(fragment1);
        pages.add(fragment2);


        // create new fragments

        adapter = new MyAdapter(getSupportFragmentManager());
        initView();
//        getData();
    }

    private void initView() {
        mainViewpager.setAdapter(adapter);
        mainViewpager.setOnPageChangeListener(this);
        mainViewpager.setCurrentItem(0);
        titles.get(0).setSelected(true);

        initTabline();

//        initData();
//        initAmount();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_tab1:
                mainViewpager.setCurrentItem(0, true);
                break;
            case R.id.main_tab2:
                mainViewpager.setCurrentItem(1, true);
                break;
            case R.id.main_tab3:
                mainViewpager.setCurrentItem(2, true);
                break;
            default:
                break;
        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mainTabLine.getLayoutParams();
        lp.leftMargin = (int) ((position + positionOffset) * mTabLineWidth);
        mainTabLine.setLayoutParams(lp);
    }

    @Override
    public void onPageSelected(int position) {
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

    private class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
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

    interface CreateFinishListener {
        void createFinish();
    }


    @SuppressLint("ValidFragment")
    public static class MyFragment extends Fragment {
        @BindView(R.id.tvBalance)
        TextView tvBalance;
        @BindView(R.id.etNumber)
        EditText etNumber;
        @BindView(R.id.ll_input)
        RelativeLayout llInput;
        @BindView(R.id.gvMoney)
        GridView gvMoney;
        int pos;
        GvAdapter adapter;
        @BindView(R.id.ll_activity)
        View llActivity;
        @BindView(R.id.tvDiscount)
        TextView tvDiscount;
        @BindView(R.id.tvMoney)
        TextView tvMoney;
        @BindView(R.id.ll_Other)
        LinearLayout llOther;
        @BindView(R.id.tvNext)
        TextView tvNext;
        @BindView(R.id.tvToast)
        TextView tvToast;
        @BindView(R.id.etPhone0)
        EditText etPhone0;
        @BindView(R.id.etPhone1)
        EditText etPhone1;
        @BindView(R.id.etCode0)
        EditText etCode0;
        @BindView(R.id.etCode1)
        EditText etCode1;
        @BindView(R.id.tvGetCode0)
        TextView tvGetCode0;
        @BindView(R.id.tvGetCode1)
        TextView tvGetCode1;
        @BindView(R.id.tvGetList0)
        TextView tvGetList0;
        @BindView(R.id.tvGetList1)
        TextView tvGetList1;

        @BindView(R.id.llTab0)
        LinearLayout llTab0;
        @BindView(R.id.llTab1)
        LinearLayout llTab1;
        @BindView(R.id.llTab2)
        LinearLayout llTab2;

        PasswordPop addPopWindow;
        String strMoney = "";
        @BindView(R.id.ll_selection)
        LinearLayout llSelection;

        List<ProductBeanDetail> productBeanDetails = new ArrayList<>();
        ProductBeanDetail chooseProductBeanDetail;

        String text;
        CheckResultDialog dialog;
        String strInputMoney;
        CreateFinishListener createFinishListener;

        public MyFragment(String text, CreateFinishListener createFinishListener) {
            this.text = text;
            this.createFinishListener = createFinishListener;

        }

        private void initData() {
            HttpPost<PostQueryProduct> httpPost = new HttpPost<>();
            PostQueryProduct postQueryProduct = new PostQueryProduct();
            postQueryProduct.setSession_token(Util.getLocalAdmin(YewubanliActivity.activity)[0]);
            postQueryProduct.setType(1);
            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost.setParameter(postQueryProduct);
            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postQueryProduct) + BaseCom.APP_PWD));
            new AccountHttp()._2019QueryProducts(new Subscriber<HttpRequest<ProductListEntity>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpRequest<ProductListEntity> productListEntityHttpRequest) {
                    Util.createToast(YewubanliActivity.activity, productListEntityHttpRequest.getMes());
                    productBeanDetails.clear();
                    if (productListEntityHttpRequest.getCode() == BaseCom.NORMAL) {
                        productBeanDetails.addAll(productListEntityHttpRequest.getData().getProducts().getVoiceProds().get(0).getProds());
                        if (productBeanDetails.size() > 0) {
                            llSelection.setVisibility(View.VISIBLE);
                            adapter = new MyFragment.GvAdapter();
                            gvMoney.setAdapter(adapter);
                            chooseProductBeanDetail = productBeanDetails.get(0);
                            strMoney = String.valueOf(productBeanDetails.get(0).getOrderAmount());
                        } else {
                            llSelection.setVisibility(View.GONE);
                        }


                    } else if (productListEntityHttpRequest.getCode() == BaseCom.LOSELOG || productListEntityHttpRequest.getCode() == BaseCom.VERSIONINCORRENT)
                        Util.gotoActy(YewubanliActivity.activity, LoginActivity.class);
                }
            }, httpPost);
        }

        public void initAmount() {
            HttpPost<PostBalanceQuery> httpPost1 = new HttpPost<>();
            PostBalanceQuery postBalanceQuery = new PostBalanceQuery();
            postBalanceQuery.setSession_token(Util.getLocalAdmin(YewubanliActivity.activity)[0]);
            httpPost1.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
            httpPost1.setParameter(postBalanceQuery);
            httpPost1.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + new Gson().toJson(postBalanceQuery) + BaseCom.APP_PWD));
            new AccountHttp().balanceQuery(new Subscriber<HttpRequest<RequestBalanceQuery>>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(HttpRequest<RequestBalanceQuery> requestBalanceQueryHttpRequest) {
                    if (requestBalanceQueryHttpRequest.getCode() == BaseCom.NORMAL) {
                        Toast.makeText(YewubanliActivity.activity, requestBalanceQueryHttpRequest.getMes(), Toast.LENGTH_SHORT).show();
                        tvBalance.setText(requestBalanceQueryHttpRequest.getData().getBalance() + "元");
                    } else if (requestBalanceQueryHttpRequest.getCode() == BaseCom.LOSELOG || requestBalanceQueryHttpRequest.getCode() == BaseCom.VERSIONINCORRENT) {
                        Util.gotoActy(YewubanliActivity.activity, LoginActivity.class);
                    }
                }
            }, httpPost1);

//            new AccountHttp().balanceQuery(AccountRequest.balanceQuery(new SuccessValue<HttpRequest<RequestBalanceQuery>>() {
//                @Override
//                public void OnSuccess(HttpRequest<RequestBalanceQuery> value) {
//                    Toast.makeText(YewubanliActivity.activity, value.getMes(), Toast.LENGTH_SHORT).show();
//                    tvBalance.setText(value.getData().getBalance() + "元");
//                }
//            }), httpPost1);


        }


        private View parentView;

        private void initView() {
            switch (text) {
                case "tab1":
                    llTab0.setVisibility(View.VISIBLE);
                    llTab1.setVisibility(View.GONE);
                    llTab2.setVisibility(View.GONE);
                    initData();
                    initAmount();
                    break;
                case "tab2":
                    llTab0.setVisibility(View.GONE);
                    llTab1.setVisibility(View.VISIBLE);
                    llTab2.setVisibility(View.GONE);
                    break;
                case "tab3":
                    llTab0.setVisibility(View.GONE);
                    llTab1.setVisibility(View.GONE);
                    llTab2.setVisibility(View.VISIBLE);
                    break;
            }
            dialog = new CheckResultDialog(YewubanliActivity.activity, R.style.CustomDialog);
//        dialog.setFlag(1);
            dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            etNumber.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.toString().length() == 11) {
                        dialog.getTvTitle().setText("正在查询");
                        dialog.show();
                        HttpPost<PostQueryBalance> httpPost = new HttpPost<>();
                        PostQueryBalance postQueryBalance = new PostQueryBalance();
                        postQueryBalance.setSession_token(Util.getLocalAdmin(YewubanliActivity.activity)[0]);
                        postQueryBalance.setNumber(s.toString());
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postQueryBalance) + BaseCom.APP_PWD));
                        httpPost.setParameter(postQueryBalance);
                        new AccountHttp().queryBalance(AccountRequest.queryBalance(YewubanliActivity.activity, dialog, new SuccessValue<HttpRequest<RequestQueryBalance>>() {
                            @Override
                            public void OnSuccess(HttpRequest<RequestQueryBalance> value) {
                                Util.createToast(YewubanliActivity.activity, value.getMes());
                                switch (value.getCode()) {
                                    case BaseCom.NORMAL:
                                        tvToast.setText("话费余额: " + value.getData().getMoney() + "元");
                                        tvToast.setVisibility(View.VISIBLE);
                                        break;
                                    case BaseCom.LOSELOG:
                                    case BaseCom.VERSIONINCORRENT:
                                        Util.gotoActy(YewubanliActivity.activity, LoginActivity.class);
                                        break;
                                    default:
                                        tvToast.setVisibility(View.GONE);
                                        break;
                                }

                            }
                        }), httpPost);
                    }
                }
            });
            gvMoney.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    pos = position;
                    chooseProductBeanDetail = productBeanDetails.get(position);
                    strMoney = String.valueOf(productBeanDetails.get(position).getOrderAmount());
                    adapter.notifyDataSetChanged();
                    tvMoney.setText("");
                    tvDiscount.setText("");
                    tvMoney.setVisibility(View.GONE);
                    tvDiscount.setVisibility(View.GONE);
                }
            });


        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            if (parentView == null) {
                parentView = inflater.inflate(R.layout.fragment_yw_banli, container, false);
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

        @OnClick({R.id.ll_Other, R.id.tvNext, R.id.tvGetCode0, R.id.tvGetCode1, R.id.tvGetList0, R.id.tvGetList1})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ll_Other:
                    pos = 6;
                    adapter.notifyDataSetChanged();
                    tvMoney.setText("");
                    tvDiscount.setText("");
                    final EditText editText = new EditText(YewubanliActivity.activity);
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    new AlertDialog.Builder(YewubanliActivity.activity).setTitle("请输入充值金额").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (editText.getText().toString().equals("") || editText.getText().toString().equals("0")) {
                                Util.createToast(YewubanliActivity.activity, "请输入有效金额");
                            } else if (Integer.valueOf(editText.getText().toString()) > 1000) {
                                Util.createToast(YewubanliActivity.activity, "单笔充值金额应小于1000.00元");
                            } else {
                                dialog.dismiss();
                                strInputMoney = editText.getText().toString();
                                search(strInputMoney);
                            }

                        }
                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                    break;
                case R.id.tvNext:
                    if (!Util.isFastDoubleClick()) {
                        recharge();
                    }
                    break;
                case R.id.tvGetCode0:
                    String strEtPhone0 = etPhone0.getText().toString();
                    if (strEtPhone0.length() != 11) {
                        Util.createToast(YewubanliActivity.activity, "请输入正确的手机号码!");
                    } else {
                        dialog.getTvTitle().setText("正在校验号码");
                        dialog.show();
                        HttpPost<PostNumberSegment> httpPost = new HttpPost<>();
                        PostNumberSegment postNumberSegment = new PostNumberSegment();
                        postNumberSegment.setSession_token(Util.getLocalAdmin(getActivity())[0]);
                        postNumberSegment.setTel(etPhone0.getText().toString());
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postNumberSegment);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postNumberSegment) + BaseCom.APP_PWD));
                        new CardHttp().numberSegment(CardRequest.numberSegment(new SuccessValue<HttpRequest<RequestNumberSegment>>() {
                            @Override
                            public void OnSuccess(HttpRequest<RequestNumberSegment> value) {
                                dialog.dismiss();
                                if (value.getCode() == BaseCom.NORMAL) {
                                    dialog.getTvTitle().setText("正在获取验证码");
                                    dialog.show();
                                    HttpPost<PostCode> httpPost = new HttpPost<PostCode>();
                                    PostCode postCode = new PostCode();
                                    postCode.setTel(etPhone0.getText().toString());
                                    postCode.setCaptcha_type("6");
                                    postCode.setSession_token(Util.getLocalAdmin(getActivity())[0]);
                                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postCode) + BaseCom.APP_PWD));
                                    httpPost.setParameter(postCode);
                                    new AdminHttp().getCode(new Subscriber<HttpRequest<RequestCode>>() {
                                        @Override
                                        public void onCompleted() {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onNext(HttpRequest<RequestCode> requestCodeHttpRequest) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), requestCodeHttpRequest.getMes() + "", Toast.LENGTH_SHORT).show();
                                            if (requestCodeHttpRequest.getCode() == BaseCom.NORMAL) {
                                                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetCode0, 60000, 1000);
                                                mCountDownTimerUtils.start();
                                            }
                                        }
                                    }, httpPost);
                                } else
                                    Toast.makeText(getActivity(), value.getMes() + "", Toast.LENGTH_SHORT).show();

                            }
                        }), httpPost);

                    }

                    break;
                case R.id.tvGetCode1:
                    String strEtPhone1 = etPhone1.getText().toString();
                    if (strEtPhone1.length() != 11) {
                        Util.createToast(YewubanliActivity.activity, "请输入正确的手机号码!");
                    } else {
                        dialog.getTvTitle().setText("正在校验号码");
                        dialog.show();
                        HttpPost<PostNumberSegment> httpPost = new HttpPost<>();
                        PostNumberSegment postNumberSegment = new PostNumberSegment();
                        postNumberSegment.setSession_token(Util.getLocalAdmin(getActivity())[0]);
                        postNumberSegment.setTel(etPhone1.getText().toString());
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postNumberSegment);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postNumberSegment) + BaseCom.APP_PWD));
                        new CardHttp().numberSegment(CardRequest.numberSegment(new SuccessValue<HttpRequest<RequestNumberSegment>>() {
                            @Override
                            public void OnSuccess(HttpRequest<RequestNumberSegment> value) {
                                dialog.dismiss();
                                if (value.getCode() == BaseCom.NORMAL) {
                                    dialog.getTvTitle().setText("正在获取验证码");
                                    dialog.show();

                                    HttpPost<PostCode> httpPost = new HttpPost<PostCode>();
                                    PostCode postCode = new PostCode();
                                    postCode.setTel(etPhone1.getText().toString());
                                    postCode.setCaptcha_type("6");
                                    postCode.setSession_token(Util.getLocalAdmin(getActivity())[0]);
                                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postCode) + BaseCom.APP_PWD));
                                    httpPost.setParameter(postCode);
                                    new AdminHttp().getCode(new Subscriber<HttpRequest<RequestCode>>() {
                                        @Override
                                        public void onCompleted() {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            dialog.dismiss();
                                        }

                                        @Override
                                        public void onNext(HttpRequest<RequestCode> requestCodeHttpRequest) {
                                            dialog.dismiss();
                                            Toast.makeText(getActivity(), requestCodeHttpRequest.getMes() + "", Toast.LENGTH_SHORT).show();
                                            if (requestCodeHttpRequest.getCode() == BaseCom.NORMAL) {
                                                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tvGetCode1, 60000, 1000);
                                                mCountDownTimerUtils.start();
                                            }
                                        }
                                    }, httpPost);
                                } else
                                    Toast.makeText(getActivity(), value.getMes() + "", Toast.LENGTH_SHORT).show();

                            }
                        }), httpPost);


                    }
                    break;
                case R.id.tvGetList0:
//                    Intent intent = new Intent(YewubanliActivity.activity, LiuliangActivity.class);
//                    intent.putExtra("phone", etPhone0.getText().toString());
//                    startActivity(intent);
                    if (!etPhone0.getText().toString().equals("") && !etCode0.getText().toString().equals("")) {
                        HttpPost<PostCaptcha> httpPost = new HttpPost<>();
                        PostCaptcha postCaptcha = new PostCaptcha();
                        postCaptcha.setTel(etPhone0.getText().toString());
                        postCaptcha.setCaptcha(etCode0.getText().toString());
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postCaptcha);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postCaptcha) + BaseCom.APP_PWD));
                        new AdminHttp().verifyCaptcha(AdminRequest.verifyCaptcha(getActivity(), dialog, new SuccessNull() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(getActivity(), LiuliangActivity.class);
                                intent.putExtra("phone", etPhone0.getText().toString());
                                startActivity(intent);
                            }
                        }), httpPost);
                    }

                    break;
                case R.id.tvGetList1:
//                    Intent intent0 = new Intent(YewubanliActivity.activity, HuiyuanActivity.class);
//                    intent0.putExtra("phone", etPhone1.getText().toString());
//                    startActivity(intent0);
                    if (!etPhone1.getText().toString().equals("") && !etCode1.getText().toString().equals("")) {
                        HttpPost<PostCaptcha> httpPost = new HttpPost<>();
                        PostCaptcha postCaptcha = new PostCaptcha();
                        postCaptcha.setTel(etPhone1.getText().toString());
                        postCaptcha.setCaptcha(etCode1.getText().toString());
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postCaptcha);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postCaptcha) + BaseCom.APP_PWD));
                        new AdminHttp().verifyCaptcha(AdminRequest.verifyCaptcha(YewubanliActivity.activity, dialog, new SuccessNull() {
                            @Override
                            public void onSuccess() {
                                Intent intent = new Intent(YewubanliActivity.activity, HuiyuanActivity.class);
                                intent.putExtra("phone", etPhone1.getText().toString());
                                startActivity(intent);
                            }
                        }), httpPost);
                    }
                    break;
            }
        }

        public void search(String string) {
            HttpPost<PostOtherDiscount> httpPost = new HttpPost<>();
            PostOtherDiscount postOtherDiscount = new PostOtherDiscount();
            postOtherDiscount.setSession_token(Util.getLocalAdmin(YewubanliActivity.activity)[0]);
            postOtherDiscount.setActualAmount(string);
            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost.setParameter(postOtherDiscount);
            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postOtherDiscount) + BaseCom.APP_PWD));
            Log.d("aaa", new Gson().toJson(httpPost));
            new AccountHttp().otherRechargeDiscount(AccountRequest.otherRechargeDiscount(YewubanliActivity.activity, new SuccessValue<RequestOtherDiscount>() {
                @Override
                public void OnSuccess(RequestOtherDiscount value) {
                    if (value.getDiscountAmount() != null && value.getDiscountAmount() != null) {
                        /*有优惠*/
                        tvMoney.setVisibility(View.VISIBLE);
                        tvDiscount.setVisibility(View.VISIBLE);
                        tvMoney.setText(String.valueOf(value.getDiscountAmount()));
                        tvDiscount.setText(String.valueOf(value.getActualAmount()));
                        tvDiscount.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
                        strMoney = tvDiscount.getText().toString();
                    } else {
                        /*无优惠*/
                        tvMoney.setVisibility(View.VISIBLE);
                        tvDiscount.setVisibility(View.GONE);
                        tvMoney.setText(strInputMoney);
                    }

                }
            }), httpPost);

        }

        protected void dialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(YewubanliActivity.activity);
            builder.setMessage("是否创建支付密码？");
            builder.setTitle("提示");
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    ((YewubanliActivity) YewubanliActivity.activity).gotoActy(PayPassCreateActivity.class);
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }


        public void recharge() {
            Log.d("aaa", strMoney);
            String strBalance = tvBalance.getText().toString();
            int v = strBalance.length();
            final String strNumber = etNumber.getText().toString();
            if (strNumber.equals("")) {
                Util.createToast(YewubanliActivity.activity, "请输入手机号码");
            } else if (Double.valueOf(strMoney) > 1000) {
                Toast.makeText(YewubanliActivity.activity, "话费充值不能多于1000元", Toast.LENGTH_SHORT).show();
            } else if (Double.valueOf(strMoney) > Double.valueOf(strBalance.substring(0, v - 1))) {
                Util.createToast(YewubanliActivity.activity, "账户余额不足");
            } else {
                SharedPreferences share = YewubanliActivity.activity.getSharedPreferences(BaseCom.hasPassword, MODE_PRIVATE);
                final Integer click = share.getInt("hasPass", 0);
                final SharedPreferences.Editor edit = share.edit(); //编辑文件
                if (click == 0) {
                    Log.d("ddd", "0");
                    HttpPost<PostPsdCheck> httpPost = new HttpPost<>();
                    PostPsdCheck postPsdCheck = new PostPsdCheck();
                    postPsdCheck.setSession_token(Util.getLocalAdmin(YewubanliActivity.activity)[0]);
                    httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                    httpPost.setParameter(postPsdCheck);
                    httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postPsdCheck) + BaseCom.APP_PWD));
                    new AccountHttp().initialPsdCheck(AccountRequest.initialPsdCheck(YewubanliActivity.activity, new SuccessValue<HttpRequest<RequestPsdCheck>>() {
                        @Override
                        public void OnSuccess(HttpRequest<RequestPsdCheck> value) {
                            if (value.getCode() == BaseCom.NORMAL) {
                                Log.d("ddd", "1");
                                edit.putInt("hasPass", 1);
                                edit.commit();  //保存数据信息
                                recharge();
                            } else if (value.getCode() == BaseCom.INNORMAL) {
                                Log.d("ddd", "2");
                                dialog();
                            } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {

                                ((YewubanliActivity) YewubanliActivity.activity).gotoActy(LoginActivity.class);
                            }
                        }
                    }), httpPost);


                } else {
                    //有支付密码,支付
                    Log.d("ddd", "3");
                    Log.d("ddd", strMoney);
                    addPopWindow = new PasswordPop(YewubanliActivity.activity);
                    addPopWindow.showPopupWindow(llActivity);
                    llActivity.setAlpha(0.2f);
                    addPopWindow.getPwdView().setOnFinishInput(new OnPasswordInputFinish() {
                        @Override
                        public void inputFinish(String content) {
                            //输入完成后我们简单显示一下输入的密码
                            //也就是说——>实现你的交易逻辑什么的在这里写
                            dialog.getTvTitle().setText("正在充值");
                            dialog.show();
                            HttpPost<PostServiceProduct> httpPost = new HttpPost<>();
                            PostServiceProduct serviceProduct = new PostServiceProduct();
                            serviceProduct.setSession_token(Util.getLocalAdmin(YewubanliActivity.activity)[0]);
                            serviceProduct.setType(1);
                            serviceProduct.setProductId(chooseProductBeanDetail.getProductId());
                            serviceProduct.setNumber(strNumber);
                            serviceProduct.setOperateType(1);
                            serviceProduct.setProductAmount(chooseProductBeanDetail.getProductAmount() + "");
                            serviceProduct.setOrderAmount(chooseProductBeanDetail.getOrderAmount() + "");
                            serviceProduct.setPayPassword(Util.encode(BaseCom.PASSWORD0 + content + BaseCom.PASSWORD1));
                            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                            httpPost.setParameter(serviceProduct);
                            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(serviceProduct) + BaseCom.APP_PWD));
                            new AccountHttp()._2019ServiceProduct(new Subscriber<HttpRequest<RequestServiceProduct>>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(HttpRequest<RequestServiceProduct> value) {
                                    dialog.dismiss();
                                    Toast.makeText(YewubanliActivity.activity, value.getMes(), Toast.LENGTH_SHORT).show();
                                    if (value.getCode() == BaseCom.NORMAL) {
                                        Intent intent = new Intent(YewubanliActivity.activity, PayResultActivity.class);
                                        intent.putExtra("from", "0");
                                        intent.putExtra("money", strMoney);
                                        intent.putExtra("date", BaseUtils.getDate());
                                        intent.putExtra("phone", strNumber);
                                        intent.putExtra("reason", value.getMes());
                                        if (value.getCode() == BaseCom.NORMAL) {
                                            intent.putExtra("flag", 0);
                                            intent.putExtra("id", value.getData().getOrderNo());
                                        } else {
                                            intent.putExtra("flag", 1);

                                            intent.putExtra("id", "无");
                                        }
                                        startActivity(intent);
                                        YewubanliActivity.activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                        addPopWindow.dismiss();
//                                    NumberRechargeActivity.this.finish();

                                    } else if (value.getCode() == BaseCom.LOSELOG) {
                                        //AppManager.getAppManager().finishActivity();
                                        ((YewubanliActivity) YewubanliActivity.activity).gotoActy(LoginActivity.class);
                                    } else {
                                        addPopWindow.dismiss();
                                    }

                                }
                            }, httpPost);


//                            HttpPost<PostRecharge> httpPost = new HttpPost<>();
//                            PostRecharge postRecharge = new PostRecharge();
//                            postRecharge.setSession_token(Util.getLocalAdmin(YewubanliActivity.activity)[0]);
//                            postRecharge.setNumber(strNumber);
//                            postRecharge.setMoney(strMoney);
//                            postRecharge.setPay_password(Util.encode(BaseCom.PASSWORD0 + addPopWindow.getPwdView().getStrPassword() + BaseCom.PASSWORD1));
//                            httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
//                            httpPost.setParameter(postRecharge);
//                            httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + new Gson().toJson(postRecharge) + BaseCom.APP_PWD));
//                            Log.d("aaa", new Gson().toJson(httpPost));
//                            new AccountHttp().recharge(AccountRequest.recharge(dialog, new SuccessValue<HttpRequest<RequestRecharge>>() {
//                                @Override
//                                public void OnSuccess(HttpRequest<RequestRecharge> value) {
//                                    Toast.makeText(YewubanliActivity.activity, value.getMes(), Toast.LENGTH_SHORT).show();
//                                    Log.d("ccc", value.getMes());
//                                    Log.d("ccc", value.getCode() + "");
//
//                                    if (value.getCode() == BaseCom.NORMAL) {
//                                        Intent intent = new Intent(YewubanliActivity.activity, PayResultActivity.class);
//                                        intent.putExtra("from", "0");
//                                        intent.putExtra("money", strMoney);
//                                        intent.putExtra("date", BaseUtils.getDate());
//                                        intent.putExtra("phone", strNumber);
//                                        intent.putExtra("reason", value.getMes());
//                                        if (value.getCode() == BaseCom.NORMAL) {
//                                            intent.putExtra("flag", 0);
//                                            intent.putExtra("id", value.getData().getNumbers());
//                                        } else {
//                                            intent.putExtra("flag", 1);
//
//                                            intent.putExtra("id", "无");
//                                        }
//                                        startActivity(intent);
//                                        YewubanliActivity.activity.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
//                                        addPopWindow.dismiss();
////                                    NumberRechargeActivity.this.finish();
//
//                                    } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {
//                                        //AppManager.getAppManager().finishActivity();
//                                        ((YewubanliActivity) YewubanliActivity.activity).gotoActy(LoginActivity.class);
//                                    } else
//                                        addPopWindow.dismiss();
//
//                                }
//                            }), httpPost);
                        }
                    });
                    addPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            llActivity.setAlpha(1f);
                        }
                    });
                }

            }
        }

        class GvAdapter extends BaseAdapter {

            @Override
            public int getCount() {
                return productBeanDetails.size();
            }

            @Override
            public Object getItem(int position) {
                return productBeanDetails.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                ViewHolder holder = null;

                if (convertView == null) {
                    convertView = LayoutInflater.from(YewubanliActivity.activity).inflate(R.layout.adapter_account_recharge, null);
                    holder = new ViewHolder(convertView);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                holder.tvPrime.setText(productBeanDetails.get(position).getProductAmount() + "元");
                holder.tvCurrent.setText("售价：" + productBeanDetails.get(position).getOrderAmount() + "元");
                if (position == pos) {
                    holder.llClick.setBackgroundResource(R.drawable.shape_recharge_click);
                    holder.tvPrime.setTextColor(getResources().getColor(R.color.colorWhite));
                    holder.tvCurrent.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    holder.llClick.setBackgroundResource(R.drawable.shape_recharge);
                    holder.tvPrime.setTextColor(getResources().getColor(R.color.colorBlue3));
                    holder.tvCurrent.setTextColor(getResources().getColor(R.color.colorBlue3));
                }

                return convertView;
            }


            class ViewHolder {
                @BindView(R.id.tvPrime)
                TextView tvPrime;
                @BindView(R.id.tvCurrent)
                TextView tvCurrent;
                @BindView(R.id.ll_click)
                LinearLayout llClick;

                ViewHolder(View view) {
                    ButterKnife.bind(this, view);
                }
            }

        }

    }

}
