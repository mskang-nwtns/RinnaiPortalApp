package kr.co.rinnai.dms.common.listener;

import kr.co.rinnai.dms.common.http.model.AddressJusoData;

public interface AddressSelectedListener {
    /**
     * 검색된 주소 선택
     *
     */
    public void onSelected(AddressJusoData jusoData);



}
