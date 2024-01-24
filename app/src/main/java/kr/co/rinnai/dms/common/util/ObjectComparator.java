package kr.co.rinnai.dms.common.util;

import java.util.Comparator;

import kr.co.rinnai.dms.wms.model.OrderReportResult;

public class ObjectComparator implements Comparator<Object> {
	String compareType;
	public ObjectComparator (String type) {
		compareType = type;
	}
	@Override
	public int compare(Object first, Object second) {
		if (first instanceof OrderReportResult &&  second instanceof OrderReportResult) {
			OrderReportResult f = (OrderReportResult) first;
			OrderReportResult s = (OrderReportResult) second;
			if("custCode".equals(compareType )) { 
				return f.getCustCode().compareTo(s.getCustCode());
			}
			
		}
		return 0;
	}
	
	
	
	
}
