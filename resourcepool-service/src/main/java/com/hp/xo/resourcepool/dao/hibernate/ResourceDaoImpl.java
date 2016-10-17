package com.hp.xo.resourcepool.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.ResourceDao;
import com.hp.xo.resourcepool.model.Resource;

//import com.cmsz.cloudplatform.dao.ResourceDao;
//import com.cmsz.cloudplatform.model.Resource;
//import com.cmsz.cloudplatform.model.THost;
//import com.hp.core.dao.hibernate.GenericDaoImpl;

@Repository("resourceDao")
public class ResourceDaoImpl extends GenericDaoImpl<Resource, Long> implements ResourceDao {

	public ResourceDaoImpl(Class<Resource> persistentClass) {
		super(persistentClass);
	}

	public ResourceDaoImpl() {
		super(Resource.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> applicationTotalResource(Integer type, String resourcePoolId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("select decode(sum(t.amount), null, 0, sum(t.amount)) unit,");
		sbf.append("       decode(sum(t.amount * to_number(t.cpu)), null, 0, sum(t.amount * to_number(t.cpu))) cpu,");
		sbf.append("       decode(sum(t.amount * to_number(t.memory)), null, 0, sum(t.amount * to_number(t.memory))) memory");
		sbf.append("  from T_RESOURCE t");
		sbf.append(" where t.resourcepoolid = '").append(resourcePoolId).append("'");
		sbf.append("   and t.type = ").append(type);

		return this.getSession().createSQLQuery(sbf.toString()).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> hostTotalResource(Integer type, String resourcePoolId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("select count(1) unit,");
		sbf.append("       decode(sum(t.cpucores), null, 0, sum(t.cpucores)) cpu,");
		sbf.append("       decode(sum(t.memory), null, 0, sum(t.memory)) memory");
		sbf.append("  from t_host t");
		sbf.append(" where t.resourcepoolid = '").append(resourcePoolId).append("'");
//		if(THost.TYPE_HP.equals(type)) {
//			sbf.append("   and t.type = ").append(type);
//		} else if(THost.TYPE_X86_RACK.equals(type)) {
//			sbf.append("   and (t.type = ").append(THost.TYPE_X86_RACK);
//			sbf.append("   or t.type = ").append(THost.TYPE_X86_ENCLOSURE).append(")");
//		}

		return this.getSession().createSQLQuery(sbf.toString()).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object> hpvmTotalResource() {
		StringBuffer sbf = new StringBuffer();
		sbf.append("select count(1) unit,");
		sbf.append("       decode(sum(to_number(t.cpucount)), null, 0, sum(to_number(t.cpucount))) cpu,");
		sbf.append("       decode(sum(to_number(t.memory)), null, 0, sum(to_number(t.memory))) memory");
		sbf.append("  from t_hpvm t");

		return this.getSession().createSQLQuery(sbf.toString()).list();
	}
}
