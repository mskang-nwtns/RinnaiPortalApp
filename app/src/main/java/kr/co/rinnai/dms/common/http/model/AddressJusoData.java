package kr.co.rinnai.dms.common.http.model;

public class AddressJusoData {

    private String engAddr;

    private String udrtYn;
    private String lnbrMnnm;
    private String roadAddr;


    private String mtYn;


    private String zipNo;
    private String roadAddrPart1;

    private String roadAddrPart2;
    private String jibunAddr;
    private String admCd;
    private String rnMgtSn;
    private String bdMgtSn;
    private String detBdNmList;
    private String bdNm;
    private String bdKdcd;
    private String siNm;

    private String sggNm;
    private String emdNm;
    private String liNm;
    private String rn;
    private String buldMnnm;
    private String buldSlno;
    private String lnbrSlno;
    private String emdNo;



    public String getDetBdNmList() {
        if(this.detBdNmList == null)
            detBdNmList = "";
        return detBdNmList;
    }

    public void setDetBdNmList(String detBdNmList) {
        this.detBdNmList = detBdNmList;
    }

    public String getEngAddr() {
        return engAddr;
    }

    public void setEngAddr(String engAddr) {
        this.engAddr = engAddr;
    }

    public String getRn() {
        if(this.rn == null)
            rn = "";

        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getEmdNm() {

        if(this.emdNm == null)
            emdNm = "";
        return emdNm;
    }

    public void setEmdNm(String emdNm) {
        this.emdNm = emdNm;
    }

    public String getZipNo() {
        if(this.zipNo == null)
            zipNo = "";
        return zipNo;
    }

    public void setZipNo(String zipNo) {
        this.zipNo = zipNo;
    }

    public String getRoadAddrPart2() {
        if(this.roadAddrPart2 == null)
            roadAddrPart2 = "";
        return roadAddrPart2;
    }

    public void setRoadAddrPart2(String roadAddrPart2) {
        this.roadAddrPart2 = roadAddrPart2;
    }

    public String getEmdNo() {
        if(this.emdNo == null)
            emdNo = "";
        return emdNo;
    }

    public void setEmdNo(String emdNo) {
        this.emdNo = emdNo;
    }

    public String getSggNm() {
        if(this.sggNm == null)
            sggNm = "";
        return sggNm;
    }

    public void setSggNm(String sggNm) {
        this.sggNm = sggNm;
    }

    public String getJibunAddr() {

        if(this.jibunAddr == null)
            jibunAddr = "";
        return jibunAddr;
    }

    public void setJibunAddr(String jibunAddr) {
        this.jibunAddr = jibunAddr;
    }

    public String getSiNm() {

        if(this.siNm == null)
            siNm = "";
        return siNm;
    }

    public void setSiNm(String siNm) {
        this.siNm = siNm;
    }

    public String getRoadAddrPart1() {

        if(this.roadAddrPart1 == null)
            roadAddrPart1 = "";
        return roadAddrPart1;
    }

    public void setRoadAddrPart1(String roadAddrPart1) {
        this.roadAddrPart1 = roadAddrPart1;
    }

    public String getBdNm() {

        if(this.bdNm == null)
            bdNm = "";
        return bdNm;
    }

    public void setBdNm(String bdNm) {
        this.bdNm = bdNm;
    }

    public String getAdmCd() {

        if(this.admCd == null)
            admCd = "";
        return admCd;
    }

    public void setAdmCd(String admCd) {
        this.admCd = admCd;
    }

    public String getUdrtYn() {
        if(this.udrtYn == null)
            udrtYn = "";

        return udrtYn;
    }

    public void setUdrtYn(String udrtYn) {
        this.udrtYn = udrtYn;
    }

    public String getLnbrMnnm() {

        if(this.lnbrMnnm == null)
            lnbrMnnm = "";
        return lnbrMnnm;
    }

    public void setLnbrMnnm(String lnbrMnnm) {
        this.lnbrMnnm = lnbrMnnm;
    }

    public String getRoadAddr() {

        if(this.roadAddr == null)
            roadAddr = "";
        return roadAddr;
    }

    public void setRoadAddr(String roadAddr) {
        this.roadAddr = roadAddr;
    }

    public String getLnbrSlno() {
        return lnbrSlno;
    }

    public void setLnbrSlno(String lnbrSlno) {
        this.lnbrSlno = lnbrSlno;
    }

    public String getBuldMnnm() {

        if(this.buldMnnm == null)
            buldMnnm = "";
        return buldMnnm;
    }

    public void setBuldMnnm(String buldMnnm) {
        this.buldMnnm = buldMnnm;
    }

    public String getBdKdcd() {

        if(this.bdKdcd == null)
            bdKdcd = "";
        return bdKdcd;
    }

    public void setBdKdcd(String bdKdcd) {
        this.bdKdcd = bdKdcd;
    }

    public String getLiNm() {
        if(this.liNm == null)
            liNm = "";
        return liNm;
    }

    public void setLiNm(String liNm) {
        this.liNm = liNm;
    }

    public String getRnMgtSn() {

        if(this.rnMgtSn == null)
            rnMgtSn = "";
        return rnMgtSn;
    }

    public void setRnMgtSn(String rnMgtSn) {
        this.rnMgtSn = rnMgtSn;
    }

    public String getMtYn() {
        return mtYn;
    }

    public void setMtYn(String mtYn) {
        this.mtYn = mtYn;
    }

    public String getBdMgtSn() {

        if(this.bdMgtSn == null)
            bdMgtSn = "";
        return bdMgtSn;
    }

    public void setBdMgtSn(String bdMgtSn) {
        this.bdMgtSn = bdMgtSn;
    }

    public String getBuldSlno() {
        if(this.buldSlno == null)
            buldSlno = "";
        return buldSlno;
    }

    public void setBuldSlno(String buldSlno) {


        this.buldSlno = buldSlno;
    }
}
