package kr.co.rinnai.dms.wms.model;

/**
 *
 * @author mini3248
 * Picking 지시서 Location Picking 완료 후 서버 전송 Result
 * 서버 VO (PickingResult)
 *
 */

public class WmsMenu03ReadingListEntity {

    private int index;
    private String inSaleDate;
    private String inComId;
    private String inWareHouse;
    private String inCarId;
    private String inTrsNo;
    private String inIoItemSeq;
    private String inPdaJobQty;
    private String inPdaJobNo;
    private String inModelCode;
    private String inOrderDetail;
    /**
     * 로케이션 위치
     */
    private String inCellNo;
    private boolean status;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getInSaleDate() {
        return inSaleDate;
    }
    public void setInSaleDate(String inSaleDate) {
        this.inSaleDate = inSaleDate;
    }
    public String getInComId() {
        return inComId;
    }
    public void setInComId(String inComId) {
        this.inComId = inComId;
    }
    public String getInWareHouse() {
        return inWareHouse;
    }
    public void setInWareHouse(String inWareHouse) {
        this.inWareHouse = inWareHouse;
    }
    public String getInCarId() {
        return inCarId;
    }
    public void setInCarId(String inCarId) {
        this.inCarId = inCarId;
    }
    public String getInTrsNo() {
        return inTrsNo;
    }
    public void setInTrsNo(String inTrsNo) {
        this.inTrsNo = inTrsNo;
    }
    public String getInIoItemSeq() {
        return inIoItemSeq;
    }
    public void setInIoItemSeq(String inIoItemSeq) {
        this.inIoItemSeq = inIoItemSeq;
    }
    public String getInPdaJobQty() {
        return inPdaJobQty;
    }
    public void setInPdaJobQty(String inPdaJobQty) {
        this.inPdaJobQty = inPdaJobQty;
    }
    public String getInPdaJobNo() {
        return inPdaJobNo;
    }
    public void setInPdaJobNo(String inPdaJobNo) {
        this.inPdaJobNo = inPdaJobNo;
    }
    public String getInModelCode() {
        return inModelCode;
    }
    public void setInModelCode(String inModelCode) {
        this.inModelCode = inModelCode;
    }
    public String getInOrderDetail() {
        return inOrderDetail;
    }
    public void setInOrderDetail(String inOrderDetail) {
        this.inOrderDetail = inOrderDetail;
    }
    public String getInCellNo() {
        return inCellNo;
    }
    public void setInCellNo(String inCellNo) {
        this.inCellNo = inCellNo;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean isStatus() {
        return status;
    }
}
