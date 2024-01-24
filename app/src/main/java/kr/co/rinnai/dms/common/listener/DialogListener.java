package kr.co.rinnai.dms.common.listener;

public interface DialogListener {
    /**
     * 제품 바코드, 이동지시서 제품 바코드 리딩 후 적용 버튼 클릭시
     * @param barcode
     * @param qty
     */
    public void onPositiveClicked(String barcode,String qty);

    /**
     * 재고실사 화면에서 아이템 롱 클릭 후 적용 버튼 클릭시
     * @param barcode
     * @param qty
     * @param position
     */
    public void onPositiveClicked(String barcode, String qty, int position);

    /**
     * 재고실사 화면에서 아이템 롱 클릭 후 적용 버튼 클릭시
     * @param barcode
     * @param modelName
     * @param qty
     */
    public void onPositiveClicked(String barcode, String modelName, String qty);

}
