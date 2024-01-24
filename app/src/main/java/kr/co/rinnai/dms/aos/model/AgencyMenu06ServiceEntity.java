package kr.co.rinnai.dms.aos.model;

import java.sql.Timestamp;

/**
 *
 * @author mini3248
 * 서비스 접수 등록 및 수정
 * 서버 VO (ServiceReceptionRegVO)
 *
 */
public class AgencyMenu06ServiceEntity {

    private String comId;

    private int boardNum;
    private String acceptNo;
    private String custCode;
    private String custName;

    private String hp1;
    private String hp2;
    private String hp3;

    private String emailHead;
    private String emalDomain;

    private String zipCode;

    private String fullAddr;
    private String roadAddr1;
    private String addrDetail;
    private String jibunAddr;
    private String jibunSangse;
    private String roadAddr2;

    private String lCat;
    private String mCat;

    private String modelCode;
    private String modelName;

    private String content;
    private String visitReqDate;
    private String remoteIp;
    private String acceptType;


    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public int getBoardNum() {
        return boardNum;
    }

    public void setBoardNum(int boardNum) {
        this.boardNum = boardNum;
    }

    public String getAcceptNo() {
        return acceptNo;
    }

    public void setAcceptNo(String acceptNo) {
        this.acceptNo = acceptNo;
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

    public String getHp1() {
        return hp1;
    }

    public void setHp1(String hp1) {
        this.hp1 = hp1;
    }

    public String getHp2() {
        return hp2;
    }

    public void setHp2(String hp2) {
        this.hp2 = hp2;
    }

    public String getHp3() {
        return hp3;
    }

    public void setHp3(String hp3) {
        this.hp3 = hp3;
    }

    public String getEmailHead() {
        return emailHead;
    }

    public void setEmailHead(String emailHead) {
        this.emailHead = emailHead;
    }

    public String getEmalDomain() {
        return emalDomain;
    }

    public void setEmalDomain(String emalDomain) {
        this.emalDomain = emalDomain;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getFullAddr() {
        return fullAddr;
    }

    public void setFullAddr(String fullAddr) {
        this.fullAddr = fullAddr;
    }

    public String getRoadAddr1() {
        return roadAddr1;
    }

    public void setRoadAddr1(String roadAddr1) {
        this.roadAddr1 = roadAddr1;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getJibunAddr() {
        return jibunAddr;
    }

    public void setJibunAddr(String jibunAddr) {
        this.jibunAddr = jibunAddr;
    }

    public String getJibunSangse() {
        return jibunSangse;
    }

    public void setJibunSangse(String jibunSangse) {
        this.jibunSangse = jibunSangse;
    }

    public String getRoadAddr2() {
        return roadAddr2;
    }

    public void setRoadAddr2(String roadAddr2) {
        this.roadAddr2 = roadAddr2;
    }

    public String getlCat() {
        return lCat;
    }

    public void setlCat(String lCat) {
        this.lCat = lCat;
    }

    public String getmCat() {
        return mCat;
    }

    public void setmCat(String mCat) {
        this.mCat = mCat;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVisitReqDate() {
        return visitReqDate;
    }

    public void setVisitReqDate(String visitReqDate) {
        this.visitReqDate = visitReqDate;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getAcceptType() {
        return acceptType;
    }

    public void setAcceptType(String acceptType) {
        this.acceptType = acceptType;
    }
}
