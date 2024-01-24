package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu06DetailEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ServiceEntity;
import kr.co.rinnai.dms.common.http.model.CategorizationResultVO;

public class AgencyMenu06ListAdapter extends BaseAdapter {

	private Context context;
	private List<AgencyMenu06ListEntity> serviceList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvDate;
		public TextView tvName;
		public TextView tvGubun;
		public TextView tvVisitDate;

	}

	public AgencyMenu06ListAdapter() {

	}


	public AgencyMenu06ListAdapter(Context context, List<AgencyMenu06ListEntity> unShippedList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.serviceList = unShippedList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(serviceList != null) {
			count = serviceList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return serviceList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public List<AgencyMenu06ListEntity> getList() {
		
		return serviceList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		AgencyMenu06ListEntity result = serviceList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_agency_menu_06, parent, false);

			viewHolder.tvDate = v.findViewById(R.id.tv_agency_06_activity_list_header_date);
			viewHolder.tvName = v.findViewById(R.id.tv_agency_06_activity_list_header_name);
			viewHolder.tvGubun = v.findViewById(R.id.tv_agency_06_activity_list_header_gubun);
			viewHolder.tvVisitDate = v.findViewById(R.id.tv_agency_06_activity_list_header_visit_date);

			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}


		if("A2".equals(result.getProcessState())) {
			viewHolder.tvDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a2, null));
			viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a2, null));
			viewHolder.tvGubun.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a2, null));
			viewHolder.tvVisitDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a2, null));
		} else if("A5".equals(result.getProcessState())) {
			viewHolder.tvDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a5, null));
			viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a5, null));
			viewHolder.tvGubun.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a5, null));
			viewHolder.tvVisitDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a5, null));
		} else if("A4".equals(result.getProcessState())) {
			viewHolder.tvDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a4, null));
			viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a4, null));
			viewHolder.tvGubun.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a4, null));
			viewHolder.tvVisitDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.agency_menu_06_list_row_value_a4, null));
		} else {

			viewHolder.tvDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.black, null));
			viewHolder.tvName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.black, null));
			viewHolder.tvGubun.setTextColor(context.getApplicationContext().getResources().getColor(R.color.black, null));
			viewHolder.tvVisitDate.setTextColor(context.getApplicationContext().getResources().getColor(R.color.black, null));
		}
		String cpDate = result.getCpDate().substring(2,10);
		String reqDate = result.getVisitReqDate().substring(2,10);
		viewHolder.tvDate.setText(cpDate);
		viewHolder.tvName.setText(result.getCustName());
		String gubun = "";
		if(result.getGubun() != null) {
			gubun = result.getGubun();
		}
		viewHolder.tvGubun.setText(gubun);
		viewHolder.tvVisitDate.setText(reqDate);

		return v;
	}

	public int getItemPosition(int boardNum) {
		int position = 0;
		for(int i = 0; i < serviceList.size(); i ++) {
			AgencyMenu06ListEntity category = serviceList.get(i);
			if(boardNum == category.getBoardNum()) {
				position = i;
				break;
			}
		}
		return position;

	}

    public void updateItem(AgencyMenu06ServiceEntity entity) {
        int position = 0;
        for(int i = 0; i < serviceList.size(); i ++) {
            AgencyMenu06ListEntity category = serviceList.get(i);
            if(entity.getBoardNum() == category.getBoardNum()) {
                serviceList.get(i).setCustName(entity.getCustName());
                serviceList.get(i).setVisitReqDate(entity.getVisitReqDate());

                break;
            }
        }


    }



}


