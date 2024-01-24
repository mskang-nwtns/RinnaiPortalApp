package kr.co.rinnai.dms.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.custom.RinnaiSearchListDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.DialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu06Activity;
import kr.co.rinnai.dms.eos.adapter.EmployeeMenu06ListAdapter;
import kr.co.rinnai.dms.eos.adapter.ItemSpinnerAdapter;
import kr.co.rinnai.dms.eos.model.EmployeeMenu06ListEntity;
import kr.co.rinnai.dms.eos.model.ItemCode;
import kr.co.rinnai.dms.eos.model.SalesAgencyInfoResult;

/**
 * 출고 현황
 *
 */
public class WebViewTest extends BaseActivity {


	private WebView mWebView;




	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_agency_webview);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.loadUrl("http://merp.rinnai.co.kr/MEPS/index.html");
		mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClientClass());
		mWebView.getSettings().setSupportZoom(true);


	}

	@Override
	public void onResult(String result) {

		super.onResult(result);
		Log.w("onResult", result);

		dismissProgress();
	}


	private class WebViewClientClass extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d("check URL",url);
			view.loadUrl(url);
			return true;
		}
	}
}
