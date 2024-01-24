package kr.co.rinnai.dms.wms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.wms.model.ProductInfoResult;

public class WmsMenu05ListAdapter extends BaseAdapter {

//	private Context context;
	private ArrayList<ProductInfoResult> list;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvLocationName;
		public TextView tvCellNo;
		public TextView tvCellMake;
		public TextView tvOrderSeq;
	}

	public WmsMenu05ListAdapter() {

	}


	public WmsMenu05ListAdapter(Context context, ArrayList<ProductInfoResult> list) {

//		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(list != null) {
			count = list.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return list.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public ArrayList<ProductInfoResult> getList() {
		
		return list;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		ProductInfoResult result = list.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_product_location_search, parent, false);
			
			viewHolder.tvLocationName = (TextView) v.findViewById(R.id.tv_location_no);
			viewHolder.tvCellNo = (TextView) v.findViewById(R.id.tv_cell_detail);
			viewHolder.tvCellMake = (TextView) v.findViewById(R.id.tv_cell_make);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_order_seq);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvLocationName.setText(result.getCellNo());
		viewHolder.tvCellNo.setText(result.getCellDetail());
		viewHolder.tvCellMake.setText(result.getCellMake());
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getQty()));

		return v;
	}

	public void clear() {
		this.list = new ArrayList<ProductInfoResult>();
	}


}
