package kr.co.rinnai.dms.common.listener;

import kr.co.rinnai.dms.aos.model.AgencyMenu07SiteModelInfo;

public interface SiteModelModelSearchDialogListener {
    /**
     * 신축현장등록 화면에서 모델 리스트 에서 모델 선택 리스너
     * @param modelInfo
     *
     */
    public void onPositiveClicked(AgencyMenu07SiteModelInfo modelInfo);



}
