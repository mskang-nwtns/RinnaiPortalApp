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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.common.custom.RinnaiSearchListDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.eos.model.SalesModelInfoResult;
import kr.co.rinnai.dms.wms.adapter.WmsMenu02ListAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.CustomAllMoveButtonView;
import kr.co.rinnai.dms.common.custom.CustomMoveButtonView;
import kr.co.rinnai.dms.common.custom.RinnaiReceivedProductDialog;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.wms.model.LocationInfoQueryVO;
import kr.co.rinnai.dms.wms.model.LocationInfoRequest;
import kr.co.rinnai.dms.wms.model.LocationInfoResult;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.wms.model.WmsMenu02ResultEntity;

/**
 * 입고 관련 Activity
 */
public class WmsMenu02Activity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnTouchListener, AdapterView.OnItemLongClickListener, View.OnKeyListener{

    private BroadcastReceiver mReceiver = null;
    
    private boolean networkConnecting = false;

	private Button btnAddItem, btnDelItem;
	private EditText tvLocationBarcode;

	private ListView lvLocationProduct, lvProduct;
	private WmsMenu02ListAdapter locationProductAdapter, productAdapter;

	private ArrayList<LocationInfoResult> locationProductList = null;
	private ArrayList<LocationInfoResult> productList = null;

	private String locationBarcode;

	private CustomAllMoveButtonView btnAddAll, btnDelAll;
	private CustomMoveButtonView move_item;

	private boolean isPressed = false;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		setContentView(R.layout.activity_wms_menu_02);

		btnAddAll = (CustomAllMoveButtonView) findViewById(R.id.btn_add_all);
		btnDelAll  =(CustomAllMoveButtonView) findViewById(R.id.btn_del_all);
		move_item = (CustomMoveButtonView) findViewById(R.id.move_item);

		tvLocationBarcode = (EditText) findViewById(R.id.et_ware_house_location_barcode);
		tvLocationBarcode.setOnKeyListener(WmsMenu02Activity.this);

		btnAddAll.setOnTouchListener(WmsMenu02Activity.this);
		btnDelAll.setOnTouchListener(WmsMenu02Activity.this);

		move_item.setOnTouchListener(WmsMenu02Activity.this);


		lvLocationProduct = (ListView) findViewById(R.id.lv_location_product);
		lvProduct = (ListView) findViewById(R.id.lv_product);

		lvProduct.setOnItemClickListener(WmsMenu02Activity.this);
		lvLocationProduct.setOnItemClickListener(WmsMenu02Activity.this);

		lvProduct.setOnItemLongClickListener(WmsMenu02Activity.this);
		lvLocationProduct.setOnItemLongClickListener(WmsMenu02Activity.this);

		registerReceiver();

		productList = new ArrayList<LocationInfoResult>();

		locationProductAdapter = new WmsMenu02ListAdapter(WmsMenu02Activity.this, locationProductList);
		productAdapter = new WmsMenu02ListAdapter(WmsMenu02Activity.this, productList);

		helper = new MySQLiteOpenHelper(
				WmsMenu02Activity.this,  // 현재 화면의 제어권자
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

					boolean warehouseLocationPattern = Pattern.matches(CommonValue.REGEX_WAREHOUSE_LOCATION, value);
					boolean receivedBarcdoePattern = Pattern.matches(CommonValue.REGEX_RECEIVED_BARCODE, value);
					boolean productBarcodePattern = Pattern.matches(CommonValue.REGEX_PRODUCT_BARCODE, value);




					if(CommonValue.REGEX_SCAN_READ_FAIL.equals(value)) {

					} else if (warehouseLocationPattern) {

						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "로케이션 읽음 : " + value);
						tvLocationBarcode.setText(value);
						locationProductAdapter.clear();
						locationBarcode = value;
						getLocationInfo(value);

					} else if(receivedBarcdoePattern) {

						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "입하 바코드 읽음 : " + value);

						String productBarcode = value.substring(0, 6);
						String createdDateBarcode = value.substring(6, 10);
						String createdLineBarcode = value.substring(10, 11);
						String serialNumberBarcode = value.substring(11, 15);
						String countBarcode = value.substring(15, 19);
						String logText = String.format("입하 바코드 읽음 value : %s, product : %s, date : %s, line : %s, serialNumber : %s, count :%s", value, productBarcode, createdDateBarcode, createdLineBarcode, serialNumberBarcode, countBarcode);
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK,  logText);

						productAdd(value.substring(0, 15), countBarcode);
					} else if (productBarcodePattern) {

						String productBarcode = value.substring(0, 6);
						String createdDateBarcode = value.substring(6, 10);
						String createdLineBarcode = value.substring(10, 11);
						String serialNumberBarcode = value.substring(11, 15);


						String logText = String.format("제품 바코드 읽음 value : %s, product : %s, date : %s, line : %s, serialNumber : %s", value, productBarcode, createdDateBarcode, createdLineBarcode, serialNumberBarcode);

						readProductBarcode(value);
						Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "제품 바코드 읽음 : " + logText);
					} else {
						showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_validation));
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

	/**
	 * 로케이션에 등록된 빠래트 별 제품 정보
	 * @param location
	 */
	private void getLocationInfo(String location) {

		if(!networkConnecting) {

			String url = null;

			location = location.replace("-","");
			String httpHost = HttpClient.getCurrentSsid(WmsMenu02Activity.this);

			url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_LOCATION, CommonValue.WMS_INCHON_WAREHOUSE, location);

			showProgress(WmsMenu02Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		} else {
			showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
		}

	}

	/**
	 *
	 * @param value
	 */
	private void readProductBarcode(String value) {

		RinnaiReceivedProductDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiReceivedProductDialog(WmsMenu02Activity.this, value);
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

			String httpHost = HttpClient.getCurrentSsid(WmsMenu02Activity.this);
			String modelBarcode = String.format("%s%04d", barcode, iQty);
			//barcode.substring(0, 5) + "000" + barcode.substring(5, 6);

			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, modelBarcode);

			showProgress(WmsMenu02Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		} else {
			showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
		}
	}

	@Override
	public void onResult(String result) {

		Log.w("onResult", result);


		dismissProgress();
		networkConnecting = false;

		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
		if (null != responseData ) {
			if("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);

				Type type = null;

				if("getLocationInfo".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<LocationInfoResult>>(){}.getType();

					locationProductList = new Gson().fromJson(str, type);

					locationProductAdapter = new WmsMenu02ListAdapter(WmsMenu02Activity.this, locationProductList);
					lvLocationProduct.setAdapter(locationProductAdapter);

					locationProductAdapter.notifyDataSetChanged();


				} else if ("getModelInfo".equals(responseData.getResultType())) {


					type = new TypeToken<LocationInfoResult>(){}.getType();

					LocationInfoResult productInfoResult = new Gson().fromJson(str, type);


					productList = new ArrayList<LocationInfoResult>();

					productList.add(productInfoResult);

					if(lvProduct.getAdapter() == null) {
						lvProduct.setAdapter(productAdapter);
					}
					productAdapter.addItem(productList);
					productAdapter.notifyDataSetChanged();

				} else if("setLocationInfoV2".equals(responseData.getResultType())) {
					type = new TypeToken<WmsMenu02ResultEntity>(){}.getType();

					WmsMenu02ResultEntity resultEntity = new Gson().fromJson(str, type);
					String resultType = resultEntity.getType();

					List<LocationInfoQueryVO> list = resultEntity.getList();

					List<Integer> idxList = new ArrayList<Integer>();
					List<LocationInfoResult>  resultList = new ArrayList<LocationInfoResult>();
					if("add".equals(resultType) || "addAll".equals(resultType)) {

						ArrayList<LocationInfoResult> tmpList2 = productAdapter.getList();

						for(int i = 0; i <list.size(); i ++) {
							LocationInfoQueryVO vo = list.get(i);
							if(vo.isStatus()) {
								LocationInfoResult cellInfo = tmpList2.get(vo.getIndex());
								cellInfo.setCellNo(vo.getLocationNo());
								cellInfo.setType(vo.getItemCode().substring(5, 6));
								cellInfo.setCellDetail(vo.getCellDetail());
								resultList.add(cellInfo);
								idxList.add(vo.getIndex());
							}
						}
						if(resultList.size() > 0) {
							locationProductAdapter.addItem(resultList);
							locationProductAdapter.notifyDataSetChanged();
							removeSelectItem(idxList, productAdapter);

							productAdapter.clearReadingItem();
						}

					} else if("del".equals(resultType) || "delAll".equals(resultType)) {

						ArrayList<LocationInfoResult> tmpList1 = locationProductAdapter.getList();
						for(int i = 0; i <list.size(); i ++) {
							LocationInfoQueryVO vo = list.get(i);
							if(vo.isStatus()) {
								LocationInfoResult cellInfo = tmpList1.get(vo.getIndex());
								cellInfo.setCellNo(vo.getLocationNo());
								cellInfo.setType(vo.getCellStatus());
								cellInfo.setCellDetail(vo.getCellDetail());
								resultList.add(cellInfo);
								idxList.add(vo.getIndex());
							}
						}
						if(resultList.size() > 0) {

							if (lvProduct.getAdapter() == null) {
								lvProduct.setAdapter(productAdapter);
							}

							productAdapter.addItem(resultList);
							productAdapter.notifyDataSetChanged();
							removeSelectItem(idxList, locationProductAdapter);
							locationProductAdapter.clearReadingItem();

						}

					}
					showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), String.format("입고 작업 %d건 중 %d건을\n완료하였습니다.",list.size(), idxList.size() ));
				} else if ("getSalesModelInfo".equals(responseData.getResultType()))  {
					type = new TypeToken<ArrayList<SalesModelInfoResult>>(){}.getType();
					List<SalesModelInfoResult> list = new Gson().fromJson(str, type);
					showListPopup(list);


				}

			} else {
				showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), responseData.getResultMessage());
			}
		} else {
			showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.msg_order_report_barcode_not_result));
		}
	}



	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if(parent == lvProduct) {
			Log.w("onResult", "position");
			productAdapter.setSelectItem(position);
			productAdapter.notifyDataSetChanged();
			locationProductAdapter.clearReadingItem();
			locationProductAdapter.notifyDataSetChanged();
		} else if (parent == lvLocationProduct)  {
			Log.w("onResult", "position");
			locationProductAdapter.setSelectItem(position);
			locationProductAdapter.notifyDataSetChanged();
			productAdapter.clearReadingItem();
			productAdapter.notifyDataSetChanged();
		}
	}

	private List<Integer> getSelectItem(WmsMenu02ListAdapter Adapter) {

		List<Integer> list = new ArrayList<Integer>();

		//로케이션 제품 목록에서 선택한 제품 선정
		for(int i = 0; i < Adapter.getCount(); i ++ ) {
			LocationInfoResult productInfoResult = (LocationInfoResult)Adapter.getItem(i);

			if(productInfoResult.isSelected()) {

				list.add(i);
			}
		}
		return list;

	}

	/**
	 * Adapter 에서 선택된 List 제거
	 * @param list
	 * @param adapter
	 */
	private void removeSelectItem(List<Integer> list, WmsMenu02ListAdapter adapter) {
		if(list != null) {
			Collections.reverse(list);

			for (int i = 0; i < list.size(); i++) {
				adapter.removeItem(list.get(i));
			}
		} else {
			adapter.clear();
		}
		adapter.notifyDataSetChanged();
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();
		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(id == btnAddAll.getId()) {
					btnAddAll.buttonClick(true);
				} else if(id == btnDelAll.getId()) {
					btnDelAll.buttonClick(true);
				} else if(id == move_item.getId()) {
					move_item.buttonClick(true);
				}
				Log.d("test", "Action_DOWN " + id);
				break;

			case MotionEvent.ACTION_UP:
				Log.d("test", "Action_UP"  + id);
				Intent intent = null;
				if(id == btnAddAll.getId()) {

					btnAddAll.buttonClick(false);
					btnDelAll.buttonClick(false);
					if(isPressed) {
						addAllItem();
					}

				} else if(id == btnDelAll.getId()) {

					btnDelAll.buttonClick(false);
					if(isPressed) {
						deleteAllItem();
					}
				} else if(id == move_item.getId()) {
					move_item.buttonClick(false);
					if(isPressed) {
						if(null !=  locationBarcode ) {
							if(!locationProductAdapter.isSelectedItem()) {

								if(!productAdapter.isSelectedItem()) {
									showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_not_select_product));
								} else {
									addItem();
								}
							} else {
								deleteItem();
							}
						} else {
							showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_not_select_location));
						}

					}
				}
				break;

		}

		return false;
	}

	/**
	 * 지게차에 있는 데이터 전체 등록(로케이션 등록 제품 등록)
	 */
	private void addAllItem() {
		if(!networkConnecting) {
			if (null != locationBarcode) {

				ArrayList<LocationInfoResult> list = productAdapter.getList();

				ArrayList<LocationInfoRequest> requestList = new ArrayList<LocationInfoRequest>();

				for (int i = 0; i < list.size(); i++) {
					LocationInfoRequest request = new LocationInfoRequest();
					LocationInfoResult result = list.get(i);
					request.setIndex(i);
					request.setComId("R");
					request.setWareNo("16");
					request.setItemCode(result.getItemCode().trim());
					request.setQty(result.getQty());
					request.setLocationNo(locationBarcode.replace("-", ""));
					request.setCellMake(result.getOriginalCellMake());
					request.setUserId(userName);
					request.setCellStatus("2");
					requestList.add(request);
				}

				String strResponse = ParseUtil.getJSONFromObject(requestList);


				String httpHost = HttpClient.getCurrentSsid(WmsMenu02Activity.this);

				String url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_LOCATION, CommonValue.HTTP_VERSION_2, CommonValue.HTTP_PROCESS_TYPE_ADD_ALL);

				networkConnecting = true;
				showProgress(WmsMenu02Activity.this);
				HttpClient.post(url, WmsMenu02Activity.this, strResponse);

			} else {
				showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_not_select_location));
			}
		} else {
			showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
		}

	}

	/**
	 * 로케이션에 있는 데이터 전체 이동(로케이션 등록 제품 삭제)
	 */
	private void deleteAllItem() {
		if(!networkConnecting) {
			ArrayList<LocationInfoResult> list = locationProductAdapter.getList();

			ArrayList<LocationInfoRequest> requestList = new ArrayList<LocationInfoRequest>();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					LocationInfoRequest request = new LocationInfoRequest();
					LocationInfoResult result = list.get(i);
					request.setIndex(i);
					request.setCellDetail(result.getCellDetail());
					request.setComId("R");
					request.setWareNo("16");
					request.setItemCode("");
					request.setQty("");
					request.setLocationNo(locationBarcode.replace("-", ""));
					request.setCellMake("");
					request.setUserId(userName);
					request.setCellStatus("0");
					requestList.add(request);
				}

				String strResponse = ParseUtil.getJSONFromObject(requestList);

				String httpHost = HttpClient.getCurrentSsid(WmsMenu02Activity.this);

				String url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_LOCATION, CommonValue.HTTP_VERSION_2, CommonValue.HTTP_PROCESS_TYPE_DEL_ALL);
				networkConnecting = true;
				showProgress(WmsMenu02Activity.this);
				HttpClient.post(url, WmsMenu02Activity.this, strResponse);

			}
		} else {
			showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
		}

		Log.w("onResult", "position");

	}

	/**
	 * 지게차에 있는 데이터 전체 등록(로케이션 등록 제품 등록)
	 */
	private void addItem() {
		if(!networkConnecting) {
			if (null != locationBarcode) {

				List<Integer> list = getSelectItem(productAdapter);

				if (list.size() == 0) {
					showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_not_select_product));
				} else {

					List<LocationInfoResult> addList = null;

					addList = new ArrayList<LocationInfoResult>();
					ArrayList<LocationInfoRequest> requestList = new ArrayList<LocationInfoRequest>();

					for (int i = 0; i < list.size(); i++) {
						int idx = list.get(i);
						LocationInfoResult result = (LocationInfoResult) productAdapter.getItem(idx);

						LocationInfoRequest request = new LocationInfoRequest();
//						LocationInfoResult result = addList.get(i);
						request.setIndex(idx);
						request.setComId("R");
						request.setWareNo("16");
						request.setItemCode(result.getItemCode().trim());
						request.setQty(result.getQty());
						request.setLocationNo(locationBarcode.replace("-", ""));
						request.setCellMake(result.getOriginalCellMake());
						request.setUserId(userName);
						request.setCellStatus("2");
						requestList.add(request);
						addList.add(result);
					}

					String strResponse = ParseUtil.getJSONFromObject(requestList);


					String httpHost = HttpClient.getCurrentSsid(WmsMenu02Activity.this);

					String url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_LOCATION, CommonValue.HTTP_VERSION_2, CommonValue.HTTP_PROCESS_TYPE_ADD);
					networkConnecting = true;
					showProgress(WmsMenu02Activity.this);
					HttpClient.post(url, WmsMenu02Activity.this, strResponse);


				}

			} else {
				showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_not_select_location));
			}
		} else {
			showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
		}
	}
	private void deleteItem() {
		if(!networkConnecting) {

			if (null != locationBarcode) {

				List<Integer> list = getSelectItem(locationProductAdapter);

				if (list.size() == 0) {
					showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_not_select_product));
				} else {


					List<LocationInfoResult> addList = null;
					LocationInfoResult addResult = null;


					addList = new ArrayList<LocationInfoResult>();
					ArrayList<LocationInfoRequest> requestList = new ArrayList<LocationInfoRequest>();
					for (int i = 0; i < list.size(); i++) {
						int idx = list.get(i);
						LocationInfoRequest request = new LocationInfoRequest();
						LocationInfoResult result = (LocationInfoResult) locationProductAdapter.getItem(list.get(i));
						request.setIndex(idx);
						request.setCellDetail(result.getCellDetail());
						request.setComId("R");
						request.setWareNo("16");
						request.setItemCode("");
						request.setQty("");
						request.setLocationNo(locationBarcode.replace("-", ""));
						request.setCellMake("");
						request.setUserId(userName);
						request.setCellStatus("0");
						requestList.add(request);

						addList.add(result);
					}

					String strResponse = ParseUtil.getJSONFromObject(requestList);


					String httpHost = HttpClient.getCurrentSsid(WmsMenu02Activity.this);

					String url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_LOCATION, CommonValue.HTTP_VERSION_2, CommonValue.HTTP_PROCESS_TYPE_DEL);
					networkConnecting = true;
					showProgress(WmsMenu02Activity.this);
					HttpClient.post(url, WmsMenu02Activity.this, strResponse);


				}

			} else {
				showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_not_select_location));
			}
		} else {
			showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

		if(!networkConnecting) {
			if (parent == lvLocationProduct) {
				LocationInfoResult result = (LocationInfoResult) locationProductAdapter.getItem(position);
				String barcode = result.getItemCode();



				String qty = String.format("%4d", Integer.parseInt(result.getQty()));
				Log.w("onResult", "barcode:" + barcode);

				String value = String.format("%s%s%s%s", barcode, result.getOriginalCellMake().trim(), "0", "0000");
				Log.w("onResult", "value:" + value);

				RinnaiReceivedProductDialog rinnaiReceivedProductDialog;
				rinnaiReceivedProductDialog = new RinnaiReceivedProductDialog(WmsMenu02Activity.this, value, qty, position);
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);
				rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
					@Override
					public void onPositiveClicked(String barcode, String qty) {
						productAdd(barcode, qty);
					}

					@Override
					public void onPositiveClicked(String barcode, String qty, int position) {


						LocationInfoResult result = (LocationInfoResult) locationProductAdapter.getItem(position);

						ArrayList<LocationInfoRequest> requestList = new ArrayList<LocationInfoRequest>();

						if (result.getQty().trim().equals(qty.trim())) {
							showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_error_no_changes_data));
						} else {
							result.setQty(qty);

							locationProductAdapter.getList().set(position, result);
							//locationProductAdapter.removeItem(position + 1);
							locationProductAdapter.notifyDataSetChanged();

							LocationInfoRequest request = new LocationInfoRequest();
							request.setComId("R");
							request.setWareNo("16");
							request.setItemCode(result.getItemCode().trim());
							request.setQty(result.getQty());
							request.setLocationNo(locationBarcode.replace("-", ""));
							request.setCellMake(result.getOriginalCellMake());
							request.setCellDetail(result.getCellDetail());
							request.setUserId(userName);
							request.setCellStatus("2");
							requestList.add(request);
						}

						String strResponse = ParseUtil.getJSONFromObject(requestList);


						String httpHost = HttpClient.getCurrentSsid(WmsMenu02Activity.this);
						networkConnecting = true;
						showProgress(WmsMenu02Activity.this);
						String url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_WMS, CommonValue.HTTP_INFO, CommonValue.HTTP_LOCATION);
						HttpClient.put(url, WmsMenu02Activity.this, strResponse);

					}

					@Override
					public void onPositiveClicked(String barcode, String modelName, String qty) {

					}
				});
				rinnaiReceivedProductDialog.show();

			} else if (parent == lvProduct) {

				AlertDialog.Builder alert_confirm = new AlertDialog.Builder(WmsMenu02Activity.this);
				alert_confirm.setMessage("선택한 제품을 삭제 하시겠습니까?").setCancelable(false).setPositiveButton("삭제",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								List<Integer> list = new ArrayList<Integer>();

								list.add(position);
								removeSelectItem(list, productAdapter);
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
		} else {
			showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
		}
		return true;
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {

		int id = v.getId();
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			if (id == tvLocationBarcode.getId()) {
				if(!networkConnecting) {
					searchMedelName();
				} else {
					showRinnaiDialog(WmsMenu02Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(tvLocationBarcode.getWindowToken(), 0);
			}
			return true;
		}

		return false;
	}


	private void searchMedelName() {
		if(!networkConnecting) {
			String value = tvLocationBarcode.getText().toString();
			String url = "";

			url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, value.toUpperCase());

			showProgress(WmsMenu02Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	private void showListPopup(Object obj) {
		RinnaiSearchListDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiSearchListDialog(WmsMenu02Activity.this, obj, CommonValue.WMS_NOW_VIEW_NAME_MODEL_05, View.VISIBLE);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
			@Override
			public void onPositiveClicked(String type, final String code) {
				String modelName = type;
				readProductBarcode(code);
				/*
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

				 */

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
}