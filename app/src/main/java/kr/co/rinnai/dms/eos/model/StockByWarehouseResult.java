package kr.co.rinnai.dms.eos.model;

public class StockByWarehouseResult {

    private String warehouse;
    private String warehouseName;
    private String warehouseItem;
    private int sortNum;
    private int lng;
    private int lpg;
    private int etc;
    private int total;
    public String getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }
    public String getWarehouseName() {
        return warehouseName;
    }
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    public String getWarehouseItem() {
        return warehouseItem;
    }
    public void setWarehouseItem(String warehouseItem) {
        this.warehouseItem = warehouseItem;
    }
    public int getSortNum() {
        return sortNum;
    }
    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }
    public int getLng() {
        return lng;
    }
    public void setLng(int lng) {
        this.lng = lng;
    }
    public int getLpg() {
        return lpg;
    }
    public void setLpg(int lpg) {
        this.lpg = lpg;
    }
    public int getEtc() {
        return etc;
    }
    public void setEtc(int etc) {
        this.etc = etc;
    }
    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }

}
