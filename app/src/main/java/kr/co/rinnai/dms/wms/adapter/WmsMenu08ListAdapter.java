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
import kr.co.rinnai.dms.wms.model.LocationInfoResult;

public class WmsMenu08ListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<LocationInfoResult> productInfoList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvModelCode;
		public TextView tvOrderSeq;
		public TextView tvOrderTotal;
	}

	public WmsMenu08ListAdapter() {

	}


	public WmsMenu08ListAdapter(Context context, ArrayList<LocationInfoResult> productInfoList) {

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
			v = inflater.inflate(R.layout.list_item_product_info, parent, false);

			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_gas_type);
			viewHolder.tvModelCode = (TextView) v.findViewById(R.id.tv_model_code);
			viewHolder.tvOrderTotal = (TextView) v.findViewById(R.id.tv_order_total);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_order_seq);

			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvOrderTotal.setVisibility(View.VISIBLE);
		viewHolder.tvModelCode.setVisibility(View.GONE);

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "position value :" + position + "result.isRead() : " + result.isSelected() );
				
		if(result.isSelected()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvModelCode.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderTotal.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isSelected()) {
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvModelCode.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvOrderTotal.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));

		}

		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getType());
		viewHolder.tvOrderTotal.setText(result.getTotal());
		viewHolder.tvModelCode.setText(result.getCellMake());
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getQty()));
		
		return v;
	}
	
	public void setSelectItem(int position) {
		
		this.productInfoList.get(position).setSelected(!productInfoList.get(position).isSelected());

	}

	public void removeItem(int position) {
		this.productInfoList.remove(position);

	}

	public void clear() {
		this.productInfoList = new ArrayList<LocationInfoResult>();
	}

	public void addItem(Object obj) {
		if (obj instanceof  LocationInfoResult ) {

			if(productInfoList == null) {
				productInfoList = new ArrayList<LocationInfoResult>();
			}

			LocationInfoResult resultObj = (LocationInfoResult) obj;
			productInfoList.add(resultObj);
			Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : LocationInfoResult" );
		} else if(obj instanceof List) {

			List<LocationInfoResult> listObj = (ArrayList<LocationInfoResult>) obj;
			if(productInfoList == null) {
				productInfoList = new ArrayList<LocationInfoResult>();
			}
			productInfoList.addAll(listObj);
			Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : List" );
		}
	}

	public int getReadingItem(String barcode) {

		int position = -1;
		for(int i = 0; i < productInfoList.size(); i ++) {
			LocationInfoResult result = productInfoList.get(i);

			if(barcode.substring(0, 6).equals(result.getBarcode())) {
				position = i;
				break;
			}
		}

		return position;
	}

	public void clearReadingItem() {
		if(productInfoList != null) {
			for (int i = 0; i < productInfoList.size(); i++) {
				LocationInfoResult result = productInfoList.get(i);
				if (result.isSelected()) {
					result.setSelected(false);
				}
			}
		}
	}

	public boolean isSelectedItem() {
		boolean isSelected = false;
		for(int i = 0; i < productInfoList.size(); i ++) {
			LocationInfoResult result = productInfoList.get(i);
			if(result.isSelected()) {
				isSelected = true;

				break;
			}
		}

		return isSelected;
	}

	public int addItem(LocationInfoResult item) {
		boolean newObj = true;
		int position = 0;
		for(int i = 0; i < productInfoList.size(); i ++) {
			LocationInfoResult result = productInfoList.get(i);
			result.setSelected(false);
			if(result.getItemCode().equals(item.getItemCode())) {
				int qty = Integer.parseInt(result.getQty());
				qty = qty + 1;

				newObj = false;

				productInfoList.get(i).setQty(String.format("%d", qty));
				position = i;
				result.setSelected(true);
			}

		}

		if(newObj) {
			productInfoList.add(item);
			position = productInfoList.size() - 1;
		}
		return position;
	}

	public int removeItem(LocationInfoResult item) {
		int position = 0;
		for(int i = 0; i < productInfoList.size(); i ++) {
			LocationInfoResult result = productInfoList.get(i);
			result.setSelected(false);
			if(result.getItemCode().equals(item.getItemCode())) {
				int qty = Integer.parseInt(result.getQty());
				qty = qty - 1;

				productInfoList.get(i).setQty(String.format("%d", qty));
				position = productInfoList.size() - 1;
				result.setSelected(true);

			}

		}

		return position;


	}


	public int addListItem(LocationInfoResult item) {
		boolean newObj = true;
		int position = 0;
		for(int i = 0; i < productInfoList.size(); i ++) {
			LocationInfoResult result = productInfoList.get(i);
			result.setSelected(false);
			if(result.getItemCode().equals(item.getItemCode())) {
				int total = Integer.parseInt(result.getTotal());
				total = total + Integer.parseInt(item.getTotal());

				newObj = false;

				productInfoList.get(i).setTotal(String.format("%d", total));
				position = i;
//				result.setSelected(true);
			}

		}

		if(newObj) {
			productInfoList.add(item);
			position = productInfoList.size() - 1;
		}
		return position;
	}


}
