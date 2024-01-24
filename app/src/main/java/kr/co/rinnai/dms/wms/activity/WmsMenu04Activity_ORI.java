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
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.wms.adapter.WmsMenu08ListAdapter;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeReading;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeResult;
import kr.co.rinnai.dms.wms.model.LocationInfoResult;

/**
 * 검수 관련 Activity
 */
public class WmsMenu04Activity_ORI extends BaseActivity {

	private BroadcastReceiver mReceiver = null;
	private boolean networkConnecting = false;

	private ArrayList<AgencyBarcodeResult> agencyBarcodeResultList = null;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;
	private String agencyOrderBarcode = null;

	//총 작업 지시 수량
	private int totalInstructions = 0;


	//현재 읽은 수량
	private int totalOperations = 0;

	//서버에 전송 완료된 읽은 수량
	private int totalSuccessCount = 0;

	//남은 지시 수량
	private int totalRemainingCount = 0;

	private TextView tvTotalInstructions;
	private TextView tvTotalOperations;
	private TextView tvSuccessCount;
	private TextView tvRemainingCount;
	private RelativeLayout btnSubmit;

	private String lastReadBarcodeType = null;
	private List<String> productBarcodeList = new ArrayList<String>();

	private ListView lvReadProductBarcode = null;

	private ArrayList<LocationInfoResult> readProductBarcodeList = null;
	private WmsMenu08ListAdapter adapter = null;

	private TextView tvTitle;
	private TextView tvShipmentCheck;
	private TextView tvShipmentCount;

	private EditText etReadingBarcode;

	private RelativeLayout btnReadingBarcode;

	private Vibrator vibrator;


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
					if (CommonValue.REGEX_SCAN_READ_FAIL.equals(value)) {
						String log = String.format("Last Read Type : %s ", lastReadBarcodeType);
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, log);
						//2019/11/29-92175-1-00
						if ("agencyOrder".equals(lastReadBarcodeType)) {
							String barcode = CommonValue.agencyOrderBarcode = agencyOrderBarcode;
							getAgencyOrderReport(barcode);
						} else if ("product".equals(lastReadBarcodeType)) {
							for (int i = 0; i < productBarcodeList.size(); i++) {
								String barcode = productBarcodeList.get(i);
								Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcodePattern : " + barcode);
								readProductBarcode(barcode);
							}

						}
						lastReadBarcodeType = null;
						productBarcodeList = new ArrayList<String>();

					} else if (orderReportCustCodePattern) {
						try {
							if (null == lastReadBarcodeType) {
								lastReadBarcodeType = "agencyOrder";
								agencyOrderBarcode = value.replace("/", "-");
								CommonValue.agencyOrderBarcode = agencyOrderBarcode;
								getAgencyOrderReport(value);
							} else {

							}


						} catch (Exception e) {
							e.printStackTrace();
						}
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "orderReportCustCodePattern : " + value);
					} else if (productBarcodePattern) {

						readProductBarcode(value);
						/*
						if(null == lastReadBarcodeType || "product".equals(lastReadBarcodeType)) {
							readProductBarcode(value);

							lastReadBarcodeType = "product";
							productBarcodeList.add(value);
							Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcodePattern : " + value + ", count :" + productBarcodeList.size());

						}
						*/
					} else if (orderReportNoPattern) {


					}else {
						showRinnaiDialog(WmsMenu04Activity_ORI.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wms_menu_08);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		tvTotalOperations = findViewById(R.id.tv_total_operations);
		tvTotalInstructions = findViewById(R.id.tv_total_instructions);
		tvSuccessCount = findViewById(R.id.tv_success_count);
		tvRemainingCount = findViewById(R.id.tv_remaining_count);
		btnSubmit = (RelativeLayout) findViewById(R.id.btn_wms_activity_submit);
		tvTitle =(TextView)findViewById(R.id.tv_wms_activity_08_title);

//		tvShipmentCheck =(TextView)findViewById(R.id.tv_title_shipment_check);
//		tvShipmentCount =(TextView)findViewById(R.id.tv_title_shipment_count);

		lvReadProductBarcode = findViewById(R.id.lv_read_product_barcode);

		etReadingBarcode = (EditText) findViewById(R.id.et_reading_instructions_barcode);
		btnReadingBarcode = (RelativeLayout) findViewById(R.id.btn_reading_instructions_barcode);
		btnReadingBarcode.setOnClickListener(WmsMenu04Activity_ORI.this);
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
		//위치 모델명 형식 수 량
		registerReceiver();
		btnSubmit.setOnClickListener(WmsMenu04Activity_ORI.this);
		helper = new MySQLiteOpenHelper(
				WmsMenu04Activity_ORI.this,  // 현재 화면의 제어권자
				CommonValue.SQLITE_DB_FILE_NAME,// db 이름
				null,  // 커서팩토리-null : 표준커서가 사용됨
				CommonValue.SQLITE_DB_VERSION);       // 버전

		try {

			db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
			//db = helper.getReadableDatabase(); // 읽기 전용 DB select문
		} catch (SQLiteException e) {

		}

		tvTitle.setText("검수");
//		tvShipmentCheck.setVisibility(View.INVISIBLE);
//		tvShipmentCount.setVisibility(View.INVISIBLE);


		if(CommonValue.Act_4_totalInstructions > 0) {
			totalInstructions = CommonValue.Act_4_totalInstructions;
		}

		if(CommonValue.Act_4_totalOperations > 0) {
			totalOperations = CommonValue.Act_4_totalOperations;
		}

		if(CommonValue.Act_4_totalSuccessCount > 0) {
			totalSuccessCount = CommonValue.Act_4_totalSuccessCount;
		}

		if(CommonValue.Act_4_totalRemainingCount > 0) {
			totalRemainingCount = CommonValue.Act_4_totalRemainingCount;
		}

		if(CommonValue.agencyOrderBarcode != null) {
			agencyOrderBarcode = CommonValue.agencyOrderBarcode;
		}

		tvTotalOperations.setText(String.format("%d", totalOperations));
		tvTotalInstructions.setText(String.format("%d", totalInstructions));
		tvSuccessCount.setText(String.format("%d", totalSuccessCount));
		tvRemainingCount.setText(String.format("%d", totalRemainingCount));

	}

	/**
	 * 대리점 출하지시서 Reading
	 * @param orderReport
	 */
	private void getAgencyOrderReport(String orderReport) {

		//2019-06-14-99109-1-01

		String log = String.format("getOrderReport Type : %s, networkConnecting : %s  ", orderReport, networkConnecting);
		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, log);


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

			String httpHost = HttpClient.getCurrentSsid(WmsMenu04Activity_ORI.this);

			if(carItem.equals("C") || carItem.equals("D")) {

				url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_AGENCY_ORDER, date, no);
			} else {

				url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_AGENCY_ORDER, date, custCode, no);
			}
			showProgress(WmsMenu04Activity_ORI.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	private void getOrderReport() {}


	/**
	 * 제품 바코드 리딩시
	 * @param productBarcode
	 */
	private void readProductBarcode(String productBarcode) {

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcode : " + productBarcode );

		if(agencyOrderBarcode == null) {

			return;
		}


		if(adapter == null) {
			if(readProductBarcodeList == null) {
				readProductBarcodeList = new ArrayList<LocationInfoResult>();
			}
			adapter = new WmsMenu08ListAdapter(WmsMenu04Activity_ORI.this, readProductBarcodeList);

			lvReadProductBarcode.setAdapter(adapter);
		}
		//제품 일련번호 확인 쿼리(기존 Reading 유무 확인)
		String selectQuery = String.format("SELECT %s,%s,%s,%s,%s  FROM %s WHERE %s = '%s' ;",
				CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
				CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE ,
				CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE  ,
				CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE ,
				CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
				CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK,
				CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE ,
				productBarcode);

		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		int count = c.getCount();

		String itemCode = String.format("%s%s%s", productBarcode.substring(0, 5) , "000" , productBarcode.substring(5, 6) );

		if(count == 0) {

			selectQuery = String.format("SELECT SUM(%s) AS %s, SUM(%s) AS %s FROM %s WHERE %s = '%s' AND %s = '%s' GROUP BY %s, %s",
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
					CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK,
					CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
					agencyOrderBarcode,
					CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
					itemCode,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
					CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
					CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
					CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE);
			c = db.rawQuery(selectQuery, null);
			c.moveToFirst();
			count = c.getCount();


			int instructions = 0;
			int operations = 0;

			if(count > 0 ) {

				instructions = c.getInt(0);
				operations = c.getInt(1);
			}


			String custName = null;
			String custCode = null;
			String modelName = null;
			String type = null;


			if (instructions == 0 && operations == 00) {
				showRinnaiDialog(WmsMenu04Activity_ORI.this, getString(R.string.msg_title_noti), "해당 지시내역이 아닙니다.");
				long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
				if (Build.VERSION.SDK_INT >= 26) {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
				} else {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
				}
			} else if(instructions > operations) {
				selectQuery = String.format("SELECT %s,%s,%s,%s,%s,%s FROM %s WHERE %s = '%s' AND %s = '%s' AND %s != %s;",
						CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME,
						CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
						CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME,
						CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE,
						CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS,
						CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
						CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK,
						CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
						agencyOrderBarcode,
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

					String query = "INSERT OR REPLACE INTO %s (%s,%s,%s,%s,%s ) values ('%s','%s','%s','%s','%s'  );";

					query = String.format(query,
							CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE  ,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE  ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS ,
							agencyOrderBarcode,
							custCode,
							itemCode,
							productBarcode,
							"reading"

					);
					db.execSQL(query);

					query = "INSERT OR REPLACE INTO %s (%s,%s,%s,%s,%s,%s,%s,%s ) values ('%s','%s','%s','%s','%s','%s',%d,%d );";

					query = String.format(query,
							CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME ,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE  ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
							agencyOrderBarcode,
							custName,
							custCode,
							modelName,
							itemCode,
							type,
							instructions,
							operations + 1
					);
					totalOperations += 1;
					tvTotalOperations.setText(String.format("%d", totalOperations));
					CommonValue.Act_4_totalOperations = totalOperations;
					db.execSQL(query);


					LocationInfoResult result = new LocationInfoResult();

					result.setBarcode(productBarcode);
					result.setModelName(modelName);
					result.setType(productBarcode.substring(5, 6));
					result.setCellMake(productBarcode.substring(6, 10));
					result.setItemCode(itemCode);
					result.setQty("1");
					int posotion = adapter.addItem(result);
					adapter.notifyDataSetChanged();
                    lvReadProductBarcode.setSelection(posotion);

				} else {

				}


			} else {
				//지시수량 == 작업수량
				//작업이 완료된 작업 지시

				showRinnaiDialog(WmsMenu04Activity_ORI.this, getString(R.string.msg_title_noti), "해당 지시내역이 아닙니다.");

				long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
				if (Build.VERSION.SDK_INT >= 26) {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
				} else {
					((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
				}

			}


		} else {

			final String custCode = c.getString(1);
			final String finalItemCode = c.getString(2);
			final String finalProuctBarcode = productBarcode;

			long[] pattern = {2000, 50, 2000, 50}; // 1초 진동, 0.05초 대기, 1초 진동, 0.05초 대기
			if (Build.VERSION.SDK_INT >= 26) {
				((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(2000,150));
			} else {
				((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(2000);
			}
			AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WmsMenu04Activity_ORI.this);
			alert_confirm.setMessage("이미 작업이 완료된 제품입니다.\n작업을 취소하시겠습니까?").setCancelable(false).setPositiveButton("확인",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String query = "DELETE FROM %s WHERE %s = '%s' AND %s = '%s';";
							query = String.format(query,
									CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK,
									CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
									agencyOrderBarcode,
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE,
									finalProuctBarcode
							);
							db.execSQL(query);

							Log.w("DB", query);
							query = "UPDATE %s SET %s = %s -1 WHERE %s = '%s' AND %s = '%s' AND %s = '%s'";

							query = String.format(query,
									CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK,
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
									CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
									agencyOrderBarcode,
									CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
									custCode,
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
									finalItemCode
							);
							db.execSQL(query);
							totalOperations -= 1;
							tvTotalOperations.setText(String.format("%d", totalOperations));
							CommonValue.Act_4_totalOperations = totalOperations;
							Log.w("DB", query);

							int position = adapter.getReadingItem(finalProuctBarcode);

							if(position > -1) {
								LocationInfoResult result = new LocationInfoResult();

								result.setBarcode(finalProuctBarcode);
								result.setType(finalProuctBarcode.substring(5, 6));
								result.setCellMake(finalProuctBarcode.substring(6, 10));
								result.setItemCode(finalItemCode);
								result.setQty("1");

								adapter.removeItem(result);
								adapter.notifyDataSetChanged();
                                lvReadProductBarcode.setSelection(position);
							}


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
		Log.w("DB", selectQuery);

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
				if("getAgencyOrderInfo".equals(responseData.getResultType())|| "getAgencyOrderInfoByPicking".equals(responseData.getResultType())) {

					totalInstructions = 0;
					totalOperations = 0;
					totalSuccessCount = 0;
					totalRemainingCount = 0;
					super.setTotalInstructions(totalInstructions);
					super.setTotalOperations(totalOperations);
					super.setTotalSuccessCount(totalSuccessCount);
					super.setTotalRemainingCount(totalRemainingCount);
					listType = new TypeToken<ArrayList<AgencyBarcodeResult>>(){}.getType();
					agencyBarcodeResultList = new Gson().fromJson(str, listType);

					if(adapter == null) {
						if(readProductBarcodeList == null) {
							readProductBarcodeList = new ArrayList<LocationInfoResult>();
						}
						adapter = new WmsMenu08ListAdapter(WmsMenu04Activity_ORI.this, readProductBarcodeList);

						lvReadProductBarcode.setAdapter(adapter);
					}

					for(int i =0 ; i < agencyBarcodeResultList.size(); i ++) {
						AgencyBarcodeResult agencyBarcode = agencyBarcodeResultList.get(i);

						String query = "INSERT OR REPLACE INTO %s (%s,%s,%s,%s,%s,%s,%s,%s ) values ('%s', '%s', '%s', '%s', '%s', '%s', %d, %d );";

						query = String.format(query,
								CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK,
								CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
								CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME ,
								CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE  ,
								CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME ,
								CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
								CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE,
								CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
								CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
								agencyOrderBarcode,
								agencyBarcode.getCustName(),
								agencyBarcode.getCustCode(),
								agencyBarcode.getModelName(),
								agencyBarcode.getItemCode(),
								agencyBarcode.getType(),
								Integer.parseInt(agencyBarcode.getInstructions()),
								Integer.parseInt( agencyBarcode.getOperations())
//                            i
						);
						db.execSQL(query);

						totalInstructions += Integer.parseInt(agencyBarcode.getInstructions());
						totalOperations += Integer.parseInt( agencyBarcode.getOperations());

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

						adapter.addItem(infoResult);


						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, agencyBarcode.toString());
					}
					adapter.notifyDataSetChanged();

					String selectQuery = String.format("SELECT %s,%s,%s,%s,%s,%s,%s,%s FROM %s ;",
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME ,
							CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE  ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME ,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
							CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS ,
							CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS,
							CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK);
					Cursor c = db.rawQuery(selectQuery, null);
					while(c.moveToNext()) {
						String barcode = c.getString(0);
						String custName = c.getString(1);
						String custCode = c.getString(2);
						String modelName = c.getString(3);
						String itemCode = c.getString(4);
						String type = c.getString(5);
						int instructions = c.getInt(6);
						int operations = c.getInt(7);

						Log.d("DB","barcode:"+barcode+",custName:"+custName+",custCode:"+custCode+",modelName:"+modelName+",itemCode:"+itemCode+",type:"+type+",instructions:"+instructions+",operations:"+operations);
					}


					if(agencyBarcodeResultList.size() == 0) {
						showRinnaiDialog(WmsMenu04Activity_ORI.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
					}

					db.delete(CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK, "", null);
					totalRemainingCount = totalInstructions - totalSuccessCount;

					tvTotalInstructions.setText(String.format("%d", totalInstructions));
					tvSuccessCount.setText(String.format("%d", totalSuccessCount));
					tvTotalOperations.setText(String.format("%d", totalOperations));
					tvRemainingCount.setText(String.format("%d", totalRemainingCount));

					CommonValue.Act_4_totalInstructions = totalInstructions;
					CommonValue.Act_4_totalOperations = totalOperations;
					CommonValue.Act_4_totalSuccessCount = totalSuccessCount;
					CommonValue.Act_4_totalRemainingCount = totalRemainingCount;




				} if("setAgencyBarcodeWork".equals(responseData.getResultType())) {

					listType = new TypeToken<ArrayList<AgencyBarcodeReading>>(){}.getType();
					ArrayList<AgencyBarcodeReading> list = new Gson().fromJson(str, listType);

					Log.d("DB","list size:" + list.size());

					int success = 0;
					int fail = 0;
					int count= list.size();
					for(int i = 0; i < count; i ++) {
						AgencyBarcodeReading reading = list.get(i);
						String query = "UPDATE %s SET %s  = '%s'  WHERE %s = '%s' AND %s = '%s'";
						if(reading.isStatus()) {
							query = String.format(query,
									CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK,
									CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
									"success",
									CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
									reading.getAgencyOrderBarcode(),
									CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE,
									reading.getBarcode()
							);
							Log.d("DB","query:"+query);
							db.execSQL(query);
							success ++;
							totalSuccessCount ++;
						} else {
							fail ++;
						}
					}
					showRinnaiDialog(WmsMenu04Activity_ORI.this, getString(R.string.msg_title_noti), String.format(" 총 %d건 중 %d건 전송에 성공하였습니다.", count, success ));

					totalRemainingCount = totalInstructions - totalSuccessCount;
					tvSuccessCount.setText(String.format("%d", totalSuccessCount));
					tvRemainingCount.setText(String.format("%d", totalRemainingCount));

					CommonValue.Act_4_totalInstructions = totalInstructions;
					CommonValue.Act_4_totalOperations = totalOperations;
					CommonValue.Act_4_totalSuccessCount = totalSuccessCount;
					CommonValue.Act_4_totalRemainingCount = totalRemainingCount;

				}


			}
		}

	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == btnSubmit.getId()) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WmsMenu04Activity_ORI.this);
			// 제목셋팅
			alertDialogBuilder.setTitle("데이터 전송");

			// AlertDialog 셋팅
			alertDialogBuilder
					.setMessage("전송하시겠습니까?")
					.setCancelable(false)
					.setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {

									String selectQuery = String.format("SELECT %s,%s,%s,%s,%s FROM %s where %s = '%s';",
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE,
											CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE,
											CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
											CommonValue.SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK,
											CommonValue.SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS,
											"reading");
									Cursor c = db.rawQuery(selectQuery, null);
									JSONArray jsonArray = new JSONArray();
									int count = 0;
									while(c.moveToNext()) {
										String agencyOrderBarcode = c.getString(0);
										String custCode = c.getString(1);
										String itemCode = c.getString(2);
										String barcode = c.getString(3);
										String status = c.getString(4);

										JSONObject jsonObject = new JSONObject();
										try {
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE, agencyOrderBarcode);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE, custCode);
											jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE, barcode);
											jsonObject.put(CommonValue.WMS_PARAMETER_KEY_AG_CODE, CommonValue.WMS_INCHON_WAREHOUSE);

//											jsonObject.put(CommonValue.WMS_PARAMETER_KEY_PDA_NO, "9409");

											jsonObject.put(CommonValue.WMS_PARAMETER_KEY_PDA_NO, "9254");
											jsonArray.put(jsonObject);
											count ++;
										} catch ( Exception e) {

										}
										Log.d("DB","agencyOrderBarcode:"+agencyOrderBarcode+",custCode:"+custCode+",itemCode:"+itemCode+",barcode:"+barcode+",status:"+status);
									}
									Log.d("JSONArray","jsonArray:"+jsonArray.toString());
//									if(count == 0) {
//										showRinnaiDialog(WmsMenu08Activity.this, getString(R.string.msg_title_noti), "전송할 데이터가 없습니다.");
//									} else {

									String url = String.format("%s/%s/%s",CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_AGENCY_ORDER_CHK);
									showProgress(WmsMenu04Activity_ORI.this);
									networkConnecting = true;
									HttpClient.post(url, WmsMenu04Activity_ORI.this, jsonArray.toString());

//									}
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

		} else if (id == btnReadingBarcode.getId()) {
			String value = etReadingBarcode.getText().toString();
//			String value = new String(scanvalue);
			Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : " + value);

			boolean orderReportCustCodePattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_CUST_CODE, value);
			boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, value);
			if(CommonValue.REGEX_SCAN_READ_FAIL.equals(value)) {
				String log = String.format("Last Read Type : %s ", lastReadBarcodeType);
				Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, log);
				//2019/11/29-92175-1-00
				if("agencyOrder".equals(lastReadBarcodeType)) {
					String barcode  = CommonValue.agencyOrderBarcode = agencyOrderBarcode;
					getAgencyOrderReport(barcode);
				} else if ("product".equals(lastReadBarcodeType)) {
					for(int i = 0; i < productBarcodeList.size(); i ++ ) {
						String barcode = productBarcodeList.get(i);
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcodePattern : " + barcode);
						readProductBarcode(barcode);
					}

				}
				lastReadBarcodeType = null;
				productBarcodeList = new ArrayList<String>();

			} else if(orderReportCustCodePattern) {
				try{
					if(null == lastReadBarcodeType) {
						lastReadBarcodeType = "agencyOrder";
						agencyOrderBarcode = value.replace("/","-");
						CommonValue.agencyOrderBarcode = agencyOrderBarcode;
						getAgencyOrderReport(value);
					} else {

					}


				} catch(Exception e ) {
					e.printStackTrace();
				}
				Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "orderReportCustCodePattern : " + value);
			} else if (productBarcodePattern) {

				readProductBarcode(value);
						/*
						if(null == lastReadBarcodeType || "product".equals(lastReadBarcodeType)) {
							readProductBarcode(value);

							lastReadBarcodeType = "product";
							productBarcodeList.add(value);
							Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcodePattern : " + value + ", count :" + productBarcodeList.size());

						}
						*/
			} else {
				showRinnaiDialog(WmsMenu04Activity_ORI.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
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
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mReceiver);
	}
}