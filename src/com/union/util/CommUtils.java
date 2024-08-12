package com.union.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

public class CommUtils {
	
	public static String timeStamp2Date(String seconds,String format) {  
        if(seconds == null || seconds.isEmpty() || seconds.equals("null")){  
            return "";  
        }  
        if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";  
        SimpleDateFormat sdf = new SimpleDateFormat(format);  
        return sdf.format(new Date(Long.valueOf(seconds+"000")));  
    }

	public static String currentlyTime() {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return df.format(new Date());
	}

	public static String currentlyTimeYYYYMMDD() throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");

		return df.format(new Date());
	}

	public static String currentlyTimeYYYYMMDDHHMMSS() throws ParseException {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

		return df.format(new Date());
	}

	public static String getStringFromBean(Object o) throws Exception {
		if (o instanceof JSONObject || o instanceof String)
			return o.toString();
		StringBuffer sb = new StringBuffer();
		sb.append("[BeanName=");
		sb.append(o.getClass().getSimpleName());
		sb.append(",");
		Field[] farr = o.getClass().getDeclaredFields();
		for (Field field : farr) {
			field.setAccessible(true);
			sb.append(field.getName());
			sb.append("=");
			sb.append(field.get(o));
			sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	public static int getTimeByYYYYMM(String month) throws ParseException {

		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));

		DateFormat dateFormat = new SimpleDateFormat("yyyyMM");
		Date myDate = dateFormat.parse(year + month);
		return (int) (myDate.getTime() / 1000);
	}

	public static String getYearYYYY() {
		Calendar cal = Calendar.getInstance();
		String year = String.valueOf(cal.get(Calendar.YEAR));
		return year;
	}
	
	public static String convertDateTimeToMillisInt(String dateTime) throws ParseException{
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return String.valueOf(df.parse(dateTime).getTime()/1000);
		
	}
	
	public static String convertDateTimeToHour(String dateTime) throws ParseException{
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat df2 = new SimpleDateFormat("[HH:mm:ss]");
		
		return df2.format(df.parse(dateTime).getTime());
	}

	public static void scanerrRead(String fileName) throws Exception {

		FileInputStream inputStream = new FileInputStream(fileName);
		Scanner sc = new Scanner(inputStream, "UTF-8");
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
		}
		sc.close();
		inputStream.close();
	}

	/* 左右空格都去掉 */
	public static String trim(String str) {
		if (str == null || str.equals("")) {
			return str;
		} else {
			// return leftTrim(rightTrim(str));
			return str.replaceAll("^[　 ]+|[　 ]+$", "");
		}
	}

	/* 去左空格 */
	public static String leftTrim(String str) {
		if (str == null || str.equals("")) {
			return str;
		} else {
			return str.replaceAll("^[　 ]+", "");
		}
	}
	
	/* 去左引号 */
	public static String leftTrimQuote(String str) {
		if (str == null || str.equals("")) {
			return str;
		} else {
			return str.replaceAll("^[\"]+", "");
		}
	}

	/* 去右空格 */
	public static String rightTrim(String str) {
		if (str == null || str.equals("")) {
			return str;
		} else {
			return str.replaceAll("[　 ]+$", "");
		}
	}
	
	public static boolean isInteger(String str) {  
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
        return pattern.matcher(str).matches();  
  }
}
