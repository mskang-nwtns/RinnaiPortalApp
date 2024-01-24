package kr.co.rinnai.dms.wms.model;

import java.util.List;

/**
 *
 * @author mini3248
 * 입고 작업 완료 후 서버 전송 Result
 * 서버 VO (ReceivedResult)
 *
 */

public class WmsMenu02ResultEntity {

    private String type;

    private List<LocationInfoQueryVO> list;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<LocationInfoQueryVO> getList() {
        return list;
    }

    public void setList(List<LocationInfoQueryVO> list) {
        this.list = list;
    }
}
