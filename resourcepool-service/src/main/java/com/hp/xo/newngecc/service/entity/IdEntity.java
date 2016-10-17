/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.hp.xo.newngecc.service.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.hp.xo.utils.common.IdGenerator;

/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 *  引用ngecc-utils包
 * 
 * @author qiang.li7@hp.com
 */
// JPA 基类的标识
@MappedSuperclass
public abstract class IdEntity {

	protected Long id;

	@Id
	public Long getId() {
		return IdGenerator.createGenerator().generate();
	}

	public void setId(Long id) {
		this.id = id;
	}
}
