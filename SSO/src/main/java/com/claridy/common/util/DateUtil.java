package com.claridy.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author RemberSu
 * 
 */
public class DateUtil {
	public static Date getHalfYear() {

		long time = 180 * 24L * 60 * 60 * 1000;
		long time1 = new Date().getTime() - time;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(time1);
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date getStart() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = "1970-01-01";
		Date date = null;
		try {
			date = df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	// 通過日期獲得星期幾
	public static String getWeekStr(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int week = c.get(Calendar.DAY_OF_WEEK);
		String str = week + "";
		if ("1".equals(str)) {
			str = "7";
		} else if ("2".equals(str)) {
			str = "1";
		} else if ("3".equals(str)) {
			str = "2";
		} else if ("4".equals(str)) {
			str = "3";
		} else if ("5".equals(str)) {
			str = "4";
		} else if ("6".equals(str)) {
			str = "5";
		} else if ("7".equals(str)) {
			str = "6";
		}
		return str;
	}

	/**
	 * 獲取當前時間
	 * 
	 * @return Date
	 */
	public static Date now() {
		Date retDate = new Date();
		return retDate;
	}

	/**
	 * 獲取明國年
	 * 
	 * @return Date
	 */
	public static Date getMingGuoYearMonth() {
		Date retDate = new Date();
		int nowYear = retDate.getYear();
		retDate.setYear(nowYear - 1911);
		return retDate;
	}

	/**
	 * 獲取第一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date startDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 設置為1號,當前日期既為本月第一天
		return c.getTime();
	}

	/**
	 * 獲取最後一天
	 * 
	 * @param date
	 * @return Date
	 */
	public static Date lastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	/**
	 * 獲取當天時間緊缺到小時 例如(new Date(),8),則返回今天的08:00:00
	 * 
	 * @param date
	 * @param hour
	 * @return Date
	 */
	public static Date getDateHour(Date date, int hour) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, hour);
		return c.getTime();
	}

	/**
	 * 根据传入的分钟数，獲取多少分钟后的時間
	 * 
	 * @param timeoutTime
	 * @return Date
	 */
	public static Date getTimeOutTime(int timeoutTime) {
		long currentTime = System.currentTimeMillis() + timeoutTime * 60 * 1000;
		Date timeOutTime = new Date(currentTime);
		return timeOutTime;
	}

	/**
	 * 得到几天前的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @return
	 */
	public static Date getDateAfter(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return now.getTime();
	}

}
