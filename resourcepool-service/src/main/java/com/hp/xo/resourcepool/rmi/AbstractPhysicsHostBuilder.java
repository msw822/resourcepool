package com.hp.xo.resourcepool.rmi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;  
import net.sf.json.util.JSONUtils;

import org.apache.log4j.Logger;

import com.hp.xo.resourcepool.dto.PhysicsHost;
import com.hp.xo.resourcepool.exception.BuessionException;
public abstract class  AbstractPhysicsHostBuilder {
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	public abstract JSONArray getCmdbHost()throws BuessionException;	
	public List<PhysicsHost> buildHostDataSource()throws BuessionException{
		JSONArray arrHosts=getCmdbHost();
		List<PhysicsHost> listHost=new ArrayList<PhysicsHost>();
		if(arrHosts!=null && !arrHosts.isEmpty()){			
			for(int index=0;index<arrHosts.size();index++ ){
				 JSONObject json  = arrHosts.getJSONObject(index);
				 log.info(json.toString());
				 JsonConfig config=new JsonConfig();
				 config.setRootClass(PhysicsHost.class);
//				 JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] {"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"}));
//				 config.setJavaIdentifierTransformer(new JavaIdentifierTransformer() {				  
//			       @Override
//			       public String transformToJavaIdentifier(String str) {
//			         char[] chars = str.toCharArray();
//			         chars[0] = Character.toLowerCase(chars[0]);
//			         return new String(chars);
//			       }
//			        
//			     });
				  PhysicsHost host=(PhysicsHost)JSONObject.toBean(json,config);
				 listHost.add(host);
			}
			
		}
		return listHost;
	}
	public abstract JSONArray saveCmdbVirtualHost(JSONArray arrHosts,Map<String,PhysicsHost> physicsHost)throws BuessionException;
	public abstract JSONArray deleteCmdbVirtualMachines(JSONArray arrHosts,Map<String,PhysicsHost> physicsHost)throws BuessionException;
	public abstract JSONArray migrateVirtualMachineCmdb(JSONObject json)throws BuessionException;
	public abstract JSONArray scaleVirtualMachineCmdb(JSONObject json)throws BuessionException;
	public abstract JSONArray saveProjectCmdb(JSONArray arrayProject)throws BuessionException;
	public abstract JSONArray updateProjectVirtualMachineCmdb(JSONObject json)throws BuessionException;
	
}
