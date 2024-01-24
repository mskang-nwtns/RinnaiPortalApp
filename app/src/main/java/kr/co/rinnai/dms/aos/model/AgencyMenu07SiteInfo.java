package kr.co.rinnai.dms.aos.model;

import android.os.Parcel;
import android.os.Parcelable;

import kr.co.rinnai.dms.common.http.model.AddressJusoData;

/**
 *
 * @author mini3248
 * 신축현장 정보
 * 서버 VO (SiteInfoVO)
 *
 */
public class AgencyMenu07SiteInfo implements Parcelable {

    /**
     * 현장등록일자
     */
    private String siteRegdate;
    /**
     * 순번
     */
    private String siteNum;
    /**
     * 회사구분 : R
     */
    private String comId;

    /**
     * 현장명
     */
    private String siteName;
    /**
     * 현장주소_도로명주소(참고제외)
     */
    private String siteRoadaddrpart1;
    /**
     * 현장주소_도로명주소 참고항목
     */
    private String siteRoadaddrpart2;
    /**
     * 현장주소_고객입력상세
     */
    private String siteDetaddr;
    /**
     * 현장주소_지번주소
     */
    private String siteJibunaddr;
    /**
     * 현장주소_우편번호
     */
    private String siteZipcode;
    /**
     * 현장주소_행정구역코드
     */
    private String siteAdmcd;
    /**
     * 현장주소_도로명코드
     */
    private String siteRoadcode;
    /**
     * 현장주소_건물관리번호
     */
    private String siteBuildingcode;
    /**
     * 현장주소_상세건물명
     */
    private String siteBuildingdetname;
    /**
     * 현장주소_법정동/리코드
     */
    private String siteBcode;
    /**
     * 현장주소_읍면동 일련번호
     */
    private String siteEmdno;
    /**
     * 현장주소_건물명
     */
    private String siteBuildingname;
    /**
     * 현장주소_공동주택여부
     */
    private String siteApthouseyn;

    /**
     * 현장주소_시도
     */
    private String siteSido;
    /**
     * 현장주소_구군
     */
    private String siteGugun;
    /**
     * 현장주소_읍면동
     */
    private String siteDong;
    /**
     * 현장주소_법정동/리명
     */
    private String siteBname;
    /**
     * 현장주소_법정동/리의읍면
     */
    private String siteBname1;
    /**
     * 현장주소_도로명
     */
    private String siteRn;
    /**
     * 현장주소_행정동명
     */
    private String siteHname;
    /**
     * 현장주소_건물본번
     */
    private String siteBuldmnnm;
    /**
     * 현장주소_건물부번
     */
    private String siteBuldslno;
    /**
     * 현장주소_지번본번
     */
    private String siteInbrmnnm;
    /**
     * 현장주소_지번부번
     */
    private String siteInbrslno;
    /**
     * 분양형태코드
     */
    private String saleTypecode;
    /**
     * 건물형태코드
     */
    private String buildingTypecode;
    /**
     * 난방형태코드 : A1 : 개별난방
     */
    private String heatTypecode;
    /**
     * 납품업체
     */
    private String saleCust;
    /**
     * 설치업체
     */
    private String setCust;
    /**
     * 납품년월
     */
    private String saleYm;

    /**
     * 준공년월
     */
    private String completionYm;
    /**
     * 동수
     */
    private int aptBuildingCnt;
    /**
     * 총세대수
     */
    private int totHouseholdCnt;
    /**
     * 관리사무소연락처
     */
    private String mngofficeTel;
    /**
     * 시공사
     */
    private String builder;
    /**
     * 대리점코드
     */
    private String custCode;
    /**
     * 대리점/거래처명
     */
    private String custName;
    /**
     * 담당부서코드(영업전용)
     */
    private String deptItem;
    /**
     * 담당부서
     */
    private String deptName;

    /**
     * 담당자 사번
     */
    private String empNo;
    /**
     * 담당자
     */
    private String empName;

    /**
     * 전용면적코드
     */
    private String areaUsesizecode;
    /**
     * 특기사항
     */
    private String specialNote;
    /**
     * 수정자
     */
    private String operator;
    /**
     * 수정일자
     */
    private String opDate;
    /**
     * 등록자
     */
    private String cOperator;
    /**
     * 등록일자
     */
    private String cpDate;

    /**
     * 첨부파일IDX
     */
    private int fSeqnum;
    /**
     * 등록자구분코드
     */
    private String cRegGubun;

    private boolean file1Change = false;
    private boolean file2Change = false;


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
    public String getComId() {
        return comId;
    }
    public void setComId(String comId) {
        this.comId = comId;
    }
    public String getSiteName() {
        return siteName;
    }
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    public String getSiteRoadaddrpart1() {
        return siteRoadaddrpart1;
    }
    public void setSiteRoadaddrpart1(String siteRoadaddrpart1) {
        this.siteRoadaddrpart1 = siteRoadaddrpart1;
    }
    public String getSiteRoadaddrpart2() {
        return siteRoadaddrpart2;
    }
    public void setSiteRoadaddrpart2(String siteRoadaddrpart2) {
        this.siteRoadaddrpart2 = siteRoadaddrpart2;
    }
    public String getSiteDetaddr() {
        return siteDetaddr;
    }
    public void setSiteDetaddr(String siteDetaddr) {
        this.siteDetaddr = siteDetaddr;
    }
    public String getSiteJibunaddr() {
        return siteJibunaddr;
    }
    public void setSiteJibunaddr(String siteJibunaddr) {
        this.siteJibunaddr = siteJibunaddr;
    }
    public String getSiteZipcode() {
        return siteZipcode;
    }
    public void setSiteZipcode(String siteZipcode) {
        this.siteZipcode = siteZipcode;
    }
    public String getSiteAdmcd() {
        return siteAdmcd;
    }
    public void setSiteAdmcd(String siteAdmcd) {
        this.siteAdmcd = siteAdmcd;
    }
    public String getSiteRoadcode() {
        return siteRoadcode;
    }
    public void setSiteRoadcode(String siteRoadcode) {
        this.siteRoadcode = siteRoadcode;
    }
    public String getSiteBuildingcode() {
        return siteBuildingcode;
    }
    public void setSiteBuildingcode(String siteBuildingcode) {
        this.siteBuildingcode = siteBuildingcode;
    }
    public String getSiteBuildingdetname() {
        return siteBuildingdetname;
    }
    public void setSiteBuildingdetname(String siteBuildingdetname) {
        this.siteBuildingdetname = siteBuildingdetname;
    }
    public String getSiteBcode() {
        return siteBcode;
    }
    public void setSiteBcode(String siteBcode) {
        this.siteBcode = siteBcode;
    }
    public String getSiteEmdno() {
        return siteEmdno;
    }
    public void setSiteEmdno(String siteEmdno) {
        this.siteEmdno = siteEmdno;
    }
    public String getSiteBuildingname() {
        return siteBuildingname;
    }
    public void setSiteBuildingname(String siteBuildingname) {
        this.siteBuildingname = siteBuildingname;
    }
    public String getSiteApthouseyn() {
        return siteApthouseyn;
    }
    public void setSiteApthouseyn(String siteApthouseyn) {
        this.siteApthouseyn = siteApthouseyn;
    }
    public String getSiteSido() {
        return siteSido;
    }
    public void setSiteSido(String siteSido) {
        this.siteSido = siteSido;
    }
    public String getSiteGugun() {
        return siteGugun;
    }
    public void setSiteGugun(String siteGugun) {
        this.siteGugun = siteGugun;
    }
    public String getSiteDong() {
        return siteDong;
    }
    public void setSiteDong(String siteDong) {
        this.siteDong = siteDong;
    }
    public String getSiteBname() {
        return siteBname;
    }
    public void setSiteBname(String siteBname) {
        this.siteBname = siteBname;
    }
    public String getSiteBname1() {
        return siteBname1;
    }
    public void setSiteBname1(String siteBname1) {
        this.siteBname1 = siteBname1;
    }
    public String getSiteRn() {
        return siteRn;
    }
    public void setSiteRn(String siteRn) {
        this.siteRn = siteRn;
    }
    public String getSiteHname() {
        return siteHname;
    }
    public void setSiteHname(String siteHname) {
        this.siteHname = siteHname;
    }
    public String getSiteBuldmnnm() {
        return siteBuldmnnm;
    }
    public void setSiteBuldmnnm(String siteBuldmnnm) {
        this.siteBuldmnnm = siteBuldmnnm;
    }
    public String getSiteBuldslno() {
        return siteBuldslno;
    }
    public void setSiteBuldslno(String siteBuldslno) {
        this.siteBuldslno = siteBuldslno;
    }
    public String getSiteInbrmnnm() {
        return siteInbrmnnm;
    }
    public void setSiteInbrmnnm(String siteInbrmnnm) {
        this.siteInbrmnnm = siteInbrmnnm;
    }
    public String getSiteInbrslno() {
        return siteInbrslno;
    }
    public void setSiteInbrslno(String siteInbrslno) {
        this.siteInbrslno = siteInbrslno;
    }
    public String getSaleTypecode() {
        return saleTypecode;
    }
    public void setSaleTypecode(String saleTypecode) {
        this.saleTypecode = saleTypecode;
    }
    public String getBuildingTypecode() {
        return buildingTypecode;
    }
    public void setBuildingTypecode(String buildingTypecode) {
        this.buildingTypecode = buildingTypecode;
    }
    public String getHeatTypecode() {
        return heatTypecode;
    }
    public void setHeatTypecode(String heatTypecode) {
        this.heatTypecode = heatTypecode;
    }
    public String getSaleCust() {
        return saleCust;
    }
    public void setSaleCust(String saleCust) {
        this.saleCust = saleCust;
    }
    public String getSetCust() {
        return setCust;
    }
    public void setSetCust(String setCust) {
        this.setCust = setCust;
    }
    public String getSaleYm() {
        return saleYm;
    }
    public void setSaleYm(String saleYm) {
        this.saleYm = saleYm;
    }
    public String getCompletionYm() {
        return completionYm;
    }
    public void setCompletionYm(String completionYm) {
        this.completionYm = completionYm;
    }
    public int getAptBuildingCnt() {
        return aptBuildingCnt;
    }
    public void setAptBuildingCnt(int aptBuildingCnt) {
        this.aptBuildingCnt = aptBuildingCnt;
    }
    public int getTotHouseholdCnt() {
        return totHouseholdCnt;
    }
    public void setTotHouseholdCnt(int totHouseholdCnt) {
        this.totHouseholdCnt = totHouseholdCnt;
    }
    public String getMngofficeTel() {
        return mngofficeTel;
    }
    public void setMngofficeTel(String mngofficeTel) {
        this.mngofficeTel = mngofficeTel;
    }
    public String getBuilder() {
        return builder;
    }
    public void setBuilder(String builder) {
        this.builder = builder;
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
    public String getDeptItem() {
        return deptItem;
    }
    public void setDeptItem(String deptItem) {
        this.deptItem = deptItem;
    }
    public String getDeptName() {
        return deptName;
    }
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    public String getEmpNo() {
        return empNo;
    }
    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }
    public String getEmpName() {
        return empName;
    }
    public void setEmpName(String empName) {
        this.empName = empName;
    }
    public String getAreaUsesizecode() {
        return areaUsesizecode;
    }
    public void setAreaUsesizecode(String areaUsesizecode) {
        this.areaUsesizecode = areaUsesizecode;
    }
    public String getSpecialNote() {
        return specialNote;
    }
    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }
    public String getOperator() {
        return operator;
    }
    public void setOperator(String operator) {
        this.operator = operator;
    }
    public String getOpDate() {
        return opDate;
    }
    public void setOpDate(String opDate) {
        this.opDate = opDate;
    }
    public String getcOperator() {
        return cOperator;
    }
    public void setcOperator(String cOperator) {
        this.cOperator = cOperator;
    }
    public String getCpDate() {
        return cpDate;
    }
    public void setCpDate(String cpDate) {
        this.cpDate = cpDate;
    }
    public int getfSeqnum() {
        return fSeqnum;
    }
    public void setfSeqnum(int fSeqnum) {
        this.fSeqnum = fSeqnum;
    }
    public String getcRegGubun() {
        return cRegGubun;
    }
    public void setcRegGubun(String cRegGubun) {
        this.cRegGubun = cRegGubun;
    }

    public boolean isFile1Change() {
        return file1Change;
    }

    public void setFile1Change(boolean file1Change) {
        this.file1Change = file1Change;
    }

    public boolean isFile2Change() {
        return file2Change;
    }

    public void setFile2Change(boolean file2Change) {
        this.file2Change = file2Change;
    }

    public void setAddressInfo(AddressJusoData juso) {

        this.siteZipcode = juso.getZipNo();
        this.siteRoadaddrpart1 = juso.getRoadAddrPart1();
        this.siteRoadaddrpart2 = juso.getRoadAddrPart2();

        this.siteJibunaddr = juso.getJibunAddr();
        this.siteAdmcd = juso.getAdmCd();
        this.siteRoadcode = juso.getRnMgtSn();
        this.siteBuildingcode = juso.getBdMgtSn();
        this.siteBuildingdetname = juso.getDetBdNmList();
        this.siteBuildingname = juso.getBdNm();
        this.siteApthouseyn = juso.getBdKdcd();
        this.siteSido = juso.getSiNm();
        this.siteGugun = juso.getSggNm();
        this.siteDong = juso.getEmdNm();
        this.siteBname = juso.getLiNm();
        this.siteBname1 = juso.getLiNm();
        this.siteRn = juso.getRn();
        this.siteBuldmnnm = juso.getBuldMnnm();
        this.siteBuldslno = juso.getBuldSlno();
        this.siteInbrmnnm = juso.getLnbrMnnm();
        this.siteEmdno = juso.getEmdNo();

    }


    @Override
    public int describeContents() {
        return 0;
    }


    // create Parcelable
    public static final Parcelable.Creator<AgencyMenu07SiteInfo> CREATOR = new Parcelable.Creator<AgencyMenu07SiteInfo>() {
        @Override
        public AgencyMenu07SiteInfo createFromParcel(Parcel parcel) {
            return new AgencyMenu07SiteInfo(parcel);
        }
        @Override
        public AgencyMenu07SiteInfo[] newArray(int size) {
            return new AgencyMenu07SiteInfo[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.siteRegdate);
        dest.writeString(this.siteNum);
        dest.writeString(this.comId);
        dest.writeString(this.siteName);
        dest.writeString(this.siteRoadaddrpart1);
        dest.writeString(this.siteRoadaddrpart2);
        dest.writeString(this.siteDetaddr);
        dest.writeString(this.siteJibunaddr);
        dest.writeString(this.siteZipcode);
        dest.writeString(this.siteAdmcd);
        dest.writeString(this.siteRoadcode);
        dest.writeString(this.siteBuildingcode);
        dest.writeString(this.siteBuildingdetname);
        dest.writeString(this.siteBcode);
        dest.writeString(this.siteEmdno);
        dest.writeString(this.siteBuildingname);
        dest.writeString(this.siteApthouseyn);
        dest.writeString(this.siteSido);
        dest.writeString(this.siteGugun);
        dest.writeString(this.siteDong);
        dest.writeString(this.siteBname);
        dest.writeString(this.siteBname1);
        dest.writeString(this.siteRn);
        dest.writeString(this.siteHname);
        dest.writeString(this.siteBuldmnnm);
        dest.writeString(this.siteBuldslno);
        dest.writeString(this.siteInbrmnnm);
        dest.writeString(this.siteInbrslno);
        dest.writeString(this.saleTypecode);
        dest.writeString(this.buildingTypecode);
        dest.writeString(this.heatTypecode);
        dest.writeString(this.saleCust);
        dest.writeString(this.setCust);
        dest.writeString(this.saleYm);
        dest.writeString(this.completionYm);
        dest.writeInt(this.aptBuildingCnt);
        dest.writeInt(this.totHouseholdCnt);
        dest.writeString(this.mngofficeTel);
        dest.writeString(this.builder);
        dest.writeString(this.custCode);
        dest.writeString(this.custName);
        dest.writeString(this.deptItem);
        dest.writeString(this.deptName);
        dest.writeString(this.empNo);
        dest.writeString(this.empName);
        dest.writeString(this.areaUsesizecode);
        dest.writeString(this.specialNote);
        dest.writeString(this.operator);
        dest.writeString(this.opDate);
        dest.writeString(this.cOperator);
        dest.writeString(this.cpDate);
        dest.writeInt(this.fSeqnum);
        dest.writeString(this.cRegGubun);

    }

    public AgencyMenu07SiteInfo(Parcel parcel) {
        this.siteRegdate = parcel.readString();
        this.siteNum = parcel.readString();
        this.comId = parcel.readString();
        this.siteName = parcel.readString();
        this.siteRoadaddrpart1 = parcel.readString();
        this.siteRoadaddrpart2 = parcel.readString();
        this.siteDetaddr = parcel.readString();
        this.siteJibunaddr = parcel.readString();
        this.siteZipcode = parcel.readString();
        this.siteAdmcd = parcel.readString();
        this.siteRoadcode = parcel.readString();
        this.siteBuildingcode = parcel.readString();
        this.siteBuildingdetname = parcel.readString();
        this.siteBcode = parcel.readString();
        this.siteEmdno = parcel.readString();
        this.siteBuildingname = parcel.readString();
        this.siteApthouseyn = parcel.readString();
        this.siteSido = parcel.readString();
        this.siteGugun = parcel.readString();
        this.siteDong = parcel.readString();
        this.siteBname = parcel.readString();
        this.siteBname1 = parcel.readString();
        this.siteRn = parcel.readString();
        this.siteHname = parcel.readString();
        this.siteBuldmnnm = parcel.readString();
        this.siteBuldslno = parcel.readString();
        this.siteInbrmnnm = parcel.readString();
        this.siteInbrslno = parcel.readString();
        this.saleTypecode = parcel.readString();
        this.buildingTypecode = parcel.readString();
        this.heatTypecode = parcel.readString();
        this.saleCust = parcel.readString();
        this.setCust = parcel.readString();
        this.saleYm = parcel.readString();
        this.completionYm = parcel.readString();
        this.aptBuildingCnt = parcel.readInt();
        this.totHouseholdCnt = parcel.readInt();
        this.mngofficeTel = parcel.readString();
        this.builder = parcel.readString();
        this.custCode = parcel.readString();
        this.custName = parcel.readString();
        this.deptItem = parcel.readString();
        this.deptName = parcel.readString();
        this.empNo = parcel.readString();
        this.empName = parcel.readString();
        this.areaUsesizecode = parcel.readString();
        this.specialNote = parcel.readString();
        this.operator = parcel.readString();
        this.opDate = parcel.readString();
        this.cOperator = parcel.readString();
        this.cpDate = parcel.readString();
        this.fSeqnum = parcel.readInt();
        this.cRegGubun = parcel.readString();

    }
}
