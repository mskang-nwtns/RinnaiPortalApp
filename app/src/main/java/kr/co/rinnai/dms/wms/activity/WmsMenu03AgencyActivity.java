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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ObjectComparator;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.wms.adapter.OrderReportAgencyListAdapter;
import kr.co.rinnai.dms.wms.adapter.OrderReportListAdapter;
import kr.co.rinnai.dms.wms.adapter.WmsMenu03AgencyListAdapter;
import kr.co.rinnai.dms.wms.model.OrderReportResult;
import kr.co.rinnai.dms.wms.model.WmsMenu03AgencyOrderReport;
import kr.co.rinnai.dms.wms.model.WmsMenu03ReadingListEntity;

/**
 * 출고(Picking 지시서) 대리점 정렬 관련 Activity
 */
public class WmsMenu03AgencyActivity extends BaseActivity implements AdapterView.OnItemClickListener{


	private RelativeLayout llAgencyBarcode;


    private BroadcastReceiver mReceiver = null;

    private boolean networkConnecting = false;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String userName;

	private RelativeLayout btnClear;

	private ArrayList<WmsMenu03AgencyOrderReport> orderReportResultList = null;
	private ArrayList<WmsMenu03AgencyOrderReport> orderReportDetailList = null;


	private WmsMenu03AgencyListAdapter adapter = null;

	private ListView lvAgencyOrderReport = null;

	private WmsMenu03AgencyDialog dialog = null;

	private TextView tvPickingNo = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.activity_wms_menu_03_agency);

		btnClear = (RelativeLayout) findViewById(R.id.btn_wms_activity_clear);

		//위치 모델명 형식 수 량

		ScanManager.getInstance().aDecodeSetTriggerMode(ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT);
		registerReceiver();

		lvAgencyOrderReport = (ListView) findViewById(R.id.lv_agency_order_report);



		helper = new MySQLiteOpenHelper(
				WmsMenu03AgencyActivity.this,  // 현재 화면의 제어권자
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
		tvPickingNo = (TextView) findViewById(R.id.tv_wms_activity_03_picking_no);
		btnClear.setOnClickListener(WmsMenu03AgencyActivity.this);
		lvAgencyOrderReport.setOnItemClickListener(WmsMenu03AgencyActivity.this);
		
	}
	

	/**
	 * picking 지시서 로드
	 * @param orderReport
	 */
	private void getOrderReport(String orderReport) {

		if(!networkConnecting) {

			boolean orderReportNoPattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_NO, orderReport);
			boolean orderReportCustCodePattern = Pattern.matches(CommonValue.REGEX_ORDER_REPORT_CUST_CODE, orderReport);


			String url = null;
			if(orderReportNoPattern) {
				String date = orderReport.substring(0, 10);
				String no = orderReport.substring(10, orderReport.length());
				url = String.format("%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_ORDER_REPORT_AGENCY, date, no);
			} else if(orderReportCustCodePattern) {

				String date = orderReport.substring(0, 10).replace("/", "-");
				String custCode = orderReport.substring(11, 16);

				String no = orderReport.substring(17, orderReport.length() - 1);

				url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_ORDER_REPORT_AGENCY, date, no, custCode);
			}
		
			showProgress(WmsMenu03AgencyActivity.this);
		
			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	/**
	 * picking 대리점별 제품 지시서 로드
	 * @param orderReport
	 */
	private void getOrderReportDetail(String orderReport) {
		String date = orderReport.substring(0, 10).replace("/", "-");
		//String no = orderReport.substring(10, orderReport.length());

		String custCode = orderReport.substring(11, 16);

		String no = orderReport.substring(17, orderReport.length()-1);

		//(EX 2019/03/29-G7077-L-17J)

		if(!networkConnecting) {

			String url = String.format("%s/%s/%s/%s/%s/%s/%s",CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_ORDER_REPORT_AGENCY, CommonValue.HTTP_DETAIL, date, no, custCode);

			showProgress(WmsMenu03AgencyActivity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
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
			if("200".equals(responseData.getResultCode())) {
				if(responseData.getData() != null) {
					Object obj = responseData.getData();

					String str = JsonParserManager.objectToJson(Object.class, obj);

					Type listType = null;
					if("getAgencyOrderReport".equals(responseData.getResultType())) {
						listType = new TypeToken<ArrayList<WmsMenu03AgencyOrderReport>>() {}.getType();

						orderReportResultList = new Gson().fromJson(str, listType);

						adapter = new WmsMenu03AgencyListAdapter(WmsMenu03AgencyActivity.this, orderReportResultList);

						lvAgencyOrderReport.setAdapter(adapter);
						adapter.notifyDataSetChanged();

						//lvReadProductBarcode.setAdapter(adapter);

					} else if("getCustOrderReportAgencyDetail".equals(responseData.getResultType())) {

						listType = new TypeToken<ArrayList<WmsMenu03AgencyOrderReport>>() {}.getType();

						orderReportDetailList = new Gson().fromJson(str, listType);
						String custName = orderReportDetailList.get(0).getCustName();

						dialog = new WmsMenu03AgencyDialog(WmsMenu03AgencyActivity.this, orderReportDetailList, custName);
						dialog.show();
					}


				}
			} else {
				showRinnaiDialog(WmsMenu03AgencyActivity.this, getString(R.string.msg_title_noti), responseData.getResultMessage());
			}


		} else {
			showRinnaiDialog(WmsMenu03AgencyActivity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
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
			tvPickingNo.setText("");
			adapter = new WmsMenu03AgencyListAdapter(WmsMenu03AgencyActivity.this, null);
			orderReportDetailList = null;
			orderReportResultList = null;
			lvAgencyOrderReport.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
			if(null != dialog && dialog.isShowing()) {
				dialog.dismiss();
			}
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
	            		try{
							tvPickingNo.setText(value);
							getOrderReport(value);
	            		} catch(Exception e ) {
	            			e.printStackTrace();
	            		}
	            		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "orderReportNoPattern : " + value);
	            	} else if(orderReportCustCodePattern) {
						tvPickingNo.setText(value);
						getOrderReport(value);
	            		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "orderReportCustCodePattern : " + value);
	            	} else if(warehouseLocationPattern) {
						if (!networkConnecting) {

							Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "warehouseLocationPattern : " + value);
						}

						checkWarehouseLocation(value);

	            	} else {
	            		showRinnaiDialog(WmsMenu03AgencyActivity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
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
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		WmsMenu03AgencyOrderReport report = (WmsMenu03AgencyOrderReport) adapter.getItem(position);

		String barcode = report.getAgencyOrderBarcode();

		if(report.getOrderQty() <= report.getJobQty()) {

			showRinnaiDialog(WmsMenu03AgencyActivity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_complet));
		} else {
			getOrderReportDetail(barcode);
		}



	}

	/**
	 * 창고 Location 바코드 리딩(ex: 04-B10-0)
	 * @param location
	 */
	private void checkWarehouseLocation(String location) {
		if (null == dialog) {
			showRinnaiDialog(WmsMenu03AgencyActivity.this, getString(R.string.msg_title_noti), "대리점 미 선택");
		} else {
			dialog.readingBarcode(location);
		}

	}
}