package kr.co.rinnai.dms.wms.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseDialog;
import kr.co.rinnai.dms.activity.RinnaiDialog;
import kr.co.rinnai.dms.adapter.CategorySpinnerAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu06DetailEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ServiceEntity;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.callback.IAsyncCallback;
import kr.co.rinnai.dms.common.custom.CustomButtonView;
import kr.co.rinnai.dms.common.custom.RinnaiAddressSearchDialog;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.db.MySQLiteOpenHelper;
import kr.co.rinnai.dms.common.http.HttpClient;
import kr.co.rinnai.dms.common.http.model.AddressJusoData;
import kr.co.rinnai.dms.common.http.model.Categorizaion;
import kr.co.rinnai.dms.common.http.model.CategorizationResultVO;
import kr.co.rinnai.dms.common.http.model.ResponseData;
import kr.co.rinnai.dms.common.listener.AddressSelectedListener;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.listener.ServiceDialogListener;
import kr.co.rinnai.dms.common.util.JsonParserManager;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.wms.adapter.WmsMenuReadingInfoAdapter;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeReading;

public class WmsMenuReadingInfoDialog extends BaseDialog implements OnClickListener{
	private Context context;
	private List<AgencyBarcodeReading> list;
	private ListView lvReadingInfo;
	private RelativeLayout btnClose;
	private TextView tvModelName;
	private WmsMenuReadingInfoAdapter adapter;
	private String modelName;

	public WmsMenuReadingInfoDialog(Context context, List<AgencyBarcodeReading> list, String modelName) {
		super(context);
		this.context = context;
		this.list = list;
		this.modelName = modelName;


	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       // getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.activity_wms_menu_reading_info);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);
		lvReadingInfo = (ListView)findViewById(R.id.lv_wms_menu_reading_info);
		btnClose = (RelativeLayout) findViewById(R.id.btn_wms_menu_reading_info_close);
		tvModelName = (TextView) findViewById(R.id.tv_reading_info_model_name);

		btnClose.setOnClickListener(WmsMenuReadingInfoDialog.this);

		adapter = new WmsMenuReadingInfoAdapter(context, list);
		lvReadingInfo.setAdapter(adapter);
		tvModelName.setText(modelName);

	}

	@Override
	public void onBackPressed() {
		dismiss();
	}


	@Override
	public void onClick(View v) {

		int id = v.getId();

		if(id == btnClose.getId()) {
			dismiss();
		}

	}



}
