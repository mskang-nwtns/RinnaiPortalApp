package kr.co.rinnai.dms.wms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.eos.model.ItemCode;
import kr.co.rinnai.dms.wms.model.WmsMenu10SpinnerListEntity;

public class WmsMenu10SpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private List<WmsMenu10SpinnerListEntity> list;
    private LayoutInflater inflater;
    private ViewHolder viewHolder = null;
    public WmsMenu10SpinnerAdapter(Context context, List<WmsMenu10SpinnerListEntity> list) {

        this.context = context;
        this.list = list;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    private static class ViewHolder {

        TextView codeName;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        WmsMenu10SpinnerListEntity result = list.get(position);

        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.spinner_item_wms_ment_10_info, parent, false);

            viewHolder.codeName = (TextView) v.findViewById(R.id.tv_search_title_name);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.codeName.setText(result.getWarehouseName());

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return getView(position, convertView, parent);
    }

}
