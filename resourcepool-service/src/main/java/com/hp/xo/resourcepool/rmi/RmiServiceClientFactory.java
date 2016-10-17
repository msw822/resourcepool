package com.hp.xo.resourcepool.rmi;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.hp.xo.resourcepool.utils.SpringUtil;

/**
 * rmi服务的调用
 * @author dujun
 *
 */
public class RmiServiceClientFactory implements Serializable{
	protected final Logger log = Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 1L;
	private ApplicationContext context=null;
	private static class SingletonHolder {  
        static final RmiServiceClientFactory INSTANCE = new RmiServiceClientFactory();  
    }
	public static RmiServiceClientFactory getInstance() {  
        return SingletonHolder.INSTANCE;  
    }  
   
    /** 
     * private的构造函数用于避免外界直接使用new来实例化对象 
     */  
    private RmiServiceClientFactory() { 
    	try{
    		context =SpringUtil.getApplicationContext();
    				
    	}catch(BeansException er){
    		log.error("找不到指定的文件", er);
    	}
    	 
    }  
    public Object getServiceClientByName(String serviceName){
    	if(context==null){
    		log.error("找不到远程服务的本地配置信息");
    		return null;
    	}else{
    		return context.getBean(serviceName);  
    	}    	
    }
    /** 
     * readResolve方法应对单例对象被序列化时候 
     */  
    private Object readResolve() {  
        return getInstance();  
    }  
}
