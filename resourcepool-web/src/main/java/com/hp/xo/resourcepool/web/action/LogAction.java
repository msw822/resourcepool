package com.hp.xo.resourcepool.web.action;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.hp.xo.resourcepool.model.Log;
import com.hp.xo.resourcepool.request.ListLogRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.LogManager;
import com.hp.xo.resourcepool.web.action.core.BaseAction;
import com.hp.xo.resourcepool.utils.Page;

public class LogAction extends BaseAction {

	private static final long serialVersionUID = 4450577432337012813L;
	@Autowired
	private LogManager logManager =  null;
	public LogAction() {
		super();
	}
	
	public void setLogManager(LogManager logManager) {
		this.logManager = logManager;
	}

	public String list(){
		/*Date start = requestParams.get("start")==null?null : DateUtil.parseDateString(DateUtil.GMT_TIMEZONE, (String)requestParams.get("start")[0]);
		Date end = requestParams.get("end")==null?null : DateUtil.parseDateString(DateUtil.GMT_TIMEZONE, (String)requestParams.get("end")[0]);
		String module = requestParams.get("module")==null?null:(String)requestParams.get("module")[0];
		String operation = requestParams.get("operation")==null?null:(String)requestParams.get("operation")[0];
		String username = requestParams.get("username")==null?null:(String)requestParams.get("username")[0];
		int pageNo = requestParams.get("page")==null?null:Integer.parseInt(requestParams.get("page")[0].toString());
		int pageSize = requestParams.get("pagesize")==null?null:Integer.parseInt(requestParams.get("pagesize")[0].toString());
		String responseType = requestParams.get("response")==null?"json":(String)requestParams.get("response")[0];
		Page p = new Page();
		p.setPageNo(pageNo);
		p.setPageSize(pageSize);*/
		
		
		ListLogRequest request = (ListLogRequest) this.wrapRequest(new ListLogRequest());
		request.setRole(this.activeUser.getRole());
		request.setUserName(this.activeUser.getUserName());
		
		ListResponse<Log> response = logManager.list(request);
		
		int totalPage = 0;
		if (null != response.getResponses()) {
			totalPage = response.getCount();
		}
		Page page = new Page();
		page.setTotalCount(totalPage);
		page.setPageNo(request.getPage());
		page.setPageSize(request.getPagesize());
		
		JSONObject jsonPage = this.getJsonPage(page);
		jsonPage.put("logs", response.getResponses());
		
		// 返回响应
		this.renderText(jsonPage.toString());
		return NONE;
	}
}
