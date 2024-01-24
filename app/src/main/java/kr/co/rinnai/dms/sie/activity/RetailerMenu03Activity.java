package kr.co.rinnai.dms.sie.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.content.FileProvider;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.aos.activity.AgencyMenu07Activity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.sie.adapter.RetailerMenu02ListAdapter;
import kr.co.rinnai.dms.sie.model.RetailerMenu02ListEntity;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeResult;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karrel.signviewlib.SignView;
import com.karrel.signviewlib.SignViewUtil;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;

/**
 * Shipping Installation Engineer(양판점 배송 설치 기사) 작업내용 등록 화면
 */
public class RetailerMenu03Activity extends BaseActivity{
	private BroadcastReceiver mReceiver = null;
	private boolean networkConnecting = false;

	private ArrayList<AgencyBarcodeResult> agencyBarcodeResultList = null;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private SignView svSignView ;

	private RelativeLayout rlSignClear;
	private RelativeLayout rlSubmit;
	private RelativeLayout rlSignView;

	private RelativeLayout rlSignViewParent;

	private File tempFile;


	private String image1Path, image2Path;
	private RelativeLayout btnCamera1;


	private ImageView ivSpotPic1, ivSpotPic2;
	private ImageView ivSign;
	private RelativeLayout btnList;

	private boolean file1Change = false, file2Change = false;


	RetailerMenu02ListEntity retailerEntity  = null;
	//sv_retailer_menu_03_sign

	private String userId;
	private String userName;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retailer_menu_03);
//		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		helper = new MySQLiteOpenHelper(
				RetailerMenu03Activity.this,  // 현재 화면의 제어권자
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
			userId = c.getString(1);
			userName = c.getString(2);
		}

		ivSign = (ImageView) findViewById(R.id.iv_retailer_menu_03_sign);
		rlSignViewParent = (RelativeLayout) findViewById(R.id.rl_retailer_menu_03_sign_parent);
		svSignView = (SignView) findViewById(R.id.sv_retailer_menu_03_sign);
		rlSignView = (RelativeLayout) findViewById(R.id.rl_retailer_menu_03_sign);

		rlSignClear = (RelativeLayout) findViewById(R.id.btn_retailer_menu_03_sign_clear);
		rlSubmit = (RelativeLayout) findViewById(R.id.btn_retailer_menu_03_submit);

		ivSpotPic1 = (ImageView) findViewById(R.id.iv_spot_pic_1);
		btnCamera1 = (RelativeLayout) findViewById(R.id.btn_add_camera_1);
		btnList = (RelativeLayout) findViewById(R.id.btn_retailer_menu_03_list);

		rlSignClear.setOnClickListener(RetailerMenu03Activity.this);
		rlSubmit.setOnClickListener(RetailerMenu03Activity.this);
		btnCamera1.setOnClickListener(RetailerMenu03Activity.this);
		btnList.setOnClickListener(RetailerMenu03Activity.this);




		retailerEntity = (RetailerMenu02ListEntity) getIntent().getParcelableExtra(CommonValue.INTENT_RETAILER_KEY);
		String agencyOrderBarcode = retailerEntity.getAgencyOrderBarcode().replace("/", "-");
		String imgSign01Name = String.format("%s-sign01.jpg", agencyOrderBarcode);
		String imgImage01Name = String.format("%s-image01.jpg", agencyOrderBarcode);
		String imgImage02Name = String.format("%s-image02.jpg", agencyOrderBarcode);


		File fileSign01 = new File(Environment.getExternalStorageDirectory() + "/dms/" + imgSign01Name);

		File fileImage01 = new File(Environment.getExternalStorageDirectory() + "/dms/" + imgImage01Name);

		File fileImage02 = new File(Environment.getExternalStorageDirectory() + "/dms/" + imgImage02Name);

		if(fileImage01.exists()== true) {
			Log.d("Retailer", "fileImage01 유무 ");
			setInitImage(ivSpotPic1, fileImage01.getAbsolutePath());

		}

		if(fileImage02.exists()== true) {
			Log.d("Retailer", "fileImage02 유무 ");
			setInitImage(ivSpotPic1, fileImage02.getAbsolutePath());
		}

		if(fileSign01.exists()==true)   {
			Log.d("Retailer", "fileSign01 유무 ");

			setInitImage(ivSign, fileSign01.getAbsolutePath());
			setGoneSignView();

		}

	}



	@Override
	protected void onDestroy() {
		super.onDestroy();
		//this.unregisterReceiver(mReceiver);
	}

	@Override
	public void onClick(View v) {
		//super.onClick(v);
		int id = v.getId();

		if (id == rlSignClear.getId()) {
			svSignView.clearCanvas();
		} else if (id == rlSubmit.getId()) {
			setShippingData();
		} else if (id == btnCamera1.getId()) {
			takePhoto(3);
		} else if (id == btnList.getId()) {
			this.finish();
		}
	}

	private void takePhoto(int i) {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			tempFile = createImageFile();
		} catch (IOException e) {
			Toast.makeText(RetailerMenu03Activity.this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
			//finish();
			e.printStackTrace();
		}
		if (tempFile != null) {

//			Uri photoUri = Uri.fromFile(tempFile);
			Uri photoUri = FileProvider.getUriForFile(RetailerMenu03Activity.this, "kr.co.rinnai.dms.fileprovider", tempFile);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, i);
		}
	}

	private File createImageFile() throws IOException {

		// 이미지 파일 이름 ( blackJin_{시간}_ )
		String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
		String imageFileName = "dms" + timeStamp + "_";

		// 이미지가 저장될 폴더 이름 ( blackJin )
		File storageDir = new File(Environment.getExternalStorageDirectory() + "/dms/");
		if (!storageDir.exists()) storageDir.mkdirs();

		// 빈 파일 생성
		File image = File.createTempFile(imageFileName, ".jpg", storageDir);



		return image;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 1 || requestCode == 2) {

				Uri photoUri = data.getData();

				Cursor cursor = null;

				try {

					/*
					 *  Uri 스키마를
					 *  content:/// 에서 file:/// 로  변경한다.
					 */
					String[] proj = {MediaStore.Images.Media.DATA};

					assert photoUri != null;
					cursor = getContentResolver().query(photoUri, proj, null, null, null);

					assert cursor != null;
					int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

					cursor.moveToFirst();

					tempFile = new File(cursor.getString(column_index));

				} finally {
					if (cursor != null) {
						cursor.close();
					}
				}

				setImage(requestCode);

			} else if (requestCode == 3 || requestCode == 4) {
				setImage(requestCode / 2);
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


				if ("setRetailerInstallation".equals(responseData.getResultType())) {
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(RetailerMenu03Activity.this);
					alert_confirm.setMessage("등록이 완료되었습니다.").setCancelable(false).setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {

									Intent intent = new Intent();
									setResult(RESULT_OK, intent);
									finish();
								}
							});
					AlertDialog alert = alert_confirm.create();
					alert.show();

				}
			} else {
				showRinnaiDialog(RetailerMenu03Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
	}

	private void setImage(int i) {

		BitmapFactory.Options options = new BitmapFactory.Options();

		int orientation = Util.getOrientationOfImage(tempFile.getAbsolutePath());
		Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

		try {
			originalBm = Util.getRotatedBitmap(originalBm, orientation);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(i == 1) {
			image1Path = tempFile.getAbsolutePath();
			ivSpotPic1.setImageBitmap(originalBm);
			file1Change = true;
		} else if (i == 2) {
			ivSpotPic2.setImageBitmap(originalBm);
			image2Path = tempFile.getAbsolutePath();
			file2Change = true;
		}

	}

	/**
	 * 	작업 완료된 이미지 정보 표시(서명 및 설치 사진)
	 * @param v
	 * @param imagePath
	 */
	private void setInitImage(ImageView v, String imagePath) {

		BitmapFactory.Options options = new BitmapFactory.Options();


		Bitmap originalBm = BitmapFactory.decodeFile(imagePath, options);

		v.setImageBitmap(originalBm);

	};

	private void setShippingData() {
		String url = null;

		url = String.format("%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_RETAILER, CommonValue.HTTP_INSTALLATION);

		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

		entityBuilder.setMode(HttpMultipartMode.RFC6532);
		entityBuilder.setCharset(StandardCharsets.UTF_8);

		try {

			setSignInfo(entityBuilder, "sign");

			setFileInfo(entityBuilder, image1Path, "image1");


			entityBuilder.addPart("agencyOrderBarcode", new StringBody(retailerEntity.getAgencyOrderBarcode(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			entityBuilder.addPart("pdaNo", new StringBody(userId, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));

		} catch (Exception e) {
			e.printStackTrace();
		}

		HttpEntity entity = entityBuilder.build();

		//networkConnecting = true;
		showProgress(RetailerMenu03Activity.this);
		networkConnecting = true;
		HttpClient.postFile(url, this, entity);
	}

	/**
	 * 신규 현장 정보 중 이미지 관련 정보 입력
	 * @param eb
	 * @param path
	 * @param entityName
	 */
	private void setFileInfo(MultipartEntityBuilder eb, String path, String entityName) {

		if(path != null) {
			Bitmap image = Util.filePathToBitMap(path);

			int i = path.lastIndexOf(".");
			int prefix = path.lastIndexOf("/");
			String oriFileName = path;
			String filename = oriFileName.substring(prefix + 1, i);
			String fileExt = oriFileName.substring(i, oriFileName.length());

			String filePath = Util.saveBitmaptoJpeg(image, filename, fileExt);
			File file = new File(filePath);


			eb.addBinaryBody(entityName, file);
		}

	}

	/**
	 * 고갱 사인 이미지 관련 정보 입력
	 * @param eb
	 * @param entityName
	 */
	private void setSignInfo(MultipartEntityBuilder eb, String entityName) {

		Bitmap imgSign = svSignView.getBitmap();
		Bitmap parentSign = SignViewUtil.loadBitmapFromView(rlSignViewParent);

		//Bitmap overlay = SignViewUtil.overlay(imgSign, bgSign);

		String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
		String imageFileName = "dms" + timeStamp + "_";



		//ivSpotPic1.setImageBitmap(parentSign);



		if(imgSign != null) {

			String filePath = Util.saveBitmaptoJpeg(parentSign, imageFileName, ".jpg");
			File file = new File(filePath);



			eb.addBinaryBody(entityName, file);
		}



	}


	/**
	 * 작업이 완료된 현장 정보 확인 시 서명 및 설치 현장 등록 관련 버튼 및 이미지 비 활성화
	 */
	private void setGoneSignView() {
		rlSignViewParent.setVisibility(View.GONE);
		svSignView.setVisibility(View.GONE);
		rlSignView.setVisibility(View.GONE);
		ivSign.setVisibility(View.VISIBLE);

		rlSignClear.setVisibility(View.INVISIBLE);
		rlSubmit.setVisibility(View.INVISIBLE);
		btnCamera1.setVisibility(View.INVISIBLE);
	}

}

