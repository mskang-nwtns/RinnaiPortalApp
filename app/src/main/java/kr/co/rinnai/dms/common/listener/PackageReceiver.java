package kr.co.rinnai.dms.common.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


import kr.co.rinnai.dms.activity.user.LoginActivity;

public class PackageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        String packageName = intent.getData().getSchemeSpecificPart();
        String action = intent.getAction();

        if (intent.getDataString().contains("kr.co.rinnai.portalapp")){
            if(action.equals(Intent.ACTION_PACKAGE_ADDED))
            {
                Log.e("test song", "Package ADDED : " + packageName);
            }
            else if(action.equals(Intent.ACTION_PACKAGE_REMOVED))
            {
                Log.e("test song", "Package REMOVED : " + packageName);
            }
            else if(action.equals(Intent.ACTION_PACKAGE_FULLY_REMOVED))

            {
                final Intent agreementIntent = new Intent(context , LoginActivity.class);
                context.startActivity(agreementIntent);

            }
            else if(action.equals(Intent.ACTION_DELETE))

            {
                Log.e("test song", "Package ACTION_DELETE : " + packageName);
            }
        }


    }

}
