package com.hp.xo.resourcepool.dao;

import java.util.List;

import com.hp.xo.resourcepool.model.ApproveRule;

/*import com.cmsz.cloudplatform.model.ApproveRule;
import com.hp.core.dao.GenericDao;*/

public interface ApproveRuleDao extends GenericDao<ApproveRule,Long>{
	List<ApproveRule> listApproveRule(ApproveRule approveRule);
}
