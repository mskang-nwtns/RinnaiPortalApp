package kr.co.rinnai.dms.common.custom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseDialog;
import kr.co.rinnai.dms.activity.RinnaiDialog;
import kr.co.rinnai.dms.adapter.SearchListAdapter;
import kr.co.rinnai.dms.adapter.SimpleSpinnerAdapter;
import kr.co.rinnai.dms.adapter.StockStoreFragmentAdapter;
import kr.co.rinnai.dms.aos.activity.AgencyMenu04Activity;
import kr.co.rinnai.dms.aos.adapter.AgencyMenu04SpinnerAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu04ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu04SalesInfoTranEntity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.callback.IAsyncCallback;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.listener.ServiceDialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu03Activity;
import kr.co.rinnai.dms.eos.model.ItemCode;
import kr.co.rinnai.dms.eos.model.SalesAgencyInfoResult;
import kr.co.rinnai.dms.eos.model.SalesModelInfoResult;
import kr.co.rinnai.dms.eos.model.StockByStoreResult;


/**
 * Sales Information Transfer Dialog </br>
 * 판매정보 이관 정보 입력 Popup
 */
public class RinnaiSalesInforTranDialog extends BaseDialog implements IAsyncCallback<String>, AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener, OnClickListener, CompoundButton.OnCheckedChangeListener {


	private Spinner spTranMeasure, spTranSales;

	private AgencyMenu04SpinnerAdapter adapterTranMeasure, adapterTranSales;

	private ServiceDialogListener dialogListener;
	private SearchListAdapter adapter;
	private ListView listview;
	private AgencyMenu04ListEntity obj;
	private String type;
	private Context context;

	private RelativeLayout confirm;
	private RelativeLayout cancel;

	private TextView tvSalesDate, tvMeasureDate ;
	private RelativeLayout rlSalesDate, rlMeasureDate, rlModelName;


	private boolean isFirstTranMeasure = true;
	private boolean isFirstTranSales = true;

	private RinnaiCalendarDialog rinnaiReceivedProductDialog;

	private EditText etModelName, etCusRef;
	private RelativeLayout btnSearch;


	private RinnaiDialog rinnaiDialog;

	private String selectModelCode;


	private AgencyMenu04SalesInfoTranEntity tranEntity;


	public RinnaiSalesInforTranDialog(Context context, String productBarcode) {
		super(context);


	}

	public RinnaiSalesInforTranDialog(Context context, AgencyMenu04ListEntity objectList, String type) {
		super(context);
		this.context = context;
		this.obj = objectList;
		this.type = type;

	}

	public RinnaiSalesInforTranDialog(Context context, AgencyMenu04ListEntity objectList, String type, int gasVisible) {
		super(context);
		this.context = context;
		this.obj = objectList;
		this.type = type;

	}

	public void setDialogListener(ServiceDialogListener dialogListener){
		this.dialogListener = dialogListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_sales_info_tran);

		confirm = (RelativeLayout)findViewById(R.id.btn_info_tran_confirm);
		cancel = (RelativeLayout)findViewById(R.id.btn_info_tran_cancel);

		spTranMeasure  = (Spinner) findViewById(R.id.sp_sales_info_tran_measure);
		spTranSales = (Spinner) findViewById(R.id.sp_sales_info_tran_sales);

		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);

		//조치여부 스피너 view 생성
		List<ItemCode> listMeasure = getCodeList();
		adapterTranMeasure = new AgencyMenu04SpinnerAdapter(context, listMeasure);
		spTranMeasure.setAdapter(adapterTranMeasure);
		spTranMeasure.setOnItemSelectedListener(this);

		//조치일자 View 생성
		rlMeasureDate = (RelativeLayout) findViewById(R.id.rl_sales_info_tran_measure_date);
		tvMeasureDate = (TextView) findViewById(R.id.tv_sales_info_tran_measure_date);
		tvMeasureDate.setText("");
		rlMeasureDate.setOnClickListener(this);
		rlMeasureDate.setClickable(false);
		rlMeasureDate.setBackground(new ColorDrawable(0x1f000000));

		//판매여부 스피너 view 생성
		List<ItemCode> listSales = getSalesList();
		adapterTranSales = new AgencyMenu04SpinnerAdapter(context, listSales);
		spTranSales.setAdapter(adapterTranSales);
		spTranSales.setOnItemSelectedListener(this);
		spTranSales.setEnabled(false);
		spTranSales.setBackground(new ColorDrawable(0x1f000000));

		//판매일자 View 생성
		rlSalesDate = (RelativeLayout) findViewById(R.id.rl_sales_info_tran_sales_date);
		tvSalesDate = (TextView) findViewById(R.id.tv_sales_info_tran_sales_date);
		tvSalesDate.setText("");
		rlSalesDate.setOnClickListener(this);
		rlSalesDate.setClickable(false);
		rlSalesDate.setBackground(new ColorDrawable(0x1f000000));

		rlModelName = (RelativeLayout) findViewById(R.id.rl_sales_info_tran_model_name);
		etModelName = (EditText) findViewById(R.id.et_sales_info_tran_model_name);
		etModelName.setText("");
		etModelName.setEnabled(false);
		rlModelName.setBackground(new ColorDrawable(0x1f000000));

		btnSearch =(RelativeLayout) findViewById(R.id.btn_sales_info_tran_search);
		btnSearch.setOnClickListener(this);
		btnSearch.setClickable(false);

		etCusRef = (EditText) findViewById(R.id.et_sales_info_tran_cus_ref);
		etCusRef.setText("");
		etCusRef.setEnabled(true);

		etModelName.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			// 입력되는 텍스트에 변화가 있을 때
				selectModelCode = null;
			}

			@Override

			public void afterTextChanged(Editable arg0) {
				// 입력이 끝났을 때
			}

			@Override

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// 입력하기 전에
			}

		});


		int smsPosition = 0;
		for(int i = 0; i <listMeasure.size(); i ++) {
			ItemCode itemCode = listMeasure.get(i);
			if(itemCode.getCodeItem().equals(obj.getCusSmsYn())) {
				smsPosition = i;
				break;
			}

		}

		spTranMeasure.setSelection(smsPosition);

		if("1".equals(obj.getCusSmsYn()) || "2".equals(obj.getCusSmsYn())) {
			tvMeasureDate.setText(obj.getCusActDate());
			setEnableMeasureDate();
			setEnableSaleInfo();
			if("Y".equals(obj.getCusSaleYn()) || "1".equals(obj.getCusSaleYn()) ) {

				tvSalesDate.setText(obj.getCusSaleDate());
				etModelName.setText(obj.getModelName());
				selectModelCode = obj.getSaleModelNo();
				spTranSales.setSelection(1);

				setEnableAll();

			}
		} else if("9".equals(obj.getCusSmsYn())) {
			tvMeasureDate.setText(obj.getCusActDate());
			setEnableMeasureDate();
		}

		if("1".equals(obj.getCusSaleYn())) {
			spTranSales.setSelection(1);
		}

		etCusRef.setText(obj.getCusRef());
		if(obj.getModelName() != null) {
			etModelName.setSelection(etModelName.getText().length());
		}


		//tvMeasureDate.setText(obj.geCUs());


/*
		listMeasure.add("-"); //0
		listMeasure.add("전화상담"); //1
		listMeasure.add("방문상담"); //2
		listMeasure.add("기타"); //8
		listMeasure.add("고객취소"); //9



		listSales.add("미판매"); //N
		listSales.add("판매");  //Y
*/



	//	spTranMeasure.setOnItemSelectedListener();
	//	//spTranMeasure.setOnItemSelectedListener(context);
		//spTranSales.setOnItemSelectedListener(;

	}

	@Override
	public void onBackPressed() {
		dismiss();
	}


	@Override
	public void onClick(View v) {

		if(rinnaiReceivedProductDialog == null) {
			rinnaiReceivedProductDialog = new RinnaiCalendarDialog(context);
		}

		int id = v.getId();

		if( id == cancel.getId()) {
			dismiss();
		} else if (id == confirm.getId()) {
			AgencyMenu04ListEntity entity = (AgencyMenu04ListEntity) obj;


			ItemCode measure = (ItemCode) spTranMeasure.getSelectedItem();
			ItemCode sales = (ItemCode) spTranSales.getSelectedItem();

			String cusSmsYn = measure.getCodeItem();
			String cusSaleYn = sales.getCodeItem();


			if("N".equals(cusSaleYn)) {
				cusSaleYn = "0";
			} else {
				cusSaleYn = "1";
			}

			String mesureDate = (String) tvMeasureDate.getText();
			String salesDate = (String) tvSalesDate.getText();
			String modelName = etModelName.getText().toString();
			String cusRef = etCusRef.getText().toString();

			String modelCode = selectModelCode;
			String centerCode = entity.getCenterCode();
			String actDate = entity.getActDate();
			String asSeq = entity.getAsSeq();
			String transGubun = entity.getTransGubun();

			tranEntity = new AgencyMenu04SalesInfoTranEntity();
//
			tranEntity.setCusSmsYn(cusSmsYn);
			tranEntity.setCusSaleYn(cusSaleYn);
			tranEntity.setCusActDate(mesureDate);
			tranEntity.setCusSaleDate(salesDate);
			tranEntity.setModelName(modelName);
			tranEntity.setModelCode(modelCode);
			tranEntity.setCusRef(cusRef);

			tranEntity.setCenterCode(centerCode);
			tranEntity.setActDate(actDate);
			tranEntity.setAsSeq(asSeq);
			tranEntity.setTransGubun(transGubun);

			if(modelCode == null) {
				tranEntity.setModelName("");
			}



			String strParams = ParseUtil.getJSONFromObject(tranEntity);

			Log.d("판매이관", String.format("strParams : %s", strParams ));
			String url = "";

			url = String.format("%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_TRANSFER_SALE_INFOMATION);

			showProgress(context);

			HttpClient.post(url, this, strParams);




		} else if (id == rlMeasureDate.getId()) {
			Log.d("판매이관", "rlSalesDate onClick : " );
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {

						String sDate = date.replace("/", "-");
						tvMeasureDate.setText(sDate);
//						getTransferSaleInfomation();
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});

				rinnaiReceivedProductDialog.show();
			}
		} else if (id == rlSalesDate.getId()) {
			Log.d("판매이관", "rlSalesDate onClick : " );
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {

						String sDate = date.replace("/", "-");
						tvSalesDate.setText(sDate);
//						getTransferSaleInfomation();
					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});

				rinnaiReceivedProductDialog.show();
			}
		} else if(id == btnSearch.getId()) {
			String value = etModelName.getText().toString();
			String url = "";

			url = String.format("%s/%s/%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_SOS, CommonValue.HTTP_STOCK, CommonValue.HTTP_INFO, CommonValue.HTTP_MODEL, value.toUpperCase());

			showProgress(context);
            HttpClient.get(url, this);
           // HttpClient.get(url, );

		}



	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

	}

	private List<ItemCode> getCodeList() {

		List<ItemCode> list = new ArrayList<ItemCode>();
		ItemCode code = new ItemCode();

		code.setCodeItem("0");
		code.setCodeName("-");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("1");
		code.setCodeName("전화상담");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("2");
		code.setCodeName("방문상담");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("8");
		code.setCodeName("기타");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("9");
		code.setCodeName("고객취소");
		list.add(code);

		return list;
	}


	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


		Log.d("dms", "onItemSelected");

		if(isFirstTranMeasure) {
			isFirstTranMeasure = false;
		} else if (isFirstTranSales) {
			isFirstTranSales = false;
		} else {

			if (parent == spTranMeasure) {
				//조치여부 미 선택 시 나머지 입력 폼 dim 처리
				if(position == 0) {


					tvMeasureDate.setText("");
					rlMeasureDate.setBackground(new ColorDrawable(0x1f000000));
					rlMeasureDate.setClickable(false);


					tvSalesDate.setText("");

					rlSalesDate.setBackground(new ColorDrawable(0x1f000000));
					rlSalesDate.setClickable(false);


					spTranSales.setBackground(new ColorDrawable(0x1f000000));
					spTranSales.setEnabled(false);

					etModelName.setText("");
					etModelName.setEnabled(false);
					rlModelName.setBackground(new ColorDrawable(0x1f000000));

					btnSearch.setClickable(false);


				//조치여부 고객 취소 선택 시 조치 일자만 입력 가능 나머지 dim 처리
				} else if (position == 4) {

					setEnableMeasureDate();

					tvSalesDate.setText("");
					rlSalesDate.setBackground(new ColorDrawable(0x1f000000));
					rlSalesDate.setClickable(false);

					spTranSales.setBackground(new ColorDrawable(0x1f000000));
					spTranSales.setEnabled(false);

					etModelName.setText("");
					etModelName.setEnabled(false);
					rlModelName.setBackground(new ColorDrawable(0x1f000000));

					btnSearch.setClickable(false);

				//나머지 선택 시 조치 일자, 판매여부 입력 가능 나머지 dim 처리
				} else if (position > 0) {

					setEnableMeasureDate();

					setEnableSaleInfo();

					if( spTranSales.getSelectedItemPosition() == 1 ) {
						setEnableSaleDate();
						setEnableModelName();
					} else {

						etModelName.setText("");
						etModelName.setEnabled(false);
						rlModelName.setBackground(new ColorDrawable(0x1f000000));

						btnSearch.setClickable(false);
					}

				}
				Log.d("판매이관", "spTranMeasure onItemSelected : " + position);


			} else if (parent == spTranSales) {

				if(position == 0) {

					tvSalesDate.setText("");

					rlSalesDate.setBackground(new ColorDrawable(0x1f000000));
					rlSalesDate.setClickable(false);

					etModelName.setText("");
					rlModelName.setBackground(new ColorDrawable(0x1f000000));
					rlModelName.setClickable(false);

				} else if (position == 1) {

					setEnableSaleDate();


					setEnableModelName();
					/*
					rlModelName.setBackgroundResource(R.drawable.calendar_selector);
					etModelName.setEnabled(true);
					btnSearch.setClickable(true);

					 */

				}
				Log.d("판매이관", "spTranSales onItemSelected : " + position);

			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		Log.d("dms", "onNotingSelected");
	}

	private List<ItemCode> getSalesList() {

		List<ItemCode> list = new ArrayList<ItemCode>();
		ItemCode code = new ItemCode();

		code.setCodeItem("N");
		code.setCodeName("미판매");
		list.add(code);

		code = new ItemCode();
		code.setCodeItem("Y");
		code.setCodeName("판매");
		list.add(code);

		return list;

	}

    @Override
    public void onResult(String result) {
        Log.w("onResult", result);

        dismissProgress();

        ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);


        if (null != responseData ) {
            Log.w("onResult", "ok");
            if("OK".equals(responseData.getResultMessage())) {
                Object obj = responseData.getData();

                String str = JsonParserManager.objectToJson(Object.class, obj);
                Type type = null;
                if ("getSalesModelInfo".equals(responseData.getResultType()))  {
                    type = new TypeToken<ArrayList<SalesModelInfoResult>>(){}.getType();
                    List<SalesModelInfoResult> list = new Gson().fromJson(str, type);

                    if(list.size() == 1) {
                        SalesModelInfoResult modelInfo = list.get(0);

                        etModelName.setText(modelInfo.getModelName());
						selectModelCode = modelInfo.getModelCode();
						etModelName.setSelection(etModelName.getText().length());

                    } else if(list.size() > 1 ){
                        showListPopup(list);
                    }

                } else if ("setTransferSaleInfo".equals(responseData.getResultType())) {
					AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
					alert_confirm.setMessage("등록이 완료되었습니다.").setCancelable(false).setPositiveButton("확인",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {


									dialogListener.onPositiveClicked("insert", tranEntity);
									dismiss();
								}
							});
					AlertDialog alert = alert_confirm.create();
					alert.show();
				}

            } else {
                showRinnaiDialog(context, "알림", responseData.getResultMessage());
            }
        }
    }

    private void showListPopup(Object obj) {
        RinnaiSearchListDialog rinnaiReceivedProductDialog;
        rinnaiReceivedProductDialog = new RinnaiSearchListDialog(context, obj, CommonValue.AOS_NOW_VIEW_NAME_MODEL_05);
        rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
        rinnaiReceivedProductDialog.setCancelable(false);
        rinnaiReceivedProductDialog.setDialogListener(new DialogListener() {  // MyDialogListener 를 구현
            @Override
            public void onPositiveClicked(String name, final String code) {
                etModelName.setText(name);
				selectModelCode = code;
				etModelName.setSelection(etModelName.getText().length());
            }

            @Override
            public void onPositiveClicked(String barcode, String qty, int position) {
                //productAdd(barcode, qty);
            }

            @Override
            public void onPositiveClicked(String barcode, String modelName, String qty) {

            }
        });
        rinnaiReceivedProductDialog.show();

    }

    @Override
    public void exceptionOccured(Exception e) {

    }

    @Override
    public void cancelled() {

    }

    private void setEnableAll() {

		setEnableMeasureDate();

		setEnableSaleInfo();

		setEnableSaleDate();

		setEnableModelName();
	}

	/**
	 * 판매여부 사용 가능 상태 변경
	 */
	private void setEnableSaleInfo() {
		spTranSales.setBackgroundResource(R.drawable.calendar_selector);
		spTranSales.setEnabled(true);


	}

	/**
	 * 판매일자 사용 상태 변경
	 */
	private void setEnableSaleDate() {
		rlSalesDate.setClickable(true);
		rlSalesDate.setBackgroundResource(R.drawable.calendar_selector);

	}

	/**
	 * 모델명 사용 가능 사태 변경
	 */
	private void setEnableModelName() {
		rlModelName.setBackgroundResource(R.drawable.calendar_selector);
		etModelName.setEnabled(true);
		btnSearch.setClickable(true);
	}

	/**
	 * 조치일자 사용 가능 상태 변경
	 */
	private void setEnableMeasureDate() {
		rlMeasureDate.setClickable(true);
		rlMeasureDate.setBackgroundResource(R.drawable.calendar_selector);
	}
}
