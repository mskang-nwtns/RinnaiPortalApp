package kr.co.rinnai.dms.eos.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.adapter.StockWarehouseListAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.RinnaiSearchListDialog;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.model.SalesModelInfoResult;
import kr.co.rinnai.dms.eos.model.StockByWarehouseResult;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 * 창고별 재고 현황 확인
 */
public class EmployeeMenu05Activity extends BaseActivity implements  View.OnTouchListener, View.OnKeyListener{


	private EditText etSearchValue;

	private TextView tvSearchName;

	private RelativeLayout btnSearch;
	private ListView lvStockWarehouse;

	private Spinner spStockType;

	private boolean isPressed = false;

	private boolean networkConnecting = false;

	private StockWarehouseListAdapter adapter;

	private ArrayAdapter<String> arrayAdapter;





	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_menu_05);


		boolean isTablet = Util.isTabletDevice(EmployeeMenu05Activity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

		etSearchValue = (EditText) findViewById(R.id.et_search_value);
		etSearchValue.setOnKeyListener(EmployeeMenu05Activity.this);
		tvSearchName = (TextView) findViewById(R.id.tv_aos05_search_name);

		btnSearch = (RelativeLayout) findViewById(R.id.btn_aos05_search);

		lvStockWarehouse = (ListView) findViewById(R.id.lv_stock_warehouse);

		spStockType = (Spinner) findViewById(R.id.sp_stock_type);


		btnSearch.setOnClickListener(EmployeeMenu05Activity.this);

		List arrayList = new ArrayList<>();
		arrayList.add("전체");
		arrayList.add("양품");
		arrayList.add("불량");

		arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayList);

		spStockType.setAdapter(arrayAdapter);

		etSearchValue.setOnKeyListener(EmployeeMenu05Activity.this);
		try {
			String gwId = RinnaiApp.getInstance().getGwId();
			String tmpId =  null;
			if(null != gwId) tmpId = gwId.replace("@rinnai.co.kr", "");
			if("mini3248".equals(tmpId) || "ssj9567".equals(tmpId)) {

			} else {
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
			}
			if ("mini3248".equals(tmpId) || "ssj9567".equals(tmpId)) {

			} else {
				getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
			}
		} catch(Exception e) {

		}

	}



	@Override
	@SuppressWarnings({"MissingPermission"})
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etSearchValue.getWindowToken(), 0);
		if (v == btnSearch) {
			getSearchValue();

		}
	}

	private void getSearchValue() {
		if(!networkConnecting) {
			String value = etSearchValue.getText().toString();
			String url = "";

			url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, value.toUpperCase());


			showProgress(EmployeeMenu05Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}

	}

	/**
	 * 매장 재고 조회
	 *
	 */
	private void getStockByWarehouse(String value) {
//		String value =  etSearchValue.getText().toString();
		//    /stock/{date}/{type}/{value}
		if(!networkConnecting) {
			etSearchValue.setText(value);
			int position = spStockType.getSelectedItemPosition();


			String url = String.format("%s/%s/%s/%s/%d", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, value, position);

			showProgress(EmployeeMenu05Activity.this);

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
			if("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);
				Type type = null;
				if("getStockByWarehouse".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<StockByWarehouseResult>>(){}.getType();

					List<StockByWarehouseResult> list = new Gson().fromJson(str, type);
					adapter = new StockWarehouseListAdapter(EmployeeMenu05Activity.this, list);

					lvStockWarehouse.setAdapter(adapter);

					adapter.notifyDataSetChanged();

				} if ("getSalesModelInfo".equals(responseData.getResultType()))  {
					type = new TypeToken<ArrayList<SalesModelInfoResult>>(){}.getType();
					List<SalesModelInfoResult> list = new Gson().fromJson(str, type);

					if(list.size() == 1) {
						SalesModelInfoResult modelInfo = list.get(0);
						tvSearchName.setText(modelInfo.getModelName());
						final String modelCode = modelInfo.getModelCode();
						Handler delayHandler = new Handler();
						delayHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getStockByWarehouse(modelCode);
							}
						}, 100);


					} else if (list.size() > 1) {
						showListPopup(list);
					}

				}

			} else {
				showRinnaiDialog(EmployeeMenu05Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}
	}
	private void showListPopup(Object obj) {
		RinnaiSearchListDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiSearchListDialog(EmployeeMenu05Activity.this, obj, CommonValue.AOS_NOW_VIEW_NAME_MODEL_05);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
			@Override
			public void onPositiveClicked(String name, final  String code) {
				tvSearchName.setText(name);
				Handler delayHandler = new Handler();
				delayHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						getStockByWarehouse(code);
					}
				}, 100);
			}

			@Override
			public void onPositiveClicked(String barcode, final String code, int position) {

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
			if (id == etSearchValue.getId()) {
				if(!networkConnecting) {
					getSearchValue();
				} else {
					showRinnaiDialog(EmployeeMenu05Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
				}
			}
			return true;
		}

		return false;
	}



}
