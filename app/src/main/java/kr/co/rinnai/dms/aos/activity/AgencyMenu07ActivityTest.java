package kr.co.rinnai.dms.aos.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu01ListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu01ListEntity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;


/**
 *  Agency Operating System(영업 시스템, 대리점) 대리점 현장 관리 시스템 등록 화면
 */
public class AgencyMenu07ActivityTest extends BaseActivity {

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


		helper = new MySQLiteOpenHelper(
				AgencyMenu07ActivityTest.this,  // 현재 화면의 제어권자
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

//		tvCustName.setText(custName);


		setSiteManagement();

	}


	private void setSiteManagement() {
		if(!networkConnecting) {


			String url = null;

			//String httpHost = HttpClient.getCurrentSsid(AgencyMenu07Activity.this);

			url = String.format("%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_SITE_MANAGEMENT);

			showProgress(AgencyMenu07ActivityTest.this);

			networkConnecting = true;
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

			entityBuilder.addTextBody("userId", "userId");
			File image1 = new File("/sdcard/DCIM/Screenshots/Screenshot_20191104-172757_Smart DMS.jpg");
			File image2 = new File("/sdcard/DCIM/Screenshots/Screenshot_20191210-082514_Smart DMS.jpg");

			if(image1 != null)
			{
				entityBuilder.addBinaryBody("file", image1);
			}
			if(image2 != null)
			{
				entityBuilder.addBinaryBody("file1", image2);
			}

			HttpEntity entity = entityBuilder.build();


			HttpClient.postFile(url, this, entity);
/*

//
//
			try {

				File image1 = new File("/sdcard/DCIM/Screenshots/Screenshot_20191104-172757_Smart DMS.jpg");
				File image2 = new File("/sdcard/DCIM/Screenshots/Screenshot_20191210-082514_Smart DMS.jpg");
				HttpParams params = new BasicHttpParams();
				params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
				DefaultHttpClient mHttpClient = new DefaultHttpClient(params);

				HttpPost httppost = new HttpPost(url);

				MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

				multipartEntity.addPart("Title", new StringBody("Title"));
				multipartEntity.addPart("Nick", new StringBody("Nick"));
				multipartEntity.addPart("Email", new StringBody("Email"));
				multipartEntity.addPart("file", new FileBody(image1));
				multipartEntity.addPart("file1", new FileBody(image2));

				httppost.setEntity(multipartEntity);

//				httppost.setEntity(multipartEntity);

				mHttpClient.execute(httppost, new PhotoUploadResponseHandler());
			} catch (Exception e) {
				e.printStackTrace();
			}
			*/

			try
			{



			}
			catch(Exception e)
			{
				e.printStackTrace();
			}

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

					adapter = new AgencyMenu01ListAdapter(AgencyMenu07ActivityTest.this, list);
					lvUnShipped.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}
			} else {
				showRinnaiDialog(AgencyMenu07ActivityTest.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
	}



}
