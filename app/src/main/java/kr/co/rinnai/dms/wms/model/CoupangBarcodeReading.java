package kr.co.rinnai.dms.wms.model;

/**
 * 
 * @author mini3248
 *
 */
public class CoupangBarcodeReading {
	
	private String agencyOrderBarcode;
	private String custCode;
	private String itemCode;
	private String remark;
	private String agCode;
	private String userNo;
	private int boxCount;
	private int count;
	private int palletSeq;
	
	private boolean status;
	
	public String getAgencyOrderBarcode() {
		return agencyOrderBarcode;
	}
	public void setAgencyOrderBarcode(String agencyOrderBarcode) {
		this.agencyOrderBarcode = agencyOrderBarcode;
	}
	public String getCustCode() {
		return custCode;
	}
	public void setCustCode(String custCode) {
		this.custCode = custCode;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAgCode() {
		return agCode;
	}
	public void setAgCode(String agCode) {
		this.agCode = agCode;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public int getBoxCount() {
		return boxCount;
	}
	public void setBoxCount(int boxCount) {
		this.boxCount = boxCount;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}

	public boolean isStatus() {
		return status;
	}

	public int getPalletSeq() {
		return palletSeq;
	}

	public void setPalletSeq(int palletSeq) {
		this.palletSeq = palletSeq;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
