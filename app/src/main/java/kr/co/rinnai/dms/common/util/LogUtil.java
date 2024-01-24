package kr.co.rinnai.dms.common.util;


import android.util.Log;

import kr.co.rinnai.dms.common.CommonSetting;

public class LogUtil {

	static final String TAG = "SmartDMS";

	/**
	 * Log.e 형의 로그를 생성한다.
	 * @param msg 생성될 로그 메시지
	 * @param e 
	 */
	public static final void e(String msg) {

		if (CommonSetting.LOG)Log.e(TAG, buildLogMsg(msg));
	}

	/**	
	 * Log.w 형의 로그를 생성한다.
	 * @param msg 생성될 로그 메시지
	 */
	public static final void w(String msg) {

		if (CommonSetting.LOG)Log.w(TAG, buildLogMsg(msg));
	}

	/**
	 * Log.i 형의 로그를 생성한다.
	 * @param msg 생성될 로그 메시지
	 */
	public static final void i(String msg) {

		if (CommonSetting.LOG)Log.i(TAG, buildLogMsg(msg));
	}

	/**
	 * Log.d 형의 로그를 생성한다.
	 * @param msg 생성될 로그 메시지
	 */
	public static final void d(String msg) {

		if (CommonSetting.LOG)Log.d(TAG, buildLogMsg(msg));
	}

	/**
	 * Log.v 형의 로그를 생성한다.
	 * @param msg 생성될 로그 메시지
	 */
	public static final void v(String msg) {

		if (CommonSetting.LOG)Log.v(TAG, buildLogMsg(msg));
	}

	/**
	 * 로그를 생성한다. (생성위치, 로그내용)
	 * @param msg 로그로 생성될 문자열
	 * @return
	 */
	private static String buildLogMsg(String msg) {

		StackTraceElement ste = Thread.currentThread().getStackTrace()[4];
		StringBuilder sb = new StringBuilder();

		sb.append("[");
		sb.append(ste.getFileName().replace(".java", ""));
		sb.append("::");
		sb.append(ste.getMethodName());
		sb.append("]");
		sb.append(msg);

		return sb.toString();
	}
}
