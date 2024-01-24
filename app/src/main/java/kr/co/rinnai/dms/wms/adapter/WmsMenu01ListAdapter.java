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

public class WmsMenu01ListAdapter extends BaseAdapter {

//	private Context context;
	private ArrayList<ProductInfoResult> list;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvModelCode;
		public TextView tvOrderSeq;
	}

	public WmsMenu01ListAdapter() {

	}


	public WmsMenu01ListAdapter(Context context, ArrayList<ProductInfoResult> list) {

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
			v = inflater.inflate(R.layout.list_item_product_info, parent, false);
			
			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_gas_type);
			viewHolder.tvModelCode = (TextView) v.findViewById(R.id.tv_model_code);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_order_seq);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getType());
		viewHolder.tvModelCode.setText(result.getCellMake());
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getQty()));
		
		return v;
	}

	public void clear() {
		this.list = new ArrayList<ProductInfoResult>();
	}


}
