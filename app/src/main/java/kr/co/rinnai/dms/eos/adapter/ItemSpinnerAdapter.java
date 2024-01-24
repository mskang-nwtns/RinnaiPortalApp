package kr.co.rinnai.dms.eos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;

import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.http.model.CategorizationResultVO;
import kr.co.rinnai.dms.eos.model.ItemCode;

public class ItemSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private List<ItemCode> list;
    private LayoutInflater inflater;
    private ViewHolder viewHolder = null;
    public ItemSpinnerAdapter(Context context, List<ItemCode> list) {

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
        ItemCode result = list.get(position);

        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_item_search_info, parent, false);

            viewHolder.codeName = (TextView) v.findViewById(R.id.tv_search_title_name);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.codeName.setText(result.getCodeName());

        return v;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

        return getView(position, convertView, parent);
    }

    public int getItemPosition(String code) {
        int position = 0;
        for(int i = 0; i < list.size(); i ++) {
            ItemCode category = list.get(i);
            if(code.equals(category.getCodeItem())) {
                position = i;
                break;
            }
        }
        return position;

    }

}
