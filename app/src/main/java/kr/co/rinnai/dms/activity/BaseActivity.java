package kr.co.rinnai.dms.activity;

import java.io.Serializable;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.callback.IAsyncCallback;
import kr.co.rinnai.dms.common.callback.WebSocketCallBack;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.custom.RinnaiReceivedProductDialog;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.LogUtil;
import kr.co.rinnai.dms.common.util.WeakHandler;

import org.java_websocket.handshake.ServerHandshake;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class BaseActivity extends Activity implements View.OnTouchListener, OnClickListener, IAsyncCallback<String>, WebSocketCallBack {

	public static final String ACTION_SERVICE = "com.rinnai.service.WebSocketService";

	protected static boolean IS_WAIT;
	private ProgressDialog progressDlg;
	protected RelativeLayout btnBack;
	//	protected Button btnHome;
	protected TextView tvTitle;

	private RinnaiDialog rinnaiDialog;
	private RinnaiReceivedProductDialog rinnaiReceivedProductDialog;

    private int totalInstructions = 0;
    private int totalOperations = 0;
    private int totalSuccessCount = 0;
    private int totalRemainingCount = 0;

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
	// 070-7090-2722
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
		showRinnaiDialog(BaseActivity.this, getString(R.string.msg_title_noti), "네트워크 연결이 원할하지 않습니다.");
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
	
//	public void startWebSocketService() {	
//
//        Intent intent = new Intent(getApplicationContext(), WebSocketService.class);
//        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);	
//	}
//
//	public void stopWebSocketService() {		
//		unbindService(serviceConnection);		
//
//    }
	
//	private ServiceConnection serviceConnection = new ServiceConnection() {			
//		
//		@Override
//		public void onServiceConnected(ComponentName name, IBinder service) {
//			LogUtil.d("서비스 연결");
//			WebSocketService.WebSocketServiceBinder binder = (WebSocketService.WebSocketServiceBinder) service;
//			webSocketService = binder.getService(); //서비스 받아옴
//			webSocketService.registerCallback(webSocketCallback);
//			webSocketService.Connect();
//		}
//		
//		@Override
//		public void onServiceDisconnected(ComponentName name) {
//			LogUtil.d("서비스 해제");
//		}
//	};

//	public void registerCallback(WebSocketCallBack callback) {
//		
//		if (null != webSocketService) {
//			webSocketService.registerCallback(webSocketCallback);
//		}		
//	}
//	
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

	public void showRinnaiDialog(final Context context, final String title, final String msg, final String path) {

		rinnaiDialog = new RinnaiDialog(context, title, msg, path);
		rinnaiDialog.setCanceledOnTouchOutside(false);
		rinnaiDialog.setCancelable(false);
		weakHandler.sendEmptyMessage(CommonValue.HANDLER_RINNAI_DLG_SHOW);;
	}




	public void showRinnaiReceivedProductDialog(final Context context, String productBarcode) {
		rinnaiReceivedProductDialog = new RinnaiReceivedProductDialog(context, productBarcode);
		rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
		rinnaiReceivedProductDialog.setCancelable(false);
		weakHandler.sendEmptyMessage(CommonValue.HANDLER_RINNAI_RECEIVED_DLG_SHOW);;
	}
	
	public void dismissRinnaiDialog() {
		weakHandler.sendEmptyMessage(CommonValue.HANDLER_RINNAI_DLG_DISSMISS);
	}
	
	public void showProgress(final Context context) {

		if(progressDlg != null) {
			progressDlg.dismiss();
		}
		progressDlg = new ProgressDialog(context);
		progressDlg.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		progressDlg.setIndeterminate(true);
		progressDlg.setCancelable(true);
		progressDlg.setCanceledOnTouchOutside(false);
		weakHandler.sendEmptyMessage(CommonValue.HANDLER_PROGRESS_SHOW);
	}
	
	public void dismissProgress() {
		
		weakHandler.sendEmptyMessage(CommonValue.HANDLER_PROGRESS_DISSMISS);
	}
	
	final WeakHandler<BaseActivity> weakHandler = new WeakHandler<BaseActivity>(BaseActivity.this) {
		
		@Override
		protected void weakHandleMessage(BaseActivity ref, Message msg) {
        	switch (msg.what) {
			case CommonValue.HANDLER_PROGRESS_SHOW:
				if (null == progressDlg) {
					progressDlg = new ProgressDialog(BaseActivity.this);

				}
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
			case CommonValue.HANDLER_RINNAI_RECEIVED_DLG_SHOW:
				rinnaiReceivedProductDialog.show();
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
		String jsonStr = 
				JsonParserManager.getJsonFromKeyValue("userKey", RinnaiApp.getInstance().getAccessToken());
		LogUtil.d("LoginKey : " + RinnaiApp.getInstance().getAccessToken());
		LogUtil.d("WebSocket Send : " + jsonStr);   
		//RinnaiApp.getInstance().sendWebSocket(jsonStr);
	}
	
	@Override
	public void onError(String error) {
		LogUtil.d("WebSocket Error : " + error);   	
	}
	
	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {}

    public boolean onTouch(View v, MotionEvent e) {

        return false;
    }

    public int getTotalInstructions() {
        return totalInstructions;
    }

    public void setTotalInstructions(int totalInstructions) {
        this.totalInstructions = totalInstructions;
    }

    public int getTotalOperations() {
        return totalOperations;
    }

    public void setTotalOperations(int totalOperations) {
        this.totalOperations = totalOperations;
    }

    public int getTotalSuccessCount() {
        return totalSuccessCount;
    }

    public void setTotalSuccessCount(int totalSuccessCount) {
        this.totalSuccessCount = totalSuccessCount;
    }

    public int getTotalRemainingCount() {
        return totalRemainingCount;
    }

    public void setTotalRemainingCount(int totalRemainingCount) {
        this.totalRemainingCount = totalRemainingCount;
    }
}
