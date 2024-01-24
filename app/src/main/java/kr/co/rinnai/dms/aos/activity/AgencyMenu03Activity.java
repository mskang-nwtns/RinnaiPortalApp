package kr.co.rinnai.dms.aos.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu03DeliveryListAdapter;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu03ListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu03AddressInfo;
import kr.co.rinnai.dms.aos.model.AgencyMenu03DeliveryInfo;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.aos.model.AgencyMenu03DeliveryListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu03ListEntity;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 *  Agency Operating System(영업 시스템, 대리점) 배송 조회
 */
public class AgencyMenu03Activity extends BaseActivity implements AdapterView.OnItemClickListener {

	private String searchDate;

	private TextView tvDate;
	private RelativeLayout rlDate;


	private boolean networkConnecting = false;

	private AgencyMenu03ListAdapter adapter;

	private AgencyMenu03DeliveryListAdapter deliveryAdaper;

	private ListView lvDelivery, lvDeliveryInfo;

	private RelativeLayout rlMenuCargo, rlMenuDistributes;

	private String nowView;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String custName, custCode;

	private TextView tvCustName, tvCustAddress;

	private RinnaiCalendarDialog rinnaiReceivedProductDialog;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agency_menu_03);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		rlDate = (RelativeLayout)findViewById(R.id.rl_agency_03_date);
		tvDate = (TextView) findViewById(R.id.tv_agency_03_order_date);
		lvDelivery = (ListView) findViewById(R.id.lv_agency_03_delivery);
		lvDeliveryInfo = (ListView) findViewById(R.id.lv_agency_03_delivery_info);

		tvCustName = (TextView) findViewById(R.id.tv_agency_03_activity_cust_name);
		tvCustAddress = (TextView) findViewById(R.id.tv_agency_03_activity_cust_address);
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

		rlMenuCargo = (RelativeLayout) findViewById(R.id.rl_agency_03_top_menu_cargo);

		rlMenuDistributes = (RelativeLayout)findViewById(R.id.rl_agency_03_top_menu_distributes);

		Date time = new Date();

		searchDate = format1.format(time);

		tvDate.setText(searchDate);

		nowView = "distributes";

		helper = new MySQLiteOpenHelper(
				AgencyMenu03Activity.this,  // 현재 화면의 제어권자
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

			custCode = c.getString(1);
			custName = c.getString(2);



		}

		if(custCode.length() == 7) {
			custCode = custCode.substring(2, 7);
		}

		getDistributes();
		rlMenuDistributes.setBackgroundResource(R.drawable.release_top_bg_select);
		rlMenuCargo.setBackgroundResource(R.drawable.release_top_bg_disable);

		lvDelivery.setOnItemClickListener(AgencyMenu03Activity.this);

		rlMenuCargo.setOnClickListener(AgencyMenu03Activity.this);
		rlMenuDistributes.setOnClickListener(AgencyMenu03Activity.this);
		rlDate.setOnClickListener(AgencyMenu03Activity.this);

	}

	private void getCargo() {
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(AgencyMenu03Activity.this);

			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_CARGO, searchDate.replace("/","-"), custCode);

			lvDeliveryInfo.setVisibility(View.INVISIBLE);
			lvDelivery.setVisibility(View.INVISIBLE);

			showProgress(AgencyMenu03Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}

	private void getDistributes() {
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(AgencyMenu03Activity.this);

			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_DISTRIBUTES, searchDate.replace("/","-"), custCode);

			showProgress(AgencyMenu03Activity.this);

			networkConnecting = true;
			lvDeliveryInfo.setVisibility(View.INVISIBLE);
			lvDelivery.setVisibility(View.INVISIBLE);

			HttpClient.get(url, this);
		}
	}

	private void getDeliveryInfo(AgencyMenu03ListEntity entity) {
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(AgencyMenu03Activity.this);

			url = String.format("%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_DELIVERY, entity.getSaleDate(), entity.getCustCode(), entity.getTrsNo());

			showProgress(AgencyMenu03Activity.this);


			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}

	@Override
	public void onResult(String result) {
		//super.onResult(result);
		Log.w("onResult", result);
		networkConnecting = false;
		dismissProgress();
		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
		if (null != responseData) {
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);

				Type type = null;

				if ("getDistributes".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<AgencyMenu03ListEntity>>(){}.getType();

					List<AgencyMenu03ListEntity> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu03ListAdapter(AgencyMenu03Activity.this, list);
					lvDelivery.setAdapter(adapter);
					adapter.notifyDataSetChanged();

					lvDelivery.setVisibility(View.VISIBLE);
				} else if("getCargo".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<AgencyMenu03ListEntity>>(){}.getType();

					List<AgencyMenu03ListEntity> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu03ListAdapter(AgencyMenu03Activity.this, list);
					lvDelivery.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					lvDelivery.setVisibility(View.VISIBLE);


				} else if("getDeliveryInfo".equals(responseData.getResultType())) {

					type = new TypeToken<AgencyMenu03DeliveryInfo>(){}.getType();

					AgencyMenu03DeliveryInfo info = new Gson().fromJson(str, type);
					AgencyMenu03AddressInfo address = info.getAddress();
					List<AgencyMenu03DeliveryListEntity> list = info.getDelivery();

					deliveryAdaper = new AgencyMenu03DeliveryListAdapter(AgencyMenu03Activity.this, list);
					lvDeliveryInfo.setAdapter(deliveryAdaper);
					deliveryAdaper.notifyDataSetChanged();

					tvCustName.setText(address.getCustName());
					tvCustAddress.setText(address.getDestAddress());
					lvDeliveryInfo.setVisibility(View.VISIBLE);

				}
			} else {

				showRinnaiDialog(AgencyMenu03Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AgencyMenu03ListEntity entity = (AgencyMenu03ListEntity)adapter.getItem(position);

		getDeliveryInfo(entity);

	}

	@Override
	public void onClick(View v) {
		//super.onClick(v);
		int id = v.getId();

		if (id == rlMenuCargo.getId()) {
			nowView = "cargo";
			rlMenuDistributes.setBackgroundResource(R.drawable.release_top_bg_disable);
			rlMenuCargo.setBackgroundResource(R.drawable.release_top_bg_select);

			getCargo();


		} else if (id == rlMenuDistributes.getId()) {
			nowView = "distributes";
//			onClearSelectedList();
			rlMenuDistributes.setBackgroundResource(R.drawable.release_top_bg_select);
			rlMenuCargo.setBackgroundResource(R.drawable.release_top_bg_disable);

			getDistributes();


		} else if (id == rlDate.getId()) {
			if(!networkConnecting) {
				if(rinnaiReceivedProductDialog == null) {
					rinnaiReceivedProductDialog = new RinnaiCalendarDialog(AgencyMenu03Activity.this);
				}
				if(!rinnaiReceivedProductDialog.isShowing()) {
					rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
					rinnaiReceivedProductDialog.setCancelable(false);

					rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
						@Override
						public void onDateChange(String date) {
							searchDate = date;
							tvDate.setText(searchDate);
							if ("cargo".equals(nowView)) {
								getCargo();
							} else if ("distributes".equals(nowView)) {
								getDistributes();
							}


						}  // MyDialogListener 를 구현

						@Override
						public void onCalendarView() {

						}

					});

					rinnaiReceivedProductDialog.show();
				}
			}
		}
	}

}
