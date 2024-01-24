package kr.co.rinnai.dms.common.util;

import java.util.Comparator;
public class Descending implements Comparator<Integer>{
	
	public int compare(Integer a, Integer b)
	{
		return b.compareTo(a);
	}
}