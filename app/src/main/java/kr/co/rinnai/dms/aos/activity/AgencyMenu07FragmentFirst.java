package kr.co.rinnai.dms.aos.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.fragment.app.Fragment;
import kr.co.rinnai.dms.R;
import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteInfo;
import kr.co.rinnai.dms.common.custom.RinnaiAddressSearchDialog;
import kr.co.rinnai.dms.common.custom.RinnaiCalendarDialog;
import kr.co.rinnai.dms.common.custom.RinnaiMonthCalendarDialog;
import kr.co.rinnai.dms.common.http.model.AddressJusoData;
import kr.co.rinnai.dms.common.http.model.SensorData;
import kr.co.rinnai.dms.common.listener.AddressSelectedListener;
import kr.co.rinnai.dms.common.listener.CalendarListener;
import kr.co.rinnai.dms.common.util.NumberTextWatcher;
import kr.co.rinnai.dms.eos.adapter.ItemSpinnerAdapter;
import kr.co.rinnai.dms.eos.model.ItemCode;

public class AgencyMenu07FragmentFirst extends Fragment implements  View.OnClickListener, View.OnTouchListener {
	private View convertView = null;

	boolean isPressed = false;

	private TextView tvCustName;
	private EditText etSiteName;
	private EditText etHouseCount;
	private EditText etSaleCust;
	private TextView tvSaleYm;
	private EditText edSpecialNote;
	private RelativeLayout btnCalendar;
	private RelativeLayout btnAddress;
	private AddressJusoData jusoData;


	private TextView tvPostNo;
	private TextView tvAddr1;
	private TextView etAddr2;


	private RinnaiMonthCalendarDialog rinnaiReceivedProductDialog;

	private RinnaiAddressSearchDialog rinnaiAddressSearchDialog;

	private Spinner spItemType;
	private ItemSpinnerAdapter spinnerAdapter;

	private Spinner spHeatingType;
	private ItemSpinnerAdapter heatingAdapter;

	public static Fragment newInstance(Context context, int position, ArrayList<SensorData> sensorDataList) {
		Bundle b = new Bundle();
		b.putInt("pos", position);

		return Fragment.instantiate(context, AgencyMenu07FragmentFirst.class.getName(), b);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}

		convertView = inflater.inflate(R.layout.activity_agency_menu_07_fragment_one, container, false);

		tvCustName = (TextView) convertView.findViewById(R.id.tv_agency_07_activity_cust_name);
		etSiteName = (EditText) convertView.findViewById(R.id.et_agency_07_activity_site_name);
		etHouseCount = (EditText) convertView.findViewById(R.id.et_agency_07_activity_total_house_hold_count);
		etSaleCust = (EditText) convertView.findViewById(R.id.et_agency_07_activity_sale_cust);
		tvSaleYm = (TextView) convertView.findViewById(R.id.tv_agency_07_activity_sale_ym);
		edSpecialNote = (EditText) convertView.findViewById(R.id.et_agency_07_activity_special_note);
		btnCalendar = (RelativeLayout) convertView.findViewById(R.id.btn_agency_menu_07_activity_calendar);
		btnAddress = (RelativeLayout) convertView.findViewById(R.id.btn_agency_menu_07_activity_address);
		tvPostNo = (TextView) convertView.findViewById(R.id.tv_post_no);
		tvAddr1 = (TextView) convertView.findViewById(R.id.tv_addr1);
		etAddr2 = (EditText) convertView.findViewById(R.id.et_addr2);

		spItemType = (Spinner) convertView.findViewById(R.id.sp_agency_07_activity_building_type);
		spHeatingType = (Spinner) convertView.findViewById(R.id.sp_agency_07_activity_heating_type);

//		menu01 = convertView.findViewById(R.id.menu_01);

		btnCalendar.setOnClickListener(AgencyMenu07FragmentFirst.this);
		btnAddress.setOnClickListener(AgencyMenu07FragmentFirst.this);
		etHouseCount.addTextChangedListener(new NumberTextWatcher(etHouseCount));

		Calendar calendar = Calendar.getInstance();

		String toDate = String.format("%4d년%02d월", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1);
		tvSaleYm.setText(toDate);

		return convertView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == btnCalendar.getId()) {
			if(rinnaiReceivedProductDialog == null) {
				String date = tvSaleYm.getText().toString();
				rinnaiReceivedProductDialog = new RinnaiMonthCalendarDialog(getActivity(), 1, date);
			}
			if(!rinnaiReceivedProductDialog.isShowing()) {
				rinnaiReceivedProductDialog.setCanceledOnTouchOutside(false);
				rinnaiReceivedProductDialog.setCancelable(false);

				rinnaiReceivedProductDialog.setDialogListener(new CalendarListener() {
					@Override
					public void onDateChange(String date) {
						tvSaleYm.setText(date);


					}  // MyDialogListener 를 구현

					@Override
					public void onCalendarView() {

					}

				});
				String date = tvSaleYm.getText().toString();

				rinnaiReceivedProductDialog.show();
				rinnaiReceivedProductDialog.setDate(date);
			}
		} else if(id == btnAddress.getId()) {
			if(rinnaiAddressSearchDialog == null) {
				rinnaiAddressSearchDialog = new RinnaiAddressSearchDialog(getActivity(), 1);
			}

			if(!rinnaiAddressSearchDialog.isShowing()) {
				rinnaiAddressSearchDialog.setCanceledOnTouchOutside(false);
				rinnaiAddressSearchDialog.setCancelable(false);

				rinnaiAddressSearchDialog.setDialogListener(new AddressSelectedListener() {
					@Override
					public void onSelected(AddressJusoData juso) {

						jusoData = juso;
//						tvPostNo.setText(jusoData.getZipNo());
//						tvAddr1.setText(jusoData.getRoadAddrPart1());
						tvPostNo.setText(jusoData.getZipNo());
						tvAddr1.setText(jusoData.getRoadAddrPart1());

						etAddr2.postDelayed(new Runnable() {
							@Override
							public void run() {
								etAddr2.setFocusableInTouchMode(true);
								etAddr2.requestFocus();
								//etAddr2.setShowSoftInputOnFocus(true);
								String addr2 = etAddr2.getText().toString();

								InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

								imm.showSoftInput(etAddr2, 0);

								etAddr2.setText("");


							}
						}, 500);

					}
				});

				rinnaiAddressSearchDialog.show();
			}
		}

	}


	public boolean onTouch(View v, MotionEvent event) {
		int id = v.getId();



		return false;
	}

	protected AddressJusoData getJusoData() {
		return jusoData;
	}

	protected String getSiteName() {
		return etSiteName.getText().toString();
	}
	protected String getTotalHouseCount() {
		String strHouseCount = etHouseCount.getText().toString();
		String restr = strHouseCount.replaceAll("[^0-9]","");
//		int iHouseCount = Integer.parseInt(strHouseCount);
		return restr;

	}

	protected String getSaleCust() {
		return etSaleCust.getText().toString();
	}

	protected String getSaleYm() {
		String saleYm = tvSaleYm.getText().toString();
		String restr = saleYm.replaceAll("[^0-9]","");
		return restr;
	}

	protected String getSpecialNote() {
		return edSpecialNote.getText().toString();
	}



	protected String getAddrDet() {
		return etAddr2.getText().toString();
	}
	protected String getBuildingTypecode() {
		ItemCode itemCode = (ItemCode)spItemType.getSelectedItem();
		return itemCode.getCodeItem();
	}

	protected String getHeatingTypecode() {
		ItemCode itemCode = (ItemCode)spHeatingType.getSelectedItem();
		return itemCode.getCodeItem();
	}

	protected void setBuildingType(List<ItemCode> codeList) {
		spinnerAdapter = new ItemSpinnerAdapter(getActivity(), codeList);
		spItemType.setAdapter(spinnerAdapter);

		spinnerAdapter.notifyDataSetChanged();
//		spItemType.setOnItemSelectedListener(EmployeeMenu06Activity.this);
	}

	protected void setHeagtingType(List<ItemCode> codeList) {
		heatingAdapter = new ItemSpinnerAdapter(getActivity(), codeList);
		spHeatingType.setAdapter(heatingAdapter);

		heatingAdapter.notifyDataSetChanged();
//		spItemType.setOnItemSelectedListener(EmployeeMenu06Activity.this);
	}

	protected void setCustName(String custName) {
		tvCustName.setText(custName);
	}

	protected void setSiteInfo(AgencyMenu07SiteInfo siteInfo) {
		etSiteName.setText(siteInfo.getSiteName());
		etHouseCount.setText(String.format("%,d", siteInfo.getTotHouseholdCnt()));
		etSaleCust.setText(siteInfo.getSaleCust());

		String saleYm = siteInfo.getSaleYm();

		if(saleYm == null) {
			saleYm = "-";
		} else if(saleYm.length() == 5) {
			saleYm = String.format("%s년%02d월", saleYm.substring(0, 4), Integer.parseInt(saleYm.substring(4, 5)));
		} else if(saleYm.length() == 6) {
			saleYm = String.format("%s년%02d월", saleYm.substring(0, 4), Integer.parseInt(saleYm.substring(4, 6)));
		}


		tvSaleYm.setText(saleYm);
		edSpecialNote.setText(siteInfo.getSpecialNote());
		btnCalendar = (RelativeLayout) convertView.findViewById(R.id.btn_agency_menu_07_activity_calendar);
		btnAddress = (RelativeLayout) convertView.findViewById(R.id.btn_agency_menu_07_activity_address);
		tvPostNo.setText(siteInfo.getSiteZipcode());
		tvAddr1.setText(siteInfo.getSiteRoadaddrpart1());
		etAddr2.setText(siteInfo.getSiteDetaddr());

		int position = spinnerAdapter.getItemPosition(siteInfo.getBuildingTypecode());
		spItemType.setSelection(position);

		int heatPosition = heatingAdapter.getItemPosition(siteInfo.getHeatTypecode());
		spHeatingType.setSelection(heatPosition);
		if(jusoData == null) {
			jusoData = new AddressJusoData();
		}

		jusoData.setZipNo(siteInfo.getSiteZipcode());
		jusoData.setRoadAddrPart1(siteInfo.getSiteRoadaddrpart1());
		jusoData.setRoadAddrPart2(siteInfo.getSiteRoadaddrpart2());
		jusoData.setJibunAddr(siteInfo.getSiteDetaddr());
		jusoData.setAdmCd(siteInfo.getSiteAdmcd());
		jusoData.setRnMgtSn(siteInfo.getSiteRoadcode());
		jusoData.setBdMgtSn(siteInfo.getSiteBuildingcode());
		jusoData.setDetBdNmList(siteInfo.getSiteBuildingdetname());
//		jusoData.setDetBdNmList(siteInfo.getSiteBuildingdetname());
		jusoData.setBdNm(siteInfo.getSiteBuildingname());
		jusoData.setBdKdcd(siteInfo.getSiteApthouseyn());
		jusoData.setSiNm(siteInfo.getSiteSido());
		jusoData.setSggNm(siteInfo.getSiteGugun());
		jusoData.setEmdNm(siteInfo.getSiteDong());
		jusoData.setLiNm(siteInfo.getSiteBname());
		jusoData.setLiNm(siteInfo.getSiteBname1());
		jusoData.setRn(siteInfo.getSiteRn());
		jusoData.setBuldMnnm(siteInfo.getSiteBuldmnnm());
		jusoData.setBuldSlno(siteInfo.getSiteBuldslno());
		jusoData.setLnbrMnnm(siteInfo.getSiteInbrmnnm());
		jusoData.setEmdNo(siteInfo.getSiteEmdno());



	}


}
