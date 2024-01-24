package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 주문 현황 조회 쿼리용 VO
 * 서버 VO (OrderInfoResultVO)
 */

public class AgencyMenu02ListEntity {

    private String accText;
    private String ordNo;
    private String compNo;
    private String manNo;
    private String manName;
    private String ordDate;
    private int ordCnt;
    private int ordReceiptCnt;
    private int ordReleaseCnt;
    private int ordCancelCnt;

    public String getAccText() {
        return accText;
    }
    public void setAccText(String accText) {
        this.accText = accText;
    }
    public String getOrdNo() {
        return ordNo;
    }
    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }
    public String getCompNo() {
        return compNo;
    }
    public void setCompNo(String compNo) {
        this.compNo = compNo;
    }
    public String getManNo() {
        return manNo;
    }
    public void setManNo(String manNo) {
        this.manNo = manNo;
    }
    public String getManName() {
        return manName;
    }
    public void setManName(String manName) {
        this.manName = manName;
    }
    public String getOrdDate() {
        return ordDate;
    }
    public void setOrdDate(String ordDate) {
        this.ordDate = ordDate;
    }
    public int getOrdCnt() {
        return ordCnt;
    }
    public void setOrdCnt(int ordCnt) {
        this.ordCnt = ordCnt;
    }
    public int getOrdReceiptCnt() {
        return ordReceiptCnt;
    }
    public void setOrdReceiptCnt(int ordReceiptCnt) {
        this.ordReceiptCnt = ordReceiptCnt;
    }
    public int getOrdReleaseCnt() {
        return ordReleaseCnt;
    }
    public void setOrdReleaseCnt(int ordReleaseCnt) {
        this.ordReleaseCnt = ordReleaseCnt;
    }
    public int getOrdCancelCnt() {
        return ordCancelCnt;
    }
    public void setOrdCancelCnt(int ordCancelCnt) {
        this.ordCancelCnt = ordCancelCnt;
    }
}
