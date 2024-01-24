package kr.co.rinnai.dms.common.listener;

import kr.co.rinnai.dms.aos.model.AgencyMenu06ServiceEntity;

public interface ServiceDialogListener {
    /**
     * 대리점 서비스 접수 이벤트 리스너
     * @param type
     * @param entity
     */
    public void onPositiveClicked(String type, Object entity);


}
