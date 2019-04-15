package com.meiyou.hbase.manager.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String DATETIME_FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	public static final String DATETIME_FORMAT_NGINX = "dd/MMM/yyyy:HH:mm:ss";

	private static DateFormat getFormat(String pattern, String timeZone) {
		DateFormat dateFormat = new SimpleDateFormat(pattern, Locale.ENGLISH);
		if (null != timeZone) {
			dateFormat.setTimeZone(TimeZone.getTimeZone(timeZone));
		}
		return dateFormat;
	}

	public static Date parse(String pattern, String date) throws ParseException {
		return parse(pattern, null, date);
	}

	public static Date parse(String pattern, String timeZone, String date) throws ParseException {
		return getFormat(pattern, timeZone).parse(date);
	}

	public static String format(String pattern, Date date) throws ParseException {
		return format(pattern, null, date);
	}

	public static String format(String pattern, String timeZone, Date date) throws ParseException {
		return getFormat(pattern, timeZone).format(date);
	}

	public static Date passHour(Date date,int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR,calendar.get(Calendar.HOUR) + value);
		return calendar.getTime();
	}
	public static Date passDay(Date date, int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR,calendar.get(Calendar.DATE) + value);
		return calendar.getTime();
	}
	public static Date passMonth(Date date,int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR,calendar.get(Calendar.MONTH) + value);
		return calendar.getTime();
	}

	public static String format_YYYYYMM(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		return sdf.format(date);
	}

	public static String format_YYYYYMMDD(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}

	public static String format_YYYYYMMDDHH(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH");
		return sdf.format(date);
	}
}