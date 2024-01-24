package kr.co.rinnai.dms.aos.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.adapter.CategorySpinnerAdapter;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu5ListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu05WareHouseStockInfo;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.Categorizaion;
import kr.co.rinnai.dms.common.http.model.CategorizationResultVO;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 * 대리점용 직원</br>
 * Agency  Operating System(영업 시스템, 대리점) 오더 재고
 */

public class SalespersonMenu01ActivityNew extends BaseActivity  implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

	private boolean networkConnecting = false;

	private Categorizaion categori;

	private Spinner spMaster, spSub;

	private CategorySpinnerAdapter masterAdapter, subAdapter;

	private ListView lvStockOrderable;
	private AgencyMenu5ListAdapter adapter;

	private TextView tvDate;
	private TextView tvStockInchon, tvStockDaejeon, tvStockGwangju, tvStockGyeongsan, tvStockBusan, tvOrderStockQ, tvNOutQty;
//	private TextView tvStockGajwa;


	private RelativeLayout btnCalendar;

	private String searchDate;

	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private RinnaiCalendarDialog rinnaiReceivedProductDialog;

	private boolean firstExecute = false;

	private TextView tvWatermark1,tvWatermark2,tvWatermark3;

	private String saveId;
	private String userId;
	private String userName;
	private String deptName;

	private RelativeLayout rlPhoneNumber1;


	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesperson_menu_01_new);

//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		lvStockOrderable = (ListView) findViewById(R.id.lv_stock_orderable);

		tvStockInchon = (TextView) findViewById(R.id.tv_agency_05_activity_stock_warehouse_inchon);
//		tvStockGajwa = (TextView) findViewById(R.id.tv_agency_05_activity_stock_warehouse_gajwa);
		tvStockDaejeon = (TextView) findViewById(R.id.tv_agency_05_activity_stock_warehouse_daejeon);
		tvStockGwangju = (TextView) findViewById(R.id.tv_agency_05_activity_stock_warehouse_gwangju);
		tvStockGyeongsan = (TextView) findViewById(R.id.tv_agency_05_activity_stock_warehouse_gyeongsan);
		tvStockBusan = (TextView) findViewById(R.id.tv_agency_05_activity_stock_warehouse_busan);
//		tvOrderStockQ = (TextView) findViewById(R.id.tv_agency_05_activity_stock_warehouse_order_stock_q);
		tvNOutQty = (TextView) findViewById(R.id.tv_agency_05_activity_stock_warehouse_n_out_qty);

		tvWatermark1 = (TextView) findViewById(R.id.tv_salesperson_01_water_mark_01);
		tvWatermark2 = (TextView) findViewById(R.id.tv_salesperson_01_water_mark_02);
		tvWatermark3 = (TextView) findViewById(R.id.tv_salesperson_01_water_mark_03);

		rlPhoneNumber1 = (RelativeLayout) findViewById(R.id.rl_salesperson_01_phonenubmer_01);

		getModelCategory();

		spMaster = (Spinner) findViewById(R.id.sp_master);
		spSub = (Spinner) findViewById(R.id.sp_sub);

		spMaster.setOnItemSelectedListener(SalespersonMenu01ActivityNew.this);
		spSub.setOnItemSelectedListener(SalespersonMenu01ActivityNew.this);

		lvStockOrderable.setOnItemClickListener(SalespersonMenu01ActivityNew.this);

		btnCalendar = (RelativeLayout) findViewById(R.id.rl_agency_05_date);
        btnCalendar.setOnClickListener(SalespersonMenu01ActivityNew.this);

        tvDate = (TextView) findViewById(R.id.tv_agency_05_sale_date);

        Date time = new Date();

        searchDate = format1.format(time);

		tvDate.setText(searchDate);

		helper = new MySQLiteOpenHelper(
				SalespersonMenu01ActivityNew.this,  // 현재 화면의 제어권자
				CommonValue.SQLITE_DB_FILE_NAME,// db 이름
				null,  // 커서팩토리-null : 표준커서가 사용됨
				CommonValue.SQLITE_DB_VERSION);       // 버전

		try {

			db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
			//db = helper.getReadableDatabase(); // 읽기 전용 DB select문
		} catch (SQLiteException e) {

		}

		String selectQuery = String.format("SELECT %s, %s, %s, %s  FROM %s ;",
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NAME,
				CommonValue.SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_NAME,
				CommonValue.SQLITE_DB_TABLE_NAME_LOGIN_INFO);

		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		int count = c.getCount();

		if(count > 0 ) {
			saveId = c.getString(0);
			userId = c.getString(1);
			userName = c.getString(2);
			deptName = c.getString(3);

		}

		rlPhoneNumber1.setOnClickListener(SalespersonMenu01ActivityNew.this);

		tvWatermark1.setText(String.format("%s %s %s %s %s %s %s %s %s %s", deptName, userName, deptName, userName, deptName, userName, deptName, userName, deptName, userName ));
		tvWatermark2.setText(String.format("%s %s %s %s %s %s %s %s %s %s", userName, deptName, userName, deptName, userName, deptName, userName, deptName, userName, deptName ));
		tvWatermark3.setText(String.format("%s %s %s %s %s %s %s %s %s %s", deptName, userName, deptName, userName, deptName, userName, deptName, userName, deptName, userName ));


	}


	/**
	 * 매장 재고 조회
	 *
	 */
	private void getModelCategory() {
//		String value =  etSearchValue.getText().toString();
		//    /stock/{date}/{type}/{value}
		if(!networkConnecting) {

			String url = String.format("%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_CATEGORY, CommonValue.HTTP_VERSION_2);

			showProgress(SalespersonMenu01ActivityNew.this);

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

		tvStockInchon.setText("");
//		tvStockGajwa.setText("");
		tvStockDaejeon.setText("");
		tvStockGwangju.setText("");
		tvStockGyeongsan.setText("");
		tvStockBusan.setText("");
//		tvOrderStockQ.setText("");
		tvNOutQty.setText("");

		if (null != responseData) {
			Log.w("onResult", "ok");
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);
				Type type = null;
				if ("getCategorization".equals(responseData.getResultType())) {
					lvStockOrderable.setVisibility(View.INVISIBLE);
					type = new TypeToken<Categorizaion>() {}.getType();

					categori = new Gson().fromJson(str, type);

					masterAdapter = new CategorySpinnerAdapter(SalespersonMenu01ActivityNew.this, categori.getMaster());
					spMaster.setAdapter(masterAdapter);
					masterAdapter.notifyDataSetChanged();

					subAdapter = new CategorySpinnerAdapter(SalespersonMenu01ActivityNew.this, categori.getSub().get("-1"));

					spSub.setAdapter(subAdapter);
					subAdapter.notifyDataSetChanged();

					search();
/*
				} else if("getStockByWarehouse".equals(responseData.getResultType())) {
					lvStockOrderable.setVisibility(View.INVISIBLE);
					type = new TypeToken<ArrayList<StockByOrderableResult>>(){}.getType();

					List<StockByOrderableResult> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu5ListAdapter(AgencyMenu05Activity.this, list);

					lvStockOrderable.setAdapter(adapter);
					lvStockOrderable.setVisibility(View.VISIBLE);

					for(int i = 0; i <list.size(); i ++) {

						list.get(i).setSelected(false);

					}
					*/

					/*
					allStock = 0;
					localStock = 0;
					moveStock = 0;
					outStock = 0;
					thisOutStock = 0;
					saleSumStock = 0;
					for(int i = 0; i <list.size(); i ++) {

						StockByOrderableResult stock = list.get(i);

						allStock = stock.getAllRStockQ();
						localStock = stock.getLocalRStockQ();
						moveStock = stock.getMoveRStockQ();
						outStock = stock.getnOutQ();
						thisOutStock = stock.getThisOutQ();
						saleSumStock = stock.getSalSumQ();

					}


					tvAllStock.setText(String.format("%d", allStock));
					tvLocalStock.setText(String.format("%d", localStock));
					tvMoveStock.setText(String.format("%d", moveStock));
					tvOutStock.setText(String.format("%d", outStock));
					tvThisOutStock.setText(String.format("%d", thisOutStock));
					tvSaleSumStock.setText(String.format("%d", saleSumStock));

					*/

				} else if ("getProductStockByWarehouse".equals(responseData.getResultType())) {

					lvStockOrderable.setVisibility(View.INVISIBLE);
					type = new TypeToken<ArrayList<AgencyMenu05WareHouseStockInfo>>(){}.getType();

					List<AgencyMenu05WareHouseStockInfo> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu5ListAdapter(SalespersonMenu01ActivityNew.this, list);

					lvStockOrderable.setAdapter(adapter);
					lvStockOrderable.setVisibility(View.VISIBLE);

					for(int i = 0; i <list.size(); i ++) {

						list.get(i).setSelected(false);

					}


//					type = new TypeToken<AgencyMenu05WareHouseStockInfo>(){}.getType();
//
//					AgencyMenu05WareHouseStockInfo info = new Gson().fromJson(str, type);
//
//					tvStockInchon.setText(String.format("%,d", info.getInchon()));
//					tvStockGajwa.setText(String.format("%,d", info.getGajwa()));
//					tvStockDaejeon.setText(String.format("%,d", info.getDaejeon()));
//					tvStockGwangju.setText(String.format("%,d", info.getGwangju()));
//					tvStockGyeongsan.setText(String.format("%,d", info.getGyeongsan()));
//					tvStockBusan.setText(String.format("%,d", info.getBusan()));
//
//					tvStockTotal.setText(String.format("%,d", info.getInchon() + info.getGajwa() + info.getDaejeon() + info.getGwangju() + info.getGyeongsan() + info.getBusan()));

				}
			} else {
				showRinnaiDialog(SalespersonMenu01ActivityNew.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(parent == spMaster) {
			CategorizationResultVO result = categori.getMaster().get(position);
			List<CategorizationResultVO> list = new ArrayList<CategorizationResultVO>();
			list.addAll(categori.getSub().get("-1"));

			if(!"-1".equals(result.getCodeItem())) {
				list.addAll(categori.getSub().get(result.getCodeItem()));
			}
			subAdapter = new CategorySpinnerAdapter(SalespersonMenu01ActivityNew.this, list);

			spSub.setAdapter(subAdapter);
			subAdapter.notifyDataSetChanged();
			lvStockOrderable.setVisibility(View.INVISIBLE);
			if(!firstExecute) {
				firstExecute = true;
			} else if (firstExecute) {
				search();
			}
		} else if (parent == spSub) {

            search();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(parent == lvStockOrderable) {

			AgencyMenu05WareHouseStockInfo stock = (AgencyMenu05WareHouseStockInfo)adapter.getItem(position);

			adapter.setSelectItem(position);

			/*
			allStock = stock.getAllRStockQ();
			localStock = stock.getLocalRStockQ();
			moveStock = stock.getMoveRStockQ();
			outStock = stock.getnOutQ();
			thisOutStock = stock.getThisOutQ();
			saleSumStock = stock.getSalSumQ();

			tvAllStock.setText(String.format("%,d", allStock));
			tvLocalStock.setText(String.format("%,d", localStock));
			tvMoveStock.setText(String.format("%,d", moveStock));
			tvOutStock.setText(String.format("%,d", outStock));
			tvThisOutStock.setText(String.format("%,d", thisOutStock));
			tvSaleSumStock.setText(String.format("%,d", saleSumStock));
 */

			adapter.notifyDataSetChanged();
/*
			CategorizationResultVO master = (CategorizationResultVO) spMaster.getSelectedItem();
			String masterCode = master.getCodeItem();

			if(!networkConnecting && !"-1".equals(masterCode) ) {

				String tmpDate = searchDate.replace("/","");
//			    searchDate.replace("")

				String url = String.format("%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_STOCK, tmpDate, stock.getItem());

				showProgress(AgencyMenu05Activity.this);

				networkConnecting = true;
				HttpClient.get(url, this);

			}*/

			tvStockInchon.setText(String.format("%,d", stock.getInchon()));
//			tvStockGajwa.setText(String.format("%,d", stock.getGajwa()));
			tvStockDaejeon.setText(String.format("%,d", stock.getDaejeon()));
			tvStockGwangju.setText(String.format("%,d", stock.getGwangju()));
			tvStockGyeongsan.setText(String.format("%,d", stock.getGyeongsan()));
			tvStockBusan.setText(String.format("%,d", stock.getBusan()));
//			tvOrderStockQ.setText(String.format("%,d", stock.getOrderStockQ()));
			tvNOutQty.setText(String.format("%,d", stock.getnOutQty()));
//
		}
	}

    @Override
    public void onClick(View v) {


		if(v.getId() == rlPhoneNumber1.getId()) {
			Intent tt = null;

			tt = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:1588-8292"));


			if(tt != null) {
				startActivity(tt);
			}
		} else {
			if (rinnaiReceivedProductDialog == null) {
				rinnaiReceivedProductDialog = new RinnaiCalendarDialog(SalespersonMenu01ActivityNew.this);
			}

			if (!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {
						searchDate = date;
						tvDate.setText(searchDate);

						search();
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});
				rinnaiReceivedProductDialog.show();
			}
		}


    }


    private void search () {

        CategorizationResultVO master = (CategorizationResultVO) spMaster.getSelectedItem();
        CategorizationResultVO sub = (CategorizationResultVO) spSub.getSelectedItem();
        String masterCode = master.getCodeItem();
        String subCode = sub.getCodeItem();
        lvStockOrderable.setVisibility(View.INVISIBLE);
        if(!networkConnecting && !"-1".equals(masterCode) ) {

            String tmpDate = searchDate.replace("/","");
//			    searchDate.replace("")

            String url = String.format("%s/%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_VERSION_2, tmpDate, masterCode, subCode);

            showProgress(SalespersonMenu01ActivityNew.this);

            networkConnecting = true;
            HttpClient.get(url, this);

        }
    }
}
