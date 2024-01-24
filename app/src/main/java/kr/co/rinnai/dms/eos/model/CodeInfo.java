package kr.co.rinnai.dms.eos.model;

import java.util.List;

public class CodeInfo {
    /**
     * 건물 형태 타입 목록
     */

    private List<ItemCode> buildingCode;

    /**
     * 난방 형태 타입 목록
     */
    private List<ItemCode> heatingCode;

    public List<ItemCode> getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(List<ItemCode> buildingCode) {
        this.buildingCode = buildingCode;
    }

    public List<ItemCode> getHeatingCode() {
        return heatingCode;
    }

    public void setHeatingCode(List<ItemCode> heatingCode) {
        this.heatingCode = heatingCode;
    }
}
