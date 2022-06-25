package org.smile.framework.orm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.smile.framework.orm.exception.SqlTemplateException;

public class SqlTemplateUtil {

	public static String retriveFirstKey(String sqlStringTemplate) {
		char string[] = sqlStringTemplate.toCharArray();
		int i=0, start=0, end=0;
		boolean isOccur = false;
		while(i<string.length) {
			char c = string[i];
			if (c=='#') {
				if (isOccur) {
					end = i;
					if (start+1<end) {
						return new String(Arrays.copyOfRange(string, start+1, end));
					} else {
						isOccur = false;
						start = 0;
						end = 0;
					}
				} else {
					start = i;
					isOccur = true;
				}
			}
			++i;
		}
		
		return null;
	}
	
	public static List<String> split2List(String sqlStringTemplate) throws Exception {
		List<String> list = new ArrayList<String>();
		char string[] = sqlStringTemplate.toCharArray();
		
		int i=0, start=0, end=0;
		boolean isReOccur = false;
		
		while(i<string.length) {
			char c = string[i];			
			if (c=='#') {
				if (isReOccur) {
					++i;
					isReOccur = false;
				} else {
					isReOccur = true;
				}
				
				end = i;
				String element = new String(Arrays.copyOfRange(string, start, end));
				list.add(element);
				start = end;
			}
			++i;
		}
		
		if (end<string.length) {
			String element = new String(Arrays.copyOfRange(string, end, string.length));
			list.add(element);
		}
		
		if (isReOccur) {
			throw new SqlTemplateException(String.format("Sql Mapping Format is Not correct! The Sql Template is %s", sqlStringTemplate));
		}
		
		return list;
	}
	
	public static void main(String[] args) {
		String sql = "SELECT * FROM CL_LOAN_REPAY WHERE user_id = #userInd# and out_order_no = #outOrderNo# and username = #name#";
		
		String result = retriveFirstKey(sql);
		System.out.println("+||"+result+"||+");
	}
}