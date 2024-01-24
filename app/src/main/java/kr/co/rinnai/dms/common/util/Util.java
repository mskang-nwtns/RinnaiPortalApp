package kr.co.rinnai.dms.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build;
//import android.support.v4.content.ContextCompat;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebView;

import androidx.core.content.ContextCompat;

public class Util {

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
            return false;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 안드로이드 버전별로 뷰의 배경을 변경
     * @param context 환경변수
     * @param view 배경을 변경할 뷰
     * @param resID 리소스 아이디
     */
    @SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static void setBackgroundImage(Context context, View view, int resID) {
    	
    	final int sdk = Build.VERSION.SDK_INT;
    	if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
    		view.setBackgroundDrawable(getDrawable(context, resID));
    	} else {
    		view.setBackground(getDrawable(context, resID));
    	}
    }  
    

    /**
     * Drawable리소스를 안드로이드 버전별로 다른 방식으로 얻는다
     * @param context 환경변수
     * @param id drawable 리소스 아이디
     * @return 얻은 Drawable 리소스
     */
    public static final Drawable getDrawable(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }  
    
    /**
     * 난수 생성
     * @param limit 최대 생성숫자
     * @return 생성된 난수
     */
    public static String getRandNumber(int limit) {
    	int number = (int)(Math.random() * limit);
    	return String.format("%04d", number); 	
    }
    

    /**
     * 현재 날짜를 원하는 포맷으로 얻음
     * @param formatValue 원하는 포맷
     * @return 변환된 날짜
     */
    public static String getDateTimeString(String formatValue) {
    	
    	SimpleDateFormat format = new SimpleDateFormat (formatValue);		
    	Date timeNow = new Date();
    	return format.format(timeNow);
    }
    
    /**
     * 현재 최상위에 내앱이 띄워져 있는지 확인
     * @return
     */
    public static boolean isFourgroundApp(Context context) {
    	
    	ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        
        if (context.getPackageName().equals(componentInfo.getPackageName())) {
        	return true;
        }
        else {
        	return false;
        } 	
    }


    /**
     *
     * @param s
     * @return
     */
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    static public int getOrientationOfImage(String filepath) {
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(filepath);

        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }

        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

        if (orientation != -1) {
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
            }
        }

        return 0;
    }


    static public Bitmap getRotatedBitmap(Bitmap bitmap, int degrees) throws Exception {
        if(bitmap == null) return null;
        if (degrees == 0) return bitmap;

        Matrix m = new Matrix();
        m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
    }

    /**
     * 이미지 해상도 resize
     * @param filePath
     * @return
     */
    public static Bitmap filePathToBitMap(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        int orientation = Util.getOrientationOfImage(filePath);
        Bitmap originalBm = BitmapFactory.decodeFile(filePath, options);


        try {
            originalBm = Util.getRotatedBitmap(originalBm, orientation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int width = originalBm.getWidth();
        int height = originalBm.getHeight();


        if(width > height) {
            originalBm = Bitmap.createScaledBitmap(originalBm, 1024, 768, true);
        } else if (height > width) {
            originalBm = Bitmap.createScaledBitmap(originalBm, 768, 1024, true);
        } else {

        }
        //Log.d("DMS", String.format("img width : %d , height : %d, resizeW : %d, resizeH : %d resize W : %d  resize H : %d",width, height , resizeWidth, resizeHeight,   width / resizeWidth, height / resizeHeight  ));



        return originalBm;
    }


    /**
     * 해상도 변경 이미지 file로 저장
     * @param bitmap
     * @param prefix
     * @param suffix
     * @return
     */
    public static String saveBitmaptoJpeg(Bitmap bitmap, String prefix, String suffix){


        File storageDir = new File(Environment.getExternalStorageDirectory() + "/dms/");
        if (!storageDir.exists()) storageDir.mkdirs();

        String fileFull = "";
        fileFull = storageDir.getAbsolutePath();
        // 빈 파일 생성

        prefix = prefix.replace("/", "-");
        File tempFile = new File(storageDir, prefix + suffix);
        try {

            tempFile.createNewFile();  // 파일을 생성해주고

            FileOutputStream out = new FileOutputStream(tempFile);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100 , out);  // 넘거 받은 bitmap을 jpeg(손실압축)으로 저장해줌

            out.close(); // 마무리로 닫아줍니다.

            fileFull = storageDir.getAbsolutePath() + "/" +  prefix + suffix;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return fileFull;
    }

    public static boolean isTabletDevice(Context context) {

        if (Build.VERSION.SDK_INT >= 19){
            return checkTabletDeviceWithScreenSize(context) &&
                    checkTabletDeviceWithProperties() &&
                    checkTabletDeviceWithUserAgent(context);
        }else{
            return checkTabletDeviceWithScreenSize(context) &&
                    checkTabletDeviceWithProperties() ;

        }
    }

    private static boolean checkTabletDeviceWithScreenSize(Context context) {
        boolean device_large = ((context.getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE);

        if (device_large) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) context;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_300
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {
                return true;
            }
        }
        return false;
    }

    private static boolean checkTabletDeviceWithProperties()
    {
        try
        {
            InputStream ism = Runtime.getRuntime().exec("getprop ro.build.characteristics").getInputStream();
            byte[] bts = new byte[1024];
            ism.read(bts);
            ism.close();

            boolean isTablet = new String(bts).toLowerCase().contains("tablet");
            return isTablet;
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            return false;
        }
    }

    private static boolean checkTabletDeviceWithUserAgent(Context context) {
        WebView webView = new WebView(context);
        String ua=webView.getSettings().getUserAgentString();
        webView = null;
        if(ua.contains("Mobile Safari")){
            return false;
        }else{
            return true;
        }
    }


    	
}
