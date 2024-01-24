package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu04ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;

public class AgencyMenu07ModelInsertListAdapter extends BaseAdapter {

	private Context context;

	private List<AgencyMenu07SiteModelInfo> list;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvGoodsName;
		public TextView tvName;
		public TextView tvGasType;

		public TextView tvCount;

	}

	public AgencyMenu07ModelInsertListAdapter() {

	}


	public AgencyMenu07ModelInsertListAdapter(Context context, List<AgencyMenu07SiteModelInfo> list) {

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
	
	public List<AgencyMenu07SiteModelInfo> getList() {
		
		return list;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		AgencyMenu07SiteModelInfo result = list.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.item_site_model_info_insert, parent, false);

			viewHolder.tvGoodsName =  v.findViewById(R.id.tv_insert_goods_type_name);
			viewHolder.tvName = v.findViewById(R.id.tv_insert_model_name);
			viewHolder.tvGasType = v.findViewById(R.id.tv_insert_gas_type);

			viewHolder.tvCount = v.findViewById(R.id.tv_insert_count);

			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		if(result.isSelected()) {
			viewHolder.tvGoodsName.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvName.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvGasType.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvCount.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isSelected()) {
			viewHolder.tvGoodsName.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_one));
			viewHolder.tvName.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_two));
			viewHolder.tvGasType.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_one));
			viewHolder.tvCount.setBackgroundColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_two));

		}

		viewHolder.tvGoodsName.setText(result.getgGoodsTypeName());
		viewHolder.tvName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getGasName());

		viewHolder.tvCount.setText(String.format("%,d", result.getQty()));

		return v;
	}

	public void setSelectItem(int position) {

		this.list.get(position).setSelected(!this.list.get(position).isSelected());


	}

	public List<Integer> getSelectItem() {
		List<Integer> selectedList = null;

		for(int i = 0; i < list.size(); i ++) {
			if(list.get(i).isSelected()) {
				if(selectedList == null) {
					selectedList = new ArrayList<Integer>();
				}

				selectedList.add(i);

			}
		}

		return selectedList;
	}

	public void removeObj(int position) {
		list.remove(position);
	}

}
