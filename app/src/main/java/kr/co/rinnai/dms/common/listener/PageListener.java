package kr.co.rinnai.dms.common.listener;

//import android.support.v4.app.Fragment;

import androidx.fragment.app.Fragment;

public interface PageListener {
	public void onPageChange(Fragment fragment, int position, float positionOffset, int positionOffsetPixels);

	public void onPageSelected(Fragment fragment, int position);
	
	public void onPageSelected(Fragment fragment, int position, Object selecetdItem);

	public void onPageSelected(int position, String type);
}
