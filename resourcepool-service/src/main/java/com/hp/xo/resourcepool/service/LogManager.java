package com.hp.xo.resourcepool.service;

import com.hp.xo.resourcepool.model.Log;
import com.hp.xo.resourcepool.request.ListLogRequest;
import com.hp.xo.resourcepool.response.ListResponse;


public interface LogManager extends GenericManager<Log, Long> {

	ListResponse<Log> list(ListLogRequest request);
	
	/**
	 * 
	 * @param clientIP  客户端调用IP
	 * @param module    模块
	 * @param operation 操作
	 * @param username  操作用户名
	 * @param content   操作内容
	 * @param result    操作结果
	 * @param info      操作结果原因描述
	 */
	void log(String clientIP,String module,String operation,String username,String content,String result, String info);
	
	/**
	 * 
	 * cleanLog:日志清理. <br/> 
	 * 定时清理上一年的日志数据.<br/> 
	 *
	 */
	void cleanLog();
	
}
