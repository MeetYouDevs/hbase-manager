package com.meiyou.hbase.manager.controller;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.core.Result;
import com.meiyou.hbase.manager.core.ResultGenerator;
import com.meiyou.hbase.manager.entity.SnapshotTask;
import com.meiyou.hbase.manager.service.SnapshotTaskService;
import com.meiyou.hbase.manager.service.TableService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/cluster/{clusterName}/snapshot/task")
@Slf4j
public class SnapshotTaskController {

    @Autowired
    private SnapshotTaskService snapshotTaskService;
    @Autowired
    private TableService tableService;

    @RequestMapping("")
    public ModelAndView list(@PathVariable String clusterName){
        List<SnapshotTask> taskList = snapshotTaskService.list(clusterName);
        List<String> tableNameList = tableService.listTableNameByClusterName(clusterName);
        ModelAndView mv = new ModelAndView();
        mv.addObject("nav","snapshot");
        mv.addObject("clusterName", clusterName);
        mv.addObject("snapshotTaskList", JSON.toJSONString(taskList));
        mv.addObject("tableNameList", tableNameList);//表名下拉菜单内容

        mv.setViewName("snapshot_task_list");
        return mv;
    }

    @PostMapping("/add")
    public Result<String> addSnapshotTask(@RequestBody SnapshotTask snapshotTask){
        snapshotTask.setCreateDate(new Date());
        log.info("addSnapshotTask:"+JSON.toJSONString(snapshotTask));

        SnapshotTask savedTask = snapshotTaskService.add(snapshotTask);

        if(savedTask!=null){
            snapshotTaskService.deploySnapshotTask(snapshotTask);
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("任务创建失败!");
        }
    }

    @PostMapping("/del")
    public Result<String> delSnapshotTask(Integer id){
        log.info("delete snapshot task id={}",id);
        if (id!=null&&snapshotTaskService.findById(id)!=null){
            try {
                snapshotTaskService.unDeploySnapshotTask(id);
                snapshotTaskService.delete(id);
                return ResultGenerator.genSuccessResult();
            } catch (SchedulerException e) {
                log.error(e.getMessage());
                return ResultGenerator.genFailResult("Job下线失败!");
            }
        }else {
            return ResultGenerator.genFailResult("删除失败!");
        }
    }

    @RequestMapping("/{id}/edit")
    public ModelAndView edit(@PathVariable Integer id, @PathVariable String clusterName){
        SnapshotTask task = snapshotTaskService.findById(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("nav","snapshot");
        mv.addObject("clusterName", clusterName);
        mv.addObject("task", task);

        mv.setViewName("snapshot_task_edit");
        return mv;
    }
}
