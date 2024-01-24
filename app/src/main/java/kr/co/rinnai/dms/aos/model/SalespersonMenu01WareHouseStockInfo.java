package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 대리점용 오더 재고(창고별 재고) 현황 조회 결과
 * 서버 VO (ProductStockByWarehouseResultVO)
 */

public class SalespersonMenu01WareHouseStockInfo {

    private String modelName;

    private String gas;

    private String orderType;

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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
}
