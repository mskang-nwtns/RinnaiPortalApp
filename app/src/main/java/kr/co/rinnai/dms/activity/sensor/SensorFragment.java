package kr.co.rinnai.dms.activity.sensor;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.R.color;
import kr.co.rinnai.dms.common.http.model.SensorData;

public class SensorFragment extends Fragment {
	private View convertView = null;
	private TextView title = null;
	private TextView avgLou1 = null;
	private TextView nowLou1 = null;
	
	private TextView nowWoi1 = null;
	private TextView nowWoi2 = null;
	
	private TextView avgTemp1 = null;
	private TextView avgTemp2 = null;
	private TextView nowTemp1 = null;
	private TextView nowTemp2 = null;
	
	private TextView avgHum1 = null;
	private TextView avgHum2 = null;
	private TextView nowHum1 = null;
	private TextView nowHum2 = null;
	
	private static ArrayList<SensorData> list = null;
	

	public static Fragment newInstance(Context context, int position, ArrayList<SensorData> sensorDataList) {
		Bundle b = new Bundle();
		b.putInt("pos", position);
		list = sensorDataList;
		return Fragment.instantiate(context, SensorFragment.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.act_status, container, false);
		
		title = (TextView) convertView.findViewById(R.id.title);
		avgLou1 = (TextView) convertView.findViewById(R.id.avgLou1);
		nowLou1 = (TextView) convertView.findViewById(R.id.nowLou1);
		
		nowWoi1 = (TextView) convertView.findViewById(R.id.nowWoi1);
		nowWoi2 = (TextView) convertView.findViewById(R.id.nowWoi2);
		
		avgTemp1 = (TextView) convertView.findViewById(R.id.avgTemp1);
		avgTemp2 = (TextView) convertView.findViewById(R.id.avgTemp2);
		nowTemp1 = (TextView) convertView.findViewById(R.id.nowTemp1);
		nowTemp2 = (TextView) convertView.findViewById(R.id.nowTemp2);
		
		avgHum1 = (TextView) convertView.findViewById(R.id.avgHum1);
		avgHum2 = (TextView) convertView.findViewById(R.id.avgHum2);
		nowHum1 = (TextView) convertView.findViewById(R.id.nowHum1);;
		nowHum2 = (TextView) convertView.findViewById(R.id.nowHum2);
		
		
		nowLou1.setTextColor(getResources().getColor(color.black));
		avgLou1.setTextColor(getResources().getColor(color.black));
		avgTemp1.setTextColor(getResources().getColor(color.black));
		avgTemp2.setTextColor(getResources().getColor(color.black));
		nowTemp1.setTextColor(getResources().getColor(color.black));
		nowTemp2.setTextColor(getResources().getColor(color.black));
		
		avgHum1.setTextColor(getResources().getColor(color.black));
		avgHum2.setTextColor(getResources().getColor(color.black));
		nowHum1.setTextColor(getResources().getColor(color.black));
		nowHum2.setTextColor(getResources().getColor(color.black));
		
		int pos = this.getArguments().getInt("pos");
//		
//		chart.setData(gasComsumptionList.get(pos));
		
		SensorData data = list.get(pos); 
		
		title.setText(data.getRoomType() + "(" +  data.getConnected()+ ")");
		avgLou1.setText(data.getAvgLoudness1() + "");
		nowLou1.setText(data.getLoudness1() + ""); 
		
		String moisture1 = "미감지";
		String moisture2 = "미감지";
		
		nowWoi1.setTextColor(getResources().getColor(color.black));
		nowWoi2.setTextColor(getResources().getColor(color.black));
		
		if(Integer.parseInt(data.getMoisture1()) > 50 )
		{
			moisture1 = "감지";
			nowWoi1.setTextColor(getResources().getColor(color.red));
		}
		if(Integer.parseInt(data.getMoisture2()) > 50 )
		{
			moisture2 = "감지";
			nowWoi2.setTextColor(getResources().getColor(color.red));
		}
		nowWoi1.setText(moisture1);
		nowWoi2.setText(moisture2);
		        
		avgTemp1.setText(data.getAvgTemperature1());
		avgTemp2.setText(data.getAvgTemperature2());
		nowTemp1.setText(data.getTemperature1());
		nowTemp2.setText(data.getTemperature2());
		
		avgHum1.setText(data.getAvgHumidity1()); 
		avgHum2.setText(data.getAvgHumidity2()); 
		nowHum1.setText(data.getHumidity1()); 
		nowHum2.setText(data.getHumidity2()); 
		return convertView;
	}
}
