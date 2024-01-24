package kr.co.rinnai.dms.common.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.listener.CalendarListener;

public class RinnaiCalendarDialog extends Dialog implements  View.OnClickListener{


	private RelativeLayout btnConfirm, btnCancel;

	private MaterialCalendarView calendarView;

	private CalendarListener listener;
	private int type = 0;

	public RinnaiCalendarDialog(Context context) {

		super(context);

	}
	public RinnaiCalendarDialog(Context context, int type) {
		super(context);
		this.type = type;
	}
	public void setDialogListener(CalendarListener listener){
		this.listener = listener;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_calendar);

		calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);



		btnConfirm = findViewById(R.id.btn_calendar_confirm);
		btnCancel = findViewById(R.id.btn_calendar_cancel);

		btnConfirm.setOnClickListener(RinnaiCalendarDialog.this);
		btnCancel.setOnClickListener(RinnaiCalendarDialog.this);
		//listener.onCalendarView();

//		calendarView

	}

	@Override
	public void onBackPressed() {
		dismiss();
	}

	@Override
	public void onClick(View v) {

		if(v == btnConfirm) {
			CalendarDay day = calendarView.getSelectedDate();
			if(day == null) {
				cancel();
			} else {

				if(type == 0 ) {
					String date = String.format("%4d/%02d/%02d", day.getYear(), day.getMonth(), day.getDay());
					listener.onDateChange(date);
				} else if (type == 1) {
					String date = String.format("%4d년%02d월", day.getYear(), day.getMonth());
					listener.onDateChange(date);
				}
				cancel();
			}

		} else if(v == btnCancel) {
			cancel();
		}

	}
}

