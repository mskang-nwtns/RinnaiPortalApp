package kr.co.rinnai.dms.eos.model;

public class StockByStoreResult {

    /**
     * 대리점 명
     */
    private String custName;

    /**
     * 모델 명
     */
    private String modelName;

    /**
     * LPG 이월 재고 수량
     */
    private int lpTrsQty;

    /**
     * LNG 이월 재고 수량
     */
    private int lnTrsQty;

    /**
     * LPG 입고 수량
     */
    private int lpInQty;

    /**
     * LNG 입고 수량
     */
    private int lnInQty;

    /**
     * LPG 출고 수량
     */
    private int lpOutQty;

    /**
     * LNG 출고 수량
     */
    private int lnOutQty;

    /**
     * LPG 재고 수량
     */
    private int lpStockQty;

    /**
     * LNG 재고 수량
     */
    private int lnStockQty;
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
    public int getLpTrsQty() {
        return lpTrsQty;
    }
    public void setLpTrsQty(int lpTrsQty) {
        this.lpTrsQty = lpTrsQty;
    }
    public int getLnTrsQty() {
        return lnTrsQty;
    }
    public void setLnTrsQty(int lnTrsQty) {
        this.lnTrsQty = lnTrsQty;
    }
    public int getLpInQty() {
        return lpInQty;
    }
    public void setLpInQty(int lpInQty) {
        this.lpInQty = lpInQty;
    }
    public int getLnInQty() {
        return lnInQty;
    }
    public void setLnInQty(int lnInQty) {
        this.lnInQty = lnInQty;
    }
    public int getLpOutQty() {
        return lpOutQty;
    }
    public void setLpOutQty(int lpOutQty) {
        this.lpOutQty = lpOutQty;
    }
    public int getLnOutQty() {
        return lnOutQty;
    }
    public void setLnOutQty(int lnOutQty) {
        this.lnOutQty = lnOutQty;
    }
    public int getLpStockQty() {
        return lpStockQty;
    }
    public void setLpStockQty(int lpStockQty) {
        this.lpStockQty = lpStockQty;
    }
    public int getLnStockQty() {
        return lnStockQty;
    }
    public void setLnStockQty(int lnStockQty) {
        this.lnStockQty = lnStockQty;
    }
}
