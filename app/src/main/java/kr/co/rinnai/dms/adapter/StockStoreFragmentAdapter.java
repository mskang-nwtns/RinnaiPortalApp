package kr.co.rinnai.dms.adapter;

import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//import android.support.v4.view.ViewPager.OnPageChangeListener;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import kr.co.rinnai.dms.eos.activity.EmployeeMenu03Fragment;
import kr.co.rinnai.dms.common.listener.PageListener;

public class StockStoreFragmentAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
	private Context context = null;
	private FragmentManager fm = null;
	private List<Object> list = null;
	private PageListener callback = null;
	private Fragment stockStoreFragment = null;
	private String type = null;

	public StockStoreFragmentAdapter(Context context, FragmentManager fm, List<Object> objList, PageListener callback, String type) {
		super(fm);
		this.list = objList;
		this.fm = fm;
		this.context = context;
		this.callback = callback;
		this.type = type;
		stockStoreFragment = new Fragment();
	}


	@Override
	public Fragment getItem(int position) {
		// make the first pager bigger than others
		stockStoreFragment = EmployeeMenu03Fragment.newInstance(context, position, list, type);

		return stockStoreFragment;
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
}
