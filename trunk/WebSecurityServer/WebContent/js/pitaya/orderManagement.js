
function emptyUpdateOrderWindow(){
	//init registerOrderWindow widget data
	$("#updateOrderPhoneInput").jqxMaskedInput({value: null });
	$('#updateOrderBirthdayInput').jqxDateTimeInput({value:findDateTime("2000-01-01 00:00:00")});
	
	$("#updateOrderSexRadioButton1").jqxRadioButton({checked: true});
    $("#updateOrderSexRadioButton2").jqxRadioButton({checked: false});
    $('#acceptInput').jqxCheckBox({checked: false});

	$("updateOrderSexInput").val("男");
	$("#updateOrderIdInput").val(null);
	$("#updateOrderAccountInput").val(null);
	$("#updateOrderRealNameInput").val(null);
	$("#updateOrderPasswordInput").val(null);
	$("#updateOrderPasswordConfirmInput").val(null);
	$("#updateOrderAddressInput").val(null);
	
	$("#updateOrderDepartmentInput").val(null);
	
	$("#updateOrderEmailInput").val(null);
	$("#updateOrderResult").text("");
}

function initUpdateOrderElements() {
//	var theme = getDemoTheme();
    $("#updateOrderDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#updateOrderUpdateButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateOrderResetButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateOrderCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
    

    
  //  $("#updateOrderAddressInput").jqxMaskedInput({ mask: '省-市-区-街道-门牌号', width: 150, height: 22, theme: theme });
    $("#updateOrderPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 25, theme: theme });
    $('.updateOrderTextInput').jqxInput({ theme: theme });
    
    var d1 = new Date();
    $('#updateOrderBirthdayInput').jqxDateTimeInput({ theme: theme,width: 180, height: 22,formatString: "yyyy/MM/dd HH:mm:ss", value: d1 });
    
	$("#updateOrderSexRadioButton1").jqxRadioButton({ width: 70, height: 25,checked: true, theme: theme });
	$("#updateOrderSexRadioButton2").jqxRadioButton({ width: 70, height: 25,  theme: theme });
	$("updateOrderSexInput").val("男");
    
    
  
    
    var orderDepartmentData = [
          //  { value: 1, label: "USER_DEPARTMENT" },
            { value: 2, label: "USER_DEPARTMENT_BUSSINESS" },
            { value: 3, label: "USER_DEPARTMENT_PRODUCTION" },
            { value: 4, label: "USER_DEPARTMENT_FINANCE" },
            { value: 5, label: "USER_DEPARTMENT_PERSONNEL" },
            { value: 6, label: "USER_DEPARTMENT_DEVERLOPE" },
            { value: 7, label: "USER_DEPARTMENT_MANAGEMENT" }
        ];
    
	// Create a jqxComboBox
	$("#updateOrderDepartmentCombobox").jqxComboBox({ 
		selectedIndex: 0, 
		source: orderDepartmentData, 
		displayMember: "label", 
		valueMember: "value", 
		width: 150, 
		height: 25, 
		theme: theme 
	});
    
    // initialize validator.
    $('#updateOrderInfoForm').jqxValidator({
     rules: [
            { input: '#updateOrderAccountInput', message: 'Ordername is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateOrderAccountInput', message: 'Your ordername must be between 3 and 12 characters!', action: 'keyup, blur', rule: 'length=3,12' },
            { input: '#updateOrderRealNameInput', message: 'Real Name is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateOrderRealNameInput', message: 'Your real name must contain only letters!', action: 'keyup', rule: 'notNumber' },
            { input: '#updateOrderRealNameInput', message: 'Your real name must be between 3 and 12 characters!', action: 'keyup', rule: 'length=3,12' },
            
            { input: '#updateOrderPasswordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateOrderPasswordInput', message: 'Your password must be between 6 and 20 characters!', action: 'keyup, blur', rule: 'length=6,20' },
            { input: '#updateOrderPasswordConfirmInput', message: 'Confirm Password is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateOrderPasswordConfirmInput', message: 'Confirm Passwords doesn\'t match!', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                    if (input.val() === $('#updateOrderPasswordInput').val()) {
                        return true;
                    }
                    return false;
            	}
            },
            { input: '#updateOrderBirthdayInput', message: 'Your birth date must be between 1/1/1900 and ' + ((new Date()).getFullYear() + 1), action: 'valuechanged', rule: function (input, commit) {
                var date = $('#updateOrderBirthdayInput').jqxDateTimeInput('value');
                var result = date.getFullYear() >= 1900 && date.getFullYear() <= (new Date()).getFullYear();
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                return result;
            	}
            },
            { input: '#updateOrderEmailInput', message: 'E-mail is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateOrderEmailInput', message: 'Invalid e-mail!', action: 'keyup', rule: 'email' }
            ], 
            theme: theme
    });
	
};

function formatUpdateOrderElements(rowData) {
    
    var d1 = {};
	if (rowData["birthday"] != null) {
		d1 = findDateTime(rowData["birthday"]);
	}else {
		d1= new Date();
	}
    $('#updateOrderBirthdayInput').jqxDateTimeInput({formatString: "yyyy/MM/dd HH:mm:ss", value: d1 });
    
    if(rowData["sex"] != null && rowData["sex"] == '女') {
    	$("#updateOrderSexRadioButton1").jqxRadioButton({checked: false});
    	$("#updateOrderSexRadioButton2").jqxRadioButton({checked: true});
    }else {
    	$("#updateOrderSexRadioButton1").jqxRadioButton({checked: true});
    	$("#updateOrderSexRadioButton2").jqxRadioButton({checked: false});
    }
    
    
    $("#updateOrderIdInput").val(rowData["id"]);
	$("#updateOrderAccountInput").val(rowData["account"]);
	$("#updateOrderRealNameInput").val(rowData["name"]);
	$("#updateOrderPasswordInput").val(rowData["password"]);
	$("#updateOrderPasswordConfirmInput").val(rowData["password"]);
	$("#updateOrderAddressInput").val(rowData["address"]);
	
	$("#updateOrderDepartmentInput").val(rowData["department"]);
	
	$("#updateOrderEmailInput").val(rowData["email"]);
	$("#updateOrderPhoneInput").val(rowData["phone"]);
	$("#updateOrderSexInput").val(rowData["sex"]);
    
	// Create a jqxComboBox
	$("#updateOrderDepartmentCombobox").jqxComboBox({ selectedIndex: findDepartmentValue($("#updateOrderDepartmentInput").val()) - 2});
}

function addUpdateOrderEventListeners() {
	$('#updateOrderPopupWindow').on('close', function (event) { 
		emptyUpdateOrderWindow();
		$('#updateOrderInfoForm').jqxValidator('hide');
      //  $('#updateOrderPopupWindow').jqxWindow('close');
	});
	$('#updateOrderUpdateButton').on('click', function () {
        $('#updateOrderInfoForm').jqxValidator('validate');
    });
    
    $("#updateOrderSexRadioButton1").on('change', function (event) {
        var checked = event.args.checked;
        if (checked) {
        	 $("#updateOrderSexInput").val("男");
        }
    });
    $("#updateOrderSexRadioButton2").on('change', function (event) {
        var checked = event.args.checked;
        if (checked) {
        	$("#updateOrderSexInput").val("女");
        }
    });
    
    $("#updateOrderCancelButton").on('click', function (event) {
    	$('#updateOrderInfoForm').jqxValidator('hide');
        $('#updateOrderPopupWindow').jqxWindow('close');
    });
        
    $("#updateOrderDepartmentCombobox").on('select', function (event) {
            if (event.args) {
                var item = event.args.item;
                if (item) {
                	$("#updateOrderDepartmentInput").val(item.value);
                }
            }
        });
    
    $('#updateOrderInfoForm').on('validationSuccess', function (event) { 
    	// Some code here. 
    	updateOrder();
    });
}

function initUpdateOrderWindow(rowData,position) {
	$("#updateOrderPopupWindow").removeAttr("style");
	
//	var theme = getDemoTheme();
	
	formatUpdateOrderElements(rowData);
	
	// initialize the popup window and buttons.
    $("#updateOrderPopupWindow").jqxWindow({
    	position:position, isModal: true,width: 350, height: 450, resizable: true, theme: theme, cancelButton: $("#updateOrderCancelButton"), modalOpacity: 0.01,
    	initContent: function () {
            $('#updateOrderPopupWindow').jqxWindow('focus');
        }
    });
    
    $("#updateOrderPopupWindow").jqxWindow('open');
}

function updateOrder() {

	var updateOrderUIData = {
		"order.id" : $("#updateOrderIdInput").val(),
		"order.account" : $("#updateOrderAccountInput").val(),
		"order.name" : $("#updateOrderRealNameInput").val(),
		"order.password" : $("#updateOrderPasswordConfirmInput").val(),
		"order.address" : $("#updateOrderAddressInput").val(),
		"order.birthday" : $('#updateOrderBirthdayInput').jqxDateTimeInput('value'),
		"order.department" : $("#updateOrderDepartmentInput").val(),
		"order.email" : $("#updateOrderEmailInput").val(),
		"order.phone" : $("#updateOrderPhoneInput").val(),
		"order.sex" : $("#updateOrderSexInput").val()
	};
	
	var updateOrderData = parseUIDataToOrderData(updateOrderUIData);

	$.post("updateOrder.action", updateOrderData, function(result) {
		if (result != null && result["id"] != null) {
			
			$("#updateOrderResult").text("update order successfully!");
			$("#updateOrderPopupWindow").jqxWindow('close');
			
			var orderUIResult = parseOrderDataToUIData(result);
			
			//update row in grid
			var selectedrowindex = $("#orderDataGrid").jqxGrid('getselectedrowindex');
            var rowscount = $("#orderDataGrid").jqxGrid('getdatainformation').rowscount;
            if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
            	var id = $("#orderDataGrid").jqxGrid('getrowid', selectedrowindex);
                var commit = $("#orderDataGrid").jqxGrid('updaterow', id, orderUIResult);
                $("#orderDataGrid").jqxGrid('ensurerowvisible', selectedrowindex);
            }
			
			$("#eventLog").text("update order successfully!");

		} else if (result != null && result["executeResult"] != null
				&& result["executeResult"] == false) {
			$("#updateOrderResult").text("update order failed,please check order info!");
		}
	});
}


function initOperateOrderGridElements() {
//	var theme = getDemoTheme();
	$("#addOrderRowButton").jqxButton({ theme: theme });
	$("#deleteOrderRowButton").jqxButton({ theme: theme });
	$("#updateOrderRowButton").jqxButton({ theme: theme });
}	

function addOperateOrderGridEventListeners() {
	// update row.
	$("#updateOrderRowButton").on('click', function () {
		selectedupdaterowindex = $("#orderDataGrid").jqxGrid('getselectedrowindex');
		//id = $("#orderDataGrid").jqxGrid('getrowid', selectedrowindex);
	    rowData = $('#orderDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
	    
		
		if(rowData != null) {
			var offset = $("#orderDataGrid").offset();
			var position = {};
			position.x = parseInt(offset.left) + 200;
			position.y = parseInt(offset.top) - 200;
			
			initUpdateOrderWindow(rowData,position);
		}
	    
	    
	});
    
	// show the popup window
	$("#addOrderRowButton").on('click', function () {
	//	openContentPage('addOrderPopupWindowDiv','page/common/addOrderPopupWindow.html','content');
		
		var offset = $("#orderDataGrid").offset();
		var position = {};
		position.x = parseInt(offset.left) + 200;
		position.y = parseInt(offset.top) - 200;
		
		
		// show the popup window.
		initRegisterOrderWindow(position);
		
			
	});
	
	// delete row.
	$("#deleteOrderRowButton").on('click', function () {
	    var selectedrowindex = $("#orderDataGrid").jqxGrid('getselectedrowindex');
	    var rowscount = $("#orderDataGrid").jqxGrid('getdatainformation').rowscount;
	    if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
	        //var id = $("#orderDataGrid").jqxGrid('getrowid', selectedrowindex);
	        
	        var rowData = $('#orderDataGrid').jqxGrid('getrowdata', selectedrowindex);
	        $.post("removeOrder.action", {"order.id": rowData["id"]},function(result){
				if(result != null && result["executeResult"] != null && result["executeResult"] == true){
					var id = $("#orderDataGrid").jqxGrid('getrowid', selectedrowindex);
                    var commit = $("#orderDataGrid").jqxGrid('deleterow', id);
	            	
	            	if(commit != null) {
	            		
	            	}
	            }
			});
	        
	    }
	});
}


function parseOrderGridHtml() {
		$.post("searchOrder.action", 
			{"order.enabled": true}, 
			function(matchedorders){
	var option = {
			lng: 'zh-CN',
			fallbackLng: 'zh-CN',
	//		fallbackLng: 'en-US',
	//		lng: 'en-US',
			resGetPath: 'resources/locales/__lng__/__ns__.json',
			getAsync: false,
			ns: 'bookingnow.content.orderManagement',
			load:'current'
		};
	i18n.init(option);
				
				
	if(matchedorders != null && matchedorders.result != null){
		orders = matchedorders.result;
	}
	
	var orderSize = orders.length;
	var orderData = {};
	
	var columns = {};
	
	var datafields = {};
	for(var i = 0;i < orderSize; i++) {
		order = parseOrderDataToUIData(orders[i]);
		if(i==0) {
			var j=0;
			
			for(var item in order) {
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
					datafield["type"] = "number";
					column["text"] = i18n.t("field.customer_id");
					column["filtertype"] = 'number';
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
				}else if(item == "customer" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
					//do nothing
				}else {
					datafield["type"] = "string";
					column["text"] = i18n.t("field.xx");
				}
				
				if(item == "customer" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
					
				}else {
					column["datafield"] = item;
					
					if(item == "id"){
						column["width"] = "50";
					}else if(item == "allowance") {
						column["width"] = "50";
					}else if(item == "customer_count") {
						column["width"] = "80";
					}else if(item == "customer_id") {
						column["width"] = "80";
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
					}else if(item == "customer" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
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
		for(var item in order) {
			if(item == "customer" || item == "food_details" ||item == "table_details" ||item == "user" || item == "enabled"){
					
			}else {
				rowData[item] = order[item];
			}
			
		}
		
		orderData[i] = rowData;
	}
	
	
//	var theme = getDemoTheme();
	
	var source =
	{
	    localdata: orderData,
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
	$("#orderDataGrid").jqxGrid(
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
	  //  columnsreorder: true,
	    columns: columns
	});
	
	// display selected row index.
    $("#orderDataGrid").on('rowselect', function (event) {
        $("#eventLog").text("select row index : " + event.args.rowindex);
    });
	
	initOperateOrderGridElements();
	addOperateOrderGridEventListeners();
	
	initUpdateOrderElements();
	addUpdateOrderEventListeners();
	
	// initialize the popup window and buttons.
	initRegisterOrderElements();
	addRegisterOrderEventListeners();
	
	/*
	 $("#orderDataGrid").on('columnreordered', function (event) {
	    var column = event.args.columntext;
	    var newindex = event.args.newindex
	    var oldindex = event.args.oldindex;
	});
	*/
	});
		
}



function parseUIDataToOrderData(record) {
	if(record != null){
		for(var attr in record) {
			if(attr == "order.status") {
				record[attr] = findOrderStatusValue(record[attr]);
			}else if(attr == "order.modifyTime") {
				record[attr] = record[attr].getTime();
			}else if(attr == "order.submit_time") {
				record[attr] = record[attr].getTime();
			}
		}
		return record;
	}
	return null;
}

function parseOrderDataToUIData(order) {
	if(order != null){
		for(var attr in order) {
			if(attr == "status") {
				order[attr] = findOrderStatusLable(order[attr]);
			}else if(attr == "modifyTime") {
				order[attr] = new Date(order[attr]).Format("yyyy-MM-dd HH:mm:ss");
			}else if(attr == "submit_time") {
				order[attr] = new Date(order[attr]).Format("yyyy-MM-dd HH:mm:ss");
			}
		}
		return order;
	}
	return null;
}