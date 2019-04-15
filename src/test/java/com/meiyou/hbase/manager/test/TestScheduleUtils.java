package com.meiyou.hbase.manager.test;

import com.meiyou.hbase.manager.utils.ScheduleUtils;

public class TestScheduleUtils {
	public static void main(String[] args) {
		String cron = ScheduleUtils.joinCron(1, 3, 1, 2);
		System.out.println(cron);
	}
}
