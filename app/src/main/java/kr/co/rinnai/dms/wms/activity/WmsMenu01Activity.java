package kr.co.rinnai.dms.wms.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.regex.Pattern;

import device.common.ScanConst;
import device.sdk.ScanManager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.wms.adapter.WmsMenu01ListAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.CustomButtonView;
import kr.co.rinnai.dms.common.custom.RinnaiReceivedProductDialog;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.wms.model.ProductInfoResult;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ParseUtil;

/**
 * 입하 관련 Activity
 */
public class WmsMenu01Activity extends BaseActivity   {

	private String barcodeType = null;

	private WmsMenu01ListAdapter wmsMenu01ListAdapter;

	private ArrayList<ProductInfoResult> movementInstructionsList = null;
	
    private BroadcastReceiver mReceiver = null;
    
    private boolean networkConnecting = false;

	private ListView lvMovementInstructions;

	private CustomButtonView btnPrint, btnClear;
	private TextView tvMovementInstructionsBarcode;
	private String movementInstructionsBarcode;


	private boolean isPressed = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.activity_wms_menu_01);
		lvMovementInstructions = (ListView) findViewById(R.id.lv_movement_instructions_product);
		//위치 모델명 형식 수 량
		ScanManager.getInstance().aDecodeSetTriggerMode(ScanConst.TriggerMode.DCD_TRIGGER_MODE_ONESHOT);

		registerReceiver();
		btnPrint = (CustomButtonView) findViewById(R.id.btn_print);
		btnClear = (CustomButtonView) findViewById(R.id.btn_clear);

		tvMovementInstructionsBarcode = (TextView) findViewById(R.id.tv_movement_instructions_barcode);


		btnPrint.setOnTouchListener(WmsMenu01Activity.this);

		btnClear.setOnTouchListener(WmsMenu01Activity.this);

		wmsMenu01ListAdapter = new WmsMenu01ListAdapter();



		
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


					boolean movementInstructionsPattern = Pattern.matches(CommonValue.REGEX_MOVEMENT_INSTRUCTIONS_BARCODE, value);
					boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, value);

					if(CommonValue.REGEX_SCAN_READ_FAIL.equals(value)) {

					} else if(movementInstructionsPattern) {
						barcodeType = "movement";
						tvMovementInstructionsBarcode.setText(value);
						movementInstructionsBarcode = value;
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "orderReportCustCodePattern : " + value);

						wmsMenu01ListAdapter.clear();
						wmsMenu01ListAdapter.notifyDataSetChanged();

						getMovementInstructions(value);
					} else if (productBarcodePattern) {
						tvMovementInstructionsBarcode.setText("");
						if(movementInstructionsList == null) {
							movementInstructionsList = new ArrayList<ProductInfoResult>();
						}


						barcodeType = "product";
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "productBarcodePattern : " + value);
						readProductBarcode(value);
					} else {

						showRinnaiDialog(WmsMenu01Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));

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
	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(mReceiver);
	}

	private void getMovementInstructions(String value) {

		String[] barcode = value.split("-");
		String comId = barcode[0];
		String workDate = barcode[1].replace("/", "-");
		String inWareNo = barcode[2];
		String outWareNo = barcode[3];
		String orderNo = barcode[4];

		//String.format("제품 바코드 읽음 value : %s, product : %s, date : %s, line : %s, serialNumber : %s", value, productBarcode, createdDateBarcode, createdLineBarcode, serialNumberBarcode);

		String log = String.format("이동지시서 바코드 읽음 value : %s, comId : %s, workDate : %s, inWareNo : %s, orderNo : %s", value, comId, workDate, inWareNo, orderNo);

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, log);
		//R-2019/09/06-01-10

		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu01Activity.this);

			url = String.format("%s/%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_MOVEMENT_INSTRUCTIONS, workDate, inWareNo, outWareNo, orderNo);

			showProgress(WmsMenu01Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}


	}

	private void readProductBarcode(String value) {
		RinnaiReceivedProductDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiReceivedProductDialog(WmsMenu01Activity.this, value);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
			@Override
			public void onPositiveClicked(String barcode, String qty) {
				productAdd(barcode, qty);
			}

			@Override
			public void onPositiveClicked(String barcode, String qty, int position) {
				productAdd(barcode, qty);
			}

			@Override
			public void onPositiveClicked(String barcode, String modelName, String qty) {

			}
		});
		rinnaiReceivedProductDialog.show();

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

				if("getMovementInstructions".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<ProductInfoResult>>(){}.getType();

					String[] barcode = movementInstructionsBarcode.split("-");
					String comId = barcode[0];
					String workDate = barcode[1].replace("/", "-");
					String inWareNo = barcode[2];
					String orderNo = barcode[3];
//2019-09

					movementInstructionsList = new Gson().fromJson(str, type);

					for(int i = 0; i <movementInstructionsList.size();i++) {
//						String cellMake;
						movementInstructionsList.get(i).setCellMake(workDate.substring(2,4) + workDate.substring(5,7));
					//	String itemCode = movementInstructionsList.get(i).getItemCode();
						//String temp = itemCode.substring(0, 5) + itemCode.substring(8, 9);
						String date = workDate.substring(2, 4) + workDate.substring(5, 7);
						String goodsCode =  movementInstructionsList.get(i).getGoodsCode(date);
						movementInstructionsList.get(i).setGoodsCode(goodsCode);

					}

					wmsMenu01ListAdapter = new WmsMenu01ListAdapter(WmsMenu01Activity.this, movementInstructionsList);

					lvMovementInstructions.setAdapter(wmsMenu01ListAdapter);
				} else if ("getModelInfo".equals(responseData.getResultType())) {
					type = new TypeToken<ProductInfoResult>(){}.getType();

					ProductInfoResult productInfoResult = new Gson().fromJson(str, type);

					if(movementInstructionsList == null) {
						movementInstructionsList = new ArrayList<ProductInfoResult>();
					}
					movementInstructionsList.add(productInfoResult);
					wmsMenu01ListAdapter = new WmsMenu01ListAdapter(WmsMenu01Activity.this, movementInstructionsList);
					lvMovementInstructions.setAdapter(wmsMenu01ListAdapter);
					wmsMenu01ListAdapter.notifyDataSetChanged();


				} else if("getBarcodePrint".equals(responseData.getResultType())) {

					movementInstructionsList = new ArrayList<ProductInfoResult>();
					wmsMenu01ListAdapter.clear();
					wmsMenu01ListAdapter.notifyDataSetChanged();
				}

			} else {
				showRinnaiDialog(WmsMenu01Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_server_error_result));
			}
		} else {
			showRinnaiDialog(WmsMenu01Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
		}
	}

	/*

	@Override
	public void onClick(View v) {

		if (btnPrint == v) {

		} else if (btnClear == v) {
			wmsMenu01ListAdapter.clear();
			wmsMenu01ListAdapter.notifyDataSetChanged();
		}
	}

*/
	/**
	 *
	 * @param barcode
	 * @param qty
	 */
	public void productAdd(String barcode, String qty) {
		String log = String.format("제품 바코드 추가 barcode : %s, qty : %s", barcode, qty);

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, log);

		int iQty = Integer.parseInt(qty);
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(WmsMenu01Activity.this);
			String modelBarcode = String.format("%s%04d", barcode, iQty);
			//barcode.substring(0, 5) + "000" + barcode.substring(5, 6);

			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, modelBarcode);

			showProgress(WmsMenu01Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}

	public void printBarcode() {

		if(!networkConnecting) {
			ArrayList<ProductInfoResult> lit = wmsMenu01ListAdapter.getList();

			String strResponse = ParseUtil.getJSONFromObject(lit);

			strResponse = strResponse.replaceAll("기타", "ETC");
			String httpHost = HttpClient.getCurrentSsid(WmsMenu01Activity.this);

			String url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_PRINT, CommonValue.HTTP_BARCODE);

			showProgress(WmsMenu01Activity.this);

			networkConnecting = true;

			HttpClient.post(url, WmsMenu01Activity.this, strResponse);
		} else {
			showRinnaiDialog(WmsMenu01Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_barcode_printing));
		}




	}

	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();
		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(id == btnPrint.getId()) {
					btnPrint.buttonClick(true);
				} else if(id == btnClear.getId()) {
					btnClear.buttonClick(true);
				}
				Log.d("test", "Action_DOWN " + id);
				break;
			case MotionEvent.ACTION_UP:
				Log.d("test", "Action_UP"  + id);
				Intent intent = null;
				if(id == btnPrint.getId()) {

					btnPrint.buttonClick(false);
					printBarcode();

				} else if(id == btnClear.getId()) {

					btnClear.buttonClick(false);
					if(isPressed) {
						movementInstructionsList = new ArrayList<ProductInfoResult>();
						wmsMenu01ListAdapter.clear();
						wmsMenu01ListAdapter.notifyDataSetChanged();
					}
				}
				break;

		}

		return false;
	}

}