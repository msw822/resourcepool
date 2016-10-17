package com.hp.xo.resourcepool.schedule.service;

import java.util.Map;

import net.sf.json.JSONObject;

import com.hp.xo.resourcepool.model.WorkOrder;

public interface SAManager {
	public Map<String,String> getOrderTypeSpecificParams();
    public Map<String,Object[]> getProvisionAttributes(Map<String,Object[]> cloudStackParams, WorkOrder workOrder);
    public void jobSucceedDeal(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject resultJSONObj,Map<String,String> woSpecificParams);
    public  void putUserCredentialParams(Map<String,Object[]> cloudStackParams,WorkOrder workOrder);
    /**
     * 订单后处理
     * @param cloudStackParams
     * @param workOrder
     * @param responseObj
     * @return
     */
    public boolean jobSucceedPost(Map<String,Object[]> cloudStackParams, WorkOrder workOrder,JSONObject responseObj,Map<String, String> woSpecificParams);
}
