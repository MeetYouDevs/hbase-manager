package com.meiyou.hbase.manager.test;

import com.meiyou.hbase.manager.utils.HBaseWebUICrawler;

public class TestHBaseWebUICrawler {

	public static void main(String[] args) {
		System.out.println(HBaseWebUICrawler.getStatsInfoByTable("", "", "test"));
		System.out.println(HBaseWebUICrawler.getRegionServerSet(""));
	}
}
