/**
 *
 */
package wintone.passportreader.sdk;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.test.tworldapplication.R;
import com.test.tworldapplication.base.BaseCom;

import java.io.File;

import wintone.passport.sdk.utils.AppManager;
import wintone.passport.sdk.utils.SharedPreferencesHelper;

/**
 * 项目名称：PassportReader_Sample_Sdk 类名称：ShowResultActivity 类描述： 创建人：yujin
 * 创建时间：2015-6-12 上午10:25:47 修改人：yujin 修改时间：2015-6-12 上午10:25:47 修改备注：
 */
public class ShowResultActivity extends Activity implements OnClickListener {
    private DisplayMetrics displayMetrics = new DisplayMetrics();
    private int srcWidth, srcHeight;
    private EditText et_recogPicture;
    private String recogResult = "";
    private String exception;
    private String[] splite_Result;
    private String result = "";
    private Button btDone, btn_repeat_takePic, btn_back, btn_save_full_picture;
    private String devcode = "";
    private String fullPagePath = "";
    private boolean isSaveFullPic = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
        // 屏幕常亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        srcWidth = displayMetrics.widthPixels;
        srcHeight = displayMetrics.heightPixels;
        setContentView(R.layout.activity_show_result);
        Intent intent = getIntent();
        recogResult = intent.getStringExtra("recogResult");
        exception = intent.getStringExtra("exception");
        devcode = intent.getStringExtra("devcode");
        fullPagePath = intent.getStringExtra("fullPagePath");
        findView();
        //需要释放掉相机界面 start
        AppManager.getAppManager().finishAllActivity();
    }

    /**
     * @param
     * @return void 返回类型
     * @throws
     * @Title: findView
     * @Description: TODO(这里用一句话描述这个方法的作用)
     */
    private void findView() {
        // TODO Auto-generated method stub
        et_recogPicture = (EditText) this.findViewById(R.id.et_recogPicture);
        btDone = (Button) this.findViewById(R.id.btnDone);
        btDone.setOnClickListener(this);
        btn_repeat_takePic = (Button) this
                .findViewById(R.id.btn_repeat_takePic);
        btn_repeat_takePic.setOnClickListener(this);
        btn_back = (Button) this.findViewById(R.id.btn_back);
        btn_save_full_picture = (Button) this
                .findViewById(R.id.btn_save_full_picture);
        btn_back.setOnClickListener(this);
        btn_save_full_picture.setOnClickListener(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                srcWidth, (int) (srcHeight * 0.9));
        et_recogPicture.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams((int) (srcWidth * 0.15),
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.BELOW, R.id.et_recogPicture);
        btn_back.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams((int) (srcWidth * 0.15),
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(RelativeLayout.BELOW, R.id.et_recogPicture);
        btn_repeat_takePic.setLayoutParams(params);
        params = new RelativeLayout.LayoutParams((int) (srcWidth * 0.15),
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.LEFT_OF, R.id.btn_back);
        params.addRule(RelativeLayout.BELOW, R.id.et_recogPicture);
        btn_save_full_picture.setLayoutParams(params);

        if (exception != null && !exception.equals("")) {
            et_recogPicture.setText(exception);
        } else {
            splite_Result = recogResult.split(",");
            for (int i = 0; i < splite_Result.length; i++) {
                if (result.equals("")) {
                    result = splite_Result[i] + "\n";
                } else {
                    result = result + splite_Result[i] + "\n";
                }
                Log.d("ccc", splite_Result[i]);
                Log.d("ccc", "ccc");
            }
            et_recogPicture.setText(result);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnDone:
                Log.d("aaa","1");
                SharedPreferences share = ShowResultActivity.this.getSharedPreferences(BaseCom.ID, MODE_PRIVATE);
                SharedPreferences.Editor edit = share.edit(); //编辑文件
                edit.clear();
                edit.putString("name", splite_Result[0].substring(3));
                edit.putString("sex", splite_Result[1].substring(3));
                edit.putString("people", splite_Result[2].substring(3));
                edit.putString("birth", splite_Result[3].substring(3));
                edit.putString("address", splite_Result[4].substring(3));
                edit.putString("id", splite_Result[5].substring(7));
                edit.commit();  //保存数据信息

                if (!isSaveFullPic) {
                    if (fullPagePath != null && !fullPagePath.equals("")) {
                        File file = new File(fullPagePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

//                Intent intent1 = new Intent(ShowResultActivity.this, MainActivity.class);
                ShowResultActivity.this.finish();
//                startActivity(intent1);

                break;
            case R.id.btn_repeat_takePic:
                if (!isSaveFullPic) {
                    if (fullPagePath != null && !fullPagePath.equals("")) {
                        File file = new File(fullPagePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

                Intent intent = new Intent(ShowResultActivity.this,
                        CameraActivity.class);
                intent.putExtra("nMainId", SharedPreferencesHelper.getInt(
                        getApplicationContext(), "nMainId", 2));
                intent.putExtra("devcode", devcode);
                ShowResultActivity.this.finish();
                startActivity(intent);
                break;
            case R.id.btn_back:
                if (!isSaveFullPic) {
                    if (fullPagePath != null && !fullPagePath.equals("")) {
                        File file = new File(fullPagePath);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                }

                SharedPreferences shared = getSharedPreferences(BaseCom.ID, MODE_PRIVATE);
                SharedPreferences.Editor editd = shared.edit(); //编辑文件
                editd.clear();
                editd.commit();
//                intent = new Intent(ShowResultActivity.this, MainActivity.class);
                ShowResultActivity.this.finish();
//                startActivity(intent);
                break;
            case R.id.btn_save_full_picture:
                isSaveFullPic = true;
                if (fullPagePath != null) {
                    Toast.makeText(getApplicationContext(), "保存图像成功!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (!isSaveFullPic) {
                if (fullPagePath != null && !fullPagePath.equals("")) {
                    File file = new File(fullPagePath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
            Intent intent = new Intent(ShowResultActivity.this,
                    MainActivity.class);
            ShowResultActivity.this.finish();
            startActivity(intent);
        }
        return true;
    }
}
