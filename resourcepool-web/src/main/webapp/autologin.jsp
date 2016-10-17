<%@ page contentType="text/html; charset=UTF-8" %>
 
<%		
		boolean haslogo = request.getParameter("haslogo")==null?true:Boolean.valueOf(request.getParameter("haslogo"));
		String url="api.action";
		String domain ="";
		String username =  request.getParameter("username");
		String pwd =  request.getParameter("pwd");
		domain =  request.getParameter("domain")==null?"":request.getParameter("domain");
		String service=request.getParameter("service");		


%> 
 
<script src="lib/jquery.js" type="text/javascript"></script>
<script src="lib/jquery.cookies.js" type="text/javascript"></script>
<script src="scripts/autologin.js" type="text/javascript"></script>
<script type="text/javascript">

		var service ='<%=service%>';

		var contextPath = '${pageContext.request.contextPath}';
		
		var url =contextPath+"/"+"<%=url%>";
		
		$(document).ready(function() {
			
			var loginCmdText="&username=<%=username%>&password=<%=pwd%>&domain=<%=domain%>";
			
			 if(login(url,loginCmdText))
			 {
				 if(service!=null)
				{
					 window.location.href=service;
					 
				}
				 else{
					 window.location.href ="index.jsp?haslogo="+<%=haslogo%>;
				 }
				
			 }
			 else{
				 
				 alert("登录失败!");
				 
			 }
			
			
		});
	</script>
        
