package kr.co.rinnai.dms.common;

public class CommonSetting {

	/* 인트로 */
	public final static boolean INTRO_SKIP = false;
	
	/* 로그 생성 여부 */
	public final static boolean LOG = true;
	
	/* 모바일 백신 관련 */
	public final static String M_VACCINE_SITE_ID = "Police_Dream";
	public final static String M_VACCINE_LICENSE_KEY = "f758806d7637c1a96000de74814e5eab286b3d7d";  //백신키 받은 것
	
	/* 키보드 보안 보안키 */
	public final static byte[] KEYPAD_SECURE_KEY = { 's', 'a', 'f', 'e', 't', 'y', 'D', 'r', 'e', 'a', 'm', '6', '0', '8', '0', '4' };
	
	/* 구글 맵 최초 세팅시 확대 정도 */
	public final static int SAFE_MAP_DETAIL_ZOOM_LEVEL = 15;
	
	/* 182, 117 전화번호 */
	public final static String MISSING_CHILDE_CALL_NUMBER = "182";
	public final static String COUNSELING_CALL_NUMBER = "117";
	
	/* 웹서버와 통신시 정의 값 */
	public final static String DEVICE_CODE = "android";
	public final static String DEVICE_CODE_NUMBER = "2";
	
	/* 위치를 받아오지 못했을 때의 위도 경도 */
	public final static double DEFAULT_LATITUDE = 37.5647848;
	public final static double DEFAULT_LONGITUDE = 126.9667762;
}
