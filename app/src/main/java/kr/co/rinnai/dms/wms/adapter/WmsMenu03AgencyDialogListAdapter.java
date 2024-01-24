package kr.co.rinnai.dms.wms.adapter;

import android.content.Context;
import android.graphics.Color;
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
import kr.co.rinnai.dms.wms.model.OrderReportResult;
import kr.co.rinnai.dms.wms.model.WmsMenu03AgencyOrderReport;

public class WmsMenu03AgencyDialogListAdapter extends BaseAdapter {

	private Context context;
	private List<WmsMenu03AgencyOrderReport> orderReportList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {


		public TextView tvOrderLocation;
		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvOrderSeq;
		public TextView tvMake;
	}

	public WmsMenu03AgencyDialogListAdapter() {

	}


	public WmsMenu03AgencyDialogListAdapter(Context context, List<WmsMenu03AgencyOrderReport> orderReportList) {

		this.context = context;
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
	
	public List<WmsMenu03AgencyOrderReport> getList() {
		
		return orderReportList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		WmsMenu03AgencyOrderReport result = orderReportList.get(position);

		if (v == null) {
			viewHolder = new WmsMenu03AgencyDialogListAdapter.ViewHolder();
			v = inflater.inflate(R.layout.list_item_get_order_report_location, parent, false);
			viewHolder.tvOrderLocation = (TextView) v.findViewById(R.id.tv_wms_activity_03_location);
			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_wms_activity_03_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_wms_activity_03_gas_type);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_wms_activity_03_order_seq);
			viewHolder.tvMake = (TextView) v.findViewById(R.id.tv_wms_activity_03_make);

			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "position value :" + position + "result.isRead() : " + result.isRead() );


		if(result.isRead()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.tvOrderLocation.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvMake.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isRead()) {

			viewHolder.tvOrderLocation.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvMake.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));

		}


		viewHolder.tvOrderLocation.setText(result.getOrderLocation());
		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getGasType());
		String cellMake = "-";
		if(null != result.getCellMake()) {
			cellMake = String.format("%s.%s", result.getCellMake().substring(0, 2), result.getCellMake().substring(2, 4));
		}
		viewHolder.tvMake.setText(cellMake);
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getCount()));

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

	public void setList( List<WmsMenu03AgencyOrderReport> orderReportList) {

		this.orderReportList = orderReportList;

	}


}
