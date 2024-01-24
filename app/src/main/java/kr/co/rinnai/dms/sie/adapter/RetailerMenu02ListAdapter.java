package kr.co.rinnai.dms.sie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu02ListEntity;
import kr.co.rinnai.dms.sie.model.RetailerMenu02ListEntity;

public class RetailerMenu02ListAdapter extends BaseAdapter {

//	private Context context;
	private List<RetailerMenu02ListEntity> orderList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		private TextView agencyOrderBarcode;
		private TextView tvAddr;
		private TextView tvModelName;
		private TextView tvType;
		private TextView tvImage;
		private TextView tvSign;
		private TextView tvQty;



	}

	public RetailerMenu02ListAdapter() {

	}


	public RetailerMenu02ListAdapter(Context context, List<RetailerMenu02ListEntity> orderList) {

//		this.context = context;
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
	
	public List<RetailerMenu02ListEntity> getList() {
		
		return orderList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {





		View v = convertView;
		RetailerMenu02ListEntity result = orderList.get(position);

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_retailer_menu_02, parent, false);

			viewHolder.tvAddr =  v.findViewById(R.id.tv_retailer_02_activity_list_header_addr);
			viewHolder.tvModelName = v.findViewById(R.id.tv_retailer_02_activity_list_header_model);
			viewHolder.tvType = v.findViewById(R.id.tv_retailer_02_activity_list_header_type);

			viewHolder.tvImage = v.findViewById(R.id.tv_retailer_02_activity_list_header_image);
			viewHolder.tvSign = v.findViewById(R.id.tv_retailer_02_activity_list_header_sign);

			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}
		String addrInfo = String.format("%s(%s)", result.getAddr1(), result.getClientName());
		viewHolder.tvAddr.setText(addrInfo);
		viewHolder.tvModelName.setText(result.getModelName().trim());
		viewHolder.tvType.setText(result.getType());
		String isImageUpload = "N";
		if(result.getImage01() != null) {
			isImageUpload = "Y";
		} else if (result.getImage01() == null) {
			isImageUpload = "N";
		}

		String isSignUpload = "N";
		if(result.getSign01() != null) {
			isSignUpload = "Y";
		} else if (result.getSign01() == null) {
			isSignUpload = "N";
		}



		viewHolder.tvImage.setText(isImageUpload);
		viewHolder.tvSign.setText(isSignUpload);

		return v;
	}
	

}
