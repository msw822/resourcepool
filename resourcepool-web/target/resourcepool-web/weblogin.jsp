<%@ page contentType="text/html; charset=UTF-8"%>

<%
	String web= request.getParameter("web");

	if(web!=null)
	{
		session.setAttribute("web", web);
	}

%>

<script src="lib/jquery.js" type="text/javascript"></script>
<script src="lib/jquery.cookies.js" type="text/javascript"></script>
<script src="scripts/autologin.js" type="text/javascript"></script>
<script type="text/javascript">

	var web='<%=request.getParameter("web")%>';

	var contextPath = '${pageContext.request.contextPath}';
	
	var loginUsername = 'admin'
	var loginPassword='password';
	var domain='';
	var loginCmdText="&username="+loginUsername+"&password="+loginPassword+"&domain="+domain;
	var url = contextPath+'/api.action';
	
	$(document).ready(function() {
		
		var weburl=web+'/getuserinfo.jsp';
		 
		if(web=="")
		{
			alert('非法的用户!');
			return;
			
		}
		
		login(url,loginCmdText);
		var username;
		var roleId;
		var deptId;
		var accounttype;
		
		$.ajaxSetup({
			  
			  url: contextPath+"/api.action",
			  global: false,
			  type: "GET"
			});
		
		$.ajax({
			type : 'GET',
			url : weburl,
			dataType : 'jsonp',
			async:false,
			jsonp : "jsoncallback",
			success : function(data) {
			
				username = data.user_id;
				roleId = data.role_id;
				deptId = data.dept_id;
				//alert("用户名：" + username);
				
				if ($.trim(username).length ==0) 
				{ 
					alert('username不能为空');
					return;
				} 
				if ($.trim(roleId).length ==0) 
				{ 
					alert('roleId不能为空');
					return;
				}
				if ($.trim(deptId).length ==0) 
				{ 
					alert('deptId不能为空');
					return; 
				} 
				
				var accountusername="";
				
				//alert(roleId);
				
				if(roleId=='admin')
				//if(roleId.contains('admin'))
				{
					accounttype=1;
					
					accountusername="admin_"+deptId;
					
				}else{
					accounttype=0;
					accountusername="user_"+deptId;
				}
				
				var url1="command=createAccount&response=json&accounttype="+accounttype+"&email=hp@hp.com&firstname="+accountusername+"&lastname="+accountusername+"&password="+accountusername+"&username="+accountusername;
				
					$.ajax({
						  async:false,
						  dataType : 'json',
						  data:url1
					});
					
				
				
				var url2="command=createUser&response=json&account="+accountusername+"&email=hp@hp.com&firstname="+username+"&lastname="+username+"&password="+username+"&username="+username;
					
					$.ajax({
						  async:false,
						  dataType : 'json',
						  data:url2
					}); 
				
					var url ="autologin.jsp?username="+username+"&pwd="+username+"&haslogo=true";
					
					if(username==loginUsername)
					{
						  url ="autologin.jsp?username="+username+"&pwd="+loginPassword+"&haslogo=true";
						  
					}
					
					
					url =url+"&service="+web;	
					window.location.href =url;
					
					
					//window.navigate(url);
			 	/**/
				//alert(username);
				//alert(roleId);
				//alert(deptId);
			},
			error : function() {
				alert('未登录，或者登录超时.');
			}
			
		});
		
		
	
		
		
		
		
	});
</script>





