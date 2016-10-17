<%@ page contentType="text/html; charset=UTF-8" %>
 
<%		
		boolean haslogo = request.getParameter("haslogo")==null?true:Boolean.valueOf(request.getParameter("haslogo"));
		String url="api.action";
		String domain ="";
		String username =  request.getParameter("username");
		String pwd =  request.getParameter("pwd");
		domain =  request.getParameter("domain")==null?"":request.getParameter("domain");
		String cmd=request.getParameter("cmd")==null?"":request.getParameter("cmd");
		String vm=request.getParameter("vm")==null?"":request.getParameter("vm");

%> 
 <iframe  src="${pageContext.request.contextPath}/console?cmd=<%=cmd%>&vm=<%=vm%>" width="100%" height="100%" frameborder="no" border="0" marginwidth="0" marginheight="0" scrolling="no" allowtransparency="yes">
 </iframe>       
