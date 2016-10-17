package com.hp.xo.resourcepool.dao.hibernate;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.ApproveRuleDao;
import com.hp.xo.resourcepool.model.ApproveRule;

/*import com.cmsz.cloudplatform.dao.ApproveRuleDao;
import com.cmsz.cloudplatform.model.ApproveRule;
import com.hp.core.dao.hibernate.GenericDaoImpl;*/
@Repository("approveRuleDao")
public class ApproveRuleDaoImpl extends GenericDaoImpl<ApproveRule, Long>  implements ApproveRuleDao{

	
	public ApproveRuleDaoImpl(Class<ApproveRule> clazz) {
		super(clazz);
	}

	public ApproveRuleDaoImpl(){
		super(ApproveRule.class);
	}

	private void addLikeCriteria(DetachedCriteria detachedCriteria, String fieldName, String criteria){
		if (StringUtils.isNotBlank(criteria) && StringUtils.isNotBlank(criteria)) {				
			detachedCriteria.add(Restrictions.like(fieldName , "%" + criteria+ "%"));
		}
	}
	
	private void addEqualCriteria(DetachedCriteria detachedCriteria, String fieldName, Integer criteria){
		if (criteria!=null) {				
			detachedCriteria.add(Restrictions.like(fieldName ,  criteria));
		}
	}
	
	@Override
	public List<ApproveRule> listApproveRule(ApproveRule approveRule) {
		
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(ApproveRule.class);
		
		addEqualCriteria(detachedCriteria, "workOrderType",  approveRule.getWorkOrderType());
		addLikeCriteria(detachedCriteria, "poolId", approveRule.getPoolId());
		return (List<ApproveRule>) this.findByCriteria(detachedCriteria);
	}

}
