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
  </head>
  
  <body>
	<div id="header">
		<div class="header-wrap">
		
			<div class="h-logo">
	            <a href="http://dd.taobao.com/" target="_self"><img width="120" src="http://img01.taobaocdn.com/tps/i1/T16OG2XpNdXXaOesbb-144-50.png"/>
	            </a>
	        </div>
	        <div class="h-city">
	            <a href="http://life.taobao.com/market/life/city_page.php">
	                <span>南京</span>
	                <i>[城市切换]</i>
	            </a>
	        </div>
		

			<div id="J_Nav" class="main-nav">
				<ul>
					<li class="J_nav_1 "><a class="nav_dd" title="菜单管理"
						href="http://dd.taobao.com" target="_top"> <!--<i class="iconfont">ì</i>-->
							<s></s> <span>菜单管理</span>
					</a></li>
					<li class="J_nav_2 "><a class="nav_waimai" title="订单管理"
						href="http://waimai.taobao.com" target="_top"> <!--<i class="iconfont">?</i>-->
							<s></s> <span>订单管理</span>
					</a></li>
					<li class="J_nav_3"><a class="nav_shanghu" title="用户管理"
						href="http://list.bendi.taobao.com/list.htm" target="_top"> <s></s>
							<!--<i class="iconfont">?</i>--> 用户管理
					</a></li>
					
					<li class="J_nav_4"><a class="nav_shanghu" title="餐桌管理"
						href="http://list.bendi.taobao.com/list.htm" target="_top"> <s></s>
							<!--<i class="iconfont">?</i>--> 餐桌管理
					</a></li>
					<li class="J_nav_5 "><a class="nav_map"
						href="http://map.taobao.com" title="地图" target="_blank"> <!--<i class="iconfont">?</i>-->
							<s></s> <span>地图</span></a></li>
				</ul>
			</div>
			<div id="J_HeaderSearch" class="h-search-box">
				<ul id="J_SearchTab" class="h-search-tab">
					<li class="h-selected-tab"><a data-form="goodsForm"
						data-placeholder="找外卖" data-chartset="gbk" href="javascript:;">找外卖</a>
					</li>
					<li><a data-form="shopForm" data-placeholder="找商户"
						href="javascript:;"
						data-suggest="http://list.bendi.taobao.com/provider/suggest.htm?_input_charset=utf-8&city={cityId}&callback=KISSY.Suggest.callback">找商户</a>
					</li>
				</ul>
				<div class="h-search-wrap">
					<form id="J_goodsForm" target="_top" action="http://waimai.taobao.com/shop_list.htm">
						<input id="J_SearchInput" data-placeholder="找外卖"
							class="h-search-input J_KeyWord" name="q" type="text"
							x-webkit-speech x-webkit-grammar accept=" builtin:translate"
							autocomplete="off" /> 
						<input type="hidden" name="city" class="J_CityId" /> 
						<input type="hidden" name="keyword" value="" class="J_KeyWord" /> 
						<input type="hidden" name="nowhere"	value="true" />
					</form>
					<form id="J_shopForm" target="_top"	action="http://list.bendi.taobao.com/list.htm">
						<input type="hidden" name="q" value="" class="J_KeyWord" /> 
						<input type="hidden" class="J_CityId" name="city" /> 
						<input type="hidden" name="from" value="bendi_search" />
					</form>
					<a id="J_SubmitFrom" href="javascript:;"></a>
				</div>
			</div>
		</div>
	</div>
	<hr />


	
</body>
</html>