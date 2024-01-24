package kr.co.rinnai.dms.aos.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.common.custom.CustomMenuButtonView;
import kr.co.rinnai.dms.common.http.model.SensorData;
import kr.co.rinnai.dms.eos.activity.EmployeeActivity;

//import android.support.v4.app.Fragment;

public class AgencyFragmentSecond extends Fragment implements View.OnClickListener, View.OnTouchListener {
	private View convertView = null;

	CustomMenuButtonView menu08;


	boolean isPressed = false;



	public static Fragment newInstance(Context context, int position, ArrayList<SensorData> sensorDataList) {
		Bundle b = new Bundle();
		b.putInt("pos", position);

		return Fragment.instantiate(context, AgencyFragmentSecond.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.act_agency_second_page, container, false);

		menu08 = convertView.findViewById(R.id.menu_08);


		menu08.setOnTouchListener(this);
//		menu01.setOnClickListener(this);


		return convertView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == menu08.getId()) {


		}
	}



	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();

		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(id == menu08.getId()) {
					menu08.buttonClick(true);
				}
				Log.d("test", "Action_DOWN " + id);
				break;

			case MotionEvent.ACTION_OUTSIDE:
				Log.d("test", "ACTION_OUTSIDE"  + id);
			case MotionEvent.ACTION_UP:

				Log.d("test", "Action_UP"  + id);
				Intent intent = null;
				if(id == menu08.getId()) {
					menu08.buttonClick(false);
					if(isPressed) {
						intent = new Intent(getActivity(), AgencyMenu07ActivityList.class);
						((AgencyActivity)getActivity()).setLog("AgencyMenu07ActivityList");
					}
				}
				if(intent != null) {
					startActivity(intent);
				}
				break;

		}

		return false;
	}

	public void clearView() {
		menu08.buttonClick(false);

	}
}
