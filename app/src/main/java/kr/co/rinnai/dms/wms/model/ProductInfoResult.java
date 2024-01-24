package kr.co.rinnai.dms.wms.model;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * 
 * @author mini3248
 * 이동지시서 조회 결과
 */
public class ProductInfoResult {
	
	/**
	 * ORDER_SEQ : 지시 수량(주문수량)
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String qty;


	/**
	 * GOODS_CODE : 제품 바코드
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String goodsCode;

	/**
	 * ITEM_CODE 모델 코드
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String itemCode;
	
	/**
	 * MODEL_NAME : 모델 이름
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String modelName;
	
	/**
	 * GAS_TYPE : 가스 구분
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String type;

	/**
	 * 로케이션 번호
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String cellNo;

	/**
	 * 로케이션 빠레트 번호
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String cellDetail;

	/**
	 * 로케이션
	 * Cell 등록 일자
	 */
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String cellMake;

	private boolean read = false;


	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getItemCode() {
	    String modelCode = "";
	    if(itemCode != null) {
			modelCode = itemCode.substring(0, 6);
		}
		return modelCode;
	}

	public String getGoodsCode(String date) {

		String goodsCode = "";
		if(itemCode != null) {
			String code = itemCode.substring(0, 5) + itemCode.substring(8, 9);
			goodsCode = String.format("%s%s%s%04d", code, date, "99999", Integer.parseInt(getQty()));
		}
		return goodsCode;
	}


	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getType() {
		String gasType = "";
		switch (type) {
			case "0":
				gasType = "기타";
				break;
			case "1":
				gasType = "LPG";
				break;
			case "2":
				gasType = "LNG";
				break;
		}
		return gasType;
	}



	public void setType(String type) {
		this.type = type;
	}

	public String getCellNo() {

		String locationNo = cellNo;
		if(!"".equals(cellNo) && cellNo != null) {
			locationNo = String.format("%s-%s-%s", cellNo.substring(0, 2), cellNo.substring(2, 5), cellNo.substring(5, 6));
		}
		return locationNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}

	public String getCellDetail() {
		return cellDetail;
	}

	public void setCellDetail(String cellDetail) {
		this.cellDetail = cellDetail;
	}

	public String getCellMake() {
		String date = cellMake;

		if(!"".equals(cellMake) && cellMake != null) {
			date = String.format("%s.%s", cellMake.substring(0, 2), cellMake.substring(2, 4));
		}
		return date;
	}

	public void setCellMake(String cellMake) {
		this.cellMake = cellMake;
	}

	public boolean isRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}
}

