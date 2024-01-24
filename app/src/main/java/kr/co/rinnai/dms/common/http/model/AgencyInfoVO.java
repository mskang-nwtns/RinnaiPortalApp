package kr.co.rinnai.dms.common.http.model;


/**
 * 양판점 정보(양판점 명, 전화번호)
 * @author kimsangmin
 *
 */
public class AgencyInfoVO {

    /**
     * 양판점 명
     */
    private String siteName;

    /**
     * 양판점 전화번호
     */
    private String telNo;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getTelNo() {
        return telNo;
    }

    public void setTelNo(String telNo) {
        this.telNo = telNo;
    }



}
