package kr.co.rinnai.dms.eos.activity;

import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.fragment.app.Fragment;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.util.ParseUtil;
import kr.co.rinnai.dms.eos.model.SalesProgress;
import kr.co.rinnai.dms.eos.model.SalesProgressV3;

import static java.lang.Float.isNaN;

public class EmployeeMenu01Fragment extends Fragment {
	private View convertView = null;

	//	private String ="합계"
	//	private String ="18일실적"
	//	private String ="18일계획"

	private TextView tvDeptTypeName;

	private TextView tvSalePlanAmt;
	private TextView tvValueAddedPlanAmt;


	private TextView tvSaleDayPlanAmt;
	private TextView tvValueAddedDayPlanAmt;


	private TextView tvSaleAmt;
	private TextView tvValueAddedAmt;

	private TextView tvMPlanAdditiveRate;
	private TextView tvDPlanAdditiveRate;
	private TextView tvAdditiveRate;

	private TextView tvMPlanRateValueAdded;
	private TextView tvDPlanRateValueAdded;

	private TextView tvMPlanRate;
	private TextView tvDPlanRate;

	private TextView tvTitleDayPlan;
	private TextView tvTitleDay;

	private TextView tvTitleDayAmt;
	private TextView tvDSaleAmt;
	
	private static ArrayList<SalesProgressV3> list = null;
	private SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy/MM/dd");

	public static Fragment newInstance(Context context, int position, ArrayList<SalesProgressV3> progressList) {
		Bundle b = new Bundle();
		b.putInt("pos", position);
		list = progressList;
		return Fragment.instantiate(context, EmployeeMenu01Fragment.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.layout_sales_processor_fragment, container, false);

		//tvDeptTypeName = (TextView) convertView.findViewById(R.id.tv_dept_type_name);

		tvSalePlanAmt = (TextView) convertView.findViewById(R.id.tv_sale_plan_amt);
		tvValueAddedPlanAmt = (TextView) convertView.findViewById(R.id.tv_value_added_plan_amt);
		tvMPlanAdditiveRate = (TextView) convertView.findViewById(R.id.tv_m_plan_ratio_value_added);

		tvSaleDayPlanAmt = (TextView) convertView.findViewById(R.id.tv_sale_day_plan_amt);
		tvValueAddedDayPlanAmt = (TextView) convertView.findViewById(R.id.tv_value_added_day_plan_amt);
		tvDPlanAdditiveRate = (TextView) convertView.findViewById(R.id.tv_d_plan_ratio_value_added);

		tvSaleAmt = (TextView) convertView.findViewById(R.id.tv_sale_amt);
		tvValueAddedAmt = (TextView) convertView.findViewById(R.id.tv_value_added_amt);
		tvAdditiveRate = (TextView) convertView.findViewById(R.id.tv_ratio_value_added);

		tvMPlanRateValueAdded = (TextView) convertView.findViewById(R.id.tv_m_plan_rate_value_added);
		tvDPlanRateValueAdded = (TextView) convertView.findViewById(R.id.tv_d_plan_rate_value_added);

		tvMPlanRate = (TextView) convertView.findViewById(R.id.tv_m_plan_rate);
		tvDPlanRate = (TextView) convertView.findViewById(R.id.tv_d_plan_rate);

		tvTitleDayPlan = (TextView) convertView.findViewById(R.id.tv_title_day_plan);
		tvTitleDay = (TextView) convertView.findViewById(R.id.tv_title_day);

		tvTitleDayAmt = (TextView) convertView.findViewById(R.id.tv_title_day_amt);

		tvDSaleAmt = (TextView) convertView.findViewById(R.id.tv_d_sale_amt);

		int pos = this.getArguments().getInt("pos");
		SalesProgressV3 progress = list.get(pos);
		//tvDeptTypeName.setText(progress.getDeptTypeName());






		float salePlanAmt = ParseUtil.parseFloat(progress.getSalePlanAmt());
		float saleDayPlanAmt = ParseUtil.parseFloat(progress.getSaleDayPlanAmt());
		float saleAmt = ParseUtil.parseFloat(progress.getSaleAmt());

		float valueAddedPlanAmt = ParseUtil.parseFloat(progress.getValueAddedPlanAmt());
		float valueAddedDayPlanAmt = ParseUtil.parseFloat(progress.getValueAddedDayPlanAmt());
		float valueAddedAmt = ParseUtil.parseFloat(progress.getValueAddedAmt());

		float saleDayAmt = ParseUtil.parseFloat(progress.getSaleDayAmt());

		float mPlanAdditiveRate = ParseUtil.parseFloat(progress.getPlanAdditiveRate());
		float dPlanAdditiveRate= ParseUtil.parseFloat(progress.getdPlanAdditiveRate());
		float additiveRate = ParseUtil.parseFloat(progress.getAdditiveRate());

		float mPlanRate = ParseUtil.parseFloat(progress.getmPlanRate());
		float dPlanRate = ParseUtil.parseFloat(progress.getdPlanRate());

		float mPlanRateValueAdded = ParseUtil.parseFloat(progress.getmPlanRateValueAdded());
		float dPlanRateValueAdded = ParseUtil.parseFloat(progress.getdPlanRateValueAdded());


//		tvSalePlanAmt.setText(progress.getSalePlanAmt());
		tvSalePlanAmt.setText(String.format("%,.1f", salePlanAmt ));
//		tvValueAddedPlanAmt.setText(progress.getValueAddedPlanAmt());
		tvValueAddedPlanAmt.setText(String.format("%,.1f", valueAddedPlanAmt));

//		tvSaleDayPlanAmt.setText(progress.getSaleDayPlanAmt());
		tvSaleDayPlanAmt.setText(String.format("%,.1f", saleDayPlanAmt));

//		tvValueAddedDayPlanAmt.setText(progress.getValueAddedDayPlanAmt());
		tvValueAddedDayPlanAmt.setText(String.format("%,.1f", valueAddedDayPlanAmt));

//		tvSaleAmt.setText(progress.getSaleAmt());
		tvSaleAmt.setText(String.format("%,.1f", saleAmt));

//		tvValueAddedAmt.setText(progress.getValueAddedAmt());
		tvValueAddedAmt.setText(String.format("%,.1f", valueAddedAmt));
		
		if(isNaN(mPlanRate)) {
			mPlanRate = (float)0.0;
		}
		if(isNaN(dPlanRate)) {
			dPlanRate = (float)0.0;
		}
		if(isNaN(saleDayAmt)) {
			saleDayAmt = (float)0.0;
		}


		if(isNaN(mPlanRateValueAdded)) {
			mPlanRateValueAdded = (float)0.0;
		}
		if(isNaN(dPlanRateValueAdded)) {
			dPlanRateValueAdded = (float)0.0;
		}


		if(isNaN(mPlanAdditiveRate)) {
			mPlanAdditiveRate = (float)0.0;
		}
		if(isNaN(dPlanAdditiveRate)) {
			dPlanAdditiveRate = (float)0.0;
		}
		if(isNaN(additiveRate)) {
			additiveRate = (float)0.0;
		}


		if (Double.isInfinite(mPlanRate)) {
			mPlanRate = (float)0.0;
		}

		if (Double.isInfinite(dPlanRate)) {
			dPlanRate = (float)0.0;
		}

		if (Double.isInfinite(mPlanRateValueAdded)) {
			mPlanRateValueAdded = (float)0.0;
		}

		if (Double.isInfinite(dPlanRateValueAdded)) {
			dPlanRateValueAdded = (float)0.0;
		}


		String date = progress.getOpDate();

		Date time = new Date();

		String searchDate = format1.format(time);
		String day = "";
		if(date == null) {
			day = searchDate.substring(8, 10);
		} else {
			day = date.substring(8, 10);
		}

		//String month = date.substring(5, 7);

		//String year = date.substring(0, 4);
		//String time = date.substring(11, date.length()-2);

		tvTitleDayPlan.setText(String.format("%s일계획", day));
		tvTitleDay.setText(String.format("%s일실적", day));
		tvTitleDayAmt.setText(String.format("%s일당일실적", day));

		tvMPlanRate.setText(String.format("%.1f", mPlanRate ) + "%");
		tvDPlanRate.setText(String.format("%.1f", dPlanRate ) + "%");



		tvMPlanRateValueAdded.setText(String.format("%.1f", mPlanRateValueAdded ) + "%");
		tvDPlanRateValueAdded.setText(String.format("%.1f", dPlanRateValueAdded ) + "%");


		tvMPlanAdditiveRate.setText(String.format("%.1f", mPlanAdditiveRate ) + "%");
		tvDPlanAdditiveRate.setText(String.format("%.1f", dPlanAdditiveRate ) + "%");
		tvAdditiveRate.setText(String.format("%.1f", additiveRate ) + "%");

		tvDSaleAmt.setText(String.format("%.1f", saleDayAmt));


/*
		tvMPlanAdditiveRate.setText();
		tvDPlanAdditiveRate.setText();

		tvAdditiveRate.setText();
		tvMPlanRateValueAdded.setText();
		tvDPlanRateValueAdded.setText();

		tvMPlanRate.setText();
		tvDPlanRate.setText();
		*/

		return convertView;
	}
}
