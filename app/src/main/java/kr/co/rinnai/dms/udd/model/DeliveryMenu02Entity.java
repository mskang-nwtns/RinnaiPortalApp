package kr.co.rinnai.dms.udd.model;


/**
 *
 * @author mini3248
 * 대리점 제품 바코드 조회 결과
 * 서버 VO (ProductBarcodeResultVO)
 *
 */
public class DeliveryMenu02Entity {
    private String grDate;
    private String pdaNo;
    private String stCode;
    private String custName;
    private String modelName;

    public String getGrDate() {
        return grDate;
    }
    public void setGrDate(String grDate) {
        this.grDate = grDate;
    }
    public String getPdaNo() {
        return pdaNo;
    }
    public void setPdaNo(String pdaNo) {
        this.pdaNo = pdaNo;
    }
    public String getStCode() {
        return stCode;
    }
    public void setStCode(String stCode) {
        this.stCode = stCode;
    }
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
