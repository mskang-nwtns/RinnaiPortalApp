package kr.co.rinnai.dms.wms.model;

import java.util.regex.Pattern;

import kr.co.rinnai.dms.common.CommonValue;


/**
 * 
 * @author mini3248
 * Picking 지시서 조회 결과
 */
public class OrderReportResult {
	
	/**
	 * ORDER_SEQ : 지시 수량(주문수량)
	 */
	private int orderSeq;
	
	/**
	 * ORDER_DATE : 출고 지시일자
	 */
	private String orderDate;
	
	/**
	 * ORDER_NO
	 */
	private String orderNo;
	
	/**
	 * CUST_CODE 
	 */
	private String custCode;
	
	/**
	 * CUST_NAME
	 */
	private String custName;
	
	/**
	 * MODEL_CODE 모델 코드
	 */
	private String modelCode;
	
	/**
	 * MODEL_NAME : 모델 이름
	 */
	private String modelName;
	
	/**
	 * GAS_TYPE : 가스 구분
	 */
	private String gasType;
	
	/**
	 * ORDER_LOCATION : 위치
	 */
	private String orderLocation;
	
	/**
	 * ORDER_DETAIL 출고 지시 번호
	 */
	private String orderDetail;
	
	/**
	 * JOB_NAME : 출고 지시 수량
	 */
	private String jobName;

	/**
	 * CELL_MAKE : 제품 생성 년월
	 */
	private String cellMake;

	/**
	 * 	대리점 피킹 지시서 번호
	 */
	private String agencyOrderBarcode;
	
	private boolean read = false;

	private boolean completed = false;

	public int getOrderSeq() {
		return orderSeq;
	}

	public void setOrderSeq(int orderSeq) {
		this.orderSeq = orderSeq;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCustCode() {
		return custCode;
	}

	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getGasType() {
		return gasType;
	}

	public void setGasType(String gasType) {
		this.gasType = gasType;
	}

	public String getOrderLocation() {
		
		if(null == this.orderLocation) {
			this.orderLocation = "수동찾기";
		}
		if(Pattern.matches(CommonValue.REGEX_DB_WAREHOUSE_LOCATION, this.orderLocation)) {
			String parseLocation = this.orderLocation.substring(0, 2) + "-"  + this.orderLocation.substring(2, 5) + "-" + this.orderLocation.substring(5, 6);
			return parseLocation;
		} else {
			return orderLocation;
		}
		
	}

	public void setOrderLocation(String orderLocation) {
		this.orderLocation = orderLocation;
	}

	public String getOrderDetail() {
		return orderDetail;
	}

	public void setOrderDetail(String orderDetail) {
		this.orderDetail = orderDetail;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getCellMake() {
		return cellMake;
	}

	public void setCellMake(String cellMake) {
		this.cellMake = cellMake;
	}

	public String getAgencyOrderBarcode() {
		return agencyOrderBarcode;
	}

	public void setAgencyOrderBarcode(String agencyOrderBarcode) {
		this.agencyOrderBarcode = agencyOrderBarcode;
	}
}

