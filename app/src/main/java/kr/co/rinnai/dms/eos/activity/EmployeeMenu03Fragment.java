package kr.co.rinnai.dms.eos.activity;

import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.adapter.StockStoreListAdapter;
import kr.co.rinnai.dms.eos.model.StockByStoreResult;

public class EmployeeMenu03Fragment extends ListFragment {
	private View convertView = null;

	//	private String ="합계"
	//	private String ="18일실적"
	//	private String ="18일계획"

	private ListView lvAosMewnu03;
	
	private static List<Object> list = null;

	private StockStoreListAdapter listAdapter = null;
	private static Context con;
	private static String viewType;
	

	public static Fragment newInstance(Context context, int position, List<Object> objList, String type) {
		con = context;
		Bundle b = new Bundle();
		b.putInt("pos", position);
		list = objList;
		viewType= type;
		return Fragment.instantiate(context, EmployeeMenu03Fragment.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.layout_stock_store_fragment, container, false);

		lvAosMewnu03 = (ListView) convertView.findViewById(R.id.lv_aos_menu_03);
		int pos = this.getArguments().getInt("pos");
		ArrayList<StockByStoreResult> resultList = (ArrayList<StockByStoreResult>) list.get(pos);

		String gasType = "LNG";
		if(pos == 0) {
			gasType = "LPG";
		} else if(pos == 1) {
			gasType = "LNG";
		}

		listAdapter = new StockStoreListAdapter(con, resultList, viewType, gasType);

		lvAosMewnu03.setAdapter(listAdapter);

/*
		tvMPlanAdditiveRate.setText();
		tvDPlanAdditiveRate.setText();

		tvAdditiveRate.setText();
		tvMPlanRateValueAdded.setText();
		tvDPlanRateValueAdded.setText();

		tvMPlanRate.setText();
		tvDPlanRate.setText();
		*/

		return convertView;
	}

}
