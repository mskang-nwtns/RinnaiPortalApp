package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu02DetailListEntity;


public class AgencyMenu02DetailListAdapter extends BaseAdapter {

//	private Context context;
	private List<AgencyMenu02DetailListEntity> orderList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvStats;
		public TextView tvItemName;
		public TextView tvGas;

		public TextView tvOrdQty;
		public TextView tvReqArea;
		public TextView tvReqDate;


	}

	public AgencyMenu02DetailListAdapter() {

	}


	public AgencyMenu02DetailListAdapter(Context context, List<AgencyMenu02DetailListEntity> orderList) {

//		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.orderList = orderList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(orderList != null) {
			count = orderList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return orderList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public List<AgencyMenu02DetailListEntity> getList() {
		
		return orderList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		AgencyMenu02DetailListEntity result = orderList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_agency_menu_02_detail, parent, false);


			viewHolder.tvStats =  v.findViewById(R.id.tv_agency_02_activity_detail_list_header_stats);
			viewHolder.tvItemName = v.findViewById(R.id.tv_agency_02_activity_detail_list_header_item_name);
			viewHolder.tvGas = v.findViewById(R.id.tv_agency_02_activity_detail_list_header_gas);

			viewHolder.tvOrdQty = v.findViewById(R.id.tv_agency_02_activity_detail_list_header_order_qty);
			viewHolder.tvReqArea = v.findViewById(R.id.tv_agency_02_activity_detail_list_header_req_area);
			viewHolder.tvReqDate = v.findViewById(R.id.tv_agency_02_activity_detail_list_header_req_date);

			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvStats.setText(result.getOrdStats());
		viewHolder.tvItemName.setText(result.getItemName());
		viewHolder.tvGas.setText(result.getGas());
		viewHolder.tvOrdQty.setText(result.getOrdQty());
		viewHolder.tvReqArea.setText(result.getReqArea());
		viewHolder.tvReqDate.setText(result.getRegDate());
		/*
		viewHolder.tvDate.setText(result.getOrdDate());
		viewHolder.tvOrderNo.setText(result.getOrdNo());
		viewHolder.tvOrderCnt.setText(String.format("%d",result.getOrdCnt()));

		viewHolder.tvOrderReceiptCnt.setText(String.format("%d",result.getOrdReceiptCnt()));
		viewHolder.tvOrderReleaseCnt.setText(String.format("%d",result.getOrdReleaseCnt()));
		viewHolder.tvOrderCancelCnt.setText(String.format("%d",result.getOrdCancelCnt()));
		*/


		return v;
	}

}
