package kr.co.rinnai.dms.wms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeResult;
import kr.co.rinnai.dms.wms.model.LocationInfoResult;
import kr.co.rinnai.dms.wms.model.WmsMenu03AgencyOrderReport;

public class WmsMenu03AgencyListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<WmsMenu03AgencyOrderReport> orderReportArrayList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		//대리점명
		public TextView tvAgencyName;
		//작업률
		public TextView tvWorkRate;
		//
		public TextView tvCompleted;
	}

	public WmsMenu03AgencyListAdapter() {

	}


	public WmsMenu03AgencyListAdapter(Context context, ArrayList<WmsMenu03AgencyOrderReport> productInfoList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.orderReportArrayList = productInfoList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(orderReportArrayList != null) {
			count = orderReportArrayList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return orderReportArrayList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public ArrayList<WmsMenu03AgencyOrderReport> getList() {
		
		return orderReportArrayList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		WmsMenu03AgencyOrderReport result = orderReportArrayList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_wms_menu_03_agency_order, parent, false);

			viewHolder.tvAgencyName = (TextView) v.findViewById(R.id.tv_agency_name);
			viewHolder.tvWorkRate = (TextView) v.findViewById(R.id.tv_work_rate);
			viewHolder.tvCompleted = (TextView) v.findViewById(R.id.tv_completed);

			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}


		//Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "position value :" + position + "result.isRead() : " + result.isSelected() );
				
		if(result.getOrderQty() <= result.getJobQty()) {
			viewHolder.tvAgencyName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvWorkRate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvCompleted.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvCompleted.setText("완료");

		} else {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.tvAgencyName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvWorkRate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvCompleted.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvCompleted.setText("미완료");
		}

		viewHolder.tvAgencyName.setText(result.getCustName());
		int workRate = result.getJobQty() / result.getOrderQty();
		viewHolder.tvWorkRate.setText(workRate * 100 + "%");

		if(workRate == 0) {
//			float fRate = (double) result.getJobQty() / (double) result.getOrderQty();
			int tmp = (int)((double) result.getJobQty() / (double) result.getOrderQty() * 100);
			viewHolder.tvWorkRate.setText( tmp + "%");
		}



		return v;
	}




}
