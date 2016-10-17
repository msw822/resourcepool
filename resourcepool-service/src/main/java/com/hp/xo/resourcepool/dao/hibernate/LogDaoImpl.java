package com.hp.xo.resourcepool.dao.hibernate;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hp.xo.resourcepool.dao.LogDao;
import com.hp.xo.resourcepool.model.Log;
import com.hp.xo.resourcepool.request.ListLogRequest;
@Repository("logDao")
public class LogDaoImpl extends GenericDaoImpl<Log, Long> implements LogDao {

	public LogDaoImpl(Class<Log> clazz) {
		super(clazz);
	}

	public LogDaoImpl() {
		super(Log.class);
	}

	@Override
	public int count(ListLogRequest logParam) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Log.class);
		if (StringUtils.isNotBlank(logParam.getLoginName())) {
			criteria.add(Restrictions.eq("loginName", "%"+logParam.getLoginName()+"%"));
		}
		
		if(logParam.getOperationUsers()!=null && logParam.getOperationUsers().length>0){
				criteria.add(Restrictions.in("loginName", logParam.getOperationUsers()));
		}

		if (StringUtils.isNotBlank(logParam.getModule())) {
			criteria.add(Restrictions.eq("module", logParam.getModule()));
		}
		if (StringUtils.isNotBlank(logParam.getOperation())) {
			criteria.add(Restrictions.eq("operation", logParam.getOperation()));
		}

		if (logParam.getStartDate() != null) {
			criteria.add(Restrictions.ge("createdOn", logParam.getStartDate()));
		}
		if (logParam.getEndDate() != null) {
			criteria.add(Restrictions.le("createdOn", logParam.getEndDate()));
		}
		return this.countByCriteria(criteria);
	}

	@Override
	public List<Log> list(ListLogRequest logParam, int pageIndex, int pageSize) {

		DetachedCriteria criteria = DetachedCriteria.forClass(Log.class);

		//TODO 添加大小不敏感
		if (StringUtils.isNotBlank(logParam.getLoginName())) {
			criteria.add(Restrictions.like("loginName", "%"+logParam.getLoginName()+"%"));
		}
		
		if(logParam.getOperationUsers()!=null && logParam.getOperationUsers().length>0){
				criteria.add(Restrictions.in("loginName", logParam.getOperationUsers()));
		}
		if (StringUtils.isNotBlank(logParam.getModule())) {
			criteria.add(Restrictions.eq("module", logParam.getModule()));
		}
		if (StringUtils.isNotBlank(logParam.getOperation())) {
			criteria.add(Restrictions.eq("operation", logParam.getOperation()));
		}

		if (logParam.getStartDate() != null) {
			criteria.add(Restrictions.ge("createdOn", logParam.getStartDate()));
		}
		if (logParam.getEndDate() != null) {
			criteria.add(Restrictions.le("createdOn", logParam.getEndDate()));
		}
		criteria.addOrder(Order.desc("createdOn"));

		List result = (List<Log>) findByCriteria(criteria, pageIndex, pageSize);

		return result;
	}

	@Override
	public void cleanLog() {
		SQLQuery query = getSession().createSQLQuery("DELETE FROM T_LOG WHERE TRUNC(t_log.CREATED_ON, 'yyyy') < TRUNC(SYSDATE, 'yyyy') ");
		query.executeUpdate();
	}

}
