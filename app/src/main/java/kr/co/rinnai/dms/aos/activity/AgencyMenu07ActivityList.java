package kr.co.rinnai.dms.aos.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.activity.BaseFragmentActivity;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu01ListAdapter;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu07SiteListAdapter;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu5ListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu01ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu05WareHouseStockInfo;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteInfo;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.custom.RinnaiMonthCalendarDialog;
import kr.co.rinnai.dms.common.custom.RinnaiSiteModelCountDialog;
import kr.co.rinnai.dms.common.custom.RinnaiSiteModelSearchDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.AddressJusoData;
import kr.co.rinnai.dms.common.http.model.Categorizaion;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.listener.PageListener;
import kr.co.rinnai.dms.common.listener.SiteModelModelSearchDialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu01Activity;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu06Activity;
import kr.co.rinnai.dms.eos.model.EmployeeMenu06ListEntity;
import kr.co.rinnai.dms.eos.model.ItemCode;


/**
 *  Agency Operating System(영업 시스템, 대리점) 대리점 현장 관리 시스템 현장 검색 화면
 */
public class AgencyMenu07ActivityList extends BaseActivity implements  AdapterView.OnItemClickListener {

	private AgencyMenu07SiteListAdapter adapter;

	private boolean networkConnecting = false;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String custName, custCode;

	private ListView lvSite;

	private RelativeLayout btnInsert, btnClose;

	private RelativeLayout btnSearch;

	private EditText etKeyword;

	private RelativeLayout rlFromDate, rlToDate;

	private TextView tvFromDate, tvToDate;

	private RinnaiMonthCalendarDialog rinnaiReceivedProductDialog;


	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agency_menu_07_list);


		helper = new MySQLiteOpenHelper(
				AgencyMenu07ActivityList.this,  // 현재 화면의 제어권자
				CommonValue.SQLITE_DB_FILE_NAME,// db 이름
				null,  // 커서팩토리-null : 표준커서가 사용됨
				CommonValue.SQLITE_DB_VERSION);       // 버전

		try {

			db = helper.getWritableDatabase(); // 읽고 쓸수 있는 DB
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

		lvSite = (ListView) findViewById(R.id.lv_agency_menu_07_list);

		btnInsert = (RelativeLayout) findViewById(R.id.btn_agency_menu_07_list_insert);
		btnClose = (RelativeLayout) findViewById(R.id.btn_agency_menu_07_list_close);
		btnSearch = (RelativeLayout) findViewById(R.id.btn_agency_menu_07_list_search);

		etKeyword = (EditText) findViewById(R.id.et_agency_menu_07_list_search_keyword);

		rlFromDate = (RelativeLayout) findViewById(R.id.rl_agency_menu_07_list_search_from_date);
		rlToDate = (RelativeLayout) findViewById(R.id.rl_agency_menu_07_list_search_to_date);

		tvFromDate = (TextView) findViewById(R.id.tv_agency_menu_07_list_search_from_date);
		tvToDate = (TextView) findViewById(R.id.tv_agency_menu_07_list_search_to_date);

		btnInsert.setOnClickListener(AgencyMenu07ActivityList.this);
		btnClose.setOnClickListener(AgencyMenu07ActivityList.this);
		lvSite.setOnItemClickListener(AgencyMenu07ActivityList.this);
		btnSearch.setOnClickListener(AgencyMenu07ActivityList.this);

		rlFromDate.setOnClickListener(AgencyMenu07ActivityList.this);
		rlToDate.setOnClickListener(AgencyMenu07ActivityList.this);



		Calendar calendar = Calendar.getInstance();

		String toDate = String.format("%4d년%02d월", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		tvToDate.setText(toDate);
		calendar.add(Calendar.MONTH, - 6);
		String keyword = "null";
		String fromDate = String.format("%4d년%02d월", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		tvFromDate.setText(fromDate);

		String reFrom = fromDate.replaceAll("[^0-9]","");
		String reTo = toDate.replaceAll("[^0-9]","");

		getSiteList(keyword, reFrom, reTo);

	}


	private void getSiteList(String keyword, String fromDate, String toDate) {

		if(!networkConnecting  ) {

			String url = String.format("%s/%s/%s/%s/%s/%s/%s/%s",
					CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_SITE_MANAGEMENT,
					CommonValue.HTTP_INFO, custCode, keyword, fromDate, toDate
			);

			showProgress(AgencyMenu07ActivityList.this);

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

		if (null != responseData) {
			Log.w("onResult", "ok");
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);
				Type type = null;
				if ("getSiteInfo".equals(responseData.getResultType())) {
					type = new TypeToken<ArrayList<AgencyMenu07SiteInfo>>(){}.getType();
					List<AgencyMenu07SiteInfo> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu07SiteListAdapter(AgencyMenu07ActivityList.this, list);

					lvSite.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			} else {
				showRinnaiDialog(AgencyMenu07ActivityList.this, getString(R.string.msg_title_noti),responseData.getResultMessage());


				if ("getSiteInfo".equals(responseData.getResultType())) {
					lvSite.setVisibility(View.INVISIBLE);

				}

			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		AgencyMenu07SiteInfo entity = (AgencyMenu07SiteInfo)adapter.getItem(position);
		Intent intent = null;
		intent = new Intent(AgencyMenu07ActivityList.this, AgencyMenu07Activity.class);
		intent.putExtra(CommonValue.INTENT_SITEINFO_KEY, entity);
		startActivityForResult(intent, 0);
	}
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == btnInsert.getId()) {
			Intent intent = null;
			intent = new Intent(AgencyMenu07ActivityList.this, AgencyMenu07Activity.class);
			startActivity(intent);
		} else if (id == btnClose.getId()) {
			finish();
		} else if(id == btnSearch.getId()) {
			String keyword = "null";
			String fromDate = "null";
			String toDate = "null";


			String from = tvFromDate.getText().toString();
			String reFrom = from.replaceAll("[^0-9]","");

			String to = tvToDate.getText().toString();
			String reTo = to.replaceAll("[^0-9]","");

			if(!etKeyword.getText().toString().equals("")) {
				keyword = etKeyword.getText().toString();
			}


			if(!reFrom.equals("")) {
				fromDate = reFrom;
			}

			if(!reTo.equals("")) {
				toDate = reTo;
			}

			if(!reFrom.equals("") && !reTo.equals("")) {

				int iSDate = Integer.parseInt(fromDate);
				int iEDate = Integer.parseInt(toDate);

				if (iSDate > iEDate) {

					showRinnaiDialog(AgencyMenu07ActivityList.this, getString(R.string.msg_title_noti), "조회 시작일이 조회 종료일보다 \n느립니다.");
					return;

				}
			} else if(reFrom.equals("") && !reTo.equals("")) {
				showRinnaiDialog(AgencyMenu07ActivityList.this, getString(R.string.msg_title_noti), "조회 시작일을 선택하세요.");
				return;
			} else if(!reFrom.equals("") && reTo.equals("")) {
				showRinnaiDialog(AgencyMenu07ActivityList.this, getString(R.string.msg_title_noti), "조회 종료일을 선택하세요.");
				return;
			}

			getSiteList(keyword, fromDate, toDate);


		} else if (id == rlFromDate.getId()) {
			if(rinnaiReceivedProductDialog == null) {
				String date = tvFromDate.getText().toString();
				rinnaiReceivedProductDialog = new RinnaiMonthCalendarDialog(AgencyMenu07ActivityList.this, 1, date);
			}
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {
						tvFromDate.setText(date);


					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});
				String date = tvFromDate.getText().toString();

				rinnaiReceivedProductDialog.show();
				rinnaiReceivedProductDialog.setDate(date);
			}
		} else if (id == rlToDate.getId()) {
			if(rinnaiReceivedProductDialog == null) {
				String date = tvFromDate.getText().toString();
				rinnaiReceivedProductDialog = new RinnaiMonthCalendarDialog(AgencyMenu07ActivityList.this, 1, date);
			}
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {
						tvToDate.setText(date);


					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});

				String date = tvToDate.getText().toString();

				rinnaiReceivedProductDialog.show();
				rinnaiReceivedProductDialog.setDate(date);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0) {
			if (resultCode == RESULT_CANCELED) {
//
			} else {   // RESULT_CANCEL
				String keyword = "null";
				String fromDate = "null";
				String toDate = "null";


				String from = tvFromDate.getText().toString();
				String reFrom = from.replaceAll("[^0-9]","");

				String to = tvToDate.getText().toString();
				String reTo = to.replaceAll("[^0-9]","");

				if(!etKeyword.getText().toString().equals("")) {
					keyword = etKeyword.getText().toString();
				}


				if(!reFrom.equals("")) {
					fromDate = reFrom;
				}

				if(!reTo.equals("")) {
					toDate = reTo;
				}
				getSiteList(keyword, fromDate, toDate);
//				Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
			}
//        } else if (requestCode == REQUEST_ANOTHER) {
//            ...
		}
	}

}
