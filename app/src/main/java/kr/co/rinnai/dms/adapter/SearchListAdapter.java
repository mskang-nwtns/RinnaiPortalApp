package kr.co.rinnai.dms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.eos.model.SalesAgencyInfoResult;
import kr.co.rinnai.dms.eos.model.SalesModelInfoResult;

public class SearchListAdapter extends BaseAdapter {

	private Context context;
	private List<SalesModelInfoResult> modelList;
	private List<SalesAgencyInfoResult> agencyList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;
	private String type;

	class ViewHolder {

		public TextView tvName;

	}

	public SearchListAdapter() {

	}


	public SearchListAdapter(Context context, Object objectList, String type) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.type = type;
		if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type) || CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)  || CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
			this.modelList = (List<SalesModelInfoResult>) objectList;
		} else if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type) || CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
			this.agencyList = (List<SalesAgencyInfoResult>) objectList;
		}

	}

	@Override
	public int getCount() {
		int count = 0;
		if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type) || CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type) || CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
			count = modelList.size();
		} else if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type) || CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
			count = agencyList.size();
		}

		return count;
	}
	@Override
	public Object getItem(int position) {
		Object obj = null;
		if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type) || CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)  || CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
			obj = modelList.get(position);
		} else if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type) || CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
			obj = agencyList.get(position);
		}
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

		if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type) || CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)  || CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {
			name = String.format("%s(%s)", modelList.get(position).getModelCode(),  modelList.get(position).getModelName() );
		} else if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type) || CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
			name = String.format("%s(%s)", agencyList.get(position).getCustCode(), agencyList.get(position).getCustName() );
		}

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_search_info, parent, false);

			viewHolder.tvName = (TextView) v.findViewById(R.id.tv_search_title_name);

			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type) || CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)  || CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {

			for(int i = 0; i <this.modelList.size(); i ++) {
				if(this.modelList.get(i).isSelected()) {
					viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
				} else {
					viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
				}
			}

		} else if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type) || CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
			for(int i = 0; i <this.agencyList.size(); i ++) {
				if(this.agencyList.get(i).isSelected()) {
					viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
				} else {
					viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
				}

			}
		}


		viewHolder.tvName.setText(name);
		return v;
	}

	public void setSelectItem(int position, TextView tv) {
		tv.setText("");
		if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type) || CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)  || CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {

			for(int i = 0; i < this.modelList.size(); i ++) {
				this.modelList.get(i).setSelected(false);
				if(position == i) {
					this.modelList.get(i).setSelected(!this.modelList.get(i).isSelected());
					if(this.modelList.get(i).isSelected()) {
						tv.setText(this.modelList.get(i).getModelName() + String.format("(%s)", this.modelList.get(i).getModelCode() ));

					}

				} else {

				}

			}

		} else if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type) || CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
			for(int i = 0; i <this.agencyList.size(); i ++) {
				this.agencyList.get(i).setSelected(false);
				if(position == i) {
					this.agencyList.get(i).setSelected(!this.agencyList.get(i).isSelected());

					if(this.agencyList.get(i).isSelected()) {
						tv.setText(this.agencyList.get(i).getCustName() + String.format("(%s)", this.agencyList.get(i).getCustCode() ));

					}
				} else {

				}

			}
		}

	}

	public String getSelectCode() {

		String code = null;
		if(CommonValue.AOS_NOW_VIEW_NAME_MODEL.equals(type) || CommonValue.AOS_NOW_VIEW_NAME_MODEL_05.equals(type)  || CommonValue.WMS_NOW_VIEW_NAME_MODEL_05.equals(type)) {

			for(int i = 0; i <this.modelList.size(); i ++) {
				if(this.modelList.get(i).isSelected()) {
					code = this.modelList.get(i).getModelCode();
					break;
				}
			}

		} else if(CommonValue.AOS_NOW_VIEW_NAME_CUST.equals(type) || CommonValue.WMS_NOW_VIEW_NAME_AGENCY_06.equals(type)) {
			for(int i = 0; i <this.agencyList.size(); i ++) {
				if(this.agencyList.get(i).isSelected()) {
					code = this.agencyList.get(i).getCustCode();
					break;
				}

			}
		}

		return code;
	}

	public String getSelectName() {

		String name = null;

		for(int i = 0; i <this.modelList.size(); i ++) {
			if(this.modelList.get(i).isSelected()) {
				name = this.modelList.get(i).getModelName();
				break;
			}
		}


		return name;
	}

	public String getAgencyName() {

		String name = null;

		for(int i = 0; i <this.agencyList.size(); i ++) {
			if(this.agencyList.get(i).isSelected()) {
				name = this.agencyList.get(i).getCustName();
				break;
			}
		}


		return name;
	}

}
