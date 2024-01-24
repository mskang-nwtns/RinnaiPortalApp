package kr.co.rinnai.dms.common.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.activity.BaseDialog;
import kr.co.rinnai.dms.common.listener.DialogListener;

public class RinnaiLoginType extends BaseDialog implements OnClickListener {

	private DialogListener dialogListener;

	private RelativeLayout btnWms, btnDriver, btnEmployee, btnAgency, btnRetailer;
	private String modelName;

	private int position = -1;
	public RinnaiLoginType(Context context, int position, String modelName) {
		super(context);
		this.position = position;
		this.modelName = modelName;


	}

	public void setDialogListener(DialogListener dialogListener){
		this.dialogListener = dialogListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_login_type);

		btnWms = (RelativeLayout) findViewById(R.id.btn_login_type_wms);
		btnDriver = (RelativeLayout) findViewById(R.id.btn_login_type_wms_driver);
		btnEmployee = (RelativeLayout) findViewById(R.id.btn_login_type_dms_employee);
		btnAgency = (RelativeLayout) findViewById(R.id.btn_login_type_dms_agency);
		btnRetailer = (RelativeLayout) findViewById(R.id.btn_login_type_wms_retailer);


		btnWms.setOnClickListener(this);
		btnDriver.setOnClickListener(this);
		btnEmployee.setOnClickListener(this);
		btnAgency.setOnClickListener(this);
		btnRetailer.setOnClickListener(this);

		
	}

	@Override
	public void onBackPressed() {
		dismiss();
	}

	@Override
	public void onClick(View v) {

		if (btnWms == v) {

			dialogListener.onPositiveClicked("", "1");
			dismiss();

		} else if(btnDriver == v) {

			dialogListener.onPositiveClicked("", "2");
			dismiss();

		} else if(btnEmployee == v) {

			dialogListener.onPositiveClicked("", "3");
			dismiss();

		} else if(btnAgency == v) {

			dialogListener.onPositiveClicked("", "4");
			dismiss();

		} else if(btnRetailer == v) {

			dialogListener.onPositiveClicked("", "5");
			dismiss();

		}
	}

}
