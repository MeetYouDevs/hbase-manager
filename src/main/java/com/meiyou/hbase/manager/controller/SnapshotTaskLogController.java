package com.meiyou.hbase.manager.controller;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.core.Result;
import com.meiyou.hbase.manager.core.ResultGenerator;
import com.meiyou.hbase.manager.entity.SnapshotTaskLog;
import com.meiyou.hbase.manager.service.SnapshotTaskLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cluster/{clusterName}/snapshot/task/log")
public class SnapshotTaskLogController {

    @Autowired
    private SnapshotTaskLogService snapshotTaskLogService;

    @RequestMapping("")
    public ModelAndView list(@PathVariable String clusterName){

        List<SnapshotTaskLog> snapshotTaskLogList = snapshotTaskLogService.list();

        ModelAndView mv = new ModelAndView();
        mv.addObject("nav","snapshot");
        mv.addObject("clusterName", clusterName);
        mv.addObject("snapshotTaskLogList", JSON.toJSONString(snapshotTaskLogList));

        mv.setViewName("snapshot_task_log_list");
        return mv;
    }

    @PostMapping("/setDeleted")
    public Result<String> setDeleted(Integer id, Integer hasDeleted ){
        log.info("delete snapshot task id={}",id);
        if (id!=null&&snapshotTaskLogService.findById(id)!=null){
            snapshotTaskLogService.updateHasDeleted(id,hasDeleted);
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败!");
        }
    }
}
