package kr.co.rinnai.dms.eos.activity;

import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
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
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.aos.activity.AgencyMenu05Activity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.custom.RinnaiSearchListDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.CategorizationResultVO;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.adapter.EmployeeMenu06ListAdapter;
import kr.co.rinnai.dms.eos.adapter.ItemSpinnerAdapter;
import kr.co.rinnai.dms.eos.model.EmployeeMenu06ListEntity;
import kr.co.rinnai.dms.eos.model.ItemCode;
import kr.co.rinnai.dms.eos.model.SalesAgencyInfoResult;

/**
 * 출고 현황
 *
 */
public class EmployeeMenu06Activity extends BaseActivity implements  AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, View.OnTouchListener, View.OnKeyListener{


	private List<ItemCode> codeList = null;
	private RelativeLayout rlSDate, rlEDate;
	private RelativeLayout btnSearch;

	private Spinner spItemType;
	private ItemSpinnerAdapter spinnerAdapter;

	private ListView lvOrderList;
	private EmployeeMenu06ListAdapter listAdapter;

	private EditText etCustName;

	private boolean networkConnecting = false;

	private TextView tvSDate, tvEDate;

	private TextView tvOrderDate, tvOrderItemName, tvWareHouseName, tvDriverName, tvTelNo, tvRemark;

	private TextView tvCustName;

	private LinearLayout llCustName;

	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	private String custCode = "";

	private boolean  isFirstStart = true;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String userName;

	private RinnaiCalendarDialog calendarDialog;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_menu_06);


		boolean isTablet = Util.isTabletDevice(EmployeeMenu06Activity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

//		codeList = getCodeList();


		rlSDate = (RelativeLayout) findViewById(R.id.rl_employee_05_order_from_date);
		rlEDate = (RelativeLayout) findViewById(R.id.rl_employee_05_order_to_date);

		tvSDate = (TextView) findViewById(R.id.tv_employee_05_order_from_date);
		tvEDate = (TextView) findViewById(R.id.tv_employee_05_order_to_date);
		spItemType = (Spinner) findViewById(R.id.sp_employee_06_activity_item_type);
		lvOrderList = (ListView) findViewById(R.id.lv_employee_06_order_list);
		btnSearch = (RelativeLayout) findViewById(R.id.btn_empolyee_06_activity_search);

		lvOrderList.setOnItemClickListener(EmployeeMenu06Activity.this);
		etCustName = (EditText) findViewById(R.id.et_empolyee_06_activity_cust_name);
		etCustName.setOnKeyListener(EmployeeMenu06Activity.this);
//		etCustName.setOnClickListener(EmployeeMenu06Activity.this);

		tvOrderDate = (TextView) findViewById(R.id.tv_employee_06_activity_list_detail_order_date);
		tvOrderItemName = (TextView) findViewById(R.id.tv_employee_06_activity_list_detail_order_item_name);
		tvWareHouseName = (TextView) findViewById(R.id.tv_employee_06_activity_list_detail_warehouse_name);
		tvDriverName = (TextView) findViewById(R.id.tv_employee_06_activity_list_detail_driver_name);
		tvTelNo = (TextView) findViewById(R.id.tv_employee_06_activity_list_detail_tel_no);
		tvRemark = (TextView) findViewById(R.id.tv_employee_06_activity_list_detail_remark);

		tvCustName = (TextView) findViewById(R.id.tv_empolyee_06_activity_cust_name);
		llCustName = (LinearLayout) findViewById(R.id.ll_empolyee_06_activity_cust_name);


		rlSDate.setOnClickListener(EmployeeMenu06Activity.this);
		rlEDate.setOnClickListener(EmployeeMenu06Activity.this);
		btnSearch.setOnClickListener(EmployeeMenu06Activity.this);

		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		String eDate = format1.format(cal.getTime());
//		cal.add(Calendar.DATE, );
		String sDate = format1.format(cal.getTime());
		tvEDate.setText(eDate);
		tvSDate.setText(sDate);


		helper = new MySQLiteOpenHelper(
				EmployeeMenu06Activity.this,  // 현재 화면의 제어권자
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

		String gwId = RinnaiApp.getInstance().getGwId();
		String tmpId =  null;
		if(null != gwId) tmpId = gwId.replace("@rinnai.co.kr", "");
		if("mini3248".equals(tmpId) || "ssj9567".equals(tmpId)) {

		} else {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		}
		if("mini3248".equals(tmpId) || "ssj9567".equals(tmpId)) {

		} else {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		}

		getItemCode();

	}

	@Override
	public void onResult(String result) {

		super.onResult(result);
		Log.w("onResult", result);

		dismissProgress();
		networkConnecting = false;

		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);

		if (null != responseData ) {
			Log.w("onResult", "ok");
			if("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);
				Type type = null;
				if("getForwardingOrder".equals(responseData.getResultType())) {


					type = new TypeToken<ArrayList<EmployeeMenu06ListEntity>>(){}.getType();

					List<EmployeeMenu06ListEntity> list = new Gson().fromJson(str, type);
					listAdapter = new EmployeeMenu06ListAdapter(EmployeeMenu06Activity.this, list);
					lvOrderList.setAdapter(listAdapter);
					listAdapter.notifyDataSetChanged();
//					lvStockWarehouse.setAdapter(adapter);

//					adapter.notifyDataSetChanged();
					lvOrderList.setVisibility(View.VISIBLE);

				} else if ("getAgencyList".equals(responseData.getResultType())) {
					type = new TypeToken<ArrayList<SalesAgencyInfoResult>>(){}.getType();
					List<SalesAgencyInfoResult> list = new Gson().fromJson(str, type);
					if(list.size() == 1) {
						SalesAgencyInfoResult agencyInfo = list.get(0);

						custCode = agencyInfo.getCustCode();
					//	etCustName.setText(agencyInfo.getCustName());

						etCustName.setText(custCode);
						tvCustName.setText(agencyInfo.getCustName());
						llCustName.setVisibility(View.VISIBLE);


						spItemType.setSelection(0);
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
				} else if("getItemCode".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<ItemCode>>(){}.getType();
					List<ItemCode> codeList = new Gson().fromJson(str, type);

					spinnerAdapter = new ItemSpinnerAdapter(EmployeeMenu06Activity.this, codeList);
					spItemType.setAdapter(spinnerAdapter);

					spinnerAdapter.notifyDataSetChanged();
					spItemType.setOnItemSelectedListener(EmployeeMenu06Activity.this);
				}

			} else {
				dismissProgress();
				showRinnaiDialog(EmployeeMenu06Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
				lvOrderList.setVisibility(View.INVISIBLE);
			}
		}
	}


	private List<ItemCode> getCodeList() {

		List<ItemCode> list = new ArrayList<ItemCode>();
		ItemCode code = new ItemCode();

		code.setCodeItem("0");
		code.setCodeName("전체");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("1");
		code.setCodeName("주방부문");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("2");
		code.setCodeName("가전부문");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("3");
		code.setCodeName("온수시스템부문");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("4");
		code.setCodeName("보일러부문");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("5");
		code.setCodeName("업소용");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("6");
		code.setCodeName("공사구분");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("8");
		code.setCodeName("수출");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("9");
		code.setCodeName("판매할인");
		list.add(code);

		return list;
	}

	private void getForwardingOrder() {
		if(!networkConnecting) {

			String url = "";
			ItemCode itemCode = (ItemCode)spItemType.getSelectedItem();


			String itemType = itemCode.getCodeItem();
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

				showRinnaiDialog(EmployeeMenu06Activity.this, getString(R.string.msg_title_noti), "조회 시작일이 조회 종료일보다 느립니다.");
				return;

			}

			if("".equals(custCode)) {
				showRinnaiDialog(EmployeeMenu06Activity.this, getString(R.string.msg_title_noti), "대리점 조회");
				return;
			}

			url = String.format("%s/%s/%s/%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_ORDER, CommonValue.HTTP_INFO, custCode, itemType, sDate, eDate, userName);

			showProgress(EmployeeMenu06Activity.this);

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
		String orderItemName = "";
		String wareHouseName = "";
		String driverName = "";
		String telNo = "";
		String remark = "";

		EmployeeMenu06ListEntity entity = (EmployeeMenu06ListEntity)listAdapter.getItem(position);
		if(entity.isSelected()) {


		} else {
			orderDate = String.format("%s-%s-%s",entity.getOrderDate().substring(0,4),entity.getOrderDate().substring(4,6),entity.getOrderDate().substring(6,8));
			orderItemName = entity.getOrderItemName();
			wareHouseName = entity.getWareHouseName();
			driverName = entity.getDeriverName();
			telNo = entity.getTelNo();
			remark = entity.getRemark();
		}
		listAdapter.setSelectItem(position);


		tvOrderDate.setText(orderDate);
		tvOrderItemName.setText(orderItemName);
		tvWareHouseName.setText(wareHouseName);
		tvDriverName.setText(driverName);
		tvTelNo.setText(telNo);
		tvRemark.setText(remark);
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
				calendarDialog = new RinnaiCalendarDialog(EmployeeMenu06Activity.this);
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
				calendarDialog = new RinnaiCalendarDialog(EmployeeMenu06Activity.this);
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
			String value = etCustName.getText().toString();
			if(value.equals(custCode)) {
				getForwardingOrder();
			} else {
				getAgencyInfo();
			}

		}
	}

	private void getAgencyInfo() {
		if(!networkConnecting) {

			String value = etCustName.getText().toString();

			if(!"".equals(value)) {
				String url = "";

				url = String.format("%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_AGENCY, CommonValue.HTTP_INFO, value);

				showProgress(EmployeeMenu06Activity.this);

				networkConnecting = true;
				HttpClient.get(url, this);
			} else {
				showRinnaiDialog(EmployeeMenu06Activity.this, getString(R.string.msg_title_noti), "거래처를 입력해주세요.");
				return;
			}
		}

	}

	private void showListPopup(Object obj) {
		RinnaiSearchListDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiSearchListDialog(EmployeeMenu06Activity.this, obj, CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
			@Override
			public void onPositiveClicked(String name, final String code) {

				custCode = code;
				etCustName.setText(custCode);
				tvCustName.setText(name);
				llCustName.setVisibility(View.VISIBLE);
				Handler delayHandler = new Handler();

				delayHandler.postDelayed(new Runnable() {
					@Override
					public void run() {

						spItemType.setSelection(0);
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

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int id = v.getId();
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			 if (id == etCustName.getId()) {
				if(!networkConnecting) {
					getForwardingOrder();
				} else {
					showRinnaiDialog(EmployeeMenu06Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 매장 재고 조회
	 *
	 */
	private void getItemCode() {
//		String value =  etSearchValue.getText().toString();
		//    /stock/{date}/{type}/{value}
		if(!networkConnecting) {

			String url = String.format("%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_ITEMCODE);

			showProgress(EmployeeMenu06Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}

	}
}
