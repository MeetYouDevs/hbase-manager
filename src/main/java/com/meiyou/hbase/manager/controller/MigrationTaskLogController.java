package com.meiyou.hbase.manager.controller;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.entity.MigrationTaskLog;
import com.meiyou.hbase.manager.service.MigrationTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/mig/task/log")
public class MigrationTaskLogController {
    @Autowired
    private MigrationTaskLogService migrationTaskLogService;

    @RequestMapping("")
    public ModelAndView list(){

        List<MigrationTaskLog> migrationTaskLogList = migrationTaskLogService.list();

        ModelAndView mv = new ModelAndView();
        mv.addObject("nav","mig");
        mv.addObject("migrationTaskLogList", JSON.toJSONString(migrationTaskLogList));

        mv.setViewName("migration_task_log_list");
        return mv;
    }
}
