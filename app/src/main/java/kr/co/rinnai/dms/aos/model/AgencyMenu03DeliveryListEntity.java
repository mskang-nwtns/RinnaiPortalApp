package kr.co.rinnai.dms.aos.model;
/**
 *
 * @author mini3248
 * 배송 제품 목록 조회(배차 or 화물 조회 쿼리용 VO)
 * 서버 VO (DeliveryInfoResultVO)
 */

public class AgencyMenu03DeliveryListEntity {

    private String modelName;
    private String gas;
    private String warehouse;
    private String qty;
    private String saleDate;

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
    public String getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }
}
