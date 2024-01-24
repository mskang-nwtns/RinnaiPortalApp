package kr.co.rinnai.dms.common.custom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseDialog;
import kr.co.rinnai.dms.adapter.AddressSearchListAdapter;
import kr.co.rinnai.dms.aos.activity.AgencyMenu07Activity;
import kr.co.rinnai.dms.aos.activity.AgencyMenu07FragmentFirst;
import kr.co.rinnai.dms.aos.activity.AgencyMenu07FragmentSecond;
import kr.co.rinnai.dms.aos.model.AgencyMenu01ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteDetailInfo;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.callback.IAsyncCallback;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.AddressCommonData;
import kr.co.rinnai.dms.common.http.model.AddressData;
import kr.co.rinnai.dms.common.http.model.AddressJusoData;
import kr.co.rinnai.dms.common.http.model.AddressResults;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.AddressSelectedListener;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.eos.model.ItemCode;

public class RinnaiAddressSearchDialog extends BaseDialog implements IAsyncCallback<String>, View.OnClickListener, AdapterView.OnItemClickListener, AbsListView.OnScrollListener{

	private boolean networkConnecting = false;

	private Context context;

	private RelativeLayout btnConfirm, btnCancel;

	private AddressSelectedListener listener;

	private RelativeLayout btnSearch;

	private EditText etKeyword;

	private int currentPage = 0;

	private ListView lvZipCode;
	private LinearLayout lvInit;

	private AddressSearchListAdapter adapter;

	private List<AddressJusoData> adapterList = null;

	private boolean lastItemVisibleFlag = false;  // 리스트 스크롤이 마지막 셀(맨 바닥)로 이동했는지 체크할 변수

	private String keyword;

	private boolean mLockListView = false;          // 데이터 불러올때 중복안되게 하기위한 변수

	private int type = -1;
	private AddressJusoData jusoData;

	public RinnaiAddressSearchDialog(Context context) {
		super(context);
		this.context = context;


	}

	public RinnaiAddressSearchDialog(Context context, int type) {
		super(context);
		this.context = context;
		this.type = type;


	}
	public void setDialogListener(AddressSelectedListener listener){
		this.listener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_address_search);

		btnSearch = (RelativeLayout) findViewById(R.id.btn_common_address_search);
		etKeyword = (EditText)findViewById(R.id.et_common_address_search_keyword);
		btnSearch.setOnClickListener(RinnaiAddressSearchDialog.this);

		lvZipCode = (ListView) findViewById(R.id.list_zipcode);

		lvZipCode.setOnScrollListener(RinnaiAddressSearchDialog.this);
		lvZipCode.setOnItemClickListener(RinnaiAddressSearchDialog.this);

		lvInit = (LinearLayout) findViewById(R.id.lv_init);
/*
		btnConfirm = findViewById(R.id.btn_calendar_confirm);
		btnCancel = findViewById(R.id.btn_calendar_cancel);

		btnSearch.setOnClickListener(RinnaiAddressSearchDialog.this);
		btnCancel.setOnClickListener(RinnaiAddressSearchDialog.this);
 */
		//listener.onCalendarView();

	}

	@Override
	public void onBackPressed() {
		dismiss();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == btnSearch.getId()) {
			keyword = etKeyword.getText().toString();

			if (keyword.length() == 0) {

			} else {
				currentPage = 0;
				adapter = null;
				getAddressItem(keyword);

			}

		}

	}

	@Override
	public void onResult(String result) {
		Log.w("onResult", result);
		networkConnecting = false;
		mLockListView = false;
		dismissProgress();

		Type type = null;

		if(result.indexOf("getSiteAddressCheck") == -1) {
			type = new TypeToken<AddressData>() {}.getType();

			AddressData addressData = new Gson().fromJson(result, type);

			AddressResults addressResults = addressData.getResults();

			AddressCommonData commonData = addressResults.getCommon();

			List<AddressJusoData> jusoData = addressResults.getJuso();

			Log.d("onResult", "test");

			if("0".equals(commonData.getErrorCode())) {
				int totalCount = Integer.parseInt(commonData.getTotalCount());
				if(totalCount > 300) {
					lvInit.setVisibility(View.VISIBLE);
					lvZipCode.setVisibility(View.GONE);
					showRinnaiDialog(context, context.getString(R.string.msg_title_noti), context.getString(R.string.address_search_max_result));
				} else {

					int count = (CommonValue.ADDRESS_SEARCH_CURRENT_PER_PAGE * (currentPage - 1) )  + jusoData.size();

					if(totalCount > count) {
						lastItemVisibleFlag = true;
					} else {
						lastItemVisibleFlag = false;
					}

					lvInit.setVisibility(View.GONE);
					lvZipCode.setVisibility(View.VISIBLE);
					if (adapter == null && currentPage == 1) {
						adapterList = new ArrayList<AddressJusoData>();
						adapterList.addAll(jusoData);
						adapter = new AddressSearchListAdapter(context, adapterList);
						lvZipCode.setAdapter(adapter);
					} else {
						adapterList.addAll(jusoData);

					}
					adapter.notifyDataSetChanged();
				}
			}
		} else {
			ResponseData responseData = JsonParserManager.jsonToObject(ResponseData.class, result);
			if ("OK".equals(responseData.getResultMessage())) {
				Object obj = responseData.getData();

				String str = JsonParserManager.objectToJson(Object.class, obj);

				if ("getSiteAddressCheck".equals(responseData.getResultType())) {

					type = new TypeToken<Integer>(){}.getType();

					int count = new Gson().fromJson(str, type);

					if(count == 0) {
						listener.onSelected(jusoData);
						dismiss();
					} else {
						jusoData = null;
						showRinnaiDialog(context, context.getString(R.string.msg_title_noti), "이미 등록된 현장입니다. \n다른 현장을 등록해주세요.");
					}
				}
			}
		}


		//else if ("getSiteAddressCheck"){

		//}

	}

	@Override
	public void exceptionOccured(Exception e) {

	}

	@Override
	public void cancelled() {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemVisibleFlag && mLockListView == false) {
			// 화면이 바닦에 닿을때 처리
			// 로딩중을 알리는 프로그레스바를 보인다.
//			progressBar.setVisibility(View.VISIBLE);
//
			// 다음 데이터를 불러온다.
			getAddressItem(keyword);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		//lastItemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
	}

	private void getAddressItem(String keyword){
		if(!networkConnecting) {
			String url = null;

			mLockListView = true;

			//String httpHost = HttpClient.getCurrentSsid(context.this);
			currentPage ++;
			url = String.format("%s?currentPage=%d&countPerPage=%d&keyword=%s&confmKey=%s&resultType=%s",
					CommonValue.HTTP_ADDRESS_SEARCH, currentPage, CommonValue.ADDRESS_SEARCH_CURRENT_PER_PAGE,
					keyword, CommonValue.ADDRESS_SEARCH_KEY, CommonValue.ADDRESS_SEARCH_RESULT_TYPE);

			showProgress(context);

			networkConnecting = true;
			//adapter = null;

			HttpClient.get(url, this);

		} else {


		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		jusoData = (AddressJusoData) adapter.getItem(position);
		if(type == -1) {

			listener.onSelected(jusoData);
			dismiss();
		} else {
			checkAddress(jusoData);
		}
	}

	private void checkAddress(AddressJusoData jusoData) {

		if(!networkConnecting) {
			String url = null;

			mLockListView = true;

			//String httpHost = HttpClient.getCurrentSsid(context.this);
			url = String.format("%s/%s/%s/%s/%s/%s/%s/%s",
					CommonValue.HTTP_HOST, CommonValue.HTTP_AOS, CommonValue.HTTP_SITE_MANAGEMENT,  CommonValue.HTTP_INFO, CommonValue.HTTP_CHECK,
					jusoData.getAdmCd(), jusoData.getRnMgtSn(), jusoData.getBdMgtSn()
			);

			showProgress(context);

			networkConnecting = true;
			//adapter = null;

			HttpClient.get(url, this);

		}
	}
}

