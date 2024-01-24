package kr.co.rinnai.dms.aos.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
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
import kr.co.rinnai.dms.aos.adapter.AgencyMenu06ListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu06DetailEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ServiceEntity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.CustomButtonView;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.ServiceDialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 *  Agency Operating System(영업 시스템, 대리점) 서비스 접수
 */
public class AgencyMenu06Activity extends BaseActivity implements  CompoundButton.OnTouchListener, AdapterView.OnItemClickListener{

	private ListView lvService;
	private TextView tvCustName;
	private AgencyMenu06ListAdapter adapter;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String custName, custCode;

	private CustomButtonView btnReceiveAs;

	private boolean isPressed = false;

	private boolean networkConnecting;

	private AgencyMenu06ServiceDialog dialog;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agency_menu_06);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		lvService = (ListView) findViewById(R.id.lv_agency_menu_06_service_list);
		tvCustName = (TextView) findViewById(R.id.tv_agency_01_activity_cust_name);
		helper = new MySQLiteOpenHelper(
				AgencyMenu06Activity.this,  // 현재 화면의 제어권자
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

		//custCode = custCode.substring(2, 7);


		tvCustName.setText(custName);
		getServiceReception();
		lvService.setOnItemClickListener(AgencyMenu06Activity.this);
		btnReceiveAs = (CustomButtonView) findViewById(R.id.btn_receive_as);
		btnReceiveAs.setOnTouchListener(AgencyMenu06Activity.this);

	}


	private void getServiceReception() {
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(AgencyMenu06Activity.this);

			url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_SERVICE, custCode);

			showProgress(AgencyMenu06Activity.this);

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

				if ("getServiceReception".equals(responseData.getResultType())) {

					type = new TypeToken<ArrayList<AgencyMenu06ListEntity>>(){}.getType();

					List<AgencyMenu06ListEntity> list = new Gson().fromJson(str, type);

					adapter = new AgencyMenu06ListAdapter(AgencyMenu06Activity.this, list);
					lvService.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else if ("getServiceReceptionDetail".equals(responseData.getResultType())) {
					type = new TypeToken<AgencyMenu06DetailEntity>(){}.getType();

					AgencyMenu06DetailEntity entity = new Gson().fromJson(str, type);

					if(entity != null){

						dialog = new AgencyMenu06ServiceDialog(AgencyMenu06Activity.this);

						if(!dialog.isShowing()) {
							dialog.setCanceledOnTouchOutside(false);
							dialog.setCancelable(false);
							dialog.setDialogListener(new ServiceDialogListener() {
								@Override
								public void onPositiveClicked(String type, Object obj) {

									AgencyMenu06ServiceEntity entity= (AgencyMenu06ServiceEntity) obj;
									if("delete".equals(type)) {
                                       int position =  adapter.getItemPosition(entity.getBoardNum());
                                       adapter.getList().remove(position);

									}  else if ("update".equals(type)) {
										adapter.updateItem(entity);

									}
									adapter.notifyDataSetChanged();
								}
							});
							dialog.setEntity(entity);
							dialog.show();
						}

					}

				}
			} else {
				showRinnaiDialog(AgencyMenu06Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if(parent == lvService) {

			AgencyMenu06ListEntity service = (AgencyMenu06ListEntity)adapter.getItem(position);

			if(!networkConnecting) {

				String url = null;

				String httpHost = HttpClient.getCurrentSsid(AgencyMenu06Activity.this);

				url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_SERVICE, custCode, service.getAcceptNo());

				showProgress(AgencyMenu06Activity.this);

				networkConnecting = true;

				HttpClient.get(url, this);
			}

		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();
		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(id == btnReceiveAs.getId()) {
					btnReceiveAs.buttonClick(true);
				}
				Log.d("test", "Action_DOWN " + id);
				break;
			case MotionEvent.ACTION_UP:
				Log.d("test", "Action_UP"  + id);
				Intent intent = null;
				if(id == btnReceiveAs.getId()) {

					btnReceiveAs.buttonClick(false);
					dialog = new AgencyMenu06ServiceDialog(AgencyMenu06Activity.this);

					if(!dialog.isShowing()) {
						dialog.setCanceledOnTouchOutside(false);
						dialog.setCancelable(false);

						dialog.setEntity(null);
						dialog.setDialogListener(new ServiceDialogListener() {
							@Override
							public void onPositiveClicked(String type, Object obj) {
								if ("insert".equals(type)) {

									AgencyMenu06ListEntity listEntity = new AgencyMenu06ListEntity();
									AgencyMenu06ServiceEntity entity = (AgencyMenu06ServiceEntity) obj;
									listEntity.setVisitReqDate(entity.getVisitReqDate());
									listEntity.setCustName(entity.getCustName());

									SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd");
									Date time = new Date();

									String cpDate = format1.format(time);
									listEntity.setAcceptNo(entity.getAcceptNo());
									listEntity.setCpDate(cpDate);

									adapter.getList().add(0, listEntity);

								}
								adapter.notifyDataSetChanged();
							}
						});
						dialog.show();
					}


				}
				break;

		}

		return false;
	}
}
