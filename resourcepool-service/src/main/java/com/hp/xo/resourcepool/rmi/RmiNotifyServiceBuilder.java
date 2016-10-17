package com.hp.xo.resourcepool.rmi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.hp.avmon.notify.AddNotifyService;
import com.hp.xo.resourcepool.dto.PhysicsHost;
import com.hp.xo.resourcepool.exception.BuessionException;
import com.hp.xo.resourcepool.utils.ClassLoaderUtil;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;
import com.hp.xo.uip.cmdb.domain.CiAttribute;
import com.hp.xo.uip.cmdb.domain.Node;
import com.hp.xo.uip.cmdb.domain.RelationNode;
import com.hp.xo.uip.cmdb.exception.CmdbException;
import com.hp.xo.uip.cmdb.service.CmdbService;
import com.hp.xo.utils.common.XmlHandler;

public class RmiNotifyServiceBuilder {
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	private AddNotifyService getNotifyService() throws BuessionException{
		Object service=RmiServiceClientFactory.getInstance().getServiceClientByName("addNotifyService");
		if(service!=null && service instanceof AddNotifyService){
			AddNotifyService addNotifyService=(AddNotifyService)service;
			if(addNotifyService==null){
				log.error("找不到notify远程服务的本地配置信息");				
			}
			return addNotifyService;
		}else{
			log.error("找不到notify远程服务的本地配置信息");
			return null;
		}
	}
	public void addNotifyToDB(Integer message_type, String phone_no, String title, String content, Date plan_send_time, String alarm_id, Integer seq){
		//通知的类型-0:sms 1:email
		try {
			//不能为空	message_type, phone_no, title, content
			this.getNotifyService().addNotifyToDB( message_type,  phone_no,  title,  content,plan_send_time,  alarm_id, seq);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BuessionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
