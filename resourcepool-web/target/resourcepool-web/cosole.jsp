<%@ page contentType="text/html; charset=UTF-8" %>
 
<%		
		boolean haslogo = request.getParameter("haslogo")==null?true:Boolean.valueOf(request.getParameter("haslogo"));
		String url="api";
		String domain ="";
		 String username =  request.getParameter("username");
		String pwd =  request.getParameter("pwd"); 
		
		/* String username = "admin";
		String pwd =  "password"; */
		domain =  request.getParameter("domain")==null?"":request.getParameter("domain");
		String cmd=request.getParameter("cmd")==null?"":request.getParameter("cmd");
		String vm=request.getParameter("vm")==null?"":request.getParameter("vm");

%> 
 
<script src="lib/jquery.js" type="text/javascript"></script>
<script src="lib/jquery.cookies.js" type="text/javascript"></script>
<script src="scripts/autologin.js" type="text/javascript"></script>
<script type="text/javascript">

		var contextPath = '${pageContext.request.contextPath}';
		
		var url =contextPath+"/"+"<%=url%>";
		
		$(document).ready(function() {
			
			var loginCmdText="&username=<%=username%>&password=<%=pwd%>&domain=<%=domain%>";
			
			 if(login(url,loginCmdText))
			 {
				window.location.href ="index2.jsp?vm=<%=vm%>&cmd=<%=cmd%>";
				//alert(333);
			 }
			 else{
				 
				 alert("登录失败!");
				 
			 }
			
			
		});
	</script>
 
