package com.hp.xo.resourcepool.dao.hibernate;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hp.xo.resourcepool.dao.AttributeDao;
import com.hp.xo.resourcepool.model.ProvisionAttribute;

/*import com.cmsz.cloudplatform.dao.AttributeDao;
import com.cmsz.cloudplatform.model.ProvisionAttribute;
import com.hp.core.dao.hibernate.GenericDaoImpl;*/

@Repository("attributeDao")
public class AttributeDaoImpl extends GenericDaoImpl<ProvisionAttribute, Long>  implements AttributeDao{

	public AttributeDaoImpl(Class<ProvisionAttribute> clazz) {
		super(clazz);
	}

	public AttributeDaoImpl(){
		super(ProvisionAttribute.class);
	}

	@Override
	public List<ProvisionAttribute> findWorkOrderListByExample(
			ProvisionAttribute example) {
		
		return (List<ProvisionAttribute>) this.findByExample(example);
	}
	
}
