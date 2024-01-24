package kr.co.rinnai.dms.eos.model;

/**
 * 출고 지시 일람표 기능 조희
 * @author mini3248
 * 서버 VO (ForwardOrderResultVO)
 */
public class EmployeeMenu06ListEntity {
    /**
     * 거래처코드
     */
    private String custCode;

    /**
     * 거래처명
     */
    private String custName;
    private String location;
    private String itemType;
    private String itemName;

    /**
     * 대기종명
     */
    private String gGoodsName;
    private String gGoodsCode;

    /**
     * 중기종명
     */
    private String goodsTypeName;
    private String goodType;

    /**
     * 제품코드
     */
    private String itemCode;

    /**
     * 모델코드
     */
    private String modelCode;

    /**
     * 모델명(ListView Item)
     */
    private String modelName;

    /**
     * 가스(ListView Item)
     */
    private String gas;

    /**
     * 단가코드(ListView Item)
     */
    private String upriceCode;

    /**
     * 출고지시번호
     */
    private String outSeq;

    /**
     * 차번
     */
    private String trsNo;
    private String excelTrsNo;

    /**
     * 수량(ListView Item)
     */
    private int qty;

    /**
     * 단가(ListView Item)
     */
    private int uprie;

    /**
     * 금액(ListView Item)
     */
    private int amt;

    /**
     * 오더일자(DetailView Item)
     */
    private String orderDate;

    /**
     * 오더구분(DetailView Item)
     */
    private String orderItemName;
    private String deliveryGbn;
    private String deliveryKor;

    /**
     * 기사명(DetailView Item)
     */
    private String deriverName;

    /**
     * 연락처(DetailView Item)
     */
    private String telNo;

    /**
     * 대부서코드
     */
    private String deptType;

    /**
     * 대부서명
     */
    private String deptTypeName;

    /**
     * 공장구분명
     */
    private String facType;

    /**
     * 중부서코드
     */
    private String mDeptItem;

    /**
     * 중부서명
     */
    private String mDeptFname;
    private String regionName;

    /**
     * 창고명(DetailView Item)
     */
    private String wareHouseName;

    /**
     * 주소
     */
    private String addr;

    /**
     * 비고사항(DetailView Item)
     */
    private String remark;

    /**
     * 도착지코드
     */
    private String destType;

    /**
     * 도착지명
     */
    private String destName;

    private int m3;

    private String orderNo;

    /**
     * 출고일
     */
    private String saleDate;


    private boolean selected = false;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getgGoodsName() {
        return gGoodsName;
    }

    public void setgGoodsName(String gGoodsName) {
        this.gGoodsName = gGoodsName;
    }

    public String getgGoodsCode() {
        return gGoodsCode;
    }

    public void setgGoodsCode(String gGoodsCode) {
        this.gGoodsCode = gGoodsCode;
    }

    public String getGoodsTypeName() {
        return goodsTypeName;
    }

    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }

    public String getGoodType() {
        return goodType;
    }

    public void setGoodType(String goodType) {
        this.goodType = goodType;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
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

    public String getUpriceCode() {
        return upriceCode;
    }

    public void setUpriceCode(String upriceCode) {
        this.upriceCode = upriceCode;
    }

    public String getOutSeq() {
        return outSeq;
    }

    public void setOutSeq(String outSeq) {
        this.outSeq = outSeq;
    }

    public String getTrsNo() {
        return trsNo;
    }

    public void setTrsNo(String trsNo) {
        this.trsNo = trsNo;
    }

    public String getExcelTrsNo() {
        return excelTrsNo;
    }

    public void setExcelTrsNo(String excelTrsNo) {
        this.excelTrsNo = excelTrsNo;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getUprie() {
        return uprie;
    }

    public void setUprie(int uprie) {
        this.uprie = uprie;
    }

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderItemName() {
        return orderItemName;
    }

    public void setOrderItemName(String orderItemName) {
        this.orderItemName = orderItemName;
    }

    public String getDeliveryGbn() {
        return deliveryGbn;
    }

    public void setDeliveryGbn(String deliveryGbn) {
        this.deliveryGbn = deliveryGbn;
    }

    public String getDeliveryKor() {
        return deliveryKor;
    }

    public void setDeliveryKor(String deliveryKor) {
        this.deliveryKor = deliveryKor;
    }

    public String getDeriverName() {
        return deriverName;
    }

    public void setDeriverName(String deriverName) {
        this.deriverName = deriverName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }

    public String getDeptType() {
        return deptType;
    }

    public void setDeptType(String deptType) {
        this.deptType = deptType;
    }

    public String getDeptTypeName() {
        return deptTypeName;
    }

    public void setDeptTypeName(String deptTypeName) {
        this.deptTypeName = deptTypeName;
    }

    public String getFacType() {
        return facType;
    }

    public void setFacType(String facType) {
        this.facType = facType;
    }

    public String getmDeptItem() {
        return mDeptItem;
    }

    public void setmDeptItem(String mDeptItem) {
        this.mDeptItem = mDeptItem;
    }

    public String getmDeptFname() {
        return mDeptFname;
    }

    public void setmDeptFname(String mDeptFname) {
        this.mDeptFname = mDeptFname;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getWareHouseName() {
        return wareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        this.wareHouseName = wareHouseName;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDestType() {
        return destType;
    }

    public void setDestType(String destType) {
        this.destType = destType;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public int getM3() {
        return m3;
    }

    public void setM3(int m3) {
        this.m3 = m3;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(String saleDate) {
        this.saleDate = saleDate;
    }
}
