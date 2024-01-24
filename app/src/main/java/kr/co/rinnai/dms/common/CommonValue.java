package kr.co.rinnai.dms.common;

import java.util.ArrayList;

import kr.co.rinnai.dms.wms.adapter.OrderReportAgencyListAdapter;
import kr.co.rinnai.dms.wms.adapter.OrderReportListAdapter;
import kr.co.rinnai.dms.wms.model.OrderReportResult;

public class CommonValue {

	public static final String HTTP_ADDRESS_SEARCH = "http://www.juso.go.kr/addrlink/addrLinkApi.do";

	public static final int ADDRESS_SEARCH_CURRENT_PER_PAGE = 30;

	public static final String ADDRESS_SEARCH_KEY = "U01TX0FVVEgyMDIwMDQyMzE1NDIxMTEwOTY5NzE=";

	public static final String ADDRESS_SEARCH_RESULT_TYPE = "json";

	public static final String BR_RINNAI_ERROR_DLG = "rinnai.error.dlg";
	public static final String BR_RINNAI_ERROR_NOTI = "rinnai.error.noti";

	public static final String INTENT_ERROR_TITLE = "intent_error_title";
	public static final String INTENT_ERROR_DEVICE = "intent_error_device";
	public static final String INTENT_ERROR_DATE = "intent_error_date";
	public static final String INTENT_CONNECT_ERROR = "intent_connect_error";


	public static final String INTENT_SITEINFO_KEY = "siteInfo";

	public static final String INTENT_RETAILER_KEY = "retailerInfo";



	public static final String HTTP_WMS = "wms";
	public static final String HTTP_ORDER_REPORT = "orderreport";
	public static final String HTTP_ORDER_REPORT_AGENCY = "orderreportagency";
	public static final String HTTP_LOCATION = "location";
	public static final String HTTP_AGENCY_ORDER = "agencyorder";
	public static final String HTTP_RETAILER = "retailer";
	public static final String HTTP_PICKING = "picking";
	public static final String HTTP_SUSPEND = "suspend";
	public static final String HTTP_COUPANG = "coupang";
	public static final String HTTP_AGENCY_ORDER_CHK = "agencyorderchk";
	public static final String HTTP_VERSION_2 = "v2";
	public static final String HTTP_VERSION_3 = "v3";
	public static final String HTTP_VERSION_4 = "v4";
	public static final String HTTP_VERSION_5 = "v5";
	public static final String HTTP_PROCESS_TYPE_DEL = "del";
	public static final String HTTP_PROCESS_TYPE_DEL_ALL = "delAll";
	public static final String HTTP_PROCESS_TYPE_ADD = "add";
	public static final String HTTP_PROCESS_TYPE_ADD_ALL = "addAll";

	public static final String HTTP_MOVEMENT_INSTRUCTIONS = "movement";
	public static final String HTTP_INSTALLATION = "installation";

	public static final String HTTP_INFO = "info";
	public static final String HTTP_DETAIL = "detail";
	public static final String HTTP_FILE = "file";
	public static final String HTTP_CHECK = "check";



	public static final String HTTP_MODEL = "model";
	public static final String HTTP_AGENCY= "agency";
	public static final String HTTP_PRODUCT= "product";


	// 대리점 운영 시스템
	public static final String HTTP_AOS = "aos";
	public static final String HTTP_SITE_MANAGEMENT = "site";
	public static final String HTTP_SERVICE = "service";
	public static final String HTTP_TYPE = "type";


	public static final String HTTP_BUILDING= "building";
	public static final String HTTP_COMMON= "common";

	public static final String HTTP_COMM= "comm";
	// 영업 직원 운영 시스템
	public static final String HTTP_SOS = "sos";

	public static final String HTTP_SALES = "sales";
	public static final String HTTP_STOCK = "stock";
	public static final String HTTP_PRINT = "print";
	public static final String HTTP_BARCODE = "barcode";
	public static final String HTTP_CATEGORY = "category";
	public static final String HTTP_UNSHIPPED = "unshipped";
	public static final String HTTP_CARGO = "cargo";
	public static final String HTTP_DISTRIBUTES = "distributes";

	public static final String HTTP_DELIVERY = "delivery";

	public static final String productByOrderableResultVO = "order";


	public static final String HTTP_TRANSFER_SALE_INFOMATION = "tsi";

	public static final String HTTP_CONSUMER_DOUBLE_CHECK = "cdc";

	public static final String HTTP_ITEMCODE = "itemcode";
	public static final String HTTP_OHTER = "other";
	public static final String HTTP_ORDER = "order";




	public static final String HTTP_SIE= "sie";

	public static final String BROADCAST_SCANNER_MESSAGE_INTENT = "device.scanner.EVENT";
	public static final String BROADCAST_SCANNER_EXTRA_EVENT_DECODE_VALUE = "EXTRA_EVENT_DECODE_VALUE";

	public static final int SQLITE_DB_VERSION = 14;
	public static final String SQLITE_DB_FILE_NAME = "portal_app.db";

	/**
	 * 사용자 정보 관련 DB 테이블
	 */
	public static final String SQLITE_DB_TABLE_NAME_USER_INFO = "TB_USER_INFO";
	public static final String SQLITE_DB_TABLE_USER_INFO_FILED_NAME_CLIENT_ID = "clientId";
	public static final String SQLITE_DB_TABLE_USER_INFO_FILED_NAME_ACCESS_TOKEN = "accessToken";
    public static final String SQLITE_DB_TABLE_USER_INFO_FILED_NAME_REFRESH_TOKEN = "refreshToken";
    public static final String SQLITE_DB_TABLE_USER_INFO_FILED_NAME_CODE = "code";
    public static final String SQLITE_DB_TABLE_USER_INFO_FILED_NAME_ACCESS_TOKEN_CREATE_DATE = "createdAt";
    public static final String SQLITE_DB_TABLE_USER_INFO_FILED_NAME_REFRESH_TOKEN_CREATE_DATE = "createdRt";
    public static final String SQLITE_DB_TABLE_USER_INFO_FILED_NAME_EXPIRES_TIME = "expiresIn";

    public static final String SQLITE_DB_TABLE_NAME_LOGIN_INFO = "TB_LOGIN_INFO";

    public static final String SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_ID = "idx";
    public static final String SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NO = "userNo";
	public static final String SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_USER_NAME = "userName";
	public static final String SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_CODE = "codeDept";
	public static final String SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_DEPT_NAME = "deptName";
	public static final String SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_SAVE_USER_NO = "saveUserNo";
	public static final String SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_GROUP_WARE_ID = "userId";
	public static final String SQLITE_DB_TABLE_LOGIN_INFO_FILED_NAME_LOGIN_ID_TYPE = "type";

	/**
	 * 제품 모델 번호
	 */
	public static final String SQLITE_DB_TABLE_COMMON_FILED_NAME_ITEM_CODE  = "itemCode";

	/**
	 * 제품 모델 이름
	 */
	public static final String SQLITE_DB_TABLE_COMMON_FILED_NAME_MODEL_NAME = "modelName";

	/**
	 * 가스 종류
	 */
	public static final String SQLITE_DB_TABLE_COMMON_FILED_NAME_TYPE = "type";

	/**
	 * 작업 상황( reading, processing , complete)
	 */
	public static final String SQLITE_DB_TABLE_COMMON_FILED_NAME_STATUS = "status";


	/**
	 * 작업 상황( reading, processing , complete)
	 */
	public static final String SQLITE_DB_TABLE_COMMON_FILED_NAME_PALLET_SEQ = "palletSeq";


    /**
	 * 대리점 출하 관련 DB 테이블
	 */
	public static final String SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO = "TB_AGENCY_ORDER_INFO";

	/**
	 * 대리점 출하 검수 관련 DB 테이블
	 */
	public static final String SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_CHK = "TB_AGENCY_ORDER_INFO_CHK";

	/**
	 * 대리점 출하 관련 DB 테이블 쿠팡
	 */
	public static final String SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_COUPANG = "TB_AGENCY_ORDER_INFO_COUPANG";

	/**
	 * 대리점 출하 검수 관련 DB 테이블
	 */
	public static final String SQLITE_DB_TABLE_NAME_AGENCY_ORDER_INFO_RETAILER = "TB_AGENCY_ORDER_INFO_RETAILER";

	/**
	 * 대리점 주문 번호(ex : 2019/05/28-00666-3-01)
	 */
	public static final String SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_AGENCY_ORDER_BARCODE = "agencyOrderBarcode";

	/**
	 * 대리점 정보(이름)
	 */
	public static final String SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_NAME = "custName";

	/**
	 * 대리점 코드(코드 번호)
	 */
	public static final String SQLITE_DB_TABLE_AGNECY_ORDER_FILED_NAME_CUST_CODE = "custCode";

	/**
	 * 제품 주문 수량(PICKING 지시 수량)
	 */
	public static final String SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_INSTRUCTIONS = "instructions";

	/**
	 * 제품 주문 수량(PICKING 작업 수량)
	 */
	public static final String SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_OPERATIONS = "operations";

	/**
	 * 제품 주문 수량(PICKING 작업 수량)
	 */
	public static final String SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BOX_COUNT = "boxCount";

	/**
	 * 대리점
 	 */

	public static final String SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO = "TB_AGENCY_ORDER_REDING_INFO";

	/**
	 * 대리점 검수
	 */

	public static final String SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_CHK = "TB_AGENCY_ORDER_REDING_INFO_CHK";

	/**
	 * 대리점 쿠팡
	 */

	public static final String SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_COUPANG = "TB_AGENCY_ORDER_REDING_INFO_COUPANG";

	/**
	 * 대리점 쿠팡
	 */

	public static final String SQLITE_DB_TABLE_NAME_AGENCY_ORDER_READING_INFO_RETAILER = "TB_AGENCY_ORDER_REDING_INFO_RETAILER";


	public static final String SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_REMARK = "remark";

	/**
	 * 제품 바코드 번호
	 */
	public static final String SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_BARCODE  = "barcode";

	/**
	 * 2,3 공장 제품 이동 지시 수량
	 */
	public static final String SQLITE_DB_TABLE_AGENCY_ORDER_FILED_NAME_COUNT = "count";


	public static final String SQLITE_DB_TABLE_NAME_COUPANG_INFO = "TB_COUPANG_INFO";

	public static final String SQLITE_DB_TABLE_COUPANG_INFO_FILED_NAME_ID = "idx";
	public static final String SQLITE_DB_TABLE_COUPANG_INFO_FILED_NAME_ORDER_NO = "orderNo";
	public static final String SQLITE_DB_TABLE_COUPANG_INFO_FILED_NAME_ORDER_SEQUENCE = "orderSeq";
	public static final String SQLITE_DB_TABLE_COUPANG_INFO_FILED_NAME_COUPANG_PRODUCT_NO = "coupangProductNo";
	public static final String SQLITE_DB_TABLE_COUPANG_INFO_FILED_NAME_COUPANG_PRODUCT_NAME = "coupangProductName";
	public static final String SQLITE_DB_TABLE_COUPANG_INFO_FILED_NAME_RINNAI_PRODUCT_NO = "rinnaiProcuctNo";
	public static final String SQLITE_DB_TABLE_COUPANG_INFO_FILED_NAME_RINNAI_PRODUCT_CODE = "rinnaiProcuctCode";
	public static final String SQLITE_DB_TABLE_COUPANG_INFO_FILED_NAME_ORDER_COUNT = "orderCount";

	public static final String SQLITE_DB_TABLE_NAME_PRODUCT_MOVE_HANDLING = "TB_PRODUCT_MOVE_HANDLING";

	public static final String SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_USER_NO = "userNo";
	public static final String SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_OUT_DATA = "outData";
	public static final String SQLITE_DB_TABLE_PRODUCT_MOVE_HANDLING_FILED_NAME_IN_DATA = "inData";






	/**
	 * PICKING 작업지시서 바코드 번호 확인 (EX 2019-10-30L-27)
	 */
	public static final String REGEX_ORDER_REPORT_NO= "^\\d{4}-\\d{2}-\\w{2,4}-\\d{1,2}$";
	
	/**
	 * 대리점 바코드 번호 확인(EX 2019/03/29-G7077-L-17J)
	 */
	public static final String REGEX_ORDER_REPORT_CUST_CODE = "^\\d{4}/\\d{2}/\\d{2}-\\w{3,5}-\\w{1}-\\w{1,4}$";
	
	/**
	 * 물류 센터 로케이션 번호 확인(EX 02-A15-0)
	 */
	public static final String REGEX_WAREHOUSE_LOCATION = "^\\d{2}-\\w{3}-\\d{1}$";

	/**
	 * DB에 저장되어있는 물류센터 로케이션 번호(EX 02A150)
	 */
	public static final String REGEX_DB_WAREHOUSE_LOCATION = "^\\d{2}\\w{3}\\d{1}$";
	
	/**
	 * 
	 */
	public static final String REGEX_AGENCY_BARCODE = "^\\d{4}/\\d{2}/\\d{2}-\\w{5}-w{1}-w{2,3}$";

	/**
	 * 입하에서 출력된 바코드(바코드시스템에서 생성된 바코드) </br>
	 * ex) 1288421909600410025
	 * 128842( itemCode : 128840002) 000은 의미없음  2 가스구분 ) 1906(생산년월 : yymm) 6 (생산라인) 0041 (일련번호) 0025 (등록 갯수)
	 */
	public static final String REGEX_RECEIVED_BARCODE = "^\\d{10}\\w{1}\\d{8}$";


	/**
	 * 106541190140001
	 *
	 * 제품 일련번호 ex) R331-16KF : 106541190140001(신정보에서 생성된 바코드)
	 * 106541 ( itemCode: 제품 코드 (106540001) 000은 의미 없음) 1901 (생산년월: yymm) 4 (생산 line) 0001 (일련번호)
	 */
	public static final String REGEX_PRODUCT_BARCODE = "^\\d{10}\\w{1}\\d{4}$";

	/**
	 * 106541190*140001
	 * 제품 일련번호 ex) R331-16KF : 106541190140001(신정보에서 생성된 바코드)
	 * 106541 ( itemCode: 제품 코드 (106540001) 000은 의미 없음) 1901 (생산년월: yymm) *(아리스톤 모델 Type(삭제 필요)) 4 (생산 line) 0001 (일련번호)
	 * 아리스톤 수입용 전기온수기 모델 체크
	 * 바코드 자릿수 오류로 모델Type (더미데이터 삭제 필요)
	 */
	public static final String REGEX_PRODUCT_BARCODE_ARISTON = "^\\d{10}\\w{1}\\w{1}\\d{4}$";

	/**
	 * 입화 화면에서 사용 물류 이동 지시서 번호 확인(EX R-2019/09/06-01-10) YYYY/MM/DD-01(이동처리 번호 01:인천 물류)-10( 이동지시 순번)
	 */
	public static final String REGEX_MOVEMENT_INSTRUCTIONS_BARCODE = "^\\w{1}-\\d{4}/\\d{2}/\\d{1,2}-\\d{1,2}-\\d{1,2}-\\d{1,2}$";

	public static final String LOG_TAG_SCANNER_RESULT_CHECK = "rinnai_scanner";

	public static final String REGEX_SCAN_READ_FAIL = "READ_FAIL";
	
	
	

	/*
	 * Sample Data
	   I_COM_ID(R)              IN      CHAR,
	   I_WAREHOUSE(16)            IN      CHAR,
	   I_SALE_DATE(orderDate)            IN      CHAR,
	   I_CAR_ITEM(3:orderNo 앞자리)             IN      CHAR,
	   I_TRS_NO(9 : orderNo 뒷자리)               IN      CHAR,
	   I_IO_ITEM_SEQ(10:jobName)        IN      CHAR,
	   I_PDA_JOB_QTY (0:orderSeq)         IN      CHAR,
	   I_PDA_JOB_NO (PDA_IP)          IN      CHAR,
	   I_MODEL_CODE(128801:modelCode)           IN      CHAR,
	   I_ORDER_DETAIL(531:orderDetail)         IN      CHAR,
       I_CELL_NO(08-A03-0 : 로케이션 위치)              IN      CHAR

		"orderSeq": 0,
	   "orderDate": "2019-02-27", I_SALE_DATE
	   "orderNo": "3-9",
	   "custCode": "E0882",
	   "custName": "동해주방설비",
	   "modelCode": "128801", I_MODEL_CODE
	   "modelName": "RC300-18KFN",
	   "gasType": "LPG",
	   "orderLocation": "01B120",
	   "orderDetail": "531  ",  I_ORDER_DETAIL
	   "jobName": "10" I_IO_ITEM_SEQ
   */


	public static final String WMS_I_COM_ID = "R";
	public static final String WMS_INCHON_WAREHOUSE = "16";
	
	public static final String WMS_PARAMETER_KEY_I_COM_ID = "inComId";
	public static final String WMS_PARAMETER_KEY_I_WAREHOUSE = "inWareHouse";

	public static final String WMS_PARAMETER_KEY_I_INDEX = "index";
	/**
	 * orderDate
	 */
	public static final String WMS_PARAMETER_KEY_I_SALE_DATE = "inSaleDate";
	/**
	 * orderNo 앞자리
	 */
	public static final String WMS_PARAMETER_KEY_I_CAR_ITEM = "inCarId";
	/**
	 * orderNo 뒷자리
	 */
	public static final String WMS_PARAMETER_KEY_I_TRS_NO = "inTrsNo";
	/**
	 * jobName
	 */
	public static final String WMS_PARAMETER_KEY_I_IO_ITEM_SEQ = "inIoItemSeq";
	/**
	 * orderSeq
	 */
	public static final String WMS_PARAMETER_KEY_I_PDA_JOB_QTY = "inPdaJobQty";
	/**
	 * PDA_IP
	 */
	public static final String WMS_PARAMETER_KEY_I_PDA_JOB_NO = "inPdaJobNo";
	/**
	 * modelCode
	 */
	public static final String WMS_PARAMETER_KEY_I_MODEL_CODE = "inModelCode";
	/**
	 * orderDetail
	 */
	public static final String WMS_PARAMETER_KEY_I_ORDER_DETAIL = "inOrderDetail";
	/**
	 * 로케이션 위치
	 */
	public static final String WMS_PARAMETER_KEY_I_CELL_NO = "inCellNo";

	/**
	 * 물류 창고 구분 코드
	 */
	public static final String WMS_PARAMETER_KEY_AG_CODE = "agCode";

	/**
	 * PDA 번호
	 */
	public static final String WMS_PARAMETER_KEY_PDA_NO = "pdaNo";


	/**
	 * modelCode
	 */
	public static final String WMS_PARAMETER_KEY_MODEL_CODE = "modelCode";

	public static final String WMS_PARAMETER_KEY_MODEL_NAME = "modelName";
	public static final String WMS_PARAMETER_KEY_LOCATION = "location";
	
	
	public static final String WMS_NOW_VIEW_NAME_LOCATION_BARCODE = "location";
	public static final String WMS_NOW_VIEW_NAME_AGENCY_BARCODE = "agency";

	public static final String WMS_NOW_VIEW_NAME_MODEL_05 = "model_wms";

	public static final String WMS_NOW_VIEW_NAME_AGENCY_06 = "agency_sos";


	public static final String AOS_NOW_VIEW_NAME_CUST = "cust";
	public static final String AOS_NOW_VIEW_NAME_MODEL = "model";
	public static final String AOS_NOW_VIEW_NAME_MODEL_05 = "model_05";

	public static final String WMS_PARAMETER_KEY_WAREHOUSE = "warehouse";
	public static final String WMS_PARAMETER_KEY_ITEM_SEQ= "itemSeq";
	public static final String WMS_PARAMETER_KEY_OUT_WAREHOUSE= "outData";
	public static final String WMS_PARAMETER_KEY_IN_WAREHOUSE= "inData";



	
	
    public static String HMACSHA1_KEY = "CasCadeKey";
    public static final boolean IS_LOG = true;
    
    public static final String TRUE = "1";
    public static final String FALSE = "0";
    
//	public static final String WS_HOST = "ws://192.168.30.30:8080/cascade/ws/websocket/";
//    public static final String HTTP_HOST = "http://192.168.30.30:8080/cascade/";
    

	public static final String WS_HOST = "ws://logistics.rinnai.co.kr/cascade/ws/websocket/";
	public static final String HTTP_IMAGE_DOWNLOAD_HOST = "http://ferp.rinnai.co.kr/upload/s/FS1_BUY_070I/upload_file";

//    public static final String HTTP_HOST = "http://10.10.90.101:8080";
    public static final String HTTP_HOST = "http://logistics.rinnai.co.kr";
//	public static final String HTTP_HOST = "http://10.10.25.138:8080";
//	public static final String HTTP_HOST = "http://192.168.2.1:8080";
//	public static final String HTTP_LOCAL_HOST = "http://logistics.rinnai.co.kr";
//    public static final String HTTP_LOGIN = HTTP_HOST + "user";
    public static final String HTTP_EVENT = HTTP_HOST + "event";
    public static final String HTTP_RESERVE = HTTP_HOST + "reserve";

    public static final String HEADER_LOGIN_KEY = "accessToken";
    public static final String REST_SUCCESS = "200";
    public static final String REST_PROGRESS = "202";
    public static final String REST_FAIL = "400";
	public static final String RINNAI_HOT_WATER = "0";
	public static final String RINNAI_HEATER = "1";
	public static final int RINNAI_ON = 1;
	public static final int RINNAI_OFF = 0;
    
    public static final String SERVICE_NAME = "com.rinnai.service.WebSocketService";
    
	public static final String INTENT_RECEIVER = "intent_receiver";
	public static final String INTENT_MSG = "intent_msg";
	public static final String INTENT_TITLE = "intent_title";
	public static final String INTENT_RESERVE_DATA = "intent_reserve_data";
	public static final String INTENT_RESERVE_TYPE = "intent_reserve_type";
	public static final String INTENT_SELECTED_DAY = "intent_selected_day";
	public static final String INTENT_BOILER_DATA = "intent_boiler_data";
	public static final String INTENT_BOILER_TYPE = "intent_boiler_type";
	public static final String INTENT_BOILER_INDEX = "intent_boiler_index";
	public static final String INTENT_TIME_CELL = "intent_time_cell";
	public static final String INTENT_DAY_OF_WEEK_CELL = "intent_day_of_week_cell";
	
//	public static final int HANDLER_MSG_DLG_SHOW = 1;
//	public static final int HANDLER_MSG_DLG_DISMISS = 2;
	
	public static final int INTENT_HEATER_ACT = 1;
	public static final int INTENT_HOT_WATER_ACT = 2;

	public static final int RESERVE_WEEK_TYPE= 1;
	public static final int RESERVE_DAY_TYPE= 2 ;
	
	public static final String DATE_FORMAT_EVENT_ID = "yyyyMMDD_HHmmss";
	public static final int HANDLER_PROGRESS_SHOW = 0;
	public static final int HANDLER_PROGRESS_DISSMISS = 1;
	public static final int HANDLER_RINNAI_DLG_SHOW = 2;
	public static final int HANDLER_RINNAI_DLG_DISSMISS = 3;
	public static final int HANDLER_RINNAI_RECEIVED_DLG_SHOW = 4;

	public enum DayOfWeek {		
		SUNDAY(0), MONDAY(1), TUESDAY(2), WEDNESDAY(3), THURSDAY(4), FRIDAY(5), SATURDAY(6);
		
	    private int val;

	    private DayOfWeek(int value){
	        val = value;
	    }

	    public int getValue(){
	        return val;
	    }
	}

	public static final String CLEAR_FLAG = "clear";
	public static final int CHECK_STATE = 0;


	public static final String HTTP_SENSOR_HOST = "http://wifitest.rinnai.co.kr:9204/";
	public static final String HTTP_SENSOR_LOCAL_HOST = "http://10.10.25.54:8080/";
	public static final String HTTP_PORTAL = "portal/";
	public static final String HTTP_SENSOR = HTTP_SENSOR_HOST + HTTP_PORTAL + "sensor";
	public static final String HTTP_USER = "user";
	public static final String HTTP_PUSH = "push/";
	public static final String HTTP_TOKEN = "token/";
	public static final String HTTP_LOGIN = "login";

	public static final String HTTP_LOG = "log";

	//검수화면 전체 출하 지시 수량
	public static int Act_4_totalInstructions = 0;
	//검수화면 제품 바코드 리딩 수량(제품 바코드 완료)
	public static int Act_4_totalOperations = 0;
	//검수화면 작업 수량(제품 바코드 리딩 후 서버로 전송 완료)
	public static int Act_4_totalSuccessCount = 0;
	//검수화면 제품 지시 남은 수량
	public static int Act_4_totalRemainingCount = 0;

	//출하화면 전체 출하 지시 수량
	public static int Act_8_totalInstructions = 0;
	//출하화면 제품 바코드 리딩 수량(제품 바코드 완료)
	public static int Act_8_totalOperations = 0;
	//출하화면 작업 수량(제품 바코드 리딩 후 서버로 전송 완료)
	public static int Act_8_totalSuccessCount = 0;
	//출하화면 제품 지시 남은 수량
	public static int Act_8_totalRemainingCount = 0;

	//쿠팡배송화면 전체 출하 지시 수량
	public static int Act_9_totalInstructions = 0;
	//쿠팡배송화면 제품 바코드 리딩 수량(제품 바코드 완료)
	public static int Act_9_totalOperations = 0;
	//쿠팡배송화면 작업 수량(제품 바코드 리딩 후 서버로 전송 완료)
	public static int Act_9_totalSuccessCount = 0;
	//쿠팡배송화면 제품 지시 남은 수량
	public static int Act_9_totalRemainingCount = 0;

	//쿠팡배송화면 전체 출하 지시 수량
	public static int Installation_Act_1_totalInstructions = 0;
	//쿠팡배송화면 제품 바코드 리딩 수량(제품 바코드 완료)
	public static int Installation_Act_1_totalOperations = 0;
	//쿠팡배송화면 작업 수량(제품 바코드 리딩 후 서버로 전송 완료)
	public static int Installation_Act_1_totalSuccessCount = 0;
	//쿠팡배송화면 제품 지시 남은 수량
	public static int Installation_Act_1_totalRemainingCount = 0;

	//출고화면 로케이션 별 List Adapter
	public static OrderReportListAdapter adapter = null;
	//출고화면 대리점 별 List Adapter
	public static OrderReportAgencyListAdapter adapterAgency = null;

	// 출고 지시서 목록
	public static ArrayList<OrderReportResult> orderReportResultList = null;

	public static ArrayList<String> pickingList = null;
	// 출고화면 현재 선택된 뷰(로케이션 or 대리점)
	public static String nowView = null;

	public static String agencyOrderBarcode = null;
	public static ArrayList<String> Act_4_agencyOrderBarcodeList = null;
	public static ArrayList<String> Act_8_agencyOrderBarcodeList = null;
	public static ArrayList<String> Act_9_agencyOrderBarcodeList = null;
	public static ArrayList<String> Installation_Act_1_agencyOrderBarcodeList = null;

	public static String LOGIN_TYPE_FAIL = "0";
	public static String LOGIN_TYPE_WMS = "1";
	public static String LOGIN_TYPE_SALES = "2";
	public static String LOGIN_TYPE_AGENCY = "3";
	public static String LOGIN_TYPE_SHIPPING_DRIVER = "4";
	public static String LOGIN_TYPE_INSTALLATION_DRIVER= "5";
	public static String LOGIN_TYPE_SALESPORSON= "6";
	public static String LOGIN_TYPE_SALESPORSON_MANAGER= "7";


	public static String PASS_TYPE_OK = "0";
	public static String PASS_NOT_FOUND = "1";
	public static String PASS_PASS_FAIL = "2";
	public static String PASS_UNLICENSED_AGENCY = "3";
	public static String PASS_VERSION_UPDATE ="4";



	//	--R_LOGIN_TYPE  CHAR(1);  -- 0 : FAIL, 1 : RK직원(WMS), 2 : RK직원(영업), 3 : 대리점, 4 : 상차기사, 5 : 양판점 설치기사
	//	--R_PASS_YN     CHAR(1);  -- 0 : OK , 1 : NOT FOUND, 2 : PASS FAIL, 3 : 비인가 (대리점) 4: Version Update
	//	--R_USER_NUMBER CHAR(20); -- C_USER_ID
	//	--R_USER_NAME   CHAR(40); -- 사용자명
	//	--R_CUST_CODE   CHAR(6); -- 부서코드, 대리점코드
	//	--R_CUST_NAME   CHAR(30); -- 부서명, 대리점명


	public final static String PROCUCT_TYPE_SI_600 = "24236";
	public final static String PROCUCT_TYPE_SI_610 = "24237";
	public final static String PROCUCT_TYPE_SA_100A = "24238";
	public final static String PROCUCT_TYPE_SA_100B = "24239";

	public static String[] PRODUCT_CO_ALARM_CHECK_SUM = {
		"1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
		"b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
		"l", "m", "n", "o", "p", "q", "r", "s", "t", "u",
		"v", "w", "x", "y", "z", "A", "B", "C", "D", "E",
		"F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
		"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
		"Z"
	};

	public static String[] CUSTOMER_DOUBLE_CHECK_CITY_NAME = {
		"서울", "경기", "인천"
	};



	public static String[][] CUSTOMER_DOUBLE_CHECK_SEUOL = {
		{"서초구", "강남구", "용산구", "마포구"},
		{"양천구", "강서구", "영등포구"},
		{"종로구", "은평구", "성북구", "강북구", "서대문구"},
		{"도봉구", "노원구"},
		{"광진구", "동대문구", "중랑구", "중구", "성동구"},
		{"구로구", "금천구", "동작구", "관악구"},
		{"송파구", "강동구"},
	};

	public static String[][] CUSTOMER_DOUBLE_CHECK_GYEONGGI = {
		{"과천시"},
		{"수원시", "화성시", "평택시", "안성시", "오산시"},
		{"의정부시", "동두천시", "양주시", "연천군", "포천시"},
		{"안양시", "안산시", "시흥시", "군포시", "의왕시"},
		{"성남시", "용인시"},
		{"부천시"},
		{"광명시"},
		{"김포시", "일산서구", "파주시"},
		{"광주시", "하남시", "구리시", "남양주시", "양평군", "가평군"},
		{"일산동구", "덕양구"}
	};

	public static String[][] CUSTOMER_DOUBLE_CHECK_INCHEON = {
		{"부평구", "계양구"},
		{"동구", "중구", "남구", "미추홀구", "연수구", "남동구", "서구", "강화군", "옹진군"}
	};

	public static Object[] CUSTOMER_DOUBLE_CHECK_CITY_VALUE = {
		CUSTOMER_DOUBLE_CHECK_SEUOL, CUSTOMER_DOUBLE_CHECK_GYEONGGI, CUSTOMER_DOUBLE_CHECK_INCHEON
	};


	public static String[] CUSTOMER_DOUBLE_CHECK_SEOUL_DILIVERY_NAME = {
		"공영건 010-4727-2808", "김상수 010-3386-8060", "김양선 010-6205-5791", "김영식 010-8831-9576",
		"이재룡 010-8747-7286", "이재영 010-6326-4545", "임재천 010-8729-5701"
	};

	public static String[] CUSTOMER_DOUBLE_CHECK_GYEONGGI_DILIVERY_NAME = {
			"공영건 010-4727-2808", "김성길 010-6462-4935", "김영식 010-8831-9576", "박의희 010-4273-9239",
			"손길수 010-5201-2476", "이승권 010-9975-6485", "이재영 010-6326-4545", "임정보 010-2288-6141",
			"최세웅 010-7511-1094", "황근수 010-3773-6795"
	};

	public static String[] CUSTOMER_DOUBLE_CHECK_INCHEON_DILIVERY_NAME = {
			"이승권 010-9975-6485", "최진환 010-4192-3434"
	};

	//센코
	//어댑터 타입 SI-600
	//242360200800001 SI-600
	//242368888888888 SI-600 * 40EA
	//배터리 타입 SI-610
	//242370200800001 SI-610
	//242378888888888 SI-610 * 50EA

	//세원엠테크
	//어댑터 타입 SA-100A
	//242380200800001 SA-100A
	//242389999999999 SA-100A * 10EA
	//242388888888888 SA-100A * 40EA
	//배터리 타입 SA-100B
	//242390200800001 SA-100B
	//242399999999999 SA-100B * 10EA
	//242398888888888 SA-100B * 40EA
}