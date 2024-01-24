package kr.co.rinnai.dms.eos.activity;

import android.content.pm.ActivityInfo;
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
import java.util.Date;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.aos.adapter.AosMenu2ListAdapter;
import kr.co.rinnai.dms.adapter.CategorySpinnerAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.Categorizaion;
import kr.co.rinnai.dms.common.http.model.CategorizationResultVO;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.model.StockByOrderableResult;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 * 영업본부 직원</br>
 * Employee Operating System(영업 시스템, 대리점) 오더 재고
 */

public class EmployeeMenu02Activity extends BaseActivity  implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

	private boolean networkConnecting = false;

	private Categorizaion categori;

	private Spinner spMaster, spSub;

	private CategorySpinnerAdapter masterAdapter, subAdapter;

	private ListView lvStockOrderable;
	private AosMenu2ListAdapter adapter;

	private TextView tvAllStock, tvLocalStock, tvMoveStock, tvOutStock, tvThisOutStock, tvSaleSumStock;
	private TextView tvDate;

	private int allStock, localStock, moveStock, outStock, thisOutStock, saleSumStock;

	private RelativeLayout btnCalendar;

	private String searchDate;

	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private RinnaiCalendarDialog rinnaiReceivedProductDialog;

	private boolean firstExecute = false;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_menu_02);

		boolean isTablet = Util.isTabletDevice(EmployeeMenu02Activity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}


		lvStockOrderable = (ListView) findViewById(R.id.lv_stock_orderable);
		tvAllStock = (TextView) findViewById(R.id.tv_all_stock);
		tvLocalStock = (TextView) findViewById(R.id.tv_local_stock);
		tvMoveStock = (TextView) findViewById(R.id.tv_move_stock);
		tvOutStock = (TextView) findViewById(R.id.tv_out_stock);
		tvThisOutStock = (TextView) findViewById(R.id.tv_this_out_stock);
		tvSaleSumStock = (TextView) findViewById(R.id.tv_sale_sum);

		getModelCategory();

		spMaster = (Spinner) findViewById(R.id.sp_master);
		spSub = (Spinner) findViewById(R.id.sp_sub);

		spMaster.setOnItemSelectedListener(EmployeeMenu02Activity.this);
		spSub.setOnItemSelectedListener(EmployeeMenu02Activity.this);

		lvStockOrderable.setOnItemClickListener(EmployeeMenu02Activity.this);

		btnCalendar = (RelativeLayout) findViewById(R.id.rl_employee_02_date);
        btnCalendar.setOnClickListener(EmployeeMenu02Activity.this);

        tvDate = (TextView) findViewById(R.id.tv_employee_02_sale_date);

        Date time = new Date();

        searchDate = format1.format(time);

		tvDate.setText(searchDate);

		helper = new MySQLiteOpenHelper(
				EmployeeMenu02Activity.this,  // 현재 화면의 제어권자
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
			String userName = c.getString(2);

		}

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


	/**
	 * 매장 재고 조회
	 *
	 */
	private void getModelCategory() {
//		String value =  etSearchValue.getText().toString();
		//    /stock/{date}/{type}/{value}
		if(!networkConnecting) {

			String url = String.format("%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_CATEGORY);

			showProgress(EmployeeMenu02Activity.this);

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

		tvAllStock.setText("");
		tvLocalStock.setText("");
		tvMoveStock.setText("");
		tvOutStock.setText("");
		tvThisOutStock.setText("");
		tvSaleSumStock.setText("");

		lvStockOrderable.setVisibility(View.INVISIBLE);
		if (null != responseData) {
			Log.w("onResult", "ok");
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);
				Type type = null;
				if ("getCategorization".equals(responseData.getResultType())) {
					type = new TypeToken<Categorizaion>() {}.getType();

					categori = new Gson().fromJson(str, type);

					masterAdapter = new CategorySpinnerAdapter(EmployeeMenu02Activity.this, categori.getMaster());
					spMaster.setAdapter(masterAdapter);
					masterAdapter.notifyDataSetChanged();

					subAdapter = new CategorySpinnerAdapter(EmployeeMenu02Activity.this, categori.getSub().get("-1"));

					spSub.setAdapter(subAdapter);
					subAdapter.notifyDataSetChanged();

					search();

				} else if("getStockByWarehouse".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<StockByOrderableResult>>(){}.getType();

					List<StockByOrderableResult> list = new Gson().fromJson(str, type);

					adapter = new AosMenu2ListAdapter(EmployeeMenu02Activity.this, list);

					lvStockOrderable.setAdapter(adapter);
					lvStockOrderable.setVisibility(View.VISIBLE);

					for(int i = 0; i <list.size(); i ++) {

						list.get(i).setSelected(false);

					}
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

				}
			} else {
				showRinnaiDialog(EmployeeMenu02Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
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
			subAdapter = new CategorySpinnerAdapter(EmployeeMenu02Activity.this, list);

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

			StockByOrderableResult stock = (StockByOrderableResult)adapter.getItem(position);

			adapter.setSelectItem(position);
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

			adapter.notifyDataSetChanged();
		}
	}

    @Override
    public void onClick(View v) {
        if(rinnaiReceivedProductDialog == null) {
			rinnaiReceivedProductDialog = new RinnaiCalendarDialog(EmployeeMenu02Activity.this);
		}

        if(!rinnaiReceivedProductDialog.isShowing()) {
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


    private void search () {

        CategorizationResultVO master = (CategorizationResultVO) spMaster.getSelectedItem();
        CategorizationResultVO sub = (CategorizationResultVO) spSub.getSelectedItem();
        String masterCode = master.getCodeItem();
        String subCode = sub.getCodeItem();
        lvStockOrderable.setVisibility(View.INVISIBLE);
        if(!networkConnecting && !"-1".equals(masterCode) ) {

            String tmpDate = searchDate.replace("/","");
//			    searchDate.replace("")

            String url = String.format("%s/%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, tmpDate, masterCode, subCode, "E");

            showProgress(EmployeeMenu02Activity.this);

            networkConnecting = true;
            HttpClient.get(url, this);

        }
    }
}
