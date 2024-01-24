package kr.co.rinnai.dms.eos.activity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.custom.RinnaiSearchListDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.adapter.EmployeeMenu06ListAdapter;
import kr.co.rinnai.dms.eos.adapter.EmployeeMenu07ListAdapter;
import kr.co.rinnai.dms.eos.adapter.ItemSpinnerAdapter;
import kr.co.rinnai.dms.eos.model.EmployeeMenu06ListEntity;
import kr.co.rinnai.dms.eos.model.EmployeeMenu07ListEntity;
import kr.co.rinnai.dms.eos.model.ItemCode;
import kr.co.rinnai.dms.eos.model.SalesAgencyInfoResult;

/**
 * 소비자 이중 체크
 */
public class EmployeeMenu07Activity extends BaseActivity implements  AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnTouchListener, View.OnKeyListener{


	private RelativeLayout rlSDate, rlEDate;
	private RelativeLayout btnSearch;
	//private LinearLayout llDetailView;

	private ListView lvOrderList;
	private EmployeeMenu07ListAdapter listAdapter;

	private EditText etCustName;

	private boolean networkConnecting = false;

	private TextView tvSDate, tvEDate, tvCustName;

	private TextView tvOrderDate, tvTelNo, tvAddr, tvRemark, tvDriverName, tvDriverTel;


	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	private String custCode = "";

	private boolean  isFirstStart = true;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String userName;

	private RinnaiCalendarDialog calendarDialog;

	private Map<String, String[][]> mapAddrInfo;
	private Map<String, String[]> mapDeliveryInfo;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_menu_07);


		boolean isTablet = Util.isTabletDevice(EmployeeMenu07Activity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

		rlSDate = (RelativeLayout) findViewById(R.id.rl_employee_07_order_from_date);
		rlEDate = (RelativeLayout) findViewById(R.id.rl_employee_07_order_to_date);

		tvSDate = (TextView) findViewById(R.id.tv_employee_07_order_from_date);
		tvEDate = (TextView) findViewById(R.id.tv_employee_07_order_to_date);
//		spItemType = (Spinner) findViewById(R.id.sp_employee_06_activity_item_type);
		lvOrderList = (ListView) findViewById(R.id.lv_employee_07_order_list);
		btnSearch = (RelativeLayout) findViewById(R.id.btn_empolyee_07_activity_search);

		lvOrderList.setOnItemClickListener(EmployeeMenu07Activity.this);
		etCustName = (EditText) findViewById(R.id.et_empolyee_07_activity_cust_name);
		etCustName.setOnKeyListener(EmployeeMenu07Activity.this);
//		etCustName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
//		etCustName.setOnClickListener(EmployeeMenu06Activity.this);

//		llDetailView = (LinearLayout)findViewById(R.id.ll_empolyee_07_activity_detail_view);

		tvOrderDate = (TextView) findViewById(R.id.tv_employee_07_activity_list_detail_order_date);
		tvTelNo = (TextView) findViewById(R.id.tv_employee_07_activity_list_detail_tel_no);
		tvAddr = (TextView) findViewById(R.id.tv_employee_07_activity_list_detail_addr);
		tvRemark = (TextView) findViewById(R.id.tv_employee_07_activity_list_detail_remark);
		tvDriverName = (TextView) findViewById(R.id.tv_employee_07_activity_list_detail_driver_name);
		tvDriverTel = (TextView) findViewById(R.id.tv_employee_07_activity_list_detail_d_tel);

		tvCustName = (TextView) findViewById(R.id.tv_employee_07_activity_search_name);

		rlSDate.setOnClickListener(EmployeeMenu07Activity.this);
		rlEDate.setOnClickListener(EmployeeMenu07Activity.this);
		btnSearch.setOnClickListener(EmployeeMenu07Activity.this);

		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);


		String eDate = format1.format(cal.getTime());
//		cal.add(Calendar.DATE, );

		cal.add(Calendar.DATE, -30);
		String sDate = format1.format(cal.getTime());
		tvEDate.setText(eDate);
		tvSDate.setText(sDate);


		helper = new MySQLiteOpenHelper(
				EmployeeMenu07Activity.this,  // 현재 화면의 제어권자
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
			if(userId.indexOf("@rinnai.co.kr") != -1) {
				userName = userId.replace("@rinnai.co.kr", "");
			} else {
				userName = userId;
			}


		}
		if(null != RinnaiApp.getInstance().getGwId()) {
			String gwId = RinnaiApp.getInstance().getGwId();
			String tmpId =  null;
			if(null != gwId) tmpId = gwId.replace("@rinnai.co.kr", "");
			if("mini3248".equals(tmpId) || "ssj9567".equals(tmpId)) {

			} else {
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
			}
			if ("mini3248".equals(tmpId) || "ssj9567".equals(tmpId)) {

			} else {
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
			}
		}

		mapAddrInfo = new HashMap<String, String[][]>();

		mapAddrInfo.put("서울", CommonValue.CUSTOMER_DOUBLE_CHECK_SEUOL);
		mapAddrInfo.put("경기", CommonValue.CUSTOMER_DOUBLE_CHECK_GYEONGGI);
		mapAddrInfo.put("인천", CommonValue.CUSTOMER_DOUBLE_CHECK_INCHEON);


		mapDeliveryInfo = new HashMap<String, String[]>();

		mapDeliveryInfo.put("서울", CommonValue.CUSTOMER_DOUBLE_CHECK_SEOUL_DILIVERY_NAME);
		mapDeliveryInfo.put("경기", CommonValue.CUSTOMER_DOUBLE_CHECK_GYEONGGI_DILIVERY_NAME);
		mapDeliveryInfo.put("인천", CommonValue.CUSTOMER_DOUBLE_CHECK_INCHEON_DILIVERY_NAME);


	}

	@Override
	public void onResult(String result) {

		super.onResult(result);
		Log.w("onResult", result);

		dismissProgress();
		networkConnecting = false;

		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);

		tvOrderDate.setText("");
		tvTelNo.setText("");
		tvAddr.setText("");
		tvRemark.setText("");
		tvDriverName.setText("");
		tvDriverTel.setText("");

		if (null != responseData ) {
			Log.w("onResult", "ok");
			if("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);
				Type type = null;
				if("getConsumerDoubleCheck".equals(responseData.getResultType())) {


					type = new TypeToken<ArrayList<EmployeeMenu07ListEntity>>(){}.getType();

					List<EmployeeMenu07ListEntity> list = new Gson().fromJson(str, type);
					listAdapter = new EmployeeMenu07ListAdapter(EmployeeMenu07Activity.this, list);
					lvOrderList.setAdapter(listAdapter);
					listAdapter.notifyDataSetChanged();
//					lvStockWarehouse.setAdapter(adapter);

//					adapter.notifyDataSetChanged();
					lvOrderList.setVisibility(View.VISIBLE);

				} else if ("getSalesAgencyInfo".equals(responseData.getResultType()))  {
					type = new TypeToken<ArrayList<SalesAgencyInfoResult>>(){}.getType();
					List<SalesAgencyInfoResult> list = new Gson().fromJson(str, type);
					if(list.size() == 1) {
						SalesAgencyInfoResult agencyInfo = list.get(0);
						custCode = agencyInfo.getCustCode();


						etCustName.setText(custCode);
						tvCustName.setText(agencyInfo.getCustName());

						Handler delayHandler = new Handler();
						delayHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getForwardingOrder();
							}
						}, 100);


					} else if(list.size() > 1 ){
						showListPopup(list);
					}

				} else if ("getAgencyList".equals(responseData.getResultType())) {
					type = new TypeToken<ArrayList<SalesAgencyInfoResult>>(){}.getType();
					List<SalesAgencyInfoResult> list = new Gson().fromJson(str, type);
					if(list.size() == 1) {
						SalesAgencyInfoResult agencyInfo = list.get(0);

						custCode = agencyInfo.getCustCode();
					//	etCustName.setText(agencyInfo.getCustName());

						etCustName.setText(custCode);
//						tvCustName.setText(agencyInfo.getCustName());



//						spItemType.setSelection(0);
						getForwardingOrder();
					} else if (list.size() > 1) {
						showListPopup(list);
					}

					/*
					if(list.size() == 1) {
						SalesAgencyInfoResult agencyInfo = list.get(0);
						final String custCode = agencyInfo.getCustCode();

						Handler delayHandler = new Handler();
						delayHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getStockByStore(custCode);
							}
						}, 100);


					} else if(list.size() > 1 ){
						//showListPopup(list);
					}
					*/
				}

			} else {
				dismissProgress();
				showRinnaiDialog(EmployeeMenu07Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
				lvOrderList.setVisibility(View.INVISIBLE);
			}
		}
	}


	private void getForwardingOrder() {
		//llDetailView.setVisibility(View.INVISIBLE);
		if(!networkConnecting) {

			String url = "";

			String sDate = tvSDate.getText().toString();
			String eDate = tvEDate.getText().toString();


//			if("0".equals(itemType)) {
//				showRinnaiDialog(EmployeeMenu06Activity.this, getString(R.string.msg_title_noti), "Item구분을 선택하여주세요.");
//				return;
//			}

			if(sDate.indexOf("/") > - 1) {
				sDate = sDate.replace("/", "");
			}

			if(eDate.indexOf("/") > - 1) {
				eDate = eDate.replace("/", "");
			}

			int iSDate = Integer.parseInt(sDate);
			int iEDate = Integer.parseInt(eDate);

			if(iSDate> iEDate) {

				showRinnaiDialog(EmployeeMenu07Activity.this, getString(R.string.msg_title_noti), "조회 시작일이 조회 종료일보다 느립니다.");
				return;

			}

			if("".equals(custCode)) {
				showRinnaiDialog(EmployeeMenu07Activity.this, getString(R.string.msg_title_noti), "매장 조회");
				return;
			}

			String itemType = "1";

			url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_CONSUMER_DOUBLE_CHECK,  custCode, sDate, eDate);

			showProgress(EmployeeMenu07Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		/*
		*
		* orderDate, orderItemName
		* wareHouseName, driverName
		* telNo, remark
		* */
		String orderDate = "";
		String telNo = "";
		String telNo2 ="";
		String addr = "";
		String remark = "";
		String driverName = "";
		String driverTel = "";


		//llDetailView.setVisibility(View.VISIBLE);
		EmployeeMenu07ListEntity entity = (EmployeeMenu07ListEntity)listAdapter.getItem(position);
		if(entity.isSelected()) {


		} else {
			orderDate = String.format("%s-%s-%s",entity.getOrderDate().substring(0,4),entity.getOrderDate().substring(4,6),entity.getOrderDate().substring(6,8));
			telNo = entity.getTelNo();
			telNo2 = entity.getTelNo2();
			remark = entity.getRemark();
			addr = entity.getAddr();
			driverName = entity.getDriverName();
			driverTel = entity.getdTel();
		}

		String deliveryInfo = getDeliveryDriverInfo(addr);
		listAdapter.setSelectItem(position);

		tvOrderDate.setText(orderDate);
		String tel = "";
		if("null".equals(telNo2)) {
			tel = telNo;
		} else if (null == telNo2) {
			tel = telNo;
		} else {
			tel = telNo + "\n" + telNo2;
		}

		if(null != deliveryInfo) {
			String[] tmp = deliveryInfo.split(" ");
			driverName = tmp[0];
			driverTel= tmp[1];
		}
		tvTelNo.setText(tel);
		tvAddr.setText(addr);
		tvRemark.setText(remark);
		tvDriverName.setText(driverName);
		tvDriverTel.setText(driverTel);

		listAdapter.notifyDataSetChanged();
	}

	@Override
	public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
		if(isFirstStart) {
			isFirstStart = false;
		} else {
			getForwardingOrder();
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etCustName.getWindowToken(), 0);

		int id = v.getId();

		if (id == rlSDate.getId()) {
			if(calendarDialog == null) {
				calendarDialog = new RinnaiCalendarDialog(EmployeeMenu07Activity.this);
			}
			if(!calendarDialog.isShowing()) {
				calendarDialog.setCanceledOnTouchOutside(false);
				calendarDialog.setCancelable(false);

				calendarDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {

						tvSDate.setText(date);
						getForwardingOrder();
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});
				calendarDialog.show();
			}

		} else if(id == rlEDate.getId()) {
			if(calendarDialog == null) {
				calendarDialog = new RinnaiCalendarDialog(EmployeeMenu07Activity.this);
			}
			if(!calendarDialog.isShowing()) {
				calendarDialog.setCanceledOnTouchOutside(false);
				calendarDialog.setCancelable(false);

				calendarDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {

						tvEDate.setText(date);
						getForwardingOrder();
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});
				calendarDialog.show();
			}

		} else if(id == btnSearch.getId()) {
			getSearchValue();

		}
	}

	private void getAgencyInfo() {
		if(!networkConnecting) {

			String value = etCustName.getText().toString();

			if(!"".equals(value)) {
				String url = "";

				url = String.format("%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_AGENCY, CommonValue.HTTP_INFO, value);

				showProgress(EmployeeMenu07Activity.this);

				networkConnecting = true;
				HttpClient.get(url, this);
			} else {
				showRinnaiDialog(EmployeeMenu07Activity.this, getString(R.string.msg_title_noti), "거래처를 입력해주세요.");
				return;
			}
		}

	}

	private void showListPopup(Object obj) {
		RinnaiSearchListDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiSearchListDialog(EmployeeMenu07Activity.this, obj, CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
			@Override
			public void onPositiveClicked(String name, final String code) {

				custCode = code;
				etCustName.setText(custCode);
				tvCustName.setText(name);
//				llCustName.setVisibility(View.VISIBLE);
				Handler delayHandler = new Handler();

				delayHandler.postDelayed(new Runnable() {
					@Override
					public void run() {

//						spItemType.setSelection(0);
						getForwardingOrder();
					}
				}, 100);

			}

			@Override
			public void onPositiveClicked(String barcode, String qty, int position) {
				//productAdd(barcode, qty);
			}

			@Override
			public void onPositiveClicked(String barcode, String modelName, String qty) {

			}
		});
		rinnaiReceivedProductDialog.show();

	}

	private void getSearchValue() {
		//llDetailView.setVisibility(View.INVISIBLE);
		if(!networkConnecting) {
			String value = etCustName.getText().toString();
			String url = "";

			url = String.format("%s/%s/%s/%s/%s/S20/S/319/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_INFO, CommonValue.HTTP_AGENCY, value);

			showProgress(EmployeeMenu07Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}

	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int id = v.getId();
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			 if (id == etCustName.getId()) {
				if(!networkConnecting) {
					getSearchValue();
				} else {
					showRinnaiDialog(EmployeeMenu07Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
				}
			}
			return true;
		}
		return false;
	}

	private String getDeliveryDriverInfo(String addr) {

		if(null == addr) {
			return null;
		}

		int cityPosition = -1;
		String cityName = "";
		for(int i = 0; i < CommonValue.CUSTOMER_DOUBLE_CHECK_CITY_NAME.length; i ++) {
			String city = CommonValue.CUSTOMER_DOUBLE_CHECK_CITY_NAME[i];

			if(addr.indexOf(city) != -1) {
				cityPosition = i;
				cityName = city;
				break;
			}
		}

		if(cityPosition == -1) {
			return null;
		}

		String[][] guValue = mapAddrInfo.get(cityName);
		int guPosition = -1;
		for(int i  = 0; i < guValue.length; i ++) {

			for(int n  = 0; n < guValue[i].length; n ++) {
				String guName = guValue[i][n];

				if(addr.indexOf(guName) != -1) {
					guPosition = i;
					break;
				}
			}
			if(guPosition > -1) {
				break;
			}

		}

		if(guPosition == -1) {
			return null;
		}


		String delivetyInfo = mapDeliveryInfo.get(cityName)[guPosition];

		return delivetyInfo;
	}


}
