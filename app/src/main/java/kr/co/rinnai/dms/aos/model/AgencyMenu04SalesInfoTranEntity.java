package kr.co.rinnai.dms.aos.model;



/**
 *
 * @author mini3248
 * 판매정보 이관 업데이트
 * 서버 VO (TransferSaleInfomationQueryVO)
 */
public class AgencyMenu04SalesInfoTranEntity {

    private String cusSmsYn;
    private String cusSaleYn;

    private String cusActDate;
    private String cusSaleDate;
    private String modelCode;
    private String modelName;
    private String cusRef;

    private String centerCode;
    private String actDate;
    private String asSeq;
    private String transGubun;

    public String getCusSmsYn() {
        return cusSmsYn;
    }

    public void setCusSmsYn(String cusSmsYn) {
        this.cusSmsYn = cusSmsYn;
    }

    public String getCusSaleYn() {
        return cusSaleYn;
    }

    public void setCusSaleYn(String cusSaleYn) {
        this.cusSaleYn = cusSaleYn;
    }

    public String getCusActDate() {
        return cusActDate;
    }

    public void setCusActDate(String cusActDate) {
        this.cusActDate = cusActDate;
    }

    public String getCusSaleDate() {
        return cusSaleDate;
    }

    public void setCusSaleDate(String cusSaleDate) {
        this.cusSaleDate = cusSaleDate;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getCusRef() {
        return cusRef;
    }

    public void setCusRef(String cusRef) {
        this.cusRef = cusRef;
    }

    public String getCenterCode() {
        return centerCode;
    }

    public void setCenterCode(String centerCode) {
        this.centerCode = centerCode;
    }

    public String getActDate() {
        return actDate;
    }

    public void setActDate(String actDate) {
        this.actDate = actDate;
    }

    public String getAsSeq() {
        return asSeq;
    }

    public void setAsSeq(String asSeq) {
        this.asSeq = asSeq;
    }

    public String getTransGubun() {
        return transGubun;
    }

    public void setTransGubun(String transGubun) {
        this.transGubun = transGubun;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
