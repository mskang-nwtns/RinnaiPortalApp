package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu03ListEntity;

public class AgencyMenu03ListAdapter extends BaseAdapter {

//	private Context context;
	private List<AgencyMenu03ListEntity> unShippedList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvTranNo;
		public TextView tvDrvName;
		public TextView tvDrvTelNo;

		public TextView tvWareHouseName;
		public TextView tvCustName;
		public TextView tvTime;



	}

	public AgencyMenu03ListAdapter() {

	}


	public AgencyMenu03ListAdapter(Context context, List<AgencyMenu03ListEntity> unShippedList) {

//		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.unShippedList = unShippedList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(unShippedList != null) {
			count = unShippedList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return unShippedList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public List<AgencyMenu03ListEntity> getList() {
		
		return unShippedList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {





		View v = convertView;
		AgencyMenu03ListEntity result = unShippedList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_agency_menu_03, parent, false);

			viewHolder.tvTranNo =  v.findViewById(R.id.tv_agency_03_activity_list_header_tran_no);
			viewHolder.tvDrvName = v.findViewById(R.id.tv_agency_03_activity_list_header_drv_name);
			viewHolder.tvDrvTelNo = v.findViewById(R.id.tv_agency_03_activity_list_header_drv_tel_no);

			viewHolder.tvWareHouseName = v.findViewById(R.id.tv_agency_03_activity_list_header_warehouse_name);
			viewHolder.tvCustName = v.findViewById(R.id.tv_agency_03_activity_list_header_cust_name);
			viewHolder.tvTime = v.findViewById(R.id.tv_agency_03_activity_list_header_time);

			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvTranNo.setText(result.getTrsNo());
		viewHolder.tvDrvName.setText(result.getDrvName());
		viewHolder.tvDrvTelNo.setText(result.getDrvTelNo());

		viewHolder.tvWareHouseName.setText(result.getWarehouseName());
		viewHolder.tvCustName.setText(result.getCustName());
		String destTime = result.getDestTime();
		if(destTime != null) {
			viewHolder.tvTime.setText(result.getDestTime());
		} else {
			viewHolder.tvTime.setText("-");
		}


		return v;
	}
	

}
