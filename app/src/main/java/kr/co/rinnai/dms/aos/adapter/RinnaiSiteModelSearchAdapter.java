package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.eos.model.SalesAgencyInfoResult;
import kr.co.rinnai.dms.eos.model.SalesModelInfoResult;

public class RinnaiSiteModelSearchAdapter extends BaseAdapter {

	private Context context;
	private List<AgencyMenu07SiteModelInfo> modelList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvGoodsName;
		public TextView tvName;
		public TextView tvGasType;

	}

	public RinnaiSiteModelSearchAdapter() {

	}


	public RinnaiSiteModelSearchAdapter(Context context, List<AgencyMenu07SiteModelInfo> objectList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.modelList = objectList;

	}

	@Override
	public int getCount() {
		int count = 0;
			count = modelList.size();

		return count;
	}
	@Override
	public Object getItem(int position) {
		Object obj = null;
		obj = modelList.get(position);
		return obj;
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;;
		String name = null;
		String gGoodsTypeName = null;
		String gasType = null;

		AgencyMenu07SiteModelInfo modelInfo = modelList.get(position);
		name = modelInfo.getModelName();
		gGoodsTypeName = modelInfo.getgGoodsTypeName();
		gasType = modelInfo.getGasName();

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.item_site_model_search_info, parent, false);

			viewHolder.tvGoodsName = (TextView)v.findViewById(R.id.tv_search_goods_type_name);
			viewHolder.tvName = (TextView) v.findViewById(R.id.tv_search_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_search_gas_type);


			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}


		if(modelInfo.isSelected()) {
			viewHolder.tvGoodsName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
		} else {
			viewHolder.tvGoodsName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
		}


		viewHolder.tvGoodsName.setText(gGoodsTypeName);
		viewHolder.tvName.setText(name);
		viewHolder.tvGasType.setText(gasType);

		return v;
	}

	public void setSelectItem(int position) {
		for(int i = 0; i < this.modelList.size(); i ++) {
			this.modelList.get(i).setSelected(false);
			if(position == i) {
				this.modelList.get(i).setSelected(!this.modelList.get(i).isSelected());
				if(this.modelList.get(i).isSelected()) {

				}

			} else {

			}

		}

	}

	public AgencyMenu07SiteModelInfo getSelectItem() {
		AgencyMenu07SiteModelInfo modelInfo = null;
		for(int i = 0; i < this.modelList.size(); i ++) {
			if(modelList.get(i).isSelected()) {
				modelInfo = modelList.get(i);
				break;
			}
		}
		return modelInfo;

	}


}
