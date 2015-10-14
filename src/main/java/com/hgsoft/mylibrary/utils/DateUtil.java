package com.hgsoft.mylibrary.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /*
     日期格式
      */
    public static String DATE_FORMAT_SEC_NUMBER = "yyyyMMddHHmmss";
    public static String DATE_FORMAT_DAY_NUMBER = "yyyyMMdd";
    public static String DATE_FORMAT_MONTH_NUMBER = "yyyyMM";
    public static String DATE_FORMAT_SEC = "yyyy-MM-dd HH:mm:ss";
    public static String DATE_FORMAT_DAY = "yyyy-MM-dd";
    public static String DATE_FORMAT_MONTH = "yyyy-MM";
    public static String DATE_FORMAT_DAY_ZH_CN = "yyyy年MM月dd日";
    public static String DATE_FORMAT_MONTH_ZH_CN = "yyyy年MM月";


	/**
	 * 字符串转日期，字符串的格式如:"yyyy-MM-dd HH:mm:ss"
	 *
	 * @Title:strToDate
	 * @param dateStr
	 * @param format
	 * @return
	 * @author yudapei
	 */
	public static Date strToDate(String dateStr, String format) {
		Date date = null;
		SimpleDateFormat sdfSecond = new SimpleDateFormat(format);
		try {
			date = sdfSecond.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 时间转字符串, 如"yyyy-MM-dd HH:mm:ss"
	 *
	 * @Title:dateToStr
	 * @param datetime
	 * @param format
	 * @return
	 * @author yudapei
	 */
	public static String dateToStr(Date datetime, String format) {
		return new SimpleDateFormat(format, Locale.CHINA).format(datetime);
	}

	/**
	 * 日期转换到秒格式的字符串,"yyyy-MM-dd HH:mm:ss"
	 *
	 * @Title:dateToSec
	 * @param date
	 * @return
	 * @author yudapei
	 */
	public static String dateToSec(Date date) {
		if(date == null)
			return "";
		return dateToStr(date, DATE_FORMAT_SEC);
	}

	/**
	 * 日期到秒，字符串转时间，字符串格式为"yyyy-MM-dd HH:mm:ss"
	 *
	 * @Title:strToDate
	 * @param dateStr
	 * @return
	 * @author yudapei
	 */
	public static Date secToDate(String dateStr) {
		Date date = null;
		SimpleDateFormat sdfSecond = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdfSecond.parse(dateStr);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 日期转换到天格式的字符串,"yyyy-MM-dd" TODO(这里用一句话描述这个方法的作用)
	 *
	 * @Title:dateToDay
	 * @param date
	 * @return
	 * @author yudapei
	 */
	public static String dateToDay(Date date) {
		return dateToStr(date, DATE_FORMAT_DAY);
	}

    /**
     * 日期转换到天格式的字符串,"yyyy-MM-dd" TODO(这里用一句话描述这个方法的作用)
     *
     * @Title:dateToDay
     * @param date
     * @return
     * @author yudapei
     */
    public static String dateToMonth(Date date) {
        return dateToStr(date, DATE_FORMAT_MONTH);
    }


    /**
     * 日期转换到天格式的字符串,"yyyyMMdd" TODO(这里用一句话描述这个方法的作用)
     *
     * @Title:dateToDay
     * @param date
     * @return
     * @author yudapei
     */
    public static String dateToDay2(Date date) {
        return dateToStr(date, DATE_FORMAT_DAY_NUMBER);
    }

    /**
     * 日期转换到天格式的字符串,"yyyy年MM月dd日" TODO(这里用一句话描述这个方法的作用)
     *
     * @Title:dateToDay
     * @param date
     * @return
     * @author yudapei
     */
    public static String dateToDayZh(Date date) {
        return dateToStr(date, DATE_FORMAT_DAY_ZH_CN);
    }

	/**
	 * 日期转换到分格式的字符串,"yyyy-MM-dd HH:mm" TODO(这里用一句话描述这个方法的作用)
	 *
	 * @Title:dateToMin
	 * @param date
	 * @return
	 * @author yudapei
	 */
	public static String dateToMin(Date date) {
		return dateToStr(date, "yyyy-MM-dd HH:mm");
	}

	/**
	 * 目标日期是否在离现在的n天里
	 *
	 * @Title:isMatchDateInDays
	 * @param date
	 * @param daysDuration
	 * @return
	 *
	 * @author yudapei
	 */
	public static boolean isMatchDateInDays(Date date, int daysDuration) {
		boolean result = true;
		Date nowDate = new Date();
		int diffDays = nowDate.getDate() - date.getDate();
		if (diffDays > daysDuration) {
			result = false;
		}
		return result;
	}

    /**
     * 日期格式转换（从字符串格式转成另外一种字符串格式）
     *
     * @param dateStr    时间字符串
     * @param fromFormat 初始时间格式
     * @param toFormat   目标时间格式
     * @return
     */
    public static String strToStr(String dateStr, String fromFormat, String toFormat) {
        Date date = strToDate(dateStr, fromFormat);
        String toDateStr = dateToStr(date, toFormat);
        return toDateStr;
    }

}
