package kr.co.rinnai.dms.aos.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu02DetailListAdapter;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu02ListAdapter;

import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.aos.model.AgencyMenu02DetailListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu02ListEntity;

import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 *  Agency Operating System(영업 시스템, 대리점) 주문 현황
 */

public class AgencyMenu02Activity extends BaseActivity implements AdapterView.OnItemClickListener {

	private String fromDate, toDate;
	private RelativeLayout rlFromData, rlToDate;
	private TextView tvFromDate, tvToDate;

	private ListView lvOrderList, lvOrderDetailList;

	private AgencyMenu02ListAdapter adapter;
	private AgencyMenu02DetailListAdapter detailAdapter;

	private boolean networkConnecting = false;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String custName, custCode;

	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	private RinnaiCalendarDialog rinnaiReceivedProductDialog;


	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agency_menu_02);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		tvFromDate = (TextView) findViewById(R.id.tv_agency_02_order_from_date);
		tvToDate = (TextView) findViewById(R.id.tv_agency_02_order_to_date);

		rlFromData = (RelativeLayout) findViewById(R.id.rl_agency_02_order_from_date);
		rlToDate = (RelativeLayout) findViewById(R.id.rl_agency_02_order_to_date);

		lvOrderList = (ListView) findViewById(R.id.lv_agency_menu_03_order_list);
		lvOrderDetailList = (ListView) findViewById(R.id.lv_agency_menu_03_order_detail_list);



		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		String eDate = format1.format(cal.getTime());
//		cal.add(Calendar.DATE, );
		String sDate = format1.format(cal.getTime());
		tvFromDate.setText(eDate);
		tvToDate.setText(sDate);

		helper = new MySQLiteOpenHelper(
				AgencyMenu02Activity.this,  // 현재 화면의 제어권자
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

			custCode = c.getString(1);
			custName = c.getString(2);



		} else {
			custCode ="test";
			custName = "test";
		}

		if(custCode.length() == 7) {
			custCode = custCode.substring(2, 7);
		}
		getOrderInfo();

		lvOrderList.setOnItemClickListener(AgencyMenu02Activity.this);

		rlFromData.setOnClickListener(AgencyMenu02Activity.this);
		rlToDate.setOnClickListener(AgencyMenu02Activity.this);

//971138;

		//842583;
	}

	private void getOrderInfo() {
		if(!networkConnecting) {

			String sDate = tvFromDate.getText().toString().replace("/", "-");
			String eDate = tvToDate.getText().toString().replace("/", "-");


			int iSDate = Integer.parseInt(sDate.replace("-", ""));
			int iEDate = Integer.parseInt(eDate.replace("-", ""));

			if(iSDate> iEDate) {

				showRinnaiDialog(AgencyMenu02Activity.this, getString(R.string.msg_title_noti), "조회 시작일이 조회 종료일보다 느립니다.");
				return;

			}

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(AgencyMenu02Activity.this);

			url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_ORDER, custCode, sDate, eDate);

			showProgress(AgencyMenu02Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}

	private void getOrderDetailInfo(String ordNo) {
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(AgencyMenu02Activity.this);

			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_ORDER, CommonValue.HTTP_INFO, ordNo);

			showProgress(AgencyMenu02Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}

	@Override
	public void onResult(String result) {
		//super.onResult(result);
		Log.w("onResult", result);
		networkConnecting = false;
		dismissProgress();
		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
		if (null != responseData) {
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);

				Type type = null;

				if ("getOrderInfo".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<AgencyMenu02ListEntity>>(){}.getType();

					List<AgencyMenu02ListEntity> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu02ListAdapter(AgencyMenu02Activity.this, list);
					lvOrderList.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else if ("getOrderDetailInfo".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<AgencyMenu02DetailListEntity>>(){}.getType();

					List<AgencyMenu02DetailListEntity> list = new Gson().fromJson(str, type);

					detailAdapter = new AgencyMenu02DetailListAdapter(AgencyMenu02Activity.this, list);
					lvOrderDetailList.setAdapter(detailAdapter);
					detailAdapter.notifyDataSetChanged();
				}
			} else {
				showRinnaiDialog(AgencyMenu02Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());


			}
		}

		//getSalesProgress
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(!networkConnecting) {
			AgencyMenu02ListEntity entity = (AgencyMenu02ListEntity) adapter.getItem(position);

			getOrderDetailInfo(entity.getOrdNo());
		} else {

		}
	}

	@Override
	public void onClick(View v) {
		//InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		//imm.hideSoftInputFromWindow(etCustName.getWindowToken(), 0);

		int id = v.getId();

		if(rinnaiReceivedProductDialog == null) {
			rinnaiReceivedProductDialog = new RinnaiCalendarDialog(AgencyMenu02Activity.this);
		}
		if (id == rlFromData.getId()) {


			if(!rinnaiReceivedProductDialog.isShowing()) {


				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {

						String sDate = date.replace("/", "-");
						String eDate = tvToDate.getText().toString().replace("/", "-");


						int iSDate = Integer.parseInt(sDate.replace("-", ""));
						int iEDate = Integer.parseInt(eDate.replace("-", ""));

						if(iSDate> iEDate) {

							showRinnaiDialog(AgencyMenu02Activity.this, getString(R.string.msg_title_noti), "조회 조건을 확인해주세요.");
							return;

						}
						 tvFromDate.setText(date);
						 getOrderInfo();
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {
					}

				});
				rinnaiReceivedProductDialog.show();
			}

		} else if(id == rlToDate.getId()) {
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);


				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {

						String sDate = tvFromDate.getText().toString().replace("/", "-");
						String eDate = date.replace("/", "-");


						int iSDate = Integer.parseInt(sDate.replace("-", ""));
						int iEDate = Integer.parseInt(eDate.replace("-", ""));

						if(iSDate> iEDate) {

							showRinnaiDialog(AgencyMenu02Activity.this, getString(R.string.msg_title_noti), "조회 조건을 확인해주세요.");
							return;

						}
						tvToDate.setText(date);
						getOrderInfo();
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
