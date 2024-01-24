package kr.co.rinnai.dms.wms.model;

public class AgencyBarcodeResult {


    /**
     * 모델 코드 번호
     */
    private String itemCode;
    /**
     * 모델 이름
     */
    private String modelName;
    /**
     * 가스 종류 (ETC, LPG, LNG)
     */
    private String type;
    /**
     * 지시 수량
     */
    private String instructions;

    /**
     *  작업 수량
     */
    private String operations;

    /**
     * 대리점 코드 번호
     */
    private String custCode;

    /**
     * 대리점 명
     */
    private String custName;

    /**
     * 제품 바코드(보증번호)
     */
    private String barcode;

    private boolean selected = false;

    private String agencyOrderBarcode;

    private String remark;

    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getModelName() {
        return modelName.trim();
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getInstructions() {
        return instructions;
    }
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
    public String getOperations() {
        return operations;
    }
    public void setOperations(String operations) {
        this.operations = operations;
    }
    public String getCustCode() {
        return custCode;
    }
    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAgencyOrderBarcode() {
        return agencyOrderBarcode;
    }

    public void setAgencyOrderBarcode(String agencyOrderBarcode) {
        this.agencyOrderBarcode = agencyOrderBarcode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {

        return String.format("[대리점명(custName)] : %s, [대리점 코드(custCode)] : %s, [제품명(modelName)] : %s, [제품코드(itemCode)] : %s,  [가스종류(type)] : %s, [출하지시수량(instructions)] : %s, [출하작업수량(operations)] : %s ", this.custName, this.custCode , this.modelName, this.itemCode,  this.type, this.instructions, this.operations );
    }
}
