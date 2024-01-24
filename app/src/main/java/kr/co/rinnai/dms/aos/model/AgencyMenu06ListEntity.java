package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 서비스접수현황
 * 서버 VO (ServiceReceptionResultVO)
 *
 */
public class AgencyMenu06ListEntity {

    private int boardNum;
    private String cpDate;
    private String cpCustNo;
    private String acceptNo;
    private String custId;
    private String custName;
    private String gubun;
    private String visitReqDate;
    private String processState;
    public int getBoardNum() {
        return boardNum;
    }
    public void setBoardNum(int boardNum) {
        this.boardNum = boardNum;
    }
    public String getCpDate() {
        return cpDate;
    }
    public void setCpDate(String cpDate) {
        this.cpDate = cpDate;
    }
    public String getCpCustNo() {
        return cpCustNo;
    }
    public void setCpCustNo(String cpCustNo) {
        this.cpCustNo = cpCustNo;
    }
    public String getAcceptNo() {
        return acceptNo;
    }
    public void setAcceptNo(String acceptNo) {
        this.acceptNo = acceptNo;
    }
    public String getCustId() {
        return custId;
    }
    public void setCustId(String custId) {
        this.custId = custId;
    }
    public String getCustName() {
        return custName;
    }
    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getGubun() {
        return gubun;
    }
    public void setGubun(String gubun) {
        this.gubun = gubun;
    }
    public String getVisitReqDate() {
        return visitReqDate;
    }
    public void setVisitReqDate(String visitReqDate) {
        this.visitReqDate = visitReqDate;
    }
    public String getProcessState() {
        return processState;
    }
    public void setProcessState(String processState) {
        this.processState = processState;
    }
}
