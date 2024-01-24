package kr.co.rinnai.dms.wms.model;

/**
 *
 * @author mini3248
 * 2,3 공장 제품 이동 시 상단 창고 목록 Entity
 * 서버 VO (WarehouseResultVO)
 *
 */

public class WmsMenu10SpinnerListEntity {
    private int sortNum;
    private String warehouse;
    private String warehouseName;

    public int getSortNum() {
        return sortNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

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

}
