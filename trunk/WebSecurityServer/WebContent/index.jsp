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
	
	<link rel="stylesheet" type="text/css" href="CSS/head.css">
	
	<script src="${pageContext.request.contextPath}/JS/jquery-1.10.2.js" type="text/javascript"></script>
	<script>
	 
		$(document).ready(function(){
		    var top = $('#top');
		     
		    $.post('Page/Common/top.html',function(data) {
		        var tmp = $('<div></div>').html(data);
		 
		        data = tmp.find('#content').html();
		        tmp.remove();
		         
		        top.html(data);
		    });
		});
	</script>
  
  </head>
  
  <body>
	<div id="top" alt="头部div"></div>
	<div>
		<hr />
	</div>
	<div id="main" alt="页面主体">页面主体</div>

</body>
</html>
