package com.hp.xo.resourcepool.schedule.service;

import java.util.List;

import com.hp.xo.resourcepool.model.TEquipmentenroll;


/**
 * 
 * Project Name: cloudserver <br/>
 * Package Name: com.cmsz.cloudplatform.service <br/>
 * ClassName: PhysicalResourceSynManager <br/>
 * Function: X86和HP小型机物理资源同步相关服务. <br/>
 * Date: 2014-5-12 上午10:34:58 <br/>
 * 
 * @version
 * @author 苏祖伟(Wesley.Su) mailto: zuwei.su@qq.com
 * @date 2014-5-12 上午10:34:58
 */
public interface PhysicalResourceSynManager {

	// 同步物理资源
	// public void synResource();

	// 从数据库表t_equipmentEnroll获取所有available=1的数据，按target_type排序
	public List<TEquipmentenroll> findEquipmentEnroll();

	void synX86Enclosure(String ip);

	void synHpHost(String hostName);

	void synX86Rack(String ip);

	void synX86Host(String ip);

}
