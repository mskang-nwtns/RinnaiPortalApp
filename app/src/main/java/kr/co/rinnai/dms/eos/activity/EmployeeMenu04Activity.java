package kr.co.rinnai.dms.eos.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

import device.sdk.Information;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.udd.model.DeliveryMenu02Entity;


//영업 직원 화면 02_제품 바코드 조회
public class EmployeeMenu04Activity extends BaseActivity implements View.OnClickListener {

	private BroadcastReceiver mReceiver = null;

	private boolean networkConnecting = false;

	private String modelCode;

	private TextView tvModelName, tvCustCode, tvCustName, tvModelBarcode, tvGasType, tvDate, tvPdaNo;

	private Button btnCamera;

	private RelativeLayout rlSearchBtn;

	private EditText etBarcode;


	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_menu_04);


		boolean isTablet = Util.isTabletDevice(EmployeeMenu04Activity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

		tvCustCode = (TextView) findViewById(R.id.tv_employee_04_activity_cust_code);
		tvCustName = (TextView) findViewById(R.id.tv_employee_04_activity_cust_name);
		tvModelName = (TextView) findViewById(R.id.tv_employee_04_activity_model_name);
		tvModelBarcode = (TextView) findViewById(R.id.tv_employee_04_activity_model_barcode);
		tvGasType = (TextView) findViewById(R.id.tv_employee_04_activity_model_gas_type);
		tvDate = (TextView) findViewById(R.id.tv_employee_04_activity_date);
		tvPdaNo = (TextView) findViewById(R.id.tv_employee_04_activity_pad_no);
		btnCamera = (Button) findViewById(R.id.btn_employee_04_activity_camera);

		rlSearchBtn = (RelativeLayout) findViewById(R.id.rl_employee_04_activity_barcode_search);
		etBarcode = (EditText) findViewById(R.id.et_employee_04_activity_barcode_search);

		int scannerType  = -1;
		try {
			scannerType = Information.getInstance().getScannerType();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(scannerType > -1){
			registerReceiver();
			btnCamera.setVisibility(View.INVISIBLE);

		} else {
			btnCamera.setOnClickListener(EmployeeMenu04Activity.this);
		}

		rlSearchBtn.setOnClickListener(EmployeeMenu04Activity.this);

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


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		this.unregisterReceiver(mReceiver);
	}

	/**
	 * Barcode Reader boradcasting 등록
	 */
	private void registerReceiver() {

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


					boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, value);
					if(CommonValue.REGEX_SCAN_READ_FAIL.equals(value)) {

					}  else if (productBarcodePattern) {

						modelCode = value;

						readProductBarcode(modelCode);
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcodePattern : " + value);

					} else {
						//showRinnaiDialog(WmsMenu01Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
					}

				}

			}
		};

		this.registerReceiver(this.mReceiver, theFilter);

	}

	private void readProductBarcode(String barcode) {

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, barcode);

		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(EmployeeMenu04Activity.this);

			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_PRODUCT, barcode);


			showProgress(EmployeeMenu04Activity.this);

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
			if("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);

				Type type = null;

				if("getProductBarcode".equals(responseData.getResultType())) {

//					type = new TypeToken<ArrayList<ProductInfoResult>>(){}.getType();
					type = new TypeToken<DeliveryMenu02Entity>(){}.getType();

					DeliveryMenu02Entity entity = new Gson().fromJson(str,type);
					tvCustCode.setText(String.format("(%s)", entity.getStCode()));
					tvCustName.setText(entity.getCustName());
					tvModelName.setText(entity.getModelName());
					tvModelBarcode.setText(modelCode);
					String gasType = modelCode.substring(5, 6);
					String strType = "LNG";

					if("0".equals(gasType)) {
						strType = "ETC";
					} else if("1".equals(gasType)) {
						strType = "LPG";
					} else if("2".equals(gasType)) {
						strType = "LNG";
					}
					tvGasType.setText(strType);
					String grDate = entity.getGrDate();
					String grYear = grDate.substring(0, 4);
					String grMonth = grDate.substring(4, 6);
					String grDay = grDate.substring(6, 8);
					String grHour = grDate.substring(8, 10);
					String grMin = grDate.substring(10, 12);
					String grSec = grDate.substring(12, 14);

					String date = String.format("%s-%s-%s %s시%s분%s초", grYear, grMonth, grDay, grHour, grMin, grSec );
					tvDate.setText(date);
					tvPdaNo.setText(entity.getPdaNo());

				}

			} else {
				showRinnaiDialog(EmployeeMenu04Activity.this, getString(R.string.msg_title_noti), "조회되지 않는 바코드 입니다.");
			}
		} else {

			showRinnaiDialog(EmployeeMenu04Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
		}
	}

	@Override
	public void onClick(View v) {
//		Intent intent = new Intent(this, EmployeeMenu04ScanActivity.class);

//		startActivityForResult(intent, 0);

		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etBarcode.getWindowToken(), 0);

		int id = v.getId();
		if (id == btnCamera.getId()) {
			onQrcodeScanner();
		} else if (id == rlSearchBtn.getId()) {
			modelCode = etBarcode.getText().toString();
			readProductBarcode(modelCode);
		}

	}

	private void onQrcodeScanner() {

		IntentIntegrator intentIntegrator = new IntentIntegrator(this);
		intentIntegrator.setBeepEnabled(true);//바코드 인식시 소리
		intentIntegrator.setOrientationLocked(false);
		intentIntegrator.initiateScan();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK) {
			IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
			String re = scanResult.getContents();

			boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, re);
			if(CommonValue.REGEX_SCAN_READ_FAIL.equals(re)) {

			}  else if (productBarcodePattern) {
//rin3651!
				modelCode = re;

				etBarcode.setText(re);
				readProductBarcode(modelCode);
				Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcodePattern : " + re);

			} else {
				//showRinnaiDialog(WmsMenu01Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
			}

//			Toast.makeText(this, "Zxing Custom operating :::: " + re, Toast.LENGTH_LONG).show();
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}
