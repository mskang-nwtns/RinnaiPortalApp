package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 판매정보이관 조회 결과
 * 서버 VO (TransferSaleInfomationResultVO)
 *
 */
public class AgencyMenu04ListEntity {

    private String comId;
    private String centerCode;
    private String actDate;
    private String asSeq;
    /**
     *  -- 이관 구분
     */
    private String transGubun;
    private String transType;

    private String transName;

    /**
     * -- 이관일자
     */
    private String transDate;


    /**
     * -- 이관거래처
     */
    private String rCustCode;
    /**
     *  -- 이관거래처명
     */
    private String custName;
    /**
     *  -- 고객명
     */
    private String cName;
    /**
     * -- 고객전화번호1
     */
    private String telNo;
    /**
     * -- 고객전화번호2
     */
    private String sTelNo;
    /**
     * -- 우편번호
     */
    private String zipCode;
    /**
     * -- 주소
     */
    private String addr;
    /**
     *  -- 고객희망기종
     */
    private String cModelName;
    /**
     * -- 설비코드
     */
    private String cCode;
    /**
     * -- 거래처수신여부
     */
    private String cusSmsYn;
    /**
     * -- 고객조치일자
     */
    private String cusActDate;
    /**
     * -- 판매여부
     */
    private String cusSaleYn;
    /**
     * -- 판매일자
     */
    private String cusSaleDate;
    /**
     *   -- 판매모델
     */
    private String saleModelNo;
    /**
     *  -- 모델명
     */
    private String modelName;
    /**
     * -- 가스구분
     */
    private String gas;
    /**
     *  -- 비고
     */
    private String cusRef;
    /**
     * -- 이관서비스기사(직원) 명
     */
    private String engineer;
    /**
     * -- 이관기사(직원) 연락처
     */
    private String engHpNo;
    private String shopName;
    private String modelTransType;

    private boolean selected;

    public String getComId() {
        return comId;
    }
    public void setComId(String comId) {
        this.comId = comId;
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
    public String getTransDate() {
        return transDate;
    }
    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }
    public String getrCustCode() {
        return rCustCode;
    }
    public void setrCustCode(String rCustCode) {
        this.rCustCode = rCustCode;
    }
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getcName() {
        return cName;
    }
    public void setcName(String cName) {
        this.cName = cName;
    }
    public String getTelNo() {
        return telNo;
    }
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
    public String getsTelNo() {
        return sTelNo;
    }
    public void setsTelNo(String sTelNo) {
        this.sTelNo = sTelNo;
    }
    public String getZipCode() {
        return zipCode;
    }
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getcModelName() {
        return cModelName;
    }
    public void setcModelName(String cModelName) {
        this.cModelName = cModelName;
    }
    public String getcCode() {
        return cCode;
    }
    public void setcCode(String cCode) {
        this.cCode = cCode;
    }
    public String getCusSmsYn() {
        return cusSmsYn;
    }
    public void setCusSmsYn(String cusSmsYn) {
        this.cusSmsYn = cusSmsYn;
    }
    public String getCusActDate() {
        return cusActDate;
    }
    public void setCusActDate(String cusActDate) {
        this.cusActDate = cusActDate;
    }
    public String getCusSaleYn() {
        return cusSaleYn;
    }
    public void setCusSaleYn(String cusSaleYn) {
        this.cusSaleYn = cusSaleYn;
    }
    public String getCusSaleDate() {
        return cusSaleDate;
    }
    public void setCusSaleDate(String cusSaleDate) {
        this.cusSaleDate = cusSaleDate;
    }
    public String getSaleModelNo() {
        return saleModelNo;
    }
    public void setSaleModelNo(String saleModelNo) {
        this.saleModelNo = saleModelNo;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getGas() {
        return gas;
    }
    public void setGas(String gas) {
        this.gas = gas;
    }
    public String getCusRef() {
        return cusRef;
    }
    public void setCusRef(String cusRef) {
        this.cusRef = cusRef;
    }
    public String getEngineer() {
        return engineer;
    }
    public void setEngineer(String engineer) {
        this.engineer = engineer;
    }
    public String getEngHpNo() {
        return engHpNo;
    }
    public void setEngHpNo(String engHpNo) {
        this.engHpNo = engHpNo;
    }
    public String getShopName() {
        return shopName;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getModelTransType() {
        return modelTransType;
    }
    public void setModelTransType(String modelTransType) {
        this.modelTransType = modelTransType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }
}
