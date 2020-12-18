package wintone.passport.sdk.utils;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 *
 * ���Ȩ�޵Ĺ�����
 */
public class CheckPermission {
	private final Context context;
	//������
	public CheckPermission(Context context) {
		this.context = context.getApplicationContext();
	}
	//���Ȩ��ʱ���ж�ϵͳ��Ȩ�޼���
	public boolean permissionSet(String... permissions) {
		for (String permission : permissions) {
			if (isLackPermission(permission)) {//�Ƿ������ȫ��Ȩ�޼���
				return true;
			}
		}
		return false;
	}
	//���ϵͳȨ���ǣ��жϵ�ǰ�Ƿ�ȱ��Ȩ��(PERMISSION_DENIED:Ȩ���Ƿ��㹻)
	private boolean isLackPermission(String permission) {
		return context.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED;
	}
}
