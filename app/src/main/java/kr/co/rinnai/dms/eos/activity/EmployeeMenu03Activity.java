package kr.co.rinnai.dms.eos.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseFragmentActivity;

import kr.co.rinnai.dms.adapter.StockStoreFragmentAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.RinnaiSearchListDialog;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.model.SalesAgencyInfoResult;
import kr.co.rinnai.dms.eos.model.SalesModelInfoResult;
import kr.co.rinnai.dms.eos.model.StockByStoreResult;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.listener.PageListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 * 영업본부 직원</br>
 * 매장재고
 */
public class EmployeeMenu03Activity extends BaseFragmentActivity implements PageListener, View.OnKeyListener, View.OnFocusChangeListener {

	private ViewPager viewPager = null;
	private TextView tvSearchType, tvSeachName;
	private RelativeLayout btnSearch;

	private RelativeLayout rlTopMenuCust, rlTopMenuModel;

	private ImageView ivTabCustIcon, ivTabModelicon;

	private String nowView;

	private EditText etSearchValue;

	private boolean networkConnecting = false;

	private StockStoreFragmentAdapter fragmentAdapter = null;

	private TextView tvTopTitle01 , tvTopTitle02;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_menu_03);


		boolean isTablet = Util.isTabletDevice(EmployeeMenu03Activity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

		tvSeachName = (TextView) findViewById(R.id.tv_search_name);
		tvSearchType = (TextView) findViewById(R.id.tv_search_type);

		rlTopMenuCust = (RelativeLayout) findViewById(R.id.rl_agency_03_top_menu_cust);
		rlTopMenuModel= (RelativeLayout) findViewById(R.id.rl_agency_03_top_menu_model);

		ivTabCustIcon = (ImageView) findViewById(R.id.iv_agency_03_top_menu_cust_icon);
		ivTabModelicon = (ImageView) findViewById(R.id.iv_agency_03_top_menu_model_icon);

		etSearchValue = (EditText) findViewById(R.id.et_search_value);

		etSearchValue.setOnKeyListener(EmployeeMenu03Activity.this);
		etSearchValue.setOnFocusChangeListener(EmployeeMenu03Activity.this);

		rlTopMenuCust.setOnClickListener(EmployeeMenu03Activity.this);
		rlTopMenuModel.setOnClickListener(EmployeeMenu03Activity.this);


		rlTopMenuCust.setBackgroundResource(R.drawable.release_top_bg_select);
		rlTopMenuModel.setBackgroundResource(R.drawable.release_top_bg_disable);

		tvTopTitle01 = (TextView) findViewById(R.id.tv_aos3_top_title_01);
		tvTopTitle02 = (TextView) findViewById(R.id.tv_aos3_top_title_02);

		ivTabCustIcon.setVisibility(View.VISIBLE);
		ivTabModelicon.setVisibility(View.GONE);

		btnSearch = (RelativeLayout)findViewById(R.id.btn_search);

		btnSearch.setOnClickListener(EmployeeMenu03Activity.this);
		viewPager = (ViewPager)findViewById(R.id.aos_menu03_pager);

		nowView = CommonValue.AOS_NOW_VIEW_NAME_CUST;

		tvSearchType.setText("모델명");

		viewPager = (ViewPager) findViewById(R.id.aos_menu03_pager);

		tvTopTitle01.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_select));
		tvTopTitle02.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_normal));


		tvSeachName.setSingleLine(true);
		tvSeachName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		//viewHolder.tvSearchName. 받아야 문자가 흐르기 때문에
		//포커스를 받을 수 없는 상황에서는 선택된 것으로 처리하면 마키 동작
		tvSeachName.setSelected(true);
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
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		}


	}

	@Override
	@SuppressWarnings({"MissingPermission"})
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(etSearchValue.getWindowToken(), 0);
		if(v == btnSearch) {
			getSearchValue();

		} else if (v == rlTopMenuCust) {
			if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(nowView)) {
				tvSeachName.setText("");
				nowView = CommonValue.AOS_NOW_VIEW_NAME_CUST;
				rlTopMenuCust.setBackgroundResource(R.drawable.release_top_bg_select);
				rlTopMenuModel.setBackgroundResource(R.drawable.release_top_bg_disable);

				ivTabCustIcon.setVisibility(View.VISIBLE);
				ivTabModelicon.setVisibility(View.GONE);
				tvSearchType.setText("모델명");
				viewPager.setVisibility(View.INVISIBLE);
			}

		} else if (v == rlTopMenuModel) {
			if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(nowView)) {
				tvSeachName.setText("");
				nowView = CommonValue.AOS_NOW_VIEW_NAME_MODEL;
				rlTopMenuCust.setBackgroundResource(R.drawable.release_top_bg_disable);
				rlTopMenuModel.setBackgroundResource(R.drawable.release_top_bg_select);

				ivTabCustIcon.setVisibility(View.GONE);
				ivTabModelicon.setVisibility(View.VISIBLE);
				tvSearchType.setText("매장명");
				viewPager.setVisibility(View.INVISIBLE);
			}

		}
		etSearchValue.setText("");

	}

	@Override
	public void onPageChange(Fragment fragment, int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(Fragment fragment, int position) {

	}

	@Override
	public void onPageSelected(Fragment fragment, int position, Object selecetdItem) {

	}

	@Override
	public void onPageSelected(int position, String type) {
		if(position == 0) {
			tvTopTitle01.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_select));
			tvTopTitle02.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_normal));
		} else {
			tvTopTitle01.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_normal));
			tvTopTitle02.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_select));
		}
	}

	/**
	 * 매장 재고 조회
	 *
	 */
	private void getStockByStore(String value) {
//		String value =  etSearchValue.getText().toString();
		 //    /stock/{date}/{type}/{value}

		if(!networkConnecting) {
			etSearchValue.setText(value);

			Date mDate;
			SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMM");
			long mNow = System.currentTimeMillis();
			mDate = new Date(mNow);
			String date = mFormat.format(mDate);
			String url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, date, nowView, value);

			showProgress(EmployeeMenu03Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}

	}

	private void getSearchValue() {
		if(!networkConnecting) {
			String value = etSearchValue.getText().toString();
			String url = "";
			if (CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(nowView)) {
				url = String.format("%s/%s/%s/%s/%s/S20/S/319/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_INFO, CommonValue.HTTP_AGENCY, value);
			} else if (CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(nowView)) {
				url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, value.toUpperCase());
			}

			showProgress(EmployeeMenu03Activity.this);

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
				if("getStockByStore".equals(responseData.getResultType())) {
					type = new TypeToken<ArrayList<StockByStoreResult>>(){}.getType();
					List<StockByStoreResult> list = new Gson().fromJson(str, type);

					StockByStoreResult stock = null;
					if(list.size() > 1) {
						stock = list.get(1);
					} else {
						stock = list.get(0);
					}

					List<Object> objList = new ArrayList<Object>();


					objList.add(list);
					objList.add(list);

					viewPager.setVisibility(View.VISIBLE);

					fragmentAdapter = new StockStoreFragmentAdapter(this, getSupportFragmentManager(), objList, this, nowView);
					if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(nowView)) {
						tvSeachName.setText(stock.getCustName());
					} else if (CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(nowView)) {
						tvSeachName.setText(stock.getModelName());
					}
					viewPager.setAdapter(fragmentAdapter);
					viewPager.addOnPageChangeListener(fragmentAdapter);
					viewPager.setCurrentItem(0);

					tvTopTitle01.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_select));
					tvTopTitle02.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_normal));
				} else if ("getSalesAgencyInfo".equals(responseData.getResultType()))  {
					type = new TypeToken<ArrayList<SalesAgencyInfoResult>>(){}.getType();
					List<SalesAgencyInfoResult> list = new Gson().fromJson(str, type);
					if(list.size() == 1) {
						SalesAgencyInfoResult agencyInfo = list.get(0);
						final String custCode = agencyInfo.getCustCode();

						Handler delayHandler = new Handler();
						delayHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getStockByStore(custCode);
							}
						}, 100);


					} else if(list.size() > 1 ){
						showListPopup(list);
					}

				} else if ("getSalesModelInfo".equals(responseData.getResultType()))  {
					type = new TypeToken<ArrayList<SalesModelInfoResult>>(){}.getType();
					List<SalesModelInfoResult> list = new Gson().fromJson(str, type);

					if(list.size() == 1) {
						SalesModelInfoResult modelInfo = list.get(0);
						final String modelCode = modelInfo.getModelCode();
						Handler delayHandler = new Handler();
						delayHandler.postDelayed(new Runnable() {
							@Override
							public void run() {
								getStockByStore(modelCode);
							}
						}, 100);


					} else if(list.size() > 1 ){
						showListPopup(list);
					}

				}

			} else {
				showRinnaiDialog(EmployeeMenu03Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}
	}

	private void showListPopup(Object obj) {
		RinnaiSearchListDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiSearchListDialog(EmployeeMenu03Activity.this, obj, nowView);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
			@Override
			public void onPositiveClicked(String type, final String code) {
				Handler delayHandler = new Handler();
				delayHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						getStockByStore(code);
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
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		int id = v.getId();
		if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
			if (id == etSearchValue.getId()) {
				if(!networkConnecting) {
					getSearchValue();
				} else {
					showRinnaiDialog(EmployeeMenu03Activity.this, getString(R.string.msg_title_noti), getString(R.string.common_use_network));
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {

		int id = v.getId();

		Log.d("focus change", "boolean: " +  hasFocus);
	}
}
