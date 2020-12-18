package com.test.tworldapplication.activity.card;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.NumberBalanceActivity;
import com.test.tworldapplication.activity.account.NumberRechargeActivity;
import com.test.tworldapplication.activity.home.YewubanliActivity;
import com.test.tworldapplication.adapter.AccountAdapter;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Account;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.PostDs;
import com.test.tworldapplication.entity.RequestDs;
import com.test.tworldapplication.http.CardHttp;
import com.test.tworldapplication.http.CardRequest;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.Util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CardMainFragment extends Fragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.card_main_listview)
    ListView cardMainListview;
    List<Account> list = new ArrayList<>();

    Gson gson = new Gson();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View cardLayout = inflater.inflate(R.layout.activity_card_main, container, false);
        ButterKnife.bind(this, cardLayout);
        cardMainListview.setOnItemClickListener(this);
        HashSet set = new HashSet();

        return cardLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    private void initView() {
        list.clear();
        if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
            /*qudao*/
//            list.add(new Account(R.mipmap.card_ckkh, "成卡开户"));
//            list.add(new Account(R.mipmap.card_bkkh, "白卡开户"));
//            list.add(new Account(R.mipmap.card_bkyk, "白卡预开户"));
//            list.add(new Account(R.mipmap.card_gh, "过户"));
//            list.add(new Account(R.mipmap.card_bk, "补卡"));
////            list.add(new Account(R.mipmap.card_lhpt, "靓号"));
//            list.add(new Account(R.mipmap.card_hfcz, "话费充值"));
//            list.add(new Account(R.mipmap.card_hfye, "话费余额查询"));
//            list.add(new Account(R.mipmap.card_cpgm, "流量包"));
//            list.add(new Account(R.mipmap.product_bill, "账单查询"));
//            list.add(new Account( R.mipmap.lianghao81,"靓号" ));

            list.add(new Account(R.mipmap.order_ywdd, "业务办理"));//5
            list.add(new Account(R.mipmap.card_bkkh, "白卡开户"));//1
            list.add(new Account(R.mipmap.business_icon_whiteprepare_small, "白卡预开户"));//2
            list.add(new Account(R.mipmap.card_gh, "过户"));//3
            list.add(new Account(R.mipmap.establish, "成卡开户"));//0
            list.add(new Account(R.mipmap.card_bk, "补卡"));//4
//            list.add(new Account(R.mipmap.flow, "流量包"));//7
            list.add(new Account(R.mipmap.card_hfye, "话费余额查询"));//6
            list.add(new Account(R.mipmap.product_bill, "账单查询"));//8
            list.add(new Account(R.mipmap.lianghao81, "靓号"));//9
            list.add(new Account(R.mipmap.order_icon_ownership_small, "预订单生成"));
            list.add(new Account(R.mipmap.order_icon_ownership_last, "白卡申请"));
        } else {
            /*代理*/
//            list.add(new Account(R.mipmap.card_lhpt, "靓号"));
            list.add(new Account(R.mipmap.card_bkyk, "白卡预开户"));
            list.add(new Account(R.mipmap.card_ykjl, "预开户记录"));
            list.add(new Account(R.mipmap.product_bill, "子账户开户记录"));
        }

        cardMainListview.setAdapter(new AccountAdapter(getActivity().getApplicationContext(), list));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
//            case 0:
//                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
//                    /*成卡*/
//                    Util.saveClickTime(getActivity(), BaseCom.renewOpen);
//                    gotoActy(RenewCardActivity.class);
//
//                } else {
//                    /*靓号*/
////                    gotoActy(CuteNumberActivity.class);
//                    Util.createToast(getActivity(), "该功能待开发,敬请期待!");
//                }
//
//
//                break;
//            case 1:
//                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
////                    Util.createToast(getActivity(), "该功能待开发,敬请期待!");
//                    Util.saveClickTime(getActivity(), BaseCom.newOpen);
//                    gotoActy(NewCardActivity.class);
//
//                } else {
//                    /*白卡预开户*/
////                    Util.createToast(getActivity(), "该功能待开发,敬请期待!");
//                    gotoActy(CuteNumberDailiActivity.class);
//
//                }
//
//
//                break;
//            case 2:
//                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
//                    /*白卡预开户*/
////                    Util.createToast(getActivity(), "该功能待开发,敬请期待!");
//                    gotoActy(QudaoWhitePreOpenActivity.class);
//                } else {
//  /*预开户记录*/
//                    gotoActy(PreOpenRecordActivity.class);
//                    //Util.createToast(getActivity(), "该功能待开发,敬请期待!");
//
//
//                    Util.saveClickTime(getActivity(), BaseCom.transform);
////                    gotoActy(SubAccountActivity.class);
//                }
//
//                break;
//            case 3:
//                Util.saveClickTime(getActivity(), BaseCom.transform);
//                gotoActy(TransferCardActivity.class);
//                break;
//            case 4:
//                Util.saveClickTime(getActivity(), BaseCom.replace);
//                gotoActy(ReplaceCardActivity.class);
//                break;
////            case 5:/*靓号平台*/
////                Util.createToast(getActivity(), "该功能待开发,敬请期待!");
////                Util.saveClickTime(getActivity(), BaseCom.cardQuery);
////                gotoActy(QdsCuteSelectActivity.class);
//
////                break;
//            case 5:
//                Util.saveClickTime(getActivity(), BaseCom.phoneRecharge);
//                gotoActy(NumberRechargeActivity.class);
//                break;
//            case 6:
//                Util.saveClickTime(getActivity(), BaseCom.phoneBanlance);
//                gotoActy(NumberBalanceActivity.class);
//                break;
//            case 7:/*产品购买*/
////                Util.createToast(getActivity(), "该功能待开发,敬请期待");
////                Util.saveClickTime(getActivity(), BaseCom.cardQuery);
//                gotoActy(ProductBusinessActivity.class);
//                break;
//            case 8:/*产品购买*/
////                Util.createToast(getActivity(), "该功能待开发,敬请期待");
////                Util.saveClickTime(getActivity(), BaseCom.cardQuery);
//                gotoActy(BillQueryActivity.class);
//                break;
//            case 9:/*靓号*/
//                gotoActy( QdsCuteSelectActivity.class );
////                gotoActy( FaceMainActivity.class );
//                break;

            case 0:
                Util.saveClickTime(getActivity(), BaseCom.phoneRecharge);
                gotoActy(YewubanliActivity.class);
                break;
            case 1:
                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
                    Util.createToast(getActivity(), "开发中，请耐心等待");
//                    Util.saveClickTime(getActivity(), BaseCom.newOpen);
//                    gotoActy(NewCardActivity.class);

                } else {
                    /*白卡预开户*/
//                    Util.createToast(getActivity(), "该功能待开发,敬请期待!");
                    gotoActy(CuteNumberDailiActivity.class);

                }


                break;
            case 2:
                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
                    /*白卡预开户*/
                    Util.createToast(getActivity(), "开发中，请耐心等待!");
//                    gotoActy(QudaoWhitePreOpenActivity.class);
                } else {
                    /*预开户记录*/
//                    gotoActy(PreOpenRecordActivity.class);
                    Util.createToast(getActivity(), "开发中，请耐心等待!");
//                    Util.saveClickTime(getActivity(), BaseCom.transform);
//                    gotoActy(SubAccountActivity.class);
                }
                break;
            case 3:
                Util.saveClickTime(getActivity(), BaseCom.transform);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("mySP", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("pattern", 3);
                editor.putInt("modes", 3);
                editor.putInt("readModes", 3);
                editor.commit();
                gotoActy(TransferCardActivity.class);
                break;
            case 4:
                if (Util.getLocalAdmin(getActivity())[1].equals("lev3")) {
                    /*成卡*/
                    Util.saveClickTime(getActivity(), BaseCom.renewOpen);
//                    gotoActy(RenewCardActivity.class);
                    Intent intent = new Intent(getActivity(), SelectActivity.class);
                    intent.putExtra("from", "0");
                    startActivity(intent);
                } else {
                    /*靓号*/
//                    gotoActy(CuteNumberActivity.class);
                    Util.createToast(getActivity(), "该功能待开发,敬请期待!");
                }
                break;
            case 5:
                Util.saveClickTime(getActivity(), BaseCom.replace);
                gotoActy(ReplaceCardActivity.class);
                break;
//            case 6:/*产品购买*/
////                Util.createToast(getActivity(), "该功能待开发,敬请期待");
////                Util.saveClickTime(getActivity(), BaseCom.cardQuery);
//                gotoActy(ProductBusinessActivity.class);
//                break;
            case 6:
                Util.saveClickTime(getActivity(), BaseCom.phoneBanlance);
                gotoActy(NumberBalanceActivity.class);
                break;
            case 7:/*产品购买*/
//                Util.createToast(getActivity(), "该功能待开发,敬请期待");
//                Util.saveClickTime(getActivity(), BaseCom.cardQuery);
                gotoActy(BillQueryActivity.class);
                break;
            case 8:/*靓号*/
                gotoActy(QdsCuteSelectActivity.class);
//                gotoActy( FaceMainActivity.class );
                break;
            case 9:
                HttpPost<PostDs> httpPost = new HttpPost<>();
                PostDs postDs = new PostDs();
                postDs.setSession_token(Util.getLocalAdmin(getActivity())[0]);
                httpPost.setApp_key(Util.GetMD5Code(BaseCom.APP_KEY));
                httpPost.setApp_sign(Util.GetMD5Code(BaseCom.APP_PWD + gson.toJson(postDs) + BaseCom.APP_PWD));
                httpPost.setParameter(postDs);
                Log.d("aaa", gson.toJson(httpPost));
                new CardHttp().checkDs(CardRequest.checkDs(getActivity(), new SuccessValue<RequestDs>() {
                    @Override
                    public void OnSuccess(RequestDs value) {
                        if (value.getIsDS().equals("Y")) {
                            Intent intent = new Intent(getActivity(), RenewCardActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("face", "0");
                            intent.putExtra("from", "0");
                            intent.putExtra("post", 2);
                            startActivity(intent);
                        } else {
                            Util.createToast(getActivity(), "暂未开通此功能");
                        }
                    }
                }), httpPost);
                break;
            case 10:
                gotoActy(BkApplyActivity.class);
                break;

        }
    }


    private void gotoActy(Class clazz) {
        if (!Util.isFastDoubleClick()) {
            Intent intent = new Intent(getActivity().getApplicationContext(), clazz);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }
}

