package org.smile.framework.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
	
	public final static int INFO = 1;
	
	public final static int WARN = 2;
	
	public final static int ERROR = 3;
	
	public static void log(int level, String message) {
		
		String levelString = "";
		
		if (INFO==level) {
			levelString = "[INFO]";
		} else if(WARN==level) {
			levelString = "[WARN]";
		} else if(ERROR==level) {
			levelString = "[ERROR]";
		}
		
		Date date = new Date();
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(date);
		
		String logString = String.format("%s %s %s ", dateString, levelString, message);
		System.out.println(logString);
	}
	
	public static void i(String message) {
		log(INFO, message);
	}
	
	public static void w(String message) {
		log(WARN, message);
	}
	public static void e(String message) {
		log(ERROR, message);
	}
}
