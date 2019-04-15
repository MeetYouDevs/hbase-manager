package com.meiyou.hbase.manager.controller;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.core.Result;
import com.meiyou.hbase.manager.core.ResultGenerator;
import com.meiyou.hbase.manager.entity.MySnapshotDescription;
import com.meiyou.hbase.manager.service.SnapShotService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/cluster/{clusterName}/snapshot")
public class SnapShotController {
    @Autowired
    private SnapShotService snapShotService;

    @RequestMapping("")
    public ModelAndView list(@PathVariable String clusterName){
        List<MySnapshotDescription> descriptionList = snapShotService.list(clusterName);

        ModelAndView mv = new ModelAndView();
        mv.addObject("clusterName", clusterName);
        mv.addObject("snapshotList", JSON.toJSONString(descriptionList));

        mv.addObject("nav", "snapshot");

        mv.setViewName("snapshot_list");

        return mv;
    }

    @RequestMapping("/delete")
    public Result<String> delSnapshot(@PathVariable String clusterName, String name){
        if(StringUtils.isBlank(clusterName)||StringUtils.isBlank(name)){
            return ResultGenerator.genFailResult("集群和快照不能为空!");
        }

        boolean success = snapShotService.delSnapshot(clusterName,name);
        if(success){
            return ResultGenerator.genSuccessResult();
        }else{
            return ResultGenerator.genFailResult("删除快照失败!");
        }
    }

    //初始化快照自动清理任务
    @PostConstruct
    public void initialize() {
         snapShotService.deployAutoClean();
    }
}
