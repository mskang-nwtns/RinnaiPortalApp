package kr.co.rinnai.dms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.eos.model.StockByWarehouseResult;

public class StockWarehouseListAdapter extends BaseAdapter {

//	private Context context;
	private List<StockByWarehouseResult> stockList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	private Context context;

	class ViewHolder {

		public TextView tvWarehouseName;
		public TextView tvLpgCount;
		public TextView tvLngCount;
		public TextView tvEtcCount;
		public TextView tvTotalCount;
	}

	public StockWarehouseListAdapter() {

	}


	public StockWarehouseListAdapter(Context context, List<StockByWarehouseResult> orderReportList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.stockList = orderReportList;

	}

	@Override
	public int getCount() {

		return stockList.size();
	}
	@Override
	public Object getItem(int position) {

		return stockList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public List<StockByWarehouseResult> getList() {
		
		return stockList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		StockByWarehouseResult result = stockList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_aos_menu_05, parent, false);

			viewHolder.tvWarehouseName = (TextView) v.findViewById(R.id.tv_search_type_value);
			viewHolder.tvLpgCount = (TextView) v.findViewById(R.id.tv_trs_qty_value);
			viewHolder.tvLngCount = (TextView) v.findViewById(R.id.tv_in_qty_value);
			viewHolder.tvEtcCount = (TextView) v.findViewById(R.id.tv_out_qty_value);
			viewHolder.tvTotalCount = (TextView) v.findViewById(R.id.tv_stock_qty_value);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}



		if(result.getSortNum() > 2 && result.getSortNum() < 9) {
			viewHolder.tvLngCount.setTextColor(context.getResources().getColor(R.color.red, null));
			viewHolder.tvLpgCount.setTextColor(context.getResources().getColor(R.color.red, null));
			viewHolder.tvEtcCount.setTextColor(context.getResources().getColor(R.color.red, null));
			viewHolder.tvTotalCount.setTextColor(context.getResources().getColor(R.color.red, null));
		} else if (result.getSortNum() == 9) {
			viewHolder.tvLngCount.setTextColor(context.getResources().getColor(R.color.employee_menu_03_store_stock_value, null));
			viewHolder.tvLpgCount.setTextColor(context.getResources().getColor(R.color.employee_menu_03_store_stock_value, null));
			viewHolder.tvEtcCount.setTextColor(context.getResources().getColor(R.color.employee_menu_03_store_stock_value, null));
			viewHolder.tvTotalCount.setTextColor(context.getResources().getColor(R.color.employee_menu_03_store_stock_value, null));
		} else {
			viewHolder.tvLngCount.setTextColor(context.getResources().getColor(R.color.text_view_listview_row_value_two, null));
			viewHolder.tvLpgCount.setTextColor(context.getResources().getColor(R.color.text_view_listview_row_value_one, null));
			viewHolder.tvEtcCount.setTextColor(context.getResources().getColor(R.color.text_view_listview_row_value_two, null));
			viewHolder.tvTotalCount.setTextColor(context.getResources().getColor(R.color.text_view_listview_row_value_one, null));


		}


		viewHolder.tvWarehouseName.setText(result.getWarehouseName());

		viewHolder.tvLpgCount.setText(String.format("%,d", result.getLpg()));
		viewHolder.tvLngCount.setText(String.format("%,d", result.getLng()));
		viewHolder.tvEtcCount.setText(String.format("%,d", result.getEtc()));
		viewHolder.tvTotalCount.setText(String.format("%,d", result.getTotal()));

		if(result.getWarehouse() == null && result.getWarehouseItem() == null) {
			viewHolder.tvLngCount.setTextColor(context.getResources().getColor(R.color.red, null));
			viewHolder.tvLpgCount.setTextColor(context.getResources().getColor(R.color.red, null));
			viewHolder.tvEtcCount.setTextColor(context.getResources().getColor(R.color.red, null));
			viewHolder.tvTotalCount.setTextColor(context.getResources().getColor(R.color.red, null));
		}

		return v;
	}

}
