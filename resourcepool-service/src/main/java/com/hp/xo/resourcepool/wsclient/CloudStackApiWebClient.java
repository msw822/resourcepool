package com.hp.xo.resourcepool.wsclient;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.hp.xo.resourcepool.model.Response;
import com.hp.xo.resourcepool.utils.StringUtil;

/**
 * 
 * @author john
 *
 */
@Service("cloudStackApiWebClient")
public class CloudStackApiWebClient {
	private final Logger log = Logger.getLogger(this.getClass());  
    public static final String METHOD_TYPE_GET = "GET"; 
    public static final String METHOD_TYPE_POST = "POST"; 
    private String apiSecretUrl = null;
    private String apiUnSecretUrl = null;
    
	public CloudStackApiWebClient() {
		super();
	}
	
	public String getApiSecretUrl() {
		return apiSecretUrl;
	}

	public void setApiSecretUrl(String apiSecretUrl) {
		this.apiSecretUrl = apiSecretUrl;
	}

	public String getApiUnSecretUrl() {
		return apiUnSecretUrl;
	}

	public void setApiUnSecretUrl(String apiUnSecretUrl) {
		this.apiUnSecretUrl = apiUnSecretUrl;
	}
	
	@SuppressWarnings("deprecation")
	public Response request(String methodType, Map<String, Object[]> params, boolean isSecret) { 
        HttpUriRequest httpReq = null;
        HttpResponse httpResp = null;
        String url = isSecret ? apiSecretUrl : apiUnSecretUrl;
        Response result = new Response(); 
        long timeout = 3l;
        timeout = timeout * 1000;  
        
        if (METHOD_TYPE_GET.equals(methodType)) {
        	StringBuilder sb = new StringBuilder();
        	if (null != params) {
    			for (String key : params.keySet()) {
    				if (false == "secretkey".equalsIgnoreCase(key) && false == "sessionkey".equalsIgnoreCase(key)) {
    					if(params.get(key)[0] instanceof Boolean){
    						sb.append(key).append("=").append(params.get(key)[0]).append("&");
    					}else{
    						sb.append(key).append("=").append(StringUtil.encoder((String)params.get(key)[0])).append("&");
    					}
    					
    				}
    			}
    			sb.deleteCharAt(sb.length() - 1);
    			
    			if (isSecret) {
    				url = url + "?" + sb.toString() + "&signature=" + StringUtil.getSignature(params, true);
    			} else {
    				url = url + "?" + sb.toString();
    			} 
    				
	        	if (log.isDebugEnabled()) {
	        		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ", generic url -> " + url);
	        	}
        	}
            	
        	httpReq = new HttpGet(url);
        	
         } else { 
        	 
        	httpReq = new HttpPost(url);
        	List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        		
			if (null != params) {
				Set<String> keys = params.keySet();
				for (String key : keys) {
					if (false == "secretkey".equalsIgnoreCase(key) && false == "sessionkey".equalsIgnoreCase(key)) {
						postParams.add(new BasicNameValuePair(key, (String)params.get(key)[0]));
					}
				}
				
				if (isSecret) {
					String signature = StringUtil.getSignature(params, false);
					postParams.add(new BasicNameValuePair("signature", signature));
				}
				UrlEncodedFormEntity uefEntity;
				try {
					uefEntity = new UrlEncodedFormEntity(postParams, "UTF-8");
					((HttpPost)httpReq).setEntity(uefEntity);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}        		
			
			if (log.isDebugEnabled()) {
				Iterator<NameValuePair> it = postParams.iterator();				
				while (it.hasNext()) {
					NameValuePair nv = it.next();				
					log.debug(nv.getName() + "=" + nv.getValue());				
				}
				
			}
         }
        
        CloseableHttpClient httpclient = HttpClientBuilder.create().build();
        
        try {
			httpResp = httpclient.execute(httpReq);
			
	        if (null != httpResp) {
	        	HttpEntity entityResp = httpResp.getEntity();
	        	
	        	result.setResponseString(EntityUtils.toString(entityResp, "UTF-8"));
				result.setStatusCode(httpResp.getStatusLine().getStatusCode());
				
	        	if (log.isDebugEnabled()) {
	    	        Header[] hds = httpResp.getAllHeaders();
	    	        if (null != hds) {
		    	        for (Header hd : hds) {
		    	        	log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " : HttpRequest response headName[" + hd.getName() + "] headValue[" + hd.getValue() + "]");
		    	        }
	        		} else {
	        			log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + " : HttpRequest response header is empty");
	        		}
	    	        
	            }
	        	
	        }

    	
         } catch (Exception e) {
        	 // TODO
        	log.error(e.getMessage(), e);
        	//TODO throwing and handling
         } finally {
        	 httpclient.getConnectionManager().shutdown();
         }
        	 
    	
    	if (log.isDebugEnabled()) {
    		log.debug(Thread.currentThread().getStackTrace()[1].getMethodName() + ": httpclient.execute(new HttpRequest(" + url + ") respone -> " + result);
    	}
        	
        	
        return result; 
 
    } 
 
    
    private static Map<String, String> header2Map(Header[] headers) { 
        Map<String, String> tmp = null; 
        if (null != headers) { 
            tmp = new HashMap<String, String>(); 
            for (Header header : headers) { 
                tmp.put(header.getName(), header.getValue()); 
            } 
        } 
        return tmp; 
    }  
    

	
}


