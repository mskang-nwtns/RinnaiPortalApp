package kr.co.rinnai.dms.eos.model;


/**
 * 
 * @author mini3248
 * 일별매출진도 조회 결과
 */
public class SalesProgress {

	private String comId;

	private String opDate;
	private String deptTypeName;

	private String saleDayAmt;

	private String salePlanAmt;
	private String saleDayPlanAmt;
	private String saleAmt;

	private String valueAddedPlanAmt;
	private String valueAddedDayPlanAmt;
	private String valueAddedAmt;

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

	public String getSaleDayAmt() {
		return saleDayAmt;
	}

	public void setSaleDayAmt(String saleDayAmt) {
		this.saleDayAmt = saleDayAmt;
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
}

