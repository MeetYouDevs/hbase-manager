package com.meiyou.hbase.manager.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.meiyou.hbase.manager.entity.TableStatResult;

public class HBaseWebUICrawler {
	private static final Pattern MAX_ROW_SIZE_PATTERN = Pattern.compile("max = (\\d+)");

	private static final Pattern BIGGEST_ROWKEY_PATTERN = Pattern.compile("Key of biggest row: (\\w+)");

	public static Set<String> getRegionServerSet(String masterServerUrl) {
		String masterServerHtml = HttpUtils.httpGet(masterServerUrl);
		Document masterServerDocument = Jsoup.parse(masterServerHtml);
		Elements masterServerElements = masterServerDocument.select("section").eq(0).select(".tab-content td a");
		Set<String> result = new HashSet<String>();
		for (Element element : masterServerElements) {
			String href = element.attr("href");
			String resionServer = href.split("/")[2];
			result.add(resionServer);
		}
		return result;
	}

	public static List<TableStatResult> getStatsInfoByTable(String clusterName, String regionServer, String tableName) {
		String regionServerUrl = new StringBuilder("http://").append(regionServer).append("/").toString();
		String regionServerHtml = HttpUtils.httpGet(regionServerUrl + "rs-status");
		Document regionServerDocument = Jsoup.parse(regionServerHtml);
		Elements regionServerElements = regionServerDocument.select(".tab-content td a");
		String tableNameSuffix = tableName + ",";
		Set<String> elementSet = new HashSet<String>();
		List<TableStatResult> result = new ArrayList<TableStatResult>();
		for (Element element : regionServerElements) {
			String elementText = element.text();
			if (!elementText.startsWith(tableNameSuffix) || elementSet.contains(elementText)) {
				continue;
			}
			elementSet.add(elementText);
			String tableRegionServerUrl = regionServerUrl + element.attr("href");
			String tableRegionHtml = HttpUtils.httpGet(tableRegionServerUrl);
			Document tableRegionDocument = Jsoup.parse(tableRegionHtml);
			// 解析列族
			Elements cfSet = tableRegionDocument.select("h3");
			List<String> cfList = new ArrayList<String>();
			for (Element cf : cfSet) {
				if (cf.text().contains("Column Family")) {
					cfList.add(cf.text().split(":")[1].trim());
				}
			}
			// 每个列族对应一张表格
			Elements storeFileTableSet = tableRegionDocument.select("table");
			for (int i = 0; i < cfList.size(); i++) {
				Element storeFileTable = storeFileTableSet.get(i);
				Elements storeFileSet = storeFileTable.select("tr");
				for (int j = 1; j < storeFileSet.size(); j++) {
					Element storeFile = storeFileSet.get(j);
					String storeFileUrl = regionServerUrl + storeFile.select("a").attr("href");
					// storeFile路径
					String storeFilePath = storeFile.select("a").text();
					// storeFile大小
					String storeFileSize = storeFile.select("td").eq(1).text();
					String storeFileUrlHtml = HttpUtils.httpGet(storeFileUrl);

					TableStatResult tableStatInfo = new TableStatResult(null, clusterName, tableName, regionServer,
							cfList.get(i), tableRegionServerUrl, storeFilePath, Integer.parseInt(storeFileSize), null,
							null, new Date());

					Matcher biggestRowKeyMatcher = BIGGEST_ROWKEY_PATTERN.matcher(storeFileUrlHtml);
					if (biggestRowKeyMatcher.find()) {
						String biggestRowKey = biggestRowKeyMatcher.group().split(":")[1].trim();
						tableStatInfo.setBiggestRowKey(biggestRowKey);
					}

					Matcher maxRowSizeMatcher = MAX_ROW_SIZE_PATTERN.matcher(storeFileUrlHtml);
					int k = 1;
					while (maxRowSizeMatcher.find()) {
						if (k++ == 2) {
							Integer maxRowSize = Integer.parseInt(maxRowSizeMatcher.group().split("=")[1].trim());
							tableStatInfo.setMaxRowSize(maxRowSize);
							result.add(tableStatInfo);
							break;
						}
					}
				}
			}
		}
		return result;
	}

}
