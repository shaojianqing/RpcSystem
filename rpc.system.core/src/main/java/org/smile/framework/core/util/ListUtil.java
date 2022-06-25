package org.smile.framework.core.util;

import java.util.List;

public class ListUtil {
	
	@SuppressWarnings("rawtypes") 
	public static boolean isEmpty(List list) {
		return list==null || list.size()==0;
	}
	
	@SuppressWarnings("rawtypes") 
	public static boolean isNotEmpty(List list) {
		return list!=null && list.size()>0;
	}
}
