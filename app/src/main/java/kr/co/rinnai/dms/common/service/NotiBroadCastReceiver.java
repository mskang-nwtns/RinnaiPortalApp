package kr.co.rinnai.dms.common.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.util.Util;

public class NotiBroadCastReceiver extends BroadcastReceiver {

	@Override
    public void onReceive(Context context, Intent intent) {
          
        String name = intent.getAction();    
        
        if(name.equals(CommonValue.BR_RINNAI_ERROR_NOTI)) {
            if (false == isAppPlaying(context)) {
            	Util.openApp(context, context.getPackageName());
            }
        }
        else if(name.equals(CommonValue.BR_RINNAI_ERROR_DLG)) {      	
        	
     		String errorDate = intent.getStringExtra(CommonValue.INTENT_ERROR_DATE);
    		String errorDevice = intent.getStringExtra(CommonValue.INTENT_ERROR_DEVICE);
    		String errorTitle = intent.getStringExtra(CommonValue.INTENT_ERROR_TITLE);
        	
//        	 if (true == Util.isFourgroundApp(context)) {
//            	Intent intentDialog = new Intent(context, TranslucentActivity.class);
//            	intentDialog.putExtra(CommonValue.INTENT_ERROR_DATE, errorDate);
//            	intentDialog.putExtra(CommonValue.INTENT_ERROR_DEVICE, errorDevice);
//            	intentDialog.putExtra(CommonValue.INTENT_ERROR_TITLE, errorTitle);
//            	intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            	context.startActivity(intentDialog);
//             }
//        	 else {
//        		PreferenceUtil.putSharedPreference(context, PreferenceUtil.ERROR_TITLE, errorTitle);
//        		PreferenceUtil.putSharedPreference(context, PreferenceUtil.ERROR_DEVICE, errorDevice);
//        		PreferenceUtil.putSharedPreference(context, PreferenceUtil.ERROR_DATE, errorDate);
////        		Intent appintent = new Intent();
////        		appintent.putExtra(CommonValue.INTENT_ERROR_DATE, errorDate);
////        		appintent.putExtra(CommonValue.INTENT_ERROR_DEVICE, errorDevice);
////        		appintent.putExtra(CommonValue.INTENT_ERROR_TITLE, errorTitle);
////        		RinnaiApp.getInstance().setStaticIntent(appintent);
//        	 }
        }
    }
	
	private boolean isAppPlaying(Context context) {
		
		boolean isPlaying = false;
		
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks =am.getRunningTasks(30);
        if(!tasks.isEmpty()) {
        	
            int tasksSize = tasks.size();
            
            for(int i = 0; i < tasksSize;  i++) {
            	RunningTaskInfo taskinfo = tasks.get(i);
            	if(taskinfo.topActivity.getPackageName().equals(context.getPackageName())) {
            		am.moveTaskToFront(taskinfo.id, 0);
            		isPlaying = true;
            	}
            }
        }
        
        return isPlaying;
	}
	
}
