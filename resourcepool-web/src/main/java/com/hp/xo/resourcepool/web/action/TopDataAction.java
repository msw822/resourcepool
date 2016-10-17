package com.hp.xo.resourcepool.web.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.hp.xo.resourcepool.request.ListTopDataRequest;
import com.hp.xo.resourcepool.response.ListResponse;
import com.hp.xo.resourcepool.service.TopDataManager;
import com.hp.xo.resourcepool.utils.StringUtil;
import com.hp.xo.resourcepool.vo.TopDataVO;
import com.hp.xo.resourcepool.web.action.core.BaseAction;



public class TopDataAction extends BaseAction {

	private static final long serialVersionUID = -5338585912156067440L;

	@Autowired
	private TopDataManager topDataManager;

	public String listTopData() {
		ListTopDataRequest request = (ListTopDataRequest) this.wrapRequest(new ListTopDataRequest());
		//request.setResoucePools(resoucePools);
		request.setZones(zones);
		request.setPods(pods);
		request.setClusters(clusters);
		request.setHosts(hosts);

		Map<String, Object[]> requestParamsNew = new HashMap<String, Object[]>();
		requestParamsNew.put("command", new Object[] { "listVirtualMachines" });
		requestParamsNew.put("listAll", new Object[] { "true" });
		requestParamsNew.put("response", requestParams.get("response"));
		requestParamsNew.put("sessionkey", requestParams.get("sessionkey"));
		requestParamsNew.put("apikey", requestParams.get("apikey"));
		requestParamsNew.put("secretkey", requestParams.get("secretkey"));
		if (requestParams.get("_") != null) {
			requestParamsNew.put("_", requestParams.get("_"));
		}
		ListResponse<TopDataVO> response = topDataManager.getTopData(request, requestParamsNew);

		if (requestParams.get("export") != null && requestParams.get("export")[0].equals("true") && requestParams.get("timeAndTarget") != null
				&& requestParams.get("timeAndTarget").length > 0) {
			exportTopData(response, requestParams.get("timeAndTarget")[0].toString());

		} else {
			writeResponse(response);
		}
		return NONE;
	}

	private void exportTopData(ListResponse<TopDataVO> response, String timeAndTarget) {
		String fileName = "TOPN统计";

		HttpServletResponse resp = ServletActionContext.getResponse();
		resp.reset();
		resp.addHeader("Content-Disposition", "attachment; filename=" + StringUtil.urlEncode(fileName) + ".xls");
		resp.setContentType("application/vnd.ms-excel; charset=UTF-8");
		OutputStream os = null;
		WritableWorkbook wwb = null;
		try {
			os = resp.getOutputStream();

			//String[] title = { "虚机名称", "IP 地址", "一级池", "二级池", "主机名", "值" };
			String[] title = { "虚机名称", "IP 地址", "区域", "主机名", "值" };
			// 创建Excel工作薄
			int row = 0;
			wwb = Workbook.createWorkbook(os);
			WritableSheet sheet = wwb.createSheet(fileName, row);
			Label label;
			sheet.setRowView(row, 20 * 20);
			label = new Label(0, row++, timeAndTarget);
			sheet.addCell(label);

			for (int i = 0; i < title.length; i++) {
				sheet.setColumnView(i, i == 0 ? 38 : 15);
				// Label(x,y,z) 代表单元格的第x+1列，第y+1行, 内容z
				// 在Label对象的子对象中指明单元格的位置和内容
				label = new Label(i, row, title[i]);
				// 将定义好的单元格添加到工作表中
				sheet.addCell(label);
			}

			for (TopDataVO vo : response.getResponses()) {
				row++;
				for (int i = 0; i < title.length; i++) {
					String val = "";
					switch (i) {
					case 0:
						val = vo.getName();
						break;
					case 1:
						val = vo.getIpAddress();
						break;
//					case 2:
//						val = vo.getResoucePool();
//						break;
					case 2:
						val = vo.getZone();
						break;
					case 3:
						val = vo.getHost();
						break;
					case 4:
						if ("cpunumber".equals(vo.getTarget())) {
							val = StringUtil.convertHz(String.valueOf(Float.valueOf(vo.getValue()) * Float.valueOf(vo.getCpuSpeed())));
						} else if ("cpuused".equals(vo.getTarget())) {
							val = vo.getValue();
						} else if ("networkkbsread".equals(vo.getTarget()) || "networkkbswrite".equals(vo.getTarget())) {
							val = vo.getValue() == null ? "N/A" : StringUtil.convertBytes(vo.getValue());
						} else if ("diskkbsread".equals(vo.getTarget()) || "diskkbswrite".equals(vo.getTarget())) {
							val = vo.getValue() == null ? "N/A" : ("KVM".equals(vo.getHypervisor()) ? StringUtil.convertBytes(vo.getValue())
									: ("XenServer".equals(vo.getHypervisor()) ? StringUtil.convertBytes(vo.getValue()) + "/s" : "N/A"));
						} else if ("diskioread".equals(vo.getTarget()) || "diskiowrite".equals(vo.getTarget())) {
							val = vo.getValue() == null ? "N/A" : ("KVM".equals(vo.getHypervisor()) ? vo.getValue() : "N/A");
						}
						break;
					default:
						break;
					}

					label = new Label(i, row, val);
					sheet.addCell(label);
				}
			}

			// 写入数据
			wwb.write();

			os.flush();
		} catch (IOException e) {
			if (log.isTraceEnabled()) {
				log.trace("exception writing response: " + e);
			}
		} catch (Exception ex) {
			if (!(ex instanceof IllegalStateException)) {
				log.error("unknown exception writing api response", ex);
			} else {
				log.error("exception writing api response", ex);
			}
		}

		if (wwb != null) {
			try {
				// 关闭文件
				wwb.close();
			} catch (WriteException e) {
				if (log.isTraceEnabled()) {
					log.trace("exception close writableWorkbook outputStream: " + e);
				}
			} catch (IOException e) {
				if (log.isTraceEnabled()) {
					log.trace("exception close writableWorkbook outputStream: " + e);
				}
			}
		}

		if (os != null) {
			try {
				os.close();
				os = null;
			} catch (IOException e) {
				if (log.isTraceEnabled()) {
					log.trace("exception close response outputStream: " + e);
				}
			}
		}
	}

	//private List<String> resoucePools;// 一级池
	private List<String> zones;// 二级池
	private List<String> pods;// pod
	private List<String> clusters;// cluster
	private List<String> hosts;// 主机

//	public List<String> getResoucePools() {
//		return resoucePools;
//	}
//
//	public void setResoucePools(List<String> resoucePools) {
//		this.resoucePools = resoucePools;
//	}

	public List<String> getZones() {
		return zones;
	}

	public void setZones(List<String> zones) {
		this.zones = zones;
	}

	public List<String> getPods() {
		return pods;
	}

	public void setPods(List<String> pods) {
		this.pods = pods;
	}

	public List<String> getClusters() {
		return clusters;
	}

	public void setClusters(List<String> clusters) {
		this.clusters = clusters;
	}

	public List<String> getHosts() {
		return hosts;
	}

	public void setHosts(List<String> hosts) {
		this.hosts = hosts;
	}

}

