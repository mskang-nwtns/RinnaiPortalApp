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
import android.widget.TextView;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.listener.DialogListener;

public class RinnaiSiteModelCountDialog extends Dialog implements OnClickListener {

	private DialogListener dialogListener;

	private CustomApplyButtonView btnConfirm;
	private Button btnDel;
	private Button btnNo24, btnNo30, btnNo36;
	private Button btnNo1, btnNo2, btnNo3, btnNo4, btnNo5, btnNo6, btnNo7, btnNo8, btnNo9, btnNo0;

	private LinearLayout llProductCount;
	private LinearLayout llCountBookmark;
	private TextView tvProductCount;
	private String strProductCount;

	private String qty = null;

	private TextView tvProductInputCount;
	private TextView tvModelTitle;
	private String modelName;

	private int position = -1;
	public RinnaiSiteModelCountDialog(Context context, int position, String modelName) {
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
		setContentView(R.layout.dialog_received_product);

		llProductCount = (LinearLayout) findViewById(R.id.ll_product_count);
		tvProductInputCount = (TextView) findViewById(R.id.tv_product_input_count);
		tvProductCount = (TextView) findViewById(R.id.tv_product_count);
		llCountBookmark = (LinearLayout) findViewById(R.id.ll_count_bookmark);
		llCountBookmark.setVisibility(View.GONE);

		btnNo1 = (Button) findViewById(R.id.btn_no_1);
		btnNo2 = (Button) findViewById(R.id.btn_no_2);
		btnNo3 = (Button) findViewById(R.id.btn_no_3);
		btnNo4 = (Button) findViewById(R.id.btn_no_4);
		btnNo5 = (Button) findViewById(R.id.btn_no_5);
		btnNo6 = (Button) findViewById(R.id.btn_no_6);
		btnNo7 = (Button) findViewById(R.id.btn_no_7);
		btnNo8 = (Button) findViewById(R.id.btn_no_8);
		btnNo9 = (Button) findViewById(R.id.btn_no_9);
		btnNo0 = (Button) findViewById(R.id.btn_no_0);
		btnDel = (Button) findViewById(R.id.btn_del);

		btnNo24 = (Button) findViewById(R.id.btn_no_24);
		btnNo30 = (Button) findViewById(R.id.btn_no_30);
		btnNo36 = (Button) findViewById(R.id.btn_no_36);
		btnNo24.setVisibility(View.GONE);
		btnNo30.setVisibility(View.GONE);
		btnNo36.setVisibility(View.GONE);

		btnConfirm = (CustomApplyButtonView) findViewById(R.id.btn_confirm);
		tvModelTitle = (TextView) findViewById(R.id.tv_dia_model_count_title);
		tvModelTitle.setText(String.format("%s %s", modelName, "납품수량"));

		btnNo1.setOnClickListener(this);
		btnNo2.setOnClickListener(this);
		btnNo3.setOnClickListener(this);
		btnNo4.setOnClickListener(this);
		btnNo5.setOnClickListener(this);
		btnNo6.setOnClickListener(this);
		btnNo7.setOnClickListener(this);
		btnNo8.setOnClickListener(this);
		btnNo9.setOnClickListener(this);
		btnNo0.setOnClickListener(this);
		btnDel.setOnClickListener(this);

		//btnNo24.setOnClickListener(this);
		//btnNo30.setOnClickListener(this);
		//btnNo36.setOnClickListener(this);

		btnConfirm.setOnClickListener(this);

		if(qty != null) {
			llProductCount.setVisibility(View.VISIBLE);
			tvProductInputCount.setText(qty.trim());
			tvProductCount.setText(qty.trim());
		} else {
			llProductCount.setVisibility(View.GONE);
		}

		
	}

	@Override
	public void onBackPressed() {
		dismiss();
	}

	@Override
	public void onClick(View v) {
		strProductCount = tvProductInputCount.getText().toString();
		if (btnConfirm == v) {
			if(strProductCount.length() > 0 && qty == null) {
				dialogListener.onPositiveClicked("", strProductCount, position);
				dismiss();
			} else if(strProductCount.length() > 0 && qty != null) {
				dialogListener.onPositiveClicked("", strProductCount, position);
				dismiss();
			} else {
				//showRinnaiDialog(WmsMenu08Activity.this, getString(R.string.msg_title_noti), String.format(" 총 %d건 중 %d건 전송에 성공하였습니다."));
			}
		} else if (btnNo1 == v) {
			strProductCount = strProductCount.concat(btnNo1.getText().toString());
		} else if (btnNo2 == v) {
			strProductCount = strProductCount.concat(btnNo2.getText().toString());
		} else if (btnNo3 == v) {
			strProductCount = strProductCount.concat(btnNo3.getText().toString());
		} else if (btnNo4 == v) {
			strProductCount = strProductCount.concat(btnNo4.getText().toString());
		} else if (btnNo5 == v) {
			strProductCount = strProductCount.concat(btnNo5.getText().toString());
		} else if (btnNo6 == v) {
			strProductCount = strProductCount.concat(btnNo6.getText().toString());
		} else if (btnNo7 == v) {
			strProductCount = strProductCount.concat(btnNo7.getText().toString());
		} else if (btnNo8 == v) {
			strProductCount = strProductCount.concat(btnNo8.getText().toString());
		} else if (btnNo9 == v) {
			strProductCount = strProductCount.concat(btnNo9.getText().toString());
		} else if (btnNo0 == v) {
			if(strProductCount.length() > 0) {
				strProductCount = strProductCount.concat(btnNo0.getText().toString());
			}
		} else if (btnNo24 == v) {
			strProductCount = btnNo24.getText().toString();
		} else if (btnNo30 == v) {
			strProductCount = btnNo30.getText().toString();
		} else if (btnNo36 == v) {
			strProductCount = btnNo36.getText().toString();
		} else if (btnDel == v) {
			if(strProductCount.length() > 0) {
				if(qty != null) {
					if(qty.trim().equals(strProductCount)) {
						strProductCount = "";
					} else {
						strProductCount = strProductCount.substring(0, strProductCount.length() - 1);
					}

				} else {
					strProductCount = strProductCount.substring(0, strProductCount.length() - 1);
				}

			}

		}
		tvProductInputCount.setText(strProductCount);
	}

}
