package com.test.tworldapplication.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.mapapi.search.core.PoiInfo;
import com.test.tworldapplication.R;
import java.util.List;

/**
 * @author czc
 * @since 2021/1/14
 */
public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.Holder> {

  List<PoiInfo> list;

  public AddressAdapter(List<PoiInfo> list) {
    this.list = list;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    holder.setData(list.get(position));
  }

  @Override
  public int getItemCount() {
    if (list == null) return 0;
    return list.size();
  }

  static class Holder extends RecyclerView.ViewHolder {

    PoiInfo suggestionInfo;

    TextView nameTv;
    TextView subNameTv;


    public Holder(@NonNull View itemView) {
      super(itemView);
      nameTv = itemView.findViewById(R.id.nameTv);
      subNameTv = itemView.findViewById(R.id.subNameTv);
      itemView.setOnClickListener(v -> {
        Intent intent = new Intent();
        intent.putExtra("data", this.suggestionInfo);
        ((Activity) itemView.getContext()).setResult(Activity.RESULT_OK, intent);
        ((Activity) itemView.getContext()).finish();
      });

    }
    void setData(PoiInfo suggestionInfo) {
      this.suggestionInfo = suggestionInfo;
      nameTv.setText(suggestionInfo.getName());
      subNameTv.setText(suggestionInfo.getAddress());
    }
  }
}
