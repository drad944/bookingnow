var checkoutManagement = {
		
		leave : function (){
			$("#checkOrderDataGrid").unbind('rowselect');
			$("#backToCheckoutManagement").unbind('click');
			$("#checkoutOrderButton").unbind('click');
			$("#checkOrderCancelButton").unbind('click');
			$("#checkOrderRowButton").unbind('click');
		},

		visit : function (){
			this.init();
		},
		init : function(){
			var me = this;
			me.parseCheckOrderGridHtml();
		},
		emptyCheckOrderWindow:function (){
			$("#checkOrderDiv").val(null);
			$("#checkOrderDiv").html(null);
			$("#checkOrderResult").text("");
		},

		initCheckOrderElements:function () {
			$('#backToCheckoutManagement').jqxButton({ width: 60, height: 25, theme: theme });
			
		    $('#checkoutOrderButton').jqxButton({ width: 60, height: 25, theme: theme });
		    $('#checkOrderCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
		},

		addOrderDetailEventListeners:function () {
			var me = this;
			$('#backToCheckoutManagement').bind('click', function (event) { 
				openContentPage('framework_main','page/common/checkoutManagement.html','content');
		    	me.parseCheckOrderGridHtml();
			});
			$('#checkoutOrderButton').bind('click', function () {
				var orderId = $("#checkoutOrderIdDetail").text();
				if(orderId != null && orderId > 0) {
					$.post("finishedOrder.action", {"order.id": orderId},function(order){
						if(order != null && order["id"] != null && order["status"] == Constants.ORDER_FINISHED){
							openContentPage('framework_main','page/common/checkoutManagement.html','content');
					    	me.parseCheckOrderGridHtml();
					    	$("#eventLog").text('结账成功,谢谢惠顾,欢迎下次光临!');
				        }
					});
				}
		    });
		    
		    
		    $("#checkOrderCancelButton").bind('click', function (event) {
		    	openContentPage('framework_main','page/common/checkoutManagement.html','content');
		    	me.parseCheckOrderGridHtml();
		    });
		},

		findDetailOrderTable:function (rowData) {
			var me = this;
			openContentPage('framework_main','page/common/orderDetail.html','content');
			
//			var theme = getDemoTheme();
			$.post("calculateOrder.action", {"order.id": rowData["id"],"order.allowance":rowData["allowance"]},function(order){
				if(order != null && order["id"] != null){
					order = me.formatCheckOrderData(order);
					var invoiceOrderHtml = me.invoiceOrderTable(order);
					$("#detailOrderTable").html(invoiceOrderHtml);
					me.initCheckOrderElements();
					me.addOrderDetailEventListeners();
		        }
			});
			
		},

		initOperateCheckOrderGridElements:function () {
//			var theme = getDemoTheme();
			$("#checkOrderRowButton").jqxButton({ theme: theme });
		},	

		addOperateCheckOrderGridEventListeners:function () {
			var me = this;
			
			notification.subscribeTopic("order", function(event){
				alert(event);
				//add new order in data grid here,then highlight it
				
			});
			
			$("#checkOrderDataGrid").bind('rowselect', function (event) {
		        $("#eventLog").text("select row index : " + event.args.rowindex);
		    });
			// update row.
			$("#checkOrderRowButton").bind('click', function () {
				
				selectedupdaterowindex = $("#checkOrderDataGrid").jqxGrid('getselectedrowindex');
				if(selectedupdaterowindex != -1) {
					
					rowData = $('#checkOrderDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
					
					if(rowData != null) {
						var offset = $("#checkOrderDataGrid").offset();
						var position = {};
						position.x = parseInt(offset.left) + 200;
						position.y = parseInt(offset.top) - 150;
						checkOrderAllowance.init();
						checkOrderAllowance.initCheckoutOrderAllowanceWindow(rowData,position);
					}
				}else {
					var option = {
							fallbackLng: 'en-US',
							lng: 'en-US',
					//		lng: 'zh-CN',
							resGetPath: 'resources/locales/__lng__/__ns__.json',
							getAsync: false,
							ns: 'bookingnow.content.tableManagement'
						};
					 
					i18n.init(option);
					$("#eventLog").text(i18n.t("message.requireSelectOneRow"));
				}
				
				
			});
		    
		},

		parseUIDataToCheckOrderData:function (record) {
			if(record != null){
				for(var attr in record) {
					if(attr == "checkOrder.status") {
						record[attr] = findCheckOrderStatusValue(record[attr]);
					}else if(attr == "checkOrder.modifyTime") {
						record[attr] = record[attr].getTime();
					}else if(attr == "checkOrder.submit_time") {
						record[attr] = record[attr].getTime();
					}
				}
				return record;
			}
			return null;
		},

		parseCheckOrderDataToUIData:function (checkOrder) {
			if(checkOrder != null){
				for(var attr in checkOrder) {
					if(attr == "status") {
						checkOrder[attr] = AppUtil.findOrderStatusLable(checkOrder[attr]);
					}else if(attr == "modifyTime") {
						checkOrder[attr] = new Date(checkOrder[attr]).Format("yyyy-MM-dd HH:mm:ss");
					}else if(attr == "submit_time") {
						checkOrder[attr] = new Date(checkOrder[attr]).Format("yyyy-MM-dd HH:mm:ss");
					}else if(attr == "table_details") {
						
						var tableDetails = checkOrder[attr];
						var tables = "";
						for(var i=0;i< tableDetails.length;i++) {
							var tableDetail = tableDetails[i];
							for(var item in tableDetail) {
								if(item == "table") {
									var table = tableDetail[item];
									if(table != null && table["address"] != null){
										tables = tables + table["address"] + ",";
									}
								}
							}
						}
						if(tables.length > 0) {
							tables = tables.substring(0, tables.length - 1);
						}
						
						checkOrder["tables"] = tables;
						checkOrder[attr] = undefined;
					}else if(attr == "food_details") {
						
						var foodDetails = checkOrder[attr];
						var foods = "";
						for(var i=0;i< foodDetails.length;i++) {
							var foodDetail = foodDetails[i];
							for(var item in foodDetail) {
								if(item == "food") {
									var food = foodDetail[item];
									if(food != null && food["name"] != null){
										foods = foods + food["name"] + ",";
									}
								}
							}
						}
						if(foods.length > 0) {
							foods = foods.substring(0, foods.length - 1);
						}
						
						checkOrder["foods"] = foods;
						checkOrder[attr] = undefined;
					}else if(attr == "customer") {
						var customer = checkOrder[attr];
						if(customer != null && customer["name"] != null) {
							checkOrder[attr] = customer["name"];
						}else {
							checkOrder[attr] = null;
						}
						
					}
				}
				return checkOrder;
			}
			return null;
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
			var orderDivBegin = "<div>";
			var orderDivEnd = "</div><div id='checkOrderResult'></div>";
			var orderTableBegin="<table border='1px' cellspacing='0px' style='border-collapse:collapse'>";
			var orderTableEnd="</table>";
			var orderTableRowBegin="<tr>";
			var orderTableRowEnd="</tr>";
			var orderTableColumnBegin="<td>";
			var orderTableColumnEnd="</td>";
			var orderTableFirstRow = "<tr><td>商品名称</td><td>数量</td><td>价格</td></tr>";
			var orderIdRow = "<tr><td>订单编号</td><td id='checkoutOrderIdDetail'>" + invoiceData["id"].value + "</td><td></td></tr>";
			var orderTableButton ='<tr style="text-align: center;"><td><input id="checkoutOrderButton" type="button" value="checkout" /></td><td><input id="checkOrderCancelButton" type="button" value="Cancel" /></td></tr>';
			var orderTable = orderDivBegin + orderTableBegin;
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
									columnTable = columnTable + rowObject[columnAttr];
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
									columnTable = columnTable + rowObject[columnAttr];
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
							for(var columnAttr in rowObject) {
								if(columnAttr == "price") {
									var columnTable = orderTableColumnBegin;
									columnTable = columnTable + rowObject[columnAttr];
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							
							rowTable = rowTable + orderTableRowEnd;
							orderTable = orderTable + rowTable;
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
									columnTable = columnTable + rowObject[columnAttr];
									columnTable = columnTable + orderTableColumnEnd;
									rowTable = rowTable + columnTable;
									break;
								}
							}
							rowTable = rowTable + orderTableRowEnd;
							orderTable = orderTable + rowTable;
						}
						delete invoiceData[rowAttr];
						break;
					}
				}
				
				orderTable = orderTable + orderIdRow;
				orderTable = orderTable + orderTableButton;
				orderTable = orderTable + orderTableEnd;
				orderTable = orderTable + orderDivEnd;
				
			}
			return orderTable;
		},
		parseCheckOrderGridHtml:function () {
			var me = this;
			$.post("searchOrder.action", 
				{"order.enabled": true,"order.status":5}, 
				function(matchedcheckOrders){
		var option = {
				lng: 'zh-CN',
				fallbackLng: 'zh-CN',
		//		fallbackLng: 'en-US',
		//		lng: 'en-US',
				resGetPath: 'resources/locales/__lng__/__ns__.json',
				getAsync: false,
				ns: 'bookingnow.content.checkOrderManagement',
				load:'current'
			};
		i18n.init(option);
					
					
		if(matchedcheckOrders != null && matchedcheckOrders.result != null){
			checkOrders = matchedcheckOrders.result;
		}
		
		var checkOrderSize = checkOrders.length;
		var checkOrderData = {};
		
		var columns = {};
		
		var datafields = {};
		for(var i = 0;i < checkOrderSize; i++) {
			checkOrder = me.parseCheckOrderDataToUIData(checkOrders[i]);
			if(i==0) {
				var j=0;
				
				for(var item in checkOrder) {
					var datafield = {};
					var column = {};
					
					if(item == "id"){
						/*
						datafield["type"] = "number";
						column["text"] = i18n.t("field.id");
						column["filtertype"] = 'number';
						*/
					}else if(item == "allowance") {
						datafield["type"] = "number";
						column["text"] = i18n.t("field.allowance");
						column["filtertype"] = 'number';
					}else if(item == "customer_count") {
						datafield["type"] = "number";
						column["text"] = i18n.t("field.customer_count");
						column["filtertype"] = 'number';
					}else if(item == "customer_id") {
						
					}else if(item == "modifyTime") {
						datafield["type"] = "number";
						column["text"] = i18n.t("field.modifyTime");
						
					}else if(item == "prePay") {
						/*
						datafield["type"] = "number";
						column["text"] = i18n.t("field.prePay");
						column["filtertype"] = 'number';
						*/
					}else if(item == "status") {
						datafield["type"] = "string";
						column["text"] = i18n.t("field.status");
						column["filtertype"] = 'textbox';
					}else if(item == "submit_time") {
						/*
						datafield["type"] = "number";
						column["text"] = i18n.t("field.submit_time");
						*/
					}else if(item == "total_price") {
						datafield["type"] = "number";
						column["text"] = i18n.t("field.total_price");
						column["filtertype"] = 'number';
					}else if(item == "user_id") {
						/*
						datafield["type"] = "number";
						column["text"] = i18n.t("field.user_id");
						column["filtertype"] = 'number';
						*/
					}else if(item == "foods") {
						datafield["type"] = "string";
						column["text"] = i18n.t("field.foods");
						column["filtertype"] = 'textbox';
					}else if(item == "tables") {
						datafield["type"] = "string";
						column["text"] = i18n.t("field.tables");
						column["filtertype"] = 'textbox';
					}else if(item == "customer") {
						datafield["type"] = "string";
						column["text"] = i18n.t("field.customer");
						column["filtertype"] = 'textbox';
					}else if( item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
						//do nothing
					}else {
						datafield["type"] = "string";
						column["text"] = i18n.t("field.xx");
					}
					
					if(item == "customer_id" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
						
					}else if(item == "id" || item == "submit_time" || item == "user_id" || item == "prePay"){
						
					}else {
						column["datafield"] = item;
						
						if(item == "allowance") {
							column["width"] = "80";
						}else if(item == "customer_count") {
							column["width"] = "80";
						}else if(item == "modifyTime") {
							column["width"] = "130";
						}else if(item == "status") {
							column["width"] = "60";
						}else if(item == "total_price") {
							column["width"] = "80";
						}else if(item == "foods") {
							column["width"] = "190";
						}else if(item == "tables") {
							column["width"] = "100";
						}else if(item == "customer") {
							column["width"] = "80";
						}else {
							column["width"] = "50";
						}
						
						columns[j] = column;
						
						datafields[j] = datafield;
						j++;
					}
				}
			}
			
			var rowData = {};
			for(var item in checkOrder) {
				if(item == "customer_id" || item == "food_details" || item == "table_details" || item == "user" || item == "enabled"){
						
				}else {
					rowData[item] = checkOrder[item];
				}
				
			}
			
			checkOrderData[i] = rowData;
		}
		
		
//		var theme = getDemoTheme();
		
		var source =
		{
		    localdata: checkOrderData,
		    datatype: "local",
		    datafields:datafields,
		    addrow: function (rowid, rowdata, position, commit) {
		        // synchronize with the server - send insert command
		        // call commit with parameter true if the synchronization with the server is successful 
		        //and with parameter false if the synchronization failed.
		        // you can pass additional argument to the commit callback which represents the new ID if it is generated from a DB.
		        commit(true);
		    },
		    deleterow: function (rowid, commit) {
		        // synchronize with the server - send delete command
		        // call commit with parameter true if the synchronization with the server is successful 
		        //and with parameter false if the synchronization failed.
		        commit(true);
		    },
		    updaterow: function (rowid, newdata, commit) {
		        // synchronize with the server - send update command
		        // call commit with parameter true if the synchronization with the server is successful 
		        // and with parameter false if the synchronization failed.
		        commit(true);
		    }
		};
		var dataAdapter = new $.jqx.dataAdapter(source);
		// initialize jqxGrid
		$("#checkOrderDataGrid").jqxGrid(
		{
		    width: 800,
		    height: 350,
		    source: dataAdapter,
		    theme: theme,
		    selectionmode: 'multiplerowsextended',
		    sortable: true,
		    showfilterrow: true,
	        filterable: true,
		    pageable: true,
		    autoheight: true,
		    selectionmode:'singlerow',
		    columnsresize: true,
		  //  columnsrecheckOrder: true,
		    columns: columns
		});
		
		me.initOperateCheckOrderGridElements();
		me.addOperateCheckOrderGridEventListeners();
		
		});
			
	}
};

