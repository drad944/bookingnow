<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ taglib prefix="json" uri="http://www.atg.com/taglibs/json" %>

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
	<div>
		<table>
			<tr>
				<td>username:</td>
				<td>
					<json:object>
						<json:property name="username" value="${loginUser.user_name}"/>
					</json:object>
				</td>
			</tr>
			
			<tr>
				<td>account:</td>
				<td>
					<json:object>
						<json:property name="account" value="${loginUser.user_account}"/>
					</json:object>
				</td>
			</tr>
			
			<tr>
				<td>sex:</td>
				<td>
					<json:object>
						<json:property name="sex" value="${loginUser.user_sex}"/>
					</json:object>
				</td>
			</tr>
			
		</table>
	</div>
</body>
</html>