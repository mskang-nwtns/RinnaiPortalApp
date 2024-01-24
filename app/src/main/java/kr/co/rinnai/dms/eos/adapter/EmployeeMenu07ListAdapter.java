package kr.co.rinnai.dms.eos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.eos.model.EmployeeMenu06ListEntity;
import kr.co.rinnai.dms.eos.model.EmployeeMenu07ListEntity;

public class EmployeeMenu07ListAdapter extends BaseAdapter {

	private Context context;
	private List<EmployeeMenu07ListEntity> orderList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;


	class ViewHolder {
/*
		모델명, 가스, 단가코드,
		수량, 단가, 금액(VAT)
*/
		public TextView clientName;
		public TextView modelName;
		public TextView qty;
		public TextView saleDate;
		public TextView outReqDate;

	}

	public EmployeeMenu07ListAdapter() {

	}


	public EmployeeMenu07ListAdapter(Context context, List<EmployeeMenu07ListEntity> orderList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.orderList = orderList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(orderList != null) {
			count = orderList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return orderList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public List<EmployeeMenu07ListEntity> getList() {
		
		return orderList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		EmployeeMenu07ListEntity result = orderList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_employee_menu_07, parent, false);


			viewHolder.clientName = v.findViewById(R.id.employee_07_activity_list_row_client_name);
			viewHolder.modelName = v.findViewById(R.id.employee_07_activity_list_row_model_name);
			viewHolder.qty = v.findViewById(R.id.employee_07_activity_list_row_qty);
			viewHolder.saleDate = v.findViewById(R.id.employee_07_activity_list_row_sale_date);
			viewHolder.outReqDate = v.findViewById(R.id.employee_07_activity_list_row_out_req_date);

			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.modelName.setText(result.getModelName());
		String gas = "P";
		if ("LPG".equals(result.getGas())) {
			gas = "P";
		} else if("LNG".equals(result.getGas())) {
			gas = "N";
		}
		viewHolder.clientName.setText(result.getClientName());
		viewHolder.modelName.setText(result.getModelName());
		viewHolder.qty.setText(String.format("%,d", result.getQty()));
		viewHolder.saleDate.setText(result.getSaleDate());
		viewHolder.outReqDate.setText(result.getOutReqDate());


		if(result.isSelected()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.clientName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.modelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.qty.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.saleDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.outReqDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isSelected()) {
			viewHolder.clientName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.modelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.qty.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.saleDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.outReqDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));

		}

		return v;
	}

	public void setSelectItem(int position) {
		for(int i = 0; i < orderList.size(); i ++) {
			if(i == position) {
				this.orderList.get(position).setSelected(!orderList.get(position).isSelected());
			} else {
				this.orderList.get(i).setSelected(false);
			}
		}


	}
	

}
