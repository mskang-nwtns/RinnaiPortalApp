package kr.co.rinnai.dms.wms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.wms.model.LocationInfoResult;

public class WmsMenu10ListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<LocationInfoResult> productInfoList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvOrderSeq;
		public TextView tvBoxCount;
		public TextView tvOrderTotal;

	}

	public WmsMenu10ListAdapter() {

	}


	public WmsMenu10ListAdapter(Context context, ArrayList<LocationInfoResult> productInfoList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.productInfoList = productInfoList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(productInfoList != null) {
			count = productInfoList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return productInfoList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public ArrayList<LocationInfoResult> getList() {
		
		return productInfoList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		LocationInfoResult result = productInfoList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_wms_menu_10, parent, false);

			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_gas_type);
			viewHolder.tvOrderTotal = (TextView) v.findViewById(R.id.tv_model_code);
			viewHolder.tvBoxCount = (TextView) v.findViewById(R.id.tv_order_total);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_order_seq);

			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvOrderTotal.setVisibility(View.GONE);
		viewHolder.tvBoxCount.setVisibility(View.GONE);

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "position value :" + position + "result.isRead() : " + result.isSelected() );

		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getType());
		viewHolder.tvOrderTotal.setText(result.getTotal());
		if(result.getBoxCount() == null) {
			viewHolder.tvBoxCount.setText(result.getQty());
		} else {
			viewHolder.tvBoxCount.setText(result.getBoxCount());
		}
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getQty()));
		
		return v;
	}


}
