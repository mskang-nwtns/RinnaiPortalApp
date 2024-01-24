package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 배송조회(배차 or 화물 조회 쿼리용 VO)
 * 서버 VO (DistributesResultVO)
 *
 */

public class AgencyMenu03ListEntity {

    private String custName;
    private String drvName;
    private String drvTelNo;
    private String warehouseName;
    private String carNum;
    private String pdaNo;
    private String comId;
    private String saleDate;
    private String trsNo;
    private String custCode;
    private String destTime;

    private String destName;
    private String destAddr;



    /**
     * 화물 배송 조회
     */
    private String smsYn;
    private String wareHouse;

    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getDrvName() {
        return drvName;
    }
    public void setDrvName(String drvName) {
        this.drvName = drvName;
    }
    public String getDrvTelNo() {
        return drvTelNo;
    }
    public void setDrvTelNo(String drvTelNo) {
        this.drvTelNo = drvTelNo;
    }
    public String getWarehouseName() {
        return warehouseName;
    }
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    public String getCarNum() {
        return carNum;
    }
    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }
    public String getPdaNo() {
        return pdaNo;
    }
    public void setPadNo(String pdaNo) {
        this.pdaNo = pdaNo;
    }
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
    public String getTrsNo() {
        return trsNo;
    }
    public void setTrsNo(String trsNo) {
        this.trsNo = trsNo;
    }
    public String getCustCode() {
        return custCode;
    }
    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }
    public String getSmsYn() {
        return smsYn;
    }
    public void setSmsYn(String smsYn) {
        this.smsYn = smsYn;
    }
    public String getWareHouse() {
        return wareHouse;
    }
    public void setWareHouse(String wareHouse) {
        this.wareHouse = wareHouse;
    }

    public String getDestTime() {
        return destTime;
    }

    public void setDestTime(String destTime) {
        this.destTime = destTime;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public String getDestAddr() {
        return destAddr;
    }

    public void setDestAddr(String destAddr) {
        this.destAddr = destAddr;
    }
}
