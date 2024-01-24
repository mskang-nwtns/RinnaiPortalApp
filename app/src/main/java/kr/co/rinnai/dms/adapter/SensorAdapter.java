package kr.co.rinnai.dms.adapter;

import java.util.ArrayList;

import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager.OnPageChangeListener;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.common.listener.PageListener;
import kr.co.rinnai.dms.common.http.model.SensorData;
import kr.co.rinnai.dms.activity.sensor.SensorFragment;

public class SensorAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
	private Context context = null;
	private FragmentManager fm = null;
	private ArrayList<SensorData> list = null;
	private PageListener callback = null;
	private Fragment gasComsumptionFragment = null;

	public SensorAdapter(Context context, FragmentManager fm, ArrayList<SensorData> reserve, PageListener callback) {
		super(fm);
		this.list = reserve;
		this.fm = fm;
		this.context = context;
		this.callback = callback;
		gasComsumptionFragment = new Fragment();
	}
	
	public void setGasComsumptionList(ArrayList<SensorData> reserve)
	{
		this.list = reserve;
	}
	
	@Override
	public Fragment getItem(int position) {
		// make the first pager bigger than others
		gasComsumptionFragment = SensorFragment.newInstance(context, position, list);
		
		return gasComsumptionFragment;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public int getItemPosition(Object object) {
		
		return POSITION_NONE;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int arg0) {
		
		callback.onPageSelected(gasComsumptionFragment, arg0, null);
	}
}
