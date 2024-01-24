package kr.co.rinnai.dms.common.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.listener.DialogListener;

public class RinnaiReceivedProductDialog extends Dialog implements OnClickListener, CompoundButton.OnCheckedChangeListener {

	private DialogListener dialogListener;

	private CustomApplyButtonView btnConfirm;
	private Button btnDel;
	private Button btnNo24, btnNo30, btnNo36;
	private Button btnNo1, btnNo2, btnNo3, btnNo4, btnNo5, btnNo6, btnNo7, btnNo8, btnNo9, btnNo0;

	private LinearLayout llProductCount;
	private LinearLayout llCellMake;
	private LinearLayout llCellMakeTitle;
	private TextView tvProductCount;
	private String strProductCount;

	private String productBarcode = null;
	private String qty = null;

	private TextView tvProductInputCount;
	private boolean addCellMake = false;
	private CheckBox ckCellMake;

	private LinearLayout llCellMakeYear;
	private LinearLayout llCellMakeMonth;

	private boolean isYearClick = false;
	private boolean isMonthClick = false;

	private TextView tvCellMakeYear;
	private TextView tvCellMakeMonth;

	private String strCellMakeYear;
	private String strCellMakeMonth;

	private int position = -1;


	public RinnaiReceivedProductDialog(Context context, String productBarcode) {
		super(context);

		this.productBarcode = productBarcode;
		
	}

	public RinnaiReceivedProductDialog(Context context, String productBarcode, boolean cellMake) {
		super(context);

		this.addCellMake = cellMake;
		this.productBarcode = productBarcode;

	}

	public RinnaiReceivedProductDialog(Context context, String productBarcode, String qty, int position) {
		super(context);

		this.productBarcode = productBarcode;
		this.qty = qty;
		this.position = position;

	}

	public void setDialogListener(DialogListener dialogListener){
		this.dialogListener = dialogListener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Log.d("productBarcode", productBarcode);

		String cellMake = productBarcode.substring(6, 10);

		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_received_product);

		llProductCount = (LinearLayout) findViewById(R.id.ll_product_count);
		tvProductInputCount = (TextView) findViewById(R.id.tv_product_input_count);
		tvProductCount = (TextView) findViewById(R.id.tv_product_count);
		llCellMake = (LinearLayout) findViewById(R.id.ll_cell_make);
		llCellMakeTitle = (LinearLayout) findViewById(R.id.ll_cell_make_title);
		ckCellMake = (CheckBox) findViewById(R.id.ck_cell_make);

		llCellMakeYear = (LinearLayout) findViewById(R.id.ll_input_cell_make_year);
		llCellMakeMonth = (LinearLayout) findViewById(R.id.ll_input_cell_make_month);

		tvCellMakeYear = (TextView) findViewById(R.id.tv_input_cell_make_year);
		tvCellMakeMonth = (TextView) findViewById(R.id.tv_input_cell_make_month);

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

		btnConfirm = (CustomApplyButtonView) findViewById(R.id.btn_confirm);

		ckCellMake.setOnCheckedChangeListener(this);

		llCellMakeTitle.setOnClickListener(this);
		llCellMakeYear.setOnClickListener(this);
		llCellMakeMonth.setOnClickListener(this);

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

		btnNo24.setOnClickListener(this);
		btnNo30.setOnClickListener(this);
		btnNo36.setOnClickListener(this);

		btnConfirm.setOnClickListener(this);

		if(addCellMake && "0000".equals(cellMake)) {
			llCellMake.setVisibility(View.VISIBLE);
			SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMdd");

			Calendar time = Calendar.getInstance();

			String formatTime1 = format1.format(time.getTime());
			strCellMakeYear = formatTime1.substring(2, 4);
			strCellMakeMonth = formatTime1.substring(4, 6);
		} else {
			strCellMakeYear = cellMake.substring(0, 2);
			strCellMakeMonth = cellMake.substring(2, 4);
		}

		tvCellMakeYear.setText(strCellMakeYear);
		tvCellMakeMonth.setText(strCellMakeMonth);

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
		Button buttonView = null;
		if( v instanceof Button ) {
			buttonView = (Button) v;
			//Do your stuff
		}

		if (btnConfirm == v) {

			if(strProductCount.length() > 0 ) {
				String cellMake = productBarcode.substring(6, 10);
				if(addCellMake && "0000".equals(cellMake)) {
					productBarcode = String.format("%s%s%s%s", productBarcode.substring(0, 6), strCellMakeYear, strCellMakeMonth, productBarcode.substring(10, productBarcode.length()));
				}

				if(qty == null) {
					dialogListener.onPositiveClicked(productBarcode, strProductCount);
					dismiss();
				} else if(qty != null) {
					dialogListener.onPositiveClicked(productBarcode, strProductCount, position);
					dismiss();
				}
			} else {
				//showRinnaiDialog(WmsMenu08Activity.this, getString(R.string.msg_title_noti), String.format(" 총 %d건 중 %d건 전송에 성공하였습니다."));
			}
		} else if (btnNo1 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear, buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth, buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo2 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo3 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo4 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo5 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo6 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo7 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo8 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo9 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				strProductCount = strProductCount.concat(buttonView.getText().toString());
			}
		} else if (btnNo0 == v) {
			if(ckCellMake.isChecked()) {
				if(isYearClick) {
					strCellMakeYear = setCellMakeValue(strCellMakeYear,buttonView.getText().toString());
				} else if(isMonthClick) {
					strCellMakeMonth = setCellMakeValue(strCellMakeMonth,buttonView.getText().toString());
				}
			} else {
				if(strProductCount.length() > 0) {
					strProductCount = strProductCount.concat(buttonView.getText().toString());
				}
			}

		} else if (btnNo24 == v) {
			if(!ckCellMake.isChecked()) {
				strProductCount = btnNo24.getText().toString();
			}
		} else if (btnNo30 == v) {
			if(!ckCellMake.isChecked()) {
				strProductCount = btnNo30.getText().toString();
			}
		} else if (btnNo36 == v) {
			if(!ckCellMake.isChecked()) {
				strProductCount = btnNo36.getText().toString();
			}
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

		} else if(llCellMakeTitle == v) {
			ckCellMake.setChecked(!ckCellMake.isChecked());
			isYearClick = true;
			isMonthClick = !isYearClick;


		} else if(llCellMakeYear == v) {
			//ckCellMake.setChecked(!ckCellMake.isChecked());
			isYearClick = true;
			isMonthClick = !isYearClick;


		} else if(llCellMakeMonth == v) {
			//ckCellMake.setChecked(!ckCellMake.isChecked());
			isMonthClick = true;
			isYearClick = !isMonthClick;

		}
		tvProductInputCount.setText(strProductCount);
		tvCellMakeYear.setText(strCellMakeYear);
		tvCellMakeMonth.setText(strCellMakeMonth);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		isYearClick = true;

	}

	private String setCellMakeValue(String value, String value2) {
		if(value != null) {
			if (value.length() > 1) {
				value = value.substring(value.length() - 1, value.length());
			}

			value = value.concat(value2);
		}

		return value;

	}

}
