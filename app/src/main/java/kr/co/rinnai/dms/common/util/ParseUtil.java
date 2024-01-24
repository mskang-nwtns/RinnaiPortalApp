package kr.co.rinnai.dms.common.util;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.StringWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import kr.co.rinnai.dms.common.CommonValue;
import kr.co.rinnai.dms.wms.model.OrderReportResult;
import kr.co.rinnai.dms.wms.model.WmsMenu03AgencyOrderReport;

import org.json.JSONException;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ParseUtil {

//	private static ObjectMapper mapper;
	private static Gson gson;
	private static ObjectMapper mapper;

	static {
		mapper = new ObjectMapper();
//		 1.9.x 버전 이상
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	public static JSONObject getCompletData(OrderReportResult result, String userName, int idx)  {
		JSONObject jsonObject = new JSONObject();
		
		try {
			
			String[] tmp = result.getOrderNo().split("-");
			String carId = tmp[0];
			String trsNo = tmp[1];
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_INDEX, idx);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_COM_ID, CommonValue.WMS_I_COM_ID);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_WAREHOUSE, CommonValue.WMS_INCHON_WAREHOUSE);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_SALE_DATE, result.getOrderDate().replaceAll("/", "-"));
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_CAR_ITEM, carId);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_TRS_NO, trsNo);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_IO_ITEM_SEQ, result.getJobName());
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_PDA_JOB_QTY, result.getOrderSeq());
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_PDA_JOB_NO, userName);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_MODEL_CODE, result.getModelCode());
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_ORDER_DETAIL, result.getOrderDetail());
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_CELL_NO, result.getOrderLocation().replaceAll("-",""));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jsonObject;
	}
	
	/**
	 * 서버에서 조회된 변경을 App용 리스트로 변경  group by location, modelName, gasType</br>
	 * 수동찾기의 경우 group by 하지않음. 
	 * @param reportList
	 * @return
	 */
	public static ArrayList<OrderReportResult> parseOrderReport(List<OrderReportResult> reportList) {
		ArrayList<OrderReportResult> returnList = null;
		
		String preLocation = null;
		String preModelName = null;
		String preGasType = null;
		int preOrderCount = 0;
		
		for(int i = 0; i < reportList.size(); i ++) {
			if(returnList == null) {
				returnList = new ArrayList<OrderReportResult>();
			}
			OrderReportResult orderReport = reportList.get(i);
			
			if( preLocation == null && preModelName == null && preGasType == null && preOrderCount == 0) {
				preLocation = orderReport.getOrderLocation();
				preModelName = orderReport.getModelName();
				preGasType = orderReport.getGasType();
			}
			
			if("수동찾기".equals(preLocation)) {
				preLocation = orderReport.getOrderLocation();
				preModelName = orderReport.getModelName();
				preGasType = orderReport.getGasType();
				preOrderCount += orderReport.getOrderSeq();

				OrderReportResult temp = new OrderReportResult();
				temp.setOrderLocation(preLocation);
				temp.setModelName( preModelName);
				temp.setGasType(preGasType);
				temp.setOrderSeq(preOrderCount);
				temp.setOrderNo(orderReport.getOrderNo());
				temp.setOrderDetail(orderReport.getOrderDetail());
				returnList.add(temp);
				
				preLocation = null;
				preModelName = null;
				preGasType = null;
				preOrderCount = 0;
				
			} else {



				if( preLocation.equals(orderReport.getOrderLocation()) && preModelName.equals(orderReport.getModelName()) && preGasType.equals(orderReport.getGasType()) ) {
					
					preLocation = orderReport.getOrderLocation();
					preModelName = orderReport.getModelName();
					preGasType = orderReport.getGasType();
					preOrderCount += orderReport.getOrderSeq();
				} else {
					OrderReportResult temp = new OrderReportResult();
					temp.setOrderLocation(preLocation);
					temp.setModelName( preModelName);
					temp.setGasType(preGasType);
					temp.setOrderSeq(preOrderCount);
					returnList.add(temp);

					preLocation = null;
					preModelName = null;
					preGasType = null;
					preOrderCount = 0;

					i--;
					
				}
			}
			
			if(i == reportList.size() -1) {
				if( preLocation == null && preModelName == null && preGasType == null && preOrderCount == 0) {
					
				} else {
					OrderReportResult temp = new OrderReportResult();
					temp.setOrderLocation(preLocation);
					temp.setModelName( preModelName);
					temp.setGasType(preGasType);
					temp.setOrderSeq(preOrderCount);
					returnList.add(temp);
				}
			}
			
		}
		return returnList;
	}

	public static String getJSONFromObject(Object obj) {
		try {
			StringWriter sw = new StringWriter(); // serialize
			mapper.writeValue(sw, obj);
			sw.close();

			return sw.getBuffer().toString();
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}


	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public static float parseFloat(String s) {
		if(s != null) {
			return Float.parseFloat(s);
		} else {
			return (float)0.0;
		}
	}

	public static boolean isCOAlarm(String productBarcode) {
		boolean isCOAlarm = false;

		String procuctType = productBarcode.substring(0, 5);

		switch(procuctType) {
			case CommonValue.PROCUCT_TYPE_SI_600:
				isCOAlarm = true;
				break;
			case CommonValue.PROCUCT_TYPE_SI_610:
				isCOAlarm = true;
				break;
			case CommonValue.PROCUCT_TYPE_SA_100A:
				isCOAlarm = true;
				break;
			case CommonValue.PROCUCT_TYPE_SA_100B:
				isCOAlarm = true;
				break;
		}
		return isCOAlarm;

	}


	/**
	 * 바이너리 스트링을 바이트배열로 변환
	 *
	 * @param s
	 * @return
	 */
	public static byte[] binaryStringToByteArray(String s) {
		int count = s.length() / 8;
		byte[] b = new byte[count];
		for (int i = 1; i < count; ++i) {
			String t = s.substring((i - 1) * 8, i * 8);
			b[i - 1] = binaryStringToByte(t);
		}
		return b;
	}

	/**
	 * 바이너리 스트링을 바이트로 변환
	 *
	 * @param s
	 * @return
	 */
	public static byte binaryStringToByte(String s) {
		byte ret = 0, total = 0;
		for (int i = 0; i < 8; ++i) {
			ret = (s.charAt(7 - i) == '1') ? (byte) (1 << i) : 0;
			total = (byte) (ret | total);
		}
		return total;
	}

	public static boolean CheckNumber(String str){
		char check;

		if(str.equals(""))
		{
			//문자열이 공백인지 확인
			return false;
		}

		for(int i = 0; i<str.length(); i++){
			check = str.charAt(i);
			if( check < 48 || check > 58)
			{
				//해당 char값이 숫자가 아닐 경우
				return false;
			}

		}
		return true;
	}


	/**
	 *
	 * @param result result
	 * @return
	 */
	public static JSONObject getCompletData(WmsMenu03AgencyOrderReport result, String userName, int idx)  {
		JSONObject jsonObject = new JSONObject();
/*
		try {
//			result.get
			String[] tmp = result.getOrderNo().split("-");
			String carId = tmp[0];
			String trsNo = tmp[1];
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_INDEX, idx);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_COM_ID, CommonValue.WMS_I_COM_ID);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_WAREHOUSE, CommonValue.WMS_INCHON_WAREHOUSE);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_SALE_DATE, result.getOrderDate().replaceAll("/", "-"));
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_CAR_ITEM, carId);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_TRS_NO, trsNo);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_IO_ITEM_SEQ, result.getJobName());
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_PDA_JOB_QTY, result.getOrderSeq());
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_PDA_JOB_NO, userName);
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_MODEL_CODE, result.getModelCode());
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_ORDER_DETAIL, result.getOrderDetail());
			jsonObject.put(CommonValue.WMS_PARAMETER_KEY_I_CELL_NO, result.getOrderLocation().replaceAll("-",""));

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
		return jsonObject;
	}
	
}
