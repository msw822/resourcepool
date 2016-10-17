package com.hp.xo.resourcepool.dao;


import java.util.List;

import com.hp.xo.resourcepool.model.Log;
import com.hp.xo.resourcepool.request.ListLogRequest;

public interface LogDao extends GenericDao<Log,Long> {
	public int count(ListLogRequest logParam);
	
	public List<Log> list(ListLogRequest logParam, int pageIndex, int pageSize);
	
	public void cleanLog();
}
