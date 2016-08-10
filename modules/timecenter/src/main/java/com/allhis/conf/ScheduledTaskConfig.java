package com.allhis.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by ljy on 15/11/26.
 * ok
 */
@Configuration
@EnableScheduling
public class ScheduledTaskConfig implements SchedulingConfigurer {
    @Value("${jobs.scheduledthreadpoolsize}")
    private int scheduledThreadPoolSize;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    //使用的@com.allcom.bean(destroyMethod="shutdown")。这样是为了确保当Spring应用上下文关闭的时候任务执行者也被正确地关闭。
    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(scheduledThreadPoolSize);
    }
}
