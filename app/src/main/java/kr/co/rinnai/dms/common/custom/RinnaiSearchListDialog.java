package kr.co.rinnai.dms.common.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.RinnaiDialog;
import kr.co.rinnai.dms.adapter.SearchListAdapter;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.common.listener.DialogListener;

public class RinnaiSearchListDialog extends Dialog implements AdapterView.OnItemClickListener, OnClickListener, CompoundButton.OnCheckedChangeListener {

	private DialogListener dialogListener;
	private SearchListAdapter adapter;
	private ListView listview;
	private Object obj;
	private String type;
	private Context context;

	private RelativeLayout confirm;
	private RelativeLayout cancel;

	private LinearLayout lldetailTitle;
	private LinearLayout llProductGas;

	private TextView title, selectItem;

	private int viewVisible = 8;

	private CheckBox cbEtc, cbLpg, cbLng;

	private LinearLayout llEtc, llLpg, llLng;

	private String gasType = "2";

	private RinnaiDialog rinnaiDialog;


	public RinnaiSearchListDialog(Context context, String productBarcode) {
		super(context);


	}

	public RinnaiSearchListDialog(Context context, Object objectList, String type) {
		super(context);
		this.context = context;
		this.obj = objectList;
		this.type = type;

	}

	public RinnaiSearchListDialog(Context context, Object objectList, String type, int gasVisible) {
		super(context);
		this.context = context;
		this.obj = objectList;
		this.type = type;
		this.viewVisible = gasVisible;



	}

	public void setDialogListener(DialogListener dialogListener){
		this.dialogListener = dialogListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_search_list);

		confirm = (RelativeLayout)findViewById(R.id.btn_search_confirm);
		cancel = (RelativeLayout)findViewById(R.id.btn_search_cancel);

		listview = (ListView) findViewById(R.id.lv_search_list);

		title = (TextView) findViewById(R.id.tv_dialog_search_type);
		selectItem = (TextView) findViewById(R.id.tv_dialog_search_select_item);

		lldetailTitle = (LinearLayout) findViewById(R.id.ll_dialog_search_detail);
		lldetailTitle.setVisibility(View.GONE);

		llProductGas = (LinearLayout) findViewById(R.id.ll_product_gas_type);
		llProductGas.setVisibility(View.GONE);

		adapter = new SearchListAdapter(context, obj, type);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);
		String strTitle = "매장 검색";
		if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type)) {
			strTitle = "제품 검색";
		} else if (CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type)) {
			strTitle = "매장 검색";
		} else if(CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
			strTitle = "제품 검색";
		} else if(CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
			strTitle = "제품 검색";
		}
		title.setText(strTitle);

		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);

		cbEtc = (CheckBox) findViewById(R.id.cb_wms_activity_05_gas_type_etc);
		cbLpg = (CheckBox) findViewById(R.id.cb_wms_activity_05_gas_type_lpg);
		cbLng = (CheckBox) findViewById(R.id.cb_wms_activity_05_gas_type_lng);

		llEtc = (LinearLayout) findViewById(R.id.wms_activity_05_gas_type_etc);
		llLpg = (LinearLayout) findViewById(R.id.wms_activity_05_gas_type_lpg);
		llLng = (LinearLayout) findViewById(R.id.wms_activity_05_gas_type_lng);

		llEtc.setOnClickListener(this);
		llLpg.setOnClickListener(this);
		llLng.setOnClickListener(this);

		cbEtc.setOnCheckedChangeListener(this);
		cbLpg.setOnCheckedChangeListener(this);
		cbLng.setOnCheckedChangeListener(this);



		adapter.notifyDataSetChanged();

	}

	@Override
	public void onBackPressed() {
		dismiss();
	}


	@Override
	public void onClick(View v) {

		int id = v.getId();
		if(id == confirm.getId()) {
			String code = adapter.getSelectCode();
			if(null == code) {

			} else {
				if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type)) {
					dialogListener.onPositiveClicked(type, code);
				} else if (CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type)) {
					dialogListener.onPositiveClicked(type, code);
				} else if(CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
					String name = adapter.getSelectName();
					dialogListener.onPositiveClicked(name, code);
				} else if(CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
					String name = adapter.getSelectName();
					String modelCode = code;
					if(viewVisible == 0) {
						if(!cbEtc.isChecked() && !cbLng.isChecked() && !cbLpg.isChecked()) {
							llProductGas.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.red));
							return;
						} else {
							modelCode = String.format("%s%s000000001", modelCode.substring(0, 5), gasType);
						}
					}
					dialogListener.onPositiveClicked(name, modelCode);
				} else if(CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
					String name = adapter.getAgencyName();
					dialogListener.onPositiveClicked(name, code);
				}

				dismiss();
			}
		} else if( id == cancel.getId()) {
			dismiss();
		} else if(id == llEtc.getId()) {
			if(!cbEtc.isChecked()) {
				cbEtc.setChecked(true);
			}
		} else if(id == llLpg.getId()) {
			if(!cbLpg.isChecked()) {
				cbLpg.setChecked(true);
			}
		} else if(id == llLng.getId()) {
			if(!cbLng.isChecked()) {
				cbLng.setChecked(true);
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		lldetailTitle.setVisibility(View.VISIBLE);
		adapter.setSelectItem(position, selectItem);
		adapter.notifyDataSetChanged();

		if( viewVisible == 0) {
			llProductGas.setVisibility(View.VISIBLE);
		} else if (viewVisible == 4) {
			llProductGas.setVisibility(View.INVISIBLE);
		} else if (viewVisible == 8) {
			llProductGas.setVisibility(View.GONE);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


		if(cbEtc == compoundButton) {

			if(b) {
				llProductGas.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				cbLpg.setChecked(false);
				cbLng.setChecked(false);
				gasType = "0";

			}
		} else if ( cbLpg == compoundButton) {
			if(b) {
				llProductGas.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				cbEtc.setChecked(false);
				cbLng.setChecked(false);
				gasType = "1";

			}
		} else if ( cbLng  == compoundButton) {
			if(b) {
				llProductGas.setBackground(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				cbEtc.setChecked(false);
				cbLpg.setChecked(false);
				gasType = "2";

			}
		}




	}
}
