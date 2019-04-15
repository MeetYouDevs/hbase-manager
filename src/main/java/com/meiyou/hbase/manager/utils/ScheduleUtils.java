package com.meiyou.hbase.manager.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleUtils {
	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleUtils.class);

	private static final String COLLECT_TIME_FORMAT = "yyyyMMddHHmm";
	private static final String DATE_FORMAT = "yyyyMMdd";

	/**
	 * cron表达式：每M分钟
	 *
	 * @param interval
	 * @return
	 */
	public static String getMinuteCron(int interval) {
		String baseCron = "0 0/" + interval + " * ? * *";
		return baseCron;
	}

	public static String getFiveMinuteCronByHostId(long hostId) {
		String baseCron = (hostId % 50) + " 0/5 * ? * *";
		return baseCron;
	}

	/**
	 * cron表达式：每小时，根据hostId计算小时的分钟数
	 *
	 * @param hostId
	 * @return
	 */
	public static String getHourCronByHostId(long hostId) {
		String hourCron = "0 %s 0/1 ? * *";
		Random random = new Random();
		long minute = (hostId + random.nextInt(Integer.MAX_VALUE)) % 60;
		return String.format(hourCron, minute);
	}

	/**
	 * 计算前一分钟的时间，并格式化
	 *
	 * @param collectTime
	 *            基准时间
	 * @return
	 */
	public static long getLastCollectTime(long collectTime) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(COLLECT_TIME_FORMAT);
		try {
			Date date = simpleDateFormat.parse(String.valueOf(collectTime));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.MINUTE, -1);
			Date lastDate = calendar.getTime();
			return Long.valueOf(simpleDateFormat.format(lastDate));
		} catch (ParseException e) {
			LOGGER.error(e.getMessage(), e);
			return 0L;
		}
	}

	/**
	 * 格式化时间
	 *
	 * @param date
	 * @return
	 */
	public static long getCollectTime(Date date) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(COLLECT_TIME_FORMAT);
		return Long.valueOf(simpleDateFormat.format(date));
	}

	/**
	 * 返回某一天的起始时间，如：201406300000
	 *
	 * @param date
	 *            当前日期
	 * @param offset
	 *            针对当前日期的偏移
	 * @return 日期的long形式，如201406300000
	 */
	public static long getBeginTimeOfDay(Date date, int offset) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, offset);
		date = calendar.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
		return Long.valueOf(dateFormat.format(date) + "0000");
	}

	/**
	 * 
	 * @param interval
	 *            间隔
	 * @param intervalPosition
	 *            间隔粒度
	 * @param offset
	 *            偏移量
	 * @param offsetPosition
	 *            偏移粒度
	 * @return
	 */
	public static String joinCron(int interval, int intervalPosition, int offset, int offsetPosition) {
		// 秒 分 时 日 月
		String[] cronArray = { "*", "*", "*", "*", "*", "?" };

		// 间隔之前都置为0
		for (int i = 0; i < intervalPosition; i++) {
			if (i < 3) {
				cronArray[i] = "0"; // 秒 分 时
			} else {
				cronArray[i] = "1";// 日 月
			}
		}
		if (intervalPosition == offsetPosition) {// 间隔和偏移量粒度相同
			String newInvl = offset + "/" + interval;
			cronArray[intervalPosition] = newInvl;
		} else {
			cronArray[offsetPosition] = String.valueOf(offset);
			if (intervalPosition < 3) {
				cronArray[intervalPosition] = String.valueOf("0/" + interval);
			} else {
				cronArray[intervalPosition] = String.valueOf("1/" + interval);
			}
		}
		String cron = StringUtils.join(cronArray, " ");

		return cron;
	}
}
