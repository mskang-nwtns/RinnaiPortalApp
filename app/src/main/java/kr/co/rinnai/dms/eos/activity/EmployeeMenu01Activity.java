package kr.co.rinnai.dms.eos.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseFragmentActivity;
import kr.co.rinnai.dms.adapter.SalesProgressAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.RinnaiAddressSearchDialog;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.util.Util;
import kr.co.rinnai.dms.eos.model.SaleProgressObject;
import kr.co.rinnai.dms.eos.model.SalesProgress;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.PageListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.eos.model.SalesProgressV3;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 영업본부 직원</br>
 * 매출진도 확인
 */
public class EmployeeMenu01Activity extends BaseFragmentActivity implements PageListener {

	private ViewPager topViewPager = null;
	private ViewPager bottomViewPager = null;

	private SalesProgressAdapter topAdapter = null;
	private SalesProgressAdapter bottomAdapter = null;

	private boolean networkConnecting = false;

	private ArrayList<SalesProgressV3> list = null;

	private TextView tvTopTitle01, tvTopTitle02, tvTopTitle03, tvTopTitle04, tvTopTitle05;
	private TextView tvBottomTitle01, tvBottomTitle02, tvBottomTitle03, tvBottomTitle04, tvBottomTitle05;
	private TextView tvWorkingDate;

	private ArrayList<TextView> topTvList = new ArrayList<TextView>();
	private ArrayList<TextView> bottomTvList = new ArrayList<TextView>();

	private String searchDate;

	private TextView tvDate;
	private RelativeLayout rlDate;

	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_employee_menu_01);


		boolean isTablet = Util.isTabletDevice(EmployeeMenu01Activity.this);

		if(!isTablet) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
		}

		topViewPager = (ViewPager)findViewById(R.id.wms_top_view_pager);
		bottomViewPager = (ViewPager) findViewById(R.id.wms_bottom_view_pager);


		tvTopTitle01 = (TextView) findViewById(R.id.tv_top_title_01);
		tvTopTitle02 = (TextView) findViewById(R.id.tv_top_title_02);
		tvTopTitle03 = (TextView) findViewById(R.id.tv_top_title_03);
		tvTopTitle04 = (TextView) findViewById(R.id.tv_top_title_04);
		tvTopTitle05 = (TextView) findViewById(R.id.tv_top_title_05);

		topTvList.add(tvTopTitle01);
		topTvList.add(tvTopTitle02);
		topTvList.add(tvTopTitle03);
		topTvList.add(tvTopTitle04);
		topTvList.add(tvTopTitle05);

		tvBottomTitle01 = (TextView) findViewById(R.id.tv_bottom_title_01);
		tvBottomTitle02 = (TextView) findViewById(R.id.tv_bottom_title_02);
		tvBottomTitle03 = (TextView) findViewById(R.id.tv_bottom_title_03);
		tvBottomTitle04 = (TextView) findViewById(R.id.tv_bottom_title_04);
		tvBottomTitle05 = (TextView) findViewById(R.id.tv_bottom_title_05);

		bottomTvList.add(tvBottomTitle01);
		bottomTvList.add(tvBottomTitle02);
		bottomTvList.add(tvBottomTitle03);
		bottomTvList.add(tvBottomTitle04);
		bottomTvList.add(tvBottomTitle05);

		tvTopTitle01.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_select, null));
		tvBottomTitle01.setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_select, null));

		rlDate = (RelativeLayout) findViewById(R.id.rl_employee_01_date);
		tvDate = (TextView) findViewById(R.id.tv_employee_01_sale_date);

		Date time = new Date();

		searchDate = format1.format(time);

		tvDate.setText(searchDate);
		tvWorkingDate = (TextView) findViewById(R.id.tv_employee_01_sale_working_date);

//		rlDate.setOnClickListener(EmployeeMenu01Activity.this);
		getSalesProgress();

//		topViewPager.addOnPageChangeListener(EmployeeMenu01Activity.this);

		String gwId = RinnaiApp.getInstance().getGwId();
		String tmpId =  null;
		if(null != gwId) tmpId = gwId.replace("@rinnai.co.kr", "");
		if("mini3248".equals(tmpId) || "ssj9567".equals(tmpId)) {

		} else {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
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

				if ("getSalesProgress".equals(responseData.getResultType())) {

					type = new TypeToken<SaleProgressObject>(){}.getType();
					SaleProgressObject spObject  = new Gson().fromJson(str, type);
					list = spObject.getSaleProgressList();
					String workingDate = String.format("%d / %d", spObject.getWorkingDay(), spObject.getMonthWorkingDay() );

					tvWorkingDate.setText(workingDate);

					topAdapter = new SalesProgressAdapter(this, getSupportFragmentManager(), spObject.getSaleBusinessProgressList(), this, "top");

					topViewPager.setAdapter(topAdapter);
					topViewPager.addOnPageChangeListener(topAdapter);
					topViewPager.setCurrentItem(0);
					topAdapter.notifyDataSetChanged();

					setTitleSelect(0, "top");

					bottomAdapter = new SalesProgressAdapter(this, getSupportFragmentManager(), list, this, "bottom");

					bottomViewPager.setAdapter(bottomAdapter);
					bottomViewPager.addOnPageChangeListener(bottomAdapter);
					bottomViewPager.setCurrentItem(0);
					bottomAdapter.notifyDataSetChanged();

					setTitleSelect(0, "bottom");



				}
			} else {
				showRinnaiDialog(EmployeeMenu01Activity.this, getString(R.string.msg_title_noti),responseData.getResultMessage());
			}
		}

		//getSalesProgress
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

		setTitleSelect(position, type);
	}

	private void setTitleSelect(int position, String type) {

		if("top".equals(type)) {
			String[] title = topAdapter.getTitle(position);
			for(int i = 0; i < topTvList.size(); i ++) {
				topTvList.get(i).setText(title[i]);
				if(2 == i) {
					topTvList.get(i).setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_select));
				} else {
					topTvList.get(i).setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_normal));
				}

			}
		} else if ("bottom".equals(type)) {
//			bottomViewPager.get

//
//			if(topViewPager.getCurrentItem() == position) {
//				bottomViewPager.setCurrentItem(position + 1);
//
//			} else {

			for (int i = 0; i < bottomTvList.size(); i++) {
				String[] title = bottomAdapter.getTitle(position);
				bottomTvList.get(i).setText(title[i]);
				if (2 == i) {
					bottomTvList.get(i).setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_select));
				} else {
					bottomTvList.get(i).setTextColor(getResources().getColor(R.color.employee_menu_01_title_tv_normal));
				}

			}
//			}
		}
	}
	@Override
	public void onClick(View v) {
		//super.onClick(v);
		int id = v.getId();

		if (id == rlDate.getId()) {
			if(!networkConnecting) {
				RinnaiCalendarDialog rinnaiReceivedProductDialog;
				rinnaiReceivedProductDialog = new RinnaiCalendarDialog(EmployeeMenu01Activity.this);
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);
				rinnaiReceivedProductDialog.show();
				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {
						searchDate = date;
						tvDate.setText(searchDate);

						getSalesProgress();

					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});
			}
		}
	}

	private void getSalesProgress() {
		if(!networkConnecting) {

			String url = null;

			String httpHost = HttpClient.getCurrentSsid(EmployeeMenu01Activity.this);

			url = String.format("%s/%s/%s/%s/%s", httpHost, CommonValue.HTTP_SOS, CommonValue.HTTP_SALES, CommonValue.HTTP_VERSION_5, searchDate.replace("/", "-"));

			showProgress(EmployeeMenu01Activity.this);

			networkConnecting = true;

			HttpClient.get(url, this);
		}
	}
}
