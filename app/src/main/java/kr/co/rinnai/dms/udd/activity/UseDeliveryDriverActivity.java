package kr.co.rinnai.dms.udd.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseFragmentActivity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.CustomButtonLogoutView;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.wms.activity.WmsFragmentFirst;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;


public class UseDeliveryDriverActivity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, View.OnTouchListener{

	ViewPager vp;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private TextView tvUserNo, tvUserName;

	private CustomButtonLogoutView btnLogout;

	boolean isPressed = false;


	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_use_delivery_driver_main);

		vp = (ViewPager)findViewById(R.id.delivery_view_pager);

		vp.addOnPageChangeListener(this);
		PageAdapter adapter = new PageAdapter(getSupportFragmentManager());

		adapter.addFragment(new UseDeliveryDriverFragmentFirst(), "menu0");


		tvUserNo = (TextView) findViewById(R.id.tv_udd_user_no);
		tvUserName = (TextView) findViewById(R.id.tv_udd_user_name);
		vp.setAdapter(adapter);
		vp.setCurrentItem(0);

		helper = new MySQLiteOpenHelper(
				UseDeliveryDriverActivity.this,  // 현재 화면의 제어권자
				CommonValue.SQLITE_DB_FILE_NAME,// db 이름
				null,  // 커서팩토리-null : 표준커서가 사용됨
				CommonValue.SQLITE_DB_VERSION);       // 버전

		try {

			db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
			//db = helper.getReadableDatabase(); // 읽기 전용 DB select문
		} catch (SQLiteException e) {

		}

		String selectQuery = String.format("SELECT %s, %s, %s  FROM %s ;",
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NAME,
				CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO);

		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		int count = c.getCount();

		if(count > 0 ) {
			String saveId = c.getString(0);
			String userId = c.getString(1);
			String userName = c.getString(2);
			tvUserNo.setText(userId);
			tvUserName.setText(userName);

		}

		btnLogout = (CustomButtonLogoutView) findViewById(R.id.btn_udd_logout);

		btnLogout.setOnTouchListener(UseDeliveryDriverActivity.this);

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
		WmsFragmentFirst first = (WmsFragmentFirst)adapter.getItem(0);


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
}
