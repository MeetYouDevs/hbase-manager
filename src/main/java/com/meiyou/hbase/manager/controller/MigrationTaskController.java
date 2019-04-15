package com.meiyou.hbase.manager.controller;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.constant.Constant;
import com.meiyou.hbase.manager.core.Result;
import com.meiyou.hbase.manager.core.ResultGenerator;
import com.meiyou.hbase.manager.entity.MigrationTask;
import com.meiyou.hbase.manager.service.MigrationTaskService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/mig/task")
@Slf4j
public class MigrationTaskController {

    @Autowired
    private MigrationTaskService migrationTaskService;

    @RequestMapping("")
    public ModelAndView list(){

        List<MigrationTask> migrationTaskList =  migrationTaskService.list();

        ModelAndView mv = new ModelAndView();
        mv.addObject("migrationTaskList", JSON.toJSONString(migrationTaskList));
        mv.addObject("nav", "mig");

        mv.setViewName("migration_task_list");

        return mv;
    }

    @RequestMapping("/edit/{migrationId}")
    public ModelAndView edit(@PathVariable Integer migrationId){

        MigrationTask task = migrationTaskService.findById(migrationId);

        ModelAndView mv = new ModelAndView();
        mv.addObject("nav","mig");
        mv.addObject("task", task);

        mv.setViewName("migration_task_edit");
        return mv;
    }

    @GetMapping("/add")
    public ModelAndView toAdd(){
        ModelAndView mv = new ModelAndView();
        mv.addObject("nav","mig");
        mv.addObject("task",new MigrationTask());
        mv.setViewName("migration_task_add");
        return mv;
    }

    @PostMapping("/add")
    public Result<String> saveOrUpdate(MigrationTask task){
        log.info("saveOrUpdate:{}", JSON.toJSONString(task));
        Integer id = task.getId();
        if(id!=null){//
            MigrationTask oldTask = migrationTaskService.findById(id);
            if(oldTask==null){
                return ResultGenerator.genFailResult("任务不存在! task="+id);
            }
            task.setCreatedDate(oldTask.getCreatedDate());
            if(task.getStatus() != Constant.STATUS_NORMAL || task.getCustomCronOn()!=1){
                try {
                    migrationTaskService.unDeployMigration(task.getId());
                } catch (SchedulerException e) {
                    return ResultGenerator.genFailResult("任务下线失败！task="+task.getId()+" msg="+e.getMessage());
                }
            }

        }else{
            //新增
            task.setCreatedDate(new Date());
        }

        migrationTaskService.save(task);

        if(task.getStatus()== Constant.STATUS_NORMAL && task.getCustomCronOn()==1){//状态正常&开启定时
            try {
                migrationTaskService.deployMigration(task);
            } catch (SchedulerException e) {
                return ResultGenerator.genFailResult("任务部署失败！task="+task.getId()+" msg="+e.getMessage());
            }
        }

        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/run")
    public Result<String> run(Integer id){

        MigrationTask task = migrationTaskService.findById(id);
        Date fireTime = new Date();
        int logId = migrationTaskService.createTaskLog(task,fireTime,Constant.FIRE_TYPE_MANUAL);
        migrationTaskService.runMigrationAsync(logId,task,fireTime);//异步的
//        int logId =  migrationTaskService.runByManual(id);
        if(logId>0){
           return ResultGenerator.genSuccessResult(String.valueOf(logId));
        }else{
           return ResultGenerator.genFailResult("任务启动失败!");
        }
    }

    @PostMapping("/runOnce")
    public Result<String> runOnce(Integer id){
        try {
            migrationTaskService.runOnce(id);
        } catch (SchedulerException e) {
            return ResultGenerator.genFailResult("任务启动失败！task="+id+" msg="+e.getMessage());
        }
        return ResultGenerator.genSuccessResult();
    }

    @PostMapping("/del")
    public Result<String> delete(Integer id){
        log.info("delete migration task id={}",id);
         MigrationTask task = migrationTaskService.findById(id);
        if (id !=null && task!=null){
            try {
            	migrationTaskService.unDeployMigration(id);
                migrationTaskService.delete(id);
                return ResultGenerator.genSuccessResult();
            } catch (SchedulerException e) {
                log.error(e.getMessage());
                return ResultGenerator.genFailResult("任务删除失败!");
            }
        }else {
            return ResultGenerator.genFailResult("任务不存在，删除失败!");
        }
    }

    //清理失效的job
    @PostConstruct
    public void initialize() {
        try {
            migrationTaskService.cleanExpireMigrationJobs();
            log.info("INIT SUCCESS");
        } catch (SchedulerException e) {
            log.info("INIT EXCEPTION : " + e.getMessage());
        }
    }

    @InitBinder
    protected void init(HttpServletRequest request, ServletRequestDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
    }
}
