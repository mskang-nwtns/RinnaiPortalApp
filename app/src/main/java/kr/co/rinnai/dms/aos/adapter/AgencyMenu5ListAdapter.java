package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu05WareHouseStockInfo;
import kr.co.rinnai.dms.eos.model.StockByOrderableResult;

public class AgencyMenu5ListAdapter extends BaseAdapter {

	private Context context;
	private List<AgencyMenu05WareHouseStockInfo> orderableList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvOrderPrdType;
		public TextView tvOrderStock;
	}

	public AgencyMenu5ListAdapter() {

	}


	public AgencyMenu5ListAdapter(Context context, List<AgencyMenu05WareHouseStockInfo> productInfoList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.orderableList = productInfoList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(orderableList != null) {
			count = orderableList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return orderableList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public List<AgencyMenu05WareHouseStockInfo> getList() {
		
		return orderableList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		AgencyMenu05WareHouseStockInfo result = orderableList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_orderable_info, parent, false);

			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_gas_type);
			viewHolder.tvOrderPrdType = (TextView) v.findViewById(R.id.tv_order_prd_type);
			viewHolder.tvOrderStock = (TextView) v.findViewById(R.id.tv_order_stock);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvModelName.setText(result.getModelName().trim());
		viewHolder.tvGasType.setText(result.getGas());
//		viewHolder.tvOrderPrdType.setText(result.getOrderPrdType());
		viewHolder.tvOrderStock.setText(String.format("%,d", result.getTotal()));

		if(result.isSelected()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderPrdType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderStock.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isSelected()) {
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvOrderPrdType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvOrderStock.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));

		}

		
		return v;
	}

	public void setSelectItem(int position) {
		for(int i = 0; i <this.orderableList.size(); i ++) {
			if(position == i) {
				this.orderableList.get(i).setSelected(!this.orderableList.get(i).isSelected());
			} else {
				this.orderableList.get(i).setSelected(false);
			}

		}

	}

}
