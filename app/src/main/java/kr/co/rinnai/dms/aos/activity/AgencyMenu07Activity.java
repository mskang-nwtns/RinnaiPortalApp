package kr.co.rinnai.dms.aos.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;

import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


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
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseFragmentActivity;
import kr.co.rinnai.dms.aos.model.AgencyMenu01ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ServiceEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteDetailInfo;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteFileInfo;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteInfo;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiSiteModelCountDialog;
import kr.co.rinnai.dms.common.custom.RinnaiSiteModelSearchDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;

import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.AddressJusoData;
import kr.co.rinnai.dms.common.http.model.ResponseData;

import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.listener.PageListener;
import kr.co.rinnai.dms.common.listener.SiteModelModelSearchDialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.common.util.Util;

import kr.co.rinnai.dms.eos.model.CodeInfo;
import kr.co.rinnai.dms.eos.model.ItemCode;


/**
 *  Agency Operating System(영업 시스템, 대리점) 대리점 현장 관리 시스템 등록 화면
 */
public class AgencyMenu07Activity extends BaseFragmentActivity implements ViewPager.OnPageChangeListener, PageListener {

	private boolean networkConnecting = false;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String custName, custCode;

	private ViewPager viewPager = null;

	private RelativeLayout btnSave, btnDelete, btnClose;
	private TextView tvSave;

	private PageAdapter pageAdapter;

	private List<AgencyMenu07SiteModelInfo> modelInfos;

	private ImageView ivPage1, ivPage2;

	AgencyMenu07SiteInfo siteInfo  = null;

	List<AgencyMenu07SiteFileInfo> fileList = null;
	List<AgencyMenu07SiteModelInfo> modelList = null;

	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agency_menu_07);


		helper = new MySQLiteOpenHelper(
				AgencyMenu07Activity.this,  // 현재 화면의 제어권자
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

		tvSave = (TextView)findViewById(R.id.tv_agency_menu_07_save);


		viewPager = (ViewPager)findViewById(R.id.vp_agency_menu_07);

		viewPager.addOnPageChangeListener(AgencyMenu07Activity.this);
		pageAdapter = new PageAdapter(getSupportFragmentManager());

		pageAdapter.addFragment(new AgencyMenu07FragmentFirst(), "menu0");
		pageAdapter.addFragment(new AgencyMenu07FragmentSecond(), "menu1");

		viewPager.setAdapter(pageAdapter);
		viewPager.setCurrentItem(0);

		btnSave  = (RelativeLayout) findViewById(R.id.btn_agency_menu_07_save);
		btnDelete  = (RelativeLayout) findViewById(R.id.btn_agency_menu_07_delete);
		btnClose  = (RelativeLayout) findViewById(R.id.btn_agency_menu_07_close);

		ivPage1 = (ImageView) findViewById(R.id.iv_agency_page_1);
		ivPage2 = (ImageView) findViewById(R.id.iv_agency_page_2);

		btnSave.setOnClickListener(AgencyMenu07Activity.this);
		btnDelete.setOnClickListener(AgencyMenu07Activity.this);
		btnClose.setOnClickListener(AgencyMenu07Activity.this);

		getCommonType();


		siteInfo = (AgencyMenu07SiteInfo) getIntent().getParcelableExtra(CommonValue.INTENT_SITEINFO_KEY);
		//Log.e("[ITEM] 이름 :", getResources().getString(item.getName()));

		if(siteInfo == null) {
			btnDelete.setVisibility(View.GONE);
		} else {
//			btnSave.setVisibility(View.GONE);
			tvSave.setText("수정");
		}

		showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"위 현장 납품에 대하여 허위 입력 및 할인 출고된 제품을 다른 목적으로\n재 유통 시 제재 조치(출고정지 등) 가 부과 될 수 있음을 확인 합니다.");

	}


	private void setSiteManagement() {
		if(!networkConnecting) {


			AgencyMenu07FragmentFirst first = (AgencyMenu07FragmentFirst)pageAdapter.getFragment(0);
			AgencyMenu07FragmentSecond second = (AgencyMenu07FragmentSecond)pageAdapter.getFragment(1);

			String filePath1 = second.getImageFilePath(0);
			String filePath2 = second.getImageFilePath(1);

			String url = null;

			url = String.format("%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_SITE_MANAGEMENT);




			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

			entityBuilder.setMode(HttpMultipartMode.RFC6532);
			entityBuilder.setCharset(StandardCharsets.UTF_8);


			if(!setAddressInfo(entityBuilder, first.getJusoData())) {
				return;
			}

			if(!setSiteInfo(entityBuilder, first)) {
				return;
			}
			try {
				setFileInfo(entityBuilder, filePath1, "file");
				setFileInfo(entityBuilder, filePath2, "file1");
			} catch (Exception e) {
				e.printStackTrace();
			}
			setModelInfos(entityBuilder);


			if(siteInfo != null) {
				entityBuilder.addPart("siteRegdate", new StringBody(siteInfo.getSiteRegdate(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
				entityBuilder.addPart("siteNum", new StringBody(siteInfo.getSiteNum(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
				if(siteInfo.getfSeqnum()> 0) {
					entityBuilder.addPart("fSeqnum", new StringBody(String.format("%d", siteInfo.getfSeqnum()), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
				}
			}
			HttpEntity entity = entityBuilder.build();

			//networkConnecting = true;
			showProgress(AgencyMenu07Activity.this);
			networkConnecting = true;
			HttpClient.postFile(url, this, entity);


		}
	}

	private void deleteSiteManagement(AgencyMenu07SiteInfo info) {
		String url = null;

		url = String.format("%s/%s/%s/%s/%s/%s/%s/%d",
				CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_SITE_MANAGEMENT,  CommonValue.HTTP_INFO, CommonValue.HTTP_DETAIL,
				info.getSiteRegdate(), info.getSiteNum(), info.getfSeqnum());

		AgencyMenu06ServiceEntity paramEntity = new AgencyMenu06ServiceEntity();

		paramEntity.setComId(CommonValue.WMS_I_COM_ID);
		paramEntity.setCustCode(custCode);
		showProgress(AgencyMenu07Activity.this);
		networkConnecting = true;
		try {
			String strResponse = ParseUtil.getJSONFromObject(paramEntity);
			HttpClient.delete(url, AgencyMenu07Activity.this, strResponse);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}

	private void getCommonType() {
		if(!networkConnecting) {


			String url = String.format("%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_AGENCY, CommonValue.HTTP_COMMON, CommonValue.HTTP_TYPE);

			showProgress(AgencyMenu07Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}


	protected void getSiteDetailInfo(AgencyMenu07SiteInfo info) {
		if(!networkConnecting) {

			String url = String.format("%s/%s/%s/%s/%s/%s/%s/%d",
					CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_SITE_MANAGEMENT, CommonValue.HTTP_INFO,
					CommonValue.HTTP_DETAIL, info.getSiteRegdate(), info.getSiteNum(), info.getfSeqnum());
			//showProgress(AgencyMenu07Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}

	}

	protected void getSiteFile(List<AgencyMenu07SiteFileInfo> fileList) {

		for(int i = 0; i < fileList.size(); i ++) {

			AgencyMenu07SiteFileInfo fileInfo = fileList.get(i);
			ImageDownload image = new ImageDownload(AgencyMenu07Activity.this, fileInfo.getsCount());

			String url = null;


			url = String.format("%s/%s", CommonValue.HTTP_IMAGE_DOWNLOAD_HOST, fileInfo.getFileName());

			image.execute(url);
		}

	}

	protected void getSiteModelInfo(String modelName) {
		if(!networkConnecting) {

			String url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_SITE_MANAGEMENT, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, modelName);



			showProgress(AgencyMenu07Activity.this);

			networkConnecting = true;
			HttpClient.get(url, this);
		}
	}

	@Override
	public void onResult(String result) {
		Log.w("onResult", result);
		networkConnecting = false;
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

				}  else if("getCommonType".equals(responseData.getResultType())) {

					type = new TypeToken<CodeInfo>(){}.getType();
					CodeInfo codeInfo = new Gson().fromJson(str, type);
					AgencyMenu07FragmentFirst first = (AgencyMenu07FragmentFirst)pageAdapter.getFragment(0);
					first.setBuildingType(codeInfo.getBuildingCode());
					first.setHeagtingType(codeInfo.getHeatingCode());
					first.setCustName(custName);
					if(siteInfo != null) {
						first = (AgencyMenu07FragmentFirst)pageAdapter.getFragment(0);

						first.setSiteInfo(siteInfo);

						if(siteInfo != null) {

							Handler delayHandler = new Handler();

							delayHandler.postDelayed(new Runnable() {
								@Override
								public void run() {

									getSiteDetailInfo(siteInfo);
								}
							}, 500);

						}

					}

				} else if ("getSiteModelInfo".equals(responseData.getResultType())) {
					type = new TypeToken<ArrayList<AgencyMenu07SiteModelInfo>>(){}.getType();
					List<AgencyMenu07SiteModelInfo> modelList = new Gson().fromJson(str, type);

					if(modelList.size() == 1) {
						AgencyMenu07SiteModelInfo modelInfo = modelList.get(0);
						addModelInfos(modelInfo);
						showCountPopup(modelInfos.size() - 1, modelInfo.getModelName());

					} else if(modelList.size() > 1 ){
						showListPopup(modelList);
					}


				} else if ("getSiteDetailInfo".equals(responseData.getResultType())) {
					type = new TypeToken<AgencyMenu07SiteDetailInfo>(){}.getType();
					AgencyMenu07SiteDetailInfo detailInfo = new Gson().fromJson(str, type);

					fileList = detailInfo.getFileList();
					modelList = detailInfo.getModelList();

					if(modelInfos == null) {
						modelInfos = new ArrayList<AgencyMenu07SiteModelInfo>();
					}

					modelInfos.addAll(modelList);

					AgencyMenu07FragmentSecond second = (AgencyMenu07FragmentSecond)pageAdapter.getFragment(1);
					second.datasetChange();

					Handler delayHandler = new Handler();

					delayHandler.postDelayed(new Runnable() {
						@Override
						public void run() {

							getSiteFile(fileList);
						}
					}, 500);




				} else if("setSiteManagement".equals(responseData.getResultType())) {
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(AgencyMenu07Activity.this);
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
				} else if("deleteSiteInfo".equals(responseData.getResultType())) {

					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(AgencyMenu07Activity.this);
					alert_confirm.setMessage("삭제가 완료되었습니다.").setCancelable(false).setPositiveButton("확인",
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

				} else {

				}
			} else {
				showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
	}

	/**
	 * 모델 리스트 팝업
	 * @param obj
	 */
	private void showListPopup(List<AgencyMenu07SiteModelInfo> obj) {
		RinnaiSiteModelSearchDialog rinnaiReceivedProductDialog;
		rinnaiReceivedProductDialog = new RinnaiSiteModelSearchDialog(AgencyMenu07Activity.this, obj);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		rinnaiReceivedProductDialog.setDialogListener(new SiteModelModelSearchDialogListener() {  // MyDialogListener 를 구현

			@Override
			public void onPositiveClicked(AgencyMenu07SiteModelInfo modelInfo) {
				addModelInfos(modelInfo);
				showCountPopup(modelInfos.size() - 1, modelInfo.getModelName());
			}
		});
		rinnaiReceivedProductDialog.show();

	}

	/**
	 * 모델 수량 선택 팝업
	 * @param idx
	 */
	private void showCountPopup(int idx, String modelName) {
		RinnaiSiteModelCountDialog rinnaiDialog;
		rinnaiDialog = new RinnaiSiteModelCountDialog(AgencyMenu07Activity.this, idx, modelName);
		rinnaiDialog.setCanceledOnTouchOutside(false);
		rinnaiDialog.setCancelable(false);
		rinnaiDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현

			//사용 안함
			@Override
			public void onPositiveClicked(String barcode, String qty) {

			}

			@Override
			public void onPositiveClicked(String barcode, String qty, int position) {
				modelInfos.get(position).setQty(Integer.parseInt(qty));
				AgencyMenu07FragmentSecond second = (AgencyMenu07FragmentSecond)pageAdapter.getFragment(1);
				second.datasetChange();

			}
			//사용 안함
			@Override
			public void onPositiveClicked(String barcode, String modelName, String qty) {

			}
		});
		rinnaiDialog.show();

	}


	protected void addModelInfos(AgencyMenu07SiteModelInfo modelInfo) {
		if(modelInfos == null) {
			modelInfos = new ArrayList<AgencyMenu07SiteModelInfo>();
		}
		modelInfo.setSelected(false);
		modelInfos.add(modelInfo);

	}

	protected void removeModelInfos(int idx) {
		modelInfos.remove(idx);
	}

	protected List<AgencyMenu07SiteModelInfo> getModelInfos() {
		return modelInfos;
	}




	@Override
	public void onClick(View v) {

		int id = v.getId();

		if (id == btnSave.getId()) {
			setSiteManagement();
		} else if (id == btnClose.getId()) {
			Intent intent = new Intent();
			setResult(RESULT_CANCELED, intent);
			finish();
		} else if (id == btnDelete.getId()) {
			if(siteInfo != null) {
				deleteSiteManagement(siteInfo);
			}
		}
	}

	private class PageAdapter extends FragmentPagerAdapter
	{
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public PageAdapter(FragmentManager fm)
		{
			super(fm);
		}
		@Override
		public Fragment getItem(int position) { return mFragmentList.get(position); }
		@Override
		public int getCount()
		{
			return mFragmentList.size();
		}

		public void addFragment(Fragment fragment, String title){
			mFragmentList.add(fragment);
			mFragmentTitleList.add(title);
		}

		public Fragment getFragment(int position){
			return mFragmentList.get(position);
		}

	}

	/**
	 * 신규 현장 정보 중 주소 관련 정보 입력
	 * @param eb
	 * @param juso
	 */
	private boolean setAddressInfo(MultipartEntityBuilder eb, AddressJusoData juso) {

		if(juso == null ) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"주소를 선택하여 주세요.");
			return false;
		} else {

			eb.addPart("siteZipcode", new StringBody(juso.getZipNo(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteRoadaddrpart1", new StringBody(juso.getRoadAddrPart1(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteRoadaddrpart2", new StringBody(juso.getRoadAddrPart2(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));

			eb.addPart("siteJibunaddr", new StringBody(juso.getJibunAddr(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteAdmcd", new StringBody(juso.getAdmCd(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteRoadcode", new StringBody(juso.getRnMgtSn(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteBuildingcode", new StringBody(juso.getBdMgtSn(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteBuildingdetname", new StringBody(juso.getDetBdNmList(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteBuildingname", new StringBody(juso.getBdNm(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteApthouseyn", new StringBody(juso.getBdKdcd(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteSido", new StringBody(juso.getSiNm(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteGugun", new StringBody(juso.getSggNm(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteDong", new StringBody(juso.getEmdNm(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteBname", new StringBody(juso.getLiNm(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteBname1", new StringBody(juso.getLiNm(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteRn", new StringBody(juso.getRn(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteBuldmnnm", new StringBody(juso.getBuldMnnm(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteBuldslno", new StringBody(juso.getBuldSlno(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteInbrmnnm", new StringBody(juso.getLnbrMnnm(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
			eb.addPart("siteEmdno", new StringBody(juso.getEmdNo(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));



		}
		return true;

	}

	private boolean setSiteInfo(MultipartEntityBuilder eb, AgencyMenu07FragmentFirst fragment) {

		String siteName = fragment.getSiteName();
		String totalHouseCount = fragment.getTotalHouseCount();
		String saleCust = fragment.getSaleCust();
		String saleYm = fragment.getSaleYm();
		String specialNote = fragment.getSpecialNote();
		String addrDet = fragment.getAddrDet();
		String buildingType = fragment.getBuildingTypecode();
		String heatingType = fragment.getHeatingTypecode();

		if(siteName.equals("")) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"현장명을 입력하여 주세요.");
			return false;
		} else if(totalHouseCount.equals("")) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"총 세대수를 입력하여 주세요.");
			return false;
		}
		/*
		else if(saleCust.equals("")) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"납품업체를 입력하여 주세요.");
			return false;
		}
		*/

		else if(saleYm.equals("")) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"납품년월을 선택하여 주세요.");
			return false;
		} else if(specialNote.equals("")) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"특기사항을 입력하여 주세요.");
			return false;
		} else if(addrDet.equals("")) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"상세주소를 입력하여 주세요.");
			return false;
		} else if(buildingType.equals("-1")) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"건물형태를 선택하여 주세요.");
			return false;
		} else if(heatingType.equals("-1")) {
			showRinnaiDialog(AgencyMenu07Activity.this, getString(R.string.msg_title_noti),"난방형태를 선택하여 주세요.");
			return false;
		}


		eb.addPart("siteDetaddr", new StringBody(addrDet, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("buildingTypecode", new StringBody(buildingType, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("heatTypecode", new StringBody(heatingType, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("custCode", new StringBody(custCode, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("custName", new StringBody(custName, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("siteName", new StringBody(siteName, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("totHouseholdCnt", new StringBody(totalHouseCount, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("saleCust", new StringBody(saleCust, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("saleYm", new StringBody(saleYm, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		eb.addPart("specialNote", new StringBody(specialNote, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		return true;
	}
	/**
	 * 신규 현장 정보 중 이미지 관련 정보 입력
	 * @param eb
	 * @param path
	 * @param entityName
	 */
	private void setFileInfo(MultipartEntityBuilder eb, String path, String entityName) {
		AgencyMenu07FragmentSecond second = (AgencyMenu07FragmentSecond)pageAdapter.getFragment(1);
		boolean isChange = second.isFileChange(entityName);


		if(siteInfo != null) {
			eb.addPart(entityName+"Change", new StringBody(isChange + "", ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
		}

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

	private void setModelInfos(MultipartEntityBuilder eb) {
		String strResponse = ParseUtil.getJSONFromObject(modelInfos);
		eb.addPart("modelInfo", new StringBody(strResponse, ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8")));
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {

		if(position == 0) {
			ivPage1.setImageResource(R.drawable.page_1_on);
			ivPage2.setImageResource(R.drawable.page_2_off);
		} else if(position == 1) {
			ivPage1.setImageResource(R.drawable.page_1_off);
			ivPage2.setImageResource(R.drawable.page_2_on);

		}
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	@Override
	public void onPageChange(Fragment fragment, int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(Fragment fragment, int position) {

	}

	@Override
	public void onPageSelected(Fragment fragment, int position, Object selecetdItem) {

	}

	@Override
	public void onPageSelected(int position, String type) {

	}



	class ImageDownload extends AsyncTask<String, Integer, Bitmap> {
		Context context;
		Bitmap bitmap;
		InputStream in = null;
		int responseCode = -1;
		int position = 1;
		//constructor.
		public ImageDownload(Context context, int sCount) {
			this.context = context;
			this.position = sCount;
		}
		@Override
		protected void onPreExecute() {


		}
		@Override
		protected Bitmap doInBackground(String... params) {

			URL url = null;
			try {
				url = new URL(params[0]);

				HttpURLConnection con = (HttpURLConnection) url.openConnection();
//				con.setDoOutput(true);
				con.setRequestMethod("GET");
				con.setConnectTimeout(30000);       //컨텍션타임아웃 10초
				con.setReadTimeout(5000);

				con.connect();
				responseCode = con.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					in = con.getInputStream();
					bitmap = BitmapFactory.decodeStream(in);
					in.close();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}
		@Override
		protected void onPostExecute(Bitmap data) {
			AgencyMenu07FragmentSecond second = (AgencyMenu07FragmentSecond)pageAdapter.getFragment(1);
			AgencyMenu07SiteFileInfo fileInfo = fileList.get(position - 1);
			second.setdownloadImg(data, position, fileInfo.getFileName());
		}
	}


}
