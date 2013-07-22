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
					<li class="J_nav_1 "><a class="nav_dd" title="点菜"
						href="http://dd.taobao.com" target="_top"> <!--<i class="iconfont">ì</i>-->
							<s></s> <span>点菜</span>
					</a></li>
					<li class="J_nav_2 "><a class="nav_waimai" title="外卖"
						href="http://waimai.taobao.com" target="_top"> <!--<i class="iconfont">?</i>-->
							<s></s> <span>外卖</span>
					</a></li>
					<li class="J_nav_3"><a class="nav_shanghu" title="商户"
						href="http://list.bendi.taobao.com/list.htm" target="_top"> <s></s>
							<!--<i class="iconfont">?</i>--> 商户
					</a></li>
					<li class="J_nav_4 "><a class="nav_map"
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


	<div class="main-wrap">
		<div class="col-main">
			<div class="mod">
				<h1></h1>
				<div class="pcrumbs">
					<a href="http://bendi.taobao.com/" target="_blank">淘宝本地生活</a> <a
						class="last" href='http://list.bendi.taobao.com/nanjing/list--'>南京本地商户<span>(2375)</span></a>
				</div>
			</div>

			<div class="mod">
				<div class="list_fliter" data-spm="1000217">
					<div class="repast list_nav">
						<span class="tit">餐饮美食</span>
						<div class="list">
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000035--istb-1'>小吃快餐</a><b>(4)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000033--istb-1'>甜品饮品</a><b>(9)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1002433--istb-1'>中餐菜系</a><b>(11)</b></span>
						</div>
					</div>

					<div class="Leisure">
						<span class="tit">休闲娱乐</span>
						<div class="list">
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000078--istb-1'>咖啡与茶</a><b>(5)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000079--istb-1'>运动健身</a><b>(13)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000082--istb-1'>足浴/洗浴</a><b>(3)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000077--istb-1'>美容美体</a><b>(39)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000081--istb-1'>书店/图书馆</a><b>(8)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000084--istb-1'>其他</a><b>(11)</b></span>
						</div>
					</div>
					<div class="shopping">
						<span class="tit">购物商店</span>
						<div class="list">
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000119--istb-1'>超市便利</a><b>(18)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000139--istb-1'>服装/饰品店</a><b>(343)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000140--istb-1'>鞋帽箱包皮具</a><b>(97)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1001085--istb-1'>家居/装潢</a><b>(133)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000147--istb-1'>运动/户外用品</a><b>(57)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000123--istb-1'>通讯/数码产品</a><b>(178)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000126--istb-1'>食品/零食店</a><b>(59)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000151--istb-1'>婴童用品店</a><b>(53)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000152--istb-1'>茶叶店</a><b>(4)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000141--istb-1'>化妆品店</a><b>(47)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000124--istb-1'>特产店</a><b>(4)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000150--istb-1'>烟酒专卖</a><b>(7)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000142--istb-1'>钟表/眼镜店</a><b>(14)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000145--istb-1'>花店</a><b>(33)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000149--istb-1'>珠宝首饰店</a><b>(18)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1001034--istb-1'>专柜/摊位</a><b>(15)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1002960--istb-1'>动漫/电玩店</a><b>(8)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000121--istb-1'>其他商店</a><b>(416)</b></span>
						</div>
						<span class="moreList"><a
							href='http://list.bendi.taobao.com/nanjing/list--c1-1000023--istb-1'
							target="_blank">更多>></a></span>
					</div>
					<div class="other_category list_nav">
						<span class="tit">其他类目</span>
						<div class="list">
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000024--istb-1'>摄影/便民</a><b>(140)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000026--istb-1'>教育培训</a><b>(54)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000025--istb-1'>旅游/酒店</a><b>(12)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000027--istb-1'>爱车服务</a><b>(106)</b></span>
							<span><a
								href='http://list.bendi.taobao.com/nanjing/list--c1-1000028--istb-1'>美妆/丽人</a><b>(49)</b></span>
						</div>
					</div>
					<div class="dotted-line"></div>
					<div class="district list_nav">
						<span class="tit">商圈</span>
						<div id="J_list" class="list" data-num="14" data-spm="1000218">
							<div class="list-inner">
								<span><a
									href='http://list.bendi.taobao.com/nanjing/list--istb-1'
									class="active">全市</a></span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320102--istb-1'>玄武区</a>
									<b>(532)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320115--istb-1'>江宁区</a>
									<b>(304)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320103--istb-1'>白下区</a>
									<b>(298)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320106--istb-1'>鼓楼区</a>
									<b>(247)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320113--istb-1'>栖霞区</a>
									<b>(158)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320107--istb-1'>下关区</a>
									<b>(155)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320104--istb-1'>秦淮区</a>
									<b>(140)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320111--istb-1'>浦口区</a>
									<b>(127)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320114--istb-1'>雨花台区</a>
									<b>(125)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320105--istb-1'>建邺区</a>
									<b>(122)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320116--istb-1'>六合区</a>
									<b>(77)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320124--istb-1'>溧水县</a>
									<b>(29)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320125--istb-1'>高淳县</a>
									<b>(14)</b>
								</span> <span> <a
									href='http://list.bendi.taobao.com/nanjing/list--ac-320126--istb-1'>其它区</a>
									<b>(2)</b>
								</span>
							</div>
							<div id="J_showhide" class="district-more">更多</div>

						</div>


						<!-- ba begin-->
						<!-- ba end-->


					</div>
				</div>
			</div>

			<div class="mod">
				<div class="list_tab">
					<ul class="clearfix" id="J_tab">
						<li><a href='http://list.bendi.taobao.com/nanjing/list--'
							data-spm="d1000975">所有商户</a></li>
						<li><a href="/list.htm?city=320100&amp;isdc=1" data-spm="">网上点菜</a><s
							class="hot-icon" id="J_hoticon"></s></li>
						<li><a href="/list.htm?city=320100&amp;yh=1"
							data-spm="d1000976">优惠商户</a></li>
						<li class="selected"><a
							href="/list.htm?city=320100&amp;istb=1" data-spm="">淘宝实体店</a></li>
					</ul>
				</div>

				<div class="search-wrap clearfix">
					<form action="/list.htm" id="quick-search" method="get">
						<ul class="search">
							<li class="isearch clearfix"><label for="keyword">关键字:</label>
								<div class="kw-wrap" data-spm="1000998">
									<input type="text" id="J_SearchKeyWord" placeholder="点击输入"
										d="kw" name="q" autocomplete="off" value="">
								</div>
								<button id="search-btn" type="submit">确定</button> <input
								type="hidden" id="J_From" name="from" /> <input type="hidden"
								name="city" value="320100"> <input type="hidden"
								name="istb" value="1"> <!-- 点餐 -->

								<div id="isearch-sug"></div></li>

							<li class="youhui"><label>筛选</label>
								<div class="sort" id="J_youhui_sort">
									<span class="select-item">有淘宝店</span>
									<ul class="item-list" id="J_youhui_list" data-spm="1000999">
										<li><a href="/list.htm?city=320100">所有商户</a></li>
										<li><a href="/list.htm?city=320100&amp;isdc=1">点点商户</a></li>
										<li><a href="/list.htm?city=320100&amp;yh=1">全部优惠</a></li>
										<li><a href="/list.htm?city=320100&amp;yh=2">有优惠券</a></li>
										<li><a href="/list.htm?city=320100&amp;yh=3">有团购</a></li>
										<li><a href="/list.htm?city=320100&amp;iswm=1">有外卖</a></li>
									</ul>
								</div></li>

							<li class="iprice"><a class="select-item"
								href="/list.htm?city=320100&amp;istb=1&amp;ps=avgprice&amp;sort=desc"
								data-spm="d1001000"><span>人均</span></a>
								<div class="price">
									<div class="clearfix price-inner">
										<div class="price-range" data-spm="1001001">
											<p>
												<input type="text" value="" class="txt" name="pl"
													id="start_price" title="按价格区间筛选 最低价"> <span>-</span>
												<input type="text" value="" class="txt" name="ph"
													id="end_price" title="按价格区间筛选 最高价">
											</p>
											<div class="rang-btn" id="J_RangBtn">
												<span>确定</span>
											</div>
										</div>
										<span class="price-list">
											<div class="btns" id="prices">
												<ul data-spm="1001002">
													<li data='{"lowprice":0,"highprice":20}'><a href="#">20元以内</a></li>
													<li data='{"lowprice":20,"highprice":50}'><a href="#">20-50元</a></li>
													<li data='{"lowprice":50,"highprice":100}'><a href="#">50-100元</a></li>
													<li data='{"lowprice":100,"highprice":300}'><a
														href="#">100-300元</a></li>
													<li data='{"lowprice":300,"highprice":""}'><a href="#">300元以上</a></li>
													<li data='{"lowprice":"","highprice":""}'><a href="#">不限</a></li>
												</ul>
											</div>
										</span>
									</div>
								</div></li>

							<li class="ifliter"><label>排序:</label>
								<div class="sort" id="J_paixu_sort">
									<span class="select-item"><span> 默认 </span></span> <input
										type="hidden" name="ps" id="primarySort" value=""> <input
										type="hidden" name="sort" value="">
									<ul class="item-list" _frmfield="filter" id="J_paixu_list"
										data-spm="1001003">
										<li><a href="/list.htm?city=320100&amp;istb=1" _val="">默认</a></li>
										<li id="filterHot"><a
											href="/list.htm?city=320100&amp;istb=1&amp;ps=hot" _val="hot">热度</a></li>
										<li id="filterDp"><a
											href="/list.htm?city=320100&amp;istb=1&amp;ps=jpm_count"
											_val="jpm_count">评价数</a></li>
										<li id="filterComeback"><a
											href="/list.htm?city=320100&amp;istb=1&amp;ps=comeback"
											_val="comeback">好评率</a></li>
									</ul>
								</div></li>
						</ul>
					</form>
				</div>
			</div>
		</div>







		<div class="col-sub">
			<div class="mod map-wrap">
				<div class="map" id="map-contain">
					<div id="map-bd"></div>
					<div class="ft" data-spm="1000254">
						<a href="http://map.taobao.com/?city=320100#p=store" id="big-map"
							target="_blank">查看大图</a> <input type="checkbox" id="map-follow" />
						<label for="map-follow" id="map-follow-txt">跟随屏幕移动</label>
					</div>
				</div>
			</div>

			<div id="J_Tuan" data-spm="1000246">
				<input type="hidden" id="J_CatId" value="" />
			</div>


			<div class="mod coupon-wrap" data-spm="5026.1000614.1000247">
				<div class="coupon">
					<div class="slider-coupon">
						<div class="tit">热卖优惠券</div>
						<div class="clearfix c-list-wrap">
							<div class="list" id="J_List">
								<ul>
									<li class="active  index-a"><a target="_blank"
										href="http://item.taobao.com/item.htm?id=21463743045&amp;city=320100"
										class="clearfix"> <span class="index">1</span>
											<div class="coupon-img">
												<div class="img">
													<span> <img
														src="http://img02.taobaocdn.com/bao/uploaded/i2/18878025458587134/T1bA8MFflbXXXXXXXX_!!2-item_pic.png_60x60.jpg">
													</span>
												</div>
											</div>
											<div class="info">
												<p class="name">墨明棋妙原创</p>
												<div class="price">
													<span class="g_price g_price-highlight"
														style="font-size: 12px;"> <span
														style="color: #FE7902">&yen;</span> <strong
														style="background: none; font-size: 12px; color: #FE7902; padding: 0px;">380.0</strong>
													</span>
												</div>
												<p class="num">
													月销量：<em>1020</em>件
												</p>
											</div>
									</a></li>
									<li class="active  index-b"><a target="_blank"
										href="http://item.taobao.com/item.htm?id=9117463398&amp;city=320100"
										class="clearfix"> <span class="index">2</span>
											<div class="coupon-img">
												<div class="img">
													<span> <img
														src="http://img03.taobaocdn.com/bao/uploaded/i3/19116037569412379/T1uHFTFlpXXXXXXXXX_!!2-item_pic.png_60x60.jpg">
													</span>
												</div>
											</div>
											<div class="info">
												<p class="name">自动发南京德</p>
												<div class="price">
													<span class="g_price g_price-highlight"
														style="font-size: 12px;"> <span
														style="color: #FE7902">&yen;</span> <strong
														style="background: none; font-size: 12px; color: #FE7902; padding: 0px;">24.99</strong>
													</span> <em>9.99折</em>
												</div>
												<p class="num">
													月销量：<em>343</em>件
												</p>
											</div>
									</a></li>
									
									<li><a target="_blank"
										href="http://item.taobao.com/item.htm?id=19075062323&amp;city=320100"
										class="clearfix"> <span class="index">10</span>
											<div class="info">
												<p class="name">暗荣骑士团 《剑侠情缘</p>
												<div class="price">
													<span class="g_price g_price-highlight"
														style="font-size: 12px;"> <span style="color: #999">&yen;</span>
														<strong
														style="background: none; font-size: 12px; color: #999; padding: 0px;">999.0</strong>
													</span> <span class="num">月销<em>148</em>件
													</span>
												</div>
											</div>
									</a></li>
								</ul>
							</div>
							<!--end list-->
						</div>
						<!--end c-list-wrap-->
						<a target="_blank"
							href="http://list.taobao.com/market/default.htm?cat=50008075%2C50097749%2C50097750&amp;loc=%C4%CF%BE%A9"
							class="list-more">更多优惠&gt;&gt;</a>

					</div>
				</div>
			</div>
			<div class="mod">
				<div id="J_TmsSlider" class="tmsslider" style="">
					<div class="ks-switchable-content">
						<div>
							<a
								href="http://bbs.taobao.com/catalog/thread/15543510-260534380.htm?spm=1.225126.242431.1"
								target="_blank"><img title="关联"
								src="http://img01.taobaocdn.com/tps/i1/T1PFxHXyFbXXXuL43P-230-160.png"
								width="228" height="153"></a>
						</div>
						<div>
							<a
								href="http://map.taobao.com/mapitem/mapsearch/index.htm?spm=1.225126.242431.2"
								target="_blank"><img title="地图"
								src="http://img03.taobaocdn.com/tps/i3/T114xkXB8fXXXjIN3P-230-160.gif"
								width="228" height="153"></a>
						</div>
						<div>
							<a href="http://vip.etao.com/kuai.htm?spm=1.225126.242431.3"
								target="_blank"><img title="常助一淘"
								src="http://img03.taobaocdn.com/tps/i3/T1hTpHXClgXXaSJ43P-230-160.jpg"
								width="228" height="153"></a>
						</div>
					</div>
				</div>
			</div>

			<div class="mod">
				<div class="tel-tms">
					<img
						src="http://img02.taobaocdn.com/tps/i2/T1hOOTXXXrXXXXXXXX-171-35.png"
						width="171" height="35"> <img
						src="http://img01.taobaocdn.com/tps/i1/T1aJSUXfXiXXXXXXXX-179-27.png"
						width="179" height="27">
					<p>服务时间9:00-18:00</p>
				</div>
			</div>

		</div>


	</div>



	<h1>Welcome to bookNow!</h1>
			<h2>
				<a href="/Page/Security/Order/menu.jsp">开始点菜</a>
			</h2>
			<h2>
				<a href="/Page/Security/Table/table.jsp">预定餐位</a>
			</h2>
			<h2>
				<a href="/Page/Common/registerUser.jsp">注册用户</a>
			</h2>
</body>
</html>
