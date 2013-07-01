<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="${pageContext.request.contextPath}/JS/jquery-1.6.4.min.js" type="text/javascript"></script>
	<script language="javascript">
		function loginSubmit(){
			
		    	$.post("loginUser.action", 
		    			{
		    				
		    					account:$("#account").val(),
		    					password:$("#password").val()
		    				
		    				
		    			}, 
		    		    function(data){
		    				var user = data;
		    				alert("user name:" + user.name);
		    				
		    		        $.each(data, function(areaIdx, area) {
		    		            //alert(area.radius);
		    		        });
		    		    },
		    		    "json");
		}
	</script>
  </head>
  
  <body>
    This is my JSP page. <br>
    
    	<table>
    		<tr>
    			<td>username:</td>
    			<td><input type="text" id="account" name="account" value="" /></td>
    		</tr>
    		<tr>
    			<td>password:</td>
    			<td><input type="text" id="password" name="password" value="" /></td>
    		</tr>
    		<tr>
    			<td><input type="button" name="submit" value="submit" onclick="loginSubmit();"/></td>
    			<td><input type="reset" name="reset" value="reset" /></td>
    		</tr>
    		<tr>
    			<td></td>
    			<td></td>
    		</tr>
    	</table>
    
    <h2><a href="Page/Common/registerUser.jsp">register account</a></h2>
  </body>
</html>
