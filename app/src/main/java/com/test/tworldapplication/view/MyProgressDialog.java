package com.test.tworldapplication.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class MyProgressDialog {
	
	public static final int HANDLER_MSG_TYPE_ON_PROCESSING = 101; // 正在进行中
	public static final int HANDLER_MSG_TYPE_CANCELD = 0xFD; // 用户取消
	public static final int HANDLER_MSG_TYPE_TIMEOUT = 0xFE; // 超时
	public static final int HANDLER_MSG_TYPE_EXCEPTION = 0xFF; // 发生异常
	public static final int HANDLER_MSG_TYPE_SUCCESS = 0x00; // 成功
	public static final int HANDLER_MSG_TYPE_FAILED = 0x01; // 失败

	private ProgressDialog progressBar;
	private boolean isFinished = false;
	private boolean isExcepted = false;

	private Context context;
	private String title;
	private String message;
	private int style;
	private Handler hander;

	private int maxSteps = 50;
	private int progressMin = 0;
	private int progressMax = 100;
	private boolean cancelable = false;

	private boolean needReport = true;

	public void setExcept(boolean value) {
		isExcepted = value;
	}

	public void setFinish(boolean value) {
		isFinished = value;
	}

	public boolean isCancelable() {
		return cancelable;
	}

	public void setCancelable(boolean cancelable) {
		this.cancelable = cancelable;
	}

	public int getProgressMin() {
		return progressMin;
	}

	public void setProgressMin(int progressMin) {
		this.progressMin = progressMin;
	}

	public int getProgressMax() {
		return progressMax;
	}

	public void setProgressMax(int progressMax) {
		this.progressMax = progressMax;
	}

	public int getMaxSteps() {
		return maxSteps;
	}

	public void setMaxSteps(int maxSteps) {
		this.maxSteps = maxSteps;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setNeedReport(boolean needReport) {
		this.needReport = needReport;
	}

	public void setFinishAndNoNeedReport() {
		this.isFinished = true;
		this.needReport = false;
	}

	public MyProgressDialog(Context context, String title, String message,
			int style, Handler handler) {
		this.context = context;
		this.title = title;
		this.message = message;
		this.style = style;
		this.hander = handler;
	}

	private void prepareDialog() {
		progressBar = new ProgressDialog(context);
		if (title != null && title.length() > 0)
			progressBar.setTitle(title);

		progressBar.setMessage(message);
		progressBar.setProgressStyle(style);

		progressBar.setCancelable(cancelable);
		if (style == ProgressDialog.STYLE_HORIZONTAL) // 水平带进度条
		{
			progressBar.setProgress(progressMin);
			progressBar.setMax(progressMax);
		}
	}

	public void show() {
		prepareDialog();
		progressBar.show();

		isFinished = false;
		isExcepted = false;
		needReport = true;

		new Thread() {
			public void run() {
				int i;
				for (i = 0; i < maxSteps; i++) {
					try {
						if (!isFinished && !isExcepted) {
							if (style == ProgressDialog.STYLE_HORIZONTAL)
								progressBar.setProgress(i);

							Thread.sleep(400);
						} else {
							if (style == ProgressDialog.STYLE_HORIZONTAL)
								progressBar.setProgress(100);

							// isFinished = true;
							break;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (needReport) {
					if (i == maxSteps) {
						hander.sendEmptyMessage(HANDLER_MSG_TYPE_TIMEOUT); // 超时
					} else // 未完成，成功或异常
					{
						if (isExcepted) // 发生异常
						{
							hander.sendEmptyMessage(HANDLER_MSG_TYPE_EXCEPTION); // 异常
						} else {
							hander.sendEmptyMessage(HANDLER_MSG_TYPE_SUCCESS); // 成功
						}
					}
				}
			}
		}.start();
	}

	public void dismss() {
		if (progressBar != null && progressBar.isShowing()) {
			progressBar.dismiss();
		}
	}

	/**
	 * 用户主动取消
	 * 
	 * @param needFeedBack
	 */
	public void cancel(boolean needFeedBack) {
		if (cancelable && progressBar != null && progressBar.isShowing()) {
			// progressBar.dismiss();
			progressBar.cancel();

			if (needFeedBack)
				hander.sendEmptyMessage(HANDLER_MSG_TYPE_CANCELD);
		}
	}

}
