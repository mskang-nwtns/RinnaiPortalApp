package kr.co.rinnai.dms.udd.activity;

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
import kr.co.rinnai.dms.wms.activity.WmsMenu08Activity;

//import android.support.v4.app.Fragment;

public class UseDeliveryDriverFragmentFirst extends Fragment implements View.OnClickListener, View.OnTouchListener {
	private View convertView = null;

	CustomMenuButtonView menu01;
	CustomMenuButtonView menu02;

	CustomMenuButtonView menu03;
	CustomMenuButtonView menu04;

	CustomMenuButtonView menu05;
	CustomMenuButtonView menu07;

	boolean isPressed = false;



	public static Fragment newInstance(Context context, int position, ArrayList<SensorData> sensorDataList) {
		Bundle b = new Bundle();
		b.putInt("pos", position);

		return Fragment.instantiate(context, UseDeliveryDriverFragmentFirst.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.act_udd_main_fragment_first_page, container, false);

		menu01 = convertView.findViewById(R.id.menu_01);
		menu02 = convertView.findViewById(R.id.menu_02);

//		menu03 = convertView.findViewById(R.id.menu_03);
//		menu04 = convertView.findViewById(R.id.menu_04);

//		menu05 = convertView.findViewById(R.id.menu_05);
//		menu07 = convertView.findViewById(R.id.menu_07);

		menu01.setOnTouchListener(this);
//		menu01.setOnClickListener(this);

		menu02.setOnTouchListener(this);
//		menu02.setOnClickListener(this);

//		menu03.setOnTouchListener(this);
//		menu03.setOnClickListener(this);

//		menu04.setOnTouchListener(this);
//		menu04.setOnClickListener(this);

//		menu05.setOnTouchListener(this);
//		menu05.setOnClickListener(this);

//		menu07.setOnTouchListener(this);
//		menu06.setOnClickListener(this);



		return convertView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == menu01.getId()) {


		}
	}



	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();

		isPressed = v.isPressed();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(id == menu01.getId()) {
					menu01.buttonClick(true);
				} else if(id == menu02.getId()) {
					menu02.buttonClick(true);
				}
				Log.d("test", "Action_DOWN " + id);
				break;

			case MotionEvent.ACTION_OUTSIDE:
				Log.d("test", "ACTION_OUTSIDE"  + id);
			case MotionEvent.ACTION_UP:

				Log.d("test", "Action_UP"  + id);
				Intent intent = null;
				if(id == menu01.getId()) {
					menu01.buttonClick(false);
					if(isPressed) {
						intent = new Intent(getActivity(), WmsMenu08Activity.class);
					}
				} else if(id == menu02.getId()) {

					menu02.buttonClick(false);
					if(isPressed) {
						intent = new Intent(getActivity(), UseDeliveryDriver02Activity.class);
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
		menu01.buttonClick(false);
		menu02.buttonClick(false);

	}
}
