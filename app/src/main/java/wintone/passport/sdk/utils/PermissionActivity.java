package wintone.passport.sdk.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

import com.test.tworldapplication.R;

import wintone.passportreader.sdk.CameraActivity;


/**
 * Created by LaiYingtang on 2016/5/18. �û�Ȩ�޻�ȡҳ�棬Ȩ�޴���
 */

public class PermissionActivity extends Activity {
	// ��������Ȩ����Ȩ
	public static final int PERMISSION_GRANTED = 0;// ��ʶȨ����Ȩ
	public static final int PERMISSION_DENIEG = 1;// Ȩ�޲��㣬Ȩ�ޱ��ܾ���ʱ��
	private static final int PERMISSION_REQUEST_CODE = 0;// ϵͳ��Ȩ����ҳ��ʱ�Ľ������
	private static final String EXTRA_PERMISSION = "com.wintone.permissiondemo";// Ȩ�޲���
	private static final String PACKAGE_URL_SCHEME = "package:";// Ȩ�޷���
	private CheckPermission checkPermission;// ���Ȩ�����Ȩ�޼����
	private boolean isrequestCheck;// �ж��Ƿ���ҪϵͳȨ�޼�⡣��ֹ��ϵͳ��ʾ���ص�
	private static int is_nMainId;
	private static String is_devcode;
	private static int is_flag;
	private static int VehicleLicenseflag;

	// ������ǰȨ��ҳ��Ĺ����ӿ�
	public static void startActivityForResult(Activity activity,
											  int requestCode, int nMainId, String devcode, int flag,
											  int VehicleLicenseflag, String... permission) {
		is_nMainId = nMainId;
		is_devcode = devcode;
		is_flag = flag;
		VehicleLicenseflag = VehicleLicenseflag;
		Intent intent = new Intent(activity, PermissionActivity.class);
		intent.putExtra(EXTRA_PERMISSION, permission);
		intent.putExtra("nMainId", nMainId);
		intent.putExtra("devcode", devcode);
		intent.putExtra("VehicleLicenseflag", VehicleLicenseflag);
		intent.putExtra("flag", 0);
		ActivityCompat.startActivityForResult(activity, intent, requestCode,
				null);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.permission_layout);
		if (getIntent() == null || !getIntent().hasExtra(EXTRA_PERMISSION))// ����������������õ�Ȩ�޲���ʱ
		{
			throw new RuntimeException(
					"��ǰActivity��Ҫʹ�þ�̬��StartActivityForResult��������");// �쳣��ʾ
		}
		checkPermission = new CheckPermission(this);
		isrequestCheck = true;// �ı���״̬
	}

	// �����֮�������û���Ȩ
	@Override
	protected void onResume() {
		super.onResume();
		if (isrequestCheck) {
			String[] permission = getPermissions();
			if (checkPermission.permissionSet(permission)) {
				requestPermissions(permission); // ȥ����Ȩ��
			} else {
				allPermissionGranted();// ��ȡȫ��Ȩ��
			}
		} else {
			isrequestCheck = true;
		}
	}

	// ��ȡȫ��Ȩ��
	private void allPermissionGranted() {
		setResult(PERMISSION_GRANTED);
		Intent intent = new Intent(this, CameraActivity.class);
		intent.putExtra("nMainId", is_nMainId);
		intent.putExtra("devcode", is_devcode);
		intent.putExtra("flag", is_flag);
		intent.putExtra("VehicleLicenseflag", VehicleLicenseflag);
		startActivity(intent);
		finish();
	}

	// ����Ȩ��ȥ���ݰ汾
	@TargetApi(Build.VERSION_CODES.M)
	private void requestPermissions(String... permission) {
		PermissionActivity.this.requestPermissions(permission,PERMISSION_REQUEST_CODE);
	}

	// ���ش��ݹ�����Ȩ�޲���
	private String[] getPermissions() {
		return getIntent().getStringArrayExtra(EXTRA_PERMISSION);
	}

	/**
	 * ����Ȩ�޹��� ���ȫ����Ȩ�Ļ�����ֱ��ͨ������ ���Ȩ�޾ܾ���ȱʧȨ��ʱ����ʹ��dialog��ʾ
	 *
	 * @param requestCode
	 *            �������
	 * @param permissions
	 *            Ȩ�޲���
	 * @param grantResults
	 *            ���
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (PERMISSION_REQUEST_CODE == requestCode
				&& hasAllPermissionGranted(grantResults)) // �ж����������������Ƿ�һ��
		{
			isrequestCheck = true;// ��Ҫ���Ȩ�ޣ�ֱ�ӽ��룬������ʾ�Ի����������
			allPermissionGranted(); // ����
		} else { // ��ʾ�Ի�������
			isrequestCheck = false;
			// showMissingPermissionDialog();//dialog
			Toast.makeText(this, "����ֹ�˴�Ȩ�ޣ���ѡ������", Toast.LENGTH_SHORT).show();
			finish();
		}

	}

	// ��ʾ�Ի�����ʾ�û�ȱ��Ȩ��
	// private void showMissingPermissionDialog() {
	// AlertDialog.Builder builder = new
	// AlertDialog.Builder(PermissionActivity.this);
	// builder.setTitle(R.string.help);//��ʾ����
	// builder.setMessage(R.string.string_help_text);
	//
	// //����Ǿܾ���Ȩ�����˳�Ӧ��
	// //�˳�
	// builder.setNegativeButton(R.string.quit, new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// setResult(PERMISSION_DENIEG);//Ȩ�޲���
	// finish();
	// }
	// });
	// //�����ã����û�ѡ���Ȩ��
	// builder.setPositiveButton(R.string.settings, new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int which) {
	// startAppSettings();//������
	// }
	// });
	// builder.setCancelable(false);
	// builder.show();
	// }

	// ��ȡȫ��Ȩ��
	private boolean hasAllPermissionGranted(int[] grantResults) {
		for (int grantResult : grantResults) {
			if (grantResult == PackageManager.PERMISSION_DENIED) {
				return false;
			}
		}
		return true;
	}

	// ��ϵͳӦ������(ACTION_APPLICATION_DETAILS_SETTINGS:ϵͳ����Ȩ��)
	private void startAppSettings() {
		Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
		intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
		startActivity(intent);
	}

}
