package kr.co.rinnai.dms.aos.model;

import java.util.List;

/**
 *
 * @author mini3248
 * 배송 제품 목록 조회(배차 or 화물 조회 쿼리용 VO)
 * 서버 VO (DeliveryInfoResult)
 */

public class AgencyMenu03DeliveryInfo {

    private AgencyMenu03AddressInfo address;
    private List<AgencyMenu03DeliveryListEntity> delivery;

    public AgencyMenu03AddressInfo getAddress() {
        return address;
    }
    public void setAddress(AgencyMenu03AddressInfo address) {
        this.address = address;
    }
    public List<AgencyMenu03DeliveryListEntity> getDelivery() {
        return delivery;
    }
    public void setDelivery(List<AgencyMenu03DeliveryListEntity> delivery) {
        this.delivery = delivery;
    }
}
