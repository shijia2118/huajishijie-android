package com.test.tworldapplication.adapter;

import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.plk.bluetoothlesdk.PlkBleDeviceInfo;
import com.test.tworldapplication.R;
//import com.test.tworldapplication.utils.bluetooth.PlkBleDeviceInfo;

public class DeviceAdapter extends BaseAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;

    List<PlkBleDeviceInfo> list;

    class ViewHolder {

        ViewHolder() {
        }

        TextView deviceName;
        TextView deviceDistance;
        TextView deviceAddress;
    }

    public DeviceAdapter(Activity activity) {
        this.activity = activity;

        this.layoutInflater = activity.getLayoutInflater();
    }

    public final void setList(List<PlkBleDeviceInfo> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        // TODO Auto-generated method stub
        if ((this.list != null) && (this.list.size() > 0))
            return this.list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int index) {
        // TODO Auto-generated method stub
        return this.list.get(index);
    }

    @Override
    public long getItemId(int index) {
        // TODO Auto-generated method stub
        return index;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        // TODO Auto-generated method stub
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = this.layoutInflater.inflate(
                    R.layout.list_item_device, null);
            viewHolder.deviceName = ((TextView) convertView
                    .findViewById(R.id.textview_device_name));
            viewHolder.deviceDistance = ((TextView) convertView
                    .findViewById(R.id.textview_device_distance));
            viewHolder.deviceAddress = ((TextView) convertView
                    .findViewById(R.id.textview_device_address));
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PlkBleDeviceInfo deviceInfo = list.get(position);
        viewHolder.deviceName.setText(deviceInfo.getDevice().getName());
        viewHolder.deviceDistance.setText(deviceInfo.getDistance() + "m");
        viewHolder.deviceAddress.setText(deviceInfo.getDevice().getAddress());

        return convertView;
    }
}
