package kr.co.rinnai.dms.eos.activity;

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

//import android.support.v4.app.Fragment;

public class EmployeeFragmentSecond extends Fragment implements View.OnClickListener, View.OnTouchListener {
	private View convertView = null;

	CustomMenuButtonView menu04;

	boolean isPressed = false;



	public static Fragment newInstance(Context context, int position, ArrayList<SensorData> sensorDataList) {
		Bundle b = new Bundle();
		b.putInt("pos", position);

		return Fragment.instantiate(context, EmployeeFragmentSecond.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.act_employee_second_page, container, false);

		menu04 = convertView.findViewById(R.id.menu_04);

		menu04.setOnTouchListener(this);
//		menu06.setOnClickListener(this);



		return convertView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == menu04.getId()) {


		}
	}



	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();

		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(id == menu04.getId()) {
					menu04.buttonClick(true);
				}
				Log.d("test", "Action_DOWN " + id);
				break;

			case MotionEvent.ACTION_OUTSIDE:
				Log.d("test", "ACTION_OUTSIDE"  + id);
			case MotionEvent.ACTION_UP:

				Log.d("test", "Action_UP"  + id);
				Intent intent = null;
				if(id == menu04.getId()) {
					menu04.buttonClick(false);
					if(isPressed) {
						intent = new Intent(getActivity(), EmployeeMenu04Activity.class);
						((EmployeeActivity)getActivity()).setLog("EmployeeMenu04Activity");
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

		menu04.buttonClick(false);
	}
}
