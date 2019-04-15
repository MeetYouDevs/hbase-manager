package com.meiyou.hbase.manager.controller;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.core.Result;
import com.meiyou.hbase.manager.core.ResultGenerator;
import com.meiyou.hbase.manager.entity.MigrationTaskDetailLog;
import com.meiyou.hbase.manager.service.MigrationTaskDetailLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/mig/task/detail/log")
@Slf4j
public class MigrationTaskDetailLogController {

    @Autowired
    private MigrationTaskDetailLogService migrationTaskDetailLogService;

    @RequestMapping("/{logId}")
    public ModelAndView detail(@PathVariable Integer logId){

        log.info(" detail logId = {} ",logId);

        ModelAndView mv = new ModelAndView();
        mv.addObject("nav","mig");
        mv.addObject("logId", logId);

        List<MigrationTaskDetailLog> detailLogList =  migrationTaskDetailLogService.findAllByLogId(logId,-1L);
        mv.addObject("detailLogList", JSON.toJSONString(detailLogList));
        mv.addObject("maxId",maxDetailLogId(detailLogList));

        mv.setViewName("migration_task_detail_log");
        return mv;
    }

    @RequestMapping("/{logId}/append")
    public Result<String> append(@PathVariable Integer logId,Long maxId){
        log.info("logId = {} , maxId = {}",logId,maxId);
        List<MigrationTaskDetailLog> detailLogList = migrationTaskDetailLogService.findAllByLogId(logId,maxId);
        return ResultGenerator.genSuccessResult(JSON.toJSONString(detailLogList));
    }

    private Long maxDetailLogId( List<MigrationTaskDetailLog> detailLogList){
        Long max = -1L;
        for(MigrationTaskDetailLog dl:detailLogList){
            if(dl.getId()>max){
                max = dl.getId();
            }
        }
        return max;
    }
}
