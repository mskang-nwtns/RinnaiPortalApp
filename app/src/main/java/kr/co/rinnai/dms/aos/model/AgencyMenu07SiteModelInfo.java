package kr.co.rinnai.dms.aos.model;

import kr.co.rinnai.dms.common.http.model.AddressJusoData;

/**
 *
 * @author mini3248
 * 신축현장 모델 조회
 * 서버 VO (SiteModeInfoResultVO)
 *
 */
public class AgencyMenu07SiteModelInfo {

    /**
     * 현장등록일자
     */
    private String siteRegdate;
    /**
     * 순번
     */
    private String siteNum;

    /**
     * 모델순번
     */
    private int modelIdx;

    private String comId;
    private String modelCode;
    private String modelName;
    private String goodsCode;
    private String gasType;
    private String itemType;
    private String gGoodsType;
    private String gGoodsTypeName;
    private String goodsType;
    private String goodsTypeName;
    private String gasName;
    private int qty;
    private boolean selected = false;


    public String getSiteRegdate() {
        return siteRegdate;
    }

    public void setSiteRegdate(String siteRegdate) {
        this.siteRegdate = siteRegdate;
    }

    public String getSiteNum() {
        return siteNum;
    }

    public void setSiteNum(String siteNum) {
        this.siteNum = siteNum;
    }

    public int getModelIdx() {
        return modelIdx;
    }

    public void setModelIdx(int modelIdx) {
        this.modelIdx = modelIdx;
    }

    public String getComId() {
        return comId;
    }
    public void setComId(String comId) {
        this.comId = comId;
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
    public String getGoodsCode() {
        return goodsCode;
    }
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
    public String getGasType() {
        return gasType;
    }
    public void setGasType(String gasType) {
        this.gasType = gasType;
    }
    public String getItemType() {
        return itemType;
    }
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public String getgGoodsType() {
        return gGoodsType;
    }
    public void setgGoodsType(String gGoodsType) {
        this.gGoodsType = gGoodsType;
    }

    public String getgGoodsTypeName() {
        return gGoodsTypeName;
    }

    public void setgGoodsTypeName(String gGoodsTypeName) {
        this.gGoodsTypeName = gGoodsTypeName;
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
    public String getGasName() {
        return gasName;
    }
    public void setGasName(String gasName) {
        this.gasName = gasName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
