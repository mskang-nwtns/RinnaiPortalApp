package kr.co.rinnai.dms.common.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.listener.CalendarListener;

public class RinnaiMonthCalendarDialog extends Dialog implements  View.OnClickListener{


	private RelativeLayout btnConfirm, btnCancel;

	private TextView tvYear;

	private RelativeLayout rlBtnPrev, rlBtnNext;
	private RelativeLayout rlMonthJan, rlMonthFeb, rlMonthMar, rlMonthApr, rlMonthMay, rlMonthJun;
	private RelativeLayout rlMonthJul, rlMonthAug, rlMonthSep, rlMonthOct, rlMonthNov, rlMonthDec;


	private CalendarListener listener;
	private int type = 0;

	private GridLayout glCalendar;
	private Context context;

	private int selectMonth = -1;
	private String date;

	public RinnaiMonthCalendarDialog(Context context) {

		super(context);

	}
	public RinnaiMonthCalendarDialog(Context context, int type, String date) {
		super(context);
		this.context = context;
		this.type = type;
		this.date = date;
	}
	public void setDialogListener(CalendarListener listener){
		this.listener = listener;
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_month_calendar);

		tvYear = (TextView) findViewById(R.id.tv_dig_month_calendar_year);

		rlBtnPrev = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_prev);

		rlBtnNext = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_next);


		rlMonthJan = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_jan);
		rlMonthFeb = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_feb);
		rlMonthMar = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_mar);
		rlMonthApr = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_apr);
		rlMonthMay = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_may);
		rlMonthJun = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_jun);

		rlMonthJul = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_jul);
		rlMonthAug = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_aug);
		rlMonthSep = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_sep);
		rlMonthOct = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_oct);
		rlMonthNov = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_nov);
		rlMonthDec = (RelativeLayout) findViewById(R.id.rl_dig_month_calendar_dec);


		btnConfirm = findViewById(R.id.btn_calendar_confirm);
		btnCancel = findViewById(R.id.btn_calendar_cancel);


		btnConfirm.setOnClickListener(RinnaiMonthCalendarDialog.this);
		btnCancel.setOnClickListener(RinnaiMonthCalendarDialog.this);


		rlBtnPrev.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlBtnNext.setOnClickListener(RinnaiMonthCalendarDialog.this);

		rlMonthJan.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthFeb.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthMar.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthApr.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthMay.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthJun.setOnClickListener(RinnaiMonthCalendarDialog.this);

		rlMonthJul.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthAug.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthSep.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthOct.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthNov.setOnClickListener(RinnaiMonthCalendarDialog.this);
		rlMonthDec.setOnClickListener(RinnaiMonthCalendarDialog.this);

		glCalendar = (GridLayout)findViewById(R.id.gl_dig_month_calendar);

		Calendar calendar = Calendar.getInstance();


		String year = String.format("%d년", calendar.get(Calendar.YEAR));


		int month = calendar.get(Calendar.MONTH);

		selectMonth = month + 1;



		if(!"".equals(date)) {
			date = date.replaceAll("[^0-9]","");
			year = date.substring(0, 4);
			month = Integer.parseInt(date.substring(4, 6)) - 1;
		}
		tvYear.setText(year);
		View view = glCalendar.getChildAt(month);
		clearMonthView(view);


		//listener.onCalendarView();


//		calendarView

	}

	@Override
	public void onBackPressed() {
		dismiss();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		String year = tvYear.getText().toString();
		int iYear = Integer.parseInt(year.replaceAll("[^0-9]",""));

		if(id == btnConfirm.getId()) {

			if (type == 1) {
				String date = String.format("%4d년%02d월", iYear, selectMonth);
				listener.onDateChange(date);
			}
			cancel();


		} else if(id == btnCancel.getId()) {
			cancel();
		} else if(id == rlBtnNext.getId()) {
			iYear = iYear + 1;

		} else if(id == rlBtnPrev.getId()) {
			iYear = iYear - 1;
		} else if(id == rlMonthJan.getId() ){
			clearMonthView(v);
			selectMonth = 1;
		} else if(id == rlMonthFeb.getId() ){
			clearMonthView(v);
			selectMonth = 2;
		} else if(id == rlMonthMar.getId()) {
			clearMonthView(v);
			selectMonth = 3;
		} else if(id == rlMonthApr.getId()) {
			clearMonthView(v);
			selectMonth = 4;
		} else if(id == rlMonthMay.getId()){
			clearMonthView(v);
			selectMonth = 5;
		} else if(id == rlMonthJun.getId()){
			clearMonthView(v);
			selectMonth = 6;
		} else if(id == rlMonthJul.getId()){
			clearMonthView(v);
			selectMonth = 7;
		} else if(id == rlMonthAug.getId()){
			clearMonthView(v);
			selectMonth = 8;
		} else if(id == rlMonthSep.getId()){
			clearMonthView(v);
			selectMonth = 9;
		} else if(id == rlMonthOct.getId()){
			clearMonthView(v);
			selectMonth = 10;
		} else if(id == rlMonthNov.getId()){
			clearMonthView(v);
			selectMonth = 11;
		} else if(id == rlMonthDec.getId()){
			clearMonthView(v);
			selectMonth = 12;
		}

		year = String.format("%d년", iYear);
		tvYear.setText(year);

	}

	private void clearMonthView(View v) {
		int count = glCalendar.getChildCount();
		for(int i = 0; i < count;i ++) {
			View view = glCalendar.getChildAt(i);
			view.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.trans_color, null));
		}

		int color = context.getApplicationContext().getResources().getColor(R.color.dig_month_calendar_selected_month);
		v.setBackgroundColor(color);
	}

	public void setDate(String date) {
		Calendar calendar = Calendar.getInstance();


		int year = calendar.get(Calendar.YEAR);




		int month = calendar.get(Calendar.MONTH);

		selectMonth = month + 1;


		if(!"".equals(date)) {
			date = date.replaceAll("[^0-9]","");
			year = Integer.parseInt(date.substring(0, 4));
			month = Integer.parseInt(date.substring(4, 6)) - 1;
		}
		tvYear.setText(String.format("%d년", year));
		View view = glCalendar.getChildAt(month);
		clearMonthView(view);
	}


	private void initView() {

	}
}

