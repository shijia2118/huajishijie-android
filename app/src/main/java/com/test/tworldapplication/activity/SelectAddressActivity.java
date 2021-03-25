package com.test.tworldapplication.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiDetailInfo;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchResult;
import com.baidu.mapapi.search.poi.PoiFilter;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.google.gson.Gson;
import com.luck.picture.lib.tools.ToastUtils;
import com.test.tworldapplication.R;
import com.test.tworldapplication.activity.admin.WriteInNewActivity;
import com.test.tworldapplication.adapter.AddressAdapter;
import com.test.tworldapplication.base.BaseActivity;
import java.util.ArrayList;
import java.util.List;

/**
 * @author czc
 * @since 2021/1/14
 */
public class SelectAddressActivity extends BaseActivity {

  TextView locationTv;
  RecyclerView recyclerView;
  EditText searchEt;
  AddressAdapter addressAdapter;
  List<PoiInfo> list = new ArrayList<>();
  PoiSearch mPoiSearch;
  private GeoCoder geoCoder;


  OnGetPoiSearchResultListener listener = new OnGetPoiSearchResultListener() {
    @Override
    public void onGetPoiResult(PoiResult poiResult) {
      list.clear();
      if (poiResult.getAllPoi() != null) {
        list.addAll(poiResult.getAllPoi());
      }
      addressAdapter.notifyDataSetChanged();
    }
    @Override
    public void onGetPoiDetailResult(PoiDetailSearchResult poiDetailSearchResult) {
      Log.i("info", poiDetailSearchResult.toString());
    }
    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }
    //废弃
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }
  };

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_address);
    setBackGroundTitle("地址选择", true);
    locationTv = findViewById(R.id.locationTv);
    recyclerView = findViewById(R.id.recyclerView);
    searchEt = findViewById(R.id.searchEt);
    findViewById(R.id.clearIv).setOnClickListener(v -> {
      searchEt.setText("");
    });
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    addressAdapter = new AddressAdapter(list);
    recyclerView.setAdapter(addressAdapter);

    mPoiSearch = PoiSearch.newInstance();
    mPoiSearch.setOnGetPoiSearchResultListener(listener);
//    mSuggestionSearch = SuggestionSearch.newInstance();
//    mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
//      @Override
//      public void onGetSuggestionResult(SuggestionResult suggestionResult) {
//        System.out.println("ddd");
//      }
//    });
    searchEt.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
        if (TextUtils.isEmpty(s)) {
          list.clear();
          addressAdapter.notifyDataSetChanged();
        }

        mPoiSearch.searchInCity(new PoiCitySearchOption()
            .city(TextUtils.isEmpty(locationTv.getText()) ? "杭州市" : locationTv.getText().toString())
            .pageCapacity(20)
                .cityLimit(false)
            .keyword(s.toString()));
//        mSuggestionSearch.requestSuggestion(new SuggestionSearchOption()
//                .city(TextUtils.isEmpty(locationTv.getText()) ? "杭州市" : locationTv.getText().toString())
//                .keyword(s.toString()));

      }
    });
    initLocation();


  }


  LocationClient mLocationClient;
  private void initLocation() {
    //定位初始化
    geoCoder = GeoCoder.newInstance();
    geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
      @Override
      public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

      }

      @Override
      public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
          ToastUtils.s(SelectAddressActivity.this, "获取位置信息失败");
          return;
        } else {
          locationTv.setText(reverseGeoCodeResult.getAddressDetail().city);
        }
      }
    });
    mLocationClient = new LocationClient(this);
    //通过LocationClientOption设置LocationClient相关参数
    LocationClientOption option = new LocationClientOption();
    option.setOpenGps(true); // 打开gps
    option.setCoorType("bd09ll"); // 设置坐标类型
    option.setScanSpan(0);

    //设置locationClientOption
    mLocationClient.setLocOption(option);

    //注册LocationListener监听器
    mLocationClient.registerLocationListener(bdLocation -> {
      if (bdLocation != null) {
        geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
            .location(new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude()))
            .newVersion(1)
            .radius(100)
        );

      }
    });
    //开启地图定位图层
    mLocationClient.start();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mPoiSearch.destroy();
  }
}
