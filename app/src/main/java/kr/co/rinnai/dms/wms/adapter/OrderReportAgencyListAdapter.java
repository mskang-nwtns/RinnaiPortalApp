package kr.co.rinnai.dms.wms.adapter;

import java.util.ArrayList;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.wms.model.OrderReportResult;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderReportAgencyListAdapter extends BaseAdapter {

//	private Context context;
	private ArrayList<OrderReportResult> orderReportList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvLocation;
		public TextView tvAgencyName;
		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvOrderSeq;
	}

	public OrderReportAgencyListAdapter() {
		
	}
	
	
	public OrderReportAgencyListAdapter(Context context, ArrayList<OrderReportResult> orderReportList) {

//		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.orderReportList = orderReportList;
	}

	@Override
	public int getCount() {

		return orderReportList.size();
	}
	@Override
	public Object getItem(int position) {

		return orderReportList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public ArrayList<OrderReportResult> getList() {
		
		return orderReportList;		
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		OrderReportResult result = orderReportList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_get_order_report_agency, parent, false);
			
			viewHolder.tvLocation = (TextView) v.findViewById(R.id.tv_location);
			viewHolder.tvAgencyName = (TextView) v.findViewById(R.id.tv_agency_name);
			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_gas_type);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_order_seq);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "position value :" + position + "result.isRead() : " + result.isRead() );
				
		if(result.isRead()) {
			v.setBackgroundResource(R.color.reserve_day_of_week_default);
		} else if(!result.isRead()) {
			v.setBackgroundColor(Color.WHITE);
		}
		viewHolder.tvLocation.setText(result.getOrderLocation());
		viewHolder.tvAgencyName.setText(result.getCustName());
		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getGasType());
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getOrderSeq()));
		
		return v;
	}
	
	public void setLocationRead(int position, String location) {
		
		//this.orderReportList.get(position).setRead(true);
		//this.orderReportList.remove(position);
		
	}


	public void clickPosition(int position, String location) {

		this.orderReportList.get(position).setRead(!orderReportList.get(position).isRead());

	}

	public void setCompleted(int position) {
		this.orderReportList.get(position).setCompleted(true);
		this.orderReportList.remove(position);

	}

	public void setList(ArrayList<OrderReportResult> orderReportList) {
		this.orderReportList = orderReportList;
	}

}
