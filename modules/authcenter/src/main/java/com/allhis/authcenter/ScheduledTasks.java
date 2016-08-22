package com.allhis.authcenter;

import com.allhis.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTasks {
	private static Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	AuthService authService;

    public ScheduledTasks(){
    }

    //删除全局数据并重新初始化
    @Scheduled(cron = "${jobs.schedule1}")
    public void reinit() throws Exception{
//    	log.info("************************ reinit **************************");
//    	authService.reinitGlobalData();
////		authService.getMaxtest();
//    	log.info("************************end reinit **************************");
//
    }

	//定期截断login_history,loginfaillog
	@Scheduled(cron = "${jobs.schedule2}")
	public void task2() throws Exception{
		log.info("************************ delete from history **************************");
		authService.clearData();
		log.info("************************end delete from history **************************");
	}

	//定期重新加载数据到内存表
	@Scheduled(cron = "${jobs.schedule3}")
	public void task3() throws Exception{
		log.info("************************ load data to mem table **************************");
		authService.reinitMemoryTable();
		log.info("************************end load data to mem table **************************");
	}

	//定期删除内存黑名单里的超时数据
	@Scheduled(cron = "${jobs.schedule4}")
	public void task4() throws Exception{
		log.info("************************ delete from blacklist **************************");
		authService.deleteFromBlackList();
		log.info("************************end delete from blacklist **************************");
	}

}
