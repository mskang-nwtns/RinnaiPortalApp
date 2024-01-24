package kr.co.rinnai.dms.eos.model;

public class StockByOrderableResult {

    private String goodsType;

    /**
     * 중 기종명
     */
    private String goodsTypeName;
    private String item;

    /**
     * 모델 명
     */
    private String modelName;

    /**
     * 가스 종류
     */
    private String gas;

    /**
     * 생산패턴
     */
    private String orderPrdType;


    /**
     * 주문 가능 재고
     */
    private int orderStockQ;

    /**
     * 전국 실 재고
     */
    private int allRStockQ;

    /**
     * 지방 실 재고
     */
    private int localRStockQ;

    /**
     * 이동중 재고
     */
    private int moveRStockQ;

    /**
     * 미 출고
     */
    private int nOutQ;

    /**
     * 금일 출고
     */
    private int thisOutQ;

    /**
     *판매 누계
     */
    private int salSumQ;

    private boolean selected;

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
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

    public String getOrderPrdType() {
        return orderPrdType;
    }

    public void setOrderPrdType(String orderPrdType) {
        this.orderPrdType = orderPrdType;
    }

    public int getOrderStockQ() {
        return orderStockQ;
    }

    public void setOrderStockQ(int orderStockQ) {
        this.orderStockQ = orderStockQ;
    }

    public int getAllRStockQ() {
        return allRStockQ;
    }

    public void setAllRStockQ(int allRStockQ) {
        this.allRStockQ = allRStockQ;
    }

    public int getLocalRStockQ() {
        return localRStockQ;
    }

    public void setLocalRStockQ(int localRStockQ) {
        this.localRStockQ = localRStockQ;
    }

    public int getMoveRStockQ() {
        return moveRStockQ;
    }

    public void setMoveRStockQ(int moveRStockQ) {
        this.moveRStockQ = moveRStockQ;
    }

    public int getnOutQ() {
        return nOutQ;
    }

    public void setnOutQ(int nOutQ) {
        this.nOutQ = nOutQ;
    }

    public int getThisOutQ() {
        return thisOutQ;
    }

    public void setThisOutQ(int thisOutQ) {
        this.thisOutQ = thisOutQ;
    }

    public int getSalSumQ() {
        return salSumQ;
    }

    public void setSalSumQ(int salSumQ) {
        this.salSumQ = salSumQ;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
