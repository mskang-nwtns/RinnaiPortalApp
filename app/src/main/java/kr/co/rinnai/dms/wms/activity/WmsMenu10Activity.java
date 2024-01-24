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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;
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
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.eos.model.SalesModelInfoResult;
import kr.co.rinnai.dms.wms.adapter.WmsMenu09ListAdapter;
import kr.co.rinnai.dms.wms.adapter.WmsMenu10ListAdapter;
import kr.co.rinnai.dms.wms.adapter.WmsMenu10SpinnerAdapter;
import kr.co.rinnai.dms.wms.model.LocationInfoResult;
import kr.co.rinnai.dms.wms.model.WmsMenu10SpinnerListEntity;

/**
 * 2,3 공장 이동처리 Activity
 */
public class WmsMenu10Activity extends BaseActivity implements OnItemClickListener,  AdapterView.OnItemSelectedListener  {
	private BroadcastReceiver mReceiver = null;
	private boolean networkConnecting = false;

	private SQLiteDatabase db;

	private MySQLiteOpenHelper helper;

	private String userNo;
	private String userName;

	private RelativeLayout btnSubmit, btnInit;
	private RelativeLayout btnClear, btnClose;

	private Spinner spFrom, spTo;

	private WmsMenu10SpinnerAdapter adapter;

	private TextView tvBarcode;
	private EditText etModelName, etGasType, etCount;

	private ListView lvBarcode;
	private WmsMenu10ListAdapter listAdapter;

	private ArrayList<LocationInfoResult> listItem;

	private boolean isFirstFrom = true;
	private boolean isFirstTo = true;

	private String strFrom = null;
	private String strTo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.activity_wms_menu_10);



		//위치 모델명 형식 수 량
		registerReceiver();


		helper = new MySQLiteOpenHelper(
				WmsMenu10Activity.this,  // 현재 화면의 제어권자
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
			userName = c.getString(2);

		}

		tvBarcode = (TextView) findViewById(R.id.tv_wms_activity_10_reading_barcode);
		etModelName = (EditText) findViewById(R.id.tv_wms_activity_10_model_name);
		etGasType = (EditText) findViewById(R.id.tv_wms_activity_10_model_gas_type);
		etCount = (EditText) findViewById(R.id.et_wms_activity_10_count);

		btnSubmit =(RelativeLayout) findViewById(R.id.btn_wms_activity_submit);
		btnInit = (RelativeLayout) findViewById(R.id.btn_wms_activity_init);

		btnClear = (RelativeLayout) findViewById(R.id.btn_wms_activity_clear);
		btnClose = (RelativeLayout) findViewById(R.id.btn_wms_activity_close);

		spFrom = (Spinner) findViewById(R.id.sp_wms_activity_10_from);
		spTo = (Spinner) findViewById(R.id.sp_wms_activity_10_to);

		lvBarcode = (ListView) findViewById(R.id.lv_read_product_barcode);

		btnSubmit.setOnClickListener(WmsMenu10Activity.this);
		btnInit.setOnClickListener(WmsMenu10Activity.this);


		btnClear.setOnClickListener(WmsMenu10Activity.this);
		btnClose.setOnClickListener(WmsMenu10Activity.this);

		spFrom.setOnItemSelectedListener(WmsMenu10Activity.this);
		spTo.setOnItemSelectedListener(WmsMenu10Activity.this);

		selectQuery = String.format("SELECT %s, %s, %s  FROM %s WHERE %s = %s ;",
				CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_USER_NO,
				CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_OUT_DATA,
				CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_IN_DATA,
				CommonValue.SQLITE_DB_TABLE_NAME_PRODUCT_MOVE_HANDLING,
				CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_USER_NO,
				userNo
				);

		c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		count = c.getCount();

		if(count > 0 ) {
			strFrom = c.getString(1);
			strTo = c.getString(2);

		}


		getWarehouseInfo();




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

					boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, value);

					if (productBarcodePattern) {

						tvBarcode.setText(value);
						etCount.setText("");
						getModelInfo(value);
					} else {
						showRinnaiDialog(WmsMenu10Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
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

				if("getWarehouseInfo".equals(responseData.getResultType())) {
					listType = new TypeToken<ArrayList<WmsMenu10SpinnerListEntity>>(){}.getType();

					List<WmsMenu10SpinnerListEntity> list = new Gson().fromJson(str, listType);

					adapter = new WmsMenu10SpinnerAdapter(WmsMenu10Activity.this, list);
					spFrom.setAdapter(adapter);
					spTo.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					if(strFrom != null && strTo != null) {
						for (int i = 0; i < list.size(); i++) {
							WmsMenu10SpinnerListEntity spinnerEntity = list.get(i);
							if(strFrom.equals(spinnerEntity.getWarehouse())) {
								isFirstFrom = true;

								spFrom.setSelection(i);
							}
							if(strTo.equals(spinnerEntity.getWarehouse())) {
								isFirstTo = true;
								spTo .setSelection(i);
							}
						}
					}

				} else if ("getSalesModelInfo".equals(responseData.getResultType())) {
					listType = new TypeToken<ArrayList<SalesModelInfoResult>>(){}.getType();
					List<SalesModelInfoResult> list = new Gson().fromJson(str, listType);

					if(list.size()== 1) {
						SalesModelInfoResult modelInfo = list.get(0);
						etModelName.setText(modelInfo.getModelName());
						String barcode = tvBarcode.getText().toString();
						String gasType = barcode.substring(5, 6);
						String gas="기타";
						etGasType.setBackground(getResources().getDrawable(R.drawable.layout_out_border, null));
						if("0".equals(gasType)) {
							gas = "기타";
						} else if("1".equals(gasType)) {
							gas = "LPG";

							//흰색
						} else if("2".equals(gasType)) {
							gas = "LNG";
							etGasType.setBackground(getResources().getDrawable(R.drawable.layout_inside_yellow_out_border, null));
							//노란색
						}

						etGasType.setText(gas);

					}
				} else if("setProductMoveHandling".equals(responseData.getResultType())) {
					showRinnaiDialog(WmsMenu10Activity.this, getString(R.string.msg_title_noti), (String)responseData.getData());
					listAdapter.getList().clear();
					listAdapter.notifyDataSetChanged();
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
			listAdapter.getList().clear();
			listAdapter.notifyDataSetChanged();
		} else if (id == btnClose.getId()) {
			this.finish();
		} else if (id == btnInit.getId()) {

			if(listItem == null) {
				listItem = new ArrayList<LocationInfoResult>();
			}

			if(listAdapter == null) {
				listAdapter = new WmsMenu10ListAdapter(WmsMenu10Activity.this, listItem);
				lvBarcode.setAdapter(listAdapter);
			}
			String barcode = tvBarcode.getText().toString();
			String modelName = etModelName.getText().toString();
			//String gasType = etGasType.getText().toString();
			String count = etCount.getText().toString();
			LocationInfoResult result = new LocationInfoResult();

			if(barcode.length() < 6) {
				showRinnaiDialog(WmsMenu10Activity.this, getString(R.string.msg_title_noti), "제품 바코드를 입력해 주세요.");
			} else if (count.length() < 1) {
				showRinnaiDialog(WmsMenu10Activity.this, getString(R.string.msg_title_noti), "제품 수량을 입력해 주세요.");
			} else {
				result.setQty(count);
				result.setBarcode(barcode);
				result.setModelName(modelName);
				result.setType(barcode.substring(5,6));

				listAdapter.getList().add(result);
				listAdapter.notifyDataSetChanged();

				viewClear();

			}



		} else if (id == btnSubmit.getId()) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WmsMenu10Activity.this);
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

									List<LocationInfoResult> list = listAdapter.getList();
									int size = list.size();

									if(size == 0) {
										showRinnaiDialog(WmsMenu10Activity.this, getString(R.string.msg_title_noti), "전송할 데이터가 없습니다.");
									} else {
										JSONArray jsonArray = new JSONArray();
										WmsMenu10SpinnerListEntity out = (WmsMenu10SpinnerListEntity)spFrom.getSelectedItem();
										WmsMenu10SpinnerListEntity in = (WmsMenu10SpinnerListEntity)spTo.getSelectedItem();
										for(int i = 0; i < list.size(); i ++){
											JSONObject jsonObject = new JSONObject();
											LocationInfoResult result = list.get(i);
											try {
												String barcode = String.format("%s000%s", result.getBarcode().substring(0, 5), result.getBarcode().substring(5, 6) );
												jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE, barcode);
												jsonObject.put(CommonValue.SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_COUNT, result.getQty());
												jsonObject.put(CommonValue.WMS_PARAMETER_KEY_PDA_NO, userNo);
												jsonObject.put(CommonValue.WMS_PARAMETER_KEY_ITEM_SEQ, i + 1);

												jsonObject.put(CommonValue.WMS_PARAMETER_KEY_OUT_WAREHOUSE,out.getWarehouse());
												jsonObject.put(CommonValue.WMS_PARAMETER_KEY_IN_WAREHOUSE,in.getWarehouse());

												jsonArray.put(jsonObject);

											} catch( Exception e) {
												e.printStackTrace();
											}

										}


										String url = String.format("%s/%s/%s",CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_MOVEMENT_INSTRUCTIONS);
										showProgress(WmsMenu10Activity.this);
										networkConnecting = true;
										HttpClient.post(url, WmsMenu10Activity.this, jsonArray.toString());

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

		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	/**
	 * 창고 목록 조회
	 *
	 */
	private void getWarehouseInfo() {

		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu10Activity.this);
			url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.WMS_PARAMETER_KEY_WAREHOUSE, CommonValue.HTTP_INFO);

			showProgress(WmsMenu10Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	private void getModelInfo(String barcode) {

		String modelCode = String.format("%s000", barcode.substring(0, 5));
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu10Activity.this);
			url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, modelCode);

			showProgress(WmsMenu10Activity.this);

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



	}

	private void viewClear() {
		tvBarcode.setText("");
		etModelName.setText("");
		etGasType.setText("");
		etCount.setText("");

		etGasType.setBackground(getResources().getDrawable(R.drawable.layout_out_border, null));
		InputMethodManager imm;
		imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etCount.getWindowToken(), 0);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(isFirstFrom) {
			isFirstFrom = false;
		} else if (isFirstTo) {
			isFirstTo = false;
		} else {
			saveSpinner();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	private void saveSpinner() {
		WmsMenu10SpinnerListEntity fromEntity = (WmsMenu10SpinnerListEntity) spFrom.getSelectedItem();
		WmsMenu10SpinnerListEntity toEntity = (WmsMenu10SpinnerListEntity) spTo.getSelectedItem();
		String query = "INSERT OR REPLACE INTO %s (%s,%s,%s) values ('%s','%s','%s' );";

		query = String.format(query,

				CommonValue.SQLITE_DB_TABLE_NAME_PRODUCT_MOVE_HANDLING,

				CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_USER_NO,
				CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_OUT_DATA,
				CommonValue.SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_IN_DATA,

				userNo, fromEntity.getWarehouse(), toEntity.getWarehouse()


		);

		db.execSQL(query);
	}
}