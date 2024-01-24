package kr.co.rinnai.dms.wms.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
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

public class WmsFragmentSecond extends Fragment implements View.OnTouchListener{
	private View convertView = null;

	CustomMenuButtonView menu08, menu09, menu10;
	CustomMenuButtonView menu11, menu12, menu13;
	boolean isPressed = false;


	public static Fragment newInstance(Context context, int position, ArrayList<SensorData> sensorDataList) {
		Bundle b = new Bundle();
		b.putInt("pos", position);

		return Fragment.instantiate(context, WmsFragmentSecond.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.act_wms_main_fragment_second_page, container, false);

		menu08 = convertView.findViewById(R.id.menu_08);
		menu09 = convertView.findViewById(R.id.menu_09);
		menu10 = convertView.findViewById(R.id.menu_10);

		menu12 = convertView.findViewById(R.id.menu_12);
		menu13 = convertView.findViewById(R.id.menu_13);

		menu08.setOnTouchListener(this);
		menu09.setOnTouchListener(this);
		menu10.setOnTouchListener(this);

		menu12.setOnTouchListener(this);
		menu13.setOnTouchListener(this);

		return convertView;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();
		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(id == menu08.getId()) {
					menu08.buttonClick(true);
				} else if(id == menu09.getId()) {
					menu09.buttonClick(true);
				} else if(id == menu10.getId()) {
					menu10.buttonClick(true);
				}  else if(id == menu12.getId()) {
					menu12.buttonClick(true);
				}  else if(id == menu13.getId()) {
					menu13.buttonClick(true);
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
						intent = new Intent(getActivity(), WmsMenu08Activity.class);
					}

				} else if (id == menu09.getId()) {
					menu09.buttonClick(false);
					if(isPressed) {
						intent = new Intent(getActivity(), WmsMenu09Activity.class);
					}
				} else if (id == menu10.getId()) {
					menu10.buttonClick(false);
					if(isPressed) {
						intent = new Intent(getActivity(), WmsMenu10Activity.class);
					}
				} else if (id == menu12.getId()) {
					menu12.buttonClick(false);
					if(isPressed) {
						intent = new Intent(getActivity(), WmsMenu03LocationActivity.class);
					}
				} else if (id == menu13.getId()) {
					menu13.buttonClick(false);
					if(isPressed) {
						intent = new Intent(getActivity(), WmsMenu03AgencyActivity.class);
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
		menu09.buttonClick(false);
	}
}
