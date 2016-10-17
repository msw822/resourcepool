package com.hp.xo.resourcepool.web.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hp.xo.resourcepool.service.GlobalSettingManager;
import com.hp.xo.resourcepool.utils.ServiceOptionUtil;


/**
 * Thumbnail access : /console?cmd=thumbnail&vm=xxx&w=xxx&h=xxx
 * Console access : /conosole?cmd=access&vm=xxx
 * Authentication : /console?cmd=auth&vm=xxx&sid=xxx
 */
@Component("consoleServlet")
public class ConsoleProxyServlet extends HttpServlet {
    private static final long serialVersionUID = -5515382620323808168L;
    public static final Logger s_logger = Logger.getLogger(ConsoleProxyServlet.class.getName());
    @Autowired
	private GlobalSettingManager globalSettingManager;
  //  private static final String console="http://16.159.216.55:8080/client";
      String console="";
    
    public ConsoleProxyServlet() {
    	//globalSettingManager=(GlobalSettingManager) WebApplicationContextUtils.getWebApplicationContext(this.getServletContext()).getBean("globalSettingManager");
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
    	System.out.println("进入init()方法");
    	
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
    	System.out.println("进入doPost()方法");
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    	System.out.println("进入doGet()方法"+"====>>>"+globalSettingManager);
        try {
        	
           
           System.out.println("username:"+ServiceOptionUtil.obtainCloudStackUsername()+"<<<<>>>password:"+ServiceOptionUtil.obtainCloudStackPassword());


            Map<String, Object[]> params = new HashMap<String, Object[]>();
            System.out.println("req.getParameterMap()>>>"+req.getParameterMap());
            params.putAll(req.getParameterMap());
            String cmd=req.getParameter("cmd");
            String vm=req.getParameter("vm");
            System.out.println("cmd:"+params.get("cmd")+"<<<>>>vm:"+params.get("vm"));
           
            
          //req.getRequestDispatcher("http://16.159.216.55:8080/client/cosole.jsp?username="+ServiceOptionUtil.obtainCloudStackUsername()+"&pwd="+ServiceOptionUtil.obtainCloudStackPassword()+"&cmd="+params.get("cmd")+"&vm="+params.get("vm")).forward(req, resp);
           console=globalSettingManager.getValueByKey("console");
          resp.sendRedirect(console+"/console?username="+ServiceOptionUtil.obtainCloudStackUsername()+"&pwd="+ServiceOptionUtil.obtainCloudStackPassword()+"&cmd="+cmd+"&vm="+vm);
          
        } catch (Throwable e) {
            s_logger.error("Unexepected exception in ConsoleProxyServlet", e);
        }
    }

   
}