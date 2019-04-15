package com.meiyou.hbase.manager.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncTaskService {

    @Async // 通过@Async注解表明该方法是一个异步方法，如果注解在类级别，表明该类下所有方法都是异步方法，而这里的方法自动被注入使用ThreadPoolTaskExecutor 作为 TaskExecutor
    public void executeAsyncTask(Integer i){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("执行异步任务：" + i);
    }

    @Async
    public void executeAsyncTaskPlus(Integer i){
        System.out.println("执行异步任务+1：" + (i+1));
    }
}
