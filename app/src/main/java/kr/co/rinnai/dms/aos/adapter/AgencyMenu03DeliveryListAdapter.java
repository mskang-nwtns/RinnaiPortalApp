package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu03DeliveryListEntity;

public class AgencyMenu03DeliveryListAdapter extends BaseAdapter {

//	private Context context;
	private List<AgencyMenu03DeliveryListEntity> unShippedList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGas;

		public TextView tvWareHouse;
		public TextView tvQty;
		public TextView tvDate;


	}

	public AgencyMenu03DeliveryListAdapter() {

	}


	public AgencyMenu03DeliveryListAdapter(Context context, List<AgencyMenu03DeliveryListEntity> unShippedList) {

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
	
	public List<AgencyMenu03DeliveryListEntity> getList() {
		
		return unShippedList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {





		View v = convertView;
		AgencyMenu03DeliveryListEntity result = unShippedList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_agency_menu_03_delivery, parent, false);

			viewHolder.tvModelName =  v.findViewById(R.id.tv_agency_03_activity_delivery_list_header_model_name);
			viewHolder.tvGas = v.findViewById(R.id.tv_agency_03_activity_delivery_list_header_gas);
			viewHolder.tvQty = v.findViewById(R.id.tv_agency_03_activity_delivery_list_header_qty);
			viewHolder.tvDate = v.findViewById(R.id.tv_agency_03_activity_delivery_list_header_date);



			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGas.setText(result.getGas());
		viewHolder.tvQty.setText(result.getQty());

		viewHolder.tvDate.setText(result.getSaleDate());

		return v;
	}
	

}
