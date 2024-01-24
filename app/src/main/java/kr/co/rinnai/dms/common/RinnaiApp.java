package kr.co.rinnai.dms.common;

import java.util.Queue;

import android.app.Application;

public class RinnaiApp extends Application {

	private static RinnaiApp instance = null;
	private String accessToken;
	private String FCMToken;
	private String placeCode;
	private Queue<String> msgQueue;
	private String gwId;
	private String userNo;

	public static synchronized RinnaiApp getInstance() {
        if(null == instance)
        {
        	instance = new RinnaiApp();
        }
        return instance;
    }
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getPlaceCode() {
		return placeCode;
	}

	public void setPlaceCode(String placeCode) {
		this.placeCode = placeCode;
	}
	
	public Queue<String> getMsgQueue() {
		return msgQueue;
	}

	public void setMsgQueue(Queue<String> msgQueue) {
		this.msgQueue = msgQueue;
	}




//	public void setBoilerData(BoilerData boilerData, String boilerType) {
//		
//		if (CommonValue.RINNAI_HEATER == boilerType) {
//			dataList = getHeaterList();
//		}
//		else {
//			dataList = getHotWaterList();
//		}
//		
//	}
	

	public String getFCMToken() {
		return FCMToken;
	}

	public void setFCMToken(String fCMToken) {
		FCMToken = fCMToken;
	}

	public String getGwId() {
		return gwId;
	}

	public void setGwId(String gwId) {
		this.gwId = gwId;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
}
