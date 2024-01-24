package kr.co.rinnai.dms.wms.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.wms.model.AgencyBarcodeReading;
import kr.co.rinnai.dms.wms.model.LocationInfoResult;

public class WmsMenuReadingInfoAdapter extends BaseAdapter {

	private Context context;
	private List<AgencyBarcodeReading> productInfoList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;


	class ViewHolder {

		public TextView tvBarcode;
		public TextView tvStatus;


	}

	public WmsMenuReadingInfoAdapter(Context context, List<AgencyBarcodeReading> productInfoList) {

		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.productInfoList = productInfoList;
	}

	@Override
	public int getCount() {
		int count = 0;
		if(productInfoList != null) {
			count = productInfoList.size();
		}
		return count;
	}
	@Override
	public Object getItem(int position) {

		return productInfoList.get(position);
	}
	
	@Override
	public long getItemId(int position) {

		return position;
	}
	

	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		AgencyBarcodeReading result = productInfoList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_reading_info, parent, false);

			viewHolder.tvBarcode = (TextView) v.findViewById(R.id.tv_barcode);
			viewHolder.tvStatus = (TextView) v.findViewById(R.id.tv_status);

			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvBarcode.setText(result.getBarcode());
		String status = "전송";

		if(!result.isStatus()) {
			status = "작업";
		}
		viewHolder.tvStatus.setText(status);

		return v;
	}

}
