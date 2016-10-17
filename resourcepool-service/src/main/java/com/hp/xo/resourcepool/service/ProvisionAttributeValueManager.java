package com.hp.xo.resourcepool.service;

import java.util.Map;


public interface ProvisionAttributeValueManager  {
	Map<String,Object> getExtval(String attributeName, String sessionkey, Map<String, Object[]> valueParam);
}
