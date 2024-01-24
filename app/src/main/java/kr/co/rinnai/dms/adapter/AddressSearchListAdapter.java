package kr.co.rinnai.dms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu06ListEntity;
import kr.co.rinnai.dms.common.http.model.AddressJusoData;

public class AddressSearchListAdapter extends BaseAdapter {

	private Context context;
	private List<AddressJusoData> addressList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvRoad;
		public TextView tvJibun;
		public TextView tvZipcode;

	}

	public AddressSearchListAdapter() {

	}


	public AddressSearchListAdapter(Context context, List<AddressJusoData> addressList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.addressList = addressList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(addressList != null) {
			count = addressList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return addressList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	
	public List<AddressJusoData> getList() {
		
		return addressList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		AddressJusoData result = addressList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_zipcode, parent, false);

			viewHolder.tvRoad = v.findViewById(R.id.tv_common_address_search_road);
			viewHolder.tvJibun = v.findViewById(R.id.tv_common_address_search_jibun);
			viewHolder.tvZipcode = v.findViewById(R.id.tv_common_address_search_zipcode);

			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvRoad.setText(result.getRoadAddr());
		viewHolder.tvJibun.setText(result.getJibunAddr());
		viewHolder.tvZipcode.setText(result.getZipNo());


		return v;
	}
	

}
