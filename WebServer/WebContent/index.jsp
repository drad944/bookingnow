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
  </head>
  
  <body>
    This is my JSP page. <br>
    
    <form action="test/findAccount.action" method="post">
    	<table>
    		<tr>
    			<td>username:</td>
    			<td><input type="text" name="account.name" value="" /></td>
    		</tr>
    		<tr>
    			<td>password:</td>
    			<td><input type="text" name="account.password" value="" /></td>
    		</tr>
    		<tr>
    			<td><input type="submit" name="submit" value="submit" /></td>
    			<td><input type="reset" name="reset" value="reset" /></td>
    		</tr>
    		<tr>
    			<td></td>
    			<td></td>
    		</tr>
    	</table>
    </form>
    
    <h2><a href="Page/registerAccount.jsp">register account</a></h2>
  </body>
</html>
