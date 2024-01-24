package kr.co.rinnai.dms.activity.user;

import android.app.AlertDialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;

import java.lang.reflect.Type;

import java.util.HashMap;
import java.util.Map;

import androidx.core.content.FileProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.activity.LogoActivity;
import kr.co.rinnai.dms.aos.activity.AgencyActivity;
import kr.co.rinnai.dms.aos.activity.AgencyMenu02Activity;
import kr.co.rinnai.dms.aos.activity.SalespersonActivity;
import kr.co.rinnai.dms.aos.activity.SalespersonManagerActivity;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.http.CheckConnect;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.service.ApiService;
import kr.co.rinnai.dms.common.service.DownloadNotifiaiontService;
import kr.co.rinnai.dms.common.util.LogUtil;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.activity.EmployeeActivity;
import kr.co.rinnai.dms.sie.activity.RetailerActivity;
import kr.co.rinnai.dms.udd.activity.UseDeliveryDriverActivity;
import kr.co.rinnai.dms.wms.activity.WmsFragmentActivity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.LoginResult;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.BuildConfig;
import kr.co.rinnai.dms.wms.activity.WmsMainActivity;


public class  LoginActivity extends BaseActivity implements View.OnKeyListener {

    private int delayTime = 1000;

    private BroadcastReceiver mReceiver = null;

    private EditText etUserId;
    private EditText etPassword;

    private CheckBox cbSaveId,cbRinnaiId;

    private MySQLiteOpenHelper helper;

    private SQLiteDatabase db;

    private TextView appVersion;
    private int versionCode = BuildConfig.VERSION_CODE;
    private String versionName = BuildConfig.VERSION_NAME;

    private ProgressBar progressBar;
    private TextView textView;
    private boolean networkConnecting = false;
    private RelativeLayout btnLogin;

    private RelativeLayout btnMenu01, btnMenu02, btnMenu03, btnMenu04, btnMenu05, btnMenu06;
    private LinearLayout llMasterMenu;

    MediaPlayer mAudio = null;


//    private DownloadFileFromURL downloadFileAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());


        boolean isTablet = Util.isTabletDevice(LoginActivity.this);

        if(!isTablet) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        btnLogin = (RelativeLayout)findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(LoginActivity.this);

        cbSaveId = (CheckBox) findViewById(R.id.cb_save_id);
        cbRinnaiId = (CheckBox) findViewById(R.id.cb_rinnai_id);

//        cbSaveId.setOnCheckedChangeListener(LoginActivity.this);

        etUserId = (EditText) findViewById(R.id.etUserId);
        etPassword = (EditText) findViewById(R.id.etPassword);


        etUserId.setOnKeyListener(LoginActivity.this);
        etPassword.setOnKeyListener(LoginActivity.this);

        appVersion = (TextView) findViewById(R.id.tv_login_activity_app_version);

        helper = new MySQLiteOpenHelper(
                LoginActivity.this,  // 현재 화면의 제어권자
                CommonValue.SQLITE_DB_FILE_NAME,// db 이름
                null,  // 커서팩토리-null : 표준커서가 사용됨
                CommonValue.SQLITE_DB_VERSION);       // 버전

        try {

            db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
            //db = helper.getReadableDatabase(); // 읽기 전용 DB select문
        } catch (SQLiteException e) {

        }

        String selectQuery = String.format("SELECT %s, %s, %s, %s  FROM %s ;",
                CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO,
                CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO,
                CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_LOGIN_ID_TYPE,
                CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_GROUP_WARE_ID,
                CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO);

        Cursor c = db.rawQuery(selectQuery, null);
        c.moveToFirst();
        int count = c.getCount();

        if(count > 0 ) {
            String saveId = c.getString(0);
            String userId = c.getString(1);
            String saveRinnaiId = c.getString(2);
            String gwMail = c.getString(3);
            if("1".equals(saveId)) {
                cbSaveId.setChecked(true);
                String tmpUserId = userId;

                if("1".equals(saveRinnaiId)) {
                    cbRinnaiId.setChecked(true);
                    tmpUserId = gwMail.replace("@rinnai.co.kr", "");
                } else {
                    tmpUserId = userId;
                }

                etUserId.setText(tmpUserId);

            }

        }

        btnMenu01 = (RelativeLayout) findViewById(R.id.btn_login_activity_menu1);
        btnMenu02 = (RelativeLayout) findViewById(R.id.btn_login_activity_menu2);
        btnMenu03 = (RelativeLayout) findViewById(R.id.btn_login_activity_menu3);
        btnMenu04 = (RelativeLayout) findViewById(R.id.btn_login_activity_menu4);
        btnMenu05 = (RelativeLayout) findViewById(R.id.btn_login_activity_menu5);
        btnMenu06 = (RelativeLayout) findViewById(R.id.btn_login_activity_menu6);

        btnMenu01.setOnClickListener(LoginActivity.this);
        btnMenu02.setOnClickListener(LoginActivity.this);
        btnMenu03.setOnClickListener(LoginActivity.this);
        btnMenu04.setOnClickListener(LoginActivity.this);
        btnMenu05.setOnClickListener(LoginActivity.this);
        btnMenu06.setOnClickListener(LoginActivity.this);


        llMasterMenu = (LinearLayout) findViewById(R.id.ll_login_activity_master_menu);

        appVersion.setText(String.format("App Version : %s",versionName ));

        registerReceiver();


    }

    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = null;
        String userName ="마스터";
        String userNo ="20000000";
        String codeDept = "";
        String deptName = "";
        String gwMail = "master@rinnai.co.kr";

        String saveUserNo = "0";
        String saveRinnaiID = "0";

        if(id == btnLogin.getId()) {

            if (!networkConnecting) {
                goLogin();


            } else {
                showRinnaiDialog(LoginActivity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
            }
        } else if (id == btnMenu01.getId()) {
            intent = new Intent(LoginActivity.this, WmsFragmentActivity.class);
        } else if (id == btnMenu02.getId()) {
            intent = new Intent(LoginActivity.this, EmployeeActivity.class);

        } else if (id == btnMenu03.getId()) {
            intent = new Intent(LoginActivity.this, AgencyActivity.class);
            userNo = "RS31863";
            userName ="(주)청담에너지";

        } else if (id == btnMenu04.getId()) {
            intent = new Intent(LoginActivity.this, UseDeliveryDriverActivity.class);
            userNo = "8570";
            userName ="마스터";
        } else if (id == btnMenu05.getId()) {
            intent = new Intent(LoginActivity.this, RetailerActivity.class);
            userNo = "8570";
            userName ="마스터";
        } else if (id == btnMenu06.getId()) {
//            intent = new Intent(LoginActivity.this, SalespersonActivity.class);
            intent = new Intent(LoginActivity.this, SalespersonActivity.class);
            userNo = "01012345678";
            userName = "마스터";
            deptName = "하이마트 인터넷점";
        }

        if(intent != null) {
            String query = "INSERT OR REPLACE INTO %s (%s,%s,%s,%s,%s,%s,%s,%s) values ('%s','%s','%s','%s','%s','%s', '%s', '%s' );";

            query = String.format(query,

                    CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO,

                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_ID,
                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO,
                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NAME,
                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_CODE,
                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_NAME,
                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO,
                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_GROUP_WARE_ID,
                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_LOGIN_ID_TYPE,

                    "1",
                    userNo,
                    userName,
                    codeDept,
                    deptName,
                    saveUserNo,
                    gwMail,
                    saveRinnaiID

            );

            db.execSQL(query);
            startActivity(intent);
        }

    }

    @Override
    public void onResult(String result) {
        //super.onResult(result);
        Log.w("onResult", result);
        dismissProgress();
        ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
        networkConnecting = false;
        if (null != responseData) {
            if ("OK".equals(responseData.getResultMessage())) {
                Object obj = responseData.getData();

                String str = JsonParserManager.objectToJson(Object.class, obj);

                Type type = null;

                if ("getLogin".equals(responseData.getResultType())) {
                    type = new TypeToken<LoginResult>(){}.getType();

                    LoginResult login = new Gson().fromJson(str, type);

                    if(CommonValue.PASS_NOT_FOUND.equals(login.getPassType())) {
                        //사용자가 없음
                        showRinnaiDialog(LoginActivity.this, getString(R.string.msg_title_noti), getString(R.string.login_activity_user_not_found));
                    } else if (CommonValue.PASS_PASS_FAIL.equals(login.getPassType())) {
                        // 비밀번호 틀림
                        showRinnaiDialog(LoginActivity.this, getString(R.string.msg_title_noti), getString(R.string.login_activity_pass_fail));
                    } else if (CommonValue.PASS_UNLICENSED_AGENCY.equals(login.getPassType())) {
                        // 대리점 권한 없음( ID 및 패스워드는 정상이나 등록된 전화번호가 아님)
                        showRinnaiDialog(LoginActivity.this, getString(R.string.msg_title_noti), getString(R.string.login_activity_unlicensed_agency));

                    } else if (CommonValue.PASS_VERSION_UPDATE.equals(login.getPassType())) {
                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(LoginActivity.this);
                        final String appVersion = login.getAppVersion();
                        alert_confirm.setMessage("업데이트가 필요합니다. 새로운 App을 다운로드 하시겠습니까?").setCancelable(false).setPositiveButton("다운로드",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(LoginActivity.this, DownloadNotifiaiontService.class);
                                        String fileName = String.format("http://58.72.180.60/mobileservice/deploy/last/SmartDMS_%s.apk", appVersion);
                                        ApiService.getInstance().setDownloadUrl(fileName);

                                        startService(intent);

                                    }
                                }).setNegativeButton("취소",
                                new DialogInterface.OnClickListener() {
                                    @Override

                                    public void onClick(DialogInterface dialog, int which) {
                                        finishAffinity();
                                        System.runFinalization();
                                        System.exit(0);
                                        // 'No'
                                        return;
                                    }
                                });
                        AlertDialog alert = alert_confirm.create();
                        alert.show();
                    } else if (CommonValue.PASS_TYPE_OK.equals(login.getPassType())) {

                        String userId = etUserId.getText().toString();
                        String password = etPassword.getText().toString();
                        if ("master".equals(userId) && "3651".equals(password)) {
                            llMasterMenu.setVisibility(View.VISIBLE);
                        } else {

                            boolean saveID = cbSaveId.isChecked();

                            String saveUserNo = "0";

                            if (saveID) {
                                saveUserNo = "1";
                            }

                            boolean saveRinnai = cbRinnaiId.isChecked();

                            String saveRinnaiID = "0";

                            if (saveRinnai) {
                                saveRinnaiID = "1";
                            }

                            String query = "INSERT OR REPLACE INTO %s (%s,%s,%s,%s,%s,%s,%s,%s) values ('%s','%s','%s','%s','%s','%s', '%s', '%s' );";

                            query = String.format(query,

                                    CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO,

                                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_ID,
                                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO,
                                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NAME,
                                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_CODE,
                                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_NAME,
                                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO,
                                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_GROUP_WARE_ID,
                                    CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_LOGIN_ID_TYPE,

                                    "1",
                                    login.getUserNo(),
                                    login.getUserName(),
                                    login.getCodeDept(),
                                    login.getDeptName(),
                                    saveUserNo,
                                    login.getGwMail(),
                                    saveRinnaiID

                            );


                            db.execSQL(query);

                            //정상 사용자
                            Intent intent = null;
                            if (CommonValue.LOGIN_TYPE_WMS.equals(login.getLoginType())) {
                                intent = new Intent(LoginActivity.this, WmsFragmentActivity.class);
                                RinnaiApp.getInstance().setGwId(login.getGwMail());
                                RinnaiApp.getInstance().setUserNo(login.getUserNo());
                            } else if (CommonValue.LOGIN_TYPE_SALES.equals(login.getLoginType())) {
                                intent = new Intent(LoginActivity.this, EmployeeActivity.class);
                                RinnaiApp.getInstance().setGwId(login.getGwMail());
                                RinnaiApp.getInstance().setUserNo(login.getUserNo());
                            } else if (CommonValue.LOGIN_TYPE_AGENCY.equals(login.getLoginType())) {
                                intent = new Intent(LoginActivity.this, AgencyActivity.class);
                                RinnaiApp.getInstance().setUserNo(login.getUserNo());
                            } else if (CommonValue.LOGIN_TYPE_SHIPPING_DRIVER.equals(login.getLoginType())) {
                                intent = new Intent(LoginActivity.this, UseDeliveryDriverActivity.class);
                            } else if (CommonValue.LOGIN_TYPE_INSTALLATION_DRIVER.equals(login.getLoginType())) {
                                intent = new Intent(LoginActivity.this, RetailerActivity.class);
                            } else if (CommonValue.LOGIN_TYPE_SALESPORSON.equals(login.getLoginType())) {
                                intent = new Intent(LoginActivity.this, SalespersonActivity.class);
                            } else if (CommonValue.LOGIN_TYPE_SALESPORSON_MANAGER.equals(login.getLoginType())) {
                                intent = new Intent(LoginActivity.this, SalespersonManagerActivity.class);

                            }

    //                        intent = new Intent(LoginActivity.this, RetailerActivity.class);
    //                        intent = new Intent(LoginActivity.this, AgencyActivity.class);
                            startActivity(intent);

                      }
                    }


                }

            }
        }
    }

    @Override
    public void exceptionOccured(Exception e) {
        dismissProgress();

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        int id = v.getId();
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if(id == etUserId.getId()) {
                etPassword.setFocusable(true);
            } else if (id == etPassword.getId()) {
                if(!networkConnecting) {
                    goLogin();
                } else {
                    showRinnaiDialog(LoginActivity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
                }
            }
            return true;
        }
        return false;
    }

    @SuppressWarnings({"MissingPermission"})
    private int goLogin() {
 /*
        Intent intent = new Intent(LoginActivity.this, DownloadNotifiaiontService.class);
        startService(intent);
        */
        String userId = etUserId.getText().toString();
        String password = etPassword.getText().toString();

        String phoneNum = "";
        String oriPhoneNum  = "";
        TelephonyManager tpManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        int device_type = tpManager.getPhoneType();

       // phoneNum.substring(0,5);
        switch (device_type) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                oriPhoneNum = tpManager.getLine1Number();
                break;
            case ( TelephonyManager.PHONE_TYPE_GSM):
                oriPhoneNum = tpManager.getLine1Number();
                break;

            default:
                //return something else
                oriPhoneNum = "null";
                break;
        }
        //phoneNum = userId;


        phoneNum = oriPhoneNum;

        if(!"null".equals(oriPhoneNum) && null != oriPhoneNum) {

            if(phoneNum.startsWith("+820")){
                phoneNum = phoneNum.replace("+820", "0"); // +8210xxxxyyyy 로 시작되는 번호
            }

            if(phoneNum.startsWith("+82")){
                phoneNum = phoneNum.replace("+82", "0"); // +8210xxxxyyyy 로 시작되는 번호
            }

            if(phoneNum.startsWith("821")){
                phoneNum = phoneNum.replace("821", "01"); // 8210xxxxyyyy 로 시작되는 번호
            }

            if(phoneNum.startsWith("820")){
                phoneNum = phoneNum.replace("820", "0"); // 82010xxxxyyyy 로 시작되는 번호
            }


            phoneNum = phoneNum.replaceAll("[^0-9]","");

            //
         //   String phoneRegExp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|d{4})[.-]?(\\d{4})$";
            //restr.matches(phoneRegExp)

//            if(restr.startsWith("010")) {
//                phoneNum = String.format("%s-%s-%s", restr.substring(0, 3), restr.substring(3, 7), restr.substring(7, 11));
//            }
            //int idx = restr.indexOf("010");

            Log.d("phoneNumber", String.format("original number : %s, phoneNumber : %s", userId, phoneNum));

        }



//       phoneNum="010-6671-6632";
        if("".equals(userId.trim())) {
//            userId="20150001";
            //userId = "20010092";
            showRinnaiDialog(LoginActivity.this, getString(R.string.msg_title_noti), getString(R.string.login_activity_validation_id));
            return -1;
        }

        String tmpId = userId;
        if( cbRinnaiId.isChecked() ) {
            tmpId = userId+ "@rinnai.co.kr";
        }
        else if(ParseUtil.CheckNumber(tmpId) && tmpId.length() > 8 && !tmpId.equals(phoneNum)) {
            showRinnaiDialog(LoginActivity.this, getString(R.string.msg_title_noti), getString(R.string.login_activity_validation_phone));
//            password = "@tkdala0502";
            //password = "1323";
            return -1;
        }


        if(!tmpId.equals(phoneNum)) {
            if ("".equals(userId.trim()) || password.length() < 4) {
                showRinnaiDialog(LoginActivity.this, getString(R.string.msg_title_noti), getString(R.string.login_activity_validation_pw));

                return -1;
            }
        }
        try {
            Map<String, String> param = new HashMap<String, String>();
            param.put("userNo", tmpId);
            param.put("password", password);
            param.put("phone", phoneNum);
            param.put("appVersion", versionName);
            param.put("deviceModel", Build.MODEL);
            param.put("osVersion", Build.VERSION.RELEASE);
            param.put("oriPhone", oriPhoneNum);



            String httpHost = HttpClient.getCurrentSsid(LoginActivity.this);

            String jsonStr = JsonParserManager.objectToJson(Map.class, "user", param);


            String url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_USER, CommonValue.HTTP_LOGIN, CommonValue.HTTP_VERSION_2);
            showProgress(LoginActivity.this);
            HttpClient.post(url, LoginActivity.this, jsonStr);




        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
        llMasterMenu.setVisibility(View.GONE);
        etPassword.setText("");
    }

    private BroadcastReceiver onDownloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("DMS", "MEPS_onDownloadReceiver : " + intent.getAction());
            if(intent.getAction().equals(DownloadNotifiaiontService.PROGRESS_UPDATE)) {
                boolean complete = intent.getBooleanExtra("downloadComplete", false);
                if(complete) {
                    Toast.makeText(LoginActivity.this, "completed",Toast.LENGTH_SHORT).show();

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator + "SmartDMS.apk");

                    Uri apkUri;
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                    } else {
                        apkUri = Uri.fromFile(file);
                    }
                    Intent openFileIntent = new Intent(Intent.ACTION_VIEW);
                    openFileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    openFileIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    openFileIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    startActivity(openFileIntent);
                    finish();
                }
            }
        }
    };

    private void registerReceiver() {
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        Log.d("MEPS", "registerReceiver : " + DownloadNotifiaiontService.PROGRESS_UPDATE);
        intentFilter.addAction(DownloadNotifiaiontService.PROGRESS_UPDATE);
        manager.registerReceiver(onDownloadReceiver, intentFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.unregisterReceiver(onDownloadReceiver);
    }



}
