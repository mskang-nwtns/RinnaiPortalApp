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
import kr.co.rinnai.dms.wms.model.LocationInfoResult;

public class WmsMenu09ListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<LocationInfoResult> productInfoList;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder = null;

	class ViewHolder {

		public TextView tvModelName;
		public TextView tvGasType;
		public TextView tvModelCode;
		public TextView tvOrderSeq;
		public TextView tvBoxCount;
		public TextView tvOrderTotal;

	}

	public WmsMenu09ListAdapter() {

	}


	public WmsMenu09ListAdapter(Context context, ArrayList<LocationInfoResult> productInfoList) {

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
	
	public ArrayList<LocationInfoResult> getList() {
		
		return productInfoList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View v = convertView;
		LocationInfoResult result = productInfoList.get(position);
		
		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.list_item_product_info, parent, false);

			viewHolder.tvModelName = (TextView) v.findViewById(R.id.tv_model_name);
			viewHolder.tvGasType = (TextView) v.findViewById(R.id.tv_gas_type);
			viewHolder.tvOrderTotal = (TextView) v.findViewById(R.id.tv_model_code);
			viewHolder.tvBoxCount = (TextView) v.findViewById(R.id.tv_order_total);
			viewHolder.tvOrderSeq = (TextView) v.findViewById(R.id.tv_order_seq);

			
			v.setTag(viewHolder);
		} else { 
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.tvOrderTotal.setVisibility(View.VISIBLE);
		viewHolder.tvBoxCount.setVisibility(View.VISIBLE);

		Log.w(CommonValue.LOG_TAG_SCANNER_RESULT_CHECK, "position value :" + position + "result.isRead() : " + result.isSelected() );
				
		if(result.isSelected()) {
			v.setBackgroundResource(R.drawable.list_view_select_row_border);
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderTotal.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvBoxCount.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_list_view_row_select));


		} else if(!result.isSelected()) {
			viewHolder.tvModelName.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvGasType.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));
			viewHolder.tvOrderTotal.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvBoxCount.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_one));
			viewHolder.tvOrderSeq.setTextColor(context.getApplicationContext().getResources().getColor(R.color.text_view_listview_row_value_two));


		}

		viewHolder.tvModelName.setText(result.getModelName());
		viewHolder.tvGasType.setText(result.getType());
		viewHolder.tvOrderTotal.setText(result.getTotal());
		if(result.getBoxCount() == null) {
			viewHolder.tvBoxCount.setText(result.getQty());
		} else {
			viewHolder.tvBoxCount.setText(result.getBoxCount());
		}
		viewHolder.tvOrderSeq.setText(String.valueOf(result.getQty()));
		
		return v;
	}
	
	public void setSelectItem(int position) {
		
		this.productInfoList.get(position).setSelected(!productInfoList.get(position).isSelected());

	}

	public void addItem(LocationInfoResult item) {
		int position = -1;
		for(int i = 0; i < productInfoList.size(); i++) {
			LocationInfoResult listItem = productInfoList.get(i);

			if( listItem.getCellDetail().equals(item.getCellDetail())
				&& listItem.getCellMake().equals(item.getCellMake())
				&& listItem.getItemCode().equals(item.getItemCode()) ){
				position = i;
				break;
			}
		}
		if(position == -1 ) {
			productInfoList.add(item);
		} else if (position > -1) {
			int qty = Integer.parseInt(productInfoList.get(position).getQty());
			qty = qty + 1;
			int boxCount = Integer.parseInt(productInfoList.get(position).getBoxCount());
			boxCount = boxCount + 1;
			productInfoList.get(position).setQty(String.format("%d", qty));
			productInfoList.get(position).setBoxCount(String.format("%d", boxCount));
		}

	}

	public void removeItem(LocationInfoResult item) {
		int position = -1;
		for(int i = 0; i < productInfoList.size(); i++) {
			LocationInfoResult listItem = productInfoList.get(i);
			if( listItem.getCellDetail().equals(item.getCellDetail())
					&& listItem.getCellMake().equals(item.getCellMake())
					&& listItem.getItemCode().equals(item.getItemCode()) ){
				position = i;
				break;
			}
		}

		if (position > -1) {
			int qty = Integer.parseInt(productInfoList.get(position).getQty());
			if(qty > 1) {
				qty = qty - 1;
				productInfoList.get(position).setQty(String.format("%d", qty));
			} else if(qty == 1) {
				productInfoList.remove(position);
			}


		}
	}

	public void setList(ArrayList<LocationInfoResult> productInfoList) {
		this.productInfoList = productInfoList;

	}

	public void setBoxCount(int position, String boxCount) {
		productInfoList.get(position).setBoxCount(boxCount);

	}

	public LocationInfoResult getItem(String agencyOrderBarcode, String remark, String itemCode) {
		LocationInfoResult obj = null;
		int position = -1;
		for(int i = 0; i < productInfoList.size(); i++) {
			LocationInfoResult listItem = productInfoList.get(i);
			String cellDetail = listItem.getCellDetail();
			String cellMake = listItem.getOriginalCellMake();
			String listItemCode = listItem.getOriginalItemCode();
			boolean a =cellDetail.equals(agencyOrderBarcode);
			boolean b =cellMake.trim().equals(remark.trim());
			boolean c =listItemCode.equals(itemCode);

			boolean a1 =agencyOrderBarcode.equals(cellDetail);
			boolean b1 =remark.trim().equals(cellMake.trim());
			boolean c1 =itemCode.equals(listItemCode);

			if( listItem.getCellDetail().equals(agencyOrderBarcode)
					&& listItem.getOriginalCellMake().equals(remark)
					&& listItem.getOriginalItemCode().equals(itemCode)){
				position = i;
				break;
			}
		}

		if (position > -1) {
			obj = productInfoList.get(position);
		}
		return obj;
	}


}
