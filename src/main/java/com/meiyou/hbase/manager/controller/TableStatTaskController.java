package com.meiyou.hbase.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.hbase.manager.entity.TableStatTask;
import com.meiyou.hbase.manager.service.TableStatTaskService;
import com.meiyou.hbase.manager.utils.JsonUtils;

@Controller
@RequestMapping("/cluster/{clusterName}")
public class TableStatTaskController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableStatTaskController.class);

	@Autowired
	private TableStatTaskService tableStatTaskService;

	@RequestMapping("/table/stat/task/list")
	public ModelAndView listStatTask(ModelAndView mv, @PathVariable String clusterName) {
		LOGGER.info("Query stat task list");
		return getStatTaskList(mv, clusterName);
	}

	@RequestMapping("/table/stat/task/edit")
	@ResponseBody
	public String edit(ModelAndView mv, Integer id) {
		LOGGER.info("Edit stat task");
		TableStatTask tableStatTask = tableStatTaskService.findById(id);
		return JsonUtils.toJSON(tableStatTask);
	}

	@RequestMapping("/table/stat/task/add")
	public ModelAndView addStatTask(ModelAndView mv, Integer id, @PathVariable String clusterName, String tableName,
			Integer intervalPosition, Integer interval, Integer offsetPosition, Integer offset) {
		LOGGER.info("Add table [{}] stat task", tableName);
		tableStatTaskService.addStatTask(id, clusterName, tableName, intervalPosition, interval, offsetPosition,
				offset);
		return getStatTaskList(mv, clusterName);
	}

	@RequestMapping("/table/stat/task/delete")
	public ModelAndView deleteStatTask(ModelAndView mv, @PathVariable String clusterName, Integer id) {
		LOGGER.info("Delete table stat task, id: {}", id);
		tableStatTaskService.deleteById(id);
		return getStatTaskList(mv, clusterName);
	}

	@RequestMapping("/table/stat/task/execute")
	public ModelAndView executeStatTask(ModelAndView mv, @PathVariable String clusterName, String tableName) {
		LOGGER.info("Execute table [{}] stat task", tableName);
		tableStatTaskService.executeStatTask(clusterName, tableName);
		return getStatTaskList(mv, clusterName);
	}

	private ModelAndView getStatTaskList(ModelAndView mv, String clusterName) {
		List<TableStatTask> statTaskList = tableStatTaskService.listStatTask(clusterName);
		mv.addObject("clusterName", clusterName);
		mv.addObject("statTaskList", JsonUtils.toJSON(statTaskList));
		mv.setViewName("table_stat_task_list");
		return mv;
	}
}
