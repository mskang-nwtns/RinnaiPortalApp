package kr.co.rinnai.dms.eos.model;

/**
 *
 * @author mini3248
 * 일별매출진도 조회 결과
 */
public class SalesProgressV3 {

    private String comId;

    private String opDate;
    private String deptTypeName;
    /**
     * 월간 매출 계획
     */
    private String salePlanAmt;
    /**
     * 일 계획
     */
    private String saleDayPlanAmt;

    /**
     * 일 실적
     */
    private String saleAmt;

    /**
     * 월간 계획 대비 매출
     */
    private String mPlanRate;

    /**
     * 일 계획 대비 매출
     */
    private String dPlanRate;

    /**
     * 당일 실적
     */
    private String saleDayAmt;

    /**
     * 월간 부가액 계획
     */
    private String valueAddedPlanAmt;
    /**
     * 일 부가액 계획
     */
    private String valueAddedDayPlanAmt;

    /**
     * 일 부가액 실적
     */
    private String valueAddedAmt;

    /**
     * 월간 계획 대비 부가액 실적
     */
    private String mPlanRateValueAdded;

    /**
     * 일간 계획 대비 부가액 실적
     */
    private String dPlanRateValueAdded;

    /**
     * 월 부가율 계획
     */
    private String planAdditiveRate;

    /**
     * 일 부가율 계획
     */
    private String dPlanAdditiveRate;

    /**
     * 일 부가율 실적
     */
    private String additiveRate;

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
    public String getDeptTypeName() {
        return deptTypeName;
    }
    public void setDeptTypeName(String deptTypeName) {
        this.deptTypeName = deptTypeName;
    }
    public String getSalePlanAmt() {
        return salePlanAmt;
    }
    public void setSalePlanAmt(String salePlanAmt) {
        this.salePlanAmt = salePlanAmt;
    }
    public String getSaleDayPlanAmt() {
        return saleDayPlanAmt;
    }
    public void setSaleDayPlanAmt(String saleDayPlanAmt) {
        this.saleDayPlanAmt = saleDayPlanAmt;
    }
    public String getSaleAmt() {
        return saleAmt;
    }
    public void setSaleAmt(String saleAmt) {
        this.saleAmt = saleAmt;
    }

    public String getmPlanRate() {
        return mPlanRate;
    }
    public void setmPlanRate(String mPlanRate) {
        this.mPlanRate = mPlanRate;
    }
    public String getdPlanRate() {
        return dPlanRate;
    }
    public void setdPlanRate(String dPlanRate) {
        this.dPlanRate = dPlanRate;
    }
    public String getValueAddedPlanAmt() {
        return valueAddedPlanAmt;
    }
    public void setValueAddedPlanAmt(String valueAddedPlanAmt) {
        this.valueAddedPlanAmt = valueAddedPlanAmt;
    }
    public String getValueAddedDayPlanAmt() {
        return valueAddedDayPlanAmt;
    }
    public void setValueAddedDayPlanAmt(String valueAddedDayPlanAmt) {
        this.valueAddedDayPlanAmt = valueAddedDayPlanAmt;
    }
    public String getValueAddedAmt() {
        return valueAddedAmt;
    }
    public void setValueAddedAmt(String valueAddedAmt) {
        this.valueAddedAmt = valueAddedAmt;
    }


    public String getmPlanRateValueAdded() {
        return mPlanRateValueAdded;
    }
    public void setmPlanRateValueAdded(String mPlanRateValueAdded) {
        this.mPlanRateValueAdded = mPlanRateValueAdded;
    }
    public String getdPlanRateValueAdded() {
        return dPlanRateValueAdded;
    }
    public void setdPlanRateValueAdded(String dPlanRateValueAdded) {
        this.dPlanRateValueAdded = dPlanRateValueAdded;
    }
    public String getSaleDayAmt() {
        return saleDayAmt;
    }
    public void setSaleDayAmt(String saleDayAmt) {
        this.saleDayAmt = saleDayAmt;
    }
    public String getPlanAdditiveRate() {
        return planAdditiveRate;
    }
    public void setPlanAdditiveRate(String planAdditiveRate) {
        this.planAdditiveRate = planAdditiveRate;
    }
    public String getdPlanAdditiveRate() {
        return dPlanAdditiveRate;
    }
    public void setdPlanAdditiveRate(String dPlanAdditiveRate) {
        this.dPlanAdditiveRate = dPlanAdditiveRate;
    }
    public String getAdditiveRate() {
        return additiveRate;
    }
    public void setAdditiveRate(String additiveRate) {
        this.additiveRate = additiveRate;
    }


}
