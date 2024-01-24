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

public class EmployeeMenu06ListAdapter extends BaseAdapter {

	private Context context;
	private List<EmployeeMenu06ListEntity> orderList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;


	class ViewHolder {
/*
		모델명, 가스, 단가코드,
		수량, 단가, 금액(VAT)
*/
		public TextView modelName;
		public TextView gas;
		public TextView upriceCode;

		public TextView qty;
		public TextView uprie;
		public TextView amt;

		public TextView saleDate;

	}

	public EmployeeMenu06ListAdapter() {

	}


	public EmployeeMenu06ListAdapter(Context context, List<EmployeeMenu06ListEntity> orderList) {

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
	
	public List<EmployeeMenu06ListEntity> getList() {
		
		return orderList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		EmployeeMenu06ListEntity result = orderList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_employee_menu_06, parent, false);

			viewHolder.modelName = v.findViewById(R.id.tv_employee_06_activity_list_header_model_name);
			viewHolder.gas = v.findViewById(R.id.tv_employee_06_activity_list_header_gas);
			viewHolder.upriceCode = v.findViewById(R.id.tv_employee_06_activity_list_header_upriceCode);
			viewHolder.qty = v.findViewById(R.id.tv_employee_06_activity_list_header_qty);
			viewHolder.uprie = v.findViewById(R.id.tv_employee_06_activity_list_header_uprie);
			viewHolder.amt = v.findViewById(R.id.tv_employee_06_activity_list_header_amt);
			viewHolder.saleDate = v.findViewById(R.id.tv_employee_06_activity_list_header_release_date);

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
		viewHolder.gas.setText(gas);
		viewHolder.upriceCode.setText(result.getUpriceCode());
		viewHolder.qty.setText(String.format("%,d", result.getQty()));
		viewHolder.uprie.setText(String.format("%,d", result.getUprie()));
		viewHolder.amt.setText(String.format("%,d",result.getAmt()));
		viewHolder.saleDate.setText(result.getSaleDate());


		if(result.isSelected()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.modelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.gas.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.upriceCode.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.qty.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.uprie.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.amt.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.saleDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isSelected()) {
			viewHolder.modelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.gas.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.upriceCode.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.qty.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.uprie.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.amt.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.saleDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));

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
