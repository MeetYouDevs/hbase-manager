package com.meiyou.hbase.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.hbase.manager.entity.TableReplicationTask;
import com.meiyou.hbase.manager.service.TableReplicationTaskService;
import com.meiyou.hbase.manager.utils.JsonUtils;

@Controller
public class TableReplicationTaskController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableReplicationTaskController.class);

	@Autowired
	private TableReplicationTaskService tableReplicationTaskService;

	@RequestMapping("/table/replication/task/list")
	public ModelAndView listReplicationTask(ModelAndView mv) {
		LOGGER.info("Query replication task list");
		return getReplicationTaskList(mv);
	}

	@RequestMapping("/table/replication/task/add")
	public ModelAndView addReplicationTask(ModelAndView mv, String sourceCluster, String sourceTable,
			String targetCluster, String targetTable) {
		LOGGER.info("Add table [{}] replication task", sourceTable);
		tableReplicationTaskService.addReplicationTask(sourceCluster, sourceTable, targetCluster, targetTable);
		return getReplicationTaskList(mv);
	}

	@RequestMapping("/table/replication/task/delete")
	public ModelAndView deleteReplicationTask(ModelAndView mv, Integer id) {
		LOGGER.info("Delete id [{}] replication task", id);
		tableReplicationTaskService.deleteReplicationTask(id);
		return getReplicationTaskList(mv);
	}

	public ModelAndView getReplicationTaskList(ModelAndView mv) {
		List<TableReplicationTask> tableReplicationTaskList = tableReplicationTaskService.listReplicationTask();
		mv.addObject("tableReplicationTaskList", JsonUtils.toJSON(tableReplicationTaskList));
		mv.setViewName("table_replication_task_list");
		return mv;
	}

}
