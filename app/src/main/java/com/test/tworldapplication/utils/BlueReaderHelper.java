package com.test.tworldapplication.utils;

import android.content.Context;
import android.os.Handler;

import cn.com.senter.mediator.BluetoothReader;

public class BlueReaderHelper
{
	private Context context;

	private final BluetoothReader blueCardReader;

	public BlueReaderHelper(Context context, Handler handler)
	{
		this.context=context;
		blueCardReader = new BluetoothReader(handler, context);
	}

	public String read()
	{
		return blueCardReader.readCard_Sync();
	}

	public boolean registerBlueCard(String address)
	{
		return blueCardReader.registerBlueCard(address);
	}

	public int readSimICCID(byte [] iccid){
		return blueCardReader.readSimICCID(iccid);
	}

	public int writeSimCard(String imsi, String smsNo){
		return blueCardReader.writeSimCard(imsi, smsNo);
	}

	public void setServerAddress(String server_address) {
		blueCardReader.setServerAddress(server_address);
	}

	public void setServerPort(int server_port) {
		blueCardReader.setServerPort(server_port);
	}

	public void OnDestroy(){
		blueCardReader.OnDestroy();
	}

	public boolean SimInit(){
		return blueCardReader.SimInit();
	}

	public String  TransmitCard(String mAPDU){
		return blueCardReader.TransmitCard(mAPDU);
	}

	public boolean unRegisterBlueCard(){
		return blueCardReader.unRegisterBlueCard();
	}

	public String readImsi(){
		return blueCardReader.readIMSI();
	}

	public String getCardAID(){
		return blueCardReader.getCardAID();
	}
}

