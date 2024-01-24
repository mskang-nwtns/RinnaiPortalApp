package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 미출고 조회 결과
 * 서버 VO (UnShippedResultVO)
 *
 */
public class AgencyMenu01ListEntity {

    private String custCode;
    private String custName;
    private String item;
    private String modelNo;
    private String modelName;
    private String c2UpriceCode;
    private String gas;
    private String qty;
    private int nOutQty;
    private Integer nowQty;
    private int c2Uprice;
    private int noutAmt;
    private int supplyAmt;
    private int outQty;
    private String seq;

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
    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }
    public String getModelNo() {
        return modelNo;
    }
    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }
    public String getModelName() {
        return modelName;
    }
    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    public String getC2UpriceCode() {
        return c2UpriceCode;
    }
    public void setC2UpriceCode(String c2UpriceCode) {
        this.c2UpriceCode = c2UpriceCode;
    }
    public String getGas() {
        return gas;
    }
    public void setGas(String gas) {
        this.gas = gas;
    }
    public String getQty() {
        return qty;
    }
    public void setQty(String qty) {
        this.qty = qty;
    }
    public int getnOutQty() {
        return nOutQty;
    }
    public void setnOutQty(int nOutQty) {
        this.nOutQty = nOutQty;
    }
    public Integer getNowQty() {

        return nowQty;
    }
    public void setNowQty(Integer nowQty) {
        this.nowQty = nowQty;
    }
    public int getC2Uprice() {
        return c2Uprice;
    }
    public void setC2Uprice(int c2Uprice) {
        this.c2Uprice = c2Uprice;
    }
    public int getNoutAmt() {
        return noutAmt;
    }
    public void setNoutAmt(int noutAmt) {
        this.noutAmt = noutAmt;
    }
    public int getSupplyAmt() {
        return supplyAmt;
    }
    public void setSupplyAmt(int supplyAmt) {
        this.supplyAmt = supplyAmt;
    }
    public int getOutQty() {
        return outQty;
    }
    public void setOutQty(int outQty) {
        this.outQty = outQty;
    }
    public String getSeq() {
        return seq;
    }
    public void setSeq(String seq) {
        this.seq = seq;
    }
}
