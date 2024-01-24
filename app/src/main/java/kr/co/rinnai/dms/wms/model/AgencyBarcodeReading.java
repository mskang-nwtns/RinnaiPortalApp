package kr.co.rinnai.dms.wms.model;

public class AgencyBarcodeReading {
    private String agencyOrderBarcode;
    private String custCode;
    private String itemCode;
    private String barcode;
    private String pdaNo;
    private boolean status;
    private String modelName;

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
    public String getBarcode() {
        return barcode;
    }
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
    public String getPdaNo() {
        return pdaNo;
    }
    public void setPdaNo(String pdaNo) {
        this.pdaNo = pdaNo;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
