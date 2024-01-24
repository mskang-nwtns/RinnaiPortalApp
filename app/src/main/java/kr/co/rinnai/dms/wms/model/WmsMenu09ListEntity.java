package kr.co.rinnai.dms.wms.model;

import java.util.List;

/**
 *
 * @author mini3248
 * 쿠팡 배송 리스트 작업용 Entity
 * 서버 VO (ReceivedResult)
 *
 */

public class WmsMenu09ListEntity {
    private String agencyOrderbarcode;
    private String modelName;
    private String type;
    private String instructions;
    private String operations;
    private String remark;

    public String getAgencyOrderbarcode() {
        return agencyOrderbarcode;
    }

    public void setAgencyOrderbarcode(String agencyOrderbarcode) {
        this.agencyOrderbarcode = agencyOrderbarcode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getOperations() {
        return operations;
    }

    public void setOperations(String operations) {
        this.operations = operations;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
