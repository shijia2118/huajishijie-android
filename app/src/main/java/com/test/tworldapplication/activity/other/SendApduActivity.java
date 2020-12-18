package com.test.tworldapplication.activity.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.plk.bluetoothlesdk.PlkBleConnectCallback;
import com.plk.bluetoothlesdk.PlkBleService;
import com.plk.bluetoothlesdk.PlkException;
import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseActivity;

public class SendApduActivity extends BaseActivity implements OnClickListener,
		PlkBleConnectCallback {
	private EditText et_apdu;
	private TextView tv_apdu_logs, tv_title;
	private Button bt_send;
	private Button bt_clear;
	private Button bt_reset;
	// private Button bt_connect;

	private PlkBleService bleService;

	private Context mContext;
	private int deviceIndexSelected;
	private String deviceNameSelected;

	private String TAG = "SendApduActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_apdu);

		mContext = this;

		final Intent intent = getIntent();
		deviceIndexSelected = intent.getIntExtra(
				ScanDeviceActivity.EXTRAS_DEVICE_SELECTED_INDEX, 0);
		deviceNameSelected = intent
				.getStringExtra(ScanDeviceActivity.EXTRAS_DEVICE_NAME);

		initial_ui();

		bleService = new PlkBleService(mContext);

	}

	@Override
	protected void onResume() {
		super.onResume();

		try {
			bleService.connectDevice(deviceIndexSelected, this);
		} catch (PlkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}

	private void initial_ui() {
		et_apdu = (EditText) this.findViewById(R.id.edittext_apdu);

		tv_title = (TextView) this.findViewById(R.id.textview_head_title);
		tv_title.setText("正在连接" + deviceNameSelected);

		tv_apdu_logs = (TextView) this.findViewById(R.id.textview_apdus);

		bt_send = (Button) this.findViewById(R.id.button_send);
		bt_send.setOnClickListener(this);
		// bt_send.setEnabled(false);

		bt_reset = (Button) this.findViewById(R.id.button_reset);
		bt_reset.setOnClickListener(this);
		// bt_reset.setEnabled(false);

		// bt_connect = (Button) this.findViewById(R.id.button_connect);
		// bt_connect.setOnClickListener(this);
		// bt_debug.setEnabled(false);

		bt_clear = (Button) this.findViewById(R.id.button_clear);
		bt_clear.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.button_send) {
			String apdu = et_apdu.getText().toString().trim();
			if (apdu.equals("")) {
				Toast.makeText(mContext, "命令不能为空！", Toast.LENGTH_LONG).show();
			} else {
				try {
					addLog(apdu, 1);
					String resp = bleService.transmitDataSync(apdu);
					addLog(resp, 2);
				} catch (PlkException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			}
		} else if (v.getId() == R.id.button_reset) {
			String atr;
			try {
				addLog("0062", 1);
				atr = bleService.resetDevice();
				addLog(atr, 0);

				bt_send.setEnabled(true);
				// bt_connect.setEnabled(true);
			} catch (PlkException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG)
						.show();
			}
		} else if (v.getId() == R.id.button_clear) {
			tv_apdu_logs.setText("");
		}
	}

	@Override
	protected void onDestroy() {
		System.err.println("ScanDeviceActivity onDestroy........");

		bleService.destroy();
		super.onDestroy();
	}

	private void addLog(String log, int logType) {
		String oldLogs = tv_apdu_logs.getText().toString().trim();

		if (logType == 0) // ATR
			tv_apdu_logs.setText(oldLogs + "\n" + "ATR:" + log);
		else if (logType == 1) // SEND
			tv_apdu_logs.setText(oldLogs + "\n" + "send:" + log);
		else
			tv_apdu_logs.setText(oldLogs + "\n" + "receive:" + log);
	}

	@Override
	public void onConnectSuccess() {
		// TODO Auto-generated method stub
		bt_reset.setEnabled(true);
	}

	@Override
	public void onConnectFailed(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectLost() {
		// TODO Auto-generated method stub
		bt_reset.setEnabled(false);
	}
}
