package kr.co.rinnai.dms.wms.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import device.common.ScanConst;
import device.sdk.ScanManager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.custom.RinnaiSearchListDialog;
import kr.co.rinnai.dms.eos.model.SalesModelInfoResult;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.wms.adapter.WmsMenu05ListAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.wms.model.LocationInfo;
import kr.co.rinnai.dms.wms.model.ProductInfoResult;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;

/**
 * 위치조회  관련 Activity
 */
public class WmsMenu05Activity extends BaseActivity  implements AdapterView.OnItemLongClickListener, View.OnKeyListener, CompoundButton.OnCheckedChangeListener {

	private String barcodeType = null;
	private String barcodeValue = null;

	private WmsMenu05ListAdapter adapter;

	private ArrayList<ProductInfoResult> locationSearchList = null;
	
    private BroadcastReceiver mReceiver = null;
    
    private boolean networkConnecting = false;

	private ListView lvProductLocation;

	private EditText tvModelName;

	private TextView tvModelGasType;

	private TextView tvWmsProductCount, tvErpProductCount;

	private LocationInfo locationInfo;

	private CheckBox cbEtc, cbLpg, cbLng;

	private LinearLayout llEtc, llLpg, llLng;

	private String modelCode;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.activity_wms_menu_05);

		lvProductLocation = (ListView) findViewById(R.id.lv_product_location);
//		lvProductLocation = (ListView) findViewById(R.id.lv_location_product);
		//위치 모델명 형식 수 량
		ScanManager.getInstance().aDecodeSetTriggerMode(ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT);

		registerReceiver();


		lvProductLocation.setOnItemLongClickListener(WmsMenu05Activity.this);
		tvModelName = (EditText) findViewById(R.id.tv_wms_activity_05_model_name);

		tvModelGasType = (TextView) findViewById(R.id.tv_wms_activity_05_gas);

		tvWmsProductCount = (TextView) findViewById(R.id.tv_wms_product_count);

		tvErpProductCount = (TextView) findViewById(R.id.tv_erp_product_count);

		tvModelName.setOnKeyListener(WmsMenu05Activity.this);
		adapter = new WmsMenu05ListAdapter();

		cbEtc = (CheckBox) findViewById(R.id.cb_wms_activity_05_gas_type_etc);
		cbLpg = (CheckBox) findViewById(R.id.cb_wms_activity_05_gas_type_lpg);
		cbLng = (CheckBox) findViewById(R.id.cb_wms_activity_05_gas_type_lng);

		llEtc = (LinearLayout) findViewById(R.id.wms_activity_05_gas_type_etc);
		llLpg = (LinearLayout) findViewById(R.id.wms_activity_05_gas_type_lpg);
		llLng = (LinearLayout) findViewById(R.id.wms_activity_05_gas_type_lng);

		llEtc.setOnClickListener(WmsMenu05Activity.this);
		llLpg.setOnClickListener(WmsMenu05Activity.this);
		llLng.setOnClickListener(WmsMenu05Activity.this);

		cbEtc.setOnCheckedChangeListener(WmsMenu05Activity.this);
		cbLpg.setOnCheckedChangeListener(WmsMenu05Activity.this);
		cbLng.setOnCheckedChangeListener(WmsMenu05Activity.this);

		Intent intent = getIntent();
		String name = intent.getStringExtra(CommonValue.WMS_PARAMETER_KEY_MODEL_CODE);
		String modelName = intent.getStringExtra(CommonValue.WMS_PARAMETER_KEY_MODEL_NAME);

		if(null != modelName) {
			tvModelName.setText(modelName);

		}

		if(null != name) {
			modelCode = name;

			String gasType = modelCode.substring(5, 6);

			if("0".equals(gasType)) {
				if(!cbEtc.isChecked()) {
					cbEtc.setChecked(true);
				}
			} else if("1".equals(gasType)) {
				if(!cbLpg.isChecked()) {
					cbLpg.setChecked(true);
				}
			} else if("2".equals(gasType)) {
				if(!cbLng.isChecked()) {
					cbLng.setChecked(true);
				}
			}

			readProductBarcode(modelCode);
		}




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


					//아리스톤 수입 전기온수기 바코드 관련 사전 처리 사항( 바코드가 16자리로 출력됨 11자리 코드 제거)
					boolean productBarcodeAristonPattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE_ARISTON, value);
					if(productBarcodeAristonPattern) {
						String firstValue = value.substring(0, 10);
						String lastValue = value.substring(11, value.length() );
						value = String.format("%s%s", firstValue, lastValue);
					}


					boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, value);
					if(CommonValue.REGEX_SCAN_READ_FAIL.equals(value)) {

					}  else if (productBarcodePattern) {

						String productBarcode = value.substring(0, 6);
						modelCode = productBarcode;

						String gasType = modelCode.substring(5, 6);

						if("0".equals(gasType)) {
							if(!cbEtc.isChecked()) {
								cbEtc.setChecked(true);
							}
						} else if("1".equals(gasType)) {
							if(!cbLpg.isChecked()) {
								cbLpg.setChecked(true);
							}
						} else if("2".equals(gasType)) {
							if(!cbLng.isChecked()) {
								cbLng.setChecked(true);
							}
						}

						readProductBarcode(modelCode);
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcodePattern : " + value);

					} else {
						showRinnaiDialog(WmsMenu05Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
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


	private void readProductBarcode(String barcode) {

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, barcode);

		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu05Activity.this);
			String model = barcode.substring(0, 5) + "000" + barcode.substring(5, 6);

			url = String.format("%s/%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, CommonValue.HTTP_LOCATION, CommonValue.WMS_INCHON_WAREHOUSE, barcode);

			showProgress(WmsMenu05Activity.this);

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

				if("getProductLocationInfo".equals(responseData.getResultType())) {

//					type = new TypeToken<ArrayList<ProductInfoResult>>(){}.getType();
					type = new TypeToken<LocationInfo>(){}.getType();
					locationInfo = new Gson().fromJson(str,type);

					locationSearchList = locationInfo.getList();
					int erpProductCount = 0 ;
					if(locationSearchList.size() == 0) {
						erpProductCount = locationInfo.getErpCount();
						tvWmsProductCount.setText("0");
						tvErpProductCount.setText(String.format("%,d", erpProductCount));
						lvProductLocation.setVisibility(View.INVISIBLE);

						//showRinnaiDialog(WmsMenu05Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
					} else {
						lvProductLocation.setVisibility(View.VISIBLE);
						String modelName = "";
						String gasType = "";
						int count = 0;
						for(int i = 0; i < locationSearchList.size(); i++) {
							ProductInfoResult product = locationSearchList.get(i);

							if(i == 0) {
								modelName = product.getModelName();
								gasType = product.getType();
							}
							count += Integer.parseInt(product.getQty());
						}

						erpProductCount = locationInfo.getErpCount();
						adapter = new WmsMenu05ListAdapter(WmsMenu05Activity.this, locationSearchList);

						lvProductLocation.setAdapter(adapter);
//						tvModelName.setText(modelName);
//						tvModelGasType.setText(gasType);
						tvWmsProductCount.setText(String.format("%,d", count));
						tvErpProductCount.setText(String.format("%,d", erpProductCount));

						tvModelName.setText(modelName);
						tvModelGasType.setText(gasType);
					}
				} else if ("getModelInfo".equals(responseData.getResultType())) {
					type = new TypeToken<ProductInfoResult>(){}.getType();

					ProductInfoResult productInfoResult = new Gson().fromJson(str, type);
				} else if ("getSalesModelInfo".equals(responseData.getResultType()))  {
					type = new TypeToken<ArrayList<SalesModelInfoResult>>(){}.getType();
					List<SalesModelInfoResult> list = new Gson().fromJson(str, type);

					if(list.size() == 1) {
						SalesModelInfoResult modelInfo = list.get(0);

						modelCode = modelInfo.getModelCode().substring(0, 5) + "2";;
						Handler delayHandler = new Handler();
						delayHandler.postDelayed(new Runnable() {
							@Override
							public void run() {

								cbLng.setChecked(true);
								readProductBarcode(modelCode);
							}
						}, 100);


					} else if(list.size() > 1 ){
						showListPopup(list);
					}

				}

			} else {
				showRinnaiDialog(WmsMenu05Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_server_error_result));
			}
		} else {

			showRinnaiDialog(WmsMenu05Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if(id == llEtc.getId()) {
			if(!cbEtc.isChecked()) {
				cbEtc.setChecked(true);
			}
		} else if(id == llLpg.getId()) {
			if(!cbLpg.isChecked()) {
				cbLpg.setChecked(true);
			}
		} else if(id == llLng.getId()) {
			if(!cbLng.isChecked()) {
				cbLng.setChecked(true);
			}
		}
	}

	public void productAdd(String barcode, String qty) {
		String log = String.format("제품 바코드 추가 barcode : %s, qty : %s", barcode, qty);

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, log);

		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu05Activity.this);
			String model = barcode.substring(0, 5) + "000" + barcode.substring(5, 6);

			url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, model, qty);

			showProgress(WmsMenu05Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent == lvProductLocation) {
			ProductInfoResult result = (ProductInfoResult) adapter.getItem(position);
			String barcode = result.getItemCode();
			Log.w("onResult", "barcode:" + barcode);

			String cellNo = result.getCellNo();
//			String location = String.format("%s-%s-%s", cellNo.substring(0, 2), cellNo.substring(2, 5), cellNo.substring(5, 6));

			Intent intent = null;
			intent = new Intent(WmsMenu05Activity.this, WmsMenu07Activity.class);
			intent.putExtra(CommonValue.WMS_PARAMETER_KEY_LOCATION, cellNo);

			startActivity(intent);
			finish();;


		}
		return true;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int id = v.getId();
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			if(id == tvModelName.getId()) {
				searchMedelName();
			}
			return true;
		}
		return false;
	}

	private void searchMedelName() {
		if(!networkConnecting) {
			String value = tvModelName.getText().toString();
			String url = "";

			url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, value.toUpperCase());

			showProgress(WmsMenu05Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	private void showListPopup(Object obj) {
		RinnaiSearchListDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiSearchListDialog(WmsMenu05Activity.this, obj, CommonValue.WMS_NOW_VIEW_NAME_MODEL_05);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
			@Override
			public void onPositiveClicked(String type, final String code) {
				String modelName = type;

				tvModelName.setText(modelName);
				tvModelGasType.setText("LNG");

				Handler delayHandler = new Handler();
				delayHandler.postDelayed(new Runnable() {
					@Override
					public void run() {

						modelCode = code.substring(0, 5) + "2";


//						if(cbLng.isChecked()) {
							cbLng.setChecked(true);
							readProductBarcode(modelCode);
//						} else {
//							cbLng.setChecked(true);
//						}
//
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
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
		Log.d("test", modelCode );

		if(cbEtc == compoundButton) {

			if(b) {
				cbLpg.setChecked(false);
				cbLng.setChecked(false);
				tvModelGasType.setText("ETC");
				if(!"0".equals(modelCode.substring(5, 6))) {
					modelCode = modelCode.substring(0, 5) + "0";
					readProductBarcode(modelCode);
				}
			}
		} else if ( cbLpg == compoundButton) {
			if(b) {
				cbEtc.setChecked(false);
				cbLng.setChecked(false);
				tvModelGasType.setText("LPG");
				if(!"1".equals(modelCode.substring(5, 6))) {
					modelCode = modelCode.substring(0, 5) + "1";
					readProductBarcode(modelCode);
				}
			}
		} else if ( cbLng  == compoundButton) {
			if(b) {
				cbEtc.setChecked(false);
				cbLpg.setChecked(false);
				tvModelGasType.setText("LNG");
				if(!"2".equals(modelCode.substring(5, 6))) {
					modelCode = modelCode.substring(0, 5) + "2";
					readProductBarcode(modelCode);
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