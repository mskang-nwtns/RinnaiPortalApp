package kr.co.rinnai.dms.adapter;

import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager.OnPageChangeListener;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu01Fragment;
import kr.co.rinnai.dms.eos.model.SalesProgress;
import kr.co.rinnai.dms.common.listener.PageListener;
import kr.co.rinnai.dms.eos.model.SalesProgressV3;

public class SalesProgressAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
	private Context context = null;
	private FragmentManager fm = null;
	private ArrayList<SalesProgressV3> list = null;
	private PageListener callback = null;
	private Fragment salesProgressFragment = null;
	private String type = null;

	public SalesProgressAdapter(Context context, FragmentManager fm, ArrayList<SalesProgressV3> reserve, PageListener callback, String type) {
		super(fm);
		this.list = reserve;
		this.fm = fm;
		this.context = context;
		this.callback = callback;
		this.type = type;
		salesProgressFragment = new Fragment();
	}

	public void setGasComsumptionList(ArrayList<SalesProgressV3> reserve)
	{
		this.list = reserve;
	}

	@Override
	public Fragment getItem(int position) {
		// make the first pager bigger than others
		salesProgressFragment = EmployeeMenu01Fragment.newInstance(context, position, list);

		return salesProgressFragment;
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
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		callback.onPageSelected(position, type);
	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

	public String[] getTitle(int position) {
		String[] title = new String[5];
		int count = 0;
		for(int i = 0; i < list.size(); i ++) {

			if(count == 5) {
				break;
			}
			if ( (position -  2) + i < 0) {
				title[i] = "";
				count++;
			} else if( (position - 2)  + i > -1 &&  (position - 2)  + i <list.size()) {
				title[i] = list.get((position + i) - 2).getDeptTypeName();
				count++;
			} else {
				title[i] = "";
				count++;
			}


		}

		return title;
	}
}
