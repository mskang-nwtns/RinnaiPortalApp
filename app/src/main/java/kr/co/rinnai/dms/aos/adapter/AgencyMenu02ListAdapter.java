package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu02ListEntity;

public class AgencyMenu02ListAdapter extends BaseAdapter {

//	private Context context;
	private List<AgencyMenu02ListEntity> orderList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvDate;
		public TextView tvOrderNo;
		public TextView tvOrderCnt;

		public TextView tvOrderReceiptCnt;
		public TextView tvOrderReleaseCnt;
		public TextView tvOrderCancelCnt;



	}

	public AgencyMenu02ListAdapter() {

	}


	public AgencyMenu02ListAdapter(Context context, List<AgencyMenu02ListEntity> orderList) {

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
	
	public List<AgencyMenu02ListEntity> getList() {
		
		return orderList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {





		View v = convertView;
		AgencyMenu02ListEntity result = orderList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_agency_menu_02, parent, false);

			viewHolder.tvDate =  v.findViewById(R.id.tv_agency_02_activity_list_header_date);
			viewHolder.tvOrderNo = v.findViewById(R.id.tv_agency_02_activity_list_header_order_no);
			viewHolder.tvOrderCnt = v.findViewById(R.id.tv_agency_02_activity_list_header_order_cnt);

			viewHolder.tvOrderReceiptCnt = v.findViewById(R.id.tv_agency_02_activity_list_header_order_receipt_cnt);
			viewHolder.tvOrderReleaseCnt = v.findViewById(R.id.tv_agency_02_activity_list_header_order_release_cnt);
			viewHolder.tvOrderCancelCnt = v.findViewById(R.id.tv_agency_02_activity_list_header_order_cancel_cnt);

			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvDate.setText(result.getOrdDate());
		viewHolder.tvOrderNo.setText(result.getOrdNo());
		viewHolder.tvOrderCnt.setText(String.format("%d",result.getOrdCnt()));

		viewHolder.tvOrderReceiptCnt.setText(String.format("%d",result.getOrdReceiptCnt()));
		viewHolder.tvOrderReleaseCnt.setText(String.format("%d",result.getOrdReleaseCnt()));
		viewHolder.tvOrderCancelCnt.setText(String.format("%d",result.getOrdCancelCnt()));


		return v;
	}
	

}
