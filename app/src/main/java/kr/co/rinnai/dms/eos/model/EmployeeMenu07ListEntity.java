package kr.co.rinnai.dms.eos.model;

/**
 * 소비자 이중 체크
 * @author mini3248
 * 서버 VO (ConsumerDoubleCheckResultVO)
 */
public class EmployeeMenu07ListEntity {
    private String orderDate;
    private String custCode;
    private String clientName;
    private String addr;
    private String telNo;
    private String telNo2;
    private String orderType;
    private String orderItem;
    private String deliveryGBN;
    private String deliveryName;
    private String modelName;
    private String driverName;
    private String dTel;
    private String carNo;
    private String saleDate;
    private int qty;
    private int nOutQty;
    private String cancelType;
    private String outItemName;
    private String gasType;
    private String gas;
    private String remark;
    private String outReqDate;
    private String outRemark;
    private String warehouseName;
    private String reqRemark;

    private boolean selected = false;
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public String getCustCode() {
        return custCode;
    }
    public void setCustCode(String custCode) {
        this.custCode = custCode;
    }
    public String getClientName() {
        return clientName;
    }
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    public String getAddr() {
        return addr;
    }
    public void setAddr(String addr) {
        this.addr = addr;
    }
    public String getTelNo() {
        return telNo;
    }
    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }
    public String getTelNo2() {
        return telNo2;
    }
    public void setTelNo2(String telNo2) {
        this.telNo2 = telNo2;
    }
    public String getOrderType() {
        return orderType;
    }
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }
    public String getOrderItem() {
        return orderItem;
    }
    public void setOrderItem(String orderItem) {
        this.orderItem = orderItem;
    }
    public String getDeliveryGBN() {
        return deliveryGBN;
    }
    public void setDeliveryGBN(String deliveryGBN) {
        this.deliveryGBN = deliveryGBN;
    }
    public String getDeliveryName() {
        return deliveryName;
    }
    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getDriverName() {
        return driverName;
    }
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public String getdTel() {
        return dTel;
    }
    public void setdTel(String dTel) {
        this.dTel = dTel;
    }
    public String getCarNo() {
        return carNo;
    }
    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }
    public String getSaleDate() {
        return saleDate;
    }
    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }
    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    public int getnOutQty() {
        return nOutQty;
    }
    public void setnOutQty(int nOutQty) {
        this.nOutQty = nOutQty;
    }
    public String getCancelType() {
        return cancelType;
    }
    public void setCancelType(String cancelType) {
        this.cancelType = cancelType;
    }
    public String getOutItemName() {
        return outItemName;
    }
    public void setOutItemName(String outItemName) {
        this.outItemName = outItemName;
    }
    public String getGasType() {
        return gasType;
    }
    public void setGasType(String gasType) {
        this.gasType = gasType;
    }
    public String getGas() {
        return gas;
    }
    public void setGas(String gas) {
        this.gas = gas;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOutReqDate() {
        return outReqDate;
    }
    public void setOutReqDate(String outReqDate) {
        this.outReqDate = outReqDate;
    }
    public String getOutRemark() {
        return outRemark;
    }
    public void setOutRemark(String outRemark) {
        this.outRemark = outRemark;
    }
    public String getWarehouseName() {
        return warehouseName;
    }
    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
    public String getReqRemark() {
        return reqRemark;
    }
    public void setReqRemark(String reqRemark) {
        this.reqRemark = reqRemark;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
