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

import com.meiyou.hbase.manager.entity.TableCompactTask;
import com.meiyou.hbase.manager.service.TableCompactTaskService;
import com.meiyou.hbase.manager.utils.JsonUtils;

@Controller
@RequestMapping("/cluster/{clusterName}")
public class TableCompactTaskController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableCompactTaskController.class);

	@Autowired
	private TableCompactTaskService tableCompactTaskService;
	
	@RequestMapping("/table/compact/task/list")
	public ModelAndView listCompactTask(ModelAndView mv, @PathVariable String clusterName) {
		LOGGER.info("Query compact task list");
		return getCompactTaskList(mv, clusterName);
	}
	
	@RequestMapping("/table/compact/task/edit")
	@ResponseBody
	public String edit(ModelAndView mv, Integer id) {
		LOGGER.info("Edit compact task, id: []" + id);
		TableCompactTask tableCompactTask = tableCompactTaskService.findById(id);
		return JsonUtils.toJSON(tableCompactTask);
	}

	@RequestMapping("/table/compact/task/add")
	public ModelAndView addCompactTask(ModelAndView mv, Integer id, @PathVariable String clusterName, String tableName, Integer intervalPosition, Integer interval, Integer offsetPosition,
			Integer offset) {
		LOGGER.info("Add table [{}] compact task", tableName);
		tableCompactTaskService.addCompactTask(id, clusterName, tableName, intervalPosition, interval, offsetPosition,
				offset);
		return getCompactTaskList(mv, clusterName);
	}

	@RequestMapping("/table/compact/task/delete")
	public ModelAndView deleteCompactTask(ModelAndView mv, @PathVariable String clusterName, Integer id) {
		LOGGER.info("Delete compact task, id: []" + id);
		tableCompactTaskService.deleteById(id);
		return getCompactTaskList(mv, clusterName);
	}
	
	private ModelAndView getCompactTaskList(ModelAndView mv, String clusterName) {
		List<TableCompactTask> compactTaskList = tableCompactTaskService.listCompactTask(clusterName);
		mv.addObject("clusterName", clusterName);
		mv.addObject("compactTaskList", JsonUtils.toJSON(compactTaskList));
		mv.setViewName("table_compact_task_list");
		return mv;
	}
}
