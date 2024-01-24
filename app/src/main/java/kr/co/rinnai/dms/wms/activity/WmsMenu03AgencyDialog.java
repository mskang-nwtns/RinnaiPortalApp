package kr.co.rinnai.dms.wms.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseActivity;
import kr.co.rinnai.dms.activity.BaseDialog;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.callback.IAsyncCallback;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.LogUtil;
import kr.co.rinnai.dms.common.util.ObjectComparator;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.wms.adapter.OrderReportAgencyListAdapter;
import kr.co.rinnai.dms.wms.adapter.OrderReportListAdapter;
import kr.co.rinnai.dms.wms.adapter.WmsMenu03AgencyDialogListAdapter;

import kr.co.rinnai.dms.wms.model.OrderReportResult;
import kr.co.rinnai.dms.wms.model.WmsMenu03AgencyOrderReport;
import kr.co.rinnai.dms.wms.model.WmsMenu03ReadingListEntity;

public class WmsMenu03AgencyDialog extends Dialog implements OnClickListener, IAsyncCallback<String> {
	private Context context;
	private List<WmsMenu03AgencyOrderReport> list;
	private ListView lvOrderReport;
	private RelativeLayout btnClose;
	private TextView tvModelName;
	private WmsMenu03AgencyDialogListAdapter adapter;
	private String custName;
	private TextView tvCustName;

	private boolean networkConnecting = false;

	private String userName = "20150001";

	public WmsMenu03AgencyDialog(Context context, List<WmsMenu03AgencyOrderReport> list, String custName) {
		super(context);
		this.context = context;
		this.list = list;
		this.custName = custName;


	}

	@Override

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wms_menu_03_agency_dialog);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		tvCustName = (TextView) findViewById(R.id.tv_wms_nemu_03_agency_dialog_cust_name);
		lvOrderReport = (ListView)findViewById(R.id.lv_agency_dialog_order_report);
		tvModelName = (TextView) findViewById(R.id.tv_reading_info_model_name);
		btnClose = (RelativeLayout) findViewById(R.id.btn_wms_menu_agency_dialog_info_close);

		tvCustName.setText(custName);
		btnClose.setOnClickListener(WmsMenu03AgencyDialog.this);

		adapter = new WmsMenu03AgencyDialogListAdapter(context, list);
		lvOrderReport.setAdapter(adapter);
		lvOrderReport.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
				WmsMenu03AgencyOrderReport temp = (WmsMenu03AgencyOrderReport)adapter.getItem(position);
				if("수동찾기".equals(temp.getOrderLocation())) {

				} else {
					String location = null;
					int idx = -1;
					for(int n = 0; n < adapter.getCount(); n ++) {

						WmsMenu03AgencyOrderReport object = (WmsMenu03AgencyOrderReport)adapter.getItem(n);

						if(object.isRead()) {
							location = object.getOrderLocation();
							idx = n;
							break;
						}


					}
					WmsMenu03AgencyOrderReport object = (WmsMenu03AgencyOrderReport)adapter.getItem(position);

					if(location == null || object.getOrderLocation().equals(location)) {
						adapter.clickPosition(position, "");
						adapter.notifyDataSetChanged();
					} else {
						onClearSelectedList();
						adapter.clickPosition(position, "");
						adapter.notifyDataSetChanged();
						//showRinnaiDialog(WmsMenu03Activity.this, getString(R.string.msg_title_noti), getString(R.string.value_not_equals_select_location));
					}

				}
			}
		});

	}

	@Override
	public void onBackPressed() {
		dismiss();
	}


	@Override
	public void onClick(View v) {

		int id = v.getId();

		if(id == btnClose.getId()) {
			dismiss();
		}

	}

	public void readingBarcode(String location) {
		List<Integer> readLocationList = new ArrayList<Integer>();
		List<Integer> clickList = new ArrayList<Integer>();

		List<Integer> idxList = new ArrayList<Integer>();
		for( int i = 0; i < list.size(); i ++) {
			WmsMenu03AgencyOrderReport object = list.get(i);

			if(object.isRead()) {
				clickList.add(i);
				if(location.equals(object.getOrderLocation())) {
					idxList.add(i);
					readLocationList.add(i);
				}
			}
		}

		if( clickList.size() == 0 && readLocationList.size()  == 0) {
//			showRinnaiDialog(context, context.getString(R.string.msg_title_noti), "로케이션을 선택 하신 후 바코드를 읽어 주세요." );
		} else if (clickList.size() > 0 && readLocationList.size()  == 0) {
//			showRinnaiDialog(context, context.getString(R.string.msg_title_noti), "선택하신 로케이션과 동일한 로케이션의 바코드를 읽어주세요." );
		} else {
			ArrayList<Integer> positionList = new ArrayList<Integer>();
			ArrayList<WmsMenu03AgencyOrderReport> paramList = new ArrayList<WmsMenu03AgencyOrderReport>();


			JSONArray arrayParam = new JSONArray();

//			for (int n = 0; n < paramList.size(); n++) {

//			}


			for (int n = 0; n < readLocationList.size(); n++) {
				try {
					WmsMenu03AgencyOrderReport orderReportResult = list.get(readLocationList.get(n));

					int idx = idxList.get(n);
					JSONObject jsonObject = ParseUtil.getCompletData(orderReportResult, userName, idx);
					arrayParam.put(jsonObject);

					Log.d("onItemClick", "HTTPClient 작업 필요");
					//positionList.add(n);
					paramList.add(orderReportResult);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			//int position = lvOrderReport.getSelectedItemPosition();


			//lvOrderReport.setSelection(position);
			Collections.sort(readLocationList);
			Collections.reverse(readLocationList);



			JSONArray array = new JSONArray();

			for (int n = 0; n < paramList.size(); n++) {
				JSONObject jsonObject = ParseUtil.getCompletData(paramList.get(n), userName, n);
				array.put(jsonObject);
			}
			String url = String.format("%s/%s/%s/%s", CommonValue.HTTP_HOST, CommonValue.HTTP_WMS, CommonValue.HTTP_ORDER_REPORT, CommonValue.HTTP_LOCATION);

//			2019.11.07 내부 테스트를 위한 서버 전송 X
//			showProgress(context);
			networkConnecting = true;
			HttpClient.post(url, this, arrayParam.toString());
			Log.d("onItemClick", array.toString());
			//break;
		}
	}

	/**
	 * 선택된 로케이션 정보 초기화
	 */
	public void onClearSelectedList() {
		if(adapter != null) {
			for (int n = 0; n < adapter.getCount(); n++) {
				WmsMenu03AgencyOrderReport object = (WmsMenu03AgencyOrderReport) adapter.getItem(n);
				if (object.isRead()) {
					adapter.clickPosition(n, "");
				}
			}
			adapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onResult(String result) {
		if (!TextUtils.isEmpty(result)) {
//			dismissProgress();
			networkConnecting = false;

			ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
			if (null != responseData ) {
				Log.w("onResult", "ok");
				if("200".equals(responseData.getResultCode())) {
					if(responseData.getData() != null) {
						Object obj = responseData.getData();

						String str = JsonParserManager.objectToJson(Object.class, obj);

						Type listType = null;
						if ("setOrderReportIndividualLocationWork".equals(responseData.getResultType())) {
							listType = new TypeToken<ArrayList<WmsMenu03ReadingListEntity>>() {}.getType();
							List<WmsMenu03ReadingListEntity> list = new Gson().fromJson(str, listType);

							for(int i = 0; i < list.size(); i ++) {
								WmsMenu03ReadingListEntity entity = list.get(i);

							}

							removePickingComplet(list);

						}


					}
				} else {
//					showRinnaiDialog(context, context.getString(R.string.msg_title_noti), responseData.getResultMessage());
				}


			} else {

			}
		}
	}

	@Override
	public void exceptionOccured(Exception e) {
//		showRinnaiDialog(context, context.getString(R.string.msg_title_noti), "네트워크 연결이 원할하지 않습니다.");
//		dismissProgress();
	}

	@Override
	public void cancelled() {

//		dismissProgress();
	}


	private void removePickingComplet(List<WmsMenu03ReadingListEntity> list) {

		List<WmsMenu03AgencyOrderReport> viewList = null;

		viewList = adapter.getList();

		List<Integer> viewIdx = new ArrayList<Integer>();




		for (int i = 0; i < list.size(); i++) {
			WmsMenu03ReadingListEntity entity = list.get(i);
			for (int n = 0; n < viewList.size(); n++) {

				WmsMenu03AgencyOrderReport orderReportResult = viewList.get(n);


				if (entity.getInModelCode().equals(orderReportResult.getModelCode())) {

				}



//				if (otherObject.getOrderDate().equals(orderReportResult.getOrderDate()) &&
//						otherObject.getOrderNo().equals(orderReportResult.getOrderNo()) &&
//						otherObject.getOrderLocation().equals(orderReportResult.getOrderLocation()) &&
//						otherObject.getGasType().equals(orderReportResult.getGasType()) &&
//						otherObject.getModelCode().equals(orderReportResult.getModelCode()) &&
//						otherObject.getOrderDetail().equals(orderReportResult.getOrderDetail())&&
//						entity.isStatus()) {
					viewIdx.add(entity.getIndex());
					Log.d("onItemClick", "HTTPClient 작업 필요");

					break;

//				}

				//orderReportResultList.remove(idx);

			}
		}

		Collections.sort(viewIdx);
		Collections.reverse(viewIdx);
		for (int n = 0; n < viewIdx.size(); n++) {
			int idx = viewIdx.get(n);
			//orderReportResultList.remove(idx);

			adapter.setCompleted(idx);
		}


//		showRinnaiDialog(context, context.getString(R.string.msg_title_noti), String.format("Picking 작업 %d건 중 %d건을\n완료하였습니다.",list.size(), viewIdx.size() ));


		Collections.sort(viewIdx);
		Collections.reverse(viewIdx);


		//현재 선택 탭이 아닌 탭 목록 삭제를 위한 선택된 row 삭제
		for (int i = 0; i < viewIdx.size(); i++) {


			adapter.setCompleted(viewIdx.get(i));

		}



		adapter.notifyDataSetChanged();

	}

}
