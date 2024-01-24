package kr.co.rinnai.dms.wms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeResult;
import kr.co.rinnai.dms.wms.model.LocationInfoResult;

public class WmsMenu04ListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<AgencyBarcodeResult> productInfoList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvOrderSeq;
	}

	public WmsMenu04ListAdapter() {

	}


	public WmsMenu04ListAdapter(Context context, ArrayList<AgencyBarcodeResult> productInfoList) {

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
	
	public ArrayList<AgencyBarcodeResult> getList() {
		
		return productInfoList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		AgencyBarcodeResult result = productInfoList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_wms_menu_04, parent, false);

			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_gas_type);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_order_seq);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}


		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "position value :" + position + "result.isRead() : " + result.isSelected() );
				
		if(result.isSelected()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isSelected()) {
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));

		}

		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getType());
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getInstructions()));
		
		return v;
	}
	
	public void setSelectItem(int position) {
		
		this.productInfoList.get(position).setSelected(!productInfoList.get(position).isSelected());

	}

	public void removeItem(int position) {
		this.productInfoList.remove(position);

	}

	public void clear() {
		this.productInfoList = new ArrayList<AgencyBarcodeResult>();
	}

	public void addItem(Object obj) {
		if (obj instanceof  LocationInfoResult ) {

			if(productInfoList == null) {
				productInfoList = new ArrayList<AgencyBarcodeResult>();
			}

			AgencyBarcodeResult resultObj = (AgencyBarcodeResult) obj;
			productInfoList.add(resultObj);
			Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : LocationInfoResult" );
		} else if(obj instanceof List) {

			List<AgencyBarcodeResult> listObj = (ArrayList<AgencyBarcodeResult>) obj;
			if(productInfoList == null) {
				productInfoList = new ArrayList<AgencyBarcodeResult>();
			}
			productInfoList.addAll(listObj);
			Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : List" );
		}
	}

	public int getSelectItemCount() {

		int count = 0;
		for(int i = 0; i < productInfoList.size(); i ++) {
			AgencyBarcodeResult result = productInfoList.get(i);
			if(result.isSelected()) {
				int qty = Integer.parseInt(result.getInstructions());
				Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, String.format("value : %d : %d ", count, qty));
				count += qty;
			}

		}

		return count;
	}


}
