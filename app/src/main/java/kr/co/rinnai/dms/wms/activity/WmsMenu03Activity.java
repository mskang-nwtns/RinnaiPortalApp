package kr.co.rinnai.dms.wms.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import device.common.ScanConst;
import device.sdk.ScanManager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.wms.adapter.OrderReportAgencyListAdapter;
import kr.co.rinnai.dms.wms.adapter.OrderReportListAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.wms.model.OrderReportResult;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ObjectComparator;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.wms.model.WmsMenu03ReadingListEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 출고(Picking 지시서) 관련 Activity
 */
public class WmsMenu03Activity extends BaseActivity   {

	private TextView tvOrderReportNo;
	private TextView tvOrderReportDate;
	private TextView tvOrderReportCarNo;
	private TextView tvOrderReportLocation;
	private TextView tvBarcodeType;

	private TextView tvPickingNo;
	private ListView lvOrderReport;
	private ListView lvAgencyOrderReport;

	private RelativeLayout llLocationBarcode;
	private RelativeLayout llAgencyBarcode;

	private LinearLayout llReleaseLocation;
	private LinearLayout llReleaseAgency;

	private OrderReportListAdapter adapter;
	private OrderReportAgencyListAdapter adapterAgency;

	private ArrayList<OrderReportResult> orderReportResultList = null;

	private ArrayList<String> pickingList = null;

	private BroadcastReceiver mReceiver = null;

	private boolean networkConnecting = false;

	private String nowView;

	private ImageView ivTabLocationIcon, ivTabAgencyicon;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String userName;

	private RelativeLayout btnClear;



	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.activity_wms_menu_03);
		tvOrderReportNo = (TextView) findViewById(R.id.tv_order_report_no);
		tvOrderReportDate = (TextView) findViewById(R.id.tv_order_report_date);
		tvOrderReportCarNo = (TextView) findViewById(R.id.tv_order_report_car_no);
		tvOrderReportLocation = (TextView) findViewById(R.id.tv_order_report_location);
		tvBarcodeType = (TextView) findViewById(R.id.tv_barcode_type);

		tvPickingNo = findViewById(R.id.tv_wms_activity_03_picking_no);
		lvOrderReport = (ListView) findViewById(R.id.lv_order_report);
		lvAgencyOrderReport = (ListView) findViewById(R.id.lv_agency_order_report);

		llLocationBarcode = (RelativeLayout) findViewById(R.id.layout_location_barcode);
		llAgencyBarcode = (RelativeLayout) findViewById(R.id.layout_agency_barcode);

		llReleaseLocation = (LinearLayout) findViewById(R.id.ll_release_location);
		llReleaseAgency = (LinearLayout) findViewById(R.id.ll_release_agency);

		tvOrderReportLocation.setOnClickListener(WmsMenu03Activity.this);
		llLocationBarcode.setOnClickListener(WmsMenu03Activity.this);
		llAgencyBarcode.setOnClickListener(WmsMenu03Activity.this);

		ivTabLocationIcon = (ImageView) findViewById(R.id.iv_tab_location_icon);
		ivTabAgencyicon = (ImageView) findViewById(R.id.iv_tab_agency_icon);

		btnClear = (RelativeLayout) findViewById(R.id.btn_wms_activity_clear);

		//위치 모델명 형식 수 량

		ScanManager.getInstance().aDecodeSetTriggerMode(ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT);
		registerReceiver();

		if(CommonValue.adapter != null) {
			adapter = CommonValue.adapter;
			lvOrderReportListener();
		}

		if(CommonValue.adapterAgency != null) {
			adapterAgency = CommonValue.adapterAgency;
			lvAgencyOrderReportListener();
		}

		if(CommonValue.pickingList != null) {
			pickingList = CommonValue.pickingList;
		} else {
			pickingList = new ArrayList<String>();
		}

		if(CommonValue.orderReportResultList != null) {
			orderReportResultList = CommonValue.orderReportResultList;
		}


		llLocationBarcode.setBackgroundResource(R.drawable.release_top_bg_select);
		llAgencyBarcode.setBackgroundResource(R.drawable.release_top_bg_disable);

		ivTabLocationIcon.setVisibility(View.VISIBLE);
		ivTabAgencyicon.setVisibility(View.GONE);

		btnClear.setOnClickListener(WmsMenu03Activity.this);
		if(CommonValue.nowView != null) {
			nowView = CommonValue.nowView;
		} else {
			nowView = CommonValue.WMS_NOW_VIEW_NAME_LOCATION_BARCODE;
		}
		llReleaseLocation.setVisibility(View.GONE);
		llReleaseAgency.setVisibility(View.GONE);

		if(CommonValue.WMS_NOW_VIEW_NAME_LOCATION_BARCODE.equals(nowView)) {
			llReleaseLocation.setVisibility(View.VISIBLE);
			llReleaseAgency.setVisibility(View.GONE);

			llLocationBarcode.setBackgroundResource(R.drawable.release_top_bg_select);
			llAgencyBarcode.setBackgroundResource(R.drawable.release_top_bg_disable);
		} else if (CommonValue.WMS_NOW_VIEW_NAME_AGENCY_BARCODE.equals(nowView)) {
			llReleaseLocation.setVisibility(View.GONE);
			llReleaseAgency.setVisibility(View.VISIBLE);

			llLocationBarcode.setBackgroundResource(R.drawable.release_top_bg_disable);
			llAgencyBarcode.setBackgroundResource(R.drawable.release_top_bg_select);
		}


		helper = new MySQLiteOpenHelper(
				WmsMenu03Activity.this,  // 현재 화면의 제어권자
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

	}

	/**
	 * picking 지시서 로드
	 * @param orderReport
	 */
	private void getOrderReport(String orderReport) {
		String date = orderReport.substring(0, 10);
		String no = orderReport.substring(10, orderReport.length());
		if(!networkConnecting) {
			tvOrderReportNo.setText(orderReport);
			tvOrderReportDate.setText(date);
			tvOrderReportCarNo.setText(no);

			String url = String.format("%s/%s/%s/%s/%s/%s",CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_ORDER_REPORT, date, no, CommonValue.WMS_NOW_VIEW_NAME_LOCATION_BARCODE);

			showProgress(WmsMenu03Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	/**
	 * 창고 Location 바코드 리딩(ex: 04-B10-0)
	 * @param location
	 */
	private void checkWarehouseLocation(String location) {

		tvOrderReportLocation.setText(location);
		ArrayList<OrderReportResult> list = null;
		ArrayList<OrderReportResult> otherList = null;
		if(CommonValue.WMS_NOW_VIEW_NAME_LOCATION_BARCODE.equals(nowView)) {
			list = adapter.getList();
			otherList = adapterAgency.getList();
		} else if (CommonValue.WMS_NOW_VIEW_NAME_AGENCY_BARCODE.equals(nowView)) {
			list = adapterAgency.getList();
			otherList = adapter.getList();
		}


		boolean locationRead = false;
		int readCount = 0;
		List<Integer> readLocationList = new ArrayList<Integer>();
		List<Integer> clickList = new ArrayList<Integer>();

		List<Integer> idxList = new ArrayList<Integer>();
		for( int i = 0; i < list.size(); i ++) {
			OrderReportResult object = list.get(i);

			if(object.isRead()) {
				clickList.add(i);
				if(location.equals(object.getOrderLocation())) {
					idxList.add(i);
					readLocationList.add(i);
				}
			}
		}

		if( clickList.size() == 0 && readLocationList.size()  == 0) {
			showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), "로케이션을 선택 하신 후 바코드를 읽어 주세요." );
		} else if (clickList.size() > 0 && readLocationList.size()  == 0) {
			showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), "선택하신 로케이션과 동일한 로케이션의 바코드를 읽어주세요." );
		} else {
			ArrayList<Integer> positionList = new ArrayList<Integer>();
			ArrayList<OrderReportResult> paramList = new ArrayList<OrderReportResult>();


			JSONArray arrayParam = new JSONArray();

//			for (int n = 0; n < paramList.size(); n++) {

//			}


			for (int n = 0; n < readLocationList.size(); n++) {
				try {
					OrderReportResult orderReportResult = list.get(readLocationList.get(n));

					int idx = idxList.get(n);
					JSONObject jsonObject = ParseUtil.getCompletData(orderReportResult, userName, idx);
					arrayParam.put(jsonObject);

					Log.d("onItemClick", "HTTPClient 작업 필요");
					//positionList.add(n);
					paramList.add(orderReportResult);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//int position = lvOrderReport.getSelectedItemPosition();


			//lvOrderReport.setSelection(position);
			Collections.sort(readLocationList);
			Collections.reverse(readLocationList);



			JSONArray array = new JSONArray();

			for (int n = 0; n < paramList.size(); n++) {
				JSONObject jsonObject = ParseUtil.getCompletData(paramList.get(n), userName, n);
				array.put(jsonObject);
			}
			String url = String.format("%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_ORDER_REPORT, CommonValue.HTTP_LOCATION);

//			2019.11.07 내부 테스트를 위한 서버 전송 X
			showProgress(WmsMenu03Activity.this);
			networkConnecting = true;
			HttpClient.post(url, this, arrayParam.toString());
			Log.d("onItemClick", array.toString());
			//break;
		}

	}

	@Override
	public void onResult(String result) {
		super.onResult(result);
		Log.w("onResult", result);

		dismissProgress();
		networkConnecting = false;
		tvOrderReportLocation.setText("");
		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
		if (null != responseData ) {
			Log.w("onResult", "ok");
			if("200".equals(responseData.getResultCode())) {
				if(responseData.getData() != null) {
					Object obj = responseData.getData();

					String str = JsonParserManager.objectToJson(Object.class, obj);

					Type listType = null;
					if("getOrderReport".equals(responseData.getResultType())) {
						listType = new TypeToken<ArrayList<OrderReportResult>>() {}.getType();

						orderReportResultList = new Gson().fromJson(str, listType);

						//ArrayList<OrderReportResult> tmp = ParseUtil.parseOrderReport(orderReportResultList);
						if(adapter == null) {
							adapter = new OrderReportListAdapter(WmsMenu03Activity.this, orderReportResultList);
						} else {
							ArrayList<OrderReportResult> list = adapter.getList();
							orderReportResultList.addAll(list);

						}


						ObjectComparator comparator = new ObjectComparator("orderLocation");
						Collections.sort(orderReportResultList, comparator);


						CommonValue.orderReportResultList = orderReportResultList;
						CommonValue.adapter = adapter;
						lvOrderReportListener();
						adapter.setList(orderReportResultList);
						adapter.notifyDataSetChanged();
						ArrayList<OrderReportResult> agenyList = new Gson().fromJson(str, listType);


						if(adapterAgency == null) {
							adapterAgency = new OrderReportAgencyListAdapter(WmsMenu03Activity.this, agenyList);
						} else {
							ArrayList<OrderReportResult> list = adapterAgency.getList();
							agenyList.addAll(list);

						}
						comparator = new ObjectComparator("custCode");
						Collections.sort(agenyList, comparator);

						CommonValue.adapterAgency = adapterAgency;
						lvAgencyOrderReportListener();
						adapterAgency.setList(agenyList);
						adapterAgency.notifyDataSetChanged();
						//lvAgencyOrderReport.setOnItemClickListener(WmsMenu03Activity.this);

						if (orderReportResultList.size() == 0) {
							showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
						}
					} else if ("setOrderReportIndividualLocationWork".equals(responseData.getResultType())) {
						listType = new TypeToken<ArrayList<WmsMenu03ReadingListEntity>>() {}.getType();
						List<WmsMenu03ReadingListEntity> list = new Gson().fromJson(str, listType);

						for(int i = 0; i < list.size(); i ++) {
							WmsMenu03ReadingListEntity entity = list.get(i);

						}

						removePickingComplet(list);

					}


				}
			} else {
				showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), responseData.getResultMessage());
			}


		} else {
			showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mReceiver);
	}

	@Override
	public void onClick(View v) {
		//super.onClick(v);
		int id = v.getId();

		if(id == tvOrderReportLocation.getId()) {
			showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), "로케이션 클릭");
		} else if(id == llLocationBarcode.getId()) {
			onClearSelectedList();
			llLocationBarcode.setBackgroundResource(R.drawable.release_top_bg_select);
			llAgencyBarcode.setBackgroundResource(R.drawable.release_top_bg_disable);

			ivTabLocationIcon.setVisibility(View.VISIBLE);
			ivTabAgencyicon.setVisibility(View.GONE);

/*
			llLocationBarcode.setBackgroundResource(R.color.reserve_hour_cell_default);
			llAgencyBarcode.setBackgroundResource(R.color.reserve_hour_cell_highlight);
			*/
//			tvBarcodeType.setText("위치");
			nowView = CommonValue.WMS_NOW_VIEW_NAME_LOCATION_BARCODE;

			llReleaseLocation.setVisibility(View.VISIBLE);
			llReleaseAgency.setVisibility(View.GONE);

		} else if(id == llAgencyBarcode.getId()) {
			onClearSelectedList();
			llLocationBarcode.setBackgroundResource(R.drawable.release_top_bg_disable);
			llAgencyBarcode.setBackgroundResource(R.drawable.release_top_bg_select);

			ivTabLocationIcon.setVisibility(View.GONE);
			ivTabAgencyicon.setVisibility(View.VISIBLE);
/*
			llLocationBarcode.setBackgroundResource(R.color.reserve_hour_cell_highlight);
			llAgencyBarcode.setBackgroundResource(R.color.reserve_hour_cell_default);
			*/
//			tvBarcodeType.setText("대리점");
			nowView = CommonValue.WMS_NOW_VIEW_NAME_AGENCY_BARCODE;

			llReleaseLocation.setVisibility(View.GONE);
			llReleaseAgency.setVisibility(View.VISIBLE);
		} else if (id == btnClear.getId()) {
			pickingList = new ArrayList<String>();
			CommonValue.pickingList = pickingList;
			ArrayList<OrderReportResult> orderReportList = new ArrayList<OrderReportResult>();


			adapterAgency.setList(orderReportList);
			adapter.setList(orderReportList);
			adapter.notifyDataSetChanged();
			adapterAgency.notifyDataSetChanged();

		}

		CommonValue.nowView = nowView;
	}

	/**
	 * 선택된 로케이션 정보 초기화
	 */
	public void onClearSelectedList() {
		if(adapter != null) {
			for (int n = 0; n < adapter.getCount(); n++) {
				OrderReportResult object = (OrderReportResult) adapter.getItem(n);
				if (object.isRead()) {
					adapter.clickPosition(n, "");
				}
			}
			adapter.notifyDataSetChanged();
		}

		if(adapterAgency != null) {
			for (int n = 0; n < adapterAgency.getCount(); n++) {
				OrderReportResult object = (OrderReportResult) adapterAgency.getItem(n);
				if (object.isRead()) {
					adapterAgency.clickPosition(n, "");
				}
			}
			adapterAgency.notifyDataSetChanged();
		}
	}
	/**
	 * Barcode Reader boradcasting 등록
	 */
	private void registerReceiver(){

		if(mReceiver != null) return;

		final IntentFilter theFilter = new IntentFilter();
		theFilter.addAction(CommonValue.BROADCAST_SCANNER_MESSAGE_INTENT);

		this.mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				byte[] scanvalue = intent.getByteArrayExtra(CommonValue.BROADCAST_SCANNER_EXTRA_EVENT_DECODE_VALUE);

				if( null != scanvalue) {
					String value = new String(scanvalue);
					Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : " + value);

					//아리스톤 수입 전기온수기 바코드 관련 사전 처리 사항( 바코드가 16자리로 출력됨 11자리 코드 제거)
					boolean productBarcodeAristonPattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE_ARISTON, value);
					if(productBarcodeAristonPattern) {
						String firstValue = value.substring(0, 10);
						String lastValue = value.substring(11, value.length() );
						value = String.format("%s%s", firstValue, lastValue);
					}



					boolean orderReportNoPattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_NO, value);
					boolean orderReportCustCodePattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_CUST_CODE, value);
					boolean warehouseLocationPattern = Pattern.matches(CommonValue.REGEX_WAREHOUSE_LOCATION, value);

					if(CommonValue.REGEX_SCAN_READ_FAIL.equals(value)) {

					} else if(orderReportNoPattern) {
						tvPickingNo.setText(value);
						try{
							if(pickingList == null) {
								pickingList = new ArrayList<String>();
							}

							if(pickingList.contains(value)) {
								showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), "이미 처리된 지시입니다.");
							} else {

								getOrderReport(value);
								pickingList.add(value);
								CommonValue.pickingList = pickingList;
							}
						} catch(Exception e ) {
							e.printStackTrace();
						}
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "orderReportNoPattern : " + value);
					} else if(orderReportCustCodePattern) {
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "orderReportCustCodePattern : " + value);
					} else if(warehouseLocationPattern) {
						if(!networkConnecting) {
							if(adapter != null) {
								ArrayList<OrderReportResult> list = adapter.getList();
								int readCount = 0;
								if (list != null) {
									for (int i = 0; i < list.size(); i ++) {
										OrderReportResult result = list.get(i);
										if (result.isRead()) {
											readCount ++;
										}
									}
									checkWarehouseLocation(value);

				            		/*
				            		Picking 지시서 작업 완료 후 알림 메세지
				            		if (readCount == list.size()) {
				            			showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_complet));
				            		} else {
//				            			checkWarehouseLocation(value);
				            		}
				            		*/
								}

							}
							Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "warehouseLocationPattern : " + value);
						}

						//checkWarehouseLocation(value);

					} else {
						showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
						long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
						if (Build.VERSION.SDK_INT >= 26) {
							((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
						} else {
							((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
						}

					}
				}

			}
		};

		this.registerReceiver(this.mReceiver, theFilter);


	}

	/**
	 * 로케이션 별 Picking 지시서 ItemClickListener 등록 메소드
	 */
	private void lvOrderReportListener() {
		lvOrderReport.setAdapter(adapter);
		//lvOrderReport.setOnItemClickListener(WmsMenu03Activity.this);
		lvOrderReport.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				OrderReportResult temp = (OrderReportResult)adapter.getItem(position);

				if("수동찾기".equals(temp.getOrderLocation())) {
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WmsMenu03Activity.this);
					alert_confirm.setMessage("수동찾기를 처리하시겠습니까?").setCancelable(false).setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									OrderReportResult reportResult = (OrderReportResult)adapter.getItem(position);
									int oriPosition = 0;
									for(int i = 0; i < orderReportResultList.size(); i ++) {

										OrderReportResult orderReportResult = orderReportResultList.get(i);

										if(	orderReportResult.getOrderLocation().equals(reportResult.getOrderLocation()) &&
												orderReportResult.getModelName().equals( reportResult.getModelName()) &&
												orderReportResult.getGasType().equals(reportResult.getGasType()) &&
												orderReportResult.getOrderNo().equals(reportResult.getOrderNo()) &&
												orderReportResult.getOrderDetail().equals(reportResult.getOrderDetail()) )  {
											oriPosition = i;

											//index(i) 작업 확인 필요
											JSONObject jsonObject = ParseUtil.getCompletData(orderReportResult, userName, i);
											JSONArray array = new JSONArray();
											array.put(jsonObject);
											Log.d("onItemClick", "수동찾기 클릭 : " + array.toString());
											String url = String.format("%s/%s/%s/%s",CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_ORDER_REPORT, CommonValue.HTTP_LOCATION);
											HttpClient.post(url, WmsMenu03Activity.this, array.toString());

											break;
										}

									}

									//orderReportResultList.remove(oriPosition);


									//adapter.setCompleted(position);

									//adapter.notifyDataSetChanged();
								}
							}).setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								@Override

								public void onClick(DialogInterface dialog, int which) {
									// 'No'
									return;
								}
							});
					AlertDialog alert = alert_confirm.create();
					alert.show();


				} else {
					String location = null;
					int idx = -1;
					for(int n = 0; n < adapter.getCount(); n ++) {

						OrderReportResult object = (OrderReportResult)adapter.getItem(n);

						if(object.isRead()) {
							location = object.getOrderLocation();
							idx = n;
							break;
						}


					}
					OrderReportResult object = (OrderReportResult)adapter.getItem(position);

					if(location == null || object.getOrderLocation().equals(location)) {
						adapter.clickPosition(position, "");
						adapter.notifyDataSetChanged();
					} else {
						onClearSelectedList();
						adapter.clickPosition(position, "");
						adapter.notifyDataSetChanged();
						//showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_not_equals_select_location));
					}

				}

			}

		});

		lvOrderReport.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


				OrderReportResult result = (OrderReportResult)adapter.getItem(position);
				if("수동찾기".equals(result.getOrderLocation())) {
					Intent intent = new Intent(WmsMenu03Activity.this, WmsMenu05Activity.class);
					String itemCode = result.getModelCode();
					String modelName = result.getModelName();
					intent.putExtra(CommonValue.WMS_PARAMETER_KEY_MODEL_CODE, itemCode);
					intent.putExtra(CommonValue.WMS_PARAMETER_KEY_MODEL_NAME, modelName);

					startActivity(intent);
					finish();
				}
				return false;

			}
		});

	}

	/**
	 * 대리점 별 Picking 지시서 ItemClickListener 등록 메소드
	 */
	private  void lvAgencyOrderReportListener() {
		lvAgencyOrderReport.setAdapter(adapterAgency);
		lvAgencyOrderReport.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				OrderReportResult temp = (OrderReportResult)adapterAgency.getItem(position);

				if("수동찾기".equals(temp.getOrderLocation())) {
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WmsMenu03Activity.this);
					alert_confirm.setMessage("수동찾기를 처리하시겠습니까?").setCancelable(false).setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									OrderReportResult reportResult = (OrderReportResult)adapterAgency.getItem(position);
									int oriPosition = 0;
									for(int i = 0; i < orderReportResultList.size(); i ++) {

										OrderReportResult orderReportResult = orderReportResultList.get(i);

										if(	orderReportResult.getOrderLocation().equals(reportResult.getOrderLocation()) &&
												orderReportResult.getModelName().equals( reportResult.getModelName()) &&
												orderReportResult.getGasType().equals(reportResult.getGasType()) &&
												orderReportResult.getOrderNo().equals(reportResult.getOrderNo()) &&
												orderReportResult.getOrderDetail().equals(reportResult.getOrderDetail()) )  {
											oriPosition = i;
											//index(i) 작업 확인 필요
											JSONObject jsonObject = ParseUtil.getCompletData(orderReportResult, userName, i);
											JSONArray array = new JSONArray();
											array.put(jsonObject);
											Log.d("onItemClick", "수동찾기 클릭 : " + array.toString());
											String url = String.format("%s/%s/%s/%s",CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_ORDER_REPORT, CommonValue.HTTP_LOCATION);
											HttpClient.post(url, WmsMenu03Activity.this, array.toString());

											break;
										}

									}

									//orderReportResultList.remove(oriPosition);
//									adapterAgency.setCompleted(position);
//
//									adapterAgency.notifyDataSetChanged();
								}
							}).setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								@Override

								public void onClick(DialogInterface dialog, int which) {
									// 'No'
									return;
								}
							});
					AlertDialog alert = alert_confirm.create();
					alert.show();


				} else {
					String location = null;
					int idx = -1;
					for(int n = 0; n < adapterAgency.getCount(); n ++) {

						OrderReportResult object = (OrderReportResult)adapterAgency.getItem(n);

						if(object.isRead()) {
							location = object.getOrderLocation();
							idx = n;
							break;
						}


					}
					OrderReportResult object = (OrderReportResult)adapterAgency.getItem(position);

					if(location == null || object.getOrderLocation().equals(location)) {
						adapterAgency.clickPosition(position, "");
						adapterAgency.notifyDataSetChanged();
					} else {
						onClearSelectedList();
						adapterAgency.clickPosition(position, "");
						adapterAgency.notifyDataSetChanged();
//								showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_not_equals_select_location));
					}

				}

			}

		});

		lvAgencyOrderReport.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


				OrderReportResult result = (OrderReportResult)adapterAgency.getItem(position);
				if("수동찾기".equals(result.getOrderLocation())) {
					Intent intent = new Intent(WmsMenu03Activity.this, WmsMenu05Activity.class);
					String itemCode = result.getModelCode();
					String modelName = result.getModelName();
					intent.putExtra(CommonValue.WMS_PARAMETER_KEY_MODEL_CODE, itemCode);
					intent.putExtra(CommonValue.WMS_PARAMETER_KEY_MODEL_NAME, modelName);

					startActivity(intent);
					finish();
				}
				return false;

			}
		});
	}

	private void removePickingComplet(List<WmsMenu03ReadingListEntity> list) {

		ArrayList<OrderReportResult> viewList = null;
		ArrayList<OrderReportResult> otherList = null;

		if(CommonValue.WMS_NOW_VIEW_NAME_LOCATION_BARCODE.equals(nowView)) {
			viewList = adapter.getList();
			otherList = adapterAgency.getList();
		} else if (CommonValue.WMS_NOW_VIEW_NAME_AGENCY_BARCODE.equals(nowView)) {
			viewList = adapterAgency.getList();
			otherList = adapter.getList();
		}

		List<Integer> otherIdx = new ArrayList<Integer>();
		List<Integer> viewIdx = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			WmsMenu03ReadingListEntity entity = list.get(i);
			OrderReportResult otherObject = viewList.get(entity.getIndex());
			for (int n = 0; n < otherList.size(); n++) {

				OrderReportResult orderReportResult = otherList.get(n);

				if (otherObject.getOrderDate().equals(orderReportResult.getOrderDate()) &&
						otherObject.getOrderNo().equals(orderReportResult.getOrderNo()) &&
						otherObject.getOrderLocation().equals(orderReportResult.getOrderLocation()) &&
						otherObject.getGasType().equals(orderReportResult.getGasType()) &&
						otherObject.getModelCode().equals(orderReportResult.getModelCode()) &&
						otherObject.getOrderDetail().equals(orderReportResult.getOrderDetail())&&
						entity.isStatus()) {
					viewIdx.add(entity.getIndex());
					otherIdx.add(n);
					Log.d("onItemClick", "HTTPClient 작업 필요");

					break;

				}

				//orderReportResultList.remove(idx);

			}
		}

		Collections.sort(viewIdx);
		Collections.reverse(viewIdx);
		for (int n = 0; n < viewIdx.size(); n++) {
			int idx = viewIdx.get(n);
			//orderReportResultList.remove(idx);

			if (CommonValue.WMS_NOW_VIEW_NAME_LOCATION_BARCODE.equals(nowView)) {
				adapter.setCompleted(idx);
			} else if (CommonValue.WMS_NOW_VIEW_NAME_AGENCY_BARCODE.equals(nowView)) {
				adapterAgency.setCompleted(idx);
			}
		}


		showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), String.format("Picking 작업 %d건 중 %d건을\n완료하였습니다.",list.size(), viewIdx.size() ));


		Collections.sort(otherIdx);
		Collections.reverse(otherIdx);


		//현재 선택 탭이 아닌 탭 목록 삭제를 위한 선택된 row 삭제
		for (int i = 0; i < otherIdx.size(); i++) {
			if (CommonValue.WMS_NOW_VIEW_NAME_LOCATION_BARCODE.equals(nowView)) {

				adapterAgency.setCompleted(otherIdx.get(i));
			} else if (CommonValue.WMS_NOW_VIEW_NAME_AGENCY_BARCODE.equals(nowView)) {
				adapter.setCompleted(otherIdx.get(i));
			}
		}



		adapter.notifyDataSetChanged();
		adapterAgency.notifyDataSetChanged();

	}
}