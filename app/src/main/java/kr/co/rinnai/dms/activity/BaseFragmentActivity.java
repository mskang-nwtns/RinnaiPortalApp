package kr.co.rinnai.dms.activity;

import java.io.Serializable;


import org.java_websocket.handshake.ServerHandshake;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
//import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.callback.IAsyncCallback;
import kr.co.rinnai.dms.common.callback.WebSocketCallBack;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.util.LogUtil;
import kr.co.rinnai.dms.common.util.WeakHandler;


public class BaseFragmentActivity extends FragmentActivity implements OnClickListener, IAsyncCallback<String>, WebSocketCallBack {

	public static final String ACTION_SERVICE = "com.rinnai.service.WebSocketService";

	protected static boolean IS_WAIT;
	private ProgressDialog progressDlg;
	protected RelativeLayout btnBack;
	//	protected Button btnHome;
	protected TextView tvTitle;
	private RinnaiDialog rinnaiDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	protected void baseStartActivity(Context packageContext, Class<?> cls) {	
		Intent intent = new Intent(packageContext, cls);
		startActivity(intent);		
	}
	
	protected void titleStartActivity(Context packageContext, Class<?> cls, String title) {	
		Intent intent = new Intent(packageContext, cls);
		intent.putExtra(CommonValue.INTENT_TITLE, title);
		startActivity(intent);		
	}
	
	protected void ObjectStartActivity(Context packageContext, Class<?> cls, String intentKey, Object obj) {	
		Intent intent = new Intent(packageContext, cls);
		intent.putExtra(intentKey, (Serializable)obj);
		startActivity(intent);		
	}
	
	protected void setTitle(String title) {
		
		if (TextUtils.isEmpty(title)) {
			return;
		}	 
		
		btnBack = (RelativeLayout) findViewById(R.id.btn_back);
//		btnHome = (Button) findViewById(R.id.btn_home);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		
		btnBack.setOnClickListener(this);
//		btnHome.setOnClickListener(this);
		
		tvTitle.setText(title);
	}
	
	@Override
	public void onClick(View v) {

		if (btnBack == v) {
			finish();
		}
		
//		else if (btnHome == v) {
//			Intent intent = new Intent(this, MainActivity.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|
//	                Intent.FLAG_ACTIVITY_CLEAR_TASK | 
//	                Intent.FLAG_ACTIVITY_NEW_TASK);
//			startActivity(intent);
//		}
	}

	@Override
	public void onResult(String result) {
		if (!TextUtils.isEmpty(result)) {
			LogUtil.d(result);
		}
	}

	@Override
	public void exceptionOccured(Exception e) {
		LogUtil.e(e.toString());
		dismissProgress();
	}

	@Override
	public void cancelled() {
		dismissProgress();
	}
	
	@SuppressLint("NewApi")
	public void setStatusBackgroundColor(int colorId) {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {		
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(getResources().getColor(colorId));
		}
	}
	
	@SuppressLint("NewApi")
	public void setSystemBarTextColor(boolean pIsDark) {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {		
			
		    final int lFlags = getWindow().getDecorView().getSystemUiVisibility();
		    getWindow().getDecorView().setSystemUiVisibility(pIsDark ? (lFlags & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) : (lFlags | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
		}
	}
	
//	protected void startWebSocketService() {	
//
//        Intent intent = new Intent(getApplicationContext(), WebSocketService.class);
//        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);	
//	}
//	
//
//	protected void stopWebSocketService() {
//		
//		unbindService(serviceConnection);		
//    }

	
	@Override
	protected void onDestroy() {
		
		LogUtil.d("destroy");
		dismissProgress();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void showRinnaiDialog(final Context context, final String title, final String msg) {
		
		rinnaiDialog = new RinnaiDialog(context, title, msg);
	  	rinnaiDialog.setCanceledOnTouchOutside(false);
	  	rinnaiDialog.setCancelable(false);
	  	weakHandler.sendEmptyMessage(CommonValue.HANDLER_RINNAI_DLG_SHOW);;
	}
	
	
	public void dismissRinnaiDialog() {
		weakHandler.sendEmptyMessage(CommonValue.HANDLER_RINNAI_DLG_DISSMISS);
	}
	
	public void showProgress(final Context context) {

		progressDlg = new ProgressDialog(context);
		progressDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		progressDlg.setIndeterminate(true);
		progressDlg.setCancelable(true);
		progressDlg.setCanceledOnTouchOutside(false);
		weakHandler.sendEmptyMessage(CommonValue.HANDLER_PROGRESS_SHOW);
	}
	
	public void dismissProgress() {
		if (progressDlg != null) {
			weakHandler.sendEmptyMessage(CommonValue.HANDLER_PROGRESS_DISSMISS);
		}
	}
	
	final WeakHandler<BaseFragmentActivity> weakHandler = new WeakHandler<BaseFragmentActivity>(BaseFragmentActivity.this) {
		
		@Override
		protected void weakHandleMessage(BaseFragmentActivity ref, Message msg) {
        	switch (msg.what) {
			case CommonValue.HANDLER_PROGRESS_SHOW:
				progressDlg.show();
				progressDlg.setContentView(R.layout.dialog_progress);
            	
				break;					
				
			case CommonValue.HANDLER_PROGRESS_DISSMISS:
				
				if (null != progressDlg) {
					progressDlg.dismiss();
					progressDlg = null;
				}

				break;
				
			case CommonValue.HANDLER_RINNAI_DLG_SHOW:
			  	rinnaiDialog.show();
				break;
				
			case CommonValue.HANDLER_RINNAI_DLG_DISSMISS:
				
				if (null != rinnaiDialog) {
					rinnaiDialog.dismiss();
					rinnaiDialog = null;
				}	

				break;

			default:
				
				break;
			}
		}
	};

	@Override
	public void onReceive(String msg) {
				/*
		if (RinnaiApp.getInstance().setData(msg)) {
			dismissProgress();
			return;
		}
		*/
	}
	
	@Override
	public void onOpen(ServerHandshake arg) {
		/*
		String jsonStr = 
				JsonParserManager.getJsonFromKeyValue("userKey", RinnaiApp.getInstance().getLoginKey());
		LogUtil.d("LoginKey : " + RinnaiApp.getInstance().getLoginKey());
		LogUtil.d("WebSocket Send : " + jsonStr);   
		RinnaiApp.getInstance().sendWebSocket(jsonStr);
		*/
	}
	
	@Override
	public void onError(String error) {
		LogUtil.d("WebSocket Error : " + error);   	
	}
	
	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {}
}
