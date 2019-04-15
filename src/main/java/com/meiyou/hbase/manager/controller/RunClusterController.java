package com.meiyou.hbase.manager.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.hbase.manager.entity.RunCluster;
import com.meiyou.hbase.manager.service.RunClusterService;
import com.meiyou.hbase.manager.utils.JsonUtils;

@RestController
@RequestMapping("/run/cluster")
public class RunClusterController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RunClusterController.class);

	@Autowired
	private RunClusterService runClusterService;

	@RequestMapping("")
	public ModelAndView list(ModelAndView mv) {
		LOGGER.info("Query run cluster list");
		return getRunClusterList(mv);
	}

	@RequestMapping("/list")
	@ResponseBody
	public String list() {
		LOGGER.info("Query run cluster list");
		List<RunCluster> runClusterList = runClusterService.listRunCluster();
		return JsonUtils.toJSON(runClusterList);
	}

	@RequestMapping("/add")
	public ModelAndView addRunCluster(ModelAndView mv, Integer id, String clusterName, String ip, String port,
			String sshUser, String sshPassword) {
		LOGGER.info("Add or update run cluster: id={}, clusterName={}, ip={}, port={}, sshUser={}, sshPassword={}.", id,
				clusterName, ip, port, sshUser, sshPassword);
		RunCluster runCluster = new RunCluster(id, clusterName, ip, port, sshUser, sshPassword);
		runClusterService.addRunCluster(runCluster);
		return getRunClusterList(mv);
	}

	@RequestMapping("/delete")
	public ModelAndView deleteRunCluster(ModelAndView mv, Integer id) {
		LOGGER.info("Delete run cluster: id={}.", id);
		runClusterService.deleteRunCluster(id);
		return getRunClusterList(mv);
	}

	private ModelAndView getRunClusterList(ModelAndView mv) {
		List<RunCluster> runClusterList = runClusterService.listRunCluster();
		mv.addObject("runClusterList", JsonUtils.toJSON(runClusterList));
		mv.setViewName("run_cluster");
		return mv;
	}
}
