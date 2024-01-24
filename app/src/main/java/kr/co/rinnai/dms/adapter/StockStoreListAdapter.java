package kr.co.rinnai.dms.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.eos.model.StockByStoreResult;

public class StockStoreListAdapter extends BaseAdapter {

//	private Context context;
	private ArrayList<StockByStoreResult> stockList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	private String viewType;
	private String gasType = "LNG";

	class ViewHolder {

		public TextView tvSearchName;
		public TextView tvTrsQty;
		public TextView tvInQty;
		public TextView tvOutQty;
		public TextView tvStockQty;
	}

	public StockStoreListAdapter() {

	}


	public StockStoreListAdapter(Context context, ArrayList<StockByStoreResult> orderReportList, String viewType, String gasType) {

//		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.stockList = orderReportList;
		this.viewType = viewType;
		this.gasType = gasType;
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
	
	public ArrayList<StockByStoreResult> getList() {
		
		return stockList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		StockByStoreResult result = stockList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_aos_menu_03, parent, false);

			viewHolder.tvSearchName = (TextView) v.findViewById(R.id.tv_search_type_value);
			viewHolder.tvTrsQty = (TextView) v.findViewById(R.id.tv_trs_qty_value);
			viewHolder.tvInQty = (TextView) v.findViewById(R.id.tv_in_qty_value);
			viewHolder.tvOutQty = (TextView) v.findViewById(R.id.tv_out_qty_value);
			viewHolder.tvStockQty = (TextView) v.findViewById(R.id.tv_stock_qty_value);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}


		if("cust".equals(viewType)) {
			viewHolder.tvSearchName.setText(result.getModelName());


		} else {
			viewHolder.tvSearchName.setText(result.getCustName());

		}

		viewHolder.tvSearchName.setSingleLine(true);
		viewHolder.tvSearchName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
		//viewHolder.tvSearchName. 받아야 문자가 흐르기 때문에
		//포커스를 받을 수 없는 상황에서는 선택된 것으로 처리하면 마키 동작
		viewHolder.tvSearchName.setSelected(true);



		int trsQty = 0;
		int inQty = 0;
		int outQty = 0;
		int stockQty = 0;

		if("LNG".equals(gasType)) {
			trsQty = result.getLnTrsQty();
			inQty = result.getLnInQty();
			outQty = result.getLnOutQty();
			stockQty = result.getLnStockQty();

		} else if ("LPG".equals(gasType)) {
			trsQty = result.getLpTrsQty();
			inQty = result.getLpInQty();
			outQty = result.getLpOutQty();
			stockQty = result.getLpStockQty();
		}



		viewHolder.tvTrsQty.setText(String.format("%,d", trsQty));
		viewHolder.tvInQty.setText(String.format("%,d", inQty));
		viewHolder.tvOutQty.setText(String.format("%,d", outQty));
		viewHolder.tvStockQty.setText(String.format("%,d", stockQty));

		return v;
	}
	
	public void setLocationRead(int position, String location) {
		
		//this.orderReportList.get(position).setRead(true);
		//this.orderReportList.remove(position);
		
	}

	public void changeGasType(String gasType) {
		this.gasType = gasType;
	}



}
