package com.meiyou.hbase.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.hbase.manager.entity.TableStatResult;
import com.meiyou.hbase.manager.service.TableStatResultService;
import com.meiyou.hbase.manager.utils.JsonUtils;

@Controller
@RequestMapping("/cluster/{clusterName}")
public class TableStatResultController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TableStatResultController.class);

	@Autowired
	private TableStatResultService tableStatResultService;

	@RequestMapping("/table/stat/result/list")
	public ModelAndView list(@PathVariable String clusterName) {
		LOGGER.info("Query table stat result list");
		List<TableStatResult> tableStatResultList = tableStatResultService.listTableStatResultByClusterName(clusterName);
		ModelAndView mv = new ModelAndView();
		mv.addObject("clusterName", clusterName);
		mv.addObject("tableStatResultList", JsonUtils.toJSON(tableStatResultList));
		mv.setViewName("table_stat_result_list");
		return mv;
	}

}
