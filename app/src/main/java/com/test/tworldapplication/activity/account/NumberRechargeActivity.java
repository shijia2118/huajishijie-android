package com.test.tworldapplication.activity.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.PayPassCreateActivity;
import com.test.tworldapplication.activity.main.LoginActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Discount;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.PostBalanceQuery;
import com.test.tworldapplication.entity.PostOtherDiscount;
import com.test.tworldapplication.entity.PostPsdCheck;
import com.test.tworldapplication.entity.PostQueryBalance;
import com.test.tworldapplication.entity.PostRecharge;
import com.test.tworldapplication.entity.PostRechargeDiscount;
import com.test.tworldapplication.entity.RequestBalanceQuery;
import com.test.tworldapplication.entity.RequestOtherDiscount;
import com.test.tworldapplication.entity.RequestPsdCheck;
import com.test.tworldapplication.entity.RequestQueryBalance;
import com.test.tworldapplication.entity.RequestRecharge;
import com.test.tworldapplication.entity.RequestRechargeDiscount;
import com.test.tworldapplication.http.AccountHttp;
import com.test.tworldapplication.http.AccountRequest;
import com.test.tworldapplication.inter.OnPasswordInputFinish;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.Util;
import com.test.tworldapplication.view.PasswordPop;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NumberRechargeActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.etNumber)
    EditText etNumber;
    @BindView(R.id.ll_input)
    RelativeLayout llInput;
    @BindView(R.id.gvMoney)
    GridView gvMoney;
    int pos;
    MyAdapter adapter;
    @BindView(R.id.ll_activity)
    View llActivity;
    @BindView(R.id.tvDiscount)
    TextView tvDiscount;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.ll_Other)
    LinearLayout llOther;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.tvCollection)
    TextView tvCollection;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvRegister)
    TextView tvRegister;
    @BindView(R.id.tvNext)
    TextView tvNext;
    @BindView(R.id.tvToast)
    TextView tvToast;
    PasswordPop addPopWindow;
    String strMoney = "";
    Discount[] discounts;
    @BindView(R.id.ll_selection)
    LinearLayout llSelection;
    String strInputMoney;
    double autr = 0;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count == 11) {
                Log.d("ssss", "sdfg");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (s.toString().length() == 11) {
                HttpPost<PostQueryBalance> httpPost = new HttpPost<>();
                PostQueryBalance postQueryBalance = new PostQueryBalance();
                postQueryBalance.setSession_token(Util.getLocalAdmin(NumberRechargeActivity.this)[0]);
                postQueryBalance.setNumber(s.toString());
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postQueryBalance) + BaseCom.APP_PWD));
                httpPost.setParameter(postQueryBalance);
                new AccountHttp().queryBalance(AccountRequest.queryBalance(NumberRechargeActivity.this, dialog, new SuccessValue<HttpRequest<RequestQueryBalance>>() {
                    @Override
                    public void OnSuccess(HttpRequest<RequestQueryBalance> value) {
                        Util.createToast(NumberRechargeActivity.this, value.getMes());
                        switch (value.getCode()) {
                            case BaseCom.NORMAL:
                                tvToast.setText("话费余额: " + value.getData().getMoney() + "元");
                                tvToast.setVisibility(View.VISIBLE);
                                break;
                            case BaseCom.LOSELOG:
                            case BaseCom.VERSIONINCORRENT:
                                Util.gotoActy(NumberRechargeActivity.this, LoginActivity.class);
                                break;
                            default:
                                tvToast.setVisibility(View.GONE);
                                break;
                        }

                    }
                }), httpPost);
            }
        }
    };

   /* 先获取充值优惠和账户余额,默认金额为grideview的第一个,点击其他充值金额 点击弹出dialog的确定按钮,计算优惠,金额为优惠金额,点击item,金额为item的金额,对手机号码,账户余额进行校验,支付*/
/*先获取本地支付密码标识,默认0,1为有密码直接支付,0为无密码标识,调用接口校验支付密码,若有密码本地存1,支付,无密码弹出dialog提示创建密码,创建成功后,返回该页面,点击支付按钮*/

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_recharge);
        ButterKnife.bind(this);
        setBackGroundTitle("话费充值", true);
        init();

    }

    private void init() {
        etNumber.addTextChangedListener(textWatcher);
        gvMoney.setOnItemClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        HttpPost<PostRechargeDiscount> httpPost = new HttpPost<>();
        PostRechargeDiscount postRechargeDiscount = new PostRechargeDiscount();
        postRechargeDiscount.setSession_token(Util.getLocalAdmin(NumberRechargeActivity.this)[0]);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postRechargeDiscount);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRechargeDiscount) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new AccountHttp().rechargeDiscount(AccountRequest.rechargeDiscount(NumberRechargeActivity.this, new SuccessValue<RequestRechargeDiscount>() {
            @Override
            public void OnSuccess(RequestRechargeDiscount value) {
                discounts = value.getDiscount();
                /*有优惠信息*/
                if (discounts != null) {
                    llSelection.setVisibility(View.VISIBLE);
                    adapter = new MyAdapter();
                    gvMoney.setAdapter(adapter);
                    strMoney = String.valueOf(discounts[0].getActualAmount());
                    Log.d("aaa", strMoney);
                } else {
/*无优惠  页面隐藏*/
                    llSelection.setVisibility(View.GONE);
                }
            }
        }), httpPost);
//        if (Util.isLog(NumberRechargeActivity.this)) {
        HttpPost<PostBalanceQuery> httpPost1 = new HttpPost<>();
        PostBalanceQuery postBalanceQuery = new PostBalanceQuery();
        postBalanceQuery.setSession_token(Util.getLocalAdmin(NumberRechargeActivity.this)[0]);
        httpPost1.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
        httpPost1.setParameter(postBalanceQuery);
        httpPost1.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postBalanceQuery) + BaseCom.APP_PWD));
        new AccountHttp().balanceQuery(AccountRequest.balanceQuery(new SuccessValue<HttpRequest<RequestBalanceQuery>>() {
            @Override
            public void OnSuccess(HttpRequest<RequestBalanceQuery> value) {
                Toast.makeText(NumberRechargeActivity.this, value.getMes(), Toast.LENGTH_SHORT).show();
                tvBalance.setText(value.getData().getBalance() + "元");
            }
        }), httpPost1);

//        }
    }


    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(NumberRechargeActivity.this);
        builder.setMessage("是否创建支付密码？");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                gotoActy(PayPassCreateActivity.class);
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
            Util.createToast(NumberRechargeActivity.this, "请输入手机号码");
        } else if (Double.valueOf(strMoney) > 1000) {
            Toast.makeText(NumberRechargeActivity.this, "话费充值不能多于1000元", Toast.LENGTH_SHORT).show();
        } else if (Double.valueOf(strMoney) > Double.valueOf(strBalance.substring(0, v - 1))) {
            Util.createToast(NumberRechargeActivity.this, "账户余额不足");
        } else {
            SharedPreferences share = NumberRechargeActivity.this.getSharedPreferences(BaseCom.hasPassword, MODE_PRIVATE);
            final Integer click = share.getInt("hasPass", 0);
            final SharedPreferences.Editor edit = share.edit(); //编辑文件
            if (click == 0) {
                Log.d("ddd", "0");
                HttpPost<PostPsdCheck> httpPost = new HttpPost<>();
                PostPsdCheck postPsdCheck = new PostPsdCheck();
                postPsdCheck.setSession_token(Util.getLocalAdmin(NumberRechargeActivity.this)[0]);
                httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                httpPost.setParameter(postPsdCheck);
                httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postPsdCheck) + BaseCom.APP_PWD));
                new AccountHttp().initialPsdCheck(AccountRequest.initialPsdCheck(NumberRechargeActivity.this, new SuccessValue<HttpRequest<RequestPsdCheck>>() {
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
                            gotoActy(LoginActivity.class);
                        }
                    }
                }), httpPost);


            } else {
                //有支付密码,支付
                Log.d("ddd", "3");
                Log.d("ddd", strMoney);
                addPopWindow = new PasswordPop(NumberRechargeActivity.this);
                addPopWindow.showPopupWindow(llActivity);
                llActivity.setAlpha(0.2f);
                addPopWindow.getPwdView().setOnFinishInput(new OnPasswordInputFinish() {
                    @Override
                    public void inputFinish(String content) {
                        //输入完成后我们简单显示一下输入的密码
                        //也就是说——>实现你的交易逻辑什么的在这里写
                        dialog.getTvTitle().setText("正在充值");
                        dialog.show();
                        HttpPost<PostRecharge> httpPost = new HttpPost<>();
                        PostRecharge postRecharge = new PostRecharge();
                        postRecharge.setSession_token(Util.getLocalAdmin(NumberRechargeActivity.this)[0]);
                        postRecharge.setNumber(strNumber);
                        postRecharge.setMoney(strMoney);
                        postRecharge.setPay_password(Util.encode(BaseCom.PASSWORD0 + addPopWindow.getPwdView().getStrPassword() + BaseCom.PASSWORD1));
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postRecharge);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postRecharge) + BaseCom.APP_PWD));
                        Log.d("aaa", gson.toJson(httpPost));
                        new AccountHttp().recharge(AccountRequest.recharge(dialog, new SuccessValue<HttpRequest<RequestRecharge>>() {
                            @Override
                            public void OnSuccess(HttpRequest<RequestRecharge> value) {
                                Toast.makeText(NumberRechargeActivity.this, value.getMes(), Toast.LENGTH_SHORT).show();
                                Log.d("ccc", value.getMes());
                                Log.d("ccc", value.getCode() + "");

                                if (value.getCode() == BaseCom.NORMAL) {
                                    Intent intent = new Intent(NumberRechargeActivity.this, PayResultActivity.class);
                                    intent.putExtra("from", "0");
                                    intent.putExtra("money", strMoney);
                                    intent.putExtra("date", BaseUtils.getDate());
                                    intent.putExtra("phone", strNumber);
                                    intent.putExtra("reason", value.getMes());
                                    if (value.getCode() == BaseCom.NORMAL) {
                                        intent.putExtra("flag", 0);
                                        intent.putExtra("id", value.getData().getNumbers());
                                    } else {
                                        intent.putExtra("flag", 1);

                                        intent.putExtra("id", "无");
                                    }
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                                    addPopWindow.dismiss();
//                                    NumberRechargeActivity.this.finish();

                                } else if (value.getCode() == BaseCom.LOSELOG || value.getCode() == BaseCom.VERSIONINCORRENT) {
                                    //AppManager.getAppManager().finishActivity();
                                    gotoActy(LoginActivity.class);
                                } else
                                    addPopWindow.dismiss();

                            }
                        }), httpPost);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        pos = position;

        strMoney = String.valueOf(discounts[pos].getActualAmount());
        adapter.notifyDataSetChanged();
        tvMoney.setText("");
        tvDiscount.setText("");
        tvMoney.setVisibility(View.GONE);
        tvDiscount.setVisibility(View.GONE);

    }

    @OnClick({R.id.ll_Other, R.id.tvNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_Other:
                pos = 6;
                adapter.notifyDataSetChanged();
                tvMoney.setText("");
                tvDiscount.setText("");
                final EditText editText = new EditText(this);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                new AlertDialog.Builder(this).setTitle("请输入充值金额").setView(editText).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.getText().toString().equals("") || editText.getText().toString().equals("0")) {
                            Util.createToast(NumberRechargeActivity.this, "请输入有效金额");
                        } else if (Integer.valueOf(editText.getText().toString()) > 1000) {
                            Util.createToast(NumberRechargeActivity.this, "单笔充值金额应小于1000.00元");
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
        }
    }

    public void search(String string) {
        HttpPost<PostOtherDiscount> httpPost = new HttpPost<>();
        PostOtherDiscount postOtherDiscount = new PostOtherDiscount();
        postOtherDiscount.setSession_token(Util.getLocalAdmin(NumberRechargeActivity.this)[0]);
        postOtherDiscount.setActualAmount(string);
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postOtherDiscount);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postOtherDiscount) + BaseCom.APP_PWD));
        Log.d("aaa", gson.toJson(httpPost));
        new AccountHttp().otherRechargeDiscount(AccountRequest.otherRechargeDiscount(NumberRechargeActivity.this, new SuccessValue<RequestOtherDiscount>() {
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

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return discounts.length;
        }

        @Override
        public Object getItem(int position) {
            return discounts[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(NumberRechargeActivity.this).inflate(R.layout.adapter_account_recharge, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvPrime.setText(discounts[position].getActualAmount() + "元");
            holder.tvCurrent.setText("售价：" + discounts[position].getDiscountAmount() + "元");
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

//

            }
        }

    }


}
