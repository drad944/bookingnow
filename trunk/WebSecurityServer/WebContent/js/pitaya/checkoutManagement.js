
function emptyUpdateCheckOrderWindow(){
	//init registerCheckOrderWindow widget data
	$("#updateCheckOrderPhoneInput").jqxMaskedInput({value: null });
	$('#updateCheckOrderBirthdayInput').jqxDateTimeInput({value:findDateTime("2000-01-01 00:00:00")});
	
	$("#updateCheckOrderSexRadioButton1").jqxRadioButton({checked: true});
    $("#updateCheckOrderSexRadioButton2").jqxRadioButton({checked: false});
    $('#acceptInput').jqxCheckBox({checked: false});

	$("updateCheckOrderSexInput").val("男");
	$("#updateCheckOrderIdInput").val(null);
	$("#updateCheckOrderAccountInput").val(null);
	$("#updateCheckOrderRealNameInput").val(null);
	$("#updateCheckOrderPasswordInput").val(null);
	$("#updateCheckOrderPasswordConfirmInput").val(null);
	$("#updateCheckOrderAddressInput").val(null);
	
	$("#updateCheckOrderDepartmentInput").val(null);
	
	$("#updateCheckOrderEmailInput").val(null);
	$("#updateCheckOrderResult").text("");
}

function initUpdateCheckOrderElements() {
//	var theme = getDemoTheme();
    $("#updateCheckOrderDiv").jqxExpander({ toggleMode: 'none', width: '300px', showArrow: false, theme: theme });
    $('#updateCheckOrderUpdateButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateCheckOrderResetButton').jqxButton({ width: 60, height: 25, theme: theme });
    $('#updateCheckOrderCancelButton').jqxButton({ width: 60, height: 25, theme: theme });
    

    
  //  $("#updateCheckOrderAddressInput").jqxMaskedInput({ mask: '省-市-区-街道-门牌号', width: 150, height: 22, theme: theme });
    $("#updateCheckOrderPhoneInput").jqxMaskedInput({ mask: '### #### ####', width: 150, height: 25, theme: theme });
    $('.updateCheckOrderTextInput').jqxInput({ theme: theme });
    
    var d1 = new Date();
    $('#updateCheckOrderBirthdayInput').jqxDateTimeInput({ theme: theme,width: 180, height: 22,formatString: "yyyy/MM/dd HH:mm:ss", value: d1 });
    
	$("#updateCheckOrderSexRadioButton1").jqxRadioButton({ width: 70, height: 25,checked: true, theme: theme });
	$("#updateCheckOrderSexRadioButton2").jqxRadioButton({ width: 70, height: 25,  theme: theme });
	$("updateCheckOrderSexInput").val("男");
    
    
  
    
    var checkOrderDepartmentData = [
          //  { value: 1, label: "USER_DEPARTMENT" },
            { value: 2, label: "USER_DEPARTMENT_BUSSINESS" },
            { value: 3, label: "USER_DEPARTMENT_PRODUCTION" },
            { value: 4, label: "USER_DEPARTMENT_FINANCE" },
            { value: 5, label: "USER_DEPARTMENT_PERSONNEL" },
            { value: 6, label: "USER_DEPARTMENT_DEVERLOPE" },
            { value: 7, label: "USER_DEPARTMENT_MANAGEMENT" }
        ];
    
	// Create a jqxComboBox
	$("#updateCheckOrderDepartmentCombobox").jqxComboBox({ 
		selectedIndex: 0, 
		source: checkOrderDepartmentData, 
		displayMember: "label", 
		valueMember: "value", 
		width: 150, 
		height: 25, 
		theme: theme 
	});
    
    // initialize validator.
    $('#updateCheckOrderInfoForm').jqxValidator({
     rules: [
            { input: '#updateCheckOrderAccountInput', message: 'CheckOrdername is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateCheckOrderAccountInput', message: 'Your checkOrdername must be between 3 and 12 characters!', action: 'keyup, blur', rule: 'length=3,12' },
            { input: '#updateCheckOrderRealNameInput', message: 'Real Name is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateCheckOrderRealNameInput', message: 'Your real name must contain only letters!', action: 'keyup', rule: 'notNumber' },
            { input: '#updateCheckOrderRealNameInput', message: 'Your real name must be between 3 and 12 characters!', action: 'keyup', rule: 'length=3,12' },
            
            { input: '#updateCheckOrderPasswordInput', message: 'Password is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateCheckOrderPasswordInput', message: 'Your password must be between 6 and 20 characters!', action: 'keyup, blur', rule: 'length=6,20' },
            { input: '#updateCheckOrderPasswordConfirmInput', message: 'Confirm Password is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateCheckOrderPasswordConfirmInput', message: 'Confirm Passwords doesn\'t match!', action: 'keyup, focus', rule: function (input, commit) {
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                    if (input.val() === $('#updateCheckOrderPasswordInput').val()) {
                        return true;
                    }
                    return false;
            	}
            },
            { input: '#updateCheckOrderBirthdayInput', message: 'Your birth date must be between 1/1/1900 and ' + ((new Date()).getFullYear() + 1), action: 'valuechanged', rule: function (input, commit) {
                var date = $('#updateCheckOrderBirthdayInput').jqxDateTimeInput('value');
                var result = date.getFullYear() >= 1900 && date.getFullYear() <= (new Date()).getFullYear();
                // call commit with false, when you are doing server validation and you want to display a validation error on this field. 
                return result;
            	}
            },
            { input: '#updateCheckOrderEmailInput', message: 'E-mail is required!', action: 'keyup, blur', rule: 'required' },
            { input: '#updateCheckOrderEmailInput', message: 'Invalid e-mail!', action: 'keyup', rule: 'email' }
            ], 
            theme: theme
    });
	
};

function formatUpdateCheckOrderElements(rowData) {
    
    var d1 = {};
	if (rowData["birthday"] != null) {
		d1 = findDateTime(rowData["birthday"]);
	}else {
		d1= new Date();
	}
    $('#updateCheckOrderBirthdayInput').jqxDateTimeInput({formatString: "yyyy/MM/dd HH:mm:ss", value: d1 });
    
    if(rowData["sex"] != null && rowData["sex"] == '女') {
    	$("#updateCheckOrderSexRadioButton1").jqxRadioButton({checked: false});
    	$("#updateCheckOrderSexRadioButton2").jqxRadioButton({checked: true});
    }else {
    	$("#updateCheckOrderSexRadioButton1").jqxRadioButton({checked: true});
    	$("#updateCheckOrderSexRadioButton2").jqxRadioButton({checked: false});
    }
    
    
    $("#updateCheckOrderIdInput").val(rowData["id"]);
	$("#updateCheckOrderAccountInput").val(rowData["account"]);
	$("#updateCheckOrderRealNameInput").val(rowData["name"]);
	$("#updateCheckOrderPasswordInput").val(rowData["password"]);
	$("#updateCheckOrderPasswordConfirmInput").val(rowData["password"]);
	$("#updateCheckOrderAddressInput").val(rowData["address"]);
	
	$("#updateCheckOrderDepartmentInput").val(rowData["department"]);
	
	$("#updateCheckOrderEmailInput").val(rowData["email"]);
	$("#updateCheckOrderPhoneInput").val(rowData["phone"]);
	$("#updateCheckOrderSexInput").val(rowData["sex"]);
    
	// Create a jqxComboBox
	$("#updateCheckOrderDepartmentCombobox").jqxComboBox({ selectedIndex: findDepartmentValue($("#updateCheckOrderDepartmentInput").val()) - 2});
}

function addUpdateCheckOrderEventListeners() {
	$('#updateCheckOrderPopupWindow').on('close', function (event) { 
		emptyUpdateCheckOrderWindow();
		$('#updateCheckOrderInfoForm').jqxValidator('hide');
      //  $('#updateCheckOrderPopupWindow').jqxWindow('close');
	});
	$('#updateCheckOrderUpdateButton').on('click', function () {
        $('#updateCheckOrderInfoForm').jqxValidator('validate');
    });
    
    $("#updateCheckOrderSexRadioButton1").on('change', function (event) {
        var checked = event.args.checked;
        if (checked) {
        	 $("#updateCheckOrderSexInput").val("男");
        }
    });
    $("#updateCheckOrderSexRadioButton2").on('change', function (event) {
        var checked = event.args.checked;
        if (checked) {
        	$("#updateCheckOrderSexInput").val("女");
        }
    });
    
    $("#updateCheckOrderCancelButton").on('click', function (event) {
    	$('#updateCheckOrderInfoForm').jqxValidator('hide');
        $('#updateCheckOrderPopupWindow').jqxWindow('close');
    });
        
    $("#updateCheckOrderDepartmentCombobox").on('select', function (event) {
            if (event.args) {
                var item = event.args.item;
                if (item) {
                	$("#updateCheckOrderDepartmentInput").val(item.value);
                }
            }
        });
    
    $('#updateCheckOrderInfoForm').on('validationSuccess', function (event) { 
    	// Some code here. 
    	updateCheckOrder();
    });
}

function initUpdateCheckOrderWindow(rowData,position) {
	$("#updateCheckOrderPopupWindow").removeAttr("style");
	
//	var theme = getDemoTheme();
	
	formatUpdateCheckOrderElements(rowData);
	
	// initialize the popup window and buttons.
    $("#updateCheckOrderPopupWindow").jqxWindow({
    	position:position, isModal: true,width: 350, height: 450, resizable: true, theme: theme, cancelButton: $("#updateCheckOrderCancelButton"), modalOpacity: 0.01,
    	initContent: function () {
            $('#updateCheckOrderPopupWindow').jqxWindow('focus');
        }
    });
    
    $("#updateCheckOrderPopupWindow").jqxWindow('open');
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
	$("#addCheckOrderRowButton").jqxButton({ theme: theme });
	$("#deleteCheckOrderRowButton").jqxButton({ theme: theme });
	$("#updateCheckOrderRowButton").jqxButton({ theme: theme });
}	

function addOperateCheckOrderGridEventListeners() {
	// update row.
	$("#updateCheckOrderRowButton").on('click', function () {
		selectedupdaterowindex = $("#checkOrderDataGrid").jqxGrid('getselectedrowindex');
		//id = $("#checkOrderDataGrid").jqxGrid('getrowid', selectedrowindex);
	    rowData = $('#checkOrderDataGrid').jqxGrid('getrowdata', selectedupdaterowindex);
	    
		
		if(rowData != null) {
			var offset = $("#checkOrderDataGrid").offset();
			var position = {};
			position.x = parseInt(offset.left) + 200;
			position.y = parseInt(offset.top) - 200;
			
			initUpdateCheckOrderWindow(rowData,position);
		}
	    
	    
	});
    
	// show the popup window
	$("#addCheckOrderRowButton").on('click', function () {
	//	openContentPage('addCheckOrderPopupWindowDiv','page/common/addCheckOrderPopupWindow.html','content');
		
		var offset = $("#checkOrderDataGrid").offset();
		var position = {};
		position.x = parseInt(offset.left) + 200;
		position.y = parseInt(offset.top) - 200;
		
		
		// show the popup window.
		initRegisterCheckOrderWindow(position);
		
			
	});
	
	// delete row.
	$("#deleteCheckOrderRowButton").on('click', function () {
	    var selectedrowindex = $("#checkOrderDataGrid").jqxGrid('getselectedrowindex');
	    var rowscount = $("#checkOrderDataGrid").jqxGrid('getdatainformation').rowscount;
	    if (selectedrowindex >= 0 && selectedrowindex < rowscount) {
	        //var id = $("#checkOrderDataGrid").jqxGrid('getrowid', selectedrowindex);
	        
	        var rowData = $('#checkOrderDataGrid').jqxGrid('getrowdata', selectedrowindex);
	        $.post("removeCheckOrder.action", {"checkOrder.id": rowData["id"]},function(result){
				if(result != null && result["executeResult"] != null && result["executeResult"] == true){
					var id = $("#checkOrderDataGrid").jqxGrid('getrowid', selectedrowindex);
                    var commit = $("#checkOrderDataGrid").jqxGrid('deleterow', id);
	            	
	            	if(commit != null) {
	            		
	            	}
	            }
			});
	        
	    }
	});
}


function parseCheckOrderGridHtml() {
		$.post("searchOrder.action", 
			{"order.enabled": true,"order.status":5}, 
			function(matchedcheckOrders){
	var option = {
			fallbackLng: 'en-US',
			lng: 'en-US',
	//		lng: 'zh-CN',
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
	
	initUpdateCheckOrderElements();
	addUpdateCheckOrderEventListeners();
	
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