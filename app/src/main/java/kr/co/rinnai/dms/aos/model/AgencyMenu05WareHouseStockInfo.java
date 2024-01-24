package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 대리점용 오더 재고(창고별 재고) 현황 조회 결과
 * 서버 VO (ProductStockByWarehouseResultVO)
 */

public class AgencyMenu05WareHouseStockInfo {

    private String modelName;

    private String gas;

    private int total;

    /**
     * 인천창고 재고
     */
    private int inchon = 0;

    /**
     * 가좌창고 재고
     */
    private int gajwa = 0;

    /**
     * 대전창고 재고
     */
    private int daejeon = 0;

    /**
     * 광주창고 재고
     */
    private int gwangju = 0;

    /**
     * 경산창고 재고
     */
    private int gyeongsan = 0;

    /**
     * 부산창고 재고
     */
    private int busan = 0;

    /**
     * 미출고 수량
     */
    private int orderStockQ = 0;

    /**
     * 실 재고 수량
     */
    private int nOutQty = 0;

    private boolean selected;


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

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getInchon() {
        return inchon;
    }
    public void setInchon(int inchon) {
        this.inchon = inchon;
    }
    public int getGajwa() {
        return gajwa;
    }
    public void setGajwa(int gajwa) {
        this.gajwa = gajwa;
    }
    public int getDaejeon() {
        return daejeon;
    }
    public void setDaejeon(int daejeon) {
        this.daejeon = daejeon;
    }
    public int getGwangju() {
        return gwangju;
    }
    public void setGwangju(int gwangju) {
        this.gwangju = gwangju;
    }
    public int getGyeongsan() {
        return gyeongsan;
    }
    public void setGyeongsan(int gyeongsan) {
        this.gyeongsan = gyeongsan;
    }
    public int getBusan() {
        return busan;
    }
    public void setBusan(int busan) {
        this.busan = busan;
    }

    public int getOrderStockQ() {
        return orderStockQ;
    }

    public void setOrderStockQ(int orderStockQ) {
        this.orderStockQ = orderStockQ;
    }

    public int getnOutQty() {
        return nOutQty;
    }

    public void setnOutQty(int nOutQty) {
        this.nOutQty = nOutQty;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
