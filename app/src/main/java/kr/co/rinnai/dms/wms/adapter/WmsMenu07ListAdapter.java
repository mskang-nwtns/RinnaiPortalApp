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

public class WmsMenu07ListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<LocationInfoResult> list;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvMakdDate;
		public TextView tvOrderSeq;
	}

	public WmsMenu07ListAdapter() {

	}


	public WmsMenu07ListAdapter(Context context, ArrayList<LocationInfoResult> list) {

		this.context = context;
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
	
	public ArrayList<LocationInfoResult> getList() {
		
		return list;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		LocationInfoResult result = list.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_inventory_du_diligence, parent, false);
			
			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_model_gas_type);
			viewHolder.tvMakdDate = (TextView) v.findViewById(R.id.tv_cell_make);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_order_seq);
			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		if(result.isSelected()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvMakdDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isSelected()) {
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvMakdDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));

		}



		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getType());
		viewHolder.tvMakdDate.setText(result.getCellMake());
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getQty()));

		return v;
	}

	public void clear() {
		this.list = new ArrayList<LocationInfoResult>();
	}

	public void setSelectItem(int position) {

		this.list.get(position).setSelected(!list.get(position).isSelected());

	}
	public int getSelectItem() {
		int position = -1;
		for(int i = 0; i < getCount(); i ++) {
			if(list.get(i).isSelected()) {
				position = i;
				break;
			}
		}
		return position;
	}

	public void clearSelect() {
		for(int i = 0; i < getCount(); i ++) {
			if(list.get(i).isSelected()) {
				list.get(i).setSelected(false);
			}
		}
	}

	public void addItem(Object obj) {
		if (obj instanceof  LocationInfoResult ) {

			if(list == null) {
				list = new ArrayList<LocationInfoResult>();
			}

			LocationInfoResult resultObj = (LocationInfoResult) obj;
			list.add(resultObj);
			Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : LocationInfoResult" );
		} else if(obj instanceof List) {

			List<LocationInfoResult> listObj = (ArrayList<LocationInfoResult>) obj;
			if(list == null) {
				list = new ArrayList<LocationInfoResult>();
			}
			list.addAll(listObj);
			Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "value : List" );
		}
	}


	public void removeItem(int position) {
		list.remove(position);
	}


}
