package kr.co.rinnai.dms.common.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
//import android.support.annotation.Nullable;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class DownloadNotifiaiontService extends IntentService {
    public static final String PROGRESS_UPDATE = "dms_progress_update";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;
    private String appVersion;
    public DownloadNotifiaiontService() {
        super("downloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("download", "파일 다운로드", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{ 0 });
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationBuilder = new NotificationCompat.Builder(this, "download")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("다운로드중입니다.")
                .setContentText("다운로드중")
                .setDefaults(0)
                .setVibrate(new long[]{ 0 })
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());


        String fileName = String.format("http://58.72.180.60/mobileservice/deploy/last/SmartDMS%s.apk", this.appVersion);


        Call<ResponseBody> request = ApiService.getInstance().api.downloadFile(ApiService.getInstance().getDownloadUrl());

        try {
            downloadFile(request.execute().body());
        } catch(Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT);
        }

    }

    private void downloadFile(ResponseBody body) throws IOException {
        int count;
        byte data[] = new byte[1024 * 4];
        long fileSize = body.contentLength();
        InputStream inputStream = new BufferedInputStream(body.byteStream(), 1024 * 8);
        File outputFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SmartDMS.apk");
        if(outputFile.exists()) {
            outputFile.delete();
        }

        OutputStream outputStream = new FileOutputStream(outputFile);
        long total = 0;
        boolean downloadComplete = false;

        while((count = inputStream.read(data)) != -1) {
            total += count;
            int progress = (int) ((double) (total * 100) / (double) fileSize);
            updateNotification(progress);
            outputStream.write(data,0, count);
            downloadComplete = true;

        }

        onDownloadComplete(downloadComplete);
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private void updateNotification(int currentProgress) {
        notificationBuilder.setProgress(100, currentProgress, false);
        notificationBuilder.setContentText(currentProgress + "%");
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void sendProgressUpdate(boolean downloadComplete) {
        Intent intent = new Intent(PROGRESS_UPDATE);
        intent.putExtra("downloadComplete", downloadComplete);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void onDownloadComplete(boolean downloadComplete) {
        sendProgressUpdate(downloadComplete);
        String message;
        if(downloadComplete) {
            message = "다운로드가 완료되었습니다.";
        } else {
            message = "다운로드에 실패하였습니다.";
        }
        notificationManager.cancel(0);
        notificationBuilder.setProgress(0,0,false);
        notificationBuilder.setContentText(message);
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        notificationManager.cancel(0);
    }
}
