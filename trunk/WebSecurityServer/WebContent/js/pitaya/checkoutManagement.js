
function emptyCheckOrderWindow(){
	$("#checkOrderDiv").val(null);
	$("#checkOrderDiv").html(null);
	$("#checkOrderResult").text("");
}

function initCheckOrderElements() {
//	var theme = getDemoTheme();
    $("#checkOrderDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#checkoutOrderButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#checkOrderCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
};


function addCheckOrderEventListeners() {
	$('#checkOrderPopupWindow').on('close', function (event) { 
		emptyCheckOrderWindow();
		//$('#updateCheckOrderInfoForm').jqxValidator('hide');
      //  $('#updateCheckOrderPopupWindow').jqxWindow('close');
	});
	$('#checkoutOrderButton').on('click', function () {
       // $('#updateCheckOrderInfoForm').jqxValidator('validate');
    });
    
	$('#checkOrderAllowance').on('onchange', function () {
	       // $('#updateCheckOrderInfoForm').jqxValidator('validate');
	    });
	
    
    $("#checkOrderCancelButton").on('click', function (event) {
    	//$('#updateCheckOrderInfoForm').jqxValidator('hide');
        $('#checkOrderPopupWindow').jqxWindow('close');
    });
        
}

function initCheckOrderWindow(rowData,position) {
	$("#checkOrderPopupWindow").removeAttr("style");
	
//	var theme = getDemoTheme();
	$.post("searchFullOrder.action", {"order.id": rowData["id"]},function(order){
		if(order != null && order["id"] != null){
			order = formatCheckOrderData(order);
			var invoiceOrderHtml = invoiceOrderTable(order);
			$("#checkOrderDiv").html(invoiceOrderHtml);
			initCheckOrderElements();
			// initialize the popup window and buttons.
		    $("#checkOrderPopupWindow").jqxWindow({
		    	position:position, isModal: true,width: 350, height: 300, resizable: true, theme: theme, cancelButton: $("#checkOrderCancelButton"), modalOpacity: 0.01,
		    	initContent: function () {
		            $('#checkOrderPopupWindow').jqxWindow('focus');
		        }
		    });
		    
		    $("#checkOrderPopupWindow").jqxWindow('open');
        }
	});
	
}

function updateCheckOrder() {

	var updateCheckOrderUIData = {
		"checkOrder.id" : $("#updateCheckOrderIdInput").val(),
		"checkOrder.account" : $("#updateCheckOrderAccountInput").val(),
		"checkOrder.name" : $("#updateCheckOrderRealNameInput").val(),
		"checkOrder.password" : $("#updateCheckOrderPasswordConfirmInput").val(),
		"checkOrder.address" : $("#updateCheckOrderAddressInput").val(),
		"checkOrder.birthday" : $('#updateCheckOrderBirthdayInput').jqxDateTimeInput('value'),
		"checkOrder.department" : $("#updateCheckOrderDepartmentInput").val(),
		"checkOrder.email" : $("#updateCheckOrderEmailInput").val(),
		"checkOrder.phone" : $("#updateCheckOrderPhoneInput").val(),
		"checkOrder.sex" : $("#updateCheckOrderSexInput").val()
	};
	
	var updateCheckOrderData = parseUIDataToCheckOrderData(updateCheckOrderUIData);

	$.post("updateCheckOrder.action", updateCheckOrderData, function(result) {
		if (result != null && result["id"] != null) {
			
			$("#updateCheckOrderResult").text("update checkOrder successfully!");
			$("#updateCheckOrderPopupWindow").jqxWindow('close');
			
			var checkOrderUIResult = parseCheckOrderDataToUIData(result);
			
			//update row in grid
			var selectedrowindex = $("#checkOrderDataGrid").jqxGrid('getselectedrowindex');
            var rowscount = $("#checkOrderDataGrid").jqxGrid('getdatainformation').rowscount;
            if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
            	var id = $("#checkOrderDataGrid").jqxGrid('getrowid', selectedrowindex);
                var commit = $("#checkOrderDataGrid").jqxGrid('updaterow', id, checkOrderUIResult);
                $("#checkOrderDataGrid").jqxGrid('ensurerowvisible', selectedrowindex);
            }
			
			$("#eventLog").text("update checkOrder successfully!");

		} else if (result != null && result["executeResult"] != null
				&& result["executeResult"] == false) {
			$("#updateCheckOrderResult").text("update checkOrder failed,please check checkOrder info!");
		}
	});
}


function initOperateCheckOrderGridElements() {
//	var theme = getDemoTheme();
	$("#deleteCheckOrderRowButton").jqxButton({ theme: theme });
	$("#checkOrderRowButton").jqxButton({ theme: theme });
}	

function addOperateCheckOrderGridEventListeners() {
	// update row.
	$("#checkOrderRowButton").on('click', function () {
		selectedupdaterowindex = $("#checkOrderDataGrid").jqxGrid('getselectedrowindex');
		//id = $("#checkOrderDataGrid").jqxGrid('getrowid', selectedrowindex);
	    rowData = $('#checkOrderDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
	    
		
		if(rowData != null) {
			var offset = $("#checkOrderDataGrid").offset();
			var position = {};
			position.x = parseInt(offset.left) + 200;
			position.y = parseInt(offset.top) - 100;
			
			initCheckOrderWindow(rowData,position);
		}
	});
    
}


function parseCheckOrderGridHtml() {
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
		checkOrder = parseCheckOrderDataToUIData(checkOrders[i]);
		if(i==0) {
			var j=0;
			
			for(var item in checkOrder) {
				var datafield = {};
				var column = {};
				
				if(item == "id"){
					datafield["type"] = "number";
					column["text"] = i18n.t("field.id");
					column["filtertype"] = 'number';
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
					datafield["type"] = "number";
					column["text"] = i18n.t("field.prePay");
					column["filtertype"] = 'number';
				}else if(item == "status") {
					datafield["type"] = "string";
					column["text"] = i18n.t("field.status");
					column["filtertype"] = 'textbox';
				}else if(item == "submit_time") {
					datafield["type"] = "number";
					column["text"] = i18n.t("field.submit_time");
					
				}else if(item == "total_price") {
					datafield["type"] = "number";
					column["text"] = i18n.t("field.total_price");
					column["filtertype"] = 'number';
				}else if(item == "user_id") {
					datafield["type"] = "number";
					column["text"] = i18n.t("field.user_id");
					column["filtertype"] = 'number';
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
					
				}else {
					column["datafield"] = item;
					
					if(item == "id"){
						column["width"] = "50";
					}else if(item == "allowance") {
						column["width"] = "50";
					}else if(item == "customer_count") {
						column["width"] = "80";
					}else if(item == "customer_id") {
						
					}else if(item == "modifyTime") {
						column["width"] = "130";
					}else if(item == "prePay") {
						column["width"] = "50";
					}else if(item == "status") {
						column["width"] = "80";
					}else if(item == "submit_time") {
						column["width"] = "130";
					}else if(item == "total_price") {
						column["width"] = "100";
					}else if(item == "user_id") {
						column["width"] = "50";
					}else if(item == "foods") {
						column["width"] = "150";
					}else if(item == "tables") {
						column["width"] = "150";
					}else if(item == "customer") {
						column["width"] = "50";
					}else if( item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
						//do nothing
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
	
	
//	var theme = getDemoTheme();
	
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
	
	// display selected row index.
    $("#checkOrderDataGrid").on('rowselect', function (event) {
        $("#eventLog").text("select row index : " + event.args.rowindex);
    });
	
	initOperateCheckOrderGridElements();
	addOperateCheckOrderGridEventListeners();
	
	
	addCheckOrderEventListeners();
	
	});
		
}



function parseUIDataToCheckOrderData(record) {
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
}

function parseCheckOrderDataToUIData(checkOrder) {
	if(checkOrder != null){
		for(var attr in checkOrder) {
			if(attr == "status") {
				checkOrder[attr] = findOrderStatusLable(checkOrder[attr]);
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
}

function formatCheckOrderData(matchedOrder) {
	var invoiceData = {};

	if (matchedOrder != null && matchedOrder.id != null) {
		for ( var orderAttr in matchedOrder) {
			if (orderAttr == "food_details" || orderAttr == "table_details"
					|| orderAttr == "allowance" || orderAttr == "total_price") {
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
}

function invoiceOrderTable(invoiceData) {
	var orderDivBegin = "<div>";
	var orderDivEnd = "</div><div id='checkOrderResult'></div>";
	var orderTableBegin="<table>";
	var orderTableEnd="</table>";
	var orderTableRowBegin="<tr>";
	var orderTableRowEnd="</tr>";
	var orderTableColumnBegin="<td>";
	var orderTableColumnEnd="</td>";
	var orderTableFirstRow = "<tr><td>商品名称</td><td>数量</td><td>价格</td></tr>";
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
							columnTable = columnTable + '<input id="checkOrderAllowance" type="text" value="' + rowObject[columnAttr] +'" />';
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
		orderTable = orderTable + orderTableButton;
		orderTable = orderTable + orderTableEnd;
		orderTable = orderTable + orderDivEnd;
		
	}
	return orderTable;
}