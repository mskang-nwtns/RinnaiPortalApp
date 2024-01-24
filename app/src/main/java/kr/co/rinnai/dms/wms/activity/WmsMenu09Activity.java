package kr.co.rinnai.dms.wms.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import device.common.ScanConst;
import device.sdk.ScanManager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiReceivedProductDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ObjectComparator;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.wms.adapter.OrderReportAgencyListAdapter;
import kr.co.rinnai.dms.wms.adapter.OrderReportListAdapter;
import kr.co.rinnai.dms.wms.adapter.WmsMenu08ListAdapter;
import kr.co.rinnai.dms.wms.adapter.WmsMenu09ListAdapter;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeReading;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeResult;
import kr.co.rinnai.dms.wms.model.CoupangBarcodeReading;
import kr.co.rinnai.dms.wms.model.LocationInfoRequest;
import kr.co.rinnai.dms.wms.model.LocationInfoResult;
import kr.co.rinnai.dms.wms.model.LpsSuspendResultVO;
import kr.co.rinnai.dms.wms.model.OrderReportResult;
import kr.co.rinnai.dms.wms.model.WmsMenu03ReadingListEntity;
import kr.co.rinnai.dms.wms.model.WmsMenu09ListEntity;

/**
 * 쿠팡 배송 관련 Activity
 */
public class WmsMenu09Activity extends BaseActivity implements AdapterView.OnItemClickListener  {
	private BroadcastReceiver mReceiver = null;
	private boolean networkConnecting = false;

	private TextView tvTotalInstructions;
	private TextView tvTotalOperations;
	private TextView tvSuccessCount;
	private TextView tvRemainingCount;
	private RelativeLayout btnSubmit;
	private RelativeLayout btnClear;
	private RelativeLayout btnReset;

	private SQLiteDatabase db;

	private MySQLiteOpenHelper helper;

	//총 작업 지시 수량
	private int totalInstructions = 0;

	//현재 읽은 수량
	private int totalOperations = 0;

	//서버에 전송 완료된 읽은 수량
	private int totalSuccessCount = 0;

	//남은 지시 수량
	private int totalRemainingCount = 0;

	private ArrayList<String> agencyOrderBarcodeList = new ArrayList<String>();

	private ArrayList<AgencyBarcodeResult> agencyBarcodeResultList = null;

	private ListView lvReadProductBarcode = null;
	private WmsMenu09ListAdapter adapter = null;

	private ArrayList<LocationInfoResult> readProductBarcodeList = null;

	private String userNo;

	private EditText etReadingBarcode;

	private RelativeLayout btnReadingBarcode;


    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.activity_wms_menu_09);

		tvTotalOperations = findViewById(R.id.tv_total_operations);
		tvTotalInstructions = findViewById(R.id.tv_total_instructions);
		tvSuccessCount = findViewById(R.id.tv_success_count);
		tvRemainingCount = findViewById(R.id.tv_remaining_count);
		btnSubmit = (RelativeLayout) findViewById(R.id.btn_wms_activity_submit);
		btnClear = (RelativeLayout)findViewById(R.id.btn_wms_activity_clear);

		btnSubmit = (RelativeLayout) findViewById(R.id.btn_wms_activity_submit);
		btnClear = (RelativeLayout)findViewById(R.id.btn_wms_activity_clear);
		btnReset = (RelativeLayout) findViewById(R.id.btn_wms_activity_reset);


		etReadingBarcode = (EditText) findViewById(R.id.et_reading_instructions_barcode);

		btnReadingBarcode = (RelativeLayout) findViewById(R.id.btn_reading_instructions_barcode);
		btnReadingBarcode.setOnClickListener(WmsMenu09Activity.this);


		btnSubmit.setOnClickListener(WmsMenu09Activity.this);
		btnClear.setOnClickListener(WmsMenu09Activity.this);

		//위치 모델명 형식 수 량

		ScanManager.getInstance().aDecodeSetTriggerMode(ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT);
		registerReceiver();


		helper = new MySQLiteOpenHelper(
				WmsMenu09Activity.this,  // 현재 화면의 제어권자
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
			userNo = c.getString(1);
			String userName = c.getString(2);

		}


		if(CommonValue.Act_9_totalInstructions > 0) {
			totalInstructions = CommonValue.Act_9_totalInstructions;
		}

		if(CommonValue.Act_9_totalOperations > 0) {
			totalOperations = CommonValue.Act_9_totalOperations;
		}

		if(CommonValue.Act_9_totalSuccessCount > 0) {
			totalSuccessCount = CommonValue.Act_9_totalSuccessCount;
		}

		if(CommonValue.Act_9_totalRemainingCount > 0) {
			totalRemainingCount = CommonValue.Act_9_totalRemainingCount;
		}

		if(CommonValue.Act_9_agencyOrderBarcodeList != null) {
			agencyOrderBarcodeList = CommonValue.Act_9_agencyOrderBarcodeList;
		}

		lvReadProductBarcode = (ListView) findViewById(R.id.lv_read_product_barcode);
		lvReadProductBarcode.setOnItemClickListener(WmsMenu09Activity.this);
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

					boolean orderReportCustCodePattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_CUST_CODE, value);
					boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, value);
					boolean orderReportNoPattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_NO, value);
					if (productBarcodePattern) {


						etReadingBarcode.setText(value);
						getLpsSuspend(value);
						//readProductBarcode(value);
					} else if (orderReportCustCodePattern) {

						if(agencyOrderBarcodeList == null) {
							agencyOrderBarcodeList = new ArrayList<String>();
						}
						if(agencyOrderBarcodeList.contains(value)) {
							showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "이미 처리된 지시입니다.");
						} else {
							etReadingBarcode.setText(value);
							getOrderReport(value);
						}

					} else if (orderReportNoPattern) {
						if(agencyOrderBarcodeList == null) {
							agencyOrderBarcodeList = new ArrayList<String>();
						}
						if(agencyOrderBarcodeList.contains(value)) {
							showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "이미 처리된 지시입니다.");
						} else {
							etReadingBarcode.setText(value);
							agencyOrderBarcodeList.add(value);
							getCoupangPickingInfo(value);
						}
					} else {
						showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
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
	 * 출하정지 제품 조회
	 * @param barcode
	 */
	private void getLpsSuspend(String barcode) {


		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu09Activity.this);


			url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_SUSPEND, barcode);
			showProgress(WmsMenu09Activity.this);

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
		if (null != responseData ) {
			Log.w("onResult", "ok");
			if(responseData.getData() != null) {

				Object tmp = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, tmp);

				Type listType = null;
				if("getAgencyOrderInfo".equals(responseData.getResultType()) || "getCoupangOrderByPicking".equals(responseData.getResultType())) {
					listType = new TypeToken<ArrayList<AgencyBarcodeResult>>(){}.getType();
					agencyBarcodeResultList = new Gson().fromJson(str, listType);

					List<String> readCheck = new ArrayList<String>();

					for(int i =0 ; i < agencyBarcodeResultList.size(); i ++) {

						AgencyBarcodeResult agencyBarcode = agencyBarcodeResultList.get(i);
						String orderBarcode = agencyBarcode.getAgencyOrderBarcode();

						if(agencyOrderBarcodeList.contains(orderBarcode)&&!readCheck.contains(orderBarcode)) {
							readCheck.add(orderBarcode);

						}

					}

					for(int i =0 ; i < agencyBarcodeResultList.size(); i ++) {

						AgencyBarcodeResult agencyBarcode = agencyBarcodeResultList.get(i);
						String readingBarcode = agencyBarcode.getAgencyOrderBarcode();

						if(!agencyOrderBarcodeList.contains(readingBarcode)){
							agencyOrderBarcodeList.add(readingBarcode);
						}

						if(!readCheck.contains(agencyBarcode.getAgencyOrderBarcode())) {
							String query = "INSERT OR REPLACE INTO %s (%s,%s,%s,%s,%s,%s,%s,%s,%s ) values ('%s', '%s', '%s', '%s', '%s', '%s', %d, %d , '%s');";

							query = String.format(query,
									CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_COUPANG,
									CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
									CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME ,
									CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE  ,
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME ,
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE,
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
									agencyBarcode.getAgencyOrderBarcode(),
									agencyBarcode.getCustName(),
									agencyBarcode.getCustCode(),
									agencyBarcode.getModelName(),
									agencyBarcode.getItemCode(),
									agencyBarcode.getType(),
									Integer.parseInt(agencyBarcode.getInstructions()),
									Integer.parseInt( agencyBarcode.getOperations()),
									agencyBarcode.getRemark()
//                            i
							);
							db.execSQL(query);

							totalInstructions += Integer.parseInt(agencyBarcode.getInstructions());
							totalSuccessCount += Integer.parseInt( agencyBarcode.getOperations());

							LocationInfoResult infoResult = new LocationInfoResult();
							String itemCode = agencyBarcode.getItemCode();
							String barcode = String.format("%s%s", itemCode.substring(0, 5), itemCode.substring(8, 9));
							infoResult.setBarcode(barcode);
							infoResult.setModelName(agencyBarcode.getModelName());
							infoResult.setType(agencyBarcode.getItemCode().substring(8, 9));
							infoResult.setCellMake("");
							infoResult.setItemCode(itemCode);
							infoResult.setQty("0");
							infoResult.setTotal(agencyBarcode.getInstructions());

//								adapter.addListItem(infoResult);

							Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, agencyBarcode.toString());

							totalRemainingCount = totalInstructions - totalSuccessCount;

							tvTotalInstructions.setText(String.format("%d", totalInstructions));
							tvSuccessCount.setText(String.format("%d", totalSuccessCount));
							tvTotalOperations.setText(String.format("%d", totalOperations));
							tvRemainingCount.setText(String.format("%d", totalRemainingCount));

							CommonValue.Act_9_totalInstructions = totalInstructions;
							CommonValue.Act_9_totalOperations = totalOperations;
							CommonValue.Act_9_totalSuccessCount = totalSuccessCount;
							CommonValue.Act_9_totalRemainingCount = totalRemainingCount;

						}

					}

				} else if ("setAgencyBarcodeWorkCoupang".equals(responseData.getResultType())) {
					listType = new TypeToken<ArrayList<CoupangBarcodeReading>>(){}.getType();
					ArrayList<CoupangBarcodeReading> list = new Gson().fromJson(str, listType);

					Log.d("DB","list size:" + list.size());

					int success = 0;
					int fail = 0;
					int count= 0;

					for(int i = 0; i < list.size(); i ++) {
						CoupangBarcodeReading reading = list.get(i);
						String query = "UPDATE %s SET %s  = '%s'  WHERE %s = '%s' AND %s = '%s'";
						count = count + reading.getCount();
						if(reading.isStatus()) {
							query = String.format(query,
									CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG,
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
									"success",
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
									reading.getRemark(),
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
									reading.getItemCode()
							);
							Log.d("DB","query:"+query);
							db.execSQL(query);
							success = success + reading.getCount();
							totalSuccessCount = totalSuccessCount + reading.getCount();
						} else {
							query = String.format(query,
									CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG,
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ,
									String.format("%s", reading.getPalletSeq()),
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
									reading.getRemark(),
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
									reading.getItemCode()
							);
							Log.d("DB","query:"+query);
							db.execSQL(query);
							fail = fail + reading.getCount();
						}
					}

					showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), String.format(" 총 %d건 중 %d건 전송하였습니다.", count, success ));

					totalRemainingCount = totalInstructions - totalSuccessCount;
					totalOperations = totalOperations - success;

					tvTotalOperations.setText(String.format("%d", totalOperations));

					tvSuccessCount.setText(String.format("%d", totalSuccessCount));
					tvRemainingCount.setText(String.format("%d", totalRemainingCount));

					CommonValue.Act_9_totalInstructions = totalInstructions;
					CommonValue.Act_9_totalOperations = totalOperations;
					CommonValue.Act_9_totalSuccessCount = totalSuccessCount;
					CommonValue.Act_9_totalRemainingCount = totalRemainingCount;

					ArrayList<LocationInfoResult> tmpReadList = new ArrayList<LocationInfoResult>();

					for(int i = 0; i < readProductBarcodeList.size(); i ++ ) {
						LocationInfoResult info = readProductBarcodeList.get(i);

						for(int n = 0; n < list.size(); n ++) {
							CoupangBarcodeReading reading = list.get(n);

							Log.d("test", "::"+reading.getItemCode().trim() + "::"+info.getOriginalItemCode().trim()+"::");
							Log.d("test", "::"+reading.getCount()  + "::"+Integer.parseInt(info.getQty())+"::");
							Log.d("test", "::"+reading.getRemark().trim() + "::"+info.getOriginalCellMake().trim()+"::");
							if(reading.getAgencyOrderBarcode().equals(info.getCellDetail().replace("/", "-")) &&
									reading.getItemCode().trim().equals(info.getOriginalItemCode().trim()) &&
									reading.getCount() == Integer.parseInt(info.getQty()) &&
									reading.getRemark().trim().equals(info.getOriginalCellMake().trim())
									) {
								if(!reading.isStatus()) {
									tmpReadList.add(info);
								}
								break;

							}
						}

					}
					readProductBarcodeList = tmpReadList;

					adapter.setList(readProductBarcodeList);
					adapter.notifyDataSetChanged();
					if(totalInstructions == totalSuccessCount && totalRemainingCount == 0) {
						totalInstructions = 0;
						totalOperations = 0;
						totalSuccessCount = 0;
						totalRemainingCount = 0;

						CommonValue.Act_9_totalInstructions = totalInstructions;
						CommonValue.Act_9_totalOperations = totalOperations;
						CommonValue.Act_9_totalSuccessCount = totalSuccessCount;
						CommonValue.Act_9_totalRemainingCount = totalRemainingCount;

					}
				} else if("getLpsSuspend".equals(responseData.getResultType())) {
					listType = new TypeToken<LpsSuspendResultVO>(){}.getType();
					LpsSuspendResultVO suspend = new Gson().fromJson(str, listType);
					if(null == suspend.getMemo()) {
						readProductBarcode(suspend.getBarcode());
					} else {
						String path = "/system/media/audio/alarms/Osmium.ogg";
						String message = String.format("%s(%s) \n %s ", suspend.getModelName(), suspend.getBarcode(), getString(R.string.warehouse_04_activity_lps_suspend));
						showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), message, path);
						long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
						if (Build.VERSION.SDK_INT >= 26) {
							((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
						} else {
							((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
						}

					}
				}

			}
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mReceiver);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == btnClear.getId()) {
			totalInstructions = 0;
			totalOperations = 0;
			totalSuccessCount = 0;
			totalRemainingCount = 0;

			CommonValue.Act_9_totalInstructions = totalInstructions;
			CommonValue.Act_9_totalOperations = totalOperations;
			CommonValue.Act_9_totalSuccessCount = totalSuccessCount;
			CommonValue.Act_9_totalRemainingCount = totalRemainingCount;

			tvTotalOperations.setText(String.format("%d", totalOperations));
			tvTotalInstructions.setText(String.format("%d", totalInstructions));
			tvSuccessCount.setText(String.format("%d", totalSuccessCount));
			tvRemainingCount.setText(String.format("%d", totalRemainingCount));




			if(CommonValue.Act_9_agencyOrderBarcodeList != null) {
				CommonValue.Act_9_agencyOrderBarcodeList = agencyOrderBarcodeList;
			}

			if(agencyOrderBarcodeList.size() > 0) {
				String tmpBarcode = "";
				for (int i = 0; i < agencyOrderBarcodeList.size(); i++) {
					String barcode = agencyOrderBarcodeList.get(i);
					if (barcode.length() > 15) {
						String formatter = "";
						if (tmpBarcode.length() == 0) {
							formatter = "'%s' ";
						} else {
							formatter = ", '%s' ";
						}

						tmpBarcode += String.format(formatter, barcode);
					}

				}
				final String finalTmpBarcode = tmpBarcode;

				String query = "DELETE FROM %s WHERE %s in ( %s ) AND %s = '%s';";
				query = String.format(query,
						CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG,
						CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
						finalTmpBarcode,
						CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
						"reading"
				);
				db.execSQL(query);


				Log.w("DB", query);
			}

			agencyOrderBarcodeList = new ArrayList<String>();
			readProductBarcodeList = new ArrayList<LocationInfoResult>();
			if(adapter != null) {
				adapter.setList(readProductBarcodeList);
				adapter.notifyDataSetChanged();
			}

		} else if (id == btnSubmit.getId()) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WmsMenu09Activity.this);
			// 제목셋팅
			alertDialogBuilder.setTitle("데이터 전송");

			//SELECT agencyOrderBarcode, custCode, remark, itemCode,  count(*) AS count FROM TB_AGENCY_ORDER_REDING_INFO_COUPANG WHERE agencyOrderBarcode in ( '2020/07/15-97577-6-1J'  ) GROUP BY agencyOrderBarcode, remark, itemCode,custCode;
			// AlertDialog 셋팅
			alertDialogBuilder
					.setMessage("전송하시겠습니까?")
					.setCancelable(false)
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									String tmpBarcode = "";
									for(int i = 0; i < agencyOrderBarcodeList.size(); i ++) {
										String barcode = agencyOrderBarcodeList.get(i);
										if(barcode.length() > 15) {
											String formatter = "";
											if (tmpBarcode.length() == 0) {
												formatter = "'%s' ";
											} else {
												formatter = ", '%s' ";
											}

											tmpBarcode += String.format(formatter, barcode);
										}

									}

									//agencyOrderBarcode, remark, itemCode,custCode

									String selectQuery = String.format("SELECT %s, %s, %s, %s, %s, count(*) AS count FROM %s where %s = '%s' AND %s in ( %s ) GROUP BY %s, %s, %s, %s, %s, %s",
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
											CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ,
											CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
											"reading",
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
											tmpBarcode,
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
											CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ);

									Cursor c = db.rawQuery(selectQuery, null);
									JSONArray jsonArray = new JSONArray();
									int size = 0;
									while(c.moveToNext()) {
										String agencyOrderBarcode = c.getString(0);
										String custCode = c.getString(1);
										String itemCode = c.getString(2);
										String remark = c.getString(3);
										String palletSeq = c.getString(4);
										int count = c.getInt(5);

										LocationInfoResult obj = adapter.getItem(agencyOrderBarcode, remark, itemCode);

										JSONObject jsonObject = new JSONObject();
										try {
											agencyOrderBarcode = agencyOrderBarcode.replaceAll("/", "-");
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE, agencyOrderBarcode);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE, custCode);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK, remark);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE, itemCode);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS, count);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ, palletSeq);

											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BOX_COUNT, Integer.parseInt(obj.getBoxCount()));



											jsonObject.put(CommonValue.WMS_PARAMETER_KEY_AG_CODE, CommonValue.WMS_INCHON_WAREHOUSE);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO, userNo);

											jsonArray.put(jsonObject);
											size ++;
										} catch ( Exception e) {

										}
										Log.d("DB","agencyOrderBarcode:"+agencyOrderBarcode+",custCode:"+custCode+",itemCode:"+itemCode+",remark:"+remark+",count:"+count+"userNo:"+userNo);
									}
									Log.d("JSONArray","jsonArray:"+jsonArray.toString());
									if(size == 0) {
										showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "전송할 데이터가 없습니다.");
									} else {

										String url = String.format("%s/%s/%s",CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_COUPANG);
										showProgress(WmsMenu09Activity.this);
										networkConnecting = true;
										HttpClient.post(url, WmsMenu09Activity.this, jsonArray.toString());

									}
								}
							})
					.setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									// 다이얼로그를 취소한다
									dialog.cancel();
								}
							});

			// 다이얼로그 생성
			AlertDialog alertDialog = alertDialogBuilder.create();

			// 다이얼로그 보여주기
			alertDialog.show();

		} else if (id == btnReset.getId()) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WmsMenu09Activity.this);
			// 제목셋팅
			alertDialogBuilder.setTitle("데이터 삭제");

			//SELECT agencyOrderBarcode, custCode, remark, itemCode,  count(*) AS count FROM TB_AGENCY_ORDER_REDING_INFO_COUPANG WHERE agencyOrderBarcode in ( '2020/07/15-97577-6-1J'  ) GROUP BY agencyOrderBarcode, remark, itemCode,custCode;
			// AlertDialog 셋팅
			alertDialogBuilder
					.setMessage("데이터를 삭제 하시겠습니까?")
					.setCancelable(false)
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									String tmpBarcode = "";
									for(int i = 0; i < agencyOrderBarcodeList.size(); i ++) {
										String barcode = agencyOrderBarcodeList.get(i);
										if(barcode.length() > 15) {
											String formatter = "";
											if (tmpBarcode.length() == 0) {
												formatter = "'%s' ";
											} else {
												formatter = ", '%s' ";
											}

											tmpBarcode += String.format(formatter, barcode);
										}

									}

									//agencyOrderBarcode, remark, itemCode,custCode

									String selectQuery = String.format("SELECT %s, %s, %s, %s, %s, count(*) AS count FROM %s where %s = '%s' AND %s in ( %s ) GROUP BY %s, %s, %s, %s, %s, %s",
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
											CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ,
											CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
											"reading",
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
											tmpBarcode,
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
											CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ);

									Cursor c = db.rawQuery(selectQuery, null);
									JSONArray jsonArray = new JSONArray();
									int size = 0;
									while(c.moveToNext()) {
										String agencyOrderBarcode = c.getString(0);
										String custCode = c.getString(1);
										String itemCode = c.getString(2);
										String remark = c.getString(3);
										String palletSeq = c.getString(4);
										int count = c.getInt(5);

										LocationInfoResult obj = adapter.getItem(agencyOrderBarcode, remark, itemCode);

										JSONObject jsonObject = new JSONObject();
										try {
											agencyOrderBarcode = agencyOrderBarcode.replaceAll("/", "-");
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE, agencyOrderBarcode);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE, custCode);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK, remark);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE, itemCode);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS, count);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ, palletSeq);

											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BOX_COUNT, Integer.parseInt(obj.getBoxCount()));



											jsonObject.put(CommonValue.WMS_PARAMETER_KEY_AG_CODE, CommonValue.WMS_INCHON_WAREHOUSE);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO, userNo);

											jsonArray.put(jsonObject);
											size ++;
										} catch ( Exception e) {

										}
										Log.d("DB","agencyOrderBarcode:"+agencyOrderBarcode+",custCode:"+custCode+",itemCode:"+itemCode+",remark:"+remark+",count:"+count+"userNo:"+userNo);
									}
									Log.d("JSONArray","jsonArray:"+jsonArray.toString());
									if(size == 0) {
										showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "전송할 데이터가 없습니다.");
									} else {

										String url = String.format("%s/%s/%s",CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_COUPANG);
										showProgress(WmsMenu09Activity.this);
										networkConnecting = true;
										HttpClient.post(url, WmsMenu09Activity.this, jsonArray.toString());

									}
								}
							})
					.setNegativeButton("취소",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {
									// 다이얼로그를 취소한다
									dialog.cancel();
								}
							});

			// 다이얼로그 생성
			AlertDialog alertDialog = alertDialogBuilder.create();

			// 다이얼로그 보여주기
			alertDialog.show();

		} else if(id == btnReadingBarcode.getId()) {
			String value = etReadingBarcode.getText().toString();


			Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : " + value);
			boolean orderReportCustCodePattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_CUST_CODE, value);
			boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, value);
			boolean orderReportNoPattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_NO, value);
			if (productBarcodePattern) {
				readProductBarcode(value);
			} else if (orderReportCustCodePattern) {
				if(agencyOrderBarcodeList == null) {
					agencyOrderBarcodeList = new ArrayList<String>();
				}
				if(agencyOrderBarcodeList.contains(value)) {
					showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "이미 처리된 지시입니다.");
				} else {
					getOrderReport(value);
				}

			} else if (orderReportNoPattern) {
				if(agencyOrderBarcodeList == null) {
					agencyOrderBarcodeList = new ArrayList<String>();
				}
				if(agencyOrderBarcodeList.contains(value)) {
					showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "이미 처리된 지시입니다.");
				} else {
					agencyOrderBarcodeList.add(value);
					getCoupangPickingInfo(value);
				}
			} else {
				showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
				long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
				if (Build.VERSION.SDK_INT >= 26) {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
				} else {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
				}
			}


		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		LocationInfoResult result = (LocationInfoResult)adapter.getItem(position);

		String boxCount = result.getBoxCount();
		RinnaiReceivedProductDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiReceivedProductDialog(WmsMenu09Activity.this, "", boxCount, position);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
			@Override
			public void onPositiveClicked(String barcode, String qty) {
				//productAdd(barcode, qty);
			}

			@Override
			public void onPositiveClicked(String barcode, String qty, int position) {


				LocationInfoResult result = (LocationInfoResult)adapter.getItem(position);

//				ArrayList<LocationInfoRequest> requestList = new ArrayList<LocationInfoRequest>();

				if(result.getBoxCount().trim().equals(qty.trim())) {
					showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_box_no_changes_data));
				} else {
//					result.setQty(qty);
					adapter.setBoxCount(position, qty);
					adapter.notifyDataSetChanged();

				}
			}

			@Override
			public void onPositiveClicked(String barcode, String modelName, String qty) {


			}
		});
		rinnaiReceivedProductDialog.show();
	}

	/**
	 * 피킹지시서 리딩시
	 * @param orderReport
	 */
	private void getCoupangPickingInfo(String orderReport) {
		String date = orderReport.substring(0, 10);
//		String custCode = splitData[1];
//		String carItem = splitData[2];
//		String trsNo = splitData[3];

		String no = orderReport.substring(10, orderReport.length());

//		date = date.replace("/", "-");
//
//		String tmpCarItem = carItem;
//		String tmpTrsNo = null;
//
//		if("0".equals(trsNo.substring(0, 1) ) ) {
//			tmpTrsNo = trsNo.substring(1, 2);
//		} else {
//			tmpTrsNo = trsNo.replaceAll("[^0-9]", "");
//		}
//
//		no = tmpCarItem + "-" + tmpTrsNo;
		//car_item = c or d 일 경우 대리점 코드 호출 안함
		// 이외의 경우 대리점 코드 호출 필요
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu09Activity.this);


			url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_COUPANG, CommonValue.HTTP_PICKING, date, no);
			showProgress(WmsMenu09Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	/**
	 * 대리점 거래 번호를 리딩시
	 * @param orderReport
	 */
	private void getOrderReport(String orderReport) {
		String[] splitData = orderReport.split("-");
		String date = splitData[0];
		String custCode = splitData[1];
		String carItem = splitData[2];
		String trsNo = splitData[3];

		String no = null;

		date = date.replace("/", "-");

		String tmpCarItem = carItem;
		String tmpTrsNo = null;

		if("0".equals(trsNo.substring(0, 1) ) ) {
			tmpTrsNo = trsNo.substring(1, 2);
		} else {
			tmpTrsNo = trsNo.replaceAll("[^0-9]", "");
		}

		no = tmpCarItem + "-" + tmpTrsNo;
		//car_item = c or d 일 경우 대리점 코드 호출 안함
		// 이외의 경우 대리점 코드 호출 필요
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu09Activity.this);

			if(carItem.equals("C") || carItem.equals("D")) {

				url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_AGENCY_ORDER, date, no);
			} else {

				url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_AGENCY_ORDER, date, custCode, no);
			}
			showProgress(WmsMenu09Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	/**
	 * 제품 바코드 리딩시
	 * @param productBarcode
	 */
	private void readProductBarcode(String productBarcode) {
		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcode : " + productBarcode );

		if(agencyOrderBarcodeList.size() == 0) {

			return;
		}


		if(adapter == null) {
			if(readProductBarcodeList == null) {
				readProductBarcodeList = new ArrayList<LocationInfoResult>();
			}
			adapter = new WmsMenu09ListAdapter(WmsMenu09Activity.this, readProductBarcodeList);

			lvReadProductBarcode.setAdapter(adapter);
		}


		//제품 일련번호 확인 쿼리(기존 Reading 유무 확인)
		String selectQuery = String.format("SELECT %s,%s,%s,%s,%s,%s  FROM %s WHERE %s = '%s' ;",
				CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
				CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE ,
				CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE  ,
				CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE ,
				CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
				CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
				CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG,
				CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE ,
				productBarcode);

		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		int count = c.getCount();

		String itemCode = String.format("%s%s%s", productBarcode.substring(0, 5) , "000" , productBarcode.substring(5, 6) );

		if(count == 0) {
			String tmpBarcode = "";
			for(int i = 0; i < agencyOrderBarcodeList.size(); i ++) {
				String barcode = agencyOrderBarcodeList.get(i);
				if(barcode.length() > 15) {
					String formatter = "";
					if (tmpBarcode.length() == 0) {
						formatter = "'%s' ";
					} else {
						formatter = ", '%s' ";
					}

					tmpBarcode += String.format(formatter, barcode);
				}

			}

			selectQuery = String.format("SELECT %s AS %s, %s AS %s, %s FROM %s WHERE %s in ( %s ) AND %s = '%s' AND %s > %s ORDER BY %s LIMIT 1",
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
					CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_COUPANG,
					CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
					tmpBarcode,
					CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
					itemCode,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
					CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE);
			c = db.rawQuery(selectQuery, null);
			c.moveToFirst();
			count = c.getCount();


			int instructions = 0;
			int operations = 0;
			String remark = "";

			if(count > 0 ) {

				instructions = c.getInt(0);
				operations = c.getInt(1);
				remark = c.getString(2);
			}


			String custName = null;
			String custCode = null;
			String modelName = null;
			String type = null;


			if (instructions == 0 && operations == 00) {
				showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "해당 지시내역이 아닙니다.");
				long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
				if (Build.VERSION.SDK_INT >= 26) {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
				} else {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
				}
			} else if(instructions > operations) {
				selectQuery = String.format("SELECT %s,%s,%s,%s,%s,%s,%s FROM %s WHERE %s in ( %s ) AND %s = '%s' AND %s != %s;",
						CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME,
						CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
						CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME,
						CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE,
						CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS,
						CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
						CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
						CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_COUPANG,
						CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
						tmpBarcode,
						CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
						itemCode,
						CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS,
						CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS);
				c = db.rawQuery(selectQuery, null);
				c.moveToFirst();
				count = c.getCount();

				if(count > 0 ) {
					custName = c.getString(0);
					custCode = c.getString(1);
					modelName = c.getString(2);
					type = c.getString(3);
					instructions = c.getInt(4);
					operations = c.getInt(5);
					String orderBarcode = c.getString(6);

					String query = "INSERT OR REPLACE INTO %s (%s,%s,%s,%s,%s, %s, %s ) values ('%s','%s','%s','%s','%s', '%s', '%s' );";

					query = String.format(query,
							CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE  ,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE  ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS ,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ,
							orderBarcode,
							custCode,
							itemCode,
							productBarcode,
							"reading",
							remark,
							"0"

					);
					db.execSQL(query);

					query = "INSERT OR REPLACE INTO %s (%s,%s,%s,%s,%s,%s,%s,%s, %s ) values ('%s','%s','%s','%s','%s','%s',%d,%d ,'%s');";

					query = String.format(query,
							CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_COUPANG,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME ,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE  ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
							orderBarcode,
							custName,
							custCode,
							modelName,
							itemCode,
							type,
							instructions,
							operations + 1,
							remark

					);
					totalOperations += 1;
					tvTotalOperations.setText(String.format("%d", totalOperations));
					CommonValue.Act_9_totalOperations = totalOperations;
					db.execSQL(query);


					LocationInfoResult result = new LocationInfoResult();

					result.setBarcode(productBarcode);
					result.setModelName(modelName);
					result.setType(productBarcode.substring(5, 6));
					result.setCellMake(productBarcode.substring(6, 10));
					result.setItemCode(itemCode);
					result.setQty("1");
					result.setBoxCount("1");
					result.setTotal(String.format("%d", instructions));
					result.setCellMake(remark);
					result.setCellDetail(orderBarcode);
					adapter.addItem(result);
					adapter.notifyDataSetChanged();
//					int posotion = adapter.addItem(result);
//					adapter.notifyDataSetChanged();
//					lvReadProductBarcode.setSelection(posotion);

				} else {

				}


			} else {
				//지시수량 == 작업수량
				//작업이 완료된 작업 지시

				showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "작업이 완료된 제품입니다.");
				long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
				if (Build.VERSION.SDK_INT >= 26) {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
				} else {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
				}

			}


		} else {

			String status = c.getString(4);
			final String custCode = c.getString(1);
			final String finalItemCode = c.getString(2);
			final String finalProuctBarcode = productBarcode;
			final String agencyOrderBarcode = c.getString(0);
			final String finalRemark = c.getString(5);


			String tmpBarcode = "";
			for(int i = 0; i < agencyOrderBarcodeList.size(); i ++) {
				String barcode = agencyOrderBarcodeList.get(i);
				if(barcode.length() > 15) {
					String formatter = "";
					if (tmpBarcode.length() == 0) {
						formatter = "'%s' ";
					} else {
						formatter = ", '%s' ";
					}

					tmpBarcode += String.format(formatter, barcode);
				}

			}
			final String finalTmpBarcode = tmpBarcode;
			final String finalAgencyOrderBarcode = agencyOrderBarcode;

			long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
			if (Build.VERSION.SDK_INT >= 26) {
				((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
			} else {
				((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
			}

			if("success".equals(status)) {
				showRinnaiDialog(WmsMenu09Activity.this, getString(R.string.msg_title_noti), "서버 전송이 완료된 제품입니다.");
			} else if ("reading".equals(status)) {
				AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WmsMenu09Activity.this);
				alert_confirm.setMessage("이미 작업이 완료된 제품입니다.\n작업을 취소하시겠습니까?").setCancelable(false).setPositiveButton("확인",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								String query = "DELETE FROM %s WHERE %s in ( %s )  AND %s = '%s';";
								query = String.format(query,
										CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG,
										CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
										finalTmpBarcode,
										CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE,
										finalProuctBarcode
								);
								db.execSQL(query);

								Log.w("DB", query);

								query = "UPDATE %s SET %s = %s -1 WHERE %s = '%s' AND %s = '%s' AND %s = '%s' AND %s = '%s'";

								query = String.format(query,
										CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_COUPANG,
										CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
										CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
										CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
										agencyOrderBarcode,
										CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
										custCode,
										CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
										finalItemCode,
										CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK,
										finalRemark
								);
								db.execSQL(query);
								totalOperations -= 1;
								tvTotalOperations.setText(String.format("%d", totalOperations));
								CommonValue.Act_4_totalOperations = totalOperations;
								Log.w("DB", query);

								LocationInfoResult result = new LocationInfoResult();

								result.setBarcode(finalProuctBarcode);
								result.setType(finalProuctBarcode.substring(5, 6));
								result.setCellMake(finalRemark);
								result.setCellDetail(finalAgencyOrderBarcode);
								result.setItemCode(finalItemCode);
								result.setQty("1");

								adapter.removeItem(result);
								adapter.notifyDataSetChanged();

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
			}

		}

	}

}