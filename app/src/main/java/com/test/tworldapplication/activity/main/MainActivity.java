package com.test.tworldapplication.activity.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.account.AccountMainFragment;
import com.test.tworldapplication.activity.card.CardMainFragment;
import com.test.tworldapplication.activity.home.HomeMainFragment;
import com.test.tworldapplication.activity.order.OrderMainFragment;
import com.test.tworldapplication.activity.order.QdsMainNewFragment;
import com.test.tworldapplication.activity.other.MessageMainActivity;
import com.test.tworldapplication.base.BaseActivity;
import com.test.tworldapplication.base.BaseCom;
import com.test.tworldapplication.entity.Area;
import com.test.tworldapplication.entity.City;
import com.test.tworldapplication.entity.HttpPost;
import com.test.tworldapplication.entity.HttpRequest;
import com.test.tworldapplication.entity.JudgeLocationResponse;
import com.test.tworldapplication.entity.MessageEvent;
import com.test.tworldapplication.entity.PostFunction;
import com.test.tworldapplication.entity.PostLocationEntity;
import com.test.tworldapplication.entity.PostNoticeList;
import com.test.tworldapplication.entity.Province;
import com.test.tworldapplication.entity.RequestNoticeList;
import com.test.tworldapplication.http.OrderHttp;
import com.test.tworldapplication.http.OtherHttp;
import com.test.tworldapplication.http.OtherRequest;
import com.test.tworldapplication.inter.SuccessNull;
import com.test.tworldapplication.inter.SuccessValue;
import com.test.tworldapplication.utils.BaseUtils;
import com.test.tworldapplication.utils.LocationHelper;
import com.test.tworldapplication.utils.Util;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import wintone.passport.sdk.utils.AppManager;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;

    ArrayList<View> selection = new ArrayList<>();
    ArrayList<Fragment> pages = new ArrayList<>();
    MyAdapter adapter;
    @BindView(R.id.tab_home)
    LinearLayout tabHome;
    @BindView(R.id.tab_order)
    LinearLayout tabOrder;
    @BindView(R.id.tab_qds)
    LinearLayout tabQds;
    @BindView(R.id.tab_account)
    LinearLayout tabAccount;
    @BindView(R.id.tab_card)
    LinearLayout tabCard;
    HomeMainFragment QdHomeFragment, DlHomeFragment;
    OrderMainFragment orderMainFragment;
    AccountMainFragment accountMainFragment;
    CardMainFragment QdCardMainFragment, DlCardMainFragment;
    QdsMainNewFragment qdsMainNewFragment;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent messageEvent) {
        switch (messageEvent.getFlag()) {
            case MessageEvent.CHECK_PERMISSION:
                Integer current = new Integer(messageEvent.getMessage());
                if (current % 20 == 0) {
                    if (!Util.isOPen(MainActivity.this) || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PERMISSION_GRANTED) {
                        AlertDialog.Builder dialog = Util.createAlertDialog(AppManager.getAppManager().currentActivity(),
                                "未打开位置信息或未开启定位权限，禁止使用", null);
                        dialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Util.stopSub();
                                Util.gotoActy(AppManager.getAppManager().currentActivity(), null);
                            }
                        });

                        dialog.show();
                    }
                }
                if (current % 40 == 0) {
                    LocationHelper.getInstance().startLocation();
                }

                break;
            case MessageEvent.START_LOCATE:
                LocationHelper.getInstance().startLocation();
                break;
            case MessageEvent.STOP_COUNT:
                Util.stopSub();
                break;
            case MessageEvent.LOCATION_MESSAGE:
                String content = messageEvent.getMessage();
                if (!content.equals("null,null")) {
                    String provinceCode = "";
                    String cityCode = "";
                    String province = content.split(",")[0];
                    String city = content.split(",")[1];
                    Area area = BaseUtils.getArea(MainActivity.this);
                    List<Province> provinces = area.getList();
                    Province province1 = new Province(province);
                    if (provinces.contains(province1)) {
                        int index = provinces.indexOf(province1);
                        provinceCode = provinces.get(index).getP_id();
                        List<City> cities = provinces.get(index).getP_list();
                        City city1 = new City(city);
                        if (cities.contains(city1)) {
                            int index0 = cities.indexOf(city1);
                            cityCode = cities.get(index0).getC_id();
                        }
                    }
                    if (!provinceCode.equals("") && !cityCode.equals("")) {

                        HttpPost<PostLocationEntity> httpPost = new HttpPost<>();
                        PostLocationEntity postLocationEntity = new PostLocationEntity();
                        postLocationEntity.setSession_token(Util.getLocalAdmin(MainActivity.this)[0]);
                        postLocationEntity.setProvinceCode(provinceCode);
                        postLocationEntity.setCityCode(cityCode);
                        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
                        httpPost.setParameter(postLocationEntity);
                        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postLocationEntity) + BaseCom.APP_PWD));

                        new OrderHttp().judgeLocation(new Subscriber<HttpRequest<JudgeLocationResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(HttpRequest<JudgeLocationResponse> booleanHttpRequest) {
                                if (booleanHttpRequest.getData().getJudgeLocation()) {

                                } else {
                                    AlertDialog.Builder dialog = Util.createAlertDialog(AppManager.getAppManager().currentActivity(),
                                            "用户未在常用地使用", null);
                                    dialog.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            Util.stopSub();
                                            Util.gotoActy(AppManager.getAppManager().currentActivity(), null);
                                        }
                                    });

                                    dialog.show();
                                }
                            }
                        }, httpPost);
                    }

                }
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);

        setBackGroundTitle("首页", false, true, true);
        adapter = new MyAdapter(getSupportFragmentManager());

        mainViewpager.setAdapter(adapter);
        mainViewpager.setOnPageChangeListener(this);
        mainViewpager.setCurrentItem(0);

        tabHome.setOnClickListener(this);
        tabOrder.setOnClickListener(this);
        tabQds.setOnClickListener(this);
        tabAccount.setOnClickListener(this);
        tabCard.setOnClickListener(this);
        Util.checkCount();
        Util.startLocate();


    }

    private void initTab() {
        selection.clear();
        pages.clear();
        adapter.notifyDataSetChanged();

        if (Util.getLocalAdmin(MainActivity.this)[1].equals("lev3")) {
            /*渠道*/

            tabQds.setVisibility(View.GONE);
            tabOrder.setVisibility(View.VISIBLE);
            tabAccount.setVisibility(View.VISIBLE);
            selection.add(tabHome);
            selection.add(tabOrder);
            selection.add(tabAccount);
            selection.add(tabCard);

            if (QdHomeFragment == null)
                QdHomeFragment = new HomeMainFragment();
            if (orderMainFragment == null)
                orderMainFragment = new OrderMainFragment();
            if (accountMainFragment == null)
                accountMainFragment = new AccountMainFragment();
            if (QdCardMainFragment == null)
                QdCardMainFragment = new CardMainFragment();

            pages.add(QdHomeFragment);
            pages.add(orderMainFragment);
            pages.add(accountMainFragment);
            pages.add(QdCardMainFragment);
        } else {
            /*代理*/
            tabOrder.setVisibility(View.GONE);
            tabQds.setVisibility(View.VISIBLE);
            tabAccount.setVisibility(View.GONE);
            selection.add(tabHome);
            selection.add(tabQds);
            selection.add(tabCard);
            if (DlHomeFragment == null)
                DlHomeFragment = new HomeMainFragment();
            if (qdsMainNewFragment == null)
                qdsMainNewFragment = new QdsMainNewFragment();
            if (DlCardMainFragment == null)
                DlCardMainFragment = new CardMainFragment();
            pages.add(DlHomeFragment);
            pages.add(qdsMainNewFragment);
            pages.add(DlCardMainFragment);
        }

        adapter.notifyDataSetChanged();
//        for (int i = 0; i < selection.size(); i++) {
//            selection.get(i).setSelected(false);
//        }

        selection.get(0).setSelected(true);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ppp", "resume");
        SharedPreferences share = getSharedPreferences(BaseCom.SESSION, MODE_PRIVATE);
        String session = share.getString("session_token", "");
        if (session.equals("")) {
            AppManager.getAppManager().finishActivity();
        } else {
            initTab();
            getMessage();
        }
    }

    private void getMessage() {
        if (BaseCom.index_main == 0) {
            Log.d("aaa", "message");
            dialog.getTvTitle().setText("正在获取数据");
            dialog.show();
//            if (Util.isLog(MainActivity.this)) {
            HttpPost<PostNoticeList> httpPost1 = new HttpPost<>();
            PostNoticeList postNoticeList = new PostNoticeList();
            postNoticeList.setSession_token(Util.getLocalAdmin(MainActivity.this)[0]);
            postNoticeList.setPage("1");
            postNoticeList.setLinage("10");
            postNoticeList.setType(2);
            httpPost1.setApp_key(Util.encode(BaseCom.APP_KEY));
            httpPost1.setParameter(postNoticeList);
            httpPost1.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postNoticeList) + BaseCom.APP_PWD));
            new OtherHttp().noticeList(OtherRequest.noticeList(0, MainActivity.this, dialog, new SuccessValue<RequestNoticeList>() {
                @Override
                public void OnSuccess(RequestNoticeList value) {
                    BaseCom.index_main = 1;
                    String update = value.getNotice()[0].getUpdateDate();
                    Log.d("ccc", "update:" + update);
                    SharedPreferences share = getSharedPreferences(BaseCom.MESSAGE, MODE_PRIVATE);
                    String time = share.getString("time", "0");
                    Log.d("ccc", "now:" + time);
                    if (time.equals("0")) {
                        Log.d("ccc", "11");
                        SharedPreferences.Editor edit = share.edit(); //编辑文件
                        edit.putString("time", update);
                        edit.commit();  //保存数据信息
                    } else {
                        if (Util.compareSecond(time, update)) {
                            Log.d("ccc", "22");
                            imgNew.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("ccc", "33");
                            imgNew.setVisibility(View.GONE);
                        }
                    }


                }
            }), httpPost1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
        Util.stopSub();
        BaseCom.index_home = 0;
        BaseCom.index_main = 0;

        int phoneRecharge = Util.getClickTime(MainActivity.this, BaseCom.phoneRecharge);
        int accountRecharge = Util.getClickTime(MainActivity.this, BaseCom.accountRecharge);
        int transform = Util.getClickTime(MainActivity.this, BaseCom.transform);
        int renewOpen = Util.getClickTime(MainActivity.this, BaseCom.renewOpen);
        int newOpen = Util.getClickTime(MainActivity.this, BaseCom.newOpen);
        int replace = Util.getClickTime(MainActivity.this, BaseCom.replace);
        int phoneBanlance = Util.getClickTime(MainActivity.this, BaseCom.phoneBanlance);
        int accountRecord = Util.getClickTime(MainActivity.this, BaseCom.accountRecord);
        int cardQuery = Util.getClickTime(MainActivity.this, BaseCom.cardQuery);
        int orderQueryRenew = Util.getClickTime(MainActivity.this, BaseCom.orderQueryRenew);
        int orderQueryNew = Util.getClickTime(MainActivity.this, BaseCom.orderQueryNew);
        int orderQueryTransform = Util.getClickTime(MainActivity.this, BaseCom.orderQueryTransform);
        int orderQueryReplace = Util.getClickTime(MainActivity.this, BaseCom.orderQueryReplace);
        int orderQueryRecharge = Util.getClickTime(MainActivity.this, BaseCom.orderQueryRecharge);
        int qdsList = Util.getClickTime(MainActivity.this, BaseCom.qdsList);
        int qdsOrderList = Util.getClickTime(MainActivity.this, BaseCom.qdsOrderList);

        HttpPost<PostFunction> httpPost = new HttpPost<>();
        PostFunction postFunction = new PostFunction();
        if (phoneRecharge != 0) {
            postFunction.setPhoneRecharge(phoneRecharge);
        }
        if (accountRecharge != 0) {
            postFunction.setAccountRecharge(accountRecharge);
        }
        if (transform != 0) {
            postFunction.setTransform(transform);
        }
        if (renewOpen != 0) {
            postFunction.setRenewOpen(renewOpen);
        }
        if (newOpen != 0) {
            postFunction.setNewOpen(newOpen);
        }
        if (replace != 0) {
            postFunction.setReplace(replace);
        }
        if (phoneBanlance != 0) {
            postFunction.setPhoneBanlance(phoneBanlance);
        }
        if (accountRecord != 0) {
            postFunction.setAccountRecord(accountRecord);
        }
        if (cardQuery != 0) {
            postFunction.setCardQuery(cardQuery);
        }
        if (orderQueryRenew != 0) {
            postFunction.setOrderQueryRenew(orderQueryRenew);
        }
        if (orderQueryNew != 0) {
            postFunction.setOrderQueryNew(orderQueryNew);
        }
        if (orderQueryTransform != 0) {
            postFunction.setOrderQueryTransform(orderQueryTransform);
        }
        if (orderQueryReplace != 0) {
            postFunction.setOrderQueryReplace(orderQueryReplace);
        }
        if (orderQueryRecharge != 0) {
            postFunction.setOrderQueryRecharge(orderQueryRecharge);
        }
        if (qdsList != 0) {
            postFunction.setQdsList(qdsList);
        }
        if (qdsOrderList != 0) {
            postFunction.setQdsOrderList(qdsOrderList);
        }
        httpPost.setApp_key(Util.encode(BaseCom.APP_KEY));
        httpPost.setParameter(postFunction);
        httpPost.setApp_sign(Util.encode(BaseCom.APP_PWD + gson.toJson(postFunction) + BaseCom.APP_PWD));
        new OtherHttp().functionStatistics(OtherRequest.functionStatistics(new SuccessNull() {
            @Override
            public void onSuccess() {
                Util.cleartClickTime(MainActivity.this, BaseCom.phoneRecharge);
                Util.cleartClickTime(MainActivity.this, BaseCom.accountRecharge);
                Util.cleartClickTime(MainActivity.this, BaseCom.transform);
                Util.cleartClickTime(MainActivity.this, BaseCom.renewOpen);
                Util.cleartClickTime(MainActivity.this, BaseCom.newOpen);
                Util.cleartClickTime(MainActivity.this, BaseCom.replace);
                Util.cleartClickTime(MainActivity.this, BaseCom.phoneBanlance);
                Util.cleartClickTime(MainActivity.this, BaseCom.accountRecord);
                Util.cleartClickTime(MainActivity.this, BaseCom.cardQuery);
                Util.cleartClickTime(MainActivity.this, BaseCom.orderQueryRenew);
                Util.cleartClickTime(MainActivity.this, BaseCom.orderQueryNew);
                Util.cleartClickTime(MainActivity.this, BaseCom.orderQueryTransform);
                Util.cleartClickTime(MainActivity.this, BaseCom.orderQueryReplace);
                Util.cleartClickTime(MainActivity.this, BaseCom.orderQueryRecharge);
                Util.cleartClickTime(MainActivity.this, BaseCom.qdsList);
                Util.cleartClickTime(MainActivity.this, BaseCom.qdsOrderList);
                Log.d("aaa", "success");
            }
        }), httpPost);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (position == 0) {
            Log.d("wwwww0", position + "");
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.d("wwwww2", position + "");
        for (int i = 0; i < selection.size(); i++) {
            if (position == i) {
                selection.get(i).setSelected(true);
            } else {
                selection.get(i).setSelected(false);
            }
        }
//        if (position == 0) {
//            homeMainFragment.getScrollView().smoothScrollTo(0, 0);
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d("wwwww1", state + "");
    }

    @Override
    public void onClick(View v) {
        if (Util.getLocalAdmin(MainActivity.this)[1].equals("lev3")) {
            switch (v.getId()) {
                case R.id.tab_home:
                    mainViewpager.setCurrentItem(0, true);
                    break;
                case R.id.tab_order:
                    mainViewpager.setCurrentItem(1, true);
                    break;
                case R.id.tab_account:
                    mainViewpager.setCurrentItem(2, true);
                    break;
                case R.id.tab_card:
                    mainViewpager.setCurrentItem(3, true);
                    break;
                default:
                    break;
            }
        } else {
            switch (v.getId()) {
                case R.id.tab_home:
                    mainViewpager.setCurrentItem(0, true);
                    break;
                case R.id.tab_qds:
                    mainViewpager.setCurrentItem(1, true);
                    break;
                case R.id.tab_card:
                    mainViewpager.setCurrentItem(2, true);
                    break;
                default:
                    break;
            }
        }


    }

    @OnClick(R.id.imgMessage)
    public void onClick() {
        imgNew.setVisibility(View.GONE);
        gotoActy(MessageMainActivity.class);
    }

    private class MyAdapter extends FragmentPagerAdapter {
        private FragmentTransaction mCurTransaction = null;
        private Fragment mCurrentPrimaryItem = null;
        FragmentManager mFragmentManager;

        public MyAdapter(FragmentManager fm) {
            super(fm);
            this.mFragmentManager = fm;

        }

        /**
         * 根据viewId和项Id生成Fragment名称
         *
         * @param viewId
         * @param id
         * @return Fragment名称
         */
        private String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }

        @Override
        public int getCount() {
            return pages.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return pages.get(arg0);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d("uuuuu0", position + "");
            return super.instantiateItem(container, position);

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.d("uuuuu1", position + "");
            super.destroyItem(container, position, object);
        }
        //        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            if (mCurTransaction == null) {
//                // 创建新事务
//                mCurTransaction = mFragmentManager.beginTransaction();
//            }
//
//            // 获取单项的Id
//            final long itemId = getItemId(position);
//
//            // 根据View的Id和单项Id生成名称
//            String name = makeFragmentName(container.getId(), itemId);
//            // 根据生成的名称获取FragmentManager中的Fragment
//            Fragment fragment = mFragmentManager.findFragmentByTag(name);
//            if (fragment != null) {
////                if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
//                // 如果Fragment已被添加到FragmentManager中,则只需要附着到Activity
//                mCurTransaction.attach(fragment);
//            } else {
//                // 如果Fragment未被添加到FragmentManager中,则先获取,再添加到Activity中
//                fragment = getItem(position);
////                if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
//                mCurTransaction.add(container.getId(), fragment, makeFragmentName(container.getId(), itemId));
//            }
//            // 非当前主要项,需要隐藏相关的菜单及信息
//            if (fragment != mCurrentPrimaryItem) {
//                fragment.setMenuVisibility(false);
//                fragment.setUserVisibleHint(false);
//            }
//
//            return fragment;
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            if (mCurTransaction == null) {
//                // 创建新事务
//                mCurTransaction = mFragmentManager.beginTransaction();
//            }
////            if (DEBUG)
////                Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object + " v=" + ((Fragment) object).getView());
//            // 将Fragment移出UI,但并未从FragmentManager中移除
//            mCurTransaction.detach((Fragment) object);
//        }
//
//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            Fragment fragment = (Fragment) object;
//            if (fragment != mCurrentPrimaryItem) {
//                // 主要项切换,相关菜单及信息进行切换
//                if (mCurrentPrimaryItem != null) {
//                    mCurrentPrimaryItem.setMenuVisibility(false);
//                    mCurrentPrimaryItem.setUserVisibleHint(false);
//                }
//                if (fragment != null) {
//                    fragment.setMenuVisibility(true);
//                    fragment.setUserVisibleHint(true);
//                }
//                mCurrentPrimaryItem = fragment;
//            }
//        }
//
//        @Override
//        public void finishUpdate(ViewGroup container) {
//            if (mCurTransaction != null) {
//                // 提交事务
//                mCurTransaction.commitAllowingStateLoss();
//                mCurTransaction = null;
//                // 立即运行等待中事务
//                mFragmentManager.executePendingTransactions();
//            }
//        }
//
//        @Override
//        public boolean isViewFromObject(View view, Object object) {
//            return ((Fragment) object).getView() == view;
//        }

//        @Override
//        public Parcelable saveState() {
//            return null;
//        }
//
//        @Override
//        public void restoreState(Parcelable state, ClassLoader loader) {
//        }


    }


}
