package kr.co.rinnai.dms.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
import android.view.Window;
import android.widget.Toast;

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;
import kr.co.rinnai.dms.R;

import kr.co.rinnai.dms.activity.user.LoginActivity;
import kr.co.rinnai.dms.aos.activity.AgencyMenu07Activity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.listener.PackageReceiver;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu01Activity;

@SuppressLint("ServiceCast")
public class LogoActivity extends BaseActivity {
	private int delayTime = 1000;
	private Intent clearIntent = null;
	private String deviceToken = null;
	private MySQLiteOpenHelper helper;
	private SQLiteDatabase db;

	private PackageReceiver mPackageReceiver = new PackageReceiver();




	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean isTablet = Util.isTabletDevice(LogoActivity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.act_logo);

		if (Build.VERSION.SDK_INT >= 23) {

			List<String> listPermission = new ArrayList<String>();



			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				listPermission.add(Manifest.permission.READ_PHONE_STATE);
			}
/*
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.REQUEST_INSTALL_PACKAGES) != PackageManager.PERMISSION_GRANTED) {
				listPermission.add(Manifest.permission.REQUEST_INSTALL_PACKAGES);
			}
*/
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			}

			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				listPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
			}

			if(ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
				listPermission.add(Manifest.permission.CAMERA);
			}
			/*
			if (ActivityCompat.checkSelfPermission(getApplicationContext(),
					Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
				listPermission.add(Manifest.permission.WRITE_SETTINGS);
			}
			*/

			if (listPermission.size() > 0) {
				String[] newPermission = new String[listPermission.size()];
				for (int i = 0; i < listPermission.size(); i++) {
					newPermission[i] = listPermission.get(i);

				}
				requestWifiPermission(newPermission);
				return;
			} else {
				startApp();
			}

		}

	}

	private void requestWifiPermission(String[] newPermission) {
		ActivityCompat.requestPermissions(this, newPermission, 1);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String[] permissions, int[] grantResults) {
		// TODO Auto-generated method stub
		switch (requestCode) {
			case 1:
				boolean flag = false;
				for (int grant : grantResults) {
					if (grant < 0) {
						Toast.makeText(LogoActivity.this, "권한이 없어 앱을 종료 합니다.",
								Toast.LENGTH_SHORT).show();
						flag = true;

						break;
					}

				}

				if(flag){
					finish();
				} else {
					startApp();
				}
				break;
		}
	}


	public void startApp() {
//        FirebaseApp.initializeApp(this);
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("Token", "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//                        RinnaiApp.getInstance().setFCMToken(token);
//                        // Log and toast
//                        //String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("Token", token);
////                        Toast.makeText(LogoActivity.this, token, Toast.LENGTH_SHORT).show();
//                    }
//                });

		clearIntent = getIntent();

		helper = new MySQLiteOpenHelper(
				LogoActivity.this,  // 현재 화면의 제어권자
				CommonValue.SQLITE_DB_FILE_NAME,// db 이름
				null,  // 커서팩토리-null : 표준커서가 사용됨
				CommonValue.SQLITE_DB_VERSION);       // 버전

		try {

			db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
			//db = helper.getReadableDatabase(); // 읽기 전용 DB select문
		} catch (SQLiteException e) {

		}

		if (clearIntent.getBooleanExtra(CommonValue.CLEAR_FLAG, false)) {
			finish();
		} else {

			String packageName = "kr.co.rinnai.portalapp";

			if(isInstallApp(packageName)) {
				goDeleteApp(packageName);
			} else {


				final Intent agreementIntent = new Intent(LogoActivity.this, LoginActivity.class);
//				final Intent agreementIntent = new Intent(LogoActivity.this, WebViewTest.class);
				Handler delayHandle = new Handler();
				agreementIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				delayHandle.postDelayed(new Runnable() {

					@Override
					public void run() {

						startActivity(agreementIntent);
						finish();
					}
				}, delayTime);
			}






		}
	}

	private boolean isInstallApp(String pakageName){
		Intent intent = getPackageManager().getLaunchIntentForPackage(pakageName);

		if(intent==null){
			//미설치
			return false;
		}else{
			//설치
			return true;
		}
	}

	private void goDeleteApp(String pakageName) {
		Uri packageURI = Uri.parse("package:" + "kr.co.rinnai.portalapp");
		Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI)
				.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(uninstallIntent);
		showProgress(LogoActivity.this);
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

	}

}
