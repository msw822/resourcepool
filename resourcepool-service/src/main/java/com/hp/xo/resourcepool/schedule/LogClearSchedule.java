package com.hp.xo.resourcepool.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.service.LogManager;



@Service(value = "logClearSchedule")
public class LogClearSchedule {

	private final Logger log = LoggerFactory.getLogger(LogClearSchedule.class);
	
	@Autowired
	private LogManager logManager;

	public void syn() {

		log.info("-----------------------------------数据清理  begin--------------------------------------------------------");

		logManager.cleanLog();
		
		log.info("-----------------------------------数据清理  end--------------------------------------------------------");

	}


	public static void main(String[] args) {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		LogClearSchedule synSchedule = (LogClearSchedule) applicationContext
				.getBean("logClearSchedule");
		synSchedule.syn();
	}


}
