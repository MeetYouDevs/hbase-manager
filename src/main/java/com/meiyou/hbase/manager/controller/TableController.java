package com.meiyou.hbase.manager.controller;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.hbase.manager.core.Result;
import com.meiyou.hbase.manager.core.ResultGenerator;
import com.meiyou.hbase.manager.entity.Table;
import com.meiyou.hbase.manager.service.TableService;
import com.meiyou.hbase.manager.utils.JsonUtils;

@RestController
@RequestMapping("/cluster/{clusterName}")
public class TableController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableController.class);

	@Autowired
	private TableService tableService;

	@RequestMapping("/table/list")
	public ModelAndView list(@PathVariable String clusterName) {
		LOGGER.info("Query table list");
		ModelAndView mv = new ModelAndView();
		return getTableList(mv, clusterName);
	}
	
	@RequestMapping("/table/name/list")
	@ResponseBody
	public String listTableName(@PathVariable String clusterName) {
		LOGGER.info("Query tableName list");
		List<String> list = tableService.listTableNameByClusterName(clusterName);
		return JsonUtils.toJSON(list);
	}

	@RequestMapping("/table/add")
	public ModelAndView add(ModelAndView mv, @PathVariable String clusterName) {
		mv.addObject("clusterName", clusterName);
		mv.addObject("nav", "table");
		mv.setViewName("table_add");
		return mv;
	}

	@RequestMapping("/table/{tableName}/edit")
	public ModelAndView edit(ModelAndView mv, @PathVariable String clusterName, @PathVariable String tableName) {
		LOGGER.info("Edit table [{}] info", tableName);
		Table table = tableService.getTableByName(clusterName, tableName);
		mv.addObject("clusterName", clusterName);
		mv.addObject("table", table);
		mv.addObject("nav", "table");
		mv.setViewName("table_edit");
		return mv;
	}

	@PostMapping("/table/save")
	public Result<String> save(ModelAndView mv, @PathVariable String clusterName, String ddlStr, Integer updateFlag) {
		LOGGER.info("Add/Update table info: {}", ddlStr);
		try {
			if (updateFlag == 1) {
				tableService.update(clusterName, ddlStr);
			} else {
				tableService.save(clusterName, ddlStr);
			}
		} catch (Exception e) {
			return ResultGenerator.genFailResult("Add/Update table failed, msg: " + e.toString());
		}
		return ResultGenerator.genSuccessResult("Add/Update table success!");
	}

	@PostMapping("/table/{tableName}/enable")
	public Result<String> enable(ModelAndView mv, @PathVariable String clusterName, @PathVariable String tableName) {
		LOGGER.info("Enable table [{}]", tableName);
		try {
			tableService.enableTable(clusterName, tableName);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("Enable table failed, msg: " + e.toString());
		}
		return ResultGenerator.genSuccessResult("Enable table success!");
	}

	@PostMapping("/table/{tableName}/disable")
	public Result<String> disable(ModelAndView mv, @PathVariable String clusterName, @PathVariable String tableName) {
		LOGGER.info("Disable table [{}]", tableName);
		try {
			tableService.disableTable(clusterName, tableName);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("Disable table failed, msg: " + e.toString());
		}
		return ResultGenerator.genSuccessResult("Disable table success!");
	}

	@PostMapping("/table/{tableName}/delete")
	public Result<String> delete(ModelAndView mv, @PathVariable String clusterName, @PathVariable String tableName) {
		LOGGER.info("Delete table [{}]", tableName);
		try {
			tableService.deleteTable(clusterName, tableName);
		} catch (Exception e) {
			return ResultGenerator.genFailResult("Delete table failed, msg: " + e.toString());
		}
		return ResultGenerator.genSuccessResult("Delete table success!");
	}

	@RequestMapping("/namespace/{namespace}/table/{tableName}/getSpaceSize")
	@ResponseBody
	public String getSpaceSize(ModelAndView mv, @PathVariable String clusterName, @PathVariable String namespace,
			@PathVariable String tableName) {
		LOGGER.info("get table [{}] space size", tableName);
		long spaceSize = tableService.getTableSpaceSize(clusterName, namespace, tableName);
		return String.valueOf(spaceSize);
	}

	@RequestMapping("/api/tables")
	@ResponseBody
	public String tableList(@PathVariable String clusterName) {
		List<Table> list = tableService.listTableByClusterName(clusterName, false);
		JSONArray json = (JSONArray) JSONArray.toJSON(list);
		LOGGER.info(json.toJSONString());
		return json.toJSONString();
	}
	
	private ModelAndView getTableList(ModelAndView mv, String clusterName) {
		List<Table> list = tableService.listTableByClusterName(clusterName, true);
		mv.addObject("clusterName", clusterName);
		mv.addObject("tableList", JsonUtils.toJSON(list));
		mv.addObject("nav", "table");
		mv.setViewName("table_list");
		return mv;
	}
}
