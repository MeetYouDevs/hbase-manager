package com.meiyou.hbase.manager.controller;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.entity.TableSchema;
import com.meiyou.hbase.manager.service.TableSchemaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/schema/{clusterName}/{tableName}")
public class TableSchemaController {
	private static final Logger logger = LoggerFactory.getLogger(TableSchemaController.class);

	@Autowired
	private TableSchemaService tableSchemaService;

	@RequestMapping("")
	public ModelAndView home(@PathVariable String clusterName, @PathVariable String tableName) {
		ModelAndView mv = new ModelAndView();

		mv.addObject("clusterName", clusterName);
		mv.addObject("tableName", tableName);

		mv.setViewName("table_schema");
		return mv;
	}

	@RequestMapping(value = "/list")
	@ResponseBody
	public String list(@PathVariable String clusterName, @PathVariable String tableName) {
		logger.info("clusterName={},tableName={}", clusterName, tableName);
		List<TableSchema> tableSchemaList = tableSchemaService.get(clusterName, tableName);
		return JSON.toJSONString(tableSchemaList);
	}

	@RequestMapping(value = "/del/{ids}")
	public String delete(@PathVariable String clusterName, @PathVariable String tableName, @PathVariable String ids) {
		logger.info("clusterName={},tableName={} ,ids={}", clusterName, tableName, ids);
		if (ids != null) {
			for (String id : ids.split(",")) {
				tableSchemaService.deleteById(Integer.parseInt(id));
			}
		}
		return String.format("redirect:/schema/%s/%s", clusterName, tableName);
	}

	@RequestMapping(value = "add")
	public String add(@PathVariable String clusterName, @PathVariable String tableName, String columnName,
			String dataType, Integer nameType, Integer id) {
		TableSchema schema = new TableSchema(id, clusterName, tableName, columnName, dataType, nameType);
		logger.info("receive info:" + JSON.toJSONString(schema));
		tableSchemaService.saveOrUpdate(schema);
		return String.format("redirect:/schema/%s/%s", clusterName, tableName);
	}

}
