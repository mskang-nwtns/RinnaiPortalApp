package kr.co.rinnai.dms.eos.model;

/**
 *
 * @author mini3248
 * 영업용 제품 정보 조회
 */
public class SalesModelInfoResult {

    private String comId;
    private String modelCode;
    private String modelName;
    private String goodsType;
    private String goodsTypeName;
    /**
     *  -- 특소세
     */
    private String sConSum;
    /**
     *  -- 기준가
     */
    private String stdUprice;
    /**
     * -- 출고가
     */
    private String outUprice;
    /**
     *  --소비자가
     */
    private String custUprice;
    /**
     *  -- 체류구분
     */
    private String stayItem;
    /**
     *  -- 생산종료일자
     */
    private String prodEdate;
    /**
     *  -- 판매유형
     */
    private String saleType;
    private String codeName;

    private boolean selected = false;


    public String getComId() {
        return comId;
    }
    public void setComId(String comId) {
        this.comId = comId;
    }
    public String getModelCode() {
        return this.modelCode;
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
    public String getGoodsType() {
        return goodsType;
    }
    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
    public String getGoodsTypeName() {
        return goodsTypeName;
    }
    public void setGoodsTypeName(String goodsTypeName) {
        this.goodsTypeName = goodsTypeName;
    }
    public String getsConSum() {
        return sConSum;
    }
    public void setsConSum(String sConSum) {
        this.sConSum = sConSum;
    }
    public String getStdUprice() {
        return stdUprice;
    }
    public void setStdUprice(String stdUprice) {
        this.stdUprice = stdUprice;
    }
    public String getOutUprice() {
        return outUprice;
    }
    public void setOutUprice(String outUprice) {
        this.outUprice = outUprice;
    }
    public String getCustUprice() {
        return custUprice;
    }
    public void setCustUprice(String custUprice) {
        this.custUprice = custUprice;
    }
    public String getStayItem() {
        return stayItem;
    }
    public void setStayItem(String stayItem) {
        this.stayItem = stayItem;
    }
    public String getProdEdate() {
        return prodEdate;
    }
    public void setProdEdate(String prodEdate) {
        this.prodEdate = prodEdate;
    }
    public String getSaleType() {
        return saleType;
    }
    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }
    public String getCodeName() {
        return codeName;
    }
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

