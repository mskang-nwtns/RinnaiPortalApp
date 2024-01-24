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
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.adapter.SimpleSpinnerAdapter;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu04ListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu04SalesInfoTranEntity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.custom.RinnaiSalesInforTranDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.aos.model.AgencyMenu04ListEntity;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.listener.ServiceDialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.eos.model.ItemCode;


/**
 *  Agency Operating System(영업 시스템, 대리점) 판매정보 이관
 */
public class AgencyMenu04Activity extends BaseActivity implements ListView.OnItemClickListener, AdapterView.OnItemSelectedListener {

	private Spinner spSalesStatus, spCusSms, spTransType;

	private RelativeLayout rlStartOrder, rlEndOrder;

	private TextView tvStartOrder, tvEndOrder;

	private SimpleSpinnerAdapter adapterSalesStatus, adapterCusSms, adapterTransType;

	private List arrayList = new ArrayList<>();

	private boolean networkConnecting = false;

	private AgencyMenu04ListAdapter adapter;

	private ListView lvinfo;

	private TextView engineerName, telNo, sTelNo, tvCustName, addr, zipCode;

	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String custName, custCode;

	private boolean isFirstSalesStart = true;
	private boolean isFirstCusStart = true;
	private boolean isFirstTransStart = true;

	private RinnaiCalendarDialog rinnaiReceivedProductDialog;

	private RelativeLayout btnSearch;



/*
	arrayList.add("전체");
	arrayList.add("양품");
	arrayList.add("불량");

	*/

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agency_menu_04);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		spSalesStatus = (Spinner) findViewById(R.id.sp_agency_04_activity_sales_status);
		spCusSms = (Spinner) findViewById(R.id.sp_agency_04_activity_cus_sms);
		spTransType = (Spinner) findViewById(R.id.sp_agency_04_activity_trans_type);

		tvStartOrder = (TextView) findViewById(R.id.tv_agency_04_order_date);
		tvEndOrder = (TextView) findViewById(R.id.tv_agency_04_order_end_date);

		rlStartOrder = (RelativeLayout) findViewById(R.id.rl_agency_04_start_order_date);
		rlStartOrder.setOnClickListener(AgencyMenu04Activity.this);

		rlEndOrder = (RelativeLayout) findViewById(R.id.rl_agency_04_end_order_date);
		rlEndOrder.setOnClickListener(AgencyMenu04Activity.this);

		engineerName = (TextView) findViewById(R.id.tv_agency_04_activity_engineer_name);
		telNo = (TextView) findViewById(R.id.tv_agency_04_activity_tel_no);
		sTelNo = (TextView) findViewById(R.id.tv_agency_04_activity_s_tel_no);
		tvCustName = (TextView) findViewById(R.id.tv_agency_04_activity_cust_name);
		addr = (TextView) findViewById(R.id.tv_agency_04_activity_addr);
		zipCode = (TextView) findViewById(R.id.tv_agency_04_activity_zip_code);

		btnSearch = (RelativeLayout)findViewById(R.id.btn_aos05_search);

		List<String> listSales = new ArrayList<String>();
		List<String> listCus = new ArrayList<String>();
		List<String> listTrans = new ArrayList<String>();

		listSales.add("전체");
		listSales.add("판매");
		listSales.add("미판매");

		listCus.add("전체");
		listCus.add("조치");
		listCus.add("미조치");

		listTrans.add("전체");
		listTrans.add("해당없음");
		listTrans.add("이관");
		listTrans.add("권장");

		adapterSalesStatus = new SimpleSpinnerAdapter(getApplicationContext(), listSales);
		adapterCusSms = new SimpleSpinnerAdapter(getApplicationContext(), listCus);
		adapterTransType = new SimpleSpinnerAdapter(getApplicationContext(), listTrans);

		spSalesStatus.setAdapter(adapterSalesStatus);
		spCusSms.setAdapter(adapterCusSms);
		spTransType.setAdapter(adapterTransType);

		spSalesStatus.setOnItemSelectedListener(AgencyMenu04Activity.this);
		spCusSms.setOnItemSelectedListener(AgencyMenu04Activity.this);
		spTransType.setOnItemSelectedListener(AgencyMenu04Activity.this);

		btnSearch.setOnClickListener(AgencyMenu04Activity.this);

		lvinfo = (ListView) findViewById(R.id.lv_agency_04_transfer_sale_infomation);

		lvinfo.setOnItemClickListener(AgencyMenu04Activity.this);

		Date time = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);

		String eDate = format1.format(cal.getTime());

		cal.add(Calendar.DATE, -7);
		String sDate = format1.format(cal.getTime());

		tvStartOrder.setText(sDate);
		tvEndOrder.setText(eDate);

		helper = new MySQLiteOpenHelper(
				AgencyMenu04Activity.this,  // 현재 화면의 제어권자
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

		getTransferSaleInfomation();




	}

	private void getTransferSaleInfomation() {
		if(!networkConnecting) {
			adapter = null;
			engineerName.setText("");
			telNo.setText("");
			sTelNo.setText("");
			tvCustName.setText("");
			addr.setText("");
			zipCode.setText("");

			int saleStatus = (Integer)spSalesStatus.getSelectedItemPosition();
			int cusSms = (Integer)spCusSms.getSelectedItemPosition();
			int transType = (Integer)spTransType.getSelectedItemPosition();

			String strSale = getSelectedType(saleStatus, null); //Y/N
			String strCus = getSelectedType(cusSms, null);      //Y/N
			String strTrans = getSelectedType(transType, ""); //1/2

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(AgencyMenu04Activity.this);

			String sDate = tvStartOrder.getText().toString().replace("/", "-");
			String eDate = tvEndOrder.getText().toString().replace("/", "-");



			int iSDate = Integer.parseInt(sDate.replaceAll("[^0-9]",""));
			int iEDate = Integer.parseInt(eDate.replaceAll("[^0-9]",""));

			if(iSDate> iEDate) {

				showRinnaiDialog(AgencyMenu04Activity.this, getString(R.string.msg_title_noti), "조회 시작일이 조회 종료일보다 느립니다.");
				return;

			}

			url = String.format("%s/%s/%s/%s/%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_TRANSFER_SALE_INFOMATION, custCode,  sDate, eDate, strSale, strCus, strTrans);

			showProgress(AgencyMenu04Activity.this);

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

				if ("getTransferSaleInfomation".equals(responseData.getResultType())) {


					type = new TypeToken<ArrayList<AgencyMenu04ListEntity>>(){}.getType();

					List<AgencyMenu04ListEntity> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu04ListAdapter(AgencyMenu04Activity.this, list);
					lvinfo.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					lvinfo.setVisibility(View.VISIBLE);

				}
			} else {
				lvinfo.setVisibility(View.INVISIBLE);
				showRinnaiDialog(AgencyMenu04Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());


			}
		}

		//getSalesProgress
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AgencyMenu04ListEntity entity = (AgencyMenu04ListEntity)adapter.getItem(position);

		engineerName.setText(entity.getEngineer());
		telNo.setText(entity.getTelNo());
		sTelNo.setText(entity.getsTelNo());
		tvCustName.setText(entity.getCustName());
		addr.setText(entity.getAddr());
		zipCode.setText(entity.getZipCode());

		adapter.setSelectItem(position);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//		spSalesStatus, spCusSms, spTransType



		if(isFirstSalesStart) {
			isFirstSalesStart = false;
		} else if (isFirstCusStart) {
			isFirstCusStart = false;
		} else if (isFirstTransStart) {
			isFirstTransStart = false;

		} else {
			if (parent == spSalesStatus) {
				getTransferSaleInfomation();
			} else if (parent == spCusSms) {
				getTransferSaleInfomation();
			} else if (parent == spTransType) {
				getTransferSaleInfomation();
			}
		}

		/*
		if(parent == spMaster) {
			CategorizationResultVO result = categori.getMaster().get(position);
			List<CategorizationResultVO> list = new ArrayList<CategorizationResultVO>();
			list.addAll(categori.getSub().get("-1"));
			if(!"-1".equals(result.getCodeItem())) {
				list.addAll(categori.getSub().get(result.getCodeItem()));
			}
			subAdapter = new CategorySpinnerAdapter(AgencyMenu05Activity.this, list);

			spSub.setAdapter(subAdapter);
			subAdapter.notifyDataSetChanged();
			lvStockOrderable.setVisibility(View.INVISIBLE);
		} else if (parent == spSub) {

			search();
		}

		*/

	}

	private String getSelectedType(int position, String type) {
		String value = "-1";
		if(type == null) {
			if (position == 0) {
				value = "-1";
			} else if (position == 1) {
				value = "Y";
			} else if (position == 2) {
				value = "N";
			}
		} else if ("".equals(type)) {
			if (position == 0) {
				value = "-1";
			} else if (position == 1) {
				value = "0";
			} else if (position == 2) {
				value = "1";
			}  else if (position == 3) {
				value = "2";
			}
		}

		return value;

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if(rinnaiReceivedProductDialog == null) {
			rinnaiReceivedProductDialog = new RinnaiCalendarDialog(AgencyMenu04Activity.this);
		}

		if (id == rlStartOrder.getId()) {
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {

						String sDate = date.replace("/", "-");
						String eDate = tvEndOrder.getText().toString().replace("/", "-");



						int iSDate = Integer.parseInt(sDate.replaceAll("[^0-9]",""));
						int iEDate = Integer.parseInt(eDate.replaceAll("[^0-9]",""));

						if(iSDate> iEDate) {

							showRinnaiDialog(AgencyMenu04Activity.this, getString(R.string.msg_title_noti), "조회 시작일이 조회 종료일보다 느립니다.");
							return;

						}

						tvStartOrder.setText(date);
						getTransferSaleInfomation();
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});

				rinnaiReceivedProductDialog.show();
			}
		} else if(id == rlEndOrder.getId()) {
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {
						String sDate = tvStartOrder.getText().toString().replace("/", "-");
						String eDate = date.toString().replace("/", "-");



						int iSDate = Integer.parseInt(sDate.replaceAll("[^0-9]",""));
						int iEDate = Integer.parseInt(eDate.replaceAll("[^0-9]",""));

						if(iSDate> iEDate) {

							showRinnaiDialog(AgencyMenu04Activity.this, getString(R.string.msg_title_noti), "조회 시작일이 조회 종료일보다 느립니다.");
							return;

						}

						tvEndOrder.setText(date);
						getTransferSaleInfomation();
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});
				rinnaiReceivedProductDialog.show();
			}
		} else if (id == btnSearch.getId()) {
			if(adapter!= null && adapter.getSelectItem() != null) {
				AgencyMenu04ListEntity entity = (AgencyMenu04ListEntity) adapter.getSelectItem();
				showListPopup(entity);
			} else {
				showRinnaiDialog(AgencyMenu04Activity.this, getString(R.string.msg_title_noti), "등록하실 항목을 선택해주세요.");
			}
		}
	}

	private void showListPopup(AgencyMenu04ListEntity obj) {
		RinnaiSalesInforTranDialog rinnaiDialog;
		rinnaiDialog = new RinnaiSalesInforTranDialog(AgencyMenu04Activity.this, obj, CommonValue.AOS_NOW_VIEW_NAME_MODEL_05);
		rinnaiDialog.setCanceledOnTouchOutside(false);
		rinnaiDialog.setCancelable(false);
		rinnaiDialog.setDialogListener(new ServiceDialogListener() {
			@Override
			public void onPositiveClicked(String type, Object obj) {

				AgencyMenu04SalesInfoTranEntity entity = (AgencyMenu04SalesInfoTranEntity) obj;
				if("insert".equals(type)) {

					int position =  adapter.getItemPosition(entity.getAsSeq());

					AgencyMenu04ListEntity listEntity  = (AgencyMenu04ListEntity)adapter.getItem(position);

					listEntity.setCusSmsYn(entity.getCusSmsYn());
					listEntity.setCusActDate(entity.getCusActDate());
					listEntity.setCusSaleDate(entity.getCusSaleDate());
					listEntity.setModelName(entity.getModelName());
					listEntity.setCusSaleYn(entity.getCusSaleYn());
					listEntity.setCusRef(entity.getCusRef());
                    listEntity.setSaleModelNo(entity.getModelCode());

				}
				adapter.notifyDataSetChanged();
			}
		});
		rinnaiDialog.show();

	}
}
