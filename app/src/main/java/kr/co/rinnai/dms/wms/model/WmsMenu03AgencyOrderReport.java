package kr.co.rinnai.dms.wms.model;

import java.util.regex.Pattern;

import kr.co.rinnai.dms.common.CommonValue;

public class WmsMenu03AgencyOrderReport {
    /**
     * CUST_CODE
     */
    private String custCode;

    /**
     * CUST_NAME
     */
    private String custName;



    /**
     * 	대리점 피킹 지시서 번호
     */
    private String agencyOrderBarcode;


    /**
     * 작업 완료 Count
     */
    private int jobY;

    /**
     * 작업 미 완료 Count
     */
    private int jobN;

    /**
     * 주문 수량 Count
     */
    private int orderQty;

    /**
     * 작업 완료 Count
     */
    private int jobQty;


    private String modelCode;

    private String modelName;

    private String orderLocation;

    private String jobYn;

    private int count;

    private String gasType;

    private String cellMake;

    private boolean read = false;

    private boolean completed = false;

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

    public String getAgencyOrderBarcode() {
        return agencyOrderBarcode;
    }

    public void setAgencyOrderBarcode(String agencyOrderBarcode) {
        this.agencyOrderBarcode = agencyOrderBarcode;
    }

    public int getJobY() {
        return jobY;
    }

    public void setJobY(int jobY) {
        this.jobY = jobY;
    }

    public int getJobN() {
        return jobN;
    }

    public void setJobN(int jobN) {
        this.jobN = jobN;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public int getJobQty() {
        return jobQty;
    }

    public void setJobQty(int jobQty) {
        this.jobQty = jobQty;
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

    public String getJobYn() {
        return jobYn;
    }

    public void setJobYn(String jobYn) {
        this.jobYn = jobYn;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getGasType() {
        return gasType;
    }

    public void setGasType(String gasType) {
        this.gasType = gasType;
    }

    public String getCellMake() {
        return cellMake;
    }

    public void setCellMake(String cellMake) {
        this.cellMake = cellMake;
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
}
