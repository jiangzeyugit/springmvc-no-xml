package com.nest.currency.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;



/**
 * 时间日期工具类
 * @author <a href="mailto:ohergal@gmail.com">ohergal</a>
 * modify date : 2009-9-4
 * @since 1.0
 */
public class DateUtil {
	private static final String datePattern_YY_MM_DD = "yyyy-MM-dd";
	
	/**
	 * 格式化的一些标准
	 * @author <a href="mailto:ohergal@gmail.com">ohergal</a>
	 * create date : 2009-9-4
	 */
	private static class DateFormatPatterns{
		static final SimpleDateFormat ORACLE__STANDARD = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//标准oracle格式
		static final SimpleDateFormat ORACLE__YY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");	//标准oracle格式 年月日
		static final SimpleDateFormat ORACLE__HH_MM = new SimpleDateFormat("HH:mm");	//标准oracle格式 小时 分
		static final SimpleDateFormat ORACLE_CHN__STANDARD = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");	//标准oracle格式
		static final SimpleDateFormat ORACLE_CHN__YY_MM_DD = new SimpleDateFormat("yyyy年MM月dd日");	//标准oracle格式 年月日
		static final SimpleDateFormat ORACLE_CHN__HH_MM = new SimpleDateFormat("HH时mm分");	//标准oracle格式 分秒
	}
	
	/**
	 * 常见类型
	 * @author <a href="mailto:ohergal@gmail.com">ohergal</a>
	 * create date : 2009-9-4
	 */
	public static enum DateType{
		ORACLE_STANDARD,
		ORACLE_YY_MM_DD,
		ORACLE_HH_MM,
		ORACLE_CHN_STANDARD,
		ORACLE_CHN_YY_MM_DD,
		ORACLE_CHN_HH_MM,
		SAMPLE_FOR_HUMAN_UNDSTAND;
	}
	
	/**
	 * 获取时间的格式为yyyy-MM-dd
	 * @param date
	 * @return
	 */
	public static String getDateText(java.util.Date date){
		SimpleDateFormat df = new SimpleDateFormat(datePattern_YY_MM_DD);
		//java.util.Date now = new java.util.Date();
		return df.format(date);
	}
	/**
	 * 获取当前时间的格式为yyyy-MM-dd
	 * @return
	 */
	public static String getDateText(){
		SimpleDateFormat df = new SimpleDateFormat(datePattern_YY_MM_DD);
		java.util.Date now = new java.util.Date();
		return df.format(now);
	}
	/**
	 * 获取指定格式的当前时间
	 * @param pattern
	 * @return
	 */
	public static String getDateText(String pattern){
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		java.util.Date now = new java.util.Date();
		return df.format(now);
	}
	
	/**
	 * 获取指定格式的指定时间
	 * @param date
	 * @param formate
	 * @return
	 */
	public static String getDateText(java.util.Date date, String formate){
		SimpleDateFormat df = new SimpleDateFormat(formate);
		return df.format(date);
	}
	
	/**
	 * 格式化时间为人容易识别的方式
	 * @param timeline
	 * @return
	 */
	public static String getDateTextForHuman(long timeline){
		String text = "";
		//获取当前的时间
		long currentTimeLine = System.currentTimeMillis();
		int currentSecond = (int)(currentTimeLine/1000);
		int timeLineSecond = (int)(timeline/1000);
		/*
		 * 一分钟内的 显示  [多少秒前]
		 */
		int differ = currentSecond - timeLineSecond;

		if ( differ < 60) {
			text = String.valueOf(differ) + "秒前";
		} else if ( differ >= 60 &&  differ < 60*60 ) {
			//一分钟内到1小时内的 显示  [多少分钟前]
			text = String.valueOf(differ/60) + "分钟前";
		} else if ( differ >= 60*60 &&  differ < 60*60*24 ) {
			//1小时到24小时内的 显示  [多少小时前]
			text = String.valueOf( differ/(60*60) ) + "小时前";
		} else if ( differ >= 60*60*24 &&  differ < 60*60*24*7 ) {
			//1天到7天内的 显示  [多少天前]
			text = String.valueOf( differ/(60*60*24) ) + "天前";
		} else if ( differ >= 60*60*24*7 &&  differ < 60*60*24*30 ) {
			//7天到一个月内的 显示  [x月x日 xx:xx]
			text = formatBy(new java.util.Date(timeline), DateType.ORACLE_CHN_YY_MM_DD);
		} else if ( differ >= 60*60*24*30 ) {
			//7天到一个月内的 显示  [x月x日 xx:xx]
			text = formatBy(new Date(timeline), DateType.ORACLE_CHN_STANDARD);
		} else {
			text = formatBy(new Date(timeline), DateType.ORACLE_STANDARD);
		}
		
		return text;
	}
	
	/**
	 * 格式化时间为人容易识别的方式
	 * @param date
	 * @return
	 */
	public static String getDateTextForHuman(java.util.Date date){
		String text = "";
		//获取当前的时间
		long currentTimeLine = System.currentTimeMillis();
		int currentSecond = (int)(currentTimeLine/1000);
		int timeLineSecond = (int)(date.getTime()/1000);
		/*
		 * 一分钟内的 显示  [多少秒前]
		 */
		int differ = currentSecond - timeLineSecond;

		if ( differ < 60 && differ >=0 ) {
			text = String.valueOf(differ) + "秒前";
		} else if ( differ >= 60 &&  differ < 60*60 ) {
			//一分钟内到1小时内的 显示  [多少分钟前]
			text = String.valueOf(differ/60) + "分钟前";
		} else if ( differ >= 60*60 &&  differ < 60*60*24 ) {
			//1小时到24小时内的 显示  [多少小时前]
			text = String.valueOf( differ/(60*60) ) + "小时前";
		} else if ( differ >= 60*60*24 &&  differ < 60*60*24*7 ) {
			//1天到7天内的 显示  [多少天前]
			text = String.valueOf( differ/(60*60*24) ) + "天前";
		} else if ( differ >= 60*60*24*7 &&  differ < 60*60*24*30 ) {
			//7天到一个月内的 显示  [x月x日 xx:xx]
			text = formatBy(date, DateType.ORACLE_CHN_YY_MM_DD);
		} else if ( differ >= 60*60*24*30 ) {
			//7天到一个月内的 显示  [x月x日 xx:xx]
			text = formatBy(date, DateType.ORACLE_CHN_STANDARD);
		} else {
			text = formatBy(date, DateType.ORACLE_STANDARD);
		}
		
		return text;
	}
	/**
	 * 时间差
	 * @param dateStart
	 * @param dateStop
	 * @return
	 */
	public static long diffTime(String dateStart, String dateStop) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date d1 = null;
		Date d2 = null;

		try {
			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 毫秒ms
		long diff = d2.getTime() - d1.getTime();
		return diff ;

	}
	/**
	 * 获取当前时间的"yyyy-MM-dd HH:mm:ss"格式字符串
	 * 
	 * @return
	 */
	public static String getCurrenttime() {
		// 创建时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String currenttime = df.format(new Date());// new Date()为获取当前系统时间
		return currenttime;
	}
	/**
	 * 通用格式化时间日期
	 * @param date
	 * @param type
	 * @return
	 */
	public static String formatBy(java.util.Date date, DateType type){
		String dateText = null;
		if ( date != null ){
			switch ( type ) {
			case ORACLE_STANDARD :
				dateText = DateFormatPatterns.ORACLE__STANDARD.format(date);
				break;
			case ORACLE_YY_MM_DD :
				dateText = DateFormatPatterns.ORACLE__YY_MM_DD.format(date);
				break;
			case ORACLE_HH_MM :
				dateText = DateFormatPatterns.ORACLE__HH_MM.format(date);
				break;
			case ORACLE_CHN_STANDARD :
				dateText = DateFormatPatterns.ORACLE_CHN__STANDARD.format(date);
				break;
			case ORACLE_CHN_YY_MM_DD :
				dateText = DateFormatPatterns.ORACLE_CHN__YY_MM_DD.format(date);
				break;
			case ORACLE_CHN_HH_MM :
				dateText = DateFormatPatterns.ORACLE_CHN__HH_MM.format(date);
				break;
			case SAMPLE_FOR_HUMAN_UNDSTAND :
				dateText = getDateTextForHuman(date);
				break;
			default :
				dateText = DateFormatPatterns.ORACLE__STANDARD.format(date);
				break;
			}
		}
		return dateText;
	}
	
	/**
	 * 获取今天之前或之后的时间
	 * 例如：明天：传1，昨天传-1
	 * @param day
	 * @return
	 */
	public static Date getDate(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, day);
    	Date date = calendar.getTime();
		return date;
	}
}
