package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu05WareHouseStockInfo;
import kr.co.rinnai.dms.aos.model.SalespersonMenu01WareHouseStockInfo;

public class SalespersonMenu01ListAdapter extends BaseAdapter {

	private Context context;
	private List<SalespersonMenu01WareHouseStockInfo> orderableList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvOrderPrdType;
		public TextView tvOrderStock;
	}

	public SalespersonMenu01ListAdapter() {

	}


	public SalespersonMenu01ListAdapter(Context context, List<SalespersonMenu01WareHouseStockInfo> productInfoList) {

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
	
	public List<SalespersonMenu01WareHouseStockInfo> getList() {
		
		return orderableList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		SalespersonMenu01WareHouseStockInfo result = orderableList.get(position);
		
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
		viewHolder.tvOrderStock.setText(result.getOrderType());

		viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one, null));
		viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two, null));
		viewHolder.tvOrderPrdType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one, null));
		if("O".equals(result.getOrderType())) {
			viewHolder.tvOrderStock.setTextColor(context.getApplicationContext().getResources().getColor(R.color.employee_menu_03_store_stock_value, null));
		} else if("전화문의".equals(result.getOrderType())) {
			viewHolder.tvOrderStock.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select, null));
		}
		viewHolder.tvOrderStock.setGravity(Gravity.CENTER);
		viewHolder.tvOrderStock.setPadding(0,0,0,0);

		
		return v;
	}


}
