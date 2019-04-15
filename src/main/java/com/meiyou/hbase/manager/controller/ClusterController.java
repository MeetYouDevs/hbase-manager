package com.meiyou.hbase.manager.controller;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.meiyou.hbase.manager.entity.Cluster;
import com.meiyou.hbase.manager.service.ClusterService;
import com.meiyou.hbase.manager.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ClusterController {
	@Autowired
	private ClusterService clusterService;

	@RequestMapping("/cluster")
	public ModelAndView list(ModelAndView mv) {
		log.info("Query cluster list");
		List<Cluster> clusterList = clusterService.listCluster();
		mv.addObject("clusterList", JsonUtils.toJSON(clusterList));
		mv.addObject("nav","cluster");
		mv.setViewName("cluster_list");
		return mv;
	}

	@RequestMapping("/cluster/{clusterName}")
	public ModelAndView detail(ModelAndView mv, @PathVariable String clusterName) {
		log.info("Query cluster [{}] detail", clusterName);
		Cluster cluster = clusterService.getClusterByName(clusterName);
		mv.addObject("cluster", cluster);
		mv.addObject("nav","cluster");
		mv.setViewName("cluster_detail");
		return mv;
	}

	@RequestMapping("/cluster/add")
	public ModelAndView add(ModelAndView mv) {
		mv.setViewName("cluster_add");
		mv.addObject("nav","cluster");
		return mv;
	}

	@RequestMapping("/cluster/{clusterName}/edit")
	public ModelAndView edit(ModelAndView mv, @PathVariable String clusterName) {
		log.info("Edit cluster [{}] info", clusterName);
		Cluster cluster = clusterService.getClusterByName(clusterName);
		mv.addObject("cluster", cluster);
		mv.addObject("nav","cluster");
		mv.setViewName("cluster_edit");
		return mv;
	}

	@RequestMapping("/cluster/save")
	public ModelAndView save(ModelAndView mv, Cluster cluster) {
		log.info("Save cluster info: {}", cluster.toString());
		clusterService.save(cluster);
		mv.addObject("nav","cluster");
		mv.setViewName("cluster_edit_result");
		return mv;
	}

	@RequestMapping("/cluster/delete")
	public ModelAndView save(ModelAndView mv, Integer id) {
		log.info("Delete cluster id: [{}[", id);
		clusterService.deleteById(id);
		List<Cluster> clusterList = clusterService.listCluster();
		mv.addObject("clusterList", clusterList);
		mv.addObject("nav","cluster");
		mv.setViewName("cluster_list");
		return mv;
	}

	@RequestMapping("/api/cluster/list")
	@ResponseBody
	public String apiList() {
		List<Cluster> clusterList = clusterService.listCluster();
		JSONArray json = (JSONArray) JSONArray.toJSON(clusterList);
		return json.toJSONString();
	}

}
