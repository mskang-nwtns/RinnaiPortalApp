package kr.co.rinnai.dms.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import kr.co.rinnai.dms.R;

public class RinnaiConfirmDialog extends Dialog implements OnClickListener {

	private Button btnConfirm;
	private Button btnCancel;
	private TextView tvTitle, tvMsg;
	private String title, msg;

	public RinnaiConfirmDialog(Context context, String title, String msg) {
		super(context);
		
		this.title = title;
		this.msg = msg;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.confifm_dialog_rinnai);
		
		btnConfirm = (Button) findViewById(R.id.btn_confirm);
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvMsg = (TextView) findViewById(R.id.tv_msg);
		
		btnConfirm.setOnClickListener(this);
		
		tvTitle.setText(title);
		tvMsg.setText(msg);
		
	}

	@Override
	public void onClick(View v) {
		
		if (btnCancel == v) {
			dismiss();
		} else if (btnConfirm == v) {

		}
	}
}
