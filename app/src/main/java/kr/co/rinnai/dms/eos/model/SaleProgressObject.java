package kr.co.rinnai.dms.eos.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mini3248
 * 일별매출진도 조회 결과
 */
public class SaleProgressObject {

    /**
     * 당일까지의 근무일수
     */
    private int workingDay;

    /**
     * 당월 근무일수
     */
    private int monthWorkingDay;

    /**
     * 부서별 일 매출진도 현황
     */
    private ArrayList<SalesProgressV3> saleProgressList;

    /**
     * 사업부별 일 매출진도 현황
     */
    private ArrayList<SalesProgressV3> saleBusinessProgressList;


    public int getWorkingDay() {
        return workingDay;
    }


    public void setWorkingDay(int workingDay) {
        this.workingDay = workingDay;
    }


    public int getMonthWorkingDay() {
        return monthWorkingDay;
    }


    public void setMonthWorkingDay(int monthWorkingDay) {
        this.monthWorkingDay = monthWorkingDay;
    }


    public ArrayList<SalesProgressV3> getSaleProgressList() {
        return saleProgressList;
    }

    public ArrayList<SalesProgressV3> getSaleBusinessProgressList() {
        return saleBusinessProgressList;
    }

    public void setSaleBusinessProgressList(ArrayList<SalesProgressV3> saleBusinessProgressList) {
        this.saleBusinessProgressList = saleBusinessProgressList;
    }

    public void setSaleProgressList(ArrayList<SalesProgressV3> saleProgressList) {
        this.saleProgressList = saleProgressList;
    }

}
