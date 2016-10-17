package com.hp.xo.resourcepool.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.dao.WorkOrderDao;
import com.hp.xo.resourcepool.model.WorkOrder;
import com.hp.xo.resourcepool.vo.WorkOrderReportVO;
import com.hp.xo.resourcepool.request.ListWorkOrderReportRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.WorkOrderReportManager;


@Service(value = "workOrderReportManager")
public class WorkOrderReportManagerImpl extends GenericManagerImpl<WorkOrder, Long> implements WorkOrderReportManager {
	private WorkOrderDao orderDao;

	public WorkOrderReportManagerImpl() {
		super();
	}

	public WorkOrderReportManagerImpl(WorkOrderDao orderDao) {
		super(orderDao);
		this.orderDao = orderDao;
	}

	@Autowired
	public void setOrderDao(WorkOrderDao orderDao) {
		this.dao = orderDao;
		this.orderDao = orderDao;
	}

	public ListResponse<WorkOrderReportVO> getWorkOrderReportData(ListWorkOrderReportRequest req) {
		ListResponse<WorkOrderReportVO> listResponse = new ListResponse<WorkOrderReportVO>();

		List<Object> list = orderDao.findWorkOrderReport(req);
		if (list == null) {
			return listResponse;
		}

		List<WorkOrderReportVO> worList = new ArrayList<WorkOrderReportVO>();
		for (Object obj : list) {
			WorkOrderReportVO wor = new WorkOrderReportVO();
			wor.setObj((Object[]) obj);
			worList.add(wor);
		}
		listResponse.setResponses(worList);
		
//		Map<String, Map<String, Object>> woMap = new HashMap<String, Map<String, Object>>();
//		for (WorkOrder wo : list) {
//			String key = DateUtil.convertDateToString(wo.getCreatedOn(), Constants.DATE_PATTERN_YYYY_MM);
//			if (!woMap.containsKey(key)) {
//				woMap.put(key, new HashMap<String, Object>());
//				woMap.get(key).put("ym", key);
//			}
//			if (wo.getStatus() != null) {
//				if (woMap.get(key).get(wo.getStatus()) == null) {
//					woMap.get(key).put(wo.getStatus().toString(), new Integer(0));
//				}
//				woMap.get(key).put(wo.getStatus().toString(), new Integer((Integer) woMap.get(key).get(wo.getStatus().toString()) + 1));
//			}
//		}
//
//		for (Map<String, Object> val : woMap.values()) {
//			try {
//				Integer total = 0;
//				for (Object obj : val.values()) {
//					total += Integer.parseInt((String) obj);
//				}
//				val.put("total", total);
//			} catch (NumberFormatException e) {
//			}
//		}
//
//		List<Map<String, Object>> responses = new ArrayList<Map<String, Object>>();
//		responses.addAll(woMap.values());
//		listResponse.setResponses(responses);

		return listResponse;
	}
}
