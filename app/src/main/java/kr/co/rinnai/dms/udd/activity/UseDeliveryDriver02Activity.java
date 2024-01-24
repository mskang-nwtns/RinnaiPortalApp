package kr.co.rinnai.dms.udd.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;

import kr.co.rinnai.dms.udd.model.DeliveryMenu02Entity;


//대리점 배송기사 화면 02_제품 바코드 조회
public class UseDeliveryDriver02Activity extends BaseActivity   {

	private BroadcastReceiver mReceiver = null;

	private boolean networkConnecting = false;

	private String modelCode;

	private TextView tvModelName, tvCustCode, tvCustName, tvModelBarcode, tvGasType, tvDate, tvPdaNo;


	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delivery_menu_02);

		tvCustCode = findViewById(R.id.tv_delivery_02_activity_cust_code);
		tvCustName = findViewById(R.id.tv_delivery_02_activity_cust_name);
		tvModelName = findViewById(R.id.tv_delivery_02_activity_model_name);
		tvModelBarcode = findViewById(R.id.tv_delivery_02_activity_model_barcode);
		tvGasType = findViewById(R.id.tv_delivery_02_activity_model_gas_type);
		tvDate = findViewById(R.id.tv_delivery_02_activity_date);
		tvPdaNo = findViewById(R.id.tv_delivery_02_activity_pad_no);
		registerReceiver();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mReceiver);
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

			String httpHost = HttpClient.getCurrentSsid(UseDeliveryDriver02Activity.this);

			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_PRODUCT, barcode);


			showProgress(UseDeliveryDriver02Activity.this);

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
				showRinnaiDialog(UseDeliveryDriver02Activity.this, getString(R.string.msg_title_noti), responseData.getResultMessage());
			}
		} else {

			showRinnaiDialog(UseDeliveryDriver02Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
		}
	}

}
