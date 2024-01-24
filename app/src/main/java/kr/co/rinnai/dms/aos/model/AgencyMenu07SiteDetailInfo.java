package kr.co.rinnai.dms.aos.model;

import java.util.List;

public class AgencyMenu07SiteDetailInfo {

    private List<AgencyMenu07SiteFileInfo> fileList;
    private List<AgencyMenu07SiteModelInfo> modelList;

    public List<AgencyMenu07SiteFileInfo> getFileList() {
        return fileList;
    }
    public void setFileList(List<AgencyMenu07SiteFileInfo> fileList) {
        this.fileList = fileList;
    }
    public List<AgencyMenu07SiteModelInfo> getModelList() {
        return modelList;
    }
    public void setModelList(List<AgencyMenu07SiteModelInfo> modelList) {
        this.modelList = modelList;
    }




}
