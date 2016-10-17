package com.hp.xo.resourcepool.model;

import java.util.Date;

/**
 * 
 * @author Zhefang Chen
 *
 */
public interface Entity {
	void setCreatedBy(String createdBy);
	String getCreatedBy();
	
	void setCreatedOn(Date createdOn);
	Date getCreatedOn();
	
	void setModifiedBy(String modifiedBy);
	String getModifiedBy();
	
	void setModifiedOn(Date modifiedOn);
	Date getModifiedOn();
	
//	void setStatus(String status);
//	String getStatus();
//	
//	void setTxnKeyRef(String txyKeyRef);
//	String getTxnKeyRef();
	
}
