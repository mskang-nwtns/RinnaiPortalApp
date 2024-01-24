package kr.co.rinnai.dms.common.http.model;

import java.util.ArrayList;

public class SensorDataList {
	ArrayList<SensorData> data = new ArrayList<SensorData>();

	public ArrayList<SensorData> getData() {
		return data;
	}

	public void setData(ArrayList<SensorData> data) {
		this.data = data;
	}
	
}	
