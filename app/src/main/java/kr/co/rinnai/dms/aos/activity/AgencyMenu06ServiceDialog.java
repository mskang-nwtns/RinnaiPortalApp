package kr.co.rinnai.dms.aos.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseDialog;
import kr.co.rinnai.dms.activity.RinnaiDialog;
import kr.co.rinnai.dms.adapter.CategorySpinnerAdapter;
import kr.co.rinnai.dms.adapter.SearchListAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu06DetailEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ServiceEntity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.callback.IAsyncCallback;
import kr.co.rinnai.dms.common.custom.CustomButtonView;
import kr.co.rinnai.dms.common.custom.RinnaiAddressSearchDialog;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.AddressJusoData;
import kr.co.rinnai.dms.common.http.model.Categorizaion;
import kr.co.rinnai.dms.common.http.model.CategorizationResultVO;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.AddressSelectedListener;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.ServiceDialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ParseUtil;

public class AgencyMenu06ServiceDialog extends BaseDialog implements IAsyncCallback<String>, AdapterView.OnItemClickListener, OnClickListener, CompoundButton.OnTouchListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

	private boolean networkConnecting = false;

	private ServiceDialogListener dialogListener;

	private RelativeLayout rlDate;

	private AgencyMenu06DetailEntity obj;
	private Context context;

	private RelativeLayout confirm;
	private RelativeLayout cancel;

	private TextView title, selectItem;

	private Categorizaion categori;

	private Spinner spMaster, spSub;

	private CategorySpinnerAdapter masterAdapter, subAdapter;



	private String gasType = "2";

	private RinnaiDialog rinnaiDialog;

	private EditText etName, etEmail;

	private EditText etHp1, etHp2, etHp3;

	private TextView tvPostNo, tvAddr1;

	private EditText etaddr2, etContent;

	private TextView tvVisitDate;

	private EditText etModelName;

	private RinnaiCalendarDialog rinnaiReceivedProductDialog;

	private RinnaiAddressSearchDialog rinnaiAddressSearchDialog;

	private RelativeLayout btnAddressSearch;

	private CustomButtonView btnSave, btnDelete;

	private boolean isPressed = false;

	private MySQLiteOpenHelper helper;

	private SQLiteDatabase db;

	private String custName, custCode;

	private AddressJusoData jusoData;

	private CheckBox cbReceiveAs;

	private EditText etAnswer;

	private AgencyMenu06ServiceEntity paramEntity;

	//et_agency_menu_06_name
	//et_agency_menu_06_email

	//tv_post_no
	//tv_addr1

	//et_agency_menu_06_content

	//tv_agency_menu_06_visit_date

	public AgencyMenu06ServiceDialog(Context context) {
		super(context);
		this.context = context;


	}


	public void setEntity(AgencyMenu06DetailEntity obj) {
		this.obj = obj;


	}

	public void setDialogListener(ServiceDialogListener dialogListener){
		this.dialogListener = dialogListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.activity_agency_menu_06_service_info);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

		etName = (EditText) findViewById(R.id.et_agency_menu_06_name);
		etEmail = (EditText) findViewById(R.id.et_agency_menu_06_email);
		etContent = (EditText) findViewById(R.id.et_agency_menu_06_content);

		etHp1 = (EditText) findViewById(R.id.et_agency_menu_06_hp1);
		etHp2 = (EditText) findViewById(R.id.et_agency_menu_06_hp2);
		etHp3 = (EditText) findViewById(R.id.et_agency_menu_06_hp3);


		tvPostNo = (TextView) findViewById(R.id.tv_post_no);
		tvAddr1 = (TextView) findViewById(R.id.tv_addr1);
		tvVisitDate = (TextView) findViewById(R.id.tv_agency_06_service_info_date);

		etaddr2 = (EditText)findViewById(R.id.et_addr2);
		etModelName =  (EditText) findViewById(R.id.et_agency_menu_06_model_name);
		spMaster = (Spinner) findViewById(R.id.sp_agency_06_master);
		spSub = (Spinner) findViewById(R.id.sp_agency_06_sub);

		spMaster.setOnItemSelectedListener(AgencyMenu06ServiceDialog.this);
		spSub.setOnItemSelectedListener(AgencyMenu06ServiceDialog.this);

		rlDate = (RelativeLayout) findViewById(R.id.rl_agency_06_service_info_date);
		rlDate.setOnClickListener(AgencyMenu06ServiceDialog.this);

		btnAddressSearch = (RelativeLayout) findViewById(R.id.btn_agency_menu_06_address_search);
		btnAddressSearch.setOnClickListener(AgencyMenu06ServiceDialog.this);

		btnSave = (CustomButtonView) findViewById(R.id.btn_receive_as_save);
		btnSave.setOnTouchListener(AgencyMenu06ServiceDialog.this);

		btnDelete = (CustomButtonView) findViewById(R.id.btn_receive_as_delete);
		btnDelete.setOnTouchListener(AgencyMenu06ServiceDialog.this);

		cbReceiveAs = (CheckBox) findViewById(R.id.cb_receive_as);

		etAnswer = (EditText) findViewById(R.id.et_agency_menu_06_content_answer);

		getModelCategory();

		etName.setText("");
		etEmail.setText("");
		etContent.setText("");

		tvPostNo.setText("");

		tvAddr1.setText("");
		tvVisitDate.setText("");

		etaddr2.setText("");

		etHp1.setText("");
		etHp2.setText("");
		etHp3.setText("");

		if(obj != null) {
			etName.setText(obj.getCustName());
			etEmail.setText(obj.getEmail());
			etContent.setText(obj.getCounselContents());

			tvPostNo.setText(obj.getZipCode());

			tvAddr1.setText(obj.getRoadAddr1());
			tvVisitDate.setText(obj.getVisitReqDate().substring(0, 10));

			etaddr2.setText(obj.getAddrDet());

			etHp1.setText(obj.getHp1());
			etHp2.setText(obj.getHp2());
			etHp3.setText(obj.getHp3());
			cbReceiveAs.setChecked(true);

			etAnswer.setText(obj.getCounselAnswer());

			etModelName.setText(obj.getModelName());

			etName.setSelection(etName.getText().length());


		} else {
			etName.setText("");
			etEmail.setText("");
			etContent.setText("");

			//tvPostNo.setText();

			//tvAddr1.setText(obj.getRoadAddr1());
			//tvVisitDate.setText(obj.getVisitReqDate().substring(0, 10));

			//etaddr2.setText(obj.getAddrDet());

			etHp1.setText("");
			etHp2.setText("");
			etHp3.setText("");
			cbReceiveAs.setChecked(false);
		}


		helper = new MySQLiteOpenHelper(
				context, // 어권자
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



	}

	@Override
	public void onBackPressed() {
		dismiss();
	}


	@Override
	public void onClick(View v) {

		int id = v.getId();

		if (id == rlDate.getId()) {
			if(rinnaiReceivedProductDialog == null) {
				rinnaiReceivedProductDialog = new RinnaiCalendarDialog(context);
			}
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {
						tvVisitDate.setText(date.replace("/", "-"));
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});

				rinnaiReceivedProductDialog.show();
			}
		} else if( id == btnAddressSearch.getId()) {

			if(rinnaiAddressSearchDialog == null) {
				rinnaiAddressSearchDialog = new RinnaiAddressSearchDialog(context);
			}

			if(!rinnaiAddressSearchDialog.isShowing()) {
				rinnaiAddressSearchDialog.setCanceledOnTouchOutside(false);
				rinnaiAddressSearchDialog.setCancelable(false);

				rinnaiAddressSearchDialog.setDialogListener(new AddressSelectedListener() {
					@Override
					public void onSelected(AddressJusoData juso) {

						jusoData = juso;
						tvPostNo.setText(jusoData.getZipNo());
						tvAddr1.setText(jusoData.getRoadAddrPart1());
					}
				});

				rinnaiAddressSearchDialog.show();
			}

		}
		/*
		if(id == confirm.getId()) {
			String code = adapter.getSelectCode();
			if(null == code) {

			} else {
				if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type)) {
					dialogListener.onPositiveClicked(type, code);
				} else if (CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type)) {
					dialogListener.onPositiveClicked(type, code);
				} else if(CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
					String name = adapter.getSelectName();
					dialogListener.onPositiveClicked(name, code);
				} else if(CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
					String name = adapter.getSelectName();
					String modelCode = code;
					if(viewVisible == 0) {
						if(!cbEtc.isChecked() && !cbLng.isChecked() && !cbLpg.isChecked()) {
							llProductGas.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.red));
							return;
						} else {
							modelCode = String.format("%s%s000000001", modelCode.substring(0, 5), gasType);
						}
					}
					dialogListener.onPositiveClicked(name, modelCode);
				} else if(CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
					String name = adapter.getAgencyName();
					dialogListener.onPositiveClicked(name, code);
				}

				dismiss();
			}
		} else if( id == cancel.getId()) {
			dismiss();
		} else if(id == llEtc.getId()) {
			if(!cbEtc.isChecked()) {
				cbEtc.setChecked(true);
			}
		} else if(id == llLpg.getId()) {
			if(!cbLpg.isChecked()) {
				cbLpg.setChecked(true);
			}
		} else if(id == llLng.getId()) {
			if(!cbLng.isChecked()) {
				cbLng.setChecked(true);
			}
		}

		 */

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		/*
		lldetailTitle.setVisibility(View.VISIBLE);
		adapter.setSelectItem(position, selectItem);
		adapter.notifyDataSetChanged();

		if( viewVisible == 0) {
			llProductGas.setVisibility(View.VISIBLE);
		} else if (viewVisible == 4) {
			llProductGas.setVisibility(View.INVISIBLE);
		} else if (viewVisible == 8) {
			llProductGas.setVisibility(View.GONE);
		}

		 */
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
/*

		if(cbEtc == compoundButton) {

			if(b) {
				llProductGas.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				cbLpg.setChecked(false);
				cbLng.setChecked(false);
				gasType = "0";

			}
		} else if ( cbLpg == compoundButton) {
			if(b) {
				llProductGas.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				cbEtc.setChecked(false);
				cbLng.setChecked(false);
				gasType = "1";

			}
		} else if ( cbLng  == compoundButton) {
			if(b) {
				llProductGas.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				cbEtc.setChecked(false);
				cbLpg.setChecked(false);
				gasType = "2";

			}
		}



*/
	}

	@Override
	public void onResult(String result) {
		Log.w("onResult", result);
		networkConnecting = false;
		dismissProgress();

		ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);


		if (null != responseData ) {
			Log.w("onResult", "ok");
			if("OK".equals(responseData.getResultMessage())) {
				Object object = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, object);
				Type type = null;
				if ("getCategorization".equals(responseData.getResultType())) {

					type = new TypeToken<Categorizaion>() {}.getType();

					categori = new Gson().fromJson(str, type);

					masterAdapter = new CategorySpinnerAdapter(context, categori.getMaster());
					spMaster.setAdapter(masterAdapter);
					masterAdapter.notifyDataSetChanged();

					subAdapter = new CategorySpinnerAdapter(context, categori.getSub().get("-1"));

					spSub.setAdapter(subAdapter);
					subAdapter.notifyDataSetChanged();
					if(obj != null && obj.getlCat() != null) {
						int position = masterAdapter.getItemPosition(obj.getlCat());
						spMaster.setSelection(position);

						CategorizationResultVO vo = categori.getMaster().get(position);
						List<CategorizationResultVO> list = new ArrayList<CategorizationResultVO>();
						list.addAll(categori.getSub().get("-1"));

						if(!"-1".equals(vo.getCodeItem())) {
							list.addAll(categori.getSub().get(vo.getCodeItem()));
						}
						subAdapter = new CategorySpinnerAdapter(context, list);

						spSub.setAdapter(subAdapter);
						subAdapter.notifyDataSetChanged();


						position = subAdapter.getItemPosition(obj.getmCat());
						spSub.setSelection(position);
					}



					//search();
				} else if ("deleteService".equals(responseData.getResultType())) {
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
					alert_confirm.setMessage("삭제가 완료되었습니다.").setCancelable(false).setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialogListener.onPositiveClicked("delete", paramEntity);
									dismiss();
								}
							});
					AlertDialog alert = alert_confirm.create();
					alert.show();
				} else if("updateService".equals(responseData.getResultType())) {
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
					alert_confirm.setMessage("수정이 완료되었습니다.").setCancelable(false).setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialogListener.onPositiveClicked("update", paramEntity);
									dismiss();
								}
							});
					AlertDialog alert = alert_confirm.create();
					alert.show();
				} else if("insertService".equals(responseData.getResultType())) {
					type = new TypeToken<AgencyMenu06ServiceEntity>() {}.getType();

					final AgencyMenu06ServiceEntity acceptNo = new Gson().fromJson(str, type);
					paramEntity.setAcceptNo(acceptNo.getAcceptNo());
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
					alert_confirm.setMessage("등록이 완료되었습니다.").setCancelable(false).setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {


									dialogListener.onPositiveClicked("insert", paramEntity);
									dismiss();
								}
							});
					AlertDialog alert = alert_confirm.create();
					alert.show();
				}

			} else {
				showRinnaiDialog(context, "테스트 입니다.", "테스트중입니다.");
			}
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

			String url = String.format("%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_AGENCY, CommonValue.HTTP_CATEGORY);

			showProgress(context);

			networkConnecting = true;
			HttpClient.get(url, this);
		}

	}

	@Override
	public void cancelled() {

	}

	@Override
	public void exceptionOccured(Exception e) {

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
			subAdapter = new CategorySpinnerAdapter(context, list);

			spSub.setAdapter(subAdapter);
			subAdapter.notifyDataSetChanged();

		} else if (parent == spSub) {
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();
		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(id == btnSave.getId()) {
					btnSave.buttonClick(true);
				} else if(id == btnDelete.getId()) {
					btnDelete.buttonClick(true);
				}
				Log.d("test", "Action_DOWN " + id);
				break;
			case MotionEvent.ACTION_UP:
				Log.d("test", "Action_UP"  + id);
				Intent intent = null;
				if(id == btnSave.getId()) {

					btnSave.buttonClick(false);
					save();

				} else if(id == btnDelete.getId()) {
					btnDelete.buttonClick(false);
					delete();
				}
				break;

		}

		return false;
	}

	private void save() {

		if(!cbReceiveAs.isChecked() ) {
			showRinnaiDialog(context, context.getString(R.string.msg_title_noti),"동의 여부를 확인하여주세요.");
			return;
		} else if(etName.getText().length() < 1) {
			showRinnaiDialog(context, context.getString(R.string.msg_title_noti),"고객명을 입력해주세요.");
			return;
		} else if(etHp1.getText().length() < 1 || etHp2.getText().length() < 1 || etHp3.getText().length() < 1) {
			showRinnaiDialog(context, context.getString(R.string.msg_title_noti),"전화번호를 입력해주세요.");
			return;
		} else if(tvPostNo.getText().length() < 1 || tvAddr1.getText().length() < 1 || etaddr2.getText().length() < 1) {
			showRinnaiDialog(context, context.getString(R.string.msg_title_noti),"주소를 입력해주세요.");
			return;
		} else if(etContent.getText().length() < 1 ) {
			showRinnaiDialog(context, context.getString(R.string.msg_title_noti),"고장증상을 입력해주세요.");
			return;
		} else if(tvVisitDate.getText().length() < 1 ) {
				showRinnaiDialog(context, context.getString(R.string.msg_title_noti),"방문일자를 입력해주세요.");
			return;
		}



		/*
		 String comId = "R";
		String boardType = "A02";
	    int boardNum = 458841;
		String cpDate = "2020-04-14 10:58:15.937";
		String cpCustNo = userId;
		String opDate = "2020-04-14 12:37:37.107";
		String acceptNo = "A00264870";
		String custId = userId;
		String custName = "유진설비";
		String email = null;
		String hp1 = "010";
		String hp2 = "3723";
		String hp3 = "6663";
		String lCat = "C005000000";
		String mCat = "C005002000";
		String modelCode = "     ";
		String modelName = "331-20mf";
		String zipCode = "05252";
		String fullloadAddr = "서울특별시 강동구 고덕로14가길 6-14, (암사동) 459-33  201호";
		String roadAddr1 = "서울특별시 강동구 고덕로14가길 6-14";
		String roadAddr2 = "(암사동)";
		String addrDet = "459-33  201호";
		String roadAddr = "서울특별시 강동구 고덕로14가길 6-14 (암사동)";
		String jibunAddr = "서울특별시 강동구 암사동 459-13";
		//String visitReqDate = "2020-04-14 00:00:00.0";
		String counselContents = "누수";
		String counselAnswer = null;
		String remoteIp = "[192.168.0.100]";
		String connPath = "004";
		String precessState = "A5";
		String payagreeYn = "Y";
		 */

		paramEntity = new AgencyMenu06ServiceEntity();

		/*
		vo.setBoardNum(boardNum);
		vo.setAcceptNo(acceptNo);

		vo.setHp1(hp1);
		vo.setHp2(hp2);
		vo.setHp3(hp3);


		vo.setlCat(lCat);
		vo.setmCat(mCat);

		vo.setBoardNum(boardNum);

		vo.setRemoteIp(remoteIp);
*/

		String addrDetail = etaddr2.getText().toString();

		if(jusoData == null) {
			paramEntity.setFullAddr(String.format("%s %s", obj.getRoadAddr(), addrDetail));
			paramEntity.setJibunAddr(obj.getJibunAddr());
			paramEntity.setRoadAddr2(obj.getRoadAddr2());
		} else {
			paramEntity.setFullAddr(String.format("%s %s", jusoData.getRoadAddr(), addrDetail));

			paramEntity.setJibunAddr(jusoData.getJibunAddr());
			paramEntity.setRoadAddr2(jusoData.getRoadAddrPart2());
		}

		paramEntity.setCustName(etName.getText().toString());
		paramEntity.setContent(etContent.getText().toString());
		paramEntity.setHp1(etHp1.getText().toString());
		paramEntity.setHp2(etHp2.getText().toString());
		paramEntity.setHp3(etHp3.getText().toString());

		paramEntity.setZipCode(tvPostNo.getText().toString());
		paramEntity.setRoadAddr1(tvAddr1.getText().toString());

		paramEntity.setAddrDetail(addrDetail);


		etEmail = (EditText) findViewById(R.id.et_agency_menu_06_email);

		if(obj != null) {

			paramEntity.setBoardNum(obj.getBoardNum());
			paramEntity.setAcceptNo(obj.getAcceptNo());
			paramEntity.setAcceptType(obj.getBoardType());
		} else {

		}


		CategorizationResultVO master = (CategorizationResultVO)spMaster.getSelectedItem();
		CategorizationResultVO sub = (CategorizationResultVO) spSub.getSelectedItem();


		paramEntity.setlCat(master.getCodeItem());

		if("-1".equals(sub.getCodeItem()) ) {
			paramEntity.setmCat("ALL");
		} else {
			paramEntity.setmCat(sub.getCodeItem());
		}

		paramEntity.setCustCode(custCode);
		paramEntity.setModelName(etModelName.getText().toString());
		paramEntity.setVisitReqDate(tvVisitDate.getText().toString());
		paramEntity.setContent(etContent.getText().toString());

		paramEntity.setComId(CommonValue.WMS_I_COM_ID);
		paramEntity.setRemoteIp(getLocalIpAddress());


		String emailHead = null;
		String emailDomain = null;

		String email = etEmail.getText().toString();

		if(email != null && email.indexOf("@") > -1) {
			String[] emailSprit = email.split("@");
			paramEntity.setEmailHead(emailSprit[0]);
			paramEntity.setEmalDomain(emailSprit[1]);
		}


		if(!networkConnecting) {

			String strResponse = ParseUtil.getJSONFromObject(paramEntity);

			String httpHost = HttpClient.getCurrentSsid(context);

			String url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_SERVICE, custCode);

			showProgress(context);

			networkConnecting = true;

			if(obj != null) {
				HttpClient.put(url, AgencyMenu06ServiceDialog.this, strResponse);
			} else {
				HttpClient.post(url, AgencyMenu06ServiceDialog.this, strResponse);
			}

		}
/*

{"detBdNmList":"","engAddr":"23, Seokjeong-ro 461beon-gil, Michuhol-gu, Incheon",
"rn":"석정로461번길","emdNm":"주안동","zipNo":"22124","roadAddrPart2":" (주안동)","emdNo":"01",
"sggNm":"미추홀구","jibunAddr":"인천광역시 미추홀구 주안동 19-137 린나이 어린이집",
"siNm":"인천광역시","roadAddrPart1":"인천광역시 미추홀구 석정로461번길 23","bdNm":"린나이 어린이집",
"admCd":"2817710500","udrtYn":"0","lnbrMnnm":"19","roadAddr":"인천광역시 미추홀구 석정로461번길 23 (주안동)",
"lnbrSlno":"137","buldMnnm":"23","bdKdcd":"0","liNm":"","rnMgtSn":"281774253355","mtYn":"0",
"bdMgtSn":"2817010600100190137138573","buldSlno":"0"}
 */


	}

	private void delete() {
		if(obj.getBoardNum() == 0) {
			return;
		} else {

			paramEntity = new AgencyMenu06ServiceEntity();

			paramEntity.setComId(CommonValue.WMS_I_COM_ID);
			paramEntity.setCustCode(custCode);
			paramEntity.setAcceptNo(obj.getAcceptNo());
			paramEntity.setBoardNum(obj.getBoardNum());

			if(!networkConnecting) {

				String strResponse = ParseUtil.getJSONFromObject(paramEntity);

				String httpHost = HttpClient.getCurrentSsid(context);

				String url = String.format("%s/%s/%s/%s", httpHost, CommonValue.HTTP_AOS, CommonValue.HTTP_SERVICE, custCode);

				showProgress(context);

				networkConnecting = true;

				HttpClient.delete(url, AgencyMenu06ServiceDialog.this, strResponse);


			}
		}

	}


	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}



}
