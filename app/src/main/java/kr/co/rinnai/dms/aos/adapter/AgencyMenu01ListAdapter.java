package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu01ListEntity;

public class AgencyMenu01ListAdapter extends BaseAdapter {

//	private Context context;
	private List<AgencyMenu01ListEntity> unShippedList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvItem;
		public TextView tvModelName;
		public TextView tvModelCode;
		public TextView tvModelGasType;
		public TextView tvModelPrice;
		public TextView tvUnshipped;
		public TextView tvShipped;
		public TextView tvTodayShipped;
		public TextView tvOrderNumber;
	}

	public AgencyMenu01ListAdapter() {

	}


	public AgencyMenu01ListAdapter(Context context, List<AgencyMenu01ListEntity> unShippedList) {

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
	
	public List<AgencyMenu01ListEntity> getList() {
		
		return unShippedList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		AgencyMenu01ListEntity result = unShippedList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_aos_menu_01, parent, false);


			viewHolder.tvItem = v.findViewById(R.id.tv_agency_01_activity_list_header_item);
			viewHolder.tvModelName = v.findViewById(R.id.tv_agency_01_activity_list_header_model_name);
			viewHolder.tvModelCode = v.findViewById(R.id.tv_agency_01_activity_list_header_model_code);
			viewHolder.tvModelGasType = v.findViewById(R.id.tv_agency_01_activity_list_header_model_gas_type);
			viewHolder.tvModelPrice = v.findViewById(R.id.tv_agency_01_activity_list_header_model_price);
			viewHolder.tvUnshipped = v.findViewById(R.id.tv_agency_01_activity_list_header_unshipped);
			viewHolder.tvShipped = v.findViewById(R.id.tv_agency_01_activity_list_header_shipped);
			viewHolder.tvTodayShipped = v.findViewById(R.id.tv_agency_01_activity_list_header_today_shipped);
			viewHolder.tvOrderNumber = v.findViewById(R.id.tv_agency_01_activity_list_header_order_number);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}


		viewHolder.tvItem.setText(result.getItem().replace(" ", "").replace("부문", ""));
		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvModelCode.setText(result.getModelNo());
		viewHolder.tvModelGasType.setText(result.getGas());
		viewHolder.tvModelPrice.setText(result.getC2UpriceCode());
		viewHolder.tvUnshipped.setText(String.format("%d", result.getnOutQty()));
		viewHolder.tvShipped.setText(String.format("%d", result.getNowQty()));
		viewHolder.tvTodayShipped.setText(result.getQty());
		viewHolder.tvOrderNumber.setText(result.getSeq());

		return v;
	}
	

}
