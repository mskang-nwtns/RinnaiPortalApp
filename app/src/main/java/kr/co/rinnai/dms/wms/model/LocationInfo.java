package kr.co.rinnai.dms.wms.model;

import java.util.ArrayList;

public class LocationInfo {

    private ArrayList<ProductInfoResult> list;
    private int erpCount;

    public ArrayList<ProductInfoResult> getList() {
        return list;
    }

    public void setList(ArrayList<ProductInfoResult> list) {
        this.list = list;
    }

    public int getErpCount() {
        return erpCount;
    }

    public void setErpCount(int erpCount) {
        this.erpCount = erpCount;
    }
}
