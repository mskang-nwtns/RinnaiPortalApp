package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 배송 제품 배송지 조회
 * 서버 VO (DeliveryAddressInfoResultVO)
 */

public class AgencyMenu03AddressInfo {

    private String comId;
    private String saleDate;
    private String custCode;
    private String trsNo;
    private String pdaNo;
    private String custName;
    private String destName;
    private String destSeq;
    private String destAddress;
    private String destTime;

    public String getComId() {
        return comId;
    }
    public void setComId(String comId) {
        this.comId = comId;
    }
    public String getSaleDate() {
        return saleDate;
    }
    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }
    public String getCustCode() {
        return custCode;
    }
    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }
    public String getTrsNo() {
        return trsNo;
    }
    public void setTrsNo(String trsNo) {
        this.trsNo = trsNo;
    }
    public String getPdaNo() {
        return pdaNo;
    }
    public void setPdaNo(String pdaNo) {
        this.pdaNo = pdaNo;
    }
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getDestName() {
        return destName;
    }
    public void setDestName(String destName) {
        this.destName = destName;
    }
    public String getDestSeq() {
        return destSeq;
    }
    public void setDestSeq(String destSeq) {
        this.destSeq = destSeq;
    }
    public String getDestAddress() {
        return destAddress;
    }
    public void setDestAddress(String destAddress) {
        this.destAddress = destAddress;
    }
    public String getDestTime() {
        return destTime;
    }
    public void setDestTime(String destTime) {
        this.destTime = destTime;
    }
}
