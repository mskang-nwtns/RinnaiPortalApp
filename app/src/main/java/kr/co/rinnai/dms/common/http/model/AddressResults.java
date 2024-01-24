package kr.co.rinnai.dms.common.http.model;

import java.util.List;

public class AddressResults {

    private AddressCommonData common;

    private List<AddressJusoData> juso;

    public AddressCommonData getCommon() {
        return common;
    }

    public void setCommon(AddressCommonData common) {
        this.common = common;
    }

    public List<AddressJusoData> getJuso() {
        return juso;
    }

    public void setJuso(List<AddressJusoData> juso) {
        this.juso = juso;
    }
}
