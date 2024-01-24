package kr.co.rinnai.dms.wms.model;

public class LocationInfoResult {

    private String itemCode = null;
    private String cellNo = null;
    private String qty = null;
    /**
     *  // WmsMenu09Activity remark 대신 사용;
     */
    private String cellMake = null;
    //
    /**
     * WmsMenu09Activity 피킹지시서 번호 대신 사용;
     */
    private String cellDetail = null;
    private String modelName = null;
    private String type = null;
    private String total = null;
    private boolean selected = false;

    private String barcode;
    private String boxCount;

    public LocationInfoResult(){

    }

    public LocationInfoResult(LocationInfoQueryVO queryVO) {
        this.itemCode = queryVO.getItemCode();
        this.cellNo = queryVO.getLocationNo();
        this.qty = queryVO.getQty();
        this.cellMake = queryVO.getCellMake();
        this.cellDetail = queryVO.getCellDetail();
        this.type = queryVO.getItemCode().substring(5, 6);
        this.modelName = queryVO.getModelName();

    }
    public String getItemCode() {
        String code = itemCode.trim();
        if(itemCode.length() > 6) {
            code = code.substring(0, 5) + code.substring(code.length() -1, code.length());
        }
        return code;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getOriginalCellNo() {
        return this.cellNo;
    }
    public String getOriginalCellMake() {
        return this.cellMake;
    }
    public String getOriginalItemCode() {
        return this.itemCode;
    }
    public String getCellNo() {

        String locationNo = cellNo;
        if(!"".equals(cellNo) && cellNo != null) {
            locationNo = String.format("%s-%s-%s", cellNo.substring(0, 2), cellNo.substring(2, 5), cellNo.substring(5, 6));
        }
        return locationNo;
    }

    public void setCellNo(String cellNo) {
        this.cellNo = cellNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCellMake() {

        String date = cellMake;

        if(!"".equals(cellMake) && cellMake != null) {
            date = String.format("%s.%s", cellMake.substring(0, 2), cellMake.substring(2, 4));
        }
        return date;
    }

    public void setCellMake(String cellMake) {
        this.cellMake = cellMake;
    }

    public String getCellDetail() {
        return cellDetail;
    }

    public void setCellDetail(String cellDetail) {
        this.cellDetail = cellDetail;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getType() {
        String gasType = null;
        switch (type) {
            case "0":
                gasType = "기타";
                break;
            case "1":
                gasType = "LPG";
                break;
            case "2":
                gasType = "LNG";
                break;
        }
        return gasType;

    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBoxCount() {
        return boxCount;
    }

    public void setBoxCount(String boxCount) {
        this.boxCount = boxCount;
    }


}
