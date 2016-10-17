package com.hp.xo.resourcepool.response;

import com.hp.xo.resourcepool.model.BaseEntity;


public class EntityResponse<T extends BaseEntity> extends BaseResponse {
    private transient T entity;

    public EntityResponse() {
    	super();
    }
    
	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}
   
}
