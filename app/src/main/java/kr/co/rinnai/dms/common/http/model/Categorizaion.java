package kr.co.rinnai.dms.common.http.model;

import java.util.List;
import java.util.Map;

public class Categorizaion {
    private List<CategorizationResultVO> master;
    private Map<String, List<CategorizationResultVO>> sub;
    private List<AgencyInfoVO> agencyInfo;


    public List<CategorizationResultVO> getMaster() {
        return master;
    }
    public void setMaster(List<CategorizationResultVO> master) {
        this.master = master;
    }
    public Map<String, List<CategorizationResultVO>> getSub() {
        return sub;
    }
    public void setSub(Map<String, List<CategorizationResultVO>> sub) {
        this.sub = sub;
    }
    public List<AgencyInfoVO> getAgencyInfo() {
        return agencyInfo;
    }
    public void setAgencyInfo(List<AgencyInfoVO> agencyInfo) {
        this.agencyInfo = agencyInfo;
    }

}
