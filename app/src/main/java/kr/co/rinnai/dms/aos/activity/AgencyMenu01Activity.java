package kr.co.rinnai.dms.aos.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu01ListAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.aos.model.AgencyMenu01ListEntity;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 *  Agency Operating System(영업 시스템, 대리점) 미출고 조회
 */
public class AgencyMenu01Activity extends BaseActivity {

	private ListView lvUnShipped;
	private TextView tvCustName;
	private AgencyMenu01ListAdapter adapter;

	private boolean networkConnecting;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String custName, custCode;



	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agency_menu_01);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		lvUnShipped = (ListView) findViewById(R.id.lv_unshipped);

		tvCustName = (TextView) findViewById(R.id.tv_agency_01_activity_cust_name);

		helper = new MySQLiteOpenHelper(
				AgencyMenu01Activity.this,  // 현재 화면의 제어권자
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

		tvCustName.setText(custName);
		getUnshipped();



	}


	private void getUnshipped() {
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(AgencyMenu01Activity.this);

			url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_UNSHIPPED, custCode);

			showProgress(AgencyMenu01Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}

	@Override
	public void onResult(String result) {
		//super.onResult(result);
		Log.w("onResult", result);

		dismissProgress();
		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
		if (null != responseData) {
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);

				Type type = null;

				if ("getUnShipped".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<AgencyMenu01ListEntity>>(){}.getType();

					List<AgencyMenu01ListEntity> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu01ListAdapter(AgencyMenu01Activity.this, list);
					lvUnShipped.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			} else {
				showRinnaiDialog(AgencyMenu01Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
	}


}
