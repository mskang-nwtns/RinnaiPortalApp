package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 주문 현황 상세 조회 쿼리용 VO
 * 서버 VO (OrderDetailInfoResultVO)
 */

public class AgencyMenu02DetailListEntity {

    private String ordStats;
    private String itemName;
    private String itemNo;
    private String gas;
    private String ordQty;
    private String regDate;
    private String reqArea;
    private String accText;
    private String ordNo;
    private String deliveryGbn;
    private String reqRemark;

    public String getOrdStats() {
        return ordStats;
    }
    public void setOrdStats(String ordStats) {
        this.ordStats = ordStats;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemNo() {
        return itemNo;
    }
    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }
    public String getGas() {
        return gas;
    }
    public void setGas(String gas) {
        this.gas = gas;
    }
    public String getOrdQty() {
        return ordQty;
    }
    public void setOrdQty(String ordQty) {
        this.ordQty = ordQty;
    }
    public String getRegDate() {
        return regDate;
    }
    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    public String getReqArea() {
        return reqArea;
    }
    public void setReqArea(String reqArea) {
        this.reqArea = reqArea;
    }
    public String getAccText() {
        return accText;
    }
    public void setAccText(String accText) {
        this.accText = accText;
    }
    public String getOrdNo() {
        return ordNo;
    }
    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }
    public String getDeliveryGbn() {
        return deliveryGbn;
    }
    public void setDeliveryGbn(String deliveryGbn) {
        this.deliveryGbn = deliveryGbn;
    }
    public String getReqRemark() {
        return reqRemark;
    }
    public void setReqRemark(String reqRemark) {
        this.reqRemark = reqRemark;
    }
}
