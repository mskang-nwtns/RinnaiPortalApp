package kr.co.rinnai.dms.activity.sensor;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseFragmentActivity;
import kr.co.rinnai.dms.activity.LogoActivity;
import kr.co.rinnai.dms.adapter.SensorAdapter;
import kr.co.rinnai.dms.common.CommonValue;

import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.SensorData;
import kr.co.rinnai.dms.common.http.model.SensorDataList;
import kr.co.rinnai.dms.common.listener.PageListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;


public class StatusActivity extends BaseFragmentActivity
		implements PageListener, OnClickListener {

	private boolean checkStatus = true;
	private Handler connectionHandler = null;
	private final long reconnectionTime = 30 * 1 * 1000;

	private ViewPager sensorViewPager = null;
	private SensorAdapter sensorAdapter = null;

	private ProgressBar progressBar1 = null;
	private Runnable reconnectRunnable = new Runnable() {

		@Override
		public void run() {
			// TODO : reconnectRunnable
			// Log.d("sangmin", "reconnectRunnable - checkStatus: " +
			// checkStatus );
			if (checkStatus) {
				progressBar1.setVisibility(View.VISIBLE);
				checkStatus = false;
				HttpClient.get(CommonValue.HTTP_SENSOR, StatusActivity.this, null);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.act_view_pager);

		sensorViewPager = null;
		sensorAdapter = null;
		sensorViewPager = (ViewPager) findViewById(R.id.sensorViewPager);


		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		progressBar1.setVisibility(View.GONE);

		HttpClient.get(CommonValue.HTTP_SENSOR, StatusActivity.this, null);

		connectionHandler = new Handler();

		connectionHandler.post(reconnectRunnable);
	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	public void onResult(String result) {
		//super.onResult(result);
		if (!checkStatus) {
			checkStatus = true;
			connectionHandler.postDelayed(reconnectRunnable, reconnectionTime);
		}
		progressBar1.setVisibility(View.GONE);

		Log.d("StatusActivity", "result :" + result);
		SensorDataList responseData = JsonParserManager.jsonToObject(SensorDataList.class, result);
		ArrayList<SensorData> list = responseData.getData();

		if (sensorAdapter == null) {
			sensorAdapter = new SensorAdapter(this, getSupportFragmentManager(), list, this);

			sensorViewPager.setAdapter(sensorAdapter);
			sensorViewPager.addOnPageChangeListener(sensorAdapter);
			sensorViewPager.setCurrentItem(0);
		}

		sensorAdapter.setGasComsumptionList(list);
		sensorAdapter.notifyDataSetChanged();
	}

	/*
        @Override
        public Object onTaskBackGround(int type) {
            Object response = userController.requestSenserData();

            return response;
        }

        @Override
        public void onTaskPostExcute(Object result) {

            if (!checkStatus) {
                checkStatus = true;
                connectionHandler.postDelayed(reconnectRunnable, reconnectionTime);
            }
            progressBar1.setVisibility(View.GONE);
            SensorDataList data = (SensorDataList) result;
            ArrayList<SensorData> list = data.getData();
            if (sensorAdapter == null) {
                sensorAdapter = new SensorAdapter(this, getSupportFragmentManager(), list, this);

                sensorViewPager.setAdapter(sensorAdapter);
                sensorViewPager.addOnPageChangeListener(sensorAdapter);
                sensorViewPager.setCurrentItem(0);
            }

            sensorAdapter.setGasComsumptionList(list);
            sensorAdapter.notifyDataSetChanged();

        }
    */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		final Intent finishIntent = new Intent(StatusActivity.this, LogoActivity.class);
		finishIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finishIntent.putExtra(CommonValue.CLEAR_FLAG, true);

		startActivity(finishIntent);
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

	@Override
	protected void onPause() {
		super.onPause();
		Log.d("sangmin", "onPause - onPause: " );
		checkStatus = false;
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!checkStatus) {
			checkStatus = true;
			connectionHandler.postDelayed(reconnectRunnable, reconnectionTime);
		}
	}


	
}
