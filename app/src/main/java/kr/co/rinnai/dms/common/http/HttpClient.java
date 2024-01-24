package kr.co.rinnai.dms.common.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;


import kr.co.rinnai.dms.common.callback.IAsyncCallback;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.RinnaiApp;
import kr.co.rinnai.dms.common.task.AsyncExecutor;

public class HttpClient {

	public static final String CONNECTION_CONFIRM_CLIENT_URL = "http://clients3.google.com/generate_204";

	public static AsyncExecutor<String> asynTask;

	public static Callable<String> asyncCallable;

	public static void get(final String Url, IAsyncCallback<String> asyncCallBack, final Object... param) {

		asyncCallable = new Callable<String>() {
			@Override
			public String call() throws Exception {

				HttpRequest request = HttpRequest.get(Url, true, param);
				request.connectTimeout(10000);

//				String loginKey = RinnaiApp.getInstance().getLoginKey();
//				if (!TextUtils.isEmpty(loginKey)) {
//					request.header(CommonValue.HEADER_LOGIN_KEY, loginKey);
//				}
//
				return request.body();
			}
		};

		asynTask = new AsyncExecutor<String>();
		asynTask.setCallable(asyncCallable).setCallback(asyncCallBack).execute();
	}

	/**
	 * REST post 방식으로 http 통신을 한다.
	 *
	 * @param Url
	 *            전송할 url
	 * @param asyncCallBack
	 *            결과물 받을 callback 메소드
	 * @param jsonStr
	 *            JSON 문자열
	 */
	public static void post(final String Url, IAsyncCallback<String> asyncCallBack, final String jsonStr) {

		asyncCallable = new Callable<String>() {
			@Override
			public String call() throws Exception {

				HttpRequest request = HttpRequest.post(Url);
				request.connectTimeout(10000);
				request.contentType(HttpRequest.CONTENT_TYPE_FORM);

				String accessToken = RinnaiApp.getInstance().getAccessToken();
				if (!TextUtils.isEmpty(accessToken)) {
					request.header(CommonValue.HEADER_LOGIN_KEY, accessToken);
				}

				byte[] byteJson = jsonStr.getBytes(StandardCharsets.UTF_8);
				request.send(byteJson).code();
				return request.body();
			}
		};

		asynTask = new AsyncExecutor<String>();
		asynTask.setCallable(asyncCallable).setCallback(asyncCallBack).execute();
	}

	/**
	 * REST post 방식으로 http 통신을 한다.
	 *
	 * @param Url
	 *            전송할 url
	 * @param asyncCallBack
	 *            결과물 받을 callback 메소드
	 * @param entity
	 *            JSON 문자열
	 */
	public static void postFile(final String Url, IAsyncCallback<String> asyncCallBack, final HttpEntity entity) {

		asyncCallable = new Callable<String>() {
			@Override
			public String call() throws Exception {

				org.apache.http.client.HttpClient client = new DefaultHttpClient();
				HttpPost post = new HttpPost(Url);

				post.setEntity(entity);
				HttpResponse response = client.execute(post);
				HttpEntity httpEntity = response.getEntity();
				String result = EntityUtils.toString(httpEntity);
				Log.v("result", result);

				return result;
			}
		};

		asynTask = new AsyncExecutor<String>();
		asynTask.setCallable(asyncCallable).setCallback(asyncCallBack).execute();
	}

	/**
	 * REST put 방식으로 http 통신을 한다.
	 *
	 * @param Url
	 *            전송할 url
	 * @param asyncCallBack
	 *            결과물 받을 callback 메소드
	 * @param jsonStr
	 *            JSON 문자열
	 */
	public static void put(final String Url, IAsyncCallback<String> asyncCallBack, final String jsonStr) {

		asyncCallable = new Callable<String>() {
			@Override
			public String call() throws Exception {

				HttpRequest request = HttpRequest.put(Url);
				request.connectTimeout(10000);
				request.contentType(HttpRequest.CONTENT_TYPE_FORM);

//				String loginKey = RinnaiApp.getInstance().getLoginKey();
//				if (!TextUtils.isEmpty(loginKey)) {
//					request.header(CommonValue.HEADER_LOGIN_KEY, loginKey);
//				}

				byte[] byteJson = jsonStr.getBytes(StandardCharsets.UTF_8);
				request.send(byteJson).code();
				return request.body();
			}
		};

		asynTask = new AsyncExecutor<String>();
		asynTask.setCallable(asyncCallable).setCallback(asyncCallBack).execute();
	}

	/**
	 * REST delete 방식으로 http 통신을 한다.
	 *
	 * @param Url
	 *            전송할 url
	 * @param asyncCallBack
	 *            결과물 받을 callback 메소드
	 * @param jsonStr
	 *            JSON 문자열
	 */
	public static void delete(final String Url, IAsyncCallback<String> asyncCallBack, final String jsonStr) {

		asyncCallable = new Callable<String>() {
			@Override
			public String call() throws Exception {

				HttpRequest request = HttpRequest.delete(Url);
				request.connectTimeout(10000);
				request.contentType(HttpRequest.CONTENT_TYPE_FORM);

//				String loginKey = RinnaiApp.getInstance().getLoginKey();
//				if (!TextUtils.isEmpty(loginKey)) {
//					request.header(CommonValue.HEADER_LOGIN_KEY, loginKey);
//				}
				byte[] byteJson = null;
				byteJson = jsonStr.getBytes(StandardCharsets.UTF_8);


				request.send(byteJson).code();

				return request.body();
			}
		};

		asynTask = new AsyncExecutor<String>();
		asynTask.setCallable(asyncCallable).setCallback(asyncCallBack).execute();
	}

	/**
	 * 실행중인 asyncTask를 취소(종료)한다.
	 */
	public static void cancelTask() {

		if (null != asynTask) {
			asynTask.cancel(true);
		}
	}

	public static String getCurrentSsid(Context context) {
		String ssid = null;
		/*

		boolean isLocal = false;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null) {
			return null;
		}

		if (networkInfo.isConnected()) {
			final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();

			if (connectionInfo != null) {
				ssid = connectionInfo.getSSID();
				String extraInfo = networkInfo.getExtraInfo().replace("\"","");
				if(!"MacBook_Pro".equals(extraInfo)) {
					ssid = CommonValue.HTTP_HOST;
				} else {
					ssid = CommonValue.HTTP_LOCAL_HOST;
				}
			}
		}
*/
		ssid = CommonValue.HTTP_HOST;
		return ssid;
	}

	public static boolean isOnline() {
		CheckConnect cc = new CheckConnect(CONNECTION_CONFIRM_CLIENT_URL);
		cc.start();
		try{
			cc.join();
			return cc.isSuccess();
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	
}
