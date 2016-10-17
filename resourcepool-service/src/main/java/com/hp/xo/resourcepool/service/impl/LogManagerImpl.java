package com.hp.xo.resourcepool.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dao.LogDao;
import com.hp.xo.resourcepool.model.ActiveUser;
import com.hp.xo.resourcepool.model.Log;
import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.request.ListLogRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.LogManager;

@Service(value="logManager")
@Transactional(propagation=Propagation.REQUIRES_NEW)
public class LogManagerImpl extends GenericManagerImpl<Log,Long>implements LogManager {
	
	@Autowired
	private  GenericCloudServerManagerImpl genericCloudServerManager;
	
	public void setGenericCloudServerManager(
			GenericCloudServerManagerImpl genericCloudServerManager) {
		this.genericCloudServerManager = genericCloudServerManager;
	}

	public LogManagerImpl(){
		super(); 
	}
	
	public LogManagerImpl(LogDao logDao){
		super(logDao);
		this.logDao = logDao;
	}

	LogDao logDao;

	@Transactional(readOnly = false)
	public Log save(Log log) {

		return (Log) (logDao.save(log));
	}
	
	
	@Autowired
	public void setLogDao(LogDao logDao) {
		this.dao = logDao;
		this.logDao = logDao;
	}



	@Override
	@Transactional(readOnly = true)
	public ListResponse<Log> list(ListLogRequest request) {

		ListResponse<Log> result = new ListResponse<Log>();
		List<Log> list = null;
		Integer count = 0;
		
		if (null != request.getId()) {
			WorkOrder example = new WorkOrder();
			example.setId(request.getId());
			list = (List<Log>) logDao.findByExample(example);
			if(list!=null){
				count = list.size();
			}
		} else {
			
			if (StringUtils.isNotBlank(request.getKeyword())) {
				request.setLoginName(request.getKeyword());
			}
			//admin 1
        	//domainadmin 2
        	//user 0
			if(request.getRole().intValue()==ActiveUser.DOMAINADMIN){
				Map<String,Object[]> params = new HashMap<String,Object[]>();
				params.put("command", new Object[]{"listUsers"});
				params.put("apikey",new Object[]{request.getApikey()});
				params.put("secretkey",new Object[]{request.getSecretkey()});
				//params.put("account",new Object[]{request.getAccount()});
				params.put("domainid",new Object[]{request.getDomainid()});
				params.put("response", new Object[]{"json"});
				Response response = genericCloudServerManager.get(params);
				System.out.println("HttpStatus.OK.value()="+HttpStatus.OK.value());
				if(response!=null && response.getStatusCode()==HttpStatus.OK.value() &&  StringUtils.isNotBlank(response.getResponseString())){
					JSONObject json = JSONObject.fromObject(response.getResponseString());
					if(json.containsKey("listusersresponse")){
						json = json.getJSONObject("listusersresponse");
						if(json.containsKey("count") && json.getInt("count")>0 && json.containsKey("user")){
							JSONArray jsonArray = json.getJSONArray("user");
							String[] users = new String[jsonArray.size()];
							for(int i= 0; i<jsonArray.size();++i){
								users[i]=jsonArray.getJSONObject(i).getString("username");
							}
							request.setOperationUsers(users);
						}
					}
				}
				/*{ "listaccountsresponse" : { "count":23 ,"account" : [  {"id":"c3c700f0-7d3b-11e3-bf84-005056941242","name":"admin","accounttype":1,"domainid":"67f97989-7d3b-11e3-bf84-005056941242","domain":"ROOT","vmlimit":"Unlimited","vmtotal":18,"vmavailable":"Unlimi*/
 			}else if(request.getRole().intValue()==ActiveUser.USER){
 				request.setOperationUsers(new String[]{request.getUserName()});
 			}else if(request.getRole().intValue() == ActiveUser.ADMIN){
 				//所有用户，不加条件限制
 			}
			list = (List<Log>) logDao.list(request, request.getPage(), request.getPagesize());
			count = logDao.count(request);
				
		}
		result.setCount(count);
		result.setResponses(list);
		return result;
	}

	/**
	 * 记录日志
	 * @param clientIP  客户端调用IP
	 * @param module    模块
	 * @param operation 操作
	 * @param username  操作用户名
	 * @param content   操作内容
	 * @param result    操作结果
	 * @param info      操作结果原因描述
	 */
	public void log(String clientIP, String module, String operation,
			String username, String content, String result, String info) {
//public Log(String loginName, String content, String operation, String info, String result, String module, String ip) {
//Log log = new Log(loginName, "用户[" + loginName + "]登录成功。", LogConstants.LOGIN, null, LogConstants.SUCCESS,LogConstants.AUTH, clientIp);
//Log log = new Log(loginName, "用户[" + loginName + "]登录失败。", LogConstants.LOGIN,"找不到匹配的认证信息。", LogConstants.FAIL, LogConstants.AUTH, clientIp);
		Log log = new Log();
		log.setContent(content);
		
		log.setIp(clientIP);
		log.setModule(module);
		log.setOperation(operation);
		log.setLoginName(username);
		log.setContent(content);
		log.setResult(result);
		log.setInfo(info);
		
		log.setCreatedBy(username);
		log.setModifiedBy(username);
		log.setCreatedOn(new Date());
		log.setModifiedOn(new Date());
		
		this.logDao.save(log);
		
	}

	@Override
	public void cleanLog() {
		logDao.cleanLog();
	}

}
