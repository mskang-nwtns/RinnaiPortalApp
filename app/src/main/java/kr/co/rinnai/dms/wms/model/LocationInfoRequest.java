package kr.co.rinnai.dms.wms.model;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *
 */
public class LocationInfoRequest{

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String comId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String wareNo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String itemCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String locationNo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String qty;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cellMake;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cellDetail;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String modelName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cellStatus;

    private String userId;

    private int index;

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
        String code = itemCode.trim();
        if(itemCode.length() > 6) {
            code = code.substring(0, 5) + code.substring(code.length() -1, code.length());
        }
        return code;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }


    public String getLocationNo() {
        return locationNo;
    }

    public void setLocationNo(String locationNo) {
        this.locationNo = locationNo;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getCellMake() {

        String date = cellMake;
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
        if(type != null) {
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
        }
        return gasType;

    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCellStatus() {
        return cellStatus;
    }

    public void setCellStatus(String cellStatus) {
        this.cellStatus = cellStatus;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
