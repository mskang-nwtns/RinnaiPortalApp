package kr.co.rinnai.dms.sie.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.aos.activity.AgencyMenu03Activity;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.sie.adapter.RetailerMenu02ListAdapter;
import kr.co.rinnai.dms.sie.model.RetailerMenu02ListEntity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 *  Shipping Installation Engineer(양판점 배송 설치 기사) 작업 내역 화면
 */
public class RetailerMenu02Activity extends BaseActivity implements  AdapterView.OnItemClickListener {

	private ListView lvRetailer;

	private TextView tvCustName;

	private boolean networkConnecting;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String userName, userId;

	private RelativeLayout btnCalendar;
	private TextView tvDate;

	private String searchDate;

	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	private RetailerMenu02ListAdapter adapter;

	private RinnaiCalendarDialog rinnaiReceivedProductDialog;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailer_menu_02);

//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		lvRetailer = (ListView) findViewById(R.id.lv_retailer);
		tvCustName = (TextView) findViewById(R.id.tv_agency_01_activity_cust_name);

		btnCalendar = (RelativeLayout) findViewById(R.id.rl_installation_02_date);

		tvDate = (TextView) findViewById(R.id.tv_installation_02_work_date);

		Date time = new Date();

		searchDate = format1.format(time);

		tvDate.setText(searchDate);

		helper = new MySQLiteOpenHelper(
				RetailerMenu02Activity.this,  // 현재 화면의 제어권자
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

			userId = c.getString(1);
			userName = c.getString(2);



		}

		if(userId.length() == 7) {
			userId = userId.substring(2, 7);
		}

		//tvCustName.setText(custName);
		getInstallationList();

		lvRetailer.setOnItemClickListener(RetailerMenu02Activity.this);

		btnCalendar.setOnClickListener(RetailerMenu02Activity.this);



	}


	private void getInstallationList() {
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(RetailerMenu02Activity.this);

			String tmpDate = searchDate.replace("/","");


			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_RETAILER, tmpDate, userId);

			showProgress(RetailerMenu02Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}

	@Override
	public void onResult(String result) {
		//super.onResult(result);
		Log.w("onResult", result);

		dismissProgress();
		networkConnecting = false;
		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
		if (null != responseData) {
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);

				Type type = null;


				if ("getRetailerBarcodeList".equals(responseData.getResultType())) {


					type = new TypeToken<ArrayList<RetailerMenu02ListEntity>>(){}.getType();

					List<RetailerMenu02ListEntity> list = new Gson().fromJson(str, type);
					for(int i = 0; i < list.size(); i ++ ) {
						RetailerMenu02ListEntity entity = (RetailerMenu02ListEntity)list.get(i);
						saveImage(entity);
					}
					adapter = new RetailerMenu02ListAdapter(RetailerMenu02Activity.this, list);
					lvRetailer.setAdapter(adapter);
					adapter.notifyDataSetChanged();

				}
			} else {

				List<RetailerMenu02ListEntity> list = null;
				adapter = new RetailerMenu02ListAdapter(RetailerMenu02Activity.this, list);
				lvRetailer.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				showRinnaiDialog(RetailerMenu02Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		RetailerMenu02ListEntity entity = (RetailerMenu02ListEntity)adapter.getItem(position);
		Intent intent = null;
		intent = new Intent(RetailerMenu02Activity.this, RetailerMenu03Activity.class);
		intent.putExtra(CommonValue.INTENT_RETAILER_KEY, entity);
		startActivityForResult(intent, 0);
	}



	private void saveImage(RetailerMenu02ListEntity entity) {
		if(entity.getSign01() != null) {
			byte[] byteArray = ParseUtil.binaryStringToByteArray(entity.getStrSign01());

			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

			Util.saveBitmaptoJpeg(bmp, entity.getAgencyOrderBarcode() + "-sign01", ".jpg");
		}

		if(entity.getImage01() != null) {
			byte[] byteArray = ParseUtil.binaryStringToByteArray(entity.getStrImage01());

			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

			Util.saveBitmaptoJpeg(bmp, entity.getAgencyOrderBarcode() + "-image01", ".jpg");
		}

		if(entity.getImage02() != null) {
			byte[] byteArray = ParseUtil.binaryStringToByteArray(entity.getStrImage02());

			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

			Util.saveBitmaptoJpeg(bmp, entity.getAgencyOrderBarcode() + "-image02", ".jpg");
		}


	}

	@Override
	public void onClick(View v) {
		//super.onClick(v);
		int id = v.getId();

		if (id == btnCalendar.getId()) {
			if(!networkConnecting) {
				if(rinnaiReceivedProductDialog == null) {
					rinnaiReceivedProductDialog = new RinnaiCalendarDialog(RetailerMenu02Activity.this);
				}
				if(!rinnaiReceivedProductDialog.isShowing()) {
					rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
					rinnaiReceivedProductDialog.setCancelable(false);

					rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
						@Override
						public void onDateChange(String date) {
							searchDate = date;
							tvDate.setText(searchDate);
							getInstallationList();


						}  // MyDialogListener 를 구현

						@Override
						public void onCalendarView() {

						}

					});

					rinnaiReceivedProductDialog.show();
				}
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		getInstallationList();
	}
}
