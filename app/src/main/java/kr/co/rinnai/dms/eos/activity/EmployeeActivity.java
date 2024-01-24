package kr.co.rinnai.dms.eos.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseFragmentActivity;
import kr.co.rinnai.dms.aos.activity.AgencyMenu07Activity;
import kr.co.rinnai.dms.aos.activity.AgencyMenu07FragmentFirst;
import kr.co.rinnai.dms.aos.activity.AgencyMenu07FragmentSecond;
import kr.co.rinnai.dms.aos.model.AgencyMenu01ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteDetailInfo;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.CustomButtonLogoutView;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.model.CodeInfo;


public class EmployeeActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener{

	ViewPager vp;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private TextView tvUserNo, tvUserName;

	private CustomButtonLogoutView btnLogout;

	boolean isPressed = false;

	ImageView ivPage1, ivPage2;

	private boolean networkConnecting = false;


	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		boolean isTablet = Util.isTabletDevice(EmployeeActivity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

		setContentView(R.layout.act_employee_main);
		vp = (ViewPager)findViewById(R.id.employee_view_pager);

		vp.addOnPageChangeListener(this);
		PageAdapter adapter = new PageAdapter(getSupportFragmentManager());

		adapter.addFragment(new EmployeeFragmentFirst(), "menu0");
		adapter.addFragment(new EmployeeFragmentSecond(), "menu1");


		tvUserNo = (TextView) findViewById(R.id.tv_em_user_no);
		tvUserName = (TextView) findViewById(R.id.tv_em_user_name);

		ivPage1 = (ImageView) findViewById(R.id.iv_employee_page_1);
		ivPage2 = (ImageView) findViewById(R.id.iv_employee_page_2);
		vp.setAdapter(adapter);
		vp.setCurrentItem(0);

		helper = new MySQLiteOpenHelper(
				EmployeeActivity.this,  // 현재 화면의 제어권자
				CommonValue.SQLITE_DB_FILE_NAME,// db 이름
				null,  // 커서팩토리-null : 표준커서가 사용됨
				CommonValue.SQLITE_DB_VERSION);       // 버전

		try {

			db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
			//db = helper.getReadableDatabase(); // 읽기 전용 DB select문
		} catch (SQLiteException e) {

		}

		String selectQuery = String.format("SELECT %s, %s, %s, %s, %s  FROM %s ;",
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NAME,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_LOGIN_ID_TYPE,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_GROUP_WARE_ID,
				CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO);

		/*
		String selectQuery = String.format("SELECT %s, %s, %s  FROM %s ;",
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NAME,
				CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO);
*/
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		int count = c.getCount();

		if(count > 0 ) {
			String saveId = c.getString(0);
			String userId = c.getString(1);
			String userName = c.getString(2);
			String loginType = c.getString(3);
			String gwMail = c.getString(4);

			if("0".equals(loginType)) {
				tvUserNo.setText(userId);
			} else {
				tvUserNo.setText(gwMail);
			}

			tvUserName.setText(userName);

		}



		btnLogout = (CustomButtonLogoutView) findViewById(R.id.btn_em_logout);

		btnLogout.setOnTouchListener(EmployeeActivity.this);

	}

	private class PageAdapter extends FragmentPagerAdapter
	{
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public PageAdapter(FragmentManager fm)
		{
			super(fm);
		}
		@Override
		public Fragment getItem(int position) { return mFragmentList.get(position); }
		@Override
		public int getCount()
		{
			return mFragmentList.size();
		}

		public void addFragment(Fragment fragment, String title){
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {


		PageAdapter adapter = (PageAdapter)vp.getAdapter();
		EmployeeFragmentFirst first = (EmployeeFragmentFirst)adapter.getItem(0);
		first.clearView();

		EmployeeFragmentSecond second = (EmployeeFragmentSecond)adapter.getItem(1);
		second.clearView();

		if(position == 0) {
			ivPage1.setImageResource(R.drawable.page_1_on);
			ivPage2.setImageResource(R.drawable.page_2_off);
		} else if(position == 1) {
			ivPage1.setImageResource(R.drawable.page_1_off);
			ivPage2.setImageResource(R.drawable.page_2_on);

		}



	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				btnLogout.buttonClick(true);
				break;

			case MotionEvent.ACTION_OUTSIDE:
			case MotionEvent.ACTION_UP:
				btnLogout.buttonClick(false);
				finish();
				break;

		}

		return false;
	}

	protected  void setLog(String activityName) {
		String userNo = RinnaiApp.getInstance().getUserNo();
		String url = String.format("%s/%s/%s",
				CommonValue.HTTP_HOST, CommonValue.HTTP_COMM, CommonValue.HTTP_LOG);

		Map<String, String> param = new HashMap<String, String>();

		param.put("objectId",activityName);
		param.put("userNo",userNo);


//		showProgress(EmployeeActivity.this);
//		networkConnecting = true;
		try {

			String strResponse = ParseUtil.getJSONFromObject(param);
			HttpClient.post(url, EmployeeActivity.this, strResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onResult(String result) {
		Log.w("onResult", result);
//		networkConnecting = false;
		//dismissProgress();
		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
		if (null != responseData) {
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);

				Type type = null;
			} else {
				//showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
	}

}
