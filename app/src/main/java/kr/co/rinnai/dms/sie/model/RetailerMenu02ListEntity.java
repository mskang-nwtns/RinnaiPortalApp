package kr.co.rinnai.dms.sie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * @author mini3248
 * 양판점 배송 기사 용 작업 내역 조회 쿼리용 VO
 * 서버 VO (OrderInfoResultVO)
 */

public class RetailerMenu02ListEntity implements Parcelable {

    private String agencyOrderBarcode;
    private String clientName;
    private String addr1;
    private String addr2;
    private String modelName;
    private String type;
    private String qty;
    private Object image01;
    private Object sign01;
    private Object image02;


    private String strImage01;
    private String strImage02;
    private String strSign01;

    public String getAgencyOrderBarcode() {
        return agencyOrderBarcode;
    }

    public void setAgencyOrderBarcode(String agencyOrderBarcode) {
        this.agencyOrderBarcode = agencyOrderBarcode;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
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

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public Object getImage01() {
        return image01;
    }

    public void setImage01(Object image01) {
        this.image01 = image01;
    }

    public Object getSign01() {
        return sign01;
    }

    public void setSign01(Object sign01) {
        this.sign01 = sign01;
    }

    public String getStrImage01() {
        return strImage01;
    }

    public void setStrImage01(String strImage01) {
        this.strImage01 = strImage01;
    }

    public String getStrSign01() {
        return strSign01;
    }

    public void setStrSign01(String strSign01) {
        this.strSign01 = strSign01;
    }

    public Object getImage02() {
        return image02;
    }

    public void setImage02(Object image02) {
        this.image02 = image02;
    }

    public String getStrImage02() {
        return strImage02;
    }

    public void setStrImage02(String strImage02) {
        this.strImage02 = strImage02;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // create Parcelable
    public static final Parcelable.Creator<RetailerMenu02ListEntity> CREATOR = new Parcelable.Creator<RetailerMenu02ListEntity>() {
        @Override
        public RetailerMenu02ListEntity createFromParcel(Parcel parcel) {
            return new RetailerMenu02ListEntity(parcel);
        }
        @Override
        public RetailerMenu02ListEntity[] newArray(int size) {
            return new RetailerMenu02ListEntity[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.agencyOrderBarcode);
        dest.writeString(this.clientName);
        dest.writeString(this.addr1);
        dest.writeString(this.addr2);
        dest.writeString(this.modelName);
        dest.writeString(this.type);
        dest.writeString(this.qty);



    }

    public RetailerMenu02ListEntity(Parcel parcel) {

        this.agencyOrderBarcode = parcel.readString();
        this.clientName = parcel.readString();
        this.addr1 = parcel.readString();
        this.addr2 = parcel.readString();
        this.modelName = parcel.readString();
        this.type = parcel.readString();
        this.qty = parcel.readString();

    }
}
