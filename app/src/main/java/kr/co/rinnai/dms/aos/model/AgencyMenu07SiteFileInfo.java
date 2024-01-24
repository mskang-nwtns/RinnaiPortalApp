package kr.co.rinnai.dms.aos.model;

/**
 *
 * @author mini3248
 * 신축현장 모델 조회
 * 서버 VO (SiteModeInfoResultVO)
 *
 */
public class AgencyMenu07SiteFileInfo {

    /**
     * 시스템 구분 : S
     */
    private String sysItem;
    /**
     * 업무별 순번
     */
    private int fSeqNum;
    /**
     * 서브 순번
     */
    private int sCount;
    /**
     * 회사 구분 : R
     */
    private String comId;
    /**
     * 변경일시
     */
    private String opDate;
    /**
     * 변경자
     */
    private String operator;
    /**
     * 개발화면 Form명 : FS1_BUY_070I
     */
    private String objectId;
    /**
     * 제목
     */
    private String title;
    /**
     * 파일 경로
     */
    private String filePath;
    /**
     * 파일명
     */
    private String fileName;
    public String getSysItem() {
        return sysItem;
    }
    public void setSysItem(String sysItem) {
        this.sysItem = sysItem;
    }
    public int getfSeqNum() {
        return fSeqNum;
    }
    public void setfSeqNum(int fSeqNum) {
        this.fSeqNum = fSeqNum;
    }
    public int getsCount() {
        return sCount;
    }
    public void setsCount(int sCount) {
        this.sCount = sCount;
    }
    public String getComId() {
        return comId;
    }
    public void setComId(String comId) {
        this.comId = comId;
    }
    public String getOpDate() {
        return opDate;
    }
    public void setOpDate(String opDate) {
        this.opDate = opDate;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getObjectId() {
        return objectId;
    }
    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
