package com.hp.xo.resourcepool.service;
import com.hp.xo.resourcepool.model.Property;
import com.hp.xo.resourcepool.request.ListPropertyRequest;
import com.hp.xo.resourcepool.request.SavePropertyRequest;
import com.hp.xo.resourcepool.response.EntityResponse;
import com.hp.xo.resourcepool.response.ListResponse;

public interface PropertyManager extends GenericManager<Property, Long> {

	ListResponse<Property> listProperty(ListPropertyRequest request);
	EntityResponse<Property> saveProperty(SavePropertyRequest request);

	int deleteProperty(String serialnumber);
}
