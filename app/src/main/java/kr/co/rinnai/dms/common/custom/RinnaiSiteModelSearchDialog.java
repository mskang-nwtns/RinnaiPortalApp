package kr.co.rinnai.dms.common.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.adapter.RinnaiSiteModelSearchAdapter;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;
import kr.co.rinnai.dms.common.listener.SiteModelModelSearchDialogListener;

public class RinnaiSiteModelSearchDialog extends Dialog implements AdapterView.OnItemClickListener, OnClickListener {

	private SiteModelModelSearchDialogListener dialogListener;
	private RinnaiSiteModelSearchAdapter adapter;
	private ListView listview;
	private List<AgencyMenu07SiteModelInfo> obj;
	private Context context;

	private RelativeLayout confirm;
	private RelativeLayout cancel;

	public RinnaiSiteModelSearchDialog(Context context, List<AgencyMenu07SiteModelInfo> objectList) {
		super(context);
		this.context = context;
		this.obj = objectList;


	}


	public void setDialogListener(SiteModelModelSearchDialogListener dialogListener){
		this.dialogListener = dialogListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_site_model_search);

		confirm = (RelativeLayout)findViewById(R.id.btn_site_model_search_confirm);
		cancel = (RelativeLayout)findViewById(R.id.btn_site_model_search_cancel);

		listview = (ListView) findViewById(R.id.lv_site_model_search);



		adapter = new RinnaiSiteModelSearchAdapter(context, obj);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(this);


		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);

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
			AgencyMenu07SiteModelInfo modelInfo = adapter.getSelectItem();
			if(modelInfo != null) {
				dialogListener.onPositiveClicked(modelInfo);
				dismiss();
			} else {

			}

			//dialogListener.onPositiveClicked(name, modelCode);

		} else if( id == cancel.getId()) {
			dismiss();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.setSelectItem(position);
		adapter.notifyDataSetChanged();
	}
}
