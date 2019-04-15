package com.meiyou.hbase.manager.controller;

import com.alibaba.fastjson.JSON;
import com.meiyou.hbase.manager.entity.DataCell;
import com.meiyou.hbase.manager.service.HTableDataService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@Controller
@RequestMapping("/data")
public class DataViewController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataViewController.class);
    
    @Autowired
    private HTableDataService hTableDataService;

    @RequestMapping("")
    public ModelAndView home(){
        LOGGER.info("-----home");
        //集群列表
        ModelAndView mv = new ModelAndView();
        mv.addObject("nav","data");
        mv.setViewName("data_view");
        return mv;
    }

    @RequestMapping("/{cluster}/query.api")
    @ResponseBody
    public String data(@PathVariable String cluster,String table,String rowKey,@RequestParam(name = "limit",defaultValue = "10") int limit){

        Map<String,Map<String,DataCell>> data;
        LOGGER.info("Data Query : cluster = {} , table = {} , rowKey = {} , limit = {}",cluster,table,rowKey,limit);

        if(StringUtils.isNotBlank(rowKey)){
            data = hTableDataService.findByKeyPrefix(cluster,table,rowKey,limit);
        }else{
            data = hTableDataService.scan(cluster,table,limit);
        }
        LOGGER.info("数据查询返回结果"+JSON.toJSONString(data));
        return JSON.toJSONString(data);
    }

}
