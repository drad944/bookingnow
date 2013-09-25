var checkOrderDetail = {
		
		leave : function (){
			$("#backToCheckoutManagement").unbind('click');
			$("#checkoutOrderButton").unbind('click');
			$("#checkOrderCancelButton").unbind('click');
			
			$('#backToCheckoutManagement').jqxButton('destroy');
		    $('#checkoutOrderButton').jqxButton('destroy');
		    $('#checkOrderCancelButton').jqxButton('destroy');
		},

		visit : function (initParameter){
			this.init(initParameter);
		},
		init : function(initParameter){
			var me = this;
			
			$.post("calculateOrder.action", initParameter,function(order){
				if(order != null && order["id"] != null){
					order = me.formatCheckOrderData(order);
					var invoiceOrderHtml = me.invoiceOrderTable(order);
					$("#detailOrderTable").html(invoiceOrderHtml);
					me.initCheckOrderElements();
					me.initCheckOrderLocale();
					me.addOrderDetailEventListeners();
		        }
			});
		},
		
		formatCheckOrderData:function (matchedOrder) {
			var invoiceData = {};

			if (matchedOrder != null && matchedOrder.id != null) {
				for ( var orderAttr in matchedOrder) {
					if (orderAttr == "food_details" 
						|| orderAttr == "table_details"
							|| orderAttr == "allowance" 
								|| orderAttr == "total_price"
									|| orderAttr == "id") {
						if (orderAttr == "id") {
							var invoiceId = {};
							invoiceId["name"] = "订单号";
							invoiceId["value"] = matchedOrder["id"];
							invoiceData["id"] = invoiceId;
						}
						if (orderAttr == "food_details") {
							var food_details = matchedOrder[orderAttr];
							if (food_details != null && food_details.length > 0) {
								for ( var i = 0; i < food_details.length; i++) {
									var food_detail = food_details[i];
									if (food_detail != null
											&& food_detail["id"] != null) {
										var invoiceFood = {};

										invoiceFood["count"] = food_detail["count"];
										if (food_detail["isFree"] != null
												&& food_detail["isFree"] == false) {
											if (food_detail["food"] != null) {
												var food = food_detail["food"];
												if (food["price"] != null) {
													invoiceFood["name"] = food["name"];
													invoiceFood["price"] = food["price"]
															* food_detail["count"];

												}
											}

										} else {
											if (food_detail["food"] != null) {
												var food = food_detail["food"];
												if (food["price"] != null) {
													invoiceFood["name"] = food["name"];
													invoiceFood["price"] = food["price"] * 0;
												}
											}
										}

										invoiceData["food_" + i] = invoiceFood;
									}

								}
							}

						}

						if (orderAttr == "table_details") {
							var table_details = matchedOrder[orderAttr];
							if (table_details != null && table_details.length > 0) {
								for ( var i = 0; i < table_details.length; i++) {
									var table_detail = table_details[i];
									if (table_detail["table"] != null
											&& table_detail["id"] != null) {
										var invoiceTable = {};
										var table = table_detail["table"];
										if (table["indoorPrice"] != null
												&& table["indoorPrice"] > 0) {
											invoiceTable["name"] = table["address"];
											invoiceTable["count"] = 1;
											invoiceTable["price"] = table["indoorPrice"];

											invoiceData["table_" + i] = invoiceTable;
										}
									}

								}
							}

						}

						if (orderAttr == "allowance" && matchedOrder["allowance"] > 0) {
							var invoiceAllowance = {};
							invoiceAllowance["name"] = "折扣";
							invoiceAllowance["count"] = matchedOrder["allowance"];
							invoiceAllowance["price"] = "";
							invoiceData["allowance"] = invoiceAllowance;
						}

						if (orderAttr == "total_price") {
							var invoiceTotal_price = {};
							invoiceTotal_price["name"] = "总计";
							invoiceTotal_price["count"] = "";
							invoiceTotal_price["price"] = matchedOrder["total_price"];

							invoiceData["total_price"] = invoiceTotal_price;
						}
					}
				}
			}

			return invoiceData;
		},
		
		invoiceOrderTable:function (invoiceData) {
			var orderDivBegin = "<div id='checkOrderTableDiv'>";
			var orderDivEnd = "</div><div id='checkOrderResult'></div>";
			var orderTableBegin="<table width='400' border='1px' cellspacing='0px' style='border-collapse:collapse'>";
			var orderTableEnd="</table>";
			var orderTableRowBegin="<tr>";
			var orderTableRowEnd="</tr>";
			var orderTableColumnBegin="<td>";
			var orderTableColumnEnd="</td>";
			var orderTableFirstRow = "<tr><td width='200'>商品名称</td><td>数量</td><td>价格</td></tr>";
			var orderIdRow = "<tr><td  width='200'>订单编号</td><td id='checkoutOrderIdDetail'>" + invoiceData["id"].value + "</td></tr>";
			var orderTable = orderDivBegin + orderTableBegin;
			var summaryTable = orderTableBegin;
			summaryTable = summaryTable + orderIdRow;
			orderTable = orderTable + orderTableFirstRow;
			if(invoiceData != null) {
				for(var rowAttr in invoiceData) {
					if(rowAttr.startWith("food")) {
						var rowObject = invoiceData[rowAttr];
						if(rowObject != null) {
							var rowTable = orderTableRowBegin;
							for(var columnAttr in rowObject) {
								if(columnAttr == "name") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr];
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							for(var columnAttr in rowObject) {
								if(columnAttr == "count") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr];
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							for(var columnAttr in rowObject) {
								if(columnAttr == "price") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr] + Constants.unit.Chinese;
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							rowTable = rowTable + orderTableRowEnd;
							orderTable = orderTable + rowTable;
						}
						delete invoiceData[rowAttr];
					}
					
				}
				
				for(var rowAttr in invoiceData) {
					if(rowAttr.startWith("table")) {
						var rowObject = invoiceData[rowAttr];
						if(rowObject != null) {
							var rowTable = orderTableRowBegin;
							
							for(var columnAttr in rowObject) {
								if(columnAttr == "name") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr] + "(包间费)";
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							for(var columnAttr in rowObject) {
								if(columnAttr == "price") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr] + Constants.unit.Chinese;
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							rowTable = rowTable + orderTableRowEnd;
							summaryTable = summaryTable + rowTable;
						}
						delete invoiceData[rowAttr];
					}
				}
				
				for(var rowAttr in invoiceData) {
					if(rowAttr.startWith("allowance")) {
						var rowObject = invoiceData[rowAttr];
						if(rowObject != null) {
							var rowTable = orderTableRowBegin;
							
							for(var columnAttr in rowObject) {
								if(columnAttr == "name") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr];
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							for(var columnAttr in rowObject) {
								if(columnAttr == "count") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr];
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							
							rowTable = rowTable + orderTableRowEnd;
							summaryTable = summaryTable + rowTable;
						}
						delete invoiceData[rowAttr];
						break;
					}
				}
				for(var rowAttr in invoiceData) {
					if(rowAttr.startWith("total_price")) {
						var rowObject = invoiceData[rowAttr];
						if(rowObject != null) {
							var rowTable = orderTableRowBegin;
							
							for(var columnAttr in rowObject) {
								if(columnAttr == "name") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr];
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							
							for(var columnAttr in rowObject) {
								if(columnAttr == "price") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr] + Constants.unit.Chinese;
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							rowTable = rowTable + orderTableRowEnd;
							summaryTable = summaryTable + rowTable;
						}
						delete invoiceData[rowAttr];
						break;
					}
				}
				
				
				summaryTable = summaryTable + orderTableEnd;
				
				orderTable = orderTable + orderTableEnd;
				
				orderTable = orderTable + summaryTable;
				orderTable = orderTable + orderDivEnd;
				
			}
			return orderTable;
		},
		
		initCheckOrderLocale:function() {
			$('#backToCheckoutManagement').val(i18n.t("checkoutOrderManagement.button.back"));
			$('#checkoutOrderButton').val(i18n.t("checkoutOrderManagement.button.checkout"));
			$('#checkOrderCancelButton').val(i18n.t("checkoutOrderManagement.button.cancel"));
		},
		
		initCheckOrderElements:function () {
			
			$('#backToCheckoutManagement').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#checkoutOrderButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#checkOrderCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
		},
		

		addOrderDetailEventListeners:function () {
			var me = this;
			$('#backToCheckoutManagement').bind('click', function (event) { 
				
				if(currentPage.leave){
		    		currentPage.leave();
		    	}
				
				currentPage = checkoutManagement;
				openSubPage('framework_main','page/common/checkoutManagement.html','content',checkoutManagement,null);
			});
			$('#checkoutOrderButton').bind('click', function () {
				var orderId = $("#checkoutOrderIdDetail").text();
				if(orderId != null && orderId > 0) {
					$.post("finishedOrder.action", {"order.id": orderId},function(order){
						if(order != null && order["id"] != null && order["status"] == Constants.ORDER_FINISHED){
							//print detail order here.
							printdiv("checkOrderTableDiv");
							
							/*
							if(currentPage.leave){
					    		currentPage.leave();
					    	}
							
							currentPage = checkoutManagement;
							openSubPage('framework_main','page/common/checkoutManagement.html','content',checkoutManagement,null);
							*/
					    	$("#eventLog").text('结账成功,谢谢惠顾,欢迎下次光临!');
				        }
					});
				}
		    });
		    
		    
		    $("#checkOrderCancelButton").bind('click', function (event) {
		    	if(currentPage.leave){
		    		currentPage.leave();
		    	}
				
				currentPage = checkoutManagement;
				openSubPage('framework_main','page/common/checkoutManagement.html','content',checkoutManagement,null);
		    });
		}
};

