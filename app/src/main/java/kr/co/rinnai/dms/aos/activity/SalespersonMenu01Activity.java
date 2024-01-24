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
import kr.co.rinnai.dms.aos.adapter.SalespersonMenu01ListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu05WareHouseStockInfo;
import kr.co.rinnai.dms.aos.model.SalespersonMenu01WareHouseStockInfo;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.AgencyInfoVO;
import kr.co.rinnai.dms.common.http.model.Categorizaion;
import kr.co.rinnai.dms.common.http.model.CategorizationResultVO;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 * 양판점 판매 직원 용</br>
 * Agency  Operating System(영업 시스템, 대리점) 오더 재고
 */

public class SalespersonMenu01Activity extends BaseActivity  implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

	private boolean networkConnecting = false;

	private Categorizaion categori;

	private Spinner spMaster, spSub;

	private CategorySpinnerAdapter masterAdapter, subAdapter;

	private ListView lvStockOrderable;
	private SalespersonMenu01ListAdapter adapter;

	private TextView tvDate;

	private String searchDate;

	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private boolean firstExecute = false;

	private RelativeLayout rlPhoneNumber1, rlPhoneNumber2;

	private TextView tvPhoneNumber1, tvPhoneNumber2;
	private List<AgencyInfoVO> info = null;
	private boolean isFirstSearch = true;

	private String saveId;
	private String userId;
	private String userName;
	private String deptName;

	private TextView tvWatermark1,tvWatermark2,tvWatermark3;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_salesperson_menu_01);

//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		lvStockOrderable = (ListView) findViewById(R.id.lv_stock_orderable);

		getModelCategory();

		spMaster = (Spinner) findViewById(R.id.sp_master);
		spSub = (Spinner) findViewById(R.id.sp_sub);

		spMaster.setOnItemSelectedListener(SalespersonMenu01Activity.this);
		spSub.setOnItemSelectedListener(SalespersonMenu01Activity.this);


        tvDate = (TextView) findViewById(R.id.tv_salesperson_01_sale_date);

		rlPhoneNumber1 = (RelativeLayout) findViewById(R.id.rl_salesperson_01_phonenubmer_01);
		tvPhoneNumber1 = (TextView) findViewById(R.id.tv_salesperson_01_phonenubmer_01);
		rlPhoneNumber2 = (RelativeLayout) findViewById(R.id.rl_salesperson_01_phonenubmer_02);
		tvPhoneNumber2 = (TextView) findViewById(R.id.tv_salesperson_01_phonenubmer_02);

		tvWatermark1 = (TextView) findViewById(R.id.tv_salesperson_01_water_mark_01);
		tvWatermark2 = (TextView) findViewById(R.id.tv_salesperson_01_water_mark_02);
		tvWatermark3 = (TextView) findViewById(R.id.tv_salesperson_01_water_mark_03);

        Date time = new Date();

        searchDate = format1.format(time);

		tvDate.setText(searchDate);

		helper = new MySQLiteOpenHelper(
				SalespersonMenu01Activity.this,  // 현재 화면의 제어권자
				CommonValue.SQLITE_DB_FILE_NAME,// db 이름
				null,  // 커서팩토리-null : 표준커서가 사용됨
				CommonValue.SQLITE_DB_VERSION);       // 버전

		try {

			db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
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

		rlPhoneNumber1.setOnClickListener(SalespersonMenu01Activity.this);
		rlPhoneNumber2.setOnClickListener(SalespersonMenu01Activity.this);
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

			String url = String.format("%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_OHTER, CommonValue.HTTP_CATEGORY);

			showProgress(SalespersonMenu01Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}

	}


	@Override
	public void onResult(String result) {

		super.onResult(result);
		Log.w("onResult", result);

		if(!isFirstSearch) {
			dismissProgress();
		}

		networkConnecting = false;

		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);

		if (null != responseData) {
			Log.w("onResult", "ok");
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);
				Type type = null;
				if ("getCategorization".equals(responseData.getResultType())) {
//					lvStockOrderable.setVisibility(View.INVISIBLE);
					type = new TypeToken<Categorizaion>() {}.getType();

					categori = new Gson().fromJson(str, type);

					masterAdapter = new CategorySpinnerAdapter(SalespersonMenu01Activity.this, categori.getMaster());
					spMaster.setAdapter(masterAdapter);
					masterAdapter.notifyDataSetChanged();

					subAdapter = new CategorySpinnerAdapter(SalespersonMenu01Activity.this, categori.getSub().get("-1"));

					spSub.setAdapter(subAdapter);
					subAdapter.notifyDataSetChanged();


					info = categori.getAgencyInfo();
					String text1 = String.format("%s ☎ %s", info.get(0).getSiteName(), info.get(0).getTelNo());
					String text2 = String.format("%s ☎ %s", info.get(1).getSiteName(), info.get(1).getTelNo());
					tvPhoneNumber1.setText(text1);
					tvPhoneNumber2.setText(text2);



				} else if ("getProductByWarehouse".equals(responseData.getResultType())) {

					if(isFirstSearch) {
						dismissProgress();
						isFirstSearch = false;
					}
					lvStockOrderable.setVisibility(View.INVISIBLE);
					type = new TypeToken<ArrayList<SalespersonMenu01WareHouseStockInfo>>(){}.getType();

					List<SalespersonMenu01WareHouseStockInfo> list = new Gson().fromJson(str, type);

					adapter = new SalespersonMenu01ListAdapter(SalespersonMenu01Activity.this, list);

					lvStockOrderable.setAdapter(adapter);
					lvStockOrderable.setVisibility(View.VISIBLE);
					adapter.notifyDataSetChanged();


				}
			} else {
				showRinnaiDialog(SalespersonMenu01Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(parent == spMaster) {
			CategorizationResultVO result = categori.getMaster().get(position);
			List<CategorizationResultVO> list = new ArrayList<CategorizationResultVO>();
			CategorizationResultVO tmp = new CategorizationResultVO();
			tmp.setCodeItem("-1");
			tmp.setCodeName("전체");
			tmp.setcValue("-1");



			if(!"-1".equals(result.getCodeItem())) {
				list.add(tmp);
				list.addAll(categori.getSub().get(result.getCodeItem()));
			} else {
				list.addAll(categori.getSub().get(result.getCodeItem()));
			}
//
			subAdapter = new CategorySpinnerAdapter(SalespersonMenu01Activity.this, list);

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

//
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

    @Override
    public void onClick(View v) {

		int id = v.getId();
		Intent tt = null;
		if(info != null) {
			if (id == rlPhoneNumber1.getId()) {
				tt = new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel:%s", info.get(0).getTelNo())));
			} else if (id == rlPhoneNumber2.getId()) {
				tt = new Intent(Intent.ACTION_DIAL, Uri.parse(String.format("tel:%s", info.get(1).getTelNo())));
			}

		}
		if(tt != null) {
			startActivity(tt);
		}
    }


    private void search () {

        CategorizationResultVO master = (CategorizationResultVO) spMaster.getSelectedItem();
        CategorizationResultVO sub = (CategorizationResultVO) spSub.getSelectedItem();
        String masterCode = master.getCodeItem();
        String subCode = sub.getCodeItem();
        lvStockOrderable.setVisibility(View.INVISIBLE);
        if(!networkConnecting ) {

            String tmpDate = searchDate.replace("/","");//			    searchDate.replace("")


			String url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_PRODUCT, tmpDate, masterCode, subCode);
			if(!isFirstSearch) {
				showProgress(SalespersonMenu01Activity.this);
			}
			networkConnecting = true;

            HttpClient.get(url, this);

        }
    }

}
