package kr.co.rinnai.dms.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonParserManager {
	
	/**
	 * JSON String에서 객체 형식으로 변환해준다.
	 * 
	 * @param classType
	 *            변환을 원하는 객체 타입
	 * @param jsonStr
	 *            변환할 JSON 문자열
	 * @return 변환된 객체
	 */
	public static final <T> T jsonToObject(Class<T> classType, String jsonStr) {
		
		try {
			T jsonToObject = new Gson().fromJson(jsonStr, classType);
			return jsonToObject;
		}
		catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
	}


	
	public static final <T> T jsonToObject(Class<T> classType, String jsonStr, String elementKey) {
		
		try {		
			JsonParser parser = new JsonParser();
			JsonElement rootObejct = parser.parse(jsonStr).getAsJsonObject().get(elementKey);
			T jsonToObject = new Gson().fromJson(rootObejct, classType);
			return jsonToObject;
		}
		catch (JsonSyntaxException e) {
			return null;
		}
	}
	
	/**
	 * 객체의 내용을 JSON String으로 만들어 반환해준다.
	 * 
	 * @param calssType
	 *            변경을 원하는 객체 타입
	 * @param obj
	 *            변환한 객체
	 * @return 변환된 JSON 문자열
	 */
	public static final <T> String objectToJson(Class<T> calssType, T obj) {

		String jsonStr = new Gson().toJson(obj, calssType);
		return jsonStr;
	}
	
	public static final <T> String objectToJson(Class<T> calssType, String addKey, T obj) {
		
		Gson gson = new GsonBuilder().create();
		JsonElement jsonElement = gson.toJsonTree(obj); 
		JsonObject jsonObject = jsonElement.getAsJsonObject();
			
		JsonObject jsonRoot = new JsonObject();
		jsonRoot.add(addKey, jsonObject);
		return jsonRoot.toString();
	}
	
	
	/**
	 * JSON String에서 특정 필드의 값을 반환해준다.
	 * 
	 * @param jsonStr
	 *            변환할 JSON 문자열
	 * @param elementType
	 *            값을 가져올 필드값
	 * @return 변환된 특정 필드의 값
	 */
	public static final String getValueFromJson(String jsonStr, String elementType) {

		JSONObject jsonObj;
		try {	
			jsonObj = new JSONObject(jsonStr);
			return jsonObj.getString(elementType);
		} catch (JSONException e) {
			return null;
		}
	}

	/**
	 * JSON String에서 객체 리스트 형식으로 변환해준다.
	 * 
	 * @param classType
	 *            변환을 원하는 객체 타입
	 * @param jsonStr
	 *            변환할 JSON 문자열
	 * @return 변환된 객체 리스트
	 */
	public static final <T> List<T> getListFromJson(Class<T[]> classType, String jsonStr) {

		try {
			T[] jsonToObject = new Gson().fromJson(jsonStr, classType);
			return new ArrayList<>(Arrays.asList(jsonToObject));
		}
		catch (JsonSyntaxException e) {
			return null;
		}
		
		
	}

	/**
	 * JSON String의 특정 필드에서 객체 리스트 형식으로 변환해준다.
	 * 
	 * @param classType
	 *            변환을 원하는 객체 타입
	 * @param jsonStr
	 *            변환할 JSON 문자열
	 * @param elementType
	 *            JSON String 내 특정 필드값
	 * @return 변환된 객체 리스트
	 */
	public static final <T> ArrayList<T> getListFromJson(Class<T[]> classType, String jsonStr, String elementType) {
		
		try {
			
			JsonParser parser = new JsonParser();
			JsonElement rootObejct = parser.parse(jsonStr).getAsJsonObject().get(elementType);
			T[] jsonToObject = new Gson().fromJson(rootObejct, classType);
			return new ArrayList<>(Arrays.asList(jsonToObject));
		}
		catch (JsonSyntaxException e) {
			return null;
		}
	}



	
	/**
	 * 키와 값으로 이루어진 JSON String으로 만들어 반환해 준다.
	 * @param key  키
	 * @param value  값
	 * @return 변환된 JSON 문자열
	 */
	public static final String getJsonFromKeyValue(String key, String value) {
		
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put(key, value);
			return jsonObj.toString();
		} catch (JSONException e) {
			return null;
		}
	}

}
