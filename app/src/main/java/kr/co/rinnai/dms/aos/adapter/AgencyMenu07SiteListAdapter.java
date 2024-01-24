package kr.co.rinnai.dms.aos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu03ListEntity;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteInfo;

public class AgencyMenu07SiteListAdapter extends BaseAdapter {

//	private Context context;
	private List<AgencyMenu07SiteInfo> unShippedList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvSiteName;
		public TextView tvSaleYm;
		public TextView tvHouseCnt;

	}

	public AgencyMenu07SiteListAdapter() {

	}


	public AgencyMenu07SiteListAdapter(Context context, List<AgencyMenu07SiteInfo> unShippedList) {

//		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.unShippedList = unShippedList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(unShippedList != null) {
			count = unShippedList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return unShippedList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public List<AgencyMenu07SiteInfo> getList() {
		
		return unShippedList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {


		View v = convertView;
		AgencyMenu07SiteInfo result = unShippedList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.item_site_info, parent, false);

			viewHolder.tvSiteName =  v.findViewById(R.id.tv_site_name);
			viewHolder.tvSaleYm = v.findViewById(R.id.tv_sale_ym);
			viewHolder.tvHouseCnt = v.findViewById(R.id.tv_house_cnt);

			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		String saleYm = result.getSaleYm();
		if(saleYm == null) {
			saleYm = "-";
		} else if(saleYm.length() == 5) {
			saleYm = String.format("%s년%02d일", saleYm.substring(0, 4), Integer.parseInt(saleYm.substring(4, 5)));
		} else if(saleYm.length() == 6) {
			saleYm = String.format("%s년%02d일", saleYm.substring(0, 4), Integer.parseInt(saleYm.substring(4, 6)));
		}

		viewHolder.tvSiteName.setText(result.getSiteName());
		viewHolder.tvSaleYm.setText(saleYm);
		viewHolder.tvHouseCnt.setText(String.format("%,d", result.getTotHouseholdCnt()));

		return v;
	}
	

}
