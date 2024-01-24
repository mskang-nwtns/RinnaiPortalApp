package kr.co.rinnai.dms.wms.model;

public class LocationInfoQueryVO {
    /**
     * 조회 Query
     */
    private String locationNo = null;
    private String comId = null;
    private String wareNo = null;

    /**
     * 등록, 수정, 삭제 쿼리
     */
    private String itemCode = null;
    private String cellMake = null;
    private String qty = null;
    private String userId = null;
    private String cellDetail = null;
    private String cellNo = null;
    private String cellStatus = null;

    private int index = -1;
    private boolean status = false;
    private String modelName = null;

    public String getLocationNo() {
        return locationNo;
    }
    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }
    public String getComId() {
        return comId;
    }
    public void setComId(String comId) {
        this.comId = comId;
    }
    public String getWareNo() {
        return wareNo;
    }
    public void setWareNo(String wareNo) {
        this.wareNo = wareNo;
    }
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getCellMake() {
        return cellMake;
    }
    public void setCellMake(String cellMake) {
        this.cellMake = cellMake;
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getCellDetail() {
        return cellDetail;
    }
    public void setCellDetail(String cellDetail) {
        this.cellDetail = cellDetail;
    }
    public String getCellNo() {
        return cellNo;
    }
    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }
    public String getCellStatus() {
        return cellStatus;
    }
    public void setCellStatus(String cellStatus) {
        this.cellStatus = cellStatus;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
