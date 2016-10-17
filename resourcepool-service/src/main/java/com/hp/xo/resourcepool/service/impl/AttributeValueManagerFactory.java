package com.hp.xo.resourcepool.service.impl;

import org.springframework.context.ApplicationContext;
import com.hp.xo.resourcepool.service.ProvisionAttributeValueManager;
import com.hp.xo.resourcepool.utils.SpringUtil;

/*import com.cmsz.cloudplatform.service.ProvisionAttributeValueManager;
import com.hp.util.SpringUtil;
*/
public class AttributeValueManagerFactory {

	// private ProvisionAttributeValueManager provisionAttributeValueManager;

	/**1	项目申请*/
	public static Integer NEW_PROJECT = 1;
	
	/**2	项目扩容申请*/
	public static Integer PROJECT_LIM = 2;
	
	/**3	域扩容申请*/
	public static Integer DOMAIN_LIM = 3;
	
	/**4	账户扩容申请*/
	public static Integer ACCOUNT_LIM = 4;
	
	/**5	添加实例*/
	public static Integer DEPLOY_VM = 5;
	
	/**6	更改服务方案*/
	public static Integer SCALE_VM = 6;
	
	/**7	新建卷*/
	public static Integer NEW_VOLUME = 7;

	public static ProvisionAttributeValueManager createManager(Integer type) {
		//ApplicationContext context = new ClassPathXmlApplicationContext();
		ApplicationContext context = SpringUtil.getApplicationContext();
		ProvisionAttributeValueManager provisionAttributeValueManager = null;
		switch (type) {
		case 1:
			provisionAttributeValueManager = (ProvisionAttributeValueManager)context.getBean("newProjectAttributeValueManager");
			break;
		case 2:
			provisionAttributeValueManager = (ProvisionAttributeValueManager)context.getBean("projectLimitAttributeValueManager");
			break;
		case 3:
			provisionAttributeValueManager =(ProvisionAttributeValueManager)context.getBean("domainLimitAttributeValueManager"); 
			break;
		case 4:
			provisionAttributeValueManager = (ProvisionAttributeValueManager)context.getBean("accountLimitAttributeValueManager"); 
			break;
		case 5:
			provisionAttributeValueManager =(ProvisionAttributeValueManager)context.getBean("deployVMAttributeValueManager");
			break;
		case 6:
			provisionAttributeValueManager =(ProvisionAttributeValueManager)context.getBean("scaleVMAttributeValueManager");
			break;
		case 7:
			provisionAttributeValueManager =(ProvisionAttributeValueManager)context.getBean("newVolumeAttributeValueManager");
			break;
		case 9:
			provisionAttributeValueManager =(ProvisionAttributeValueManager)context.getBean("resizeVolumeAttributeValueManager");
			break;
		}
		return provisionAttributeValueManager;
	}

}
