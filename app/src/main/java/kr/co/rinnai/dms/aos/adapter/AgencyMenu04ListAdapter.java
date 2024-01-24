package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu04ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu04SalesInfoTranEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ListEntity;

public class AgencyMenu04ListAdapter extends BaseAdapter {

	private Context context;

	private List<AgencyMenu04ListEntity> list;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvTransGubun;
		public TextView tvTransDate;
		public TextView tvCModelName;

		public TextView tvCCode;

	}

	public AgencyMenu04ListAdapter() {

	}


	public AgencyMenu04ListAdapter(Context context, List<AgencyMenu04ListEntity> list) {

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
	
	public List<AgencyMenu04ListEntity> getList() {
		
		return list;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {





		View v = convertView;
		AgencyMenu04ListEntity result = list.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_agency_menu_04, parent, false);

			viewHolder.tvTransGubun =  v.findViewById(R.id.tv_agency_04_activity_list_header_trans_gubun);
			viewHolder.tvTransDate = v.findViewById(R.id.tv_agency_04_activity_list_header_trans_date);
			viewHolder.tvCModelName = v.findViewById(R.id.tv_agency_04_activity_list_header_c_model_name);

			viewHolder.tvCCode = v.findViewById(R.id.tv_agency_04_activity_list_header_c_code);



			v.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		if(result.isSelected()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.tvTransGubun.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvTransDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvCModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvCCode.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));

		} else if(!result.isSelected()) {
			viewHolder.tvTransGubun.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvTransDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvCModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvCCode.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));

		}

		String tranGubun = "이관";
		if ("2".equals(result.getTransGubun())) {
			tranGubun = "권장";
		}
		viewHolder.tvTransGubun.setText(result.getTransName());
		viewHolder.tvTransDate.setText(result.getTransDate());
		viewHolder.tvCModelName.setText(result.getcModelName());

		viewHolder.tvCCode.setText(result.getcCode());

		return v;
	}

	public void setSelectItem(int position) {
		for(int i = 0; i <this.list.size(); i ++) {
			if(position == i) {
				this.list.get(i).setSelected(!this.list.get(i).isSelected());
			} else {
				this.list.get(i).setSelected(false);
			}

		}

	}

	public Object getSelectItem() {
		AgencyMenu04ListEntity result = null;
		for(int i = 0; i < list.size(); i ++) {

			if(list.get(i).isSelected()) {
				result = list.get(i);
				break;

			}
		}
		return result;

	}

	public int getItemPosition(String boardNum) {
		int position = 0;
		for(int i = 0; i < list.size(); i ++) {
			AgencyMenu04ListEntity category = list.get(i);
			if(boardNum.equals( category.getAsSeq())) {
				position = i;
				break;
			}
		}
		return position;

	}
	

}
