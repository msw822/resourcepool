package com.hp.xo.resourcepool.rmi;

import java.io.InputStream;
import java.util.ArrayList;
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
import org.dom4j.Document;
import org.dom4j.Element;

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

public class RmiPhysicsHostBuilder extends AbstractPhysicsHostBuilder{
	private static Map<String,String> hostFieldMaping =new HashMap<String,String>();
	private static Map<String,String> virtualHostFieldMaping =new HashMap<String,String>();
	private static Map<String,String> projectFieldMaping =new HashMap<String,String>();
	private static Map<String,String> resourcepoolMaping =new HashMap<String,String>();
	   
	private CmdbService getCmdbService() throws BuessionException{
		Object service=RmiServiceClientFactory.getInstance().getServiceClientByName("cmdbServiceProxy");
		if(service!=null && service instanceof CmdbService){
			CmdbService cmdbServiceProxy=(CmdbService)service;
			if(cmdbServiceProxy==null){
				log.error("找不到Cmdb远程服务的本地配置信息");			
			}
			return cmdbServiceProxy;
			
		}else{
			log.error("找不到Cmdb远程服务的本地配置信息");
			return null;
		}
		
	}
	private synchronized  static Map getHostMaping(){
			
		synchronized (hostFieldMaping) {  
            if (hostFieldMaping == null) {  
            	hostFieldMaping=new HashMap<String,String>();
            } 
            if(hostFieldMaping.isEmpty()){
            	InputStream stream=RmiPhysicsHostBuilder.class.getResourceAsStream("/datasource-refrence.xml");
            	try{
	            	Document document=XmlHandler.getDocument(stream);
	   	
	            	Element element= XmlHandler.getSingleNode(document, "//rmi/host/field-maping");
	        
	            	if(element!=null){
	            		 List<Element> nodes = element.elements("field");
	            		 for (Element node : nodes) {
	            			 hostFieldMaping.put(node.attributeValue("name"), node.getTextTrim());
	            		 }          		
	            	}
            	}catch(Throwable ex){
            		ex.printStackTrace();
        			
        		}
	            	
            }
        }  
		return hostFieldMaping;
			
		
	}
	public synchronized  static Map getVirtualHostMaping(){
		
		synchronized (virtualHostFieldMaping) {  
            if (virtualHostFieldMaping == null) {  
            	virtualHostFieldMaping=new HashMap<String,String>();
            } 
            if(virtualHostFieldMaping.isEmpty()){
            	InputStream stream=RmiPhysicsHostBuilder.class.getResourceAsStream("/datasource-refrence.xml");
            	try{
	            	Document document=XmlHandler.getDocument(stream);
	   	
	            	Element element= XmlHandler.getSingleNode(document, "//rmi/virtual-host/field-maping");
	        
	            	if(element!=null){
	            		 List<Element> nodes = element.elements("field");
	            		 for (Element node : nodes) {
	            			 virtualHostFieldMaping.put(node.attributeValue("name"), node.getTextTrim());
	            		 }          		
	            	}
            	}catch(Throwable ex){
            		ex.printStackTrace();
        			
        		}
	            	
            }
        }  
		return virtualHostFieldMaping;
			
		
	}
public synchronized  static Map getProjectMaping(){		
		synchronized (projectFieldMaping) {  
            if (projectFieldMaping == null) {  
            	projectFieldMaping=new HashMap<String,String>();
            } 
            if(projectFieldMaping.isEmpty()){
            	InputStream stream=RmiPhysicsHostBuilder.class.getResourceAsStream("/datasource-refrence.xml");
            	try{
	            	Document document=XmlHandler.getDocument(stream);
	   	
	            	Element element= XmlHandler.getSingleNode(document, "//rmi/project/field-maping");
	        
	            	if(element!=null){
	            		 List<Element> nodes = element.elements("field");
	            		 for (Element node : nodes) {
	            			 projectFieldMaping.put(node.attributeValue("name"), node.getTextTrim());
	            		 }          		
	            	}
            	}catch(Throwable ex){
            		ex.printStackTrace();
        			
        		}	            	
            }
        }  
		return projectFieldMaping;		
	}
	public JSONArray getCmdbHost()throws BuessionException{
		List<Node> nodes=null;
		try{
			CmdbService cmdbServiceProxy=this.getCmdbService();	
			if(cmdbServiceProxy!=null){
				String typeName=ServiceOptionUtil.getValue("cmdb.host", "Host");
				nodes=cmdbServiceProxy.getCiByTypeName(typeName);					
			}
			JSONArray arrNodes = new JSONArray();
			if(nodes==null || nodes.isEmpty()){
				return arrNodes;
			}
			Map<String,String> mapMaping=getHostMaping();
			for(int index=0;index<nodes.size();index++){
				Node node=nodes.get(index);
				JSONObject json = new JSONObject();  
				json.put("id", String.valueOf(node.getId()));
				json.put("derivedFrom", node.getDerivedFrom());
				//Map<String,Object> map=new HashMap<String,Object>();
				for(Iterator itor= mapMaping.keySet().iterator();itor.hasNext();){	
					String key=itor.next().toString();
					if(log.isDebugEnabled()){
						log.info(key+"="+mapMaping.get(key));
					}
					if(mapMaping.get(key).equals("derivedFrom")){
						continue;
					}
					//map.put(mapMaping.get(key).toLowerCase(), node.getAttributes().get(key));	
					json.put(mapMaping.get(key), node.getAttributes().get(key)==null?node.getAttributes().get(key):node.getAttributes().get(key).getValue());	
				}
				//JSONObject json=JSONObject.fromObject(map);
				if(log.isDebugEnabled()){
					log.info(json);
				}		
				arrNodes.add(json);
			}
			return arrNodes;
			
		}catch(CmdbException ex){
			log.error(ex);
			throw new BuessionException(ex);
		}catch(Throwable ex){
			log.error(ex);
			throw new BuessionException(ex);			
		}
		
	}
	public JSONArray saveCmdbVirtualHost(JSONArray arrVirtualHosts,Map<String,PhysicsHost> physicsHost)throws BuessionException{	
		CmdbService cmdbServiceProxy=null;
		Node vmNode=null;
		Node projectNode=null;
		String vmtypeName=ServiceOptionUtil.getValue("cmdb.virtual.host", "vm");
		String projectypeName=ServiceOptionUtil.getValue("cmdb.project", "project");
		try{
			cmdbServiceProxy=this.getCmdbService();
			if(cmdbServiceProxy!=null){
				vmNode=cmdbServiceProxy.getCiTypeByName(vmtypeName);
				projectNode=cmdbServiceProxy.getCiTypeByName(projectypeName);
			}
		}catch(CmdbException ex){
			log.error("获得Cmdb远程服务失败！错误信息：",ex);
			throw new BuessionException(ex);
		}catch(Throwable ex){
			log.error("获得Cmdb远程服务失败！错误信息：",ex);
			throw new BuessionException(ex);			
		}
		if(vmNode==null || projectNode==null){
			log.error("获得Cmdb远程虚拟机服务失败！");
			throw new BuessionException("获得Cmdb远程虚拟机服务失败！");
		}
		
	
		JSONArray result= new JSONArray();
		List<Node> nodes=new ArrayList<Node>();
	
		final Map<String,PhysicsHost> mapVirtualMachine=new ConcurrentHashMap<String,PhysicsHost>();
		
		
		try{
//			InputStream inStream = ClassLoaderUtil.getResourceAsStream("config.properties",this.getClass());
//			if (null == inStream) {
//				throw new BuessionException("Not found configurtion file. file name is config.properties");
//			}
//			Properties props = new Properties();
//			props.load(inStream);
//			String hostType=props.getProperty("host_type", "HP");	
//			String hostType=ServiceOptionUtil.getValue("cmdb.default.host.type", "HP");
	
			if(arrVirtualHosts!=null && !arrVirtualHosts.isEmpty()){		
				String hostPrimaryKey=null;
				for(int index=0;index<arrVirtualHosts.size();index++ ){
					JSONObject json  =(JSONObject) arrVirtualHosts.get(index);
					if(log.isInfoEnabled()){
						 log.info("传递CMDB server的虚拟主机信息："+json.toString());
					}
					hostPrimaryKey=json.getString("hostname");
					mapVirtualMachine.put(json.getString("instancename"),physicsHost.get(hostPrimaryKey));
					Node cmdbNode=new Node();				
					cmdbNode.setName(json.getString("instancename"));
					String displayname=StringUtils.isBlank(json.getString("displayname"))?json.getString("name"):json.getString("displayname");
					 //cmdbNode.setDomain(json.getString("domain"));
//					cmdbNode.setPath(typeNode.getPath()+"\"+displayname);
					cmdbNode.setLabel(displayname);
//					Map<String, CiAttribute> noteAttributes=new LinkedHashMap<String, CiAttribute>();
//					CiAttribute attribute=new CiAttribute();
//					attribute.setValue(json.getString("instancename"));
//					noteAttributes.put("instance_name",attribute);
//					cmdbNode.setAttributes(noteAttributes);
					cmdbNode.setIsType(false);
					cmdbNode.setParentId(vmNode.getId());
					cmdbNode.setDerivedFrom(vmNode.getName());
//					cmdbNode.setIcon(typeNode.getIcon());
					
							
				 
					Map<String,String> mapMaping=getVirtualHostMaping();
					Map<String, CiAttribute> attributes=cmdbNode.getAttributes();	//new HashMap<String, CiAttribute>();
					CiAttribute catt =null;
					for(Iterator<String> itor= mapMaping.keySet().iterator();itor.hasNext();){	
					    catt = new CiAttribute();
						String key=itor.next().toString();					
						if(log.isDebugEnabled()){
							log.debug(key+"="+mapMaping.get(key));
						}
						if(key.equals("name") || key.equals("displayname")){
							continue;
						}
						catt.setName(key);
						catt.setValue(json.getString(mapMaping.get(key)));
						catt.setViewMode(0);
						attributes.put(key,catt);				
					}			
					cmdbNode.setAttributes(attributes);
					nodes.add(cmdbNode);
				}
			}
			List<Node> newNodes=cmdbServiceProxy.insertNodeCis(nodes);			
			String nodePrimaryKey=null;
			String relationType=ServiceOptionUtil.getValue("cmdb.relation", "linked");
			List<RelationNode> relations=new ArrayList<RelationNode>(); 
			for(Node node:newNodes){
				nodePrimaryKey=node.getAttributes().get("instancename").getValue();
				PhysicsHost host=mapVirtualMachine.get(nodePrimaryKey);
				RelationNode rnT = cmdbServiceProxy.getRelationTypeByName(relationType);
				RelationNode vmHostRelation=rnT.clone();
				vmHostRelation.setName(nodePrimaryKey);
				vmHostRelation.setIsType(false);
				vmHostRelation.setId(null);
				vmHostRelation.setDerivedFrom(rnT.getName());
				//relation.setSyncStatus(1);
//				relation.setSourceId(node.getId());
//				relation.setSourceLable(node.getLabel());
//				relation.setSourceName(node.getName());
//				relation.setTargetId(new Long(host.getId()).longValue());
//				relation.setTargetLable(host.getHostName());
//				relation.setTargetName(host.getHostName());
//				relation.setSourceType(node.getDerivedFrom());
//				relation.setTargetType(host.getDerivedFrom());		
				vmHostRelation.setSourceId(new Long(host.getId()).longValue());
				vmHostRelation.setSourceLable(host.getHostName());
				vmHostRelation.setSourceName(host.getHostName());
				vmHostRelation.setSourceType(host.getDerivedFrom());
				
				vmHostRelation.setTargetId(node.getId());
				vmHostRelation.setTargetName(node.getName());				
				vmHostRelation.setTargetLable(node.getLabel());			
				vmHostRelation.setTargetType(node.getDerivedFrom());	
				relations.add(vmHostRelation);	
				
				//业务系统与虚机关系
				RelationNode vmProjectRelation=rnT.clone();
				String nodeProjectName = node.getAttributes().get("project").getValue();
				Node proNode = cmdbServiceProxy.getCiByName(projectypeName, nodeProjectName);
				vmProjectRelation.setName(nodePrimaryKey+"_"+nodeProjectName);
				vmProjectRelation.setIsType(false);
				vmProjectRelation.setId(null);				
				vmProjectRelation.setDerivedFrom(rnT.getName());
				
				vmProjectRelation.setSourceId(node.getId());
				vmProjectRelation.setSourceName(node.getName());				
				vmProjectRelation.setSourceLable(node.getLabel());			
				vmProjectRelation.setSourceType(node.getDerivedFrom());	
				
				vmProjectRelation.setTargetId(new Long(proNode.getId()).longValue());
				vmProjectRelation.setTargetLable(proNode.getLabel());
				vmProjectRelation.setTargetName(proNode.getName());
				vmProjectRelation.setTargetType(proNode.getDerivedFrom());

				relations.add(vmProjectRelation);	
				
			}			
			List<RelationNode> newRelations=cmdbServiceProxy.insertRelationCis(relations);
			for(RelationNode relation :newRelations){
				JSONObject jsonObject=new JSONObject();
				if(StringUtils.isNoneBlank(String.valueOf(relation.getId()))){
//					JSONObject jsonObject = JSONObject.fromObject(relation);
					jsonObject.put("status",HttpStatus.SC_OK);					
				}else{
					jsonObject.put("status",HttpStatus.SC_FORBIDDEN);	
				}
				result.add(jsonObject);
			}
			
			
		}catch(CmdbException ex){
			log.error("发送CMDB server 发生错误，错误信息：",ex);
			throw new BuessionException(ex);
		}catch(Throwable ex){
			log.error("发送CMDB server 发生错误，错误信息：",ex);
			throw new BuessionException(ex);			
		}
		return result;
	}
	/**
	 * 
	 */
	public JSONArray deleteCmdbVirtualMachines(JSONArray virtualMachines,Map<String,PhysicsHost> physicsHost)throws BuessionException{	
		CmdbService cmdbServiceProxy=null;
		Node typeNode=null;
		try{
			cmdbServiceProxy=this.getCmdbService();
			if(cmdbServiceProxy!=null){
				String typeName=ServiceOptionUtil.getValue("cmdb.virtual.host", "vm");
				typeNode=cmdbServiceProxy.getCiTypeByName(typeName);
			}
		}catch(CmdbException ex){
			log.error("获得Cmdb远程服务失败！错误信息：",ex);
			throw new BuessionException(ex);
		}catch(Throwable ex){
			log.error("获得Cmdb远程服务失败！错误信息：",ex);
			throw new BuessionException(ex);			
		}
		if(typeNode==null){
			log.error("获得Cmdb远程虚拟机服务失败！");
			throw new BuessionException("获得Cmdb远程虚拟机服务失败！");
		}
		
	
		JSONArray result= new JSONArray();
		List<Node> nodes=new ArrayList<Node>();
	
		final Map<String,PhysicsHost> mapVirtualMachine=new ConcurrentHashMap<String,PhysicsHost>();
		
		
		try{
//			InputStream inStream = ClassLoaderUtil.getResourceAsStream("config.properties",this.getClass());
//			if (null == inStream) {
//				throw new BuessionException("Not found configurtion file. file name is config.properties");
//			}
//			Properties props = new Properties();
//			props.load(inStream);
//			String hostType=props.getProperty("host_type", "HP");	
//			String hostType=ServiceOptionUtil.getValue("cmdb.default.host.type", "HP");
			String relationType=ServiceOptionUtil.getValue("cmdb.relation", "linked");
			String virtualMachineType=ServiceOptionUtil.getValue("cmdb.virtual.host", "vm");
			List<Long> nodeIds=new ArrayList<Long>();
			if(virtualMachines!=null && !virtualMachines.isEmpty()){		
				for(int index=0;index<virtualMachines.size();index++ ){
					JSONObject json  =(JSONObject) virtualMachines.get(index);
					if(log.isInfoEnabled()){
						 log.info("传递CMDB server的虚拟主机信息："+json.toString());
					}
					RelationNode relationNode=cmdbServiceProxy.getRelationCiByName(relationType, json.getString("instancename"));
					if(relationNode !=null){
						cmdbServiceProxy.deleteRelation(relationType, relationNode.getId());
					}
					RelationNode relationNode2=cmdbServiceProxy.getRelationCiByName(relationType, json.getString("instancename")+"_"+json.getString("project"));
					if(relationNode !=null){
						cmdbServiceProxy.deleteRelation(relationType, relationNode2.getId());
					}	
					Node node= cmdbServiceProxy.getCiByName(virtualMachineType, json.getString("name"));
					if(node !=null){
						nodeIds.add(node.getId());
					}
				}
			}
			int count=cmdbServiceProxy.deleteNodes(virtualMachineType, nodeIds);
			JSONObject jsonObject=new JSONObject();
			if(count<virtualMachines.size()){
				jsonObject.put("status",HttpStatus.SC_FORBIDDEN);
				jsonObject.put("errMessage", "删除虚拟机物理节点部分失败，请检查数据。");
			}else{
				jsonObject.put("status",HttpStatus.SC_OK);	
			}
			result.add(jsonObject);	
			
		}catch(CmdbException ex){
			log.error("发送CMDB server 发生错误，错误信息：",ex);
			throw new BuessionException(ex);
		}catch(Throwable ex){
			log.error("发送CMDB server 发生错误，错误信息：",ex);
			throw new BuessionException(ex);			
		}
		return result;
	}
	@Override
	public JSONArray migrateVirtualMachineCmdb(JSONObject json)
			throws BuessionException {
		JSONArray result= new JSONArray();
		JSONObject jsonObject=new JSONObject();
		Map<String,String> mapMaping=getVirtualHostMaping();
		jsonObject.put("status",HttpStatus.SC_OK);
		String relationType=ServiceOptionUtil.getValue("cmdb.relation", "host_vm_relation");
		String virtualMachineType=ServiceOptionUtil.getValue("cmdb.virtual.host", "vm");
		String hostType=ServiceOptionUtil.getValue("cmdb.host", "Host");
		CmdbService cmdbServiceProxy=null;
		cmdbServiceProxy = this.getCmdbService();
		Node vmnode= cmdbServiceProxy.getCiByName(virtualMachineType, json.getString("name"));
		Node hostnode= cmdbServiceProxy.getCiByName(hostType, json.getString("hostname"));
		if(hostnode !=null ){
			RelationNode relationNode=cmdbServiceProxy.getRelationCiByName(relationType, json.getString("instancename"));
			vmnode.getAttributes().get(mapMaping.get("hostname")).setValue(json.getString("hostname"));
			try {
				Node node = cmdbServiceProxy.updateNode(vmnode);
				relationNode.setTargetId(hostnode.getId());
				relationNode.setTargetLable(hostnode.getName());
				relationNode.setTargetName(hostnode.getName());
				relationNode.setTargetType(hostnode.getDerivedFrom());
				RelationNode rn = cmdbServiceProxy.updateRelation(relationNode);
				if(node == null || rn == null){
					jsonObject.put("status",HttpStatus.SC_FORBIDDEN);
					jsonObject.put("errMessage", "更新虚拟机和关系节点失败，请检查数据。");	
				}
			} catch (CmdbException e) {
				log.error("发送CMDB server 发生错误，错误信息：",e);
				throw new BuessionException(e);
			}
		}
		result.add(jsonObject);
		return result;
	}
	@Override
	public JSONArray scaleVirtualMachineCmdb(JSONObject json)
			throws BuessionException {
		JSONArray result= new JSONArray();
		Map<String,String> mapMaping=getVirtualHostMaping();
		String virtualMachineType=ServiceOptionUtil.getValue("cmdb.virtual.host", "vm");
		CmdbService cmdbServiceProxy=null;
		cmdbServiceProxy = this.getCmdbService();
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("status",HttpStatus.SC_OK);
		Node vmnode= cmdbServiceProxy.getCiByName(virtualMachineType, json.getString("name"));
		if(vmnode !=null ){
			Map<String, CiAttribute> mapCiAttribute = vmnode.getAttributes();
			if(!mapCiAttribute.isEmpty()){			
				mapCiAttribute.get(mapMaping.get("cpunumber")).setValue(json.getString("cpunumber"));
				//mapCiAttribute.get(mapMaping.get("cpuspeed")).setValue(json.getString("cpuspeed"));
				mapCiAttribute.get(mapMaping.get("memory")).setValue(json.getString("memory"));
			}
			vmnode.setAttributes(mapCiAttribute);
			try {
			Node node = cmdbServiceProxy.updateNode(vmnode);
			if(node == null){
				jsonObject.put("status",HttpStatus.SC_FORBIDDEN);
				jsonObject.put("errMessage", "更新虚拟机节点部分失败，请检查数据。");	
			}
			} catch (CmdbException e) {
				log.error("发送CMDB server 发生错误，错误信息：",e);
				throw new BuessionException(e);
			}			
		}
		result.add(jsonObject);
		return result;
	}
	@Override
	public JSONArray saveProjectCmdb(JSONArray arrProject) throws BuessionException {
		
		CmdbService cmdbServiceProxy=null;
		Node typeNode=null;
		try{
			cmdbServiceProxy=this.getCmdbService();
			if(cmdbServiceProxy!=null){
				String typeName=ServiceOptionUtil.getValue("cmdb.project", "project");
				typeNode=cmdbServiceProxy.getCiTypeByName(typeName);
			}
		}catch(CmdbException ex){
			log.error("获得Cmdb远程服务失败！错误信息：",ex);
			throw new BuessionException(ex);
		}catch(Throwable ex){
			log.error("获得Cmdb远程服务失败！错误信息：",ex);
			throw new BuessionException(ex);			
		}
		if(typeNode==null){
			log.error("获得Cmdb远程虚拟机服务失败！");
			throw new BuessionException("获得Cmdb远程虚拟机服务失败！");
		}	
		JSONArray result= new JSONArray();
		List<Node> nodes=new ArrayList<Node>();		
		try{
			if(arrProject!=null && !arrProject.isEmpty()){
				for(int index=0;index<arrProject.size();index++ ){
					JSONObject json  =(JSONObject) arrProject.get(index);
					if(log.isInfoEnabled()){
						 log.info("传递CMDB server的业务系统信息："+json.toString());
					}
					Node cmdbNode=new Node();				
					cmdbNode.setName(json.getString("name"));
					String displayname=StringUtils.isBlank(json.getString("displayname"))?json.getString("name"):json.getString("displayname");
					cmdbNode.setLabel(displayname);
					cmdbNode.setIsType(false);
					cmdbNode.setParentId(typeNode.getId());
					cmdbNode.setDerivedFrom(typeNode.getName());
//					cmdbNode.setIcon(typeNode.getIcon());		 
					Map<String,String> mapMaping=getProjectMaping();
					Map<String, CiAttribute> attributes=cmdbNode.getAttributes();	//new HashMap<String, CiAttribute>();
					CiAttribute catt =null;
					for(Iterator<String> itor= mapMaping.keySet().iterator();itor.hasNext();){	
					    catt = new CiAttribute();
						String key=itor.next().toString();					
						if(log.isDebugEnabled()){
							log.debug(key+"="+mapMaping.get(key));
						}
						if(mapMaping.get(key).equals("name") || mapMaping.get(key).equals("displayname")){
							continue;
						}
						catt.setName(key);
						catt.setValue(json.getString(mapMaping.get(key)));
						catt.setViewMode(0);
						attributes.put(key,catt);				
					}			
					cmdbNode.setAttributes(attributes);
					nodes.add(cmdbNode);
				}
				List<Node> newNodes=cmdbServiceProxy.insertNodeCis(nodes);
				for(Node node :newNodes){
					JSONObject jsonObject=new JSONObject();
					if(StringUtils.isNoneBlank(String.valueOf(node.getId()))){
//						JSONObject jsonObject = JSONObject.fromObject(relation);
						jsonObject.put("status",HttpStatus.SC_OK);					
					}else{
						jsonObject.put("status",HttpStatus.SC_FORBIDDEN);	
					}
					result.add(jsonObject);
				}
			}			
		}catch(Throwable ex){
			log.error("发送CMDB server 发生错误，错误信息：",ex);
			throw new BuessionException(ex);			
		}
		return result;
	
	}
	@Override
	public JSONArray updateProjectVirtualMachineCmdb(JSONObject json)
			throws BuessionException {

		String vmTypeName=ServiceOptionUtil.getValue("cmdb.virtual.host", "vm");
		String projectTypeName = ServiceOptionUtil.getValue("cmdb.project", "project");
		String relationType=ServiceOptionUtil.getValue("cmdb.relation", "linked");
		CmdbService cmdbServiceProxy=null;
		Node vmTypeNode=null;
		Node projectTypeNode=null;
		try{
			cmdbServiceProxy=this.getCmdbService();
			if(cmdbServiceProxy!=null){				
				vmTypeNode=cmdbServiceProxy.getCiTypeByName(vmTypeName);				
				projectTypeNode=cmdbServiceProxy.getCiTypeByName(projectTypeName);
			}
		}catch(CmdbException ex){
			log.error("获得Cmdb远程服务失败！错误信息：",ex);
			throw new BuessionException(ex);
		}catch(Throwable ex){
			log.error("获得Cmdb远程服务失败！错误信息：",ex);
			throw new BuessionException(ex);			
		}
		if(vmTypeNode==null || projectTypeNode == null ){
			log.error("获得Cmdb远程虚拟机服务失败！");
			throw new BuessionException("获得Cmdb远程虚拟机服务失败！");
		}	
		JSONArray result= new JSONArray();
		List<Node> nodes=new ArrayList<Node>();		
		try{
			if(log.isInfoEnabled()){
				log.info("分配业务系统 json信息："+json.toString());
			}
			List<RelationNode> relations=new ArrayList<RelationNode>(); 
			RelationNode relationNode = cmdbServiceProxy.getRelationCiByName(relationType, json.getString("instancename")+"_"+json.getString("projectName"));
			if(relationNode !=null){
				cmdbServiceProxy.deleteRelation(relationType, relationNode.getId());
			}
			Node vmNode = cmdbServiceProxy.getCiByName(vmTypeName, json.getString("instancename"));
			Node proNode = cmdbServiceProxy.getCiByName(projectTypeName, json.getString("newProjectName"));
			if(vmNode!=null && proNode!=null ){
				
			RelationNode rnT = cmdbServiceProxy.getRelationTypeByName(relationType);
			RelationNode vmProjectRelation=rnT.clone();			
			vmProjectRelation.setName(json.getString("instancename")+"_"+json.getString("newProjectName"));
			vmProjectRelation.setIsType(false);
			vmProjectRelation.setId(null);				
			vmProjectRelation.setDerivedFrom(rnT.getName());
			
			vmProjectRelation.setSourceId(vmNode.getId());
			vmProjectRelation.setSourceName(vmNode.getName());				
			vmProjectRelation.setSourceLable(vmNode.getLabel());			
			vmProjectRelation.setSourceType(vmNode.getDerivedFrom());	
			
			vmProjectRelation.setTargetId(new Long(proNode.getId()).longValue());
			vmProjectRelation.setTargetLable(proNode.getLabel());
			vmProjectRelation.setTargetName(proNode.getName());
			vmProjectRelation.setTargetType(proNode.getDerivedFrom());
			
			relations.add(vmProjectRelation);
			List<RelationNode> newRelations=cmdbServiceProxy.insertRelationCis(relations);
			
			
			for(RelationNode relation :newRelations){
				JSONObject jsonObject=new JSONObject();
				if(StringUtils.isNoneBlank(String.valueOf(relation.getId()))){
					jsonObject.put("status",HttpStatus.SC_OK);					
				}else{
					jsonObject.put("status",HttpStatus.SC_FORBIDDEN);	
				}
				result.add(jsonObject);
			}
		}
						
		}catch(Throwable ex){
			log.error("发送CMDB server 发生错误，错误信息：",ex);
			throw new BuessionException(ex);			
		}
		return result;
	}
}
